package com.hugboga.custom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.NetWork;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.adapter.DesTabPagerAdpter;
import com.hugboga.custom.data.bean.SimpleLineGroupVo;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.DestinationTab;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.widget.DataVisibleLister;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 17/7/11.
 */

public class FgDestination extends FgBaseTravel implements HttpRequestListener,DataVisibleLister {

    @Bind(R.id.vp_view)
    ViewPager mViewPager;
    @Bind(R.id.tabs)
    XTabLayout mTabLayout;
    @Bind(R.id.search)
    RelativeLayout search;
    @Bind(R.id.city_list_empty_layout)
    LinearLayout emptyLayout;
    //ArrayList<DesPager> pagerList =  new ArrayList<DesPager>();
    //ArrayList<String> titleTextList = new ArrayList<>();
    int currentPosition = 0;
    int currentGroundId = 0;
    boolean isNetworkAvailable = true;
    DesTabPagerAdpter mAdapter;
    boolean fromEmptyWifi = false;
    @Override
    public int getContentViewId() {
        return R.layout.fg_destination;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
    protected void loadData() {
        if(mAdapter!= null && mAdapter.homePager!= null && (mAdapter.homePager.homeHotCityVos != null || mAdapter.homePager.lineGroupAgg != null)){
            visible();
        }else{
            DestinationTab destinationTab = new DestinationTab(getContext());
            HttpRequestUtils.request(getContext(),destinationTab,this,false);
        }

    }

    @Override
    protected void initView() {
        super.initView();
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
        this.getContext().startActivity(intent);
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

                SimpleLineGroupVo simpleLineGroupVo1 = new SimpleLineGroupVo();
                simpleLineGroupVo1.setGroupName("热门");
                simpleLineGroupVo.add(0,simpleLineGroupVo1);
                mAdapter = new DesTabPagerAdpter(simpleLineGroupVo,getContext());

                mViewPager.setAdapter(mAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mTabLayout.setSelectedTabIndicatorHeight(0);
                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset,
                                               int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(final int position) {
                        currentPosition = position;
                        String title = simpleLineGroupVo.get(position).getGroupName();
                        for(int i=0;i<simpleLineGroupVo.size();i++){
                            if(simpleLineGroupVo.get(i).getGroupName().equals(title)){
                                currentGroundId = simpleLineGroupVo.get(i).getGroupId();
                            }
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }
        }
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
        /*if(mViewPager!= null){
            mViewPager.removeAllViews();
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        //super.onDataRequestError(errorInfo, request);
        isNetworkAvailable = NetWork.isNetworkAvailable(getContext());
        if(request instanceof DestinationTab){
            isNetworkAvailable = NetWork.isNetworkAvailable(getContext());
            if(!isNetworkAvailable){
                CommonUtils.showToast("当前网络不可用");
                EventBus.getDefault().post(new EventAction(EventType.SHOW_EMPTY_WIFI_BY_TAB));
                return;
            }
        }

    }

    @Override
    public void visible() {
        if(mTabLayout != null && mViewPager != null) {
            mTabLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case SHOW_EMPTY_WIFI_BY_HOT_OR_LINE:
                mTabLayout.setVisibility(View.GONE);
                mViewPager.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //fromEmptyWifi = true;
                       if(mAdapter!= null && mAdapter.homePager!= null){
                            mAdapter.homePager.selectDestionTab(currentPosition, currentGroundId);
                        }
                    }
                });
                break;
            case SHOW_EMPTY_WIFI_BY_TAB:
                mTabLayout.setVisibility(View.GONE);
                mViewPager.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                emptyLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //fromEmptyWifi = true;
                        DestinationTab destinationTab = new DestinationTab(getContext());
                        HttpRequestUtils.request(getContext(),destinationTab,FgDestination.this,false);
                    }
                });
                break;
            case SHOW_DATA:
                visible();
                break;
            default:
                break;
        }
    }

    @Override
    public String getEventSource() {
        return "目的地";
    }
}
