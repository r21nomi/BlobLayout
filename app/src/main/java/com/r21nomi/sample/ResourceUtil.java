package com.r21nomi.sample;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;

import java.util.Locale;

/**
 * Created by r21nomi on 2016/12/11.
 */

public class ResourceUtil {

    public static Uri getDrawableAsUri(Context context, @DrawableRes int res) {
        return Uri.parse(String.format(Locale.US,
                "android.resource://%s/%d",
                context.getPackageName(),
                res
        ));
    }
}
