package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.utils.UIUtils;

import java.util.Objects;

/**
 * Created by qingcha on 16/6/1.
 */
public class OrderDetailStateView extends LinearLayout implements HbcViewBehavior {

    private ImageView stateIV;
    private TextView topTV, bottomTV;

    public OrderDetailStateView(Context context) {
        this(context, null);
    }

    public OrderDetailStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        inflate(context, R.layout.view_order_detail_state, this);
        stateIV = (ImageView) findViewById(R.id.order_detail_state_iv);
        topTV = (TextView) findViewById(R.id.order_detail_state_top_tv);
        bottomTV = (TextView) findViewById(R.id.order_detail_state_bottom_tv);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        OrderBean orderBean = (OrderBean) _data;
        switch (orderBean.orderStatus) {
            case INITSTATE://1:未付款
                setTypePayment(orderBean.getPayDeadTime());
                break;
            case PAYSUCCESS://2:已付款
                setStyleOther(0xFFF1FFE8, 0xFF7ABE57, R.string.order_detail_predetermined, R.string.order_detail_prompt_wait_guide, R.mipmap.order_booking);
                break;
            case AGREE:
            case ARRIVED://已接单(3:已接单,4:已到达)
                setStyleOther(0xFFF1FFE8, 0xFF7CBD55, R.string.order_detail_receiving, R.string.order_detail_prompt_new_travel, R.mipmap.order_order);
                break;
            case SERVICING://5:服务中
                setStyleOther(0xFFE1FDFF, 0xFF32C0CA, R.string.order_detail_receiving, R.string.order_detail_inservice, R.mipmap.order_service);
                break;
            case NOT_EVALUATED://6:
            case COMPLETE://7:服务完成
                if (!orderBean.isEvaluated()) {//服务完成未评价
                    setStyleOther(0xFFDEDCDD, 0xFF333333, R.string.order_detail_finish, R.string.order_detail_prompt_evaluation_2, R.mipmap.order_serviceover);
                } else {//服务完成已评价
                    setStyleOther(0xFFDEDCDD, 0xFF333333, R.string.order_detail_finish, R.string.order_detail_prompt_travel_end, R.mipmap.order_end);
                }
                break;
            case CANCELLED:
            case REFUNDED://已取消(8:已取消,9:已退款)
                setStyleCancelled();
                break;
            case COMPLAINT://订单已冻结(10:客诉处理中)
                setStyleOther(0xFFE1E9FE, 0xFF3172CE, R.string.order_detail_thaw, R.string.order_detail_disputes, R.mipmap.order_frozen);
                break;
        }
    }

    private void setStyleOther(int bgColor, int topTextColor, int topStrId, int bottomStrId, int stateImgId) {
        setBackgroundColor(bgColor);

        bottomTV.setVisibility(View.VISIBLE);
        bottomTV.setText(getContext().getString(bottomStrId));
        bottomTV.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        topTV.setTypeface(Typeface.DEFAULT_BOLD);
        topTV.setTextColor(topTextColor);
        topTV.setText(getContext().getString(topStrId));
        topTV.setTextSize(15);

        stateIV.setBackgroundResource(stateImgId);
    }

    private void setStyleCancelled() {
        setBackgroundColor(0xFFDEDCDD);

        bottomTV.setText("");
        bottomTV.setVisibility(View.GONE);
        bottomTV.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        topTV.setTypeface(Typeface.DEFAULT_BOLD);
        topTV.setTextColor(0xFF333333);
        topTV.setText(getContext().getString(R.string.order_detail_cancel));
        topTV.setTextSize(15);

        stateIV.setBackgroundResource(R.mipmap.order_cancel);
    }

    private void setTypePayment(String payDeadTime) {
        setBackgroundColor(0xFFFFFCD9);

        bottomTV.setVisibility(View.VISIBLE);
        bottomTV.setText(getContext().getString(R.string.order_detail_pay_prompt_2));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.topMargin = UIUtils.dip2px(-5);
        params.bottomMargin = UIUtils.dip2px(6);
        bottomTV.setLayoutParams(params);

        topTV.setTypeface(Typeface.DEFAULT);
        topTV.setTextColor(0xFF333333);
        topTV.setTextSize(13);

        stateIV.setBackgroundResource(R.mipmap.payment_wait);

        String promptText = getContext().getString(R.string.order_detail_pay_prompt_1, payDeadTime);
        int start = 2;
        int end = start + payDeadTime.length();
        SpannableString sp = new SpannableString(promptText);
        sp.setSpan(new RelativeSizeSpan(1.6f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(0xFFFF6634), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        topTV.setText(sp);
    }
}
