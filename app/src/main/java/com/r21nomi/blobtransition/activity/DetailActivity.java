package com.r21nomi.blobtransition.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.r21nomi.blobtransition.HeightEvaluator;
import com.r21nomi.blobtransition.MaskLayout;
import com.r21nomi.blobtransition.Position;
import com.r21nomi.blobtransition.R;
import com.r21nomi.blobtransition.ViewUtil;
import com.r21nomi.blobtransition.WidthEvaluator;
import com.r21nomi.blobtransition.WindowUtil;

public class DetailActivity extends AppCompatActivity {

    private static final String POSITION = "position";

    private MaskLayout maskLayout;
    private Position position;

    public static Intent createIntent(Context context, View sharedElement) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(POSITION, new Position(sharedElement));

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        position = getIntent().getParcelableExtra(POSITION);
        maskLayout = (MaskLayout) findViewById(R.id.maskLayout);

        ViewUtil.transform(maskLayout, position.getWidth(), position.getHeight());
        maskLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                maskLayout.getViewTreeObserver().removeOnPreDrawListener(this);

                float top = ViewUtil.calcurateWithoutToolbar(DetailActivity.this, position.getTop());
                maskLayout.setTranslationX(position.getLeft());
                maskLayout.setTranslationY(top);

                startEnterAnimation();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        startExitAnimation();
    }

    private void startEnterAnimation() {
        int targetWidth = WindowUtil.getWidth(this);
        int targetHeight = targetWidth * position.getWidth() / position.getHeight();

        AnimatorSet animSet = new AnimatorSet();

        animSet.playTogether(
                maskLayout.getEnterAnimator(),
                ObjectAnimator.ofFloat(maskLayout, "translationX", 0),
                ObjectAnimator.ofFloat(maskLayout, "translationY", 0),
                ValueAnimator.ofObject(new WidthEvaluator(maskLayout), position.getWidth(), targetWidth),
                ValueAnimator.ofObject(new HeightEvaluator(maskLayout), position.getHeight(), targetHeight)
        );

        animSet.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        animSet.setDuration(500);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animSet.start();
    }

    private void startExitAnimation() {
        float top = ViewUtil.calcurateWithoutToolbar(DetailActivity.this, position.getTop());
        AnimatorSet animSet = new AnimatorSet();

        animSet.playTogether(
                maskLayout.getExitAnimator(),
                ObjectAnimator.ofFloat(maskLayout, "translationX", position.getLeft()),
                ObjectAnimator.ofFloat(maskLayout, "translationY", top),
                ValueAnimator.ofObject(new WidthEvaluator(maskLayout), maskLayout.getWidth(), position.getWidth()),
                ValueAnimator.ofObject(new HeightEvaluator(maskLayout), maskLayout.getHeight(), position.getHeight())
        );

        animSet.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
                DetailActivity.super.onBackPressed();
                overridePendingTransition(0, 0);
            }
        });
        animSet.setDuration(500);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animSet.start();
    }
}
