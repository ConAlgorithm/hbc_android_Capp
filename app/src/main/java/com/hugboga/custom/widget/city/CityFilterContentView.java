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
 * 筛选条件内容
 * Created by HONGBO on 2017/11/22 16:26.
 */

public class CityFilterContentView extends FrameLayout {

    public CityFilterContentView(@NonNull Context context) {
        this(context, null);
    }

    public CityFilterContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_content_filter, this);
        ButterKnife.bind(this, view);
    }
}
