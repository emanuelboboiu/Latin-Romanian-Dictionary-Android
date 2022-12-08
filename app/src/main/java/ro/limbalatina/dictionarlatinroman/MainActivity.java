package ro.limbalatina.dictionarlatinroman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Class started on Sunday, 31 May 2015, created by Emanuel Boboiu.
 * This is the main class of this application.
 * */

public class MainActivity extends Activity {

    // The following fields are used for the shake detection:
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    // End fields declaration for shake detector.

    public final static String EXTRA_MESSAGE = "ro.limbalatina.dictionarlatinroman.MESSAGE";

    private DBAdapter mDbHelper;
    public static boolean isSpeech = false;
    public static boolean isSound = true;
    public static boolean isShake = true;
    public static float onshakeMagnitude = 2.5F;
    public static int direction = 0; // 0 is Latin Romanian, 1 Romanian Latin.
    public static boolean isWakeLock = false;
    public static boolean isImeAction = true;
    public static int textSize = 20; // for TextViews.
    public static String background = null;
    public static int resultsLimit = 100;
    private int mPaddingDP = 3; // for padding at text views of the results.
    public static int numberOfLaunches = 0;

    private String[] aDirection; // for text above edit.
    private StringTools st;

    final Context mFinalContext = this;

    // Controls used globally in the application:
    private LinearLayout llResults = null; // for central part of the activity.
    private LinearLayout llBottomInfo = null; // we attribute to it in onCreate.

    // Added in version 3.1, for update dictionary from the server:
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Charge settings:
        Settings set = new Settings(this);
        set.chargeSettings();

        mQueue = Volley.newRequestQueue(this);

        // Calculate the pixels in DP for mPaddingDP, for TextViews of the
        // results:
        int paddingPixel = 3;
        float density = getResources().getDisplayMetrics().density;
        mPaddingDP = (int) (paddingPixel * density);
        // end calculate mPaddingDP

        // To keep screen awake:
        if (MainActivity.isWakeLock) {
            getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } // end wake lock.

        // Start things for our database:
        mDbHelper = new DBAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        Resources res = getResources();
        aDirection = res.getStringArray(R.array.direction_array);

        // Find the llResults and llBottomInfo:
        llResults = findViewById(R.id.llResults);
        llBottomInfo = findViewById(R.id.llBottomInfo);

        // Initialise the string tools object:
        st = new StringTools(this);

        // Other things at onCreate:
        // a method found in this class.
        updateGUIFirst();

        // Some initial things like background:
        GUITools.setLayoutInitial(this, 1);
        GUITools.checkIfRated(this);

        // ShakeDetector initialisation:
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setShakeThresholdGravity(MainActivity.onshakeMagnitude);
        /*
         * Method you would use to setup whatever you want done once the
         * device has been shook.
         */
        mShakeDetector.setOnShakeListener(this::handleShakeEvent);
        // End initialisation of the shake detector.
    } // end onCreate() method.

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.isShake) {
            // Add the following line to register the Session Manager Listener
            // onResume:
            mSensorManager.registerListener(mShakeDetector, mAccelerometer,
                    SensorManager.SENSOR_DELAY_UI);
        }
    } // end onResume method.

    @Override
    public void onPause() {
        // What we want to happens on pause:
        if (MainActivity.isShake) {
            // Add the following line to unregister the Sensor Manager onPause:
            mSensorManager.unregisterListener(mShakeDetector);
        }
        super.onPause();
    } // end onPause method.

    @Override
    public void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    } // end onDestroy method.

    @Override
    public void onBackPressed() {
        this.finish();
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    } // end onBackPressed()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    } // end onCreateOptionsMenu() method.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.mnuActionSettings) {
            goToSettings();
        } else if (id == R.id.mnuBackgroundSettings) {
            goToBackgroundSettings();
        } else if (id == R.id.mnuDisplaySettings) {
            goToDisplaySettings();
        } else if (id == R.id.mnuInformation) {
            showInformation();
        } else if (id == R.id.mnuRate) {
            GUITools.showRateDialog(this);
            // end if rate option was chosen in menu.
        } else if (id == R.id.mnuUpdate) {
            requestJSON();
            // end if update option was chosen in menu.
        } else if (id == R.id.mnuResetDefaults) {
            // Get the strings to make an alert:
            String tempTitle = getString(R.string.title_default_settings);
            String tempBody = getString(R.string.body_default_settings);
            new AlertDialog.Builder(this)
                    .setTitle(tempTitle)
                    .setMessage(tempBody)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes,
                            (dialog, whichButton) -> {
                                Settings set = new Settings(mFinalContext);
                                set.setDefaultSettings();
                                set.chargeSettings();
                            }).setNegativeButton(android.R.string.no, null)
                    .show();
        } // end if is for set to defaults clicked in main menu.
        else if (id == R.id.mnuAboutDialog) {
            GUITools.aboutDialog(this);
        } // end if about game is chosen in main menu.
        else if (id == R.id.mnuOnlineDictionary) {
            GUITools.openBrowser(this, "http://www.dictionar.limbalatina.ro");
        } // end if the web version is chosen in menu.

        return super.onOptionsItemSelected(item);
    }// end onOptionsItemSelected() method.

    // Some methods to go to other activities from menu:
    private void goToSettings() {
        // Called when the user clicks the settings option in menu:
        Intent intent = new Intent(this, SettingsActivity.class);
        String message;
        message = "Latin Dictionary"; // without a reason, just to be something
        // sent by the intent.
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    } // end go to settings method.

    private void goToBackgroundSettings() {
        // Called when the user clicks the background settings option in menu:
        Intent intent = new Intent(this, BackgroundActivity.class);
        String message;
        message = "Latin Dictionary"; // without a reason.
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    } // end go to background settings method.

    private void goToDisplaySettings() {
        // Called when the user clicks the display settings option in menu:
        Intent intent = new Intent(this, DisplaySettingsActivity.class);
        String message;
        message = "Latin Dictionary"; // without a reason, just to be something
        // sent by the intent.
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    } // end go to display settings method.
    // End methods to go in other activities from menu.

    // A method for switch button:
    public void switchButton(View view) {
        switchDirectionAction();
    } // end switchButton() method.

    // The method which makes the switch:
    private void switchDirectionAction() {
        SoundPlayer.playSimple(this, "switch_direction");
        if (direction == 1) {
            // It means it is Romanian Latin, it will be Latin Romanian:
            direction = 0;
        } else {
            direction = 1;
        }
        updateGUIFirst();
        // Save the new direction in SharedPreferences:
        Settings set = new Settings(this);
        set.saveIntSettings("direction", direction);
        cancelSearchActions(0);
    } // end switchDirectionAction() method.

    // A method which is called when shaking the device:
    private void handleShakeEvent(int count) {
        cancelSearchActions(1);
    } // end method for actions on shake.

    // A method to update some text views or other GUI elements at onCreate():
    private void updateGUIFirst() {
        // To have correct the direction as message above search edit:
        updateSearchMessage();

        // The number of words in DB:
        String sql = "SELECT count(id) FROM dictionar";
        Cursor cursor = mDbHelper.queryData(sql);
        int totalWords = cursor.getInt(0);
        cursor.close();

        // Update the tvStatus TextView at the bottom of the main window:
        // First take the corresponding plural resource:
        Resources res = getResources();
        String availableWords = res.getQuantityString(R.plurals.tv_available_words, totalWords, totalWords);

        TextView tv = findViewById(R.id.tvStatus);
        tv.setText(availableWords);
// Make this text view clickable, call the update method, the same as from the menu:
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestJSON();
            }
        });

        // Add an action listener for the keyboard:
        EditText input = findViewById(R.id.etWord);
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchDirectlyFromKeyboard();
            }
            return false;
        });
        // End add action listener.
    } // end updateGUIFIrst() method.

    // Methods for buttons when searching:
    public void searchButton(View view) {
        getWordFromDB();
    } // end searchButton() method.

    public void searchDirectlyFromKeyboard() {
        if (isImeAction) {
            getWordFromDB();
        }
    } // end search directly from keyboard.

    // A method to get the text filled in the search EditText:
    private String getTextFromEditText() {
        EditText input = findViewById(R.id.etWord);
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(input.getWindowToken(), 0);
        String text = input.getText().toString();
        // Check if there is something typed there:
        if (text.length() < 2) {
            // Show a warning here if written text is shorter than 2 characters:
            GUITools.alert(this, getString(R.string.warning),
                    getString(R.string.warning_wrong_search),
                    getString(R.string.bt_ok));
            SoundPlayer.playSimple(this, "results_not_available");

            return null;
        } else {
            // Post the statistics and return:
            String language = "lat";
            if (direction == 1) {
                language = "rom";
            }
            Statistics stats = new Statistics();
            stats.postStats(text, language);
            return text;
        }
    } // end getTextFromEditText() method.

    // The method to search and show a query:
    private void getWordFromDB() {

        // Get the string filled in the EditText:

        String word = getTextFromEditText();

        // Only if there is something typed in the EditText:
        if (word != null) {
            /*
             * Polish the string if there are one apostrophe or quote or other
             * special characters:
             */
            word = st.escapeString(word);

            // First sear for direction 0, Latin Romanian:
            Cursor cursor;
            if (direction == 0) {
                cursor = mDbHelper
                        .queryData("SELECT * from dictionar WHERE termen LIKE '"
                                + word + "%' ORDER BY termen COLLATE NOCASE");
            } else {
                cursor = mDbHelper
                        .queryData("SELECT * from dictionar WHERE explicatie LIKE '%"
                                + word + "%' ORDER BY termen COLLATE NOCASE");

            } // end if is Romanian Latin direction.

            // Only if there are results:
            int count = cursor.getCount();
            if (count > 0) {

                // Play a specific sound for results shown:
                SoundPlayer.playSimple(this, "results_shown");

                // Hide the llBottomInfo layout:
                llBottomInfo.setVisibility(View.GONE);

                // Clear the previous content of the llResult layout:
                LinearLayout ll = findViewById(R.id.llResults);
                ll.removeAllViews();

                // Create a text view for title, announcing the number of
                // results:
                // First take the corresponding plural resource:
                Resources res = getResources();
                String foundResults = res.getQuantityString(
                        R.plurals.tv_number_of_results, count, count);
                // Create the number of results text view:
                TextView tv = new TextView(this);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize + 1);
                tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                tv.setPadding(mPaddingDP, mPaddingDP, mPaddingDP, mPaddingDP);
                tv.setText(foundResults);
                ll.addView(tv);

                // Create TextViews for each word:
                // For limit, we have a variable which will be incremented until
                // resultsLimit:
                int it = 0;
                cursor.moveToFirst();
                do {
                    tv = new TextView(this);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                    // tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    tv.setPadding(mPaddingDP, mPaddingDP, mPaddingDP,
                            mPaddingDP);
                    // w means word, e means explanation:
                    final String w = cursor.getString(1);
                    final String e = cursor.getString(2);
                    String tvText = String.format(
                            getString(R.string.tv_word_and_explanation), w, e);
                    CharSequence tvSeq = MyHtml.fromHtml(tvText);
                    tv.setText(tvSeq);

                    // For a short click, show paradigm of current Latin word:
                    tv.setOnClickListener(view -> {
                        Paradigm p = new Paradigm(mFinalContext, w, e);
                        p.makeParadigm();
                    });
                    // End add listener for short click on a result.

                    // For a long click, show part of speech:
                    tv.setOnLongClickListener(view -> {
                        Paradigm p = new Paradigm(mFinalContext, w, e);
                        p.showPartOfSpeech();
                        return true;
                    });
                    // End add listener for long click on a result.

                    ll.addView(tv);

                    it++;
                    if (it >= resultsLimit) {
                        break;
                    }
                } while (cursor.moveToNext());
                // end do ... while.
            } // end if there were results in cursor.

            // If there are no results, getCount is 0:
            else {
                showWhenNoResults(word);
            } // end if there were no results.
        } // end if there was something typed in the EditText.

    } // end getWordFromDB() method.

    // A method to cancel a search:
    public void cancelSearchButton(View view) {
        cancelSearchActions(1);
    } // end cancelButton method.

    /*
     * When type is 1 it means pressing GOLIRE button, if 0 it means it is the
     * switch button pressed:
     */
    private void cancelSearchActions(int where) {
        // Find the edit text to erase all content:
        EditText et = findViewById(R.id.etWord);
        et.setText("");

        // Show the keyboard for a new search:
        if (where == 1) {
            SoundPlayer.playSimple(this, "results_canceled");
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        } // end if is from cancelButton or shake action.

        // Erase also the llResults layout:
        llResults.removeAllViews();

        // Show again the llBottomInfo layout:
        llBottomInfo.setVisibility(View.VISIBLE);
    } // end cancelSearchActions method.

    // A method to write in the results area that there are no results:
    private void showWhenNoResults(String searchedWord) {

        // Play a corresponding sound if results are not available:
        SoundPlayer.playSimple(this, "results_not_available");

        // Clear the previous content of the llResult layout:
        LinearLayout ll = findViewById(R.id.llResults);
        ll.removeAllViews();

        // Create a TextView for message no results:
        TextView tv = new TextView(this);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        tv.setPadding(mPaddingDP, mPaddingDP, mPaddingDP, mPaddingDP);
        String tvText = String.format(getString(R.string.warning_not_results),
                searchedWord);
        CharSequence tvSeq = MyHtml.fromHtml(tvText);
        tv.setText(tvSeq);
        ll.addView(tv);
    } // end showWhenNoResults method.

    // A method to show information in an alert LinearLayout:
    private void showInformation() {

        // Create a LinearLayout with ScrollView with all contents as TextViews:
        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(mPaddingDP, mPaddingDP, mPaddingDP, mPaddingDP);
        // Get the items to be shown in alert:
        Resources res = getResources();
        String[] aInformation = res.getStringArray(R.array.information_array);

        // A for for each message in the history as TextView:
        for (String s : aInformation) {
            TextView tv = new TextView(this);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            tv.setPadding(mPaddingDP, mPaddingDP, mPaddingDP, mPaddingDP);
            tv.setText(s);
            ll.addView(tv);
        } // end for.
        sv.addView(ll);

        // Create now the alert:
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.information_alert_title));
        alertDialog.setView(sv);
        alertDialog.setPositiveButton(getString(R.string.bt_close), null);
        AlertDialog alert = alertDialog.create();
        alert.show();
    } // end showInformation() method.

    // The method which updates the text view for search message:
    private void updateSearchMessage() {
        EditText et = findViewById(R.id.etWord);
        et.setHint(aDirection[direction]);

        // Set also the image with flags for switch button:
        String flagFileName = "flag" + direction;
        ImageButton ib = findViewById(R.id.btSwitch);
        String uri = "@drawable/" + flagFileName;
        @SuppressLint("DiscouragedApi") int imageResource = getResources().getIdentifier(uri, null,
                getPackageName());
        ib.setImageResource(imageResource);

        // cancelSearchActions(0);
    } // end updateSearchMessage() method.

    // Added in 3.1,, first version with  update of the database:
    private void requestJSON() {
        // First of all we have to detect the maximum id of a word in our local database, the max id:
        String sql = "SELECT max(id) FROM dictionar;";
        Cursor cursor = mDbHelper.queryData(sql);
        String maxId = cursor.getString(0);
        cursor.close();

        String URLstring = "https://www.limbalatina.ro/android/api.php?act=updateDictionary&maxIdInPhone=" + maxId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // GUITools.alert(MainActivity.this, "In loc de log", "" + response, "Inchidere");
                        // Log.d("strrrrr", ">>" + response);

                        try {
                            // Getting the whole json object from the response:
                            JSONObject obj = new JSONObject(response);

                            // We create an array list of WodrModels:
                            ArrayList<WordModel> wordModelArrayList = new ArrayList<>();

                            // We convert into an json of type array:
                            JSONArray dataArray = obj.getJSONArray("package");

                            // Now through a for we fill the array list with models of type WordModel:
                            for (int i = 0; i < dataArray.length(); i++) {
                                WordModel wordModel = new WordModel();
                                JSONObject dataobj = dataArray.getJSONObject(i);
                                wordModel.setId(dataobj.getString("id"));
                                wordModel.setWord(dataobj.getString("cuvant"));
                                wordModel.setExplanation(dataobj.getString("expl"));
                                wordModel.setDate(dataobj.getString("datains"));
                                wordModelArrayList.add(wordModel);
                            } // end for.

                            // Now we have the array list of WordModels, we can use it in another method to update effectivelly:
                            updateDBEffectivelly(wordModelArrayList);

                        } catch (JSONException e) {
                            // e.printStackTrace();
                            GUITools.showUnknownErrorAlert(MainActivity.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Displaying an error if it occurrs
                        GUITools.showUnknownErrorAlert(MainActivity.this);
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    } // end requestJSON() method.

    private void updateDBEffectivelly(ArrayList<WordModel> wordModelArrayList) {

        // If the array list has at least one entry:
        int total = wordModelArrayList.size();
        if (total > 0) {
            int totalInsertions = 0;
            for (int i = 0; i < wordModelArrayList.size(); i++) {
                // Create the SQL for insert into database:
                String sql = "insert into dictionar (id, termen, explicatie, data) values ('" + wordModelArrayList.get(i).getId() + "', '" + wordModelArrayList.get(i).getWord() + "', '" + wordModelArrayList.get(i).getExplanation() + "', '" + wordModelArrayList.get(i).getDate() + "');";
                mDbHelper.insertData(sql);
                totalInsertions++; // we increment the number of added words.
                // GUITools.alert(MainActivity.this, "Un SQL demonstrativ", sql, "OK!");
            } // end for.

            // We anounce the number of added words with plural resource:
            Resources res = getResources();
            String addedWords = res.getQuantityString(R.plurals.tv_added_words, totalInsertions, totalInsertions);
            String updateMessage = String.format(getString(R.string.succes_update), addedWords);
            GUITools.alert(MainActivity.this, MainActivity.this.getString(R.string.info_title), updateMessage, MainActivity.this.getString(R.string.bt_ok));
            updateGUIFirst();
        } else { // no entries for update:
            GUITools.alert(MainActivity.this, MainActivity.this.getString(R.string.info_title), MainActivity.this.getString(R.string.no_new_words), MainActivity.this.getString(R.string.bt_ok));
        } // en dif not new words for update.
    } // end updateDBEffectivelly() method.

} // end MainActivity class.
