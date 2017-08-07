package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.widget.FilterGuideItemView;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

/**
 * Created by zhangqiang on 17/8/2.
 */

public class HomeFilterGuideAdapter extends RecyclerView.Adapter<HomeFilterGuideAdapter.MyViewHolder> {

    private Context mContext;
    private int displayImgWidth;
    private int displayImgHeight;
    List<FilterGuideBean> allGuideBeanList;
    public HomeFilterGuideAdapter(Context context,int displayImgWidth,int displayImgHeight,List<FilterGuideBean> guideBeanList){
        this.mContext = context;
        this.displayImgWidth = displayImgWidth;
        this.displayImgHeight = displayImgHeight;
        this.allGuideBeanList = guideBeanList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        FilterGuideItemView filterGuideItemView = new FilterGuideItemView(mContext);
        filterGuideItemView.setImageBound(displayImgWidth, displayImgHeight);
        itemView = filterGuideItemView;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(displayImgWidth, displayImgHeight + ScreenUtil.dip2px(184));
        itemView.setLayoutParams(params);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ((HbcViewBehavior) holder.itemView).update(allGuideBeanList.get(position));
    }

    @Override
    public int getItemCount() {
        return allGuideBeanList==null ? 0 : allGuideBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }

    //分页加载更新数据
    public void add(List<FilterGuideBean> guideBeanList) {
        allGuideBeanList.addAll(guideBeanList);
        notifyDataSetChanged();
    }

    public void setData(List<FilterGuideBean> guideBeanList) {
        allGuideBeanList = guideBeanList;
        notifyDataSetChanged();
    }
}
