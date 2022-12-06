package ro.limbalatina.dictionarlatinroman;

import android.content.Context;

public class StringTools {

    private final Context context;

    // A constructor:
    public StringTools(Context context) {
        this.context = context;
    }// end constructor for context.

    // A method to polish a string:
    public String escapeString(String str) {
        return str.replaceAll("'", "''");
    } // end polishString() method.

    public void doNothing() {
        String msg = context.getString(R.string.about);
        msg.toString();
    } // end doNothing() method to be removed.

} // end StringTools class.
