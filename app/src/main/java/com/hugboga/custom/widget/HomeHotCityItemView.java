package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/10/20.
 */
public class HomeHotCityItemView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.hotcity_item_city_iv)
    ImageView cityIV;
    @Bind(R.id.hotcity_item_cityname_tv)
    TextView citynameTV;
    @Bind(R.id.hotcity_item_guide_count_tv)
    TextView guideCountTV;

    public HomeHotCityItemView(Context context) {
        this(context, null);
    }

    public HomeHotCityItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_home_hotcity_item, this);
        ButterKnife.bind(this, view);

        final int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        int imgWidth = (UIUtils.getScreenWidth() - paddingLeft * 2 - UIUtils.dip2px(8) * 2) / 3;
        int imgHeigh = (int)((160 / 220.0f) * imgWidth);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgWidth, imgHeigh);
        cityIV.setLayoutParams(params);
    }

    @Override
    public void update(Object _data) {
        final HomeBeanV2.HotCity data = (HomeBeanV2.HotCity) _data;
        if (data == null) {
            return;
        }
        if (TextUtils.isEmpty(data.cityPicture)) {
            cityIV.setImageResource(R.mipmap.city_default);
        } else {
            Tools.showImage(cityIV, data.cityPicture, R.mipmap.city_default);
        }
        citynameTV.setText(data.cityName);
        guideCountTV.setText(String.format("%1$s位司导", "" + data.cityGuideAmount));

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CityListActivity.Params params = new CityListActivity.Params();
                params.id = data.cityId;
                params.titleName = data.cityName;
                params.cityHomeType = CityListActivity.CityHomeType.CITY;
                Intent intent = new Intent(getContext(), CityListActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                String source = "国家";
                if (getContext() instanceof CityListActivity) {
                    source = ((CityListActivity)getContext()).getEventSource();
                }
                intent.putExtra(Constants.PARAMS_SOURCE, source);
                getContext().startActivity(intent);
            }
        });
    }
}
