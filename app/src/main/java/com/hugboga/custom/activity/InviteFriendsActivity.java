package com.hugboga.custom.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.TravelFundAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.ShareUrls;
import com.hugboga.custom.data.request.RequestGetInvitationCode;
import com.hugboga.custom.data.request.RequestInvitationIntroduction;
import com.hugboga.custom.data.request.RequestTravelFundLogsInvitation;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created on 16/8/4.
 */

public class InviteFriendsActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.invite_listview)
    ZListView listView;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    private TextView headerCodeTV, headerSucceedTV;
    private LinearLayout footerItemsLayout;

    private TravelFundAdapter adapter;

    protected void initHeader() {
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerTitle.setText(getString(R.string.invite_friends_title));
        listView.setonLoadListener(onLoadListener);
        LayoutInflater inflater = LayoutInflater.from(activity);

        View headerView = inflater.inflate(R.layout.header_invite_friends, null);
        int headerExplainImgHeight = (int) ((655 / 720.0) * UIUtils.getScreenWidth());//顶部图片比例655*720
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, headerExplainImgHeight);
        headerView.findViewById(R.id.header_invite_friends_explain_iv).setLayoutParams(imgParams);
        headerCodeTV = (TextView) headerView.findViewById(R.id.header_invite_friends_promotion_code_tv);
        headerSucceedTV = (TextView) headerView.findViewById(R.id.header_invite_friends_succeed_tv);
        headerView.findViewById(R.id.header_invite_friends_share_tv).setOnClickListener(this);
        headerView.findViewById(R.id.header_invite_friends_copy_tv).setOnClickListener(this);
        listView.addHeaderView(headerView);

        View footerView = inflater.inflate(R.layout.footer_invite_friends, null);
        footerItemsLayout = (LinearLayout) footerView.findViewById(R.id.footer_invite_layout);
        listView.addFooterView(footerView);

        RequestGetInvitationCode codeRequest = new RequestGetInvitationCode(activity);
        requestData(codeRequest);
        RequestInvitationIntroduction introductionRequest = new RequestInvitationIntroduction(activity);
        requestData(introductionRequest);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_invite_friends);
        ButterKnife.bind(this);
        initHeader();
        requestData();
    }

    private Callback.Cancelable runData(int pageIndex) {
        RequestTravelFundLogsInvitation request = new RequestTravelFundLogsInvitation(activity, pageIndex);
        return requestData(request);
    }

    ZListView.OnLoadListener onLoadListener = new ZListView.OnLoadListener() {
        @Override
        public void onLoad() {
            if (adapter.getCount() > 0) {
                runData(adapter == null ? 0 : adapter.getCount());
            }
        }
    };

    protected Callback.Cancelable requestData() {
        if (adapter != null) {
            adapter = null;
        }
        return runData(0);
    }


    @Override
    public void onDataRequestSucceed(final BaseRequest _request) {
        if (_request instanceof RequestTravelFundLogsInvitation) {
            RequestTravelFundLogsInvitation request = (RequestTravelFundLogsInvitation) _request;
            TravelFundData travelFundData = request.getData();
            if (travelFundData != null) {
                ArrayList<TravelFundData.TravelFundBean> logs = travelFundData.getLogs();
                if (adapter == null) {
                    adapter = new TravelFundAdapter(activity);
                    listView.setAdapter(adapter);
                }
                adapter.addList(logs);
                if (logs != null && logs.size() < Constants.DEFAULT_PAGESIZE) {
                    listView.onLoadCompleteNone();
                }
            } else {
                listView.onLoadComplete();
            }
            setSucceedInvitedTextStyle(travelFundData.getInvitedUserCount(), travelFundData.getInvitationAmount());
            listView.onRefreshComplete();
        } else if (_request instanceof RequestGetInvitationCode) {
            RequestGetInvitationCode codeRequest = (RequestGetInvitationCode) _request;
            headerCodeTV.setText(codeRequest.getData());
        } else if (_request instanceof RequestInvitationIntroduction) {
            RequestInvitationIntroduction introductionRequest = (RequestInvitationIntroduction) _request;
            final String[] itemData = introductionRequest.getData();
            if (itemData != null) {
                footerItemsLayout.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(activity);
                for (int i = 0; i < itemData.length; i++) {
                    View footerItem = inflater.inflate(R.layout.item_travelfund_footer, null);
                    TextView serialTV = (TextView) footerItem.findViewById(R.id.item_travelfund_footer_serial_tv);
                    TextView contentTV = (TextView) footerItem.findViewById(R.id.item_travelfund_footer_content_tv);
                    serialTV.setText(String.valueOf(i + 1));
                    contentTV.setText(itemData[i]);
                    footerItemsLayout.addView(footerItem);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_invite_friends_share_tv:
                if (TextUtils.isEmpty(headerCodeTV.getText().toString())) {
                    return;
                }
                String shareUrl = ShareUrls.getShareThirtyCouponUrl(UserEntity.getUser().getAvatar(activity),
                        UserEntity.getUser().getUserName(activity),
                        headerCodeTV.getText().toString());
                CommonUtils.shareDialog(activity, R.mipmap.share_coupon,
                        getString(R.string.invite_friends_share_title), getString(R.string.invite_friends_share_content), shareUrl);
                break;
            case R.id.header_invite_friends_copy_tv:
                if (!TextUtils.isEmpty(headerCodeTV.getText())) {
                    ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(headerCodeTV.getText());
                    CommonUtils.showToast(R.string.invite_friends_copy_succeed);
                }
                break;
        }
    }

    private void setSucceedInvitedTextStyle(String userCount, String amount) {
        String succeedInvitedString = getString(R.string.invite_friends_succeed, userCount, amount);
        int userStart = 5;
        int userEnd = userStart + userCount.length();
        int amountStart = 15 + userCount.length();
        int amountEnd = amountStart + amount.length();
        SpannableString sp = new SpannableString(succeedInvitedString);

        sp.setSpan(new RelativeSizeSpan(1.8f), userStart, userEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.invite_friends_basic)), userStart, userEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StyleSpan(Typeface.BOLD), userStart, userEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        sp.setSpan(new RelativeSizeSpan(1.8f), amountStart, amountEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.invite_friends_basic)), amountStart, amountEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StyleSpan(Typeface.BOLD), amountStart, amountEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        headerSucceedTV.setText(sp);
    }
}

