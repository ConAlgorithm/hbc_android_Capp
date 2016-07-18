package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/3.
 */
public class OrderDetailRouteItemView extends TextView {

    private Paint paint;
    private RectF rect;
    private StyleType styleType;

    public OrderDetailRouteItemView(Context context) {
        this(context, null);
    }

    public OrderDetailRouteItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER_VERTICAL);
        setTextColor(0xFF3D3930);
        setTextSize(13);
        setPadding(UIUtils.dip2px(40), 0, 0, 0);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xFF3D3930);

        rect = new RectF();
        final int lineWidth = UIUtils.dip2px(1);
        rect.left = (getPaddingLeft() - lineWidth) / 2.0f;
        rect.right = rect.left + lineWidth;
    }

    public enum StyleType {
        ONE, TOP, BOTTOM, ALL
    }

    public void setStyle(int index, int size) {
        if (size == 1) {
            this.styleType = StyleType.ONE;
        } else if (index == 0) {
            this.styleType = StyleType.TOP;
        } else if (index == size - 1) {
            this.styleType = StyleType.BOTTOM;
        } else {
            this.styleType = StyleType.ALL;
        }
        invalidate();
    }

    public void setStyleType(StyleType type) {
        this.styleType = type;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY), UIUtils.dip2px(34));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (styleType != StyleType.ONE) {
            switch (styleType) {
                case TOP:
                    rect.top = getHeight() / 2.0f;
                    rect.bottom = getHeight();
                    break;
                case BOTTOM:
                    rect.top = 0;
                    rect.bottom = getHeight() / 2.0f;
                    break;
                case ALL:
                    rect.top = 0;
                    rect.bottom = getHeight();
                    break;
            }
            canvas.drawRect(rect, paint);
        }
        float circleCX = (rect.right - rect.left) / 2.0f + rect.left;
        canvas.drawCircle(circleCX, getHeight() / 2.0f, UIUtils.dip2px(3), paint);
    }
}

