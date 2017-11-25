package com.hugboga.custom.widget.city;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import tk.hongbo.label.widget.LabelItemView;

/**
 * 筛选条件内容
 * Created by HONGBO on 2017/11/22 16:26.
 */

public class CityContentFilterView extends FrameLayout {

    @BindView(R.id.city_content_filter_item)
    LabelItemView labelItemView;

    public CityContentFilterView(@NonNull Context context) {
        this(context, null);
    }

    public CityContentFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_content_filter, this);
        ButterKnife.bind(this, view);
    }

//    public void init(String leftStr, String centerStr, String rightStr, List<String> data) {
//        labelItemView.init(leftStr, centerStr, rightStr, data);
//    }
}
