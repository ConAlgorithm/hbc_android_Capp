package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/9/8.
 */
public class OrderDetailDeliverItemView extends LinearLayout implements HbcViewBehavior, HttpRequestListener {

    private static final int AVATAR_WIDTH = UIUtils.dip2px(30);
    private static final int AVATAR_MARGIN = UIUtils.dip2px(5);

    @BindView(R.id.deliver_item_layout)
    LinearLayout parrentLayout;

    @BindView(R.id.deliver_item_loading_view)
    ImageView loadingView;
    @BindView(R.id.deliver_item_countdown_view)
    OrderDetailDeliverCountDownView countdownLayout;

    @BindView(R.id.deliver_item_title_tv)
    TextView titleTV;

    @BindView(R.id.deliver_item_subtitle_tv)
    TextView subtitleIV;
    @BindView(R.id.deliver_item_guide_avatar_layout)
    LinearLayout avatarLayout;
    @BindView(R.id.deliver_item_arrow_iv)
    ImageView arrowIV;

    private String orderNo;
    private int orderType;
    private ErrorHandler errorHandler;
    private boolean isStop = false;

    public static final int FRE_INTERVAL = 5 * 1000;
    public static final int LATE_INTERVAL = 60 * 1000;

    private OrderDetailDeliverCountDownView.OnUpdateListener onUpdateListener;

    public OrderDetailDeliverItemView(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final View view = inflate(context, R.layout.view_deliver_item, this);
        ButterKnife.bind(view);
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
            case DeliverInfoBean.DeliverStatus.INFORMED:        // 3.已通知司导
            case DeliverInfoBean.DeliverStatus.INFORMED_GUIDE:  // 6.已通知该司导
                countdownLayout(deliverInfoBean);
                break;
            case DeliverInfoBean.DeliverStatus.BILLED:          // 2.已发单
            case DeliverInfoBean.DeliverStatus.BEING_ARRANGED:  // 5.正在安排司导
            case DeliverInfoBean.DeliverStatus.COORDINATION:    // 7.为您协调司导
                loadingLayout(deliverInfoBean);
                break;
            case DeliverInfoBean.DeliverStatus.DELIVERING:      // 9.发单中，需要定时刷新
                loadingLayout(deliverInfoBean);
                isStop = false;
                countdownHandler.sendEmptyMessageDelayed(deliverInfoBean.refreshCount, deliverInfoBean.refreshCount >= 0 ? FRE_INTERVAL : LATE_INTERVAL);
                break;
            case DeliverInfoBean.DeliverStatus.COMMITTED:       // 4.有司导表态  司导
                if (deliverInfoBean.isCanChoose()) {
                    guideAvatarListLayout(deliverInfoBean);
                } else {
                    countdownLayout(deliverInfoBean);
                }
                break;
        }
    }

    public void loadingLayout(DeliverInfoBean deliverInfoBean) {
        loadingView.setVisibility(View.VISIBLE);
        countdownLayout.setVisibility(View.GONE);
        avatarLayout.removeAllViews();
        avatarLayout.setVisibility(View.GONE);
        arrowIV.setVisibility(View.GONE);
        subtitleIV.setVisibility(View.VISIBLE);
        subtitleIV.setTextColor(0xFF929292);
        parrentLayout.setClickable(false);

        titleTV.setText(deliverInfoBean.deliverMessage);
        subtitleIV.setText(deliverInfoBean.deliverDetail);
    }

    private void countdownLayout(DeliverInfoBean deliverInfoBean) {
        loadingView.setVisibility(View.GONE);
        countdownLayout.setVisibility(View.VISIBLE);
        avatarLayout.removeAllViews();
        avatarLayout.setVisibility(View.GONE);
        arrowIV.setVisibility(View.GONE);
        subtitleIV.setVisibility(View.VISIBLE);
        subtitleIV.setTextColor(0xFF929292);
        parrentLayout.setClickable(false);

        countdownLayout.update(deliverInfoBean);
        titleTV.setText(deliverInfoBean.deliverMessage);
        subtitleIV.setText(deliverInfoBean.deliverDetail);
    }

    private void guideAvatarListLayout(DeliverInfoBean deliverInfoBean) {
        loadingView.setVisibility(View.GONE);
        countdownLayout.setVisibility(View.VISIBLE);

        countdownLayout.update(deliverInfoBean);
        titleTV.setText(deliverInfoBean.deliverMessage);

        if (TextUtils.isEmpty(deliverInfoBean.deliverDetail)) {
            subtitleIV.setVisibility(View.GONE);
        } else {
            subtitleIV.setVisibility(View.VISIBLE);
            subtitleIV.setTextColor(0xFF929292);
            subtitleIV.setText(deliverInfoBean.deliverDetail);
        }

        if (deliverInfoBean.isCanChoose()) {//是否可挑选司导
            subtitleIV.setTextColor(getContext().getResources().getColor(R.color.default_price_red));
            RequestAcceptGuide requestAcceptGuide = new RequestAcceptGuide(getContext(), orderNo, 10, 0);
            HttpRequestUtils.request(getContext(), requestAcceptGuide, this, false);
            avatarLayout.setVisibility(View.VISIBLE);
            arrowIV.setVisibility(View.VISIBLE);
            avatarLayout.setClickable(false);
            arrowIV.setClickable(false);
            StatisticClickEvent.click(StatisticConstant.LAUNCH_WAITG, "" + orderType);
        }
    }

    public void setOnCountdownEndListener(OrderDetailDeliverCountDownView.OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
        countdownLayout.setOnUpdateListener(onUpdateListener);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        ApiReportHelper.getInstance().addReport(request);
        CanServiceGuideBean canServiceGuideBean = ((RequestAcceptGuide)request).getData();
        if (canServiceGuideBean == null) {
            return;
        }
        List<CanServiceGuideBean.GuidesBean> guidesList = canServiceGuideBean.getGuides();
        avatarLayout.removeAllViews();

        //TODO 每次刷新都重新new,当前刷新频率低,后续优化。
        int size = guidesList.size();
        int viewWidth = UIUtils.dip2px(149) + AVATAR_MARGIN + AVATAR_WIDTH;;
        j:for (int i = 0; i < size; i++) {
            viewWidth +=  AVATAR_MARGIN + AVATAR_WIDTH;
            if (viewWidth > UIUtils.getScreenWidth()) {
                if (i < size - 1) {
                    CircleImageView moreImageView = getMoreImageView();
                    Tools.showImage(moreImageView, guidesList.get(i).getAvatarS(), R.mipmap.icon_avatar_guide);
                } else {
                    CircleImageView circleImageView = getCircleImageView();
                    Tools.showImage(circleImageView, guidesList.get(i).getAvatarS(), R.mipmap.icon_avatar_guide);
                }
                break j;
            }
            CircleImageView circleImageView = getCircleImageView();
            Tools.showImage(circleImageView, guidesList.get(i).getAvatarS(), R.mipmap.icon_avatar_guide);
        }

        if (!TextUtils.isEmpty(orderNo)) {
            parrentLayout.setClickable(true);
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
        circleImageView.setImageResource(R.mipmap.icon_avatar_guide);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AVATAR_WIDTH, AVATAR_WIDTH);
        params.rightMargin = AVATAR_MARGIN;
        avatarLayout.addView(circleImageView, params);
        return circleImageView;
    }

    private CircleImageView getMoreImageView() {
        RelativeLayout relativeLayout = new RelativeLayout(getContext());

        CircleImageView circleImageView = new CircleImageView(getContext());
        circleImageView.setImageResource(R.mipmap.icon_avatar_guide);
        relativeLayout.addView(circleImageView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText("more");
        textView.setTextSize(10);
        textView.setTextColor(0xFFFFFFFF);
        textView.setBackgroundResource(R.drawable.bg_oval_shade_black);
        relativeLayout.addView(textView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AVATAR_WIDTH, AVATAR_WIDTH);
        params.rightMargin = AVATAR_MARGIN;
        avatarLayout.addView(relativeLayout, params);
        return circleImageView;
    }

    @OnClick({R.id.deliver_item_layout})
    public void intentServiceGuideList() {
        if (orderNo == null || orderType == 0) {
            return;
        }
        Intent intent = new Intent(getContext(), CanServiceGuideListActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.order_detail_title_default));
        intent.putExtra(Constants.PARAMS_ORDER_NO, orderNo);
        intent.putExtra(Constants.PARAMS_ORDER_TYPE, orderType);
        getContext().startActivity(intent);
        StatisticClickEvent.showGuidesClick(StatisticConstant.LAUNCH_WAITG, getContext().getString(R.string.order_detail_title_default), orderType);
    }

    private Handler countdownHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isStop) {
                return;
            }
            if (onUpdateListener != null) {
                onUpdateListener.onUpdate(false);
            }
        }
    };

    public void stop() {
        isStop = true;
        if (countdownLayout != null) {
            countdownLayout.stop();
        }
    }
}
