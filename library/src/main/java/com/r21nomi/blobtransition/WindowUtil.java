package com.r21nomi.blobtransition;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Ryota Niinomi on 2016/11/15.
 */

class WindowUtil {
    static int getWidth(final Context context) {
        final Display display = getDisplay(context);
        final android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        return size.x;
    }

    static int getHeight(final Context context) {
        final Display display = getDisplay(context);
        final android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        return size.y;
    }

    static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    private static Display getDisplay(final Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }
}
