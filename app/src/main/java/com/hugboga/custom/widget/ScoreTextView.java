package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 17/6/20.
 */
public class ScoreTextView extends TextView {

    public Paint paint;

    public ScoreTextView(Context context) {
        this(context, null);
    }

    public ScoreTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(UIUtils.dip2px(1));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int partWidth = getMeasuredWidth() / 4;
        int partHeight = getMeasuredHeight() / 4;
//        canvas.drawLine(partWidth * 3, partHeight, partWidth, partHeight * 3, paint);
    }
}
