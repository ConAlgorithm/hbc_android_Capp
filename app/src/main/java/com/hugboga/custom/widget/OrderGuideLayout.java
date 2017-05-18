package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/5/17.
 */
public class OrderGuideLayout extends LinearLayout {

    @Bind(R.id.order_guide_avatar_iv)
    PolygonImageView avatarIV;
    @Bind(R.id.order_guide_desc_tv)
    TextView descTV;

    public OrderGuideLayout(Context context) {
        this(context, null);
    }

    public OrderGuideLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_guide_layout, this);
        ButterKnife.bind(view);

        int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.order_padding_left);
        setPadding(paddingLeft, UIUtils.dip2px(15), paddingLeft, UIUtils.dip2px(15));
        setBackgroundColor(0xFFFFFFFF);
        setOrientation(HORIZONTAL);
    }

    public void setData(String avatar, String guideName, String countryName) {
        Tools.showImage(avatarIV, avatar, R.mipmap.icon_avatar_guide);
        descTV.setText(String.format("Hi，我是您的司导%1$s，欢迎来到%2$s，期待与您共度美好的旅行时光~", guideName, countryName));
    }
}
