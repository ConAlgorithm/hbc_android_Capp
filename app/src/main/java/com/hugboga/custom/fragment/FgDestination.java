package com.hugboga.custom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.adapter.DesTabPagerAdpter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.bean.HomeHotCityVo;
import com.hugboga.custom.data.bean.SimpleLineGroupVo;
import com.hugboga.custom.data.request.DestinationHot;
import com.hugboga.custom.data.request.DestinationLine;
import com.hugboga.custom.data.request.DestinationTab;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.widget.DesPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by zhangqiang on 17/7/11.
 */

public class FgDestination extends BaseFragment implements HttpRequestListener {

    @Bind(R.id.vp_view)
    ViewPager mViewPager;
    @Bind(R.id.tabs)
    XTabLayout mTabLayout;
    @Bind(R.id.search)
    RelativeLayout search;
    ArrayList<DesPager> pagerList =  new ArrayList<DesPager>();
    ArrayList<String> titleTextList = new ArrayList<>();
    int currentPosition = 0;
    @Override
    public int getContentViewId() {
        return R.layout.fg_destination;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void initView() {
        super.initView();
        pagerList = new ArrayList<DesPager>();

        //本地写死一个item,后台不返回
        titleTextList.add("热门");
        pagerList.add(new DesPager(getContext()));

        DestinationTab destinationTab = new DestinationTab(getContext());
        HttpRequestUtils.request(getContext(),destinationTab,this,false);

    }
    private void selectDestionTab(int position,int groundId) {
        DesPager desPager = pagerList.get(position);
        String title = titleTextList.get(position);
        //需要将请求的数据添加到DesPager中
        if(position == 0){
            desPager.requestData(title,0);
        }else{
            DestinationLine destinationLine = new DestinationLine(getContext(),groundId);
            HttpRequestUtils.request(getContext(),destinationLine,this,false);
        }

    }

    @OnClick({R.id.search})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search:
                goChooseCity();
                break;
        }

    }
    private void goChooseCity() {
        Intent intent = new Intent(this.getContext(), ChooseCityNewActivity.class);
        //intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
        //intent.putExtra("isHomeIn", true);
        //intent.putExtra("source", "首页搜索框");
        this.getContext().startActivity(intent);
        //StatisticClickEvent.click(StatisticConstant.SEARCH_LAUNCH, "首页");
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if(request instanceof DestinationTab){
            final ArrayList<SimpleLineGroupVo> simpleLineGroupVo = (ArrayList<SimpleLineGroupVo>) request.getData();
            if(simpleLineGroupVo!= null){
                for(int i=0;i<simpleLineGroupVo.size();i++){
                    titleTextList.add(simpleLineGroupVo.get(i).getGroupName());
                }
                for(int i=0;i<simpleLineGroupVo.size();i++){
                    pagerList.add(new DesPager(getContext()));
                }
                DesTabPagerAdpter mAdapter = new DesTabPagerAdpter(pagerList,titleTextList);
                mViewPager.setAdapter(mAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mTabLayout.setSelectedTabIndicatorHeight(0);
                mViewPager.setCurrentItem(0);
                //selectDestionTab(0);
                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset,
                                               int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        currentPosition = position;
                        String title = titleTextList.get(position);
                        int groundId = 0;
                        for(int i=0;i<simpleLineGroupVo.size();i++){
                            if(simpleLineGroupVo.get(i).getGroupName().equals(title)){
                                groundId = simpleLineGroupVo.get(i).getGroupId();
                            }
                        }
                        selectDestionTab(position,groundId);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                DestinationHot destinationHot = new DestinationHot(getContext());
                HttpRequestUtils.request(getContext(),destinationHot,this,false);
            }
        }else if(request instanceof DestinationHot){
            ArrayList<HomeBeanV2.HotCity> homeHotCityVos = (ArrayList<HomeBeanV2.HotCity>) request.getData();
            if(homeHotCityVos != null){
                DesPager desPager = pagerList.get(0);
                desPager.setHotData(homeHotCityVos);
                String title = titleTextList.get(0);
                //需要将请求的数据添加到DesPager中
                desPager.requestData(title,0);
            }
        }else if(request instanceof DestinationLine){
            HomeBeanV2.LineGroupAgg lineGroupAgg = (HomeBeanV2.LineGroupAgg) request.getData();
            if(lineGroupAgg != null){
                DesPager desPager = pagerList.get(currentPosition);
                desPager.setLineData(lineGroupAgg);
                String title = titleTextList.get(currentPosition);
                desPager.requestData(title,currentPosition);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

}
