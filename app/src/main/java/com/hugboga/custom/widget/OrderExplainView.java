package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.OrderUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/12/17.
 */
public class OrderExplainView extends LinearLayout{

    @BindView(R.id.order_detail_terms_tv)
    TextView termsTV;
    @BindView(R.id.order_detail_explain_tv)
    TextView explainTV;

    public OrderExplainView(Context context) {
        this(context, null);
    }

    public OrderExplainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_explain, this);
        ButterKnife.bind(view);
    }

    public void setTermsTextViewVisibility(String source, int visibility) {
        termsTV.setVisibility(visibility);
        OrderUtils.genAgreeMent((Activity)getContext(), termsTV, source);
    }

    public void setCancleTips(String cancleTips) {
        explainTV.setText(cancleTips);
    }
}
