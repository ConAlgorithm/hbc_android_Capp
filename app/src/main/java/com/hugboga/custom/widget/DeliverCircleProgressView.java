package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/9/8.
 */
public class DeliverCircleProgressView extends View{

    private Paint mPaint;
    private RectF oval;
    
    private float mProgress;
    private int strokeWidth = UIUtils.dip2px(1);
    private int circleDiam = UIUtils.dip2px(4);

    public DeliverCircleProgressView(Context context) {
        this(context, null);
    }

    public DeliverCircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int radius = getWidth() / 2;

        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xFFD4D4D4);
        int borderRadius = (getWidth() - strokeWidth - circleDiam) / 2;
        canvas.drawCircle(radius, radius, borderRadius, mPaint);

        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(getContext().getResources().getColor(R.color.default_yellow));
        mPaint.setAntiAlias(true);
        if (oval == null) {
            int arcRadius = strokeWidth / 2 + circleDiam / 2;
            oval = new RectF(arcRadius, arcRadius, getWidth() - arcRadius, getWidth() - arcRadius);
        }
        canvas.drawArc(oval, -90, mProgress, false, mPaint);

        mPaint.reset();
        mPaint.setColor(getContext().getResources().getColor(R.color.default_yellow));
        mPaint.setAntiAlias(true);
        double x1 = radius + borderRadius * Math.cos((mProgress - 90) * Math.PI / 180);
        double y2 = radius + borderRadius * Math.sin((mProgress - 90) * Math.PI / 180);
        canvas.drawCircle((int)x1, (int)y2, circleDiam / 2, mPaint);
    }
}
