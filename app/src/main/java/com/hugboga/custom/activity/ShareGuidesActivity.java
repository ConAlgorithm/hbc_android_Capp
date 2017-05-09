package com.hugboga.custom.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.EvaluateData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.event.EventEvaluateShare;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/8/4.
 */
public class ShareGuidesActivity extends BaseActivity{

    @Bind(R.id.share_evaluate_description_tv1)
    TextView descriptionTV1;
    @Bind(R.id.share_evaluate_description_tv2)
    TextView descriptionTV2;
    @Bind(R.id.share_evaluate_collected_tv)
    TextView collectedTV;

    private Params params;
    private boolean shareSucceed = false;

    public static class Params implements Serializable {
        public EvaluateData evaluateData;
        public String orderNo;
        public int orderType;
        public int totalScore;
        public int guideAgencyType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        setContentView(R.layout.activity_share_evaluate);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shareSucceed) {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    private void initView() {
        initDefaultTitleBar();
        shareSucceed = false;
        fgTitle.setText(getString(R.string.share_evaluate_title));

        if (params == null || params.evaluateData == null) {
            finish();
        }
        if (params.totalScore > 3) {
            collectedTV.setVisibility(params.guideAgencyType != 3 ? View.VISIBLE : View.INVISIBLE); //地接社订单不显示
            descriptionTV1.setText(getString(R.string.share_evaluate_description_1) + getString(R.string.share_evaluate_description_2));
        } else {
            collectedTV.setVisibility(View.INVISIBLE);
            descriptionTV1.setText(getString(R.string.share_evaluate_description_1) + getString(R.string.share_evaluate_description_4));
        }

        String commentTipParam = params.evaluateData.commentTipParam1;
        if (TextUtils.isEmpty(commentTipParam)) {
            descriptionTV2.setVisibility(View.GONE);
        }
        String description = getString(R.string.share_evaluate_description_3, commentTipParam);
        SpannableString msp = new SpannableString(description);
        msp.setSpan(new ForegroundColorSpan(0xFFFF6633), 4, 4 + commentTipParam.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(0xFFFF6633), description.length() - 4, description.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        descriptionTV2.setText(msp);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        if (action.type == EventType.WECHAT_SHARE_SUCCEED) {
            shareSucceed = true;
        }
    }

    @OnClick({R.id.share_evaluate_wechat_layout, R.id.share_evaluate_moments_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_evaluate_wechat_layout:
                    setShare(1);
                    break;
            case R.id.share_evaluate_moments_layout:
                    setShare(2);
                    break;
        }
    }

    /**
     * 1:好友,2:朋友圈；
     * */
    private void setShare(int type) {
        final EvaluateData evaluateData = params.evaluateData;
        if (evaluateData == null || TextUtils.isEmpty(evaluateData.wechatShareUrl)) {
            return;
        }
        String shareUrl = CommonUtils.getBaseUrl(params.evaluateData.wechatShareUrl) + "orderNo=" +  params.orderNo + "&userId=" + UserEntity.getUser().getUserId(this);
        WXShareUtils wxShareUtils = WXShareUtils.getInstance(this);
        wxShareUtils.share(type
                , evaluateData.wechatShareHeadSrc
                , evaluateData.wechatShareTitle
                , evaluateData.wechatShareContent
                , shareUrl);
        MobClickUtils.onEvent(new EventEvaluateShare(params.orderType, getEventSource(), "" + type));
        SensorsUtils.setSensorsShareEvent(type == 1 ? "微信好友" : "朋友圈", ShareGuidesActivity.class.getSimpleName());
    }

    @Override
    public String getEventSource() {
        return "评价成功页";
    }
}
