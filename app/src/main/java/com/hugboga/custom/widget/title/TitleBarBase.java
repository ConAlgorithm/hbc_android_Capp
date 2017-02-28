package com.hugboga.custom.widget.title;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;

/**
 * Created by qingcha on 17/2/21.
 */
public class TitleBarBase extends RelativeLayout {

    private boolean isLineEnable = true;
    private int titleDividerColr;

    public TitleBarBase(Context context) {
        super(context, null);
    }

    public TitleBarBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(context.getResources().getColor(R.color.titlebar_bg));
        titleDividerColr = context.getResources().getColor(R.color.titlebar_dvider);
    }

    public void setImageBitmap(Bitmap bm) {
        setBackgroundDrawable(new BitmapDrawable(getContext().getResources(), bm));
    }

    public void setTitleDividerColor(int color){
        titleDividerColr = color;
    }

    protected Paint createLinePaint() {
        Paint mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(titleDividerColr);
        return mLinePaint;
    }

    public void setLineEnable(boolean enable) {
        isLineEnable = enable;
        invalidate();
    }

    public boolean isLineEnable() {
        return isLineEnable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = null;
        if (paint == null) {
            paint = createLinePaint();
        }
        if (isLineEnable) {
            canvas.drawLine(0, canvas.getHeight() - 1, canvas.getWidth(), canvas.getHeight() - 1, paint);
        }
    }
}
