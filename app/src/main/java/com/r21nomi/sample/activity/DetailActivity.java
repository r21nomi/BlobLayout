package com.r21nomi.sample.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.r21nomi.blobtransition.BlobLayout;
import com.r21nomi.blobtransition.Position;
import com.r21nomi.sample.HeightEvaluator;
import com.r21nomi.sample.R;
import com.r21nomi.sample.ViewUtil;
import com.r21nomi.sample.WidthEvaluator;
import com.r21nomi.sample.WindowUtil;

public class DetailActivity extends AppCompatActivity {

    private static final String POSITION = "position";
    private static final String NOISE = "noise";
    private static final int DURARION = 500;

    private BlobLayout blobLayout;
    private Position position;

    public static Intent createIntent(Context context, BlobLayout sharedElement) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(POSITION, new Position(sharedElement));
        intent.putExtra(NOISE, sharedElement.getNoise());

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        position = getIntent().getParcelableExtra(POSITION);
        blobLayout = (BlobLayout) findViewById(R.id.maskLayout);
        blobLayout.setNoise(getIntent().getFloatExtra(NOISE, 0));

        ViewUtil.transform(blobLayout, position.getWidth(), position.getHeight());
        blobLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                blobLayout.getViewTreeObserver().removeOnPreDrawListener(this);

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

        float top = ViewUtil.calcurateWithoutToolbar(this, position.getTop());
        // Use setX / setY to set absolute position.
        blobLayout.setX(position.getLeft());
        blobLayout.setY(top);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(
                blobLayout.getToRectAnimator(),
                // Use translationX / translationY to translate to relative position.
                ObjectAnimator.ofFloat(blobLayout, "translationX", 0),
                ObjectAnimator.ofFloat(blobLayout, "translationY", 0),
                ValueAnimator.ofObject(new WidthEvaluator(blobLayout), position.getWidth(), targetWidth),
                ValueAnimator.ofObject(new HeightEvaluator(blobLayout), position.getHeight(), targetHeight)
        );
        animSet.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        animSet.setDuration(DURARION);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.start();
    }

    private void startExitAnimation() {
        float top = ViewUtil.calcurateWithoutToolbar(this, position.getTop());

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(
                blobLayout.getToCircleAnimator(),
                // Use x / y to set absolute position.
                ObjectAnimator.ofFloat(blobLayout, "x", position.getLeft()),
                ObjectAnimator.ofFloat(blobLayout, "y", top),
                ValueAnimator.ofObject(new WidthEvaluator(blobLayout), blobLayout.getWidth(), position.getWidth()),
                ValueAnimator.ofObject(new HeightEvaluator(blobLayout), blobLayout.getHeight(), position.getHeight())
        );
        animSet.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
                DetailActivity.super.onBackPressed();
                overridePendingTransition(0, 0);
            }
        });
        animSet.setDuration(DURARION);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.start();
    }
}
