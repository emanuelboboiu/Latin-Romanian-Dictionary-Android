package ro.limbalatina.dictionarlatinroman;

import android.content.Context;

public class StringTools {

    private final Context context;

    // A constructor:
    public StringTools(Context context) {
        this.context = context;
    }// end constructor for context.

    // Escape LIKE wildcards so the search treats them as typed characters.
    public String escapeLikePattern(String str) {
        return str.replace("!", "!!").replace("%", "!%").replace("_", "!_");
    }

    public void doNothing() {
        String msg = context.getString(R.string.about);
        msg.toString();
    } // end doNothing() method to be removed.

} // end StringTools class.
