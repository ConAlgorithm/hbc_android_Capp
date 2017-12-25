package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqi on 2017/12/25.
 */

public class SkuDetailToolBarLeftButton extends RelativeLayout {

    @BindView(R.id.header_right_btn_share)
    ImageView headerRightBtnShare;
    @BindView(R.id.header_right_btn_share_red_dot)
    ImageView headerRightBtnShareRedDot;

    public SkuDetailToolBarLeftButton(Context context) {
        this(context, null);
    }

    public SkuDetailToolBarLeftButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.fg_sku_detail_header_image_share, this);
        ButterKnife.bind(view);
    }

    public void isChatRedDot(boolean b) {
        if (b) {
            headerRightBtnShareRedDot.setVisibility(View.VISIBLE);
        } else {
            headerRightBtnShareRedDot.setVisibility(View.GONE);
        }
    }

}
