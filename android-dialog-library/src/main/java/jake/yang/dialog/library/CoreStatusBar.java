package jake.yang.dialog.library;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


@SuppressWarnings({"unused", "WeakerAccess"})
public class CoreStatusBar {
    public static int getStateHeight() {
        int stateHeight = 0;
        Resources resources = CoreDialog.APPLICATION.get(0).getApplicationContext().getResources();
        int identifierState = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (identifierState > 0) {
            stateHeight = resources.getDimensionPixelSize(identifierState);
        }
        return stateHeight;
    }

    public static void theme(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.setTheme(R.style.Core_Theme_Status_AppTheme);
        }
    }

    public static void theme(Activity activity, int styleTheme) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.setTheme(styleTheme);
        }
    }

    public static int getNavigationHeight() {
        int stateHeight = 0;
        Resources resources = CoreDialog.APPLICATION.get(0).getApplicationContext().getResources();
        int identifierNavigation = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (identifierNavigation > 0) {
            stateHeight = resources.getDimensionPixelSize(identifierNavigation);
        }
        return stateHeight;
    }

    public static void statusBarColor(Activity activity, int statusColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusColor);

        } else {
            ViewGroup contentView = activity.findViewById(android.R.id.content);
            if (contentView == null) {
                return;
            }

            View statusBar = activity.findViewById(R.id.statusBarId);
            if (statusBar == null) {
                View rootView = contentView.getChildAt(0);

                if (rootView != null && rootView instanceof ViewGroup) {
                    rootView.setFitsSystemWindows(true);
                }

                View view = new View(activity);
                view.setId(R.id.statusBarId);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        getStateHeight());
                view.setLayoutParams(lp);
                view.setBackgroundColor(statusColor);

                contentView.addView(view);
            } else {
                statusBar.setBackgroundColor(statusColor);
            }
        }
    }
}
