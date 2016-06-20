package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.request.RequestDynamics;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeDynamicView extends LinearLayout implements HttpRequestListener {

    private Paint mPaint;
    private LinearGradient gradient;
    private RectF gradientRect;

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

        TextView dynamicTV = new TextView(getContext());
        dynamicTV.setSingleLine(true);
        dynamicTV.setFocusable(true);
        dynamicTV.setFocusableInTouchMode(true);
        dynamicTV.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        dynamicTV.setMarqueeRepeatLimit(-1);
        dynamicTV.setPadding(paddingLeft, 0, paddingLeft, 0);
        dynamicTV.setTextColor(0xFFFFEE31);
        dynamicTV.setTextSize(15);
        addView(dynamicTV);

        dynamicTV.setText("我先测试一下测试测试一下不是的真的吗哈哈哈美女如云啊啊2332323 adasd 打死都能看到按时打卡了圣诞节dsdSBLASD");

        gradientRect = new RectF(titleTV.getPaint().measureText(titleTV.getText().toString()) + paddingLeft * 2, 0, UIUtils.getScreenWidth() - paddingLeft, UIUtils.dip2px(55));
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int[] colors = {0xFF000000, 0x00000000, 0x00000000, 0x00000000, 0xFF000000};
        gradient = new LinearGradient(gradientRect.left, gradientRect.top, gradientRect.right, gradientRect.bottom, colors, null, Shader.TileMode.MIRROR);
        mPaint.setShader(gradient);
    }

    private void setRequest(long reqTime, int limit) {
        HttpRequestUtils.request(getContext(), new RequestDynamics(getContext(), reqTime, limit), this);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawRect(gradientRect, mPaint);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        //RequestDynamics
        //DynamicsData
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

    }

}
