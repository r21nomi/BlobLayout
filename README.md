# BlobLayout
BlobLayout is custom layout like blob style.
This layout can be transformed from CIRCLE style to RECT style with animation.

## Usage
Used in XML.
```xml
<com.r21nomi.blobtransition.BlobLayout
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:noise="0.1">

    <ImageView
        android:layout_width="48dp"
        android:layout_height="48dp"
        android:src="@drawable/img" />
</com.r21nomi.blobtransition.BlobLayout>
```

Transform from CIRCLE to RECT.
```java
blobLayout.animateToRect();

```

Shared element transition.
```java
public static Intent createIntent(Context context, BlobLayout sharedElement) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(POSITION, new Position(sharedElement));

    return intent;
}

@Override
protected void onCreate(Bundle savedInstanceState) {
    ...

    position = getIntent().getParcelableExtra(POSITION);
    blobLayout = (BlobLayout) findViewById(R.id.maskLayout);

    ViewGroup.LayoutParams param = view.getLayoutParams();
    param.width = position.getWidth();
    param.height = position.getHeight();
    view.setLayoutParams(param);

    blobLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            blobLayout.getViewTreeObserver().removeOnPreDrawListener(this);

            startEnterAnimation();
            return true;
        }
    });
}

private void startEnterAnimation() {
    int targetWidth = getTargetWidth();
    int targetHeight = targetWidth * position.getWidth() / position.getHeight();

    float top = position.getTop() - getStatusBarHeight();
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
    animSet.setDuration(500);
    animSet.setInterpolator(new AccelerateDecelerateInterpolator());
    animSet.start();
}

```