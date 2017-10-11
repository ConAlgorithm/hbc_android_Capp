package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeCityRecommentGuideAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeCityContentVo2;
import com.hugboga.custom.statistic.MobClickUtils;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/9/15.
 */

public class HomeCityRecommentGuideView extends LinearLayout implements HttpRequestListener {

    @Bind(R.id.filter_guide)
    TextView filterGuide;
    @Bind(R.id.filter_guide_recyclerview)
    RecyclerView recyclerView;
    HomeCityRecommentGuideAdapter homeCityRecommentGuideAdapter;
    public int displayImgWidth, displayImgHeight;
    Context context;
    public HomeCityRecommentGuideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = inflate(context, R.layout.view_filter_guide, this);
        ButterKnife.bind(view);

        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.home_filter_avr_space);
        displayImgWidth = getContext().getResources().getDimensionPixelOffset(R.dimen.home_filter_avr_hor);
        displayImgHeight = getContext().getResources().getDimensionPixelOffset(R.dimen.home_filter_avr_ver);
        int viewHeight = displayImgHeight + ScreenUtil.dip2px(175);
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

    public HomeCityRecommentGuideView(Context context) {
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

    public void setGuideBeanList(HomeCityContentVo2 homeCityContentVo2) {

        if(homeCityContentVo2 != null && homeCityContentVo2.cityItemList != null && homeCityContentVo2.cityItemList.size()>0){
            if(homeCityContentVo2.placeType == 1){
                filterGuide.setText(getContext().getResources().getString(R.string.home_recomment_guide,homeCityContentVo2.countryName));
            }else if(homeCityContentVo2.placeType == 2){
                filterGuide.setText(getContext().getResources().getString(R.string.home_recomment_guide,homeCityContentVo2.cityName));
            }

            if (homeCityRecommentGuideAdapter == null) {
                homeCityRecommentGuideAdapter = new HomeCityRecommentGuideAdapter(getContext(),  displayImgWidth, displayImgHeight, homeCityContentVo2,homeCityContentVo2.cityItemList);
                recyclerView.setAdapter(homeCityRecommentGuideAdapter);
            } else {
                homeCityRecommentGuideAdapter.setData(homeCityContentVo2);
            }
        }

        /*moreTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更多todo!
                intentActivity(context, FilterGuideListActivity.class,null);
                SensorsUtils.onAppClick(getEventSource(),"选择心仪的司导服务","首页-选择心仪的司导服务");
            }
        });*/
    }

    private void intentActivity(Context context, Class<?> cls, String eventId) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        context.startActivity(intent);
        if (!TextUtils.isEmpty(eventId)) {
            MobClickUtils.onEvent(eventId);
        }
    }

    public String getEventSource() {
        return "首页";
    }
}
