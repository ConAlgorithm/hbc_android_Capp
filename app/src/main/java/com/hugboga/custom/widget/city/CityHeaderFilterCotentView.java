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
 * 筛选条件显示内容
 * Created by HONGBO on 2017/11/22 11:47.
 */

public class CityHeaderFilterCotentView extends FrameLayout {
    public CityHeaderFilterCotentView(@NonNull Context context) {
        this(context, null);
    }

    public CityHeaderFilterCotentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_header_filter_content, this);
        ButterKnife.bind(this, view);
    }
}
