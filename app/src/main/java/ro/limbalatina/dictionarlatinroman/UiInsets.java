package ro.limbalatina.dictionarlatinroman;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

final class UiInsets {

    private UiInsets() {
    }

    static void enableEdgeToEdge(Activity activity) {
        WindowCompat.enableEdgeToEdge(activity.getWindow());

        boolean nightMode = (activity.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        WindowCompat.getInsetsController(activity.getWindow(), activity.getWindow().getDecorView())
                .setAppearanceLightStatusBars(nightMode);
    }

    static void applySystemBarInsets(Activity activity) {
        View root = activity.findViewById(R.id.rootLayout);
        View statusBarBackground = activity.findViewById(R.id.statusBarBackground);

        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.displayCutout());
            ViewGroup.LayoutParams statusBarLayout = statusBarBackground.getLayoutParams();
            if (statusBarLayout.height != bars.top) {
                statusBarLayout.height = bars.top;
                statusBarBackground.setLayoutParams(statusBarLayout);
            }
            view.setPadding(bars.left, 0, bars.right, bars.bottom);
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(root);
    }
}
