package ro.limbalatina.dictionarlatinroman;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Started on 21 June 2014, at 19:10 by Manu.
 * This class has some useful things for the GUI, like alerts.
 */

public class GUITools {

	// A method to go to dictionary activity:
	public static void goToDictionary(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	} // end go to dictionary activity.

	// A method to show an alert with title and message, just an OK button:
	public static void alert(Context context, String title, String message,
			String btClose) {

		AlertDialog.Builder alert = new AlertDialog.Builder(context);

		// The title:
		alert.setTitle(title);

		// The body creation:
		// Create a LinearLayout with ScrollView with all contents as TextViews:
		ScrollView sv = new ScrollView(context);
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);

		String[] mParagraphs = message.split("\\\n");

		// A for for each paragraph in the message as TextView:
		for (int i = 0; i < mParagraphs.length; i++) {
			TextView tv = new TextView(context);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.textSize);
			tv.setFocusable(true);
			tv.setText(mParagraphs[i]);
			ll.addView(tv);
		} // end for.

		// Add now the LinearLayout into ScrollView:
		sv.addView(ll);

		alert.setView(sv);

		alert.setPositiveButton(btClose, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Do nothing yet...
			}
		});
		alert.show();
	} // end alert static method.

	// A method for about dialog for this package:
	public static void aboutDialog(Context context) {
		// Inflate the about message contents
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View messageView = inflater.inflate(R.layout.about_dialog, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// builder.setIcon(R.drawable.app_icon);
		builder.setTitle(R.string.app_name);
		builder.setView(messageView);
		builder.setPositiveButton("OK", null);
		builder.create();
		builder.show();
	} // end about dialog.

	// A method to play a tone, just to make tests:
	public static void beep() {
		ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
		toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
	}

	// A method to give a toast, simple message on the screen:
	public static void toast(String message, int duration, Context context) {
		Toast.makeText(context, message, duration).show();
	} // end make toast.

	// A method to open the browser with an URL:
	private static final String HTTPS = "https://";
	private static final String HTTP = "http://";

	public static void openBrowser(final Context context, String url) {

		if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
			url = HTTP + url;
		}

		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(browserIntent);
		// context.startActivity(Intent.createChooser(intent, "Chose browser"));
		// // this would be with possibility to choose the browser.
	} // end start browser with an URL in it.

	// A method to open the help online:
	public static void openHelp(final Context context) {
		openBrowser(context, "http://www.dictionar.limbalatina.ro/");
	} // end open help online method.

	// A method to rate this application:
	public static void showRateDialog(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.title_rate_app))
				.setMessage(context.getString(R.string.body_rate_app))
				.setPositiveButton(context.getString(R.string.bt_rate),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Settings set = new Settings(context);
								set.saveBooleanSettings("wasRated", true);
								if (context != null) {
									String link = "market://details?id=";
									try {
										// play market available
										context.getPackageManager()
												.getPackageInfo(
														"com.android.vending",
														0);
									} catch (PackageManager.NameNotFoundException e) {
										e.printStackTrace();
										// Should use browser
										link = "https://play.google.com/store/apps/details?id=";
									}
									// Starts external action
									context.startActivity(new Intent(
											Intent.ACTION_VIEW, Uri.parse(link
													+ context.getPackageName())));
								}
							}
						})
				.setNegativeButton(context.getString(R.string.bt_not_now), null);
		builder.show();
	} // end showRateDialog() method.

	// A method which checks if was rated:
	public static void checkIfRated(Context context) {
		Settings set = new Settings(context);
		boolean wasRated = set.getBooleanSettings("wasRated");
		if (!wasRated) {

			if (MainActivity.numberOfLaunches % 6 == 0
					&& MainActivity.numberOfLaunches > 0) {
				GUITools.showRateDialog(context);
			} // end if was x launches.
		} // end if it was not rated.
	} // end checkIfRated() method.

	// A static method to get a random number between two integers:
	public static int random(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	} // end random method.

	// A method to set background and other global things about a layout:
	public static void setLayoutInitial(Context context, int layoutType) {
		String mBackground = null;
		if (MainActivity.background == null
				|| MainActivity.background.equals("")) {
			/*
			 * It means no background was chosen and saved, we choose a random
			 * one.
			 */
			/*
			 * Number of backgrounds, in the drawable folder. This number is
			 * also specified in BacgroundsActivity:
			 */
			int nrOfBackgrounds = 5;
			int curBackgroundNumber = GUITools.random(1, nrOfBackgrounds);

			mBackground = "paper" + curBackgroundNumber;
			// We save the one chosen by random:
			MainActivity.background = mBackground;
			Settings set = new Settings(context);
			set.saveStringSettings("background", MainActivity.background);
		} else {
			/* It means is was saved, we get it from the static String variable: */
			mBackground = MainActivity.background;
		} // end if a background was chosen.

		// Determine the background ID:
		int resId = 0;
		if (!MainActivity.background.equals("paper0")) {
			resId = context.getResources().getIdentifier(mBackground,
					"drawable", context.getPackageName());
		}

		/* layoutType 1 means relative, 2 means linear. */
		// if is a relative layout:
		if (layoutType == 1) {
			RelativeLayout rl = (RelativeLayout) ((Activity) context)
					.findViewById(R.id.layoutMain);
			rl.setBackgroundResource(resId);
		} // end if layoutType is RelativeLayout.
			// Now for LinearLayout:
		else if (layoutType == 2) {
			LinearLayout ll = (LinearLayout) ((Activity) context)
					.findViewById(R.id.layoutMain);
			ll.setBackgroundResource(resId);
		} // end if is a LinearLayout.
	} // end setLayoutInitial() method.

} // end GUITools class.
