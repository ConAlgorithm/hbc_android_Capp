package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

/**
 * Created by Administrator on 2016/10/19.
 */

public class CityHomeFilter  extends LinearLayout implements View.OnClickListener {

    private RelativeLayout unlimitType,unlimitDays,unlimitTheme,cityHomeFilterRecycle;
    private  ImageView unlimitedTypeIV,unlimitedTypeTips,unlimitedDaysIV,unlimitedDaysTips,unlimitThemeIV,unlimitThemeTips;
    RecyclerView recyclerView;

    public CityHomeFilter(Context context) {
        this(context,null);
    }

    public CityHomeFilter(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.city_home_filter_list,null);
        unlimitType=(RelativeLayout)findViewById(R.id.cityHome_unlimited_type_lay) ;
        unlimitDays=(RelativeLayout)findViewById(R.id.cityHome_unlimited_days_lay);
        unlimitTheme=(RelativeLayout)findViewById(R.id.cityHome_unlimited_theme_lay);
        cityHomeFilterRecycle=(RelativeLayout)findViewById(R.id.cityHome_filter_recycle_lay);

        unlimitedTypeIV=(ImageView)findViewById(R.id.city_home_unlimited_type) ;
        unlimitedTypeTips=(ImageView)findViewById(R.id.city_home_unlimited_type_tips) ;
        unlimitedDaysIV=(ImageView)findViewById(R.id.city_home_unlimited_days) ;
        unlimitedDaysTips=(ImageView)findViewById(R.id.city_home_unlimited_days_tips) ;
        unlimitThemeIV=(ImageView)findViewById(R.id.city_home_unlimited_theme);
        unlimitThemeTips=(ImageView)findViewById(R.id.city_home_unlimited_theme_tips);

        unlimitType.setOnClickListener(this);
        unlimitDays.setOnClickListener(this);
        unlimitTheme.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cityHome_unlimited_type_lay:
                unlimitedTypeIV.setImageResource(R.mipmap.share_unfold);
                unlimitedTypeTips.setVisibility(VISIBLE);

                break;
            case R.id.cityHome_unlimited_days_lay:
                unlimitedDaysIV.setImageResource(R.mipmap.share_unfold);
                unlimitedDaysTips.setVisibility(VISIBLE);
                break;
            case R.id.cityHome_unlimited_theme_lay:
                unlimitThemeIV.setImageResource(R.mipmap.share_unfold);
                unlimitThemeTips.setVisibility(VISIBLE);
                break;
        }
    }
}
