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
 * 城市头部统计信息
 * Created by HONGBO on 2017/11/21 19:59.
 */

public class CityHeaderCountView extends FrameLayout {

    public CityHeaderCountView(@NonNull Context context) {
        this(context, null);
    }

    public CityHeaderCountView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_header_count, this);
        ButterKnife.bind(this, view);
    }
}
