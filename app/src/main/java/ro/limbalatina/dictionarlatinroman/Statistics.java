package ro.limbalatina.dictionarlatinroman;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public final class Statistics {

    private static RequestQueue requestQueue;

    private Statistics() {
    }

    public static void postStats(Context context, String word, String language) {
        Uri uri = Uri.parse("https://www.limbalatina.ro/insert_android_stats.php")
                .buildUpon()
                .appendQueryParameter("cuvant", word)
                .appendQueryParameter("limba", language)
                .build();
        StringRequest request = new StringRequest(Request.Method.GET, uri.toString(),
                response -> { }, error -> { });
        getRequestQueue(context).add(request);
    }

    private static synchronized RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
}
