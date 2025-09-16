package ro.limbalatina.dictionarlatinroman;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RadioButton;

public class DisplaySettingsActivity extends AppCompatActivity {

    private Settings set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_settings);


        // Find the toolbar and set it as the app's action bar
        Toolbar toolbar = findViewById(R.id.toolbar_display_settings);
        setSupportActionBar(toolbar);

        // Get a reference to the action bar
        ActionBar actionBar = getSupportActionBar();

        // Enable the back button (the "up" button)
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Some initial things like background, removed to use the day night theme:
        // GUITools.setLayoutInitial(this, 2);

        set = new Settings(this);

        // Check the radio button depending of the size already chosen:
        String rb = "rbRadio" + MainActivity.textSize;
        @SuppressLint("DiscouragedApi") int resID = getResources().getIdentifier(rb, "id", getPackageName());
        RadioButton radioButton = findViewById(resID);
        radioButton.setChecked(true);

    } // end onCreate method.


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

    // The method for cchoosing the size of the font:
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked:
        int viewId = view.getId();
        if (viewId == R.id.rbRadio14) {
            if (checked) {
                MainActivity.textSize = 14;
            }
        } else if (viewId == R.id.rbRadio16) {
            if (checked) {
                MainActivity.textSize = 16;
            }
        } else if (viewId == R.id.rbRadio18) {
            if (checked) {
                MainActivity.textSize = 18;
            }
        } else if (viewId == R.id.rbRadio20) {
            if (checked) {
                MainActivity.textSize = 20;
            }
        } else if (viewId == R.id.rbRadio22) {
            if (checked) {
                MainActivity.textSize = 22;
            }
        } else if (viewId == R.id.rbRadio24) {
            if (checked) {
                MainActivity.textSize = 24;
            }
        } else if (viewId == R.id.rbRadio26) {
            if (checked) {
                MainActivity.textSize = 26;
            }
        } else if (viewId == R.id.rbRadio28) {
            if (checked) {
                MainActivity.textSize = 28;
            }
        } else if (viewId == R.id.rbRadio30) {
            if (checked) {
                MainActivity.textSize = 30;
            }
        } else if (viewId == R.id.rbRadio32) {
            if (checked) {
                MainActivity.textSize = 32;
            }
        } else if (viewId == R.id.rbRadio34) {
            if (checked) {
                MainActivity.textSize = 34;
            }
        } else if (viewId == R.id.rbRadio36) {
            if (checked) {
                MainActivity.textSize = 36;
            }
        }

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
