package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.CharterSecondStepActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.OrderSelectCityActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleNewActivity;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.guideview.Component;
import com.hugboga.custom.widget.guideview.Guide;
import com.hugboga.custom.widget.guideview.GuideBuilder;
import com.hugboga.custom.widget.guideview.MutiComponent;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeCustomLayout extends LinearLayout{

    public static final String PARAMS_LAST_GUIDE_VERSION_NAME = "last_guide_version_name";
    private boolean hasMeasured = false;
    private boolean isShow = false;
    private Guide guide;

    public HomeCustomLayout(Context context) {
        this(context, null);
    }

    public HomeCustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFFFFFFF);

        View view = inflate(getContext(), R.layout.view_home_custom, this);
        ButterKnife.bind(this, view);

        setPreDrawListener();
    }

    @OnClick({R.id.home_custom_chartered_layout, R.id.home_custom_pickup_layout,
            R.id.home_custom_single_layout, R.id.home_custom_travelfund_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_custom_chartered_layout://包车
//                intentActivity(OrderSelectCityActivity.class, StatisticConstant.LAUNCH_DETAIL_R);
                intentActivity(CharterFirstStepActivity.class, StatisticConstant.LAUNCH_DETAIL_R);
                break;
            case R.id.home_custom_pickup_layout://中文接送机
                intentActivity(PickSendActivity.class, StatisticConstant.LAUNCH_J);
                break;
            case R.id.home_custom_single_layout://单次接送
                intentActivity(SingleNewActivity.class, StatisticConstant.LAUNCH_C);
                break;
            case R.id.home_custom_travelfund_layout:
                if (!UserEntity.getUser().isLogin(getContext())) {
                    CommonUtils.showToast(R.string.login_hint);
                    intentActivity(LoginActivity.class, "");
                    break;
                }
                intentActivity(TravelFundActivity.class, StatisticConstant.CLICK_TRAVELFOUND_SY);
                break;
        }
    }

    private void intentActivity(Class<?> cls, String eventId) {
        Intent intent = new Intent(getContext(), cls);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        getContext().startActivity(intent);
        if (!TextUtils.isEmpty(eventId)) {
            MobClickUtils.onEvent(eventId);
        }
    }

    public String getEventSource() {
        return "首页";
    }

    private void setPreDrawListener() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (hasMeasured == false) {
                    showGuideView();
                    hasMeasured = true;
                }
                return true;
            }
        });
    }

    private void showGuideView() {
        final String versionName = SharedPre.getString(PARAMS_LAST_GUIDE_VERSION_NAME, "");
        if (BuildConfig.VERSION_NAME.equals(versionName)) {
            return;
        }
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(findViewById(R.id.home_custom_chartered_layout))
                .setAlpha(150)
                .setHighTargetGraphStyle(Component.CIRCLE)
                .setHighTargetPadding(-5)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
                isShow = true;
            }

            @Override
            public void onDismiss() {
                SharedPre.setString(PARAMS_LAST_GUIDE_VERSION_NAME, BuildConfig.VERSION_NAME);
                isShow = false;
                EventBus.getDefault().post(new EventAction(EventType.SHOW_GIFT_DIALOG));
            }
        });
        builder.addComponent(new MutiComponent());
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show((Activity) getContext());
    }

    public boolean closeGuideView() {
        if (isShow && guide != null) {
            guide.dismiss();
            return true;
        } else {
            return false;
        }
    }
}
