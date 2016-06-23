package com.hugboga.custom.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.hugboga.custom.utils.UIUtils;

public class RevealBackgroundView extends LinearLayout {
    public static final int STATE_NOT_STARTED = 0;
    public static final int STATE_FILL_STARTED = 1;
    public static final int STATE_FINISHED = 2;

    private static final Interpolator INTERPOLATOR = new AccelerateInterpolator();
    private static final int FILL_TIME = 200;

    private int state = STATE_NOT_STARTED;

    private Paint fillPaint;
    private int currentRadius;
    ObjectAnimator revealAnimator;

    private int startLocationX;
    private int startLocationY;

    private RectF rect;
    private int radius;


    private OnStateChangeListener onStateChangeListener;

    public RevealBackgroundView(Context context) {
        super(context);
        init();
    }

    public RevealBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RevealBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RevealBackgroundView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.WHITE);
        rect = new RectF();
        rect.left = 0;
        rect.top = 0;
        radius = UIUtils.dip2px(34) / 2;
    }

    public void setFillPaintColor(int color) {
        fillPaint.setColor(color);
    }

    public void startFromLocation(int[] tapLocationOnScreen) {
        changeState(STATE_FILL_STARTED);
        startLocationX = tapLocationOnScreen[0];
        startLocationY = tapLocationOnScreen[1];
        revealAnimator = ObjectAnimator.ofInt(this, "currentRadius", 0, getWidth() + getHeight()).setDuration(FILL_TIME);
        revealAnimator.setInterpolator(INTERPOLATOR);
        revealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                changeState(STATE_FINISHED);
            }
        });
        revealAnimator.start();
    }

    public void setToFinishedFrame() {
        changeState(STATE_FINISHED);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (state != STATE_FINISHED) {
            fillPaint.setColor(0xFFFFFFFF);
            canvas.drawCircle(startLocationX, startLocationY, currentRadius, fillPaint);
            fillPaint.setColor(0xFF2D2B28);
            canvas.drawRect(getWidth() - UIUtils.dip2px(20), 0, getWidth(), getHeight(), fillPaint);
            fillPaint.setColor(0xFFFFFFFF);
            canvas.drawCircle(canvas.getWidth() - radius, radius, radius, fillPaint);
        }
    }

    private void changeState(int state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        if (onStateChangeListener != null) {
            onStateChangeListener.onStateChange(state);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public void setCurrentRadius(int radius) {
        this.currentRadius = radius;
        invalidate();
    }

    public interface OnStateChangeListener {
        void onStateChange(int state);
    }
}