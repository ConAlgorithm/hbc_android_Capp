package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.ShareUrls;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestGetInvitationCode;
import com.hugboga.custom.data.request.RequestTravelFundLogs;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/8/4.TravelFund
 */
public class TravelFundActivity extends BaseActivity {

    @Bind(R.id.travel_fund_residue_price_tv)
    TextView residuePriceTV;
    @Bind(R.id.travel_fund_validity_tv)
    TextView validityTV;
    @Bind(R.id.travel_fund_activity_price_tv)
    TextView activityPriceTV;
    @Bind(R.id.travel_fund_description_tv)
    TextView descriptionTV;

    @Bind(R.id.tracel_fund_header)
    RelativeLayout titlerBar;

    private String invitationCode;
    private TravelFundData travelFundData;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_travel_fund);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initView() {
        initTitleBar();
        sendRequest();
        setFundAmount("0");
        setActivityPrice("600");
    }

    private void initTitleBar() {
        initDefaultTitleBar();
        titlerBar.setBackgroundColor(0x00000000);
        fgTitle.setText(getString(R.string.travel_fund_title));
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setText(getString(R.string.travel_fund_rule));
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TravelFundActivity.this, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_RAVEL_FUND_RULE);
                startActivity(intent);
            }
        });
    }

    private void sendRequest() {
        RequestTravelFundLogs request = new RequestTravelFundLogs(activity, 0);
        requestData(request);
        RequestGetInvitationCode codeRequest = new RequestGetInvitationCode(activity);
        requestData(codeRequest);
    }

    @Override
    public void onDataRequestSucceed(final BaseRequest _request) {
        if (_request instanceof RequestTravelFundLogs) {
            RequestTravelFundLogs request = (RequestTravelFundLogs) _request;
            travelFundData = request.getData();
            if (travelFundData == null) {
                return;
            }

            if (travelFundData.getFundAmountInt() <= 0) {
                validityTV.setVisibility(View.INVISIBLE);
            } else {
                validityTV.setVisibility(View.VISIBLE);
                validityTV.setText(getString(R.string.travel_fund_validity, travelFundData.getEffectiveDate()));
            }

            setFundAmount(travelFundData.getFundAmount());

            TravelFundData.RewardFields rewardFields = travelFundData.getRewardFields();
            if (rewardFields != null) {
                setActivityPrice(rewardFields.getCouponAmount());
                descriptionTV.setText(getString(R.string.travel_fund_description, rewardFields.getRewardAmountPerOrder(), rewardFields.getRewardRatePerOrder()));
            }
        } else if (_request instanceof RequestGetInvitationCode) {
            RequestGetInvitationCode codeRequest = (RequestGetInvitationCode) _request;
            invitationCode = codeRequest.getData();
        }
    }

    @OnClick({R.id.travel_fund_invite_record_tv, R.id.travel_fund_use_record_tv, R.id.travel_fund_invite_tv})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.travel_fund_invite_record_tv://邀请记录
                intent = new Intent(this, TravelFundRecordActivity.class);
                intent.putExtra(Constants.PARAMS_TYPE, TravelFundRecordActivity.TYPE_INVITE_FRIENDS);
                startActivity(intent);
                break;
            case R.id.travel_fund_use_record_tv://使用明细
                intent = new Intent(this, TravelFundRecordActivity.class);
                intent.putExtra(Constants.PARAMS_TYPE, TravelFundRecordActivity.TYPE_USE_Bill);
                startActivity(intent);
                break;
            case R.id.travel_fund_invite_tv://立即邀请
                if (TextUtils.isEmpty(invitationCode) || travelFundData == null || travelFundData.getRewardFields() == null) {
                    break;
                }
                String shareUrl = ShareUrls.getShareThirtyCouponUrl(UserEntity.getUser().getAvatar(this),
                        UserEntity.getUser().getUserName(this),
                        invitationCode);
                CommonUtils.shareDialog(activity, R.mipmap.share_coupon
                        , getString(R.string.invite_friends_share_title, travelFundData.getRewardFields().getCouponAmount())
                        , getString(R.string.invite_friends_share_content), shareUrl);
                break;
            default:
                break;
        }
    }

    private void setFundAmount(String fundAmount) {
        String fundAmountString = getString(R.string.sign_rmb) + fundAmount;
        int start = 0;
        int end = getString(R.string.sign_rmb).length() + start;
        SpannableString sp = new SpannableString(fundAmountString);
        sp.setSpan(new RelativeSizeSpan(0.5f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(0xFFFFFFFF), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        residuePriceTV.setText(sp);
    }

    private void setActivityPrice(String activityPrice) {
        String activityPriceString = activityPrice + getString(R.string.yuan);
        int start = activityPrice.length();
        int end = activityPriceString.length();
        SpannableString spannableString = new SpannableString(activityPriceString);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        activityPriceTV.setText(spannableString);
    }
}
