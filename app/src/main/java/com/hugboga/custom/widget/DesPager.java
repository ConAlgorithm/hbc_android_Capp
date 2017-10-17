package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.NetWork;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.DesPageItemAdapter;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.bean.SimpleLineGroupVo;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.DestinationHot;
import com.hugboga.custom.data.request.DestinationLine;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class DesPager extends LinearLayout implements HttpRequestListener {

    private DesPageItemAdapter recyclerAdapter;
    RecyclerView desItemView;
    public ArrayList<HomeBeanV2.HotCity> homeHotCityVos;
    public HomeBeanV2.LineGroupAgg lineGroupAgg;

    int currentPosition = 0;
    public SimpleLineGroupVo simpleLineGroupVo;
    boolean isNetworkAvailable = true;

    public DesPager(Context context) {
        this(context,null);
    }

    public DesPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View mRootView = inflate(context, R.layout.pager_des, this);
        desItemView = (RecyclerView) mRootView.findViewById(R.id.des_item_view);
        initPagerView();
    }

    public void initData(SimpleLineGroupVo simpleLineGroupVo,int currentPosition){
        this.simpleLineGroupVo = simpleLineGroupVo;
        this.currentPosition = currentPosition;
        selectDestionTab(currentPosition,simpleLineGroupVo.getGroupId());
    }

    private void initPagerView() {
        recyclerAdapter = new DesPageItemAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(getContext());
        desItemView.setLayoutManager(layoutManager);
        desItemView.setHasFixedSize(true);
        desItemView.setAdapter(recyclerAdapter);
    }

    public void setHotData(ArrayList<HomeBeanV2.HotCity> homeHotCityVos) {
        this.homeHotCityVos = homeHotCityVos;
    }

    public void setLineData(HomeBeanV2.LineGroupAgg lineGroupAgg) {
        this.lineGroupAgg = lineGroupAgg;
    }

    public void requestData(String title, int position) {
        // 需要请求每个tab的数据
        if (!title.isEmpty()) {
            if (title.equals("热门")) {
                if (recyclerAdapter != null) {
                    recyclerAdapter.addHotCitys(homeHotCityVos, position);
                }
            } else {
                if (recyclerAdapter != null) {
                    if (lineGroupAgg != null) {
                        recyclerAdapter.addDestionLineGroups(lineGroupAgg, position);
                    }
                }
            }

        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {

        if(request instanceof DestinationHot){

            ArrayList<HomeBeanV2.HotCity> homeHotCityVos = (ArrayList<HomeBeanV2.HotCity>) request.getData();
            if(homeHotCityVos != null){
                setHotData(homeHotCityVos);
                //需要将请求的数据添加到DesPager中
                requestData("热门",0);
            }
        }else if(request instanceof DestinationLine){

            HomeBeanV2.LineGroupAgg lineGroupAgg = (HomeBeanV2.LineGroupAgg) request.getData();
            if(lineGroupAgg != null){
                setLineData(lineGroupAgg);
                if(simpleLineGroupVo != null){
                    String title = simpleLineGroupVo.getGroupName();
                    requestData(title,currentPosition);
                }

            }
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }
    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

        isNetworkAvailable = NetWork.isNetworkAvailable(getContext());
        if(request instanceof DestinationHot || request instanceof DestinationLine){
            if(!isNetworkAvailable){
                CommonUtils.showToast(R.string.destination_network_available);
                EventBus.getDefault().post(new EventAction(EventType.SHOW_EMPTY_WIFI_BY_HOT_OR_LINE));
            }
        }

    }

    public void selectDestionTab(int position,int groundId) {
        if(position == 0){
            DestinationHot destinationHot = new DestinationHot(getContext());
            HttpRequestUtils.request(getContext(),destinationHot,this,false);
        }else{
            DestinationLine destinationLine = new DestinationLine(getContext(),groundId);
            HttpRequestUtils.request(getContext(),destinationLine,this,false);
        }

    }
}
