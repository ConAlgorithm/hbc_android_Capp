package com.hugboga.custom.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/8/4.
 */
public class ShareGuidesActivity extends BaseActivity {

    //@BindView(R.id.share_evaluate_description_tv1)
    //TextView descriptionTV1;
    @BindView(R.id.share_evaluate_description_tv2)
    TextView descriptionTV2;
    @BindView(R.id.share_evaluate_collected_tv)
    TextView collectedTV;
    @BindView(R.id.share_evaluate_description_tv1)
    TextView des;
    @BindView(R.id.share_evaluate_icon_iv)
    ImageView evaluateIcon;
    private Params params;
    private boolean shareSucceed = false;
    private boolean isReturnMoney = true;

    public static class Params implements Serializable {
        public EvaluateData evaluateData;
        public String orderNo;
        public int orderType;
        public int totalScore;
        public int guideAgencyType;
        public boolean isReturnMoney;
        public String guideId;
        public String goodsNo;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_share_evaluate;
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
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventAction(EventType.REFRESH_TRAVEL_DATA));
                finish();
            }
        });
        this.isReturnMoney = params.isReturnMoney;
        if (params == null || params.evaluateData == null) {
            finish();
        }
        if (params.totalScore > 3) {
            collectedTV.setVisibility(View.VISIBLE);
            //descriptionTV1.setText(getString(R.string.share_evaluate_description_1) + getString(R.string.share_evaluate_description_2));
        } else {
            collectedTV.setVisibility(View.INVISIBLE);
            //descriptionTV1.setText(getString(R.string.share_evaluate_description_1) + getString(R.string.share_evaluate_description_4));
        }

        String commentTipParam = params.evaluateData.commentTipParam1;
        if (TextUtils.isEmpty(commentTipParam)) {
            descriptionTV2.setVisibility(View.GONE);
        }
        String description = getString(R.string.share_evaluate_description_3, commentTipParam);
        SpannableString msp = new SpannableString(description);
        msp.setSpan(new ForegroundColorSpan(0xFFFF6633), 11, 11 + commentTipParam.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(0xFFFF6633), description.length() - 4, description.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        descriptionTV2.setText(msp);
        if (!isReturnMoney) {
            des.setVisibility(View.GONE);
            evaluateIcon.setBackgroundResource(R.mipmap.evaluate_successful_picture);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) evaluateIcon.getLayoutParams();
            lp.setMargins(0, UIUtils.dip2px(88), 0, 0);
            evaluateIcon.setLayoutParams(lp);
        }
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
     */
    private void setShare(int type) {
        final EvaluateData evaluateData = params.evaluateData;
        if (evaluateData == null || TextUtils.isEmpty(evaluateData.wechatShareUrl)) {
            return;
        }
        String shareUrl = CommonUtils.getBaseUrl(params.evaluateData.wechatShareUrl) + "orderNo=" + params.orderNo + "&userId=" + UserEntity.getUser().getUserId(this);
        CommonUtils.shareOptimize(this, type
                , evaluateData.wechatShareHeadSrc
                , evaluateData.wechatShareTitle
                , evaluateData.wechatShareContent
                , shareUrl, null);
        MobClickUtils.onEvent(new EventEvaluateShare(params.orderType, getEventSource(), "" + type));
        SensorsUtils.setSensorsShareEvent(type == 1 ? "微信好友" : "朋友圈", "评价", params.goodsNo, params.guideId);
    }

    @Override
    public String getEventSource() {
        return "评价成功页";
    }
}
