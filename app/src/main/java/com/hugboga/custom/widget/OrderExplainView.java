package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.net.UrlLibs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/17.
 */
public class OrderExplainView extends LinearLayout{

    @Bind(R.id.order_detail_terms_layout)
    LinearLayout termsLayout;
    @Bind(R.id.order_detail_terms_tv)
    TextView termsTV;
    @Bind(R.id.order_detail_explain_tv)
    TextView explainTV;

    public OrderExplainView(Context context) {
        this(context, null);
    }

    public OrderExplainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_explain, this);
        ButterKnife.bind(view);

        termsTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        termsTV.getPaint().setAntiAlias(true);
    }

    public void setTermsLayoutVisibility(int visibility) {
        termsLayout.setVisibility(visibility);
    }

    @OnClick({R.id.order_detail_terms_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_detail_terms_tv:
                Intent intent = new Intent(getContext(), WebInfoActivity.class);
                intent.putExtra("web_url", UrlLibs.H5_TAI_AGREEMENT);
                getContext().startActivity(intent);
                break;
        }
    }

    public void setCancleTips(String cancleTips) {
        explainTV.setText(cancleTips);
    }
}
