package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 27/12/2017.
 */

public class OrderDetailFxJourneyView extends LinearLayout implements HbcViewBehavior, View.OnClickListener{

    @BindView(R.id.order_detail_fxjourney_name_tv)
    TextView nameTV;
    @BindView(R.id.order_detail_fxjourney_subtitle_tv)
    TextView subtitleTV;
    @BindView(R.id.order_detail_fxjourney_city_tv)
    TextView cityTV;

    private OrderBean.FxJourneyInfoBean fxJourneyInfo;

    public OrderDetailFxJourneyView(Context context) {
        this(context, null);
    }

    public OrderDetailFxJourneyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_order_detail_fxjourney, this);
        ButterKnife.bind(this);
    }

    @Override
    public void update(Object _data) {
        if (!(_data instanceof OrderBean)) {
            setVisibility(View.GONE);
            return;
        }
        OrderBean data = (OrderBean) _data;
        if (data.fxJourneyInfo != null) {
            setVisibility(View.VISIBLE);
            fxJourneyInfo = data.fxJourneyInfo;
            subtitleTV.setText(fxJourneyInfo.journeyTitle);
            if (!TextUtils.isEmpty(fxJourneyInfo.guideName)) {
                String guideName = getContext().getResources().getString(R.string.order_detail_fxjourney_guidename, fxJourneyInfo.guideName);
                int startIndex = guideName.indexOf(fxJourneyInfo.guideName);
                int endIndex = startIndex + fxJourneyInfo.guideName.length();
                SpannableString spannableString = new SpannableString(guideName);
                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.default_black)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                nameTV.setText(spannableString);
            }
            setOnClickListener(this);
        } else {
            setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (fxJourneyInfo == null || TextUtils.isEmpty(fxJourneyInfo.journeyUrl)) {
            return;
        }
        Intent intent = new Intent(getContext(), WebInfoActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, fxJourneyInfo.journeyUrl);
        intent.putExtra(Constants.PARAMS_SOURCE, "订单详情");
        getContext().startActivity(intent);
    }
}
