package com.r21nomi.blobtransition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Created by Ryota Niinomi on 2016/11/10.
 */

public class MaskLayout extends FrameLayout {

    private float offsetX = 0;
    private float offsetY = 0;
    private float velocity = 0;
    private float radius;
    private ValueAnimator valueAnimator;
    private boolean initialized;
    private List<Point> points = new ArrayList<>();

    public MaskLayout(Context context) {
        this(context, null);
    }

    public MaskLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
//
//        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, DEFAULT_BORDER_WIDTH);
//        mBorderColor = a.getColor(R.styleable.CircleImageView_civ_border_color, DEFAULT_BORDER_COLOR);
//        mBorderOverlay = a.getBoolean(R.styleable.CircleImageView_civ_border_overlay, DEFAULT_BORDER_OVERLAY);
//        mFillColor = a.getColor(R.styleable.CircleImageView_civ_fill_color, DEFAULT_FILL_COLOR);
//
//        a.recycle();

//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startAnimation();
//            }
//        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (offsetX < 1) {
            offsetX = (float) Math.random() * getMeasuredWidth();
        }
        if (offsetY < 1) {
            offsetY = (float) Math.random() * getMeasuredHeight();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 0, 0, 0));

        Path path = new Path();

        float centerX = getMeasuredWidth() / 2;
        float centerY = getMeasuredHeight() / 2;

        canvas.translate(centerX, centerY);

        if (!initialized) {
            initShape();
            initialized = true;
        }

        initTarget();

        if (velocity > 0) {
            int i = 0;

            for (Point point : points) {
                Coordinate coordinate = point.getCurrentPoint();
                if (i == 0) {
                    // Move to initial position first.
                    path.moveTo(coordinate.getX(), 0);
                }
                path.lineTo(coordinate.getX(), coordinate.getY());

                Coordinate initialLocation = point.getInitialPoint();
                Coordinate targetLocation = point.getTargetPoint();
                float distX = targetLocation.getX() - initialLocation.getX();
                float distY = targetLocation.getY() - initialLocation.getY();

                if (abs(distX) < 1 && abs(distY) < 1) {
                    point.setCurrentPoint(new Coordinate(
                            targetLocation.getX(),
                            targetLocation.getY()
                    ));
                } else {
                    point.setCurrentPoint(new Coordinate(
                            initialLocation.getX() + distX * velocity,
                            initialLocation.getY() + distY * velocity
                    ));
                }

                i++;
            }
        } else {
            for (Point point : points) {
                Coordinate coordinate = point.getCurrentPoint();
                path.lineTo(coordinate.getX(), coordinate.getY());
            }
        }

        canvas.clipPath(path);
        canvas.translate(-centerX, -centerY); // Reset anchoring point.

        super.dispatchDraw(canvas);
    }

    private void initShape() {
        float angleStep = 1;
        radius = getMeasuredWidth() / 2;

        points.clear();

        for (float angle = 0, len = 360; angle <= len; angle += angleStep) {
            float radian = (float) Math.toRadians(angle);
            float ex = radius * (float) Math.cos(radian);
            float ey = radius * (float) Math.sin(radian);
            float nr = radius - MathUtil.map(MathUtil.noise(ex * 0.01f + offsetX, ey * 0.01f + offsetY), 0, 1, 1, radius * 0.02f);
            float nx = nr * (float) Math.cos(radian);
            float ny = nr * (float) Math.sin(radian);

            points.add(new Point(new Coordinate(nx, ny), new Coordinate(0, 0)));
        }
    }

    private void initTarget() {
        int angle = 0;
        radius = getMeasuredWidth() / 2;

        for (Point point : points) {
            float targetX;
            float targetY;

            if (angle >= 0 && angle < 90) {
                targetX = radius * (float) Math.cos(Math.toRadians(0));
                targetY = radius * (float) Math.sin(Math.toRadians(90));

            } else if (angle >= 90 && angle < 180) {
                targetX = radius * (float) Math.cos(Math.toRadians(180));
                targetY = radius * (float) Math.sin(Math.toRadians(90));

            } else if (angle >= 180 && angle < 270) {
                targetX = radius * (float) Math.cos(Math.toRadians(180));
                targetY = radius * (float) Math.sin(Math.toRadians(270));

            } else {
                targetX = radius * (float) Math.cos(Math.toRadians(360));
                targetY = radius * (float) Math.sin(Math.toRadians(270));
            }

            point.setTargetPoint(new Coordinate(targetX, targetY));

            angle++;
        }
    }

    public ValueAnimator getAnimator() {
        if (valueAnimator == null) {
            initAnimation();
        }
        return valueAnimator;
    }

    private void initAnimation() {
        velocity = 0;
        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                velocity = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }
}
