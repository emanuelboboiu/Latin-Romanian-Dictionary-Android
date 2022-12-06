package ro.limbalatina.dictionarlatinroman;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Some initial things like background:
        GUITools.setLayoutInitial(this, 2);

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

    // Let's see what happens when a check box is clicked in audio settings:
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        Settings set = new Settings(this); // to save changes.

        // Check which check box was clicked
        switch (view.getId()) {
            case R.id.cbtSoundsSetting:
                MainActivity.isSound = checked;
                set.saveBooleanSettings("isSound", MainActivity.isSound);
                break;
            /*
             * Not necessary yet. case R.id.cbtSpeechSetting: if (checked) {
             * MainActivity.isSpeech = true; } else { MainActivity.isSpeech = false;
             * } set.saveBooleanSettings("isSpeech", MainActivity.isSpeech); break;
             */
            case R.id.cbtOnshakeSetting:
                MainActivity.isShake = checked;
                set.saveBooleanSettings("isShake", MainActivity.isShake);
                break;
            case R.id.cbtScreenAwakeSetting:
                MainActivity.isWakeLock = checked;
                set.saveBooleanSettings("isWakeLock", MainActivity.isWakeLock);
                break;
            case R.id.cbtImeSetting:
                MainActivity.isImeAction = checked;
                set.saveBooleanSettings("isImeAction", MainActivity.isImeAction);
                break;
        } // end switch.
    } // end onClick method.

} // end settings activity class.
