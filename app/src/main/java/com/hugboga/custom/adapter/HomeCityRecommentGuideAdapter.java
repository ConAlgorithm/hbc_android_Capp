package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeCityItemVo;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.HomeRecommentGuideItemView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.ArrayList;

/**
 * Created by zhangqiang on 17/9/15.
 */

public class HomeCityRecommentGuideAdapter extends RecyclerView.Adapter<HomeCityRecommentGuideAdapter.MyViewHolder>{

    private Context mContext;
    private int displayImgWidth;
    private int displayImgHeight;
    ArrayList<HomeCityItemVo> guideBeanList;
    public HomeCityRecommentGuideAdapter(Context context, int displayImgWidth, int displayImgHeight, ArrayList<HomeCityItemVo> guideBeanList){
        this.mContext = context;
        this.displayImgWidth = displayImgWidth;
        this.displayImgHeight = displayImgHeight;
        this.guideBeanList = guideBeanList;
    }
    @Override
    public HomeCityRecommentGuideAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        HomeRecommentGuideItemView homeRecommentGuideItemView = new HomeRecommentGuideItemView(mContext);
        homeRecommentGuideItemView.setImageBound(displayImgWidth, displayImgHeight);
        itemView = homeRecommentGuideItemView;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(displayImgWidth, displayImgHeight + ScreenUtil.dip2px(175));
        itemView.setLayoutParams(params);
        return new HomeCityRecommentGuideAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeCityRecommentGuideAdapter.MyViewHolder holder, int position) {
        ((HbcViewBehavior) holder.itemView).update(guideBeanList.get(position));
    }

    @Override
    public int getItemCount() {
        return guideBeanList==null ? 0 : guideBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }

    //分页加载更新数据
    public void add(ArrayList<HomeCityItemVo> guideBeanList) {
        guideBeanList.addAll(guideBeanList);
        notifyDataSetChanged();
    }

    public void setData(ArrayList<HomeCityItemVo> guideBeanList) {
        guideBeanList = guideBeanList;
        notifyDataSetChanged();
    }
    public String getEventSource() {
        return "首页";
    }

    private void intentActivity(Context context, Class<?> cls, String eventId) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        context.startActivity(intent);
        if (!TextUtils.isEmpty(eventId)) {
            MobClickUtils.onEvent(eventId);
        }
    }
}
