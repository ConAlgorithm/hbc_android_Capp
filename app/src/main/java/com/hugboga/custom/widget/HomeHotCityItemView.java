package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityHomeListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.hugboga.custom.constants.Constants.PARAMS_DATA;

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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgWidth, imgWidth);
        cityIV.setLayoutParams(params);
    }

    @Override
    public void update(Object _data) {
        final HomeBean.HotCity data = (HomeBean.HotCity) _data;
        if (data == null) {
            return;
        }
        Tools.showImage(cityIV, data.cityHeadPicture);
        citynameTV.setText(data.cityName);

        String guideCountStr = String.format("%1$s位司导", "" + data.cityGuideAmount);
        SpannableString msp = new SpannableString(guideCountStr);
        msp.setSpan(new ForegroundColorSpan(0xFFFF6633), 0, String.valueOf(data.cityGuideAmount).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        guideCountTV.setText(msp);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                CityHomeListActivity.Params params = new CityHomeListActivity.Params();
                Intent intent = new Intent(getContext(), CityHomeListActivity.class);
                bundle.putSerializable(Constants.PARAMS_SOURCE,"首页");
                params.cityHomeType=CityHomeListActivity.CityHomeType.CITY;
                params.titleName=data.cityName;
                params.id=data.cityId;
                intent.putExtra(Constants.PARAMS_DATA,params);
                getContext().startActivity(intent);
            }
        });
    }
}
