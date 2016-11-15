package com.r21nomi.blobtransition;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by Ryota Niinomi on 2016/11/13.
 */

public class ViewUtil {
    public static int[] getViewLocation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    public static void transform(View view, int width, int height) {
        ViewGroup.LayoutParams param = view.getLayoutParams();
        param.width = width;
        param.height = height;
        view.setLayoutParams(param);
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }
}
