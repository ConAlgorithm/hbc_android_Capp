package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by qingcha on 16/9/8.
 */
public class DeliverCircleProgressView extends View{

    private Paint mPaint;
    private int mProgress;
    private RectF oval;

    public DeliverCircleProgressView(Context context) {
        this(context, null);
    }

    public DeliverCircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xFF826E6F);
        mProgress = 0;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (oval == null) {
            oval = new RectF(0, 0, getWidth(), getWidth());
        }
        canvas.drawArc(oval, -90, mProgress, true, mPaint);
    }
}
