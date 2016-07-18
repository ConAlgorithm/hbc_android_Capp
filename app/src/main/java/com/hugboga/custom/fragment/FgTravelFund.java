package com.hugboga.custom.fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.TravelFundAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.request.RequestTravelFundIntroduction;
import com.hugboga.custom.data.request.TrequestTravelFundLogs;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/5/27.
 */
@ContentView(R.layout.fg_travel_fund)
public class FgTravelFund extends BaseFragment {

    @ViewInject(R.id.tracel_fund_listview)
    private ZListView listView;
    private LinearLayout footerItemsLayout;
    private TextView amountTV, effectiveDateTV;
    private FrameLayout titleLayout;

    private TravelFundAdapter adapter;

    @Override
    protected void initHeader() {
        fgTitle.setText(getString(R.string.travel_fund_title));
        listView.setonLoadListener(onLoadListener);
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View headerView = inflater.inflate(R.layout.header_travel_fund, null);
        int headerExplainImgHeight = (int)((773 / 1080.0) * UIUtils.getScreenWidth());//顶部图片比例773*1080
        LinearLayout.LayoutParams imgParams = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, headerExplainImgHeight);
        headerView.findViewById(R.id.header_travel_fund_explain_iv).setLayoutParams(imgParams);
        amountTV = (TextView) headerView.findViewById(R.id.header_travel_fund_amount_tv);
        effectiveDateTV = (TextView) headerView.findViewById(R.id.header_travel_fund_effectivedate_tv);
        titleLayout = (FrameLayout) headerView.findViewById(R.id.header_travel_fund_title_layout);
        listView.addHeaderView(headerView);

        View footerView = inflater.inflate(R.layout.footer_invite_friends, null);
        footerItemsLayout = (LinearLayout) footerView.findViewById(R.id.footer_invite_layout);
        listView.addFooterView(footerView);

        RequestTravelFundIntroduction introductionRequest = new RequestTravelFundIntroduction(getActivity());
        requestData(introductionRequest);
    }

    @Override
    protected void initView() {

    }

    private Callback.Cancelable runData(int pageIndex) {
        TrequestTravelFundLogs request = new TrequestTravelFundLogs(getActivity(), pageIndex);
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

    @Override
    protected Callback.Cancelable requestData() {
        if (adapter != null) {
            adapter = null;
        }
        return runData(0);
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onDataRequestSucceed(final BaseRequest _request) {
        if (_request instanceof TrequestTravelFundLogs) {
            TrequestTravelFundLogs request = (TrequestTravelFundLogs) _request;
            TravelFundData travelFundData = request.getData();
            if (travelFundData != null) {
                ArrayList<TravelFundData.TravelFundBean> listData = travelFundData.getListData();
                if (adapter == null) {
                    adapter = new TravelFundAdapter(getActivity());
                    adapter.setFgTravelFund(true);
                    listView.setAdapter(adapter);
                    if (listData == null || listData.size() <= 0) {
                        titleLayout.setVisibility(View.GONE);
                    }
                }
                adapter.addList(listData);
                if (listData != null && listData.size() < Constants.DEFAULT_PAGESIZE) {
                    listView.onLoadCompleteNone();
                }
            } else {
                listView.onLoadComplete();
            }

            String fundAmountString = getString(R.string.travel_fund_price, travelFundData.getFundAmount());
            int start = 5;
            int end = start + travelFundData.getFundAmount().length();
            SpannableString sp = new SpannableString(fundAmountString);
            sp.setSpan(new RelativeSizeSpan(1.6f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            amountTV.setText(sp);

            if (travelFundData.getFundAmountInt() <= 0) {
                effectiveDateTV.setVisibility(View.GONE);
            } else {
                effectiveDateTV.setVisibility(View.VISIBLE);
                effectiveDateTV.setText(getString(R.string.travel_fund_validity, travelFundData.getEffectiveDate()));
            }

            listView.onRefreshComplete();
        } else if (_request instanceof RequestTravelFundIntroduction) {
            RequestTravelFundIntroduction introductionRequest = (RequestTravelFundIntroduction) _request;
            final String[] itemData = introductionRequest.getData();
            if (itemData != null) {
                footerItemsLayout.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                for (int i = 0; i < itemData.length; i++) {
                    View footerItem = inflater.inflate(R.layout.item_travelfund_footer, null);
                    TextView serialTV = (TextView) footerItem.findViewById(R.id.item_travelfund_footer_serial_tv);
                    serialTV.setBackgroundResource(R.drawable.item_travel_fund_serial_bg);
                    TextView contentTV = (TextView) footerItem.findViewById(R.id.item_travelfund_footer_content_tv);
                    serialTV.setText(String.valueOf(i + 1));
                    contentTV.setText(itemData[i]);
                    footerItemsLayout.addView(footerItem);
                }
            }
        }
    }
}
