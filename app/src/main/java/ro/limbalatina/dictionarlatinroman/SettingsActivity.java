package ro.limbalatina.dictionarlatinroman;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import android.os.Bundle;

import android.view.View;
import android.widget.CheckBox;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Find the toolbar and set it as the app's action bar
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);

        // Get a reference to the action bar
        ActionBar actionBar = getSupportActionBar();

        // Enable the back button (the "up" button)
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Some initial things like background, not anymore, we used now the day night theme.:
        // GUITools.setLayoutInitial(this, 2);

        // Check or check the check boxes, depending of current boolean values:

        // For sounds in program:
        CheckBox cbtSoundsSetting = findViewById(R.id.cbtSoundsSetting);
        cbtSoundsSetting.setChecked(MainActivity.isSound);

        /*
         * Not necessary yet. //For speech settings: CheckBox cbtSpeechSetting =
         * (CheckBox) findViewById(R.id.cbtSpeechSetting);
         * cbtSpeechSetting.setChecked(MainActivity.isSpeech);
         */

        // For shake:
        CheckBox cbtOnshakeSetting = findViewById(R.id.cbtOnshakeSetting);
        cbtOnshakeSetting.setChecked(MainActivity.isShake);

        // For keeping screen awake:
        CheckBox cbtScreenAwakeSetting = findViewById(R.id.cbtScreenAwakeSetting);
        cbtScreenAwakeSetting.setChecked(MainActivity.isSpeech);

        // For IME DONE button of the keyboard:
        CheckBox cbtImeSetting = findViewById(R.id.cbtImeSetting);
        cbtImeSetting.setChecked(MainActivity.isImeAction);
    } // end onCreate.


    // Handle the back button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // The user clicked the back arrow
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    } // end onOptionsItemSelected() method.

    // Let's see what happens when a check box is clicked in audio settings:
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        Settings set = new Settings(this); // to save changes.

        // Check which check box was clicked
        int viewId = view.getId();
        if (viewId == R.id.cbtSoundsSetting) {
            MainActivity.isSound = checked;
            set.saveBooleanSettings("isSound", MainActivity.isSound);
            /*
             * Not necessary yet. else if (viewId == R.id.cbtSpeechSetting) {
             * MainActivity.isSpeech = checked;
             * set.saveBooleanSettings("isSpeech", MainActivity.isSpeech);
             */
        } else if (viewId == R.id.cbtOnshakeSetting) {
            MainActivity.isShake = checked;
            set.saveBooleanSettings("isShake", MainActivity.isShake);
        } else if (viewId == R.id.cbtScreenAwakeSetting) {
            MainActivity.isWakeLock = checked;
            set.saveBooleanSettings("isWakeLock", MainActivity.isWakeLock);
        } else if (viewId == R.id.cbtImeSetting) {
            MainActivity.isImeAction = checked;
            set.saveBooleanSettings("isImeAction", MainActivity.isImeAction);
        }
    } // end onClick method.

} // end settings activity class.
