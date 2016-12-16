package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/9/8.
 */
public class DeliverCircleProgressView extends View{

    private Paint mPaint;
    private RectF oval;
    private SweepGradient mGradientSweep;

    private float mProgress;
    private int strokeWidth = UIUtils.dip2px(1);
    private int mProgressWidth = UIUtils.dip2px(9);

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

    public void setProgress(float mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }

    public void setProgressWidth(int progressWidth) {
        this.mProgressWidth = progressWidth;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = getWidth() / 2;

        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setAntiAlias(true);
        if (mGradientSweep == null) {
            mGradientSweep = new SweepGradient(radius, radius, new int[] {0xFFFFD811, 0xFFFFEC20}, null);
            Matrix matrix = new Matrix();
            matrix.setRotate(-90, radius, radius);
            mGradientSweep.setLocalMatrix(matrix);
        }
        mPaint.setShader(mGradientSweep);
        if (oval == null) {
            int arcRadius = mProgressWidth / 2 + strokeWidth;
            oval = new RectF(arcRadius, arcRadius, getWidth() - arcRadius, getWidth() - arcRadius);
        }
        canvas.drawArc(oval, -90, mProgress, false, mPaint);

        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);

        mPaint.setColor(0xFFDBDBDB);
        canvas.drawCircle(radius, radius, (getWidth() - strokeWidth) / 2, mPaint);

        mPaint.setColor(0xFFFFC110);
        canvas.drawCircle(radius, radius, getWidth() / 2 - mProgressWidth - strokeWidth - strokeWidth / 2, mPaint);
    }
}
