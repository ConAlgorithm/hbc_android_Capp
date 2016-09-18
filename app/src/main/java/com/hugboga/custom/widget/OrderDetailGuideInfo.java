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
    private TextView collectTV, promptTV, evaluateTV, chatTV, phoneTV, describeTV;
    private View lineView;
    private LinearLayout navLayout;

    private String orderNo;

    public OrderDetailGuideInfo(Context context) {
        this(context, null);
    }

    public OrderDetailGuideInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFFFFFFF);

        inflate(context, R.layout.include_order_guide_info, this);
        avatarIV = (PolygonImageView) findViewById(R.id.ogi_avatar_iv);
        collectTV = (TextView) findViewById(R.id.ogi_collect_tv);
        evaluateTV = (TextView) findViewById(R.id.ogi_evaluate_tv);
        promptTV = (TextView) findViewById(R.id.ogi_prompt_tv);
        chatTV = (TextView) findViewById(R.id.ogi_chat_tv);
        phoneTV = (TextView) findViewById(R.id.ogi_call_tv);
        lineView = findViewById(R.id.ogi_horizontal_line);
        navLayout = (LinearLayout) findViewById(R.id.ogi_nav_layout);
        describeTV = (TextView)findViewById(R.id.ogi_describe_tv);

        collectTV.setOnClickListener(this);
        evaluateTV.setOnClickListener(this);
        chatTV.setOnClickListener(this);
        phoneTV.setOnClickListener(this);
        findViewById(R.id.ogi_info_layout).setOnClickListener(this);
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
            promptTV.setVisibility(View.GONE);
            lineView.setVisibility(View.GONE);
            navLayout.setVisibility(View.GONE);

            switch (orderBean.orderStatus) {
                case AGREE://3:已接单
                case ARRIVED://4:已到达
                case SERVICING://5:服务中
                    if(orderBean.isIm || orderBean.isPhone) {
                        lineView.setVisibility(View.VISIBLE);
                        navLayout.setVisibility(View.VISIBLE);
                        evaluateTV.setVisibility(View.GONE);
                        collectTV.setVisibility(View.GONE);
                        chatTV.setVisibility(orderBean.isIm ? View.VISIBLE : View.GONE);
                        phoneTV.setVisibility(orderBean.isPhone ? View.VISIBLE : View.GONE);
                    }
                    break;
                case NOT_EVALUATED://6:服务完成
                case COMPLETE://7:已完成
                    lineView.setVisibility(View.VISIBLE);
                    navLayout.setVisibility(View.VISIBLE);
                    phoneTV.setVisibility(View.GONE);
                    collectTV.setVisibility(View.VISIBLE);
                    evaluateTV.setVisibility(View.VISIBLE);
                    chatTV.setVisibility(orderBean.isIm ? View.VISIBLE : View.GONE);
                    collectTV.setText(getContext().getString(guideInfo.isCollected() ? R.string.uncollect : R.string.collect));
                    evaluateTV.setText(getContext().getString(orderBean.isEvaluated() ? R.string.order_detail_evaluated : R.string.order_detail_evaluate));
                    promptTV.setVisibility(orderBean.isEvaluated() ? View.GONE : View.VISIBLE);
                    break;
            }

            if (TextUtils.isEmpty(guideInfo.guideAvatar)) {
                avatarIV.setImageResource(R.mipmap.journey_head_portrait);
            } else {
                Tools.showImage(avatarIV, guideInfo.guideAvatar);
            }
            ((TextView)findViewById(R.id.ogi_name_tv)).setText(guideInfo.guideName);
            ((SimpleRatingBar)findViewById(R.id.ogi_ratingview)).setRating((float)guideInfo.guideStarLevel);
            ((TextView)findViewById(R.id.ogi_describe_tv)).setText(guideInfo.guideCar);
            if (!TextUtils.isEmpty(guideInfo.carNumber)) {
                ((TextView)findViewById(R.id.ogi_plate_number_tv)).setText(getContext().getString(R.string.platenumber) + guideInfo.carNumber);
            }
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
            case R.id.ogi_chat_tv:
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_GUIDE_CHAT, orderNo));
                break;
            case R.id.ogi_call_tv:
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_GUIDE_CALL, orderNo));
                break;
            case R.id.ogi_info_layout:
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_GUIDE_INFO, orderNo));
                break;
        }
    }
}
