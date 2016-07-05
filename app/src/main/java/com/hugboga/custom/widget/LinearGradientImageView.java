package com.hugboga.custom.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hugboga.custom.R;

/**
 * Created by qingcha on 16/7/4.
 */
public class LinearGradientImageView extends ImageView{

    public static final int LEFT = 0x001;
    public static final int BOTTOM = 0x002;

    private int currentType = -1;

    private Paint mPaint;
    private int[] colors = new int[2];
    private LinearGradient gradient;

    public LinearGradientImageView(Context context) {
        this(context, null);
    }

    public LinearGradientImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinearGradientImageView);
        currentType = typedArray.getInteger(R.styleable.LinearGradientImageView_gradient_orientation, -1);
        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        colors[0] = 0x88000000;
        colors[1] = 0x00000000;
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
        gradient = null;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        switch (currentType) {
            case LEFT:
                if (gradient == null) {
                    gradient = new LinearGradient(0, getHeight() / 2, getWidth(), getHeight() / 2, colors, null, Shader.TileMode.MIRROR);
                    mPaint.setShader(gradient);
                }
                break;
            case BOTTOM:
                if (gradient == null) {
                    gradient = new LinearGradient(getWidth() / 2, getHeight(), getWidth() / 2, 0, colors, null, Shader.TileMode.MIRROR);
                    mPaint.setShader(gradient);
                }
                break;
        }
        if (gradient != null) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        }
    }
}
