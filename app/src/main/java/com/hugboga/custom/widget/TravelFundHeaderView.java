package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TravelFundHeaderView extends LinearLayout implements HbcViewBehavior {

    @BindView(R.id.travel_fund_residue_price_tv)
    TextView residuePriceTV;
    @BindView(R.id.record_invited_incentive_price_tv)
    TextView incentivePriceTV;
    @BindView(R.id.record_invited_used_price_tv)
    TextView usedPriceTV;
    @BindView(R.id.travel_fund_invite_tv)
    ImageView travelFundInviteTV;
    @BindView(R.id.record_invited_incentive_record_tv)
    TextView incentiveRecordTV;
    @BindView(R.id.record_invited_used_record_tv)
    TextView usedRecordTV;
    @BindView(R.id.record_invited_incentive_record_line)
    View incentiveRecordLine;
    @BindView(R.id.record_invited_used_record_line)
    View usedRecordLine;

    @BindView(R.id.travel_fund_empty_iv)
    ImageView emptyIV;
    @BindView(R.id.travel_fund_empty_tv)
    TextView emptyTV;
    @BindView(R.id.travel_fund_empty_get_iv)
    ImageView emptyGetIV;
    @BindView(R.id.travel_fund_empty_layout)
    LinearLayout emptyLayout;

    private OnSwitchRecordListener onSwitchRecordListener;
    private boolean isUsed;

    public TravelFundHeaderView(Context context) {
        this(context, null);
    }

    public TravelFundHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_travel_fund_header, this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.travel_fund_invite_tv, R.id.travel_fund_empty_get_iv})
    public void onClickInvite() {//邀请就送
        Intent intent = new Intent(getContext(), WebInfoActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, "https://www.baidu.com/");
        getContext().startActivity(intent);
    }

    @OnClick({R.id.record_invited_incentive_record_layout, R.id.record_invited_used_record_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.record_invited_incentive_record_layout://奖励基金
                switchRecord(false);
                break;
            case R.id.record_invited_used_record_layout://使用明细
                switchRecord(true);
                break;
        }
    }

    private void switchRecord(boolean isUsed) {
        this.isUsed = isUsed;

        incentiveRecordTV.setTextColor(isUsed ? getContext().getResources().getColor(R.color.default_black) : 0xFFFFAB6E);
        incentiveRecordTV.setTextSize(isUsed ? 14 : 16);
        incentiveRecordTV.setPadding(0, 0, 0, UIUtils.dip2px(isUsed ? 3 : 0));
        incentiveRecordLine.setVisibility(isUsed ? View.GONE : View.VISIBLE);

        usedRecordTV.setTextColor(!isUsed ? getContext().getResources().getColor(R.color.default_black) : 0xFFFFAB6E);
        usedRecordTV.setTextSize(!isUsed ? 14 : 16);
        usedRecordTV.setPadding(0, 0, 0, UIUtils.dip2px(!isUsed ? 3 : 0));
        usedRecordLine.setVisibility(!isUsed ? View.GONE : View.VISIBLE);

        if (onSwitchRecordListener != null) {
            onSwitchRecordListener.onSwitchRecord(isUsed);
        }
    }

    @Override
    public void update(Object _data) {
        TravelFundData travelFundData = (TravelFundData) _data;
        residuePriceTV.setText(CommonUtils.doubleTrans(travelFundData.availableBalanceAmount));//基金余额
        incentivePriceTV.setText(CommonUtils.doubleTrans(travelFundData.totalIncomeAmount));//累计奖励
        usedPriceTV.setText(CommonUtils.doubleTrans(travelFundData.totalExpenseAmount));//累计使用
        travelFundInviteTV.setVisibility(View.VISIBLE);
    }

    public interface OnSwitchRecordListener {
        public void onSwitchRecord(boolean isUsed);
    }

    public void setOnSwitchRecordListener(OnSwitchRecordListener onSwitchRecordListener) {
        this.onSwitchRecordListener = onSwitchRecordListener;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void showEmptyView() {
        emptyLayout.setVisibility(View.VISIBLE);
        emptyIV.setBackgroundResource(isUsed ? R.mipmap.travelfund_used_empty : R.mipmap.travelfund_empty);
        emptyTV.setText(getContext().getResources().getString(isUsed ? R.string.travel_fund_used_empty_hint : R.string.travel_fund_incentive_empty_hint));
        emptyGetIV.setVisibility(isUsed ? GONE : VISIBLE);
    }

    public void hideEmptyView() {
        emptyLayout.setVisibility(View.GONE);
    }
}
