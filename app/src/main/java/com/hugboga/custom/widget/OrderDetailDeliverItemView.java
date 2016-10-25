package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CanServiceGuideListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CanServiceGuideBean;
import com.hugboga.custom.data.bean.DeliverInfoBean;
import com.hugboga.custom.data.request.RequestAcceptGuide;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by qingcha on 16/9/8.
 */
public class OrderDetailDeliverItemView extends LinearLayout implements HbcViewBehavior, HttpRequestListener {

    @Bind(R.id.eliver_item_loading_view)
    DeliverLoadingView loadingView;
    @Bind(R.id.eliver_item_countdown_view)
    OrderDetailDeliverCountDownView countdownLayout;

    @Bind(R.id.deliver_item_title_tv)
    TextView titleTV;

    @Bind(R.id.deliver_item_subtitle_tv)
    TextView subtitleIV;
    @Bind(R.id.deliver_item_guide_avatar_layout)
    LinearLayout avatarLayout;


    private String orderNo;
    private int orderType;
    private ErrorHandler errorHandler;

    public OrderDetailDeliverItemView(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final View view = inflate(context, R.layout.view_deliver_item, this);
        ButterKnife.bind(view);
        loadingView.setVisibility(View.GONE);
    }

    public void setOrderNo(String _orderNo, int orderType) {
        this.orderNo = _orderNo;
        this.orderType = orderType;
    }

    @Override
    public void update(Object _data) {
        DeliverInfoBean deliverInfoBean = (DeliverInfoBean) _data;
        if (deliverInfoBean == null) {
            return;
        }
        //3种UI样式
        switch (deliverInfoBean.deliverStatus) {
            case DeliverInfoBean.DeliverStatus.BILLED:          // 2.已发单
            case DeliverInfoBean.DeliverStatus.INFORMED:        // 3.已通知司导
            case DeliverInfoBean.DeliverStatus.INFORMED_GUIDE:  // 6.已通知该司导
                countdownLayout(deliverInfoBean);
                break;
            case DeliverInfoBean.DeliverStatus.BEING_ARRANGED:  // 5.正在安排司导
            case DeliverInfoBean.DeliverStatus.COORDINATION:    // 7.为您协调司导
            case DeliverInfoBean.DeliverStatus.DELIVERING:      // 9.发单中
                loadingLayout(deliverInfoBean);
                break;
            case DeliverInfoBean.DeliverStatus.COMMITTED:       // 4.有司导表态  司导
                guideAvatarListLayout(deliverInfoBean);
                break;
        }
    }

    private void loadingLayout(DeliverInfoBean deliverInfoBean) {
        loadingView.setVisibility(View.VISIBLE);
        countdownLayout.setVisibility(View.GONE);
        avatarLayout.removeAllViews();
        avatarLayout.setVisibility(View.GONE);
        subtitleIV.setVisibility(View.VISIBLE);

        titleTV.setText(deliverInfoBean.deliverMessage);
        subtitleIV.setText(deliverInfoBean.deliverDetail);

    }

    private void countdownLayout(DeliverInfoBean deliverInfoBean) {
        loadingView.setVisibility(View.GONE);
        countdownLayout.setVisibility(View.VISIBLE);
        avatarLayout.removeAllViews();
        avatarLayout.setVisibility(View.GONE);
        subtitleIV.setVisibility(View.VISIBLE);

        countdownLayout.update(deliverInfoBean);
        titleTV.setText(deliverInfoBean.deliverMessage);
        subtitleIV.setText(deliverInfoBean.deliverDetail);
    }

    private void guideAvatarListLayout(DeliverInfoBean deliverInfoBean) {
        loadingView.setVisibility(View.GONE);
        countdownLayout.setVisibility(View.VISIBLE);
        avatarLayout.setVisibility(View.VISIBLE);
        subtitleIV.setVisibility(View.GONE);

        countdownLayout.update(deliverInfoBean);
        titleTV.setText(deliverInfoBean.deliverMessage);
        RequestAcceptGuide requestAcceptGuide = new RequestAcceptGuide(getContext(), orderNo, 10, 0);
        HttpRequestUtils.request(getContext(), requestAcceptGuide, this, false);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (loadingView != null) {
            loadingView.onStop();
        }
    }

    public void setOnCountdownEndListener(OrderDetailDeliverCountDownView.OnUpdateListener onUpdateListener) {
        countdownLayout.setOnUpdateListener(onUpdateListener);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        CanServiceGuideBean canServiceGuideBean = ((RequestAcceptGuide)request).getData();
        if (canServiceGuideBean == null) {
            return;
        }
        List<CanServiceGuideBean.GuidesBean> guidesList = canServiceGuideBean.getGuides();
        avatarLayout.removeAllViews();

        //TODO 每次刷新都重新new,当前刷新频率低,后续优化。
        int size = guidesList.size();
        int viewWidth = UIUtils.dip2px(114) + UIUtils.dip2px(50);
        boolean isShowMoreIV = true;
        j:for (int i = 0; i < size; i++) {
            viewWidth +=  UIUtils.dip2px(10) + UIUtils.dip2px(40);
            if (viewWidth > UIUtils.getScreenWidth()) {
                isShowMoreIV = true;
                break j;
            }
            CircleImageView circleImageView = getCircleImageView();
            Tools.showImage(circleImageView, guidesList.get(i).getAvatarS(), R.mipmap.journey_head_portrait);
            isShowMoreIV = false;
        }
        CircleImageView circleImageView = getCircleImageView();
        circleImageView.setBackgroundResource(R.mipmap.guide_avater_more);
        circleImageView.setVisibility(isShowMoreIV ? View.VISIBLE : View.GONE);

        if (!TextUtils.isEmpty(orderNo) && (orderType == 3 || orderType == 5 || orderType == 6)) {//接送次订单，隐藏表态司导列表，箭头隐藏，不可点击
            ImageView iconIV = new ImageView(getContext());
            iconIV.setImageResource(R.mipmap.personalcenter_right);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2px(20), LayoutParams.WRAP_CONTENT);
            params.rightMargin = UIUtils.dip2px(10);
            avatarLayout.addView(iconIV, params);

            avatarLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(getContext(), CanServiceGuideListActivity.class);
                        intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.order_detail_title_default));
                        intent.putExtra("orderNo", orderNo);
                        getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (errorHandler == null) {
            errorHandler = new ErrorHandler((Activity)getContext(), this);
        }
        errorHandler.onDataRequestError(errorInfo, request);
    }

    private CircleImageView getCircleImageView() {
        CircleImageView circleImageView = new CircleImageView(getContext());
        circleImageView.setBackgroundResource(R.mipmap.journey_head_portrait);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2px(40), UIUtils.dip2px(40));
        params.rightMargin = UIUtils.dip2px(10);
        avatarLayout.addView(circleImageView, params);
        return circleImageView;
    }
}
