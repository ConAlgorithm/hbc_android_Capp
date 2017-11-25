package com.hugboga.custom.widget.city;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hugboga.custom.R;

import butterknife.ButterKnife;

/**
 * 头部筛选部分
 * Created by HONGBO on 2017/11/21 20:16.
 */

public class CityHeaderFilterView extends FrameLayout {
    public CityHeaderFilterView(@NonNull Context context) {
        this(context, null);
    }

    public CityHeaderFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_header_img, this);
        ButterKnife.bind(this, view);
    }
}
