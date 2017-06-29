package com.squareup.timessquare;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class ScoreTextView extends TextView {
    private Paint paint;
    private boolean isDrawScoreLine = false;

    public ScoreTextView(Context context) {
        this(context, null);
    }

    public ScoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFFCCCCCC);
        paint.setStrokeWidth(dip2px(1));
    }

    public void isDrawScoreLine(boolean isDraw) {
        this.isDrawScoreLine = isDraw;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDrawScoreLine) {
            int partWidth = getMeasuredWidth() / 4;
            int partHeight = getMeasuredHeight() / 4;
            canvas.drawLine(partWidth * 3 - partWidth / 3, partHeight / 2, partWidth + partWidth / 3, partHeight * 3 + partHeight / 2, paint);
        }
    }

    public int dip2px(float dpValue) {
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getContext().getResources().getDisplayMetrics()) + 0.5f);
    }
}
