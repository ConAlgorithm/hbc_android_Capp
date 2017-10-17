package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.FilterGuideListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.FilterGuideItemView;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

/**
 * Created by zhangqiang on 17/8/2.
 */

public class HomeFilterGuideAdapter extends RecyclerView.Adapter<HomeFilterGuideAdapter.MyViewHolder> {

    private static final int TYPE_ITEM = 0x01;
    private static final int TYPE_MORE = 0x02;
    private Context mContext;
    private int displayImgWidth;
    private int displayImgHeight;
    List<FilterGuideBean> allGuideBeanList;
    LayoutInflater inflater;
    public HomeFilterGuideAdapter(Context context,int displayImgWidth,int displayImgHeight,List<FilterGuideBean> guideBeanList){
        this.mContext = context;
        this.displayImgWidth = displayImgWidth;
        this.displayImgHeight = displayImgHeight;
        this.allGuideBeanList = guideBeanList;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case TYPE_ITEM:
                FilterGuideItemView filterGuideItemView = new FilterGuideItemView(mContext);
                filterGuideItemView.setImageBound(displayImgWidth, displayImgHeight);
                itemView = filterGuideItemView;
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(displayImgWidth, displayImgHeight + ScreenUtil.dip2px(175));
                itemView.setLayoutParams(params);
                break;
            case TYPE_MORE:
                LinearLayout view = (LinearLayout) inflater.inflate(R.layout.home_album_more, null);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(UIUtils.dip2px(100),  UIUtils.dip2px(175));
                view.setLayoutParams(lp);
                itemView = view;
                break;
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (position == getItemCount() -1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentActivity(mContext, FilterGuideListActivity.class,null);
                    SensorsUtils.onAppClick(getEventSource(),"选择心仪的司导服务","首页-选择心仪的司导服务");
                }
            });
        }else{
            ((HbcViewBehavior) holder.itemView).update(allGuideBeanList.get(position));
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() -1) {
            return TYPE_MORE;
        } else {
            return TYPE_ITEM;
        }
    }
    @Override
    public int getItemCount() {
        return allGuideBeanList==null ? 0 : allGuideBeanList.size()+1;
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
