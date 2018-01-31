package com.hugboga.custom.widget.city;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.tools.NetImg;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 线路列表Item头像部分显示
 * Created by HONGBO on 2018/1/31 11:43.
 */
public class CityGuidesView extends RelativeLayout {

    @BindView(R.id.city_guides_layout_guide1)
    ImageView city_guides_layout_guide1;
    @BindView(R.id.city_guides_layout_guide2)
    ImageView city_guides_layout_guide2;

    public CityGuidesView(Context context) {
        this(context, null);
    }

    public CityGuidesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_guides_layout, this);
        ButterKnife.bind(this, view);
    }

    public void init(String url) {
        NetImg.showCircleImage(getContext(), city_guides_layout_guide1, url);
        NetImg.showCircleImage(getContext(), city_guides_layout_guide2, url);
    }

}
