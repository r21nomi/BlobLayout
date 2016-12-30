package com.r21nomi.sample;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

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

    public static float calcurateWithoutToolbar(Activity activity, float value) {
        int statusBarHeight = WindowUtil.getStatusBarHeight(activity);
        int toolbarHeight = activity.getResources().getDimensionPixelSize(R.dimen.toolbar_height);

        return value - statusBarHeight - toolbarHeight;
    }
}
