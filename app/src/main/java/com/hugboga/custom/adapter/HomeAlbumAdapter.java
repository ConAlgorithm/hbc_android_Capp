package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.bean.HomeAlbumInfoVo;
import com.hugboga.custom.widget.AlbumItemView;
import com.hugboga.custom.widget.FilterGuideItemView;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

/**
 * Created by zhangqiang on 17/8/2.
 */


public class HomeAlbumAdapter extends RecyclerView.Adapter<HomeAlbumAdapter.MyViewHolder> {

    private Context mContext;
    private int displayImgWidth;
    private int displayImgHeight;
    HomeAlbumInfoVo homeAlbumInfoVo;
    public HomeAlbumAdapter(Context context,int displayImgWidth,int displayImgHeight,HomeAlbumInfoVo homeAlbumInfoVo){
        this.mContext = context;
        this.displayImgWidth = displayImgWidth;
        this.displayImgHeight = displayImgHeight;
        this.homeAlbumInfoVo = homeAlbumInfoVo;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        AlbumItemView albumItemView = new AlbumItemView(mContext);
        albumItemView.setImageBound(displayImgWidth, displayImgHeight);
        itemView = albumItemView;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(displayImgWidth,  ScreenUtil.dip2px(226));
        itemView.setLayoutParams(params);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ((HbcViewBehavior) holder.itemView).update(homeAlbumInfoVo.albumRelGoods.get(position));
    }

    @Override
    public int getItemCount() {
        return homeAlbumInfoVo==null ? 0 : homeAlbumInfoVo.albumRelGoods.size();
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
}
