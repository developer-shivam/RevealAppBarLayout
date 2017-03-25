package developer.shivam.revealappbarlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class RevealAppBarLayout extends AppBarLayout {

    /* Radius of reveal effect */
    private float revealRadius = 0;

    /* width of view */
    private int width = 0;

    /* height of view */
    private int height = 0;

    /* color of reveal circle */
    int revealColor;

    Paint revealPaint;

    /* touchX-coordinate of touch */
    int touchX = 0;

    /* touchY-coordinate of touch */
    int touchY = 0;

    public RevealAppBarLayout(Context context) {
        super(context);
        init();
    }

    public RevealAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        //To make onDraw() show its magic
        setWillNotDraw(false);

        revealColor = Color.RED;

        revealPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        revealPaint.setColor(revealColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(touchX, touchY, revealRadius, revealPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                touchX = (int) event.getX();
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    private void calculateRadius() {

        float radius = 0;

        if (touchX < width / 2) {
            float x = width - touchX;
            if (touchY < height / 2) {
                float y = height - touchY;
                radius = (float) Math.hypot(x, y);
            } else {
                float y = touchY;
                radius = (float) Math.hypot(x, y);
            }
        } else {
            float x = touchX;
            if (touchY < height / 2) {
                float y = height - touchY;
                radius = (float) Math.hypot(x, y);
            } else {
                float y = touchY;
                radius = (float) Math.hypot(x, y);
            }
        }

        makeCircularRevealAnimation(radius);
    }

    private void makeCircularRevealAnimation(float finalRadius) {
        ValueAnimator revealAnimator = ValueAnimator.ofFloat(0f, finalRadius);
        revealAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                revealRadius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        int ANIMATION_DURATION = 250;
        revealAnimator.setDuration(ANIMATION_DURATION);
        revealAnimator.start();
    }

    public void makeCircularRevealAnimation(TabLayout tabLayout) {

        touchY = (tabLayout.getTop() + (tabLayout.getTop() + tabLayout.getHeight())) / 2;
        makeCircularRevealAnimation(tabLayout.getWidth());
    }
}
