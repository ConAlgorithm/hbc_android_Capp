package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeFilterGuideAdapter;
import com.hugboga.custom.adapter.HomeHotDestinationAdapter;
import com.hugboga.custom.data.bean.HomeHotDestination;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/9/13.
 */

public class HomeHotDestinationView extends LinearLayout implements HbcViewBehavior{
    Context context;
    @Bind(R.id.home_hot_destination_recyclerview)
    RecyclerView recyclerView;
    HomeHotDestinationAdapter homeHotDestinationAdapter;
    public int displayImgWidth, displayImgHeight;
    ArrayList<HomeHotDestination> hotCities;
    public HomeHotDestinationView(Context context) {
        this(context,null);
    }

    public HomeHotDestinationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = inflate(context, R.layout.home_hot_destination_view, this);
        ButterKnife.bind(view);

        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.home_filter_avr_space);
        displayImgWidth = getContext().getResources().getDimensionPixelOffset(R.dimen.home_hot_destination_img_width);
        displayImgHeight = getContext().getResources().getDimensionPixelOffset(R.dimen.home_hot_destination_img_height);
        int viewHeight = displayImgHeight + ScreenUtil.dip2px(60);
        recyclerView.getLayoutParams().height = viewHeight;

        recyclerView.setFocusable(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHorizontalScrollBarEnabled(false);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration();
        itemDecoration.setItemOffsets(paddingLeft, 0, 0, 0, LinearLayout.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecoration);


    }

    public void setHotCities(ArrayList<HomeHotDestination> hotCities){
        this.hotCities = hotCities;
    }

    @Override
    public void update(Object _data) {
        hotCities = (ArrayList<HomeHotDestination>) _data;

        if(hotCities == null){
            return;
        }
        if (homeHotDestinationAdapter == null) {
            homeHotDestinationAdapter = new HomeHotDestinationAdapter(getContext(),  displayImgWidth, displayImgHeight,hotCities);
            recyclerView.setAdapter(homeHotDestinationAdapter);
        } else {
            homeHotDestinationAdapter.setData(hotCities);
        }
    }
}
