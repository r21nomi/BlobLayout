package com.r21nomi.blobtransition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
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

public class BlobLayout extends FrameLayout {

    private float offsetX = 0;
    private float offsetY = 0;
    private float velocity = 0;
    private float angleStep;
    private ValueAnimator valueAnimator;
    private boolean initialized;
    private List<Point> points = new ArrayList<>();
    private State currentState = State.NONE;
    private float noise;

    private enum State {
        RECT,
        CIRCLE,
        NONE
    }

    public BlobLayout(Context context) {
        this(context, null);
    }

    public BlobLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlobLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BlobLayout);
        angleStep = typedArray.getInteger(R.styleable.BlobLayout_angleStep, 20);
        noise = typedArray.getFloat(R.styleable.BlobLayout_noise, 0);
        typedArray.recycle();
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
        Path path = new Path();

        float centerX = getMeasuredWidth() / 2;
        float centerY = getMeasuredHeight() / 2;

        canvas.translate(centerX, centerY);

        if (!initialized) {
            initShape();
            initialized = true;
        }

        switch (currentState) {
            case RECT:
                initRectangle();
                break;

            case CIRCLE:
                initCircle();
                break;
        }

        if (velocity > 0) {
            // Animating.
            int i = 0;

            for (Point point : points) {
                Coordinate coordinate = point.getCurrentPoint();
                if (i == 0) {
                    // Move to initial position first.
                    path.moveTo(coordinate.getX(), 0);
                    path.lineTo(coordinate.getX(), coordinate.getY());
                } else {
                    Coordinate lastCoordinate = points.get(i - 1).getCurrentPoint();
                    path.quadTo(
                            lastCoordinate.getX(),
                            lastCoordinate.getY(),
                            (coordinate.getX() + lastCoordinate.getX()) / 2,
                            (coordinate.getY() + lastCoordinate.getY()) / 2
                    );
                }

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
            // Not animating.
            int i = 0;

            for (Point point : points) {
                Coordinate coordinate = point.getCurrentPoint();
                if (i == 0) {
                    // Move to initial position first.
                    path.moveTo(coordinate.getX(), 0);
                    path.lineTo(coordinate.getX(), coordinate.getY());
                } else {
                    Coordinate lastCoordinate = points.get(i - 1).getCurrentPoint();
                    path.quadTo(
                            lastCoordinate.getX(),
                            lastCoordinate.getY(),
                            (coordinate.getX() + lastCoordinate.getX()) / 2,
                            (coordinate.getY() + lastCoordinate.getY()) / 2
                    );
                }

                i++;
            }
        }

        canvas.clipPath(path);
        canvas.translate(-centerX, -centerY); // Reset anchoring point.

        super.dispatchDraw(canvas);
    }

    public void animateToRect() {
        if (valueAnimator == null) {
            initAnimation();
        }
        changeState(State.RECT);
        valueAnimator.start();
    }

    public void animateToCircle() {
        if (valueAnimator == null) {
            initAnimation();
        }
        changeState(State.CIRCLE);
        valueAnimator.start();
    }

    public void toggleAnimation() {
        if (currentState == State.RECT) {
            animateToCircle();
        } else {
            animateToRect();
        }
    }

    public ValueAnimator getToRectAnimator() {
        if (valueAnimator == null) {
            initAnimation();
        }
        changeState(State.RECT);

        return valueAnimator;
    }

    public ValueAnimator getToCircleAnimator() {
        if (valueAnimator == null) {
            initAnimation();
        }
        changeState(State.CIRCLE);

        return valueAnimator;
    }

    public State getState() {
        return currentState;
    }

    public float getAngleStep() {
        return angleStep;
    }

    public void setAngleStep(float angleStep) {
        this.angleStep = angleStep;
    }

    public float getNoise() {
        return noise;
    }

    public void setNoise(float noise) {
        this.noise = noise;
    }

    private void changeState(State state) {
        currentState = state;
    }

    private void initShape() {
        points.clear();

        for (float angle = 0, len = 360; angle <= len; angle += angleStep) {
            points.add(new Point(getCircleCoordinate(angle), new Coordinate(0, 0)));
        }
    }

    private void initCircle() {
        float angle = 0;
        for (Point point : points) {
            point.setTargetPoint(getCircleCoordinate(angle));
            angle += angleStep;
        }
    }

    private void initRectangle() {
        int angle = 0;
        float radius = getMeasuredWidth() / 2;

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

            angle += angleStep;
        }
    }

    private Coordinate getCircleCoordinate(float angle) {
        float radius = getMeasuredWidth() / 2;
        float radian = (float) Math.toRadians(angle);
        float ex = radius * (float) Math.cos(radian);
        float ey = radius * (float) Math.sin(radian);
        float nr = noise == 0 ? radius : radius - MathUtil.map(MathUtil.noise(ex * 0.01f + offsetX, ey * 0.01f + offsetY), 0, 1, 1, radius * noise);
        float nx = nr * (float) Math.cos(radian);
        float ny = nr * (float) Math.sin(radian);

        return new Coordinate(nx, ny);
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
                // Set current coordinate as initial value.
                for (Point point : points) {
                    Coordinate currentCoordinate = point.getCurrentPoint();
                    point.setInitialPoint(new Coordinate(currentCoordinate.getX(), currentCoordinate.getY()));
                }
            }
        });
    }
}
