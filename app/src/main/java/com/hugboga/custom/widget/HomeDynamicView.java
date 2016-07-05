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
 *
 * 首页「今日动态」取数据的逻辑。
 * 1、首次请求 or 距离上次请求的时间戳大于24小时，则拉取360条未读订单动态（足够累积展示30分钟）；如果不足360条，则全部拉取。
 * 2、本地订单动态始终按照从老到新（订单动态的时间）的顺序展示在app里。当整个列表还剩10条动态可以展示时，就再次从服务器拉取动态，
 *    并接在本地的动态列表末尾。然后继续往后取动态展示直到列表末尾。就从头开始展示动态，当整个列表还剩10条动态可以展示时，
 *    就再次从服务器拉取动态，并接在本地的动态列表末尾。以此循环。
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
