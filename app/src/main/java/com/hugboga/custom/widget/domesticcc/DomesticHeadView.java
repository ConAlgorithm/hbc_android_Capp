package com.hugboga.custom.widget.domesticcc;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 国内信用卡头部信息
 * 显示还需支付
 * Created by HONGBO on 2017/10/23 14:55.
 */

public class DomesticHeadView extends FrameLayout {

    @Bind(R.id.domesticHeaderPrice)
    TextView domesticHeaderPrice;

    public DomesticHeadView(Context context) {
        this(context, null);
    }

    public DomesticHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.domestic_header_view, this);
        ButterKnife.bind(this, view);
    }

    /**
     * 设置头部价格信息
     *
     * @param price 价格，例如2,000
     */
    public void init(String price) {
        domesticHeaderPrice.setText(price);
    }
}
