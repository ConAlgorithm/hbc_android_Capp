package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.TravelFundData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 01/03/2018.
 */
public class TravelFundItemView extends LinearLayout implements HbcViewBehavior{

    @BindView(R.id.travelfund_price_tv)
    TextView priceTV;
    @BindView(R.id.travelfund_title_tv)
    TextView titleTV;
    @BindView(R.id.travelfund_date_tv)
    TextView dateTV;

    public TravelFundItemView(Context context) {
        this(context, null);
    }

    public TravelFundItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.item_travelfund, this);
        ButterKnife.bind(this);
    }

    @Override
    public void update(Object _data) {
        TravelFundData.TravelFundItemBean itemBean = (TravelFundData.TravelFundItemBean)_data;
        priceTV.setText(itemBean.amount);
        titleTV.setText(itemBean.title);
        dateTV.setText(itemBean.date);
    }
}
