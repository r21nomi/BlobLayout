package com.r21nomi.sample;

import android.animation.IntEvaluator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ryota Niinomi on 2016/11/15.
 */

public class HeightEvaluator extends IntEvaluator {

    private View mView;

    public HeightEvaluator(View v) {
        mView = v;
    }

    @Override
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        Integer num = super.evaluate(fraction, startValue, endValue);
        ViewGroup.LayoutParams params = mView.getLayoutParams();
        params.height = num;
        mView.setLayoutParams(params);
        return num;
    }
}
