package ro.limbalatina.dictionarlatinroman;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BackgroundActivity extends Activity {

    private int curBackgroundNumber = 0;
    private final int BACKGROUNDS_AVAILABLE = 5;
    private String tempBackground = null;
    private LinearLayout llLayoutMain;
    private Button btSaveBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
    } // end onCreate() method.

    @Override
    protected void onResume() {
        super.onResume();

        GUITools.setLayoutInitial(this, 2);

        // Charge the main LinearLayout:
        llLayoutMain = findViewById(R.id.layoutMain);

        btSaveBackground = findViewById(R.id.btBackgroundSave);

        // Determine current background number:
        Settings set = new Settings(this);
        String curBackground = set.getStringSettings("background");
        curBackgroundNumber = Integer.parseInt(curBackground.substring(5));
        setCurBackgroundTV(curBackgroundNumber);
    } // end onResume() method.

    // A method to set current background text:
    private void setCurBackgroundTV(int nr) {
        TextView tv = findViewById(R.id.tvCurrentBackground);
        if (nr > 0) {
            tv.setText(String.format(getString(R.string.tv_current_background),
                    "" + nr));
        } else {
            tv.setText(R.string.tv_without_background);
        }
    } // end setCurBackgroundTV() method.

    // Method for buttons in this activity:
    public void previousBackgroundButton(View view) {
        previousBackgroundButtonActions();
    }

    private void previousBackgroundButtonActions() {
        // Decrement the number of the background:
        curBackgroundNumber--;
        if (curBackgroundNumber < 0) {
            curBackgroundNumber = BACKGROUNDS_AVAILABLE;
        }
        changeBackgroundTemporary(curBackgroundNumber);
    } // end previousBackgroundButtonActions() method.

    public void nextBackgroundButton(View view) {
        nextBackgroundButtonActions();
    }

    private void nextBackgroundButtonActions() {
        // Increment the number of the background:
        curBackgroundNumber++;
        if (curBackgroundNumber > BACKGROUNDS_AVAILABLE) {
            curBackgroundNumber = 0;
        }
        changeBackgroundTemporary(curBackgroundNumber);
    } // end nextBackgroundButtonActions() method.

    public void saveBackgroundButton(View view) {
        saveBackgroundButtonActions();
    }

    private void saveBackgroundButtonActions() {
        if (tempBackground != null) {
            MainActivity.background = tempBackground;
            Settings set = new Settings(this);
            set.saveStringSettings("background", MainActivity.background);
            SoundPlayer.playSimple(this, "element_finished");

            // Go now into main windows, dictionary itself:
            GUITools.goToDictionary(this);
            this.finish();
        } // end if the background was changed.
    } // end saveBackgroundButtonActions() method.
    // end zone methods for buttons in this activity.

    // A method to change the background:
    private void changeBackgroundTemporary(int nr) {

        tempBackground = "paper" + nr;
        // Determine the background ID if is not 0:
        if (tempBackground.equals("paper0")) {
            llLayoutMain.setBackgroundResource(0);
        } else {
            @SuppressLint("DiscouragedApi") int resId = getResources().getIdentifier(tempBackground,
                    "drawable", getPackageName());
            llLayoutMain.setBackgroundResource(resId);
        }

        setCurBackgroundTV(nr);
        btSaveBackground.setEnabled(true);
        SoundPlayer.playSimple(this, "switch_direction");
    } // end changeBackgroundTemporary() method.

} // end BackgroundActivity class.
