package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.EvaluateNewActivity;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.NIMChatActivity;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderGuideInfo;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.request.RequestCollectGuidesId;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.Tools;

import net.grobas.view.PolygonImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/6/3.
 */
public class OrderDetailGuideInfo extends LinearLayout implements HbcViewBehavior, View.OnClickListener, HttpRequestListener {

    @Bind(R.id.ogi_avatar_iv)
    PolygonImageView avatarIV;
    @Bind(R.id.ogi_name_tv)
    TextView nameTV;
    @Bind(R.id.ogi_star_iv)
    ImageView starIV;
    @Bind(R.id.ogi_star_tv)
    TextView starTV;
    @Bind(R.id.ogi_state_tv)
    TextView stateTV;

    @Bind(R.id.ogi_describe_tv)
    TextView describeTV;
    @Bind(R.id.ogi_plate_number_tv)
    TextView numberTV;

    @Bind(R.id.ogi_nav_layout)
    LinearLayout navLayout;
    @Bind(R.id.ogi_call_layout)
    LinearLayout callLayout;
    @Bind(R.id.ogi_chat_layout)
    LinearLayout chatLayout;

    @Bind(R.id.ogi_evaluate_layout)
    LinearLayout evaluateLayout;
    @Bind(R.id.ogi_evaluate_iv)
    ImageView evaluateIV;
    @Bind(R.id.ogi_evaluate_tv)
    TextView evaluateTV;

    @Bind(R.id.ogi_collect_layout)
    LinearLayout collectLayout;
    @Bind(R.id.ogi_collect_tv)
    TextView collectTV;
    @Bind(R.id.ogi_collect_iv)
    ImageView collectIV;

    private OrderBean orderBean;

    public OrderDetailGuideInfo(Context context) {
        this(context, null);
    }

    public OrderDetailGuideInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFFFFFFF);

        View view = inflate(context, R.layout.include_order_guide_info, this);
        ButterKnife.bind(view);

    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        orderBean = (OrderBean) _data;
        final OrderGuideInfo guideInfo = orderBean.orderGuideInfo;
        if (orderBean.orderStatus == OrderStatus.INITSTATE || orderBean.orderStatus == OrderStatus.PAYSUCCESS || guideInfo == null) {//1:未付款 || 2:已付款
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            navLayout.setVisibility(View.GONE);
            switch (orderBean.orderStatus) {
                case AGREE://3:已接单
                case ARRIVED://4:已到达
                case SERVICING://5:服务中
                    if (orderBean.isIm || orderBean.isPhone) {
                        navLayout.setVisibility(View.VISIBLE);
                        evaluateLayout.setVisibility(View.GONE);
                        collectLayout.setVisibility(View.GONE);
                        collectIV.setVisibility(View.GONE);
                        chatLayout.setVisibility(orderBean.isIm ? View.VISIBLE : View.GONE);
                        callLayout.setVisibility(orderBean.isPhone ? View.VISIBLE : View.GONE);
                    }
                    break;
                case NOT_EVALUATED://6:服务完成
                case COMPLETE://7:已完成
                    navLayout.setVisibility(View.VISIBLE);
                    callLayout.setVisibility(View.GONE);
                    evaluateLayout.setVisibility(View.VISIBLE);
                    chatLayout.setVisibility(orderBean.isIm ? View.VISIBLE : View.GONE);
                    if (orderBean.isEvaluated()) {
                        evaluateIV.setVisibility(View.VISIBLE);
                        if (orderBean.appraisement != null) {
                            evaluateTV.setText((int)orderBean.appraisement.totalScore + "星评价");
                        } else {
                            evaluateTV.setText("已评价");
                        }
                    } else {
                        evaluateIV.setVisibility(View.GONE);
                        evaluateTV.setText("评价司导");
                    }

                    if (guideInfo.isCollected()) { //不可取消收藏
                        collectIV.setVisibility(View.VISIBLE);
                        collectLayout.setVisibility(View.GONE);
                    } else {
                        collectIV.setVisibility(View.GONE);
                        collectLayout.setVisibility(View.VISIBLE);
                    }
                    break;
                case COMPLAINT://10:客诉处理中
                    navLayout.setVisibility(View.VISIBLE);
                    callLayout.setVisibility(View.GONE);
                    evaluateLayout.setVisibility(View.GONE);
                    collectLayout.setVisibility(View.GONE);
                    collectIV.setVisibility(View.GONE);
                    chatLayout.setVisibility(orderBean.isIm ? View.VISIBLE : View.GONE);
                    break;
            }

            Tools.showImage(avatarIV, guideInfo.guideAvatar, R.mipmap.icon_avatar_guide);
            nameTV.setText(orderBean.getGuideName());
            if ((float)guideInfo.guideStarLevel >= 4) {
                starIV.setBackgroundResource(R.mipmap.star_level_full);
            } else {
                starIV.setBackgroundResource(R.mipmap.star_level_half);
            }
            starTV.setText(String.format("%1$s星 / %2$s单", "" + guideInfo.guideStarLevel, "" + guideInfo.orderCount));

            describeTV.setText("车型：" + guideInfo.guideCar);
            if (!TextUtils.isEmpty(guideInfo.carNumber)) {
                numberTV.setText(getContext().getString(R.string.platenumber) + guideInfo.carNumber);
            }
            stateTV.setText(orderBean.orderStatus.name);
        }
    }

    @OnClick({R.id.ogi_collect_layout, R.id.ogi_evaluate_layout, R.id.ogi_chat_layout, R.id.ogi_call_layout, R.id.ogi_avatar_iv})
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ogi_collect_layout:
                if (orderBean == null || orderBean.orderGuideInfo == null || orderBean.orderGuideInfo.isCollected()) {
                    return;
                }
                EventUtil.onDefaultEvent(StatisticConstant.COLLECTG, ((OrderDetailActivity) getContext()).getEventSource());
                RequestCollectGuidesId requestCollectGuidesId = new RequestCollectGuidesId(getContext(), orderBean.orderGuideInfo.guideID);
                HttpRequestUtils.request(getContext(), requestCollectGuidesId, this);
                break;
            case R.id.ogi_evaluate_layout:
                if (orderBean == null) {
                    return;
                }
                intent = new Intent(getContext(), EvaluateNewActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, orderBean);
                intent.putExtra(Constants.PARAMS_SOURCE, ((OrderDetailActivity) getContext()).getEventSource());
                intent.putExtra("isFromOrderDetail",true);
                ((OrderDetailActivity) getContext()).startActivityForResult(intent,OrderDetailActivity.EVALUATE_TYPE);
                break;
            case R.id.ogi_chat_layout:
                if (orderBean == null || orderBean.imInfo == null) {
                    return;
                }
                final ChatBean chatBean = orderBean.imInfo;
                if (!IMUtil.getInstance().isLogined() || TextUtils.isEmpty(chatBean.getNeTargetId())) {
                    return;
                }
                NIMChatActivity.start(getContext(), chatBean.getNeTargetId());
                break;
            case R.id.ogi_call_layout:
                if (orderBean == null || orderBean.orderGuideInfo == null) {
                    return;
                }
                PhoneInfo.CallDial(getContext(), orderBean.orderGuideInfo.guideTel);
                break;
            case R.id.ogi_avatar_iv:
                if (orderBean == null || orderBean.orderGuideInfo == null) {
                    return;
                }
                GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
                params.guideId = orderBean.orderGuideInfo.guideID;
//                params.guideCarId = "" + orderBean.carId;
//                params.guideAgencyDriverId = orderBean.guideAgencyDriverId;
//                params.isSelectedService = orderBean.guideAgencyType == 3;
                intent = new Intent(getContext(), GuideWebDetailActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                intent.putExtra(Constants.PARAMS_SOURCE, ((OrderDetailActivity) getContext()).getEventSource());
                getContext().startActivity(intent);
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        ApiReportHelper.getInstance().addReport(request);
        if (request instanceof RequestCollectGuidesId) {//收藏
            orderBean.orderGuideInfo.storeStatus = 1;
            if (orderBean.orderGuideInfo.isCollected()) {
                collectLayout.setVisibility(View.GONE);
                collectIV.setVisibility(View.VISIBLE);
                EventUtil.onDefaultEvent(StatisticConstant.COLLECTG, ((OrderDetailActivity) getContext()).getEventSource());
            }
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

    }
}
