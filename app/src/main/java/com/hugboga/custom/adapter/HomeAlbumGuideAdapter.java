package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.HomeAlbumInfoVo;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.HomeHotAlbumGuideItemView;

/**
 * Created by zhangqiang on 17/9/15.
 */

public class HomeAlbumGuideAdapter extends RecyclerView.Adapter<HomeAlbumGuideAdapter.MyViewHolder>{

    private static final int TYPE_ITEM = 0x01;
    private static final int TYPE_MORE = 0x02;
    private Context mContext;
    private int displayImgWidth;
    private int displayImgHeight;
    HomeAlbumInfoVo homeAlbumInfoVo;
    LayoutInflater inflater;
    public HomeAlbumGuideAdapter(Context context,int displayImgWidth,int displayImgHeight,HomeAlbumInfoVo homeAlbumInfoVo){
        this.mContext = context;
        this.displayImgWidth = displayImgWidth;
        this.displayImgHeight = displayImgHeight;
        this.homeAlbumInfoVo = homeAlbumInfoVo;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public HomeAlbumGuideAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case TYPE_ITEM:
                HomeHotAlbumGuideItemView albumItemView = new HomeHotAlbumGuideItemView(mContext);
                albumItemView.setImageBound(displayImgWidth, displayImgHeight);
                itemView = albumItemView;
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(UIUtils.dip2px(235),  UIUtils.dip2px(175) + UIUtils.dip2px(165));
                itemView.setLayoutParams(params);
                break;
            case TYPE_MORE:
                LinearLayout view = (LinearLayout) inflater.inflate(R.layout.home_album_more, null);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(UIUtils.dip2px(100),  UIUtils.dip2px(175));
                view.setLayoutParams(lp);
                itemView = view;
                break;
        }

        return new HomeAlbumGuideAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeAlbumGuideAdapter.MyViewHolder holder, int position) {

        if (position == getItemCount() -1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(holder.itemView.getContext(),WebInfoActivity.class);
                    intent.putExtra("web_url", homeAlbumInfoVo.albumLinkUrl);
                    holder.itemView.getContext().startActivity(intent);
                    SensorsUtils.onAppClick(getEventSource(),"热门专辑","首页-热门专辑");
                }
            });
        }else{
            ((HbcViewBehavior) holder.itemView).update(homeAlbumInfoVo.albumRelItems.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return homeAlbumInfoVo==null ? 0 : homeAlbumInfoVo.albumRelItems.size()+1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }

    /*//分页加载更新数据
    public void add(HomeAlbumInfoVo homeAlbumInfoVo) {
        homeAlbumInfoVo.addAll(homeAlbumInfoVo);
        notifyDataSetChanged();
    }*/

    public void setData(HomeAlbumInfoVo homeAlbumInfoVo) {
        this.homeAlbumInfoVo = homeAlbumInfoVo;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() -1) {
            return TYPE_MORE;
        } else {
            return TYPE_ITEM;
        }
    }
    private String getEventSource(){
        return "首页";
    }
}
