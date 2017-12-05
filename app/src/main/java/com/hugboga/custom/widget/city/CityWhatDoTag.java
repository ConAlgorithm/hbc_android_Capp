package com.hugboga.custom.widget.city;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 玩法列表底部下单入口
 * Created by HONGBO on 2017/11/29 20:35.
 */

public class CityWhatDoTag extends FrameLayout {

    @BindView(R.id.city_what_do_tag_name)
    TextView city_what_do_tag_name; //下单入口标签名称

    public CityWhatDoTag(@NonNull Context context) {
        this(context, null);
    }

    public CityWhatDoTag(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_what_do_tag, this);
        ButterKnife.bind(this, view);
    }

    public void init(String name) {
        if (!TextUtils.isEmpty(name)) {
            setVisibility(VISIBLE);
            city_what_do_tag_name.setText(name);
        } else {
            setVisibility(GONE);
        }
    }
}
