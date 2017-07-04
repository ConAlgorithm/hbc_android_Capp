package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/1.
 */
public class OrderDetailStateView extends RelativeLayout implements HbcViewBehavior {

    private ImageView stateIV;
    private TextView topTV, bottomTV;
    private LinearLayout titleLayout;

    public OrderDetailStateView(Context context) {
        this(context, null);
    }

    public OrderDetailStateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.view_order_detail_state, this);
        stateIV = (ImageView) findViewById(R.id.order_detail_state_iv);
        topTV = (TextView) findViewById(R.id.order_detail_state_top_tv);
        bottomTV = (TextView) findViewById(R.id.order_detail_state_bottom_tv);
        titleLayout = (LinearLayout) findViewById(R.id.order_detail_state_title_layout);
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
                setTypePayment(0xFFFF3B3B, "等待支付", R.mipmap.order_state_unpaid, orderBean.getPayDeadTime());
                break;
            case PAYSUCCESS://2:已付款
                setStyleSingle(0xFF83E06B, "预订成功", R.mipmap.order_state_success);
                break;
            case AGREE://3:已接单
                setStyleSingle(0xFF83E06B, "司导已接单", R.mipmap.order_state_success);
                break;
            case ARRIVED://4:已到达
                setStyleSingle(0xFFFFC110, "司导已到达", R.mipmap.order_state_arrive);
                break;
            case SERVICING://5:服务中
                setStyleSingle(0xFFFFC110, "服务中", R.mipmap.order_state_service);
                break;
            case NOT_EVALUATED://6:服务完成未评价
            case COMPLETE://7:服务完成
                if (!orderBean.isEvaluated() && orderBean.orderType != 888) {//服务完成未评价
                    setStyleSingle(0xFFFFC110, "服务完成，请评价", R.mipmap.order_state_finish);
                } else {//服务完成已评价
                    setStyleSingle(0xFFFFC110, "服务完成", R.mipmap.order_state_finish);
                }
                break;
            case CANCELLED://8:已取消
                setStyleSingle(0xFFDBDBDB, "已取消", R.mipmap.order_state_cancel);
                break;
            case REFUNDED://9:已退款
                setStyleSingle(0xFFDBDBDB, "已退款", R.mipmap.order_state_refund);
                break;
            case COMPLAINT://订单已冻结(10:客诉处理中)
                setStyleSingle(0xFFFF3B3B, "纠纷处理中", R.mipmap.order_state_complaint);
                break;
        }
    }

    private void setStyleSingle(int bgColor, String str, int stateImgId) {
        setBackgroundColor(bgColor);

        topTV.setText(str);
        stateIV.setBackgroundResource(stateImgId);

        bottomTV.setText("");
        bottomTV.setVisibility(View.GONE);

        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(100), UIUtils.dip2px(30));
        imgParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imgParams.addRule(RelativeLayout.CENTER_VERTICAL);
        stateIV.setLayoutParams(imgParams);

        RelativeLayout.LayoutParams titleLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.dip2px(30));
        titleLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        titleLayout.setLayoutParams(titleLayoutParams);
    }

    private void setTypePayment(int bgColor, String str, int stateImgId, String payDeadTime) {
        setBackgroundColor(bgColor);

        topTV.setText(str);
        stateIV.setBackgroundResource(stateImgId);

        bottomTV.setVisibility(View.VISIBLE);
        bottomTV.setText(String.format("请在%1$s内付款，逾期订单将被取消", payDeadTime));

        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(100), UIUtils.dip2px(50));
        imgParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imgParams.addRule(RelativeLayout.CENTER_VERTICAL);
        stateIV.setLayoutParams(imgParams);

        RelativeLayout.LayoutParams titleLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.dip2px(50));
        titleLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        titleLayout.setLayoutParams(titleLayoutParams);
    }
}
