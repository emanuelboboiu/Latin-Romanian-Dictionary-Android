package ro.limbalatina.dictionarlatinroman;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class DisplaySettingsActivity extends Activity {

	private Settings set;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_settings);

		// Some initial things like background:
		GUITools.setLayoutInitial(this, 2);

		set = new Settings(this);

		// Check the radio button depending of the size already chosen:
		String rb = "rbRadio" + MainActivity.textSize;
		int resID = getResources().getIdentifier(rb, "id", getPackageName());
		RadioButton radioButton = (RadioButton) findViewById(resID);
		radioButton.setChecked(true);

	} // end onCreate method.

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked:
		switch (view.getId()) {
		case R.id.rbRadio14:
			if (checked) {
				MainActivity.textSize = 14;
			}
			break;
		case R.id.rbRadio16:
			if (checked) {
				MainActivity.textSize = 16;
			}
			break;
		case R.id.rbRadio18:
			if (checked) {
				MainActivity.textSize = 18;
			}
			break;
		case R.id.rbRadio20:
			if (checked) {
				MainActivity.textSize = 20;
			}
			break;
		case R.id.rbRadio22:
			if (checked) {
				MainActivity.textSize = 22;
			}
			break;
		case R.id.rbRadio24:
			if (checked) {
				MainActivity.textSize = 24;
			}
			break;
		case R.id.rbRadio26:
			if (checked) {
				MainActivity.textSize = 26;
			}
			break;
		case R.id.rbRadio28:
			if (checked) {
				MainActivity.textSize = 28;
			}
			break;
		case R.id.rbRadio30:
			if (checked) {
				MainActivity.textSize = 30;
			}
			break;
		case R.id.rbRadio32:
			if (checked) {
				MainActivity.textSize = 32;
			}
			break;
		case R.id.rbRadio34:
			if (checked) {
				MainActivity.textSize = 34;
			}
			break;
		case R.id.rbRadio36:
			if (checked) {
				MainActivity.textSize = 36;
			}
			break;
		} // } // end switch.

		// Save now the setting:
		set.saveIntSettings("textSize", MainActivity.textSize);

		/*
		 * Now recreate the activity, this way we have a correct radio button
		 * chosen:
		 */
		recreateThisActivity();
	} // end onRadioButtonClicked.

	// A method which recreates this activity:
	private void recreateThisActivity() {
		startActivity(getIntent());
		finish();
	} // end recreateThisActivity() method.

} // end DisplaySettingsClass.
