package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.widget.recycler.ZDefaultDivider;
import com.huangbaoche.hbcframe.widget.recycler.ZListRecyclerView;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityHomeFilterAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/19.
 */

public class CityHomeFilter  extends LinearLayout implements View.OnClickListener {

    private RelativeLayout unlimitType,unlimitDays,unlimitTheme,cityHomeFilterRecycle;
    private  ImageView unlimitedTypeIV,unlimitedTypeTips,unlimitedDaysIV,unlimitedDaysTips,unlimitThemeIV,unlimitThemeTips;

    @Bind(R.id.cityHome_list_recycleview)
    RecyclerView recyclerView;

    CityHomeFilterAdapter adapter;
    List<String> list;
    RecyclerView.LayoutManager manager;

    public CityHomeFilter(Context context) {
        this(context,null);
    }

    public CityHomeFilter(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.city_home_filter_list,this);
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

        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cityHome_unlimited_type_lay:
                unlimitedTypeIV.setImageResource(R.mipmap.share_unfold);
                unlimitedTypeTips.setVisibility(VISIBLE);
                cityHomeFilterRecycle.setVisibility(VISIBLE);
                adapter=new CityHomeFilterAdapter(getContext(),list);
                manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                break;
            case R.id.cityHome_unlimited_days_lay:
                unlimitedDaysIV.setImageResource(R.mipmap.share_unfold);
                unlimitedDaysTips.setVisibility(VISIBLE);
                cityHomeFilterRecycle.setVisibility(VISIBLE);
                adapter=new CityHomeFilterAdapter(getContext(),list);
                manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                break;
            case R.id.cityHome_unlimited_theme_lay:
                unlimitThemeIV.setImageResource(R.mipmap.share_unfold);
                unlimitThemeTips.setVisibility(VISIBLE);
                cityHomeFilterRecycle.setVisibility(VISIBLE);
                adapter=new CityHomeFilterAdapter(getContext(),list);
                manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                break;
        }
    }

    private void init(){
        recyclerView=(RecyclerView)findViewById(R.id.cityHome_list_recycleview);
        list=new ArrayList<>();
        list.add("不限");
        list.add("一天");
        list.add("d多天");
    }
}
