package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderGuideInfo;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.Tools;

import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by qingcha on 16/6/3.
 */
public class OrderDetailGuideInfo extends LinearLayout implements HbcViewBehavior, View.OnClickListener {

    private PolygonImageView avatarIV;
    private TextView collectTV, promptTV, evaluateTV, imTV, phoneTV;
    private View lineView;
    private LinearLayout navLayout;

    private String orderNo;

    public OrderDetailGuideInfo(Context context) {
        this(context, null);
    }

    public OrderDetailGuideInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.include_order_guide_info, this);
        avatarIV = (PolygonImageView) findViewById(R.id.ogi_avatar_iv);
        collectTV = (TextView) findViewById(R.id.ogi_collect_tv);
        evaluateTV = (TextView) findViewById(R.id.ogi_evaluate_tv);
        promptTV = (TextView) findViewById(R.id.ogi_prompt_tv);
        imTV = (TextView) findViewById(R.id.ogi_im_tv);
        phoneTV = (TextView) findViewById(R.id.ogi_phone_tv);
        lineView = findViewById(R.id.ogi_horizontal_line);
        navLayout = (LinearLayout) findViewById(R.id.ogi_nav_layout);

        collectTV.setOnClickListener(this);
        evaluateTV.setOnClickListener(this);
        imTV.setOnClickListener(this);
        phoneTV.setOnClickListener(this);
        avatarIV.setOnClickListener(this);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        OrderBean orderBean = (OrderBean) _data;
        this.orderNo = orderBean.orderNo;
        final OrderGuideInfo guideInfo = orderBean.orderGuideInfo;
        if (orderBean.orderStatus == OrderStatus.INITSTATE || orderBean.orderStatus == OrderStatus.PAYSUCCESS || guideInfo == null) {//1:未付款 || 2:已付款
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            if (orderBean.orderStatus == OrderStatus.NOT_EVALUATED || orderBean.orderStatus == OrderStatus.COMPLETE) {//6:服务完成
                lineView.setVisibility(View.VISIBLE);
                navLayout.setVisibility(View.VISIBLE);
                collectTV.setText(getContext().getString(guideInfo.isCollected() ? R.string.uncollect : R.string.collect));
                promptTV.setVisibility(orderBean.isEvaluated() ? View.GONE : View.VISIBLE);
            } else {
                lineView.setVisibility(View.GONE);
                navLayout.setVisibility(View.GONE);
                promptTV.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(guideInfo.guideAvatar)) {
                avatarIV.setImageResource(R.mipmap.journey_head_portrait);
            } else {
                Tools.showImage(avatarIV, guideInfo.guideAvatar);
            }
            ((TextView)findViewById(R.id.ogi_name_tv)).setText(guideInfo.guideName);
            ((TextView)findViewById(R.id.ogi_describe_tv)).setText(guideInfo.guideCar);
            ((TextView)findViewById(R.id.ogi_plate_number_tv)).setText(guideInfo.carNumber);
            ((RatingView)findViewById(R.id.ogi_ratingview)).setLevel((float)guideInfo.guideStarLevel);

            imTV.setVisibility(orderBean.isIm ? View.VISIBLE : View.GONE);
            phoneTV.setVisibility(orderBean.isPhone ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ogi_collect_tv:
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_GUIDE_COLLECT, orderNo));
                break;
            case R.id.ogi_evaluate_tv:
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_GUIDE_EVALUATION, orderNo));
                break;
            case R.id.ogi_im_tv:
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_GUIDE_CHAT, orderNo));
                break;
            case R.id.ogi_phone_tv:
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_GUIDE_CALL, orderNo));
                break;
            case R.id.ogi_avatar_iv:
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_GUIDE_INFO, orderNo));
                break;
        }
    }
}
