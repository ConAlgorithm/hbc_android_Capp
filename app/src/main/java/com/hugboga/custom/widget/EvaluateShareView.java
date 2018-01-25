package com.hugboga.custom.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.EvaluateNewActivity;
import com.hugboga.custom.data.bean.AppraisementBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.event.EventEvaluateShare;
import com.hugboga.custom.utils.CommonUtils;

import org.json.JSONObject;

/**
 * Created by qingcha on 16/7/15.
 */
public class EvaluateShareView extends LinearLayout implements View.OnClickListener, HbcViewBehavior {

    private ImageView drawerIV;
    private TextView drawerTV, descriptionTV, rulesTV;

    private OrderBean orderBean;
    private AppraisementBean appraisement;
    private String orderNo;

    private boolean isShow = false;

    public EvaluateShareView(Context context) {
        this(context, null);
    }

    public EvaluateShareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_evaluate_share, this);

        drawerIV = (ImageView) findViewById(R.id.evaluate_share_drawer_iv);
        drawerTV = (TextView) findViewById(R.id.evaluate_share_drawer_tv);
        descriptionTV = (TextView) findViewById(R.id.evaluate_share_description_tv);
        rulesTV = (TextView) findViewById(R.id.evaluate_share_rules_tv);

        findViewById(R.id.evaluate_share_wechat_tv).setOnClickListener(this);
        findViewById(R.id.evaluate_share_moments_tv).setOnClickListener(this);
        findViewById(R.id.evaluate_share_drawer_layout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (appraisement == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.evaluate_share_wechat_tv:
                share(1);
                break;
            case R.id.evaluate_share_moments_tv:
                share(2);
                break;
            case R.id.evaluate_share_drawer_layout:
                toggle(isShow);
                break;
        }
    }

    private void share(int type) {
        String source = null;
        if (getContext() instanceof EvaluateNewActivity) {
            source = ((EvaluateNewActivity) getContext()).getEventSource();
            MobClickUtils.onEvent(new EventEvaluateShare(orderBean.orderType, source, "" + type));
        }
        String shareUrl = CommonUtils.getBaseUrl(appraisement.wechatShareUrl) + "orderNo=" + orderNo + "&userId=" + UserEntity.getUser().getUserId(getContext());
        CommonUtils.shareOptimize(getContext(), type
                , appraisement.wechatShareHeadSrc
                , appraisement.wechatShareTitle
                , appraisement.wechatShareContent
                , shareUrl, source);
        setSensorsShare(type);
    }

    public void toggle(boolean _isShow) {
        if (_isShow) {
            drawerIV.setBackgroundResource(R.mipmap.share_withdraw);
            drawerTV.setText(getContext().getString(R.string.evaluate_share_detail));
            descriptionTV.setText(getContext().getString(R.string.evaluate_share_description_1, appraisement.commentTipParam1));
            rulesTV.setVisibility(View.GONE);
        } else {
            drawerIV.setBackgroundResource(R.mipmap.share_unfold);
            drawerTV.setText(getContext().getString(R.string.evaluate_share_pack_up));
            String description = getContext().getString(R.string.evaluate_share_description_1, appraisement.commentTipParam1) + getContext().getString(R.string.evaluate_share_description_2);
            descriptionTV.setText(description);
            setRulesTextStyle(appraisement.commentTipParam2, appraisement.commentTipParam3);
            rulesTV.setVisibility(View.VISIBLE);
        }
        isShow = !_isShow;
    }

    private void setRulesTextStyle(String price, String percentage) {
        if (!TextUtils.isEmpty(rulesTV.getText())) {
            return;
        }
        String succeedInvitedString = getContext().getString(R.string.evaluate_share_rules_price, price, percentage);
        int priceStart = 0;
        int priceEnd = price.length();
        int percentageStart = 3 + priceEnd;
        int percentageEnd = percentageStart + percentage.length();
        SpannableString sp = new SpannableString(succeedInvitedString);
        sp.setSpan(new ForegroundColorSpan(0xFFF44437), priceStart, priceEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(0xFFF44437), percentageStart, percentageEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rulesTV.setText(sp);
    }

    @Override
    public void update(Object _data) {
        if (_data instanceof OrderBean) {
            orderBean = (OrderBean) _data;
            if (orderBean != null && orderBean.appraisement != null) {
                appraisement = orderBean.appraisement;
                orderNo = orderBean.orderNo;
                toggle(true);
            } else {
                setVisibility(View.GONE);
            }
        }
    }

    public void setSensorsShare(Integer type) {
        if (null == orderBean) {
            return;
        }
        try {
            JSONObject properties = new JSONObject();
            String skuType = "";
            switch (orderBean.orderType) {
                case 1:
                    skuType = "接机";
                    break;
                case 2:
                    skuType = "送机";
                    break;
                case 3:
                    skuType = "按天包车游";
                    break;
                case 4:
                    skuType = "单次接送";
                    break;
                case 5:
                    skuType = "固定线路";
                    break;
                case 6:
                    skuType = "推荐线路";
                    break;
            }
            properties.put("hbc_share_channelId", type == 1 ? "微信好友" : "朋友圈");
            properties.put("hbc_share_content", skuType);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
