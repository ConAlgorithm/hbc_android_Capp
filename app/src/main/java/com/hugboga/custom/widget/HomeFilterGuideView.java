package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeFilterGuideAdapter;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/1.
 */

public class HomeFilterGuideView extends LinearLayout implements HttpRequestListener {
    Activity activity;
    List<FilterGuideBean> guideBeanList;
    @Bind(R.id.filter_guide_more)
    TextView moreTV;
    @Bind(R.id.filter_guide_recyclerview)
    RecyclerView recyclerView;
    HomeFilterGuideAdapter homeFilterGuideAdapter;
    public int displayImgWidth, displayImgHeight;
    public HomeFilterGuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_filter_guide, this);
        ButterKnife.bind(view);

        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.home_filter_avr_space);
        displayImgWidth = getContext().getResources().getDimensionPixelOffset(R.dimen.home_filter_avr_hor);
        displayImgHeight = getContext().getResources().getDimensionPixelOffset(R.dimen.home_filter_avr_ver);
        int viewHeight = displayImgHeight + ScreenUtil.dip2px(184);
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

    public HomeFilterGuideView(Context context) {
        super(context);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {

    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

    }

    public void setGuideBeanList(List<FilterGuideBean> guideBeanList) {
        this.guideBeanList = guideBeanList;

        if (homeFilterGuideAdapter == null) {
            homeFilterGuideAdapter = new HomeFilterGuideAdapter(getContext(),  displayImgWidth, displayImgHeight, guideBeanList);
            recyclerView.setAdapter(homeFilterGuideAdapter);
        } else {
            homeFilterGuideAdapter.setData(guideBeanList);
        }


        moreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更多todo!
            }
        });
    }
    public void setActivity(Activity activity){
        this.activity = activity;
    }
}
