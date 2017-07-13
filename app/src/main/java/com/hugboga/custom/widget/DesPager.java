package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.DesPageItemAdapter;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.bean.HomeHotCityVo;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;

import java.util.ArrayList;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class DesPager {
    private Context mContext;
    private DesPageItemAdapter recyclerAdapter;
    RecyclerView desItemView;
    ArrayList<HomeBeanV2.HotCity> homeHotCityVos;
    HomeBeanV2.LineGroupAgg lineGroupAgg;
    public DesPager(Context context) {
        this.mContext = context;
    }
    public View inflateView() {
        Log.d("zq","DesPager inflate View ...");

        View mRootView = View.inflate(mContext, R.layout.pager_des, null);
        desItemView = (RecyclerView) mRootView.findViewById(R.id.des_item_view);

        initPagerView();
        return mRootView;
    }

    private void initPagerView() {
        recyclerAdapter = new DesPageItemAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(mContext);
        desItemView.setLayoutManager(layoutManager);
        desItemView.setHasFixedSize(true);
        desItemView.setAdapter(recyclerAdapter);
    }

    public void setHotData(ArrayList<HomeBeanV2.HotCity> homeHotCityVos){
        this.homeHotCityVos = homeHotCityVos;
    }

    public void setLineData(HomeBeanV2.LineGroupAgg lineGroupAgg){
        this.lineGroupAgg = lineGroupAgg;
    }

    public void requestData(String title, int position){
        //// TODO: 17/7/12
        // 需要请求每个tab的数据
        if(!title.isEmpty()){
            if(title.equals("热门")){
                if(recyclerAdapter!= null){
                    recyclerAdapter.addHotCitys(homeHotCityVos,position);
                }
            }else{
                if(recyclerAdapter!= null){
                    if (lineGroupAgg!=null) {
                            recyclerAdapter.addDestionLineGroups(lineGroupAgg,position);
                        }
                    }
                }

            }
        }
}
