package com.r21nomi.blobtransition;

import android.animation.IntEvaluator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ryota Niinomi on 2016/11/15.
 */

public class WidthEvaluator extends IntEvaluator {

    private View mView;

    public WidthEvaluator(View v) {
        mView = v;
    }

    @Override
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        Integer num = super.evaluate(fraction, startValue, endValue);
        ViewGroup.LayoutParams params = mView.getLayoutParams();
        params.width = num;
        mView.setLayoutParams(params);
        return num;
    }
}
