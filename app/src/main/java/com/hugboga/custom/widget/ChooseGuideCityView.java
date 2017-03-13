package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.GuideCropBean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/12.
 */
public class ChooseGuideCityView extends FrameLayout implements HbcViewBehavior{

    @Bind(R.id.item_choosecity_title_tv)
    TextView titleTV;
    @Bind(R.id.item_choosecity_subtitle_tv)
    TextView subtitleTV;

    public ChooseGuideCityView(@NonNull Context context) {
        this(context, null);
    }

    public ChooseGuideCityView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.item_choosecity, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        if (_data instanceof GuideCropBean) {
            GuideCropBean guideCropBean = (GuideCropBean) _data;
            titleTV.setText(guideCropBean.cityName);
            subtitleTV.setText(guideCropBean.placeName);
        }
    }
}
