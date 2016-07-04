package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeDynamicView extends LinearLayout{

    private Paint mPaint;
    private LinearGradient gradient;
    private RectF gradientRect;

    private HomeDynamicsTextView dynamicTV;


    public HomeDynamicView(Context context) {
        this(context, null);
    }

    public HomeDynamicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(0xFF000000);

        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        TextView titleTV = new TextView(getContext());
        titleTV.setPadding(paddingLeft, 0, 0, 0);
        titleTV.setTextColor(0xFFFFEE31);
        titleTV.setTextSize(15);
        titleTV.setTypeface(Typeface.DEFAULT_BOLD);
        titleTV.setText(getContext().getString(R.string.home_dynamics));
        addView(titleTV);

        dynamicTV = new HomeDynamicsTextView(getContext());
        LinearLayout.LayoutParams dynamicParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        dynamicParams.leftMargin = paddingLeft;
        dynamicParams.rightMargin = paddingLeft;
        addView(dynamicTV, dynamicParams);

        //阴影浮层 应该移到HomeDynamicsTextView里，有空优化
        gradientRect = new RectF(titleTV.getPaint().measureText(titleTV.getText().toString()) + paddingLeft * 2, 0, UIUtils.getScreenWidth() - paddingLeft, UIUtils.dip2px(40));
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int[] colors = {0xFF000000, 0x00000000, 0x00000000, 0x00000000, 0xFF000000};
        gradient = new LinearGradient(gradientRect.left, gradientRect.top, gradientRect.right, gradientRect.bottom, colors, null, Shader.TileMode.MIRROR);
        mPaint.setShader(gradient);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawRect(gradientRect, mPaint);
    }

    public void onRestart() {
        if (dynamicTV != null) {
            dynamicTV.setSwitch(true);
        }
    }

    public void onSuspend() {
        if (dynamicTV != null) {
            dynamicTV.setSwitch(false);
        }
    }

}
