package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.home.HomeAlbumItemView;
import com.hugboga.custom.widget.home.HomeMoreView;

import java.util.List;

/**
 * Created by qingcha on 17/11/23.
 */

public class HomeAlbumItemAdapter extends RecyclerView.Adapter<HomeAlbumItemAdapter.MyViewHolder>{

    private static final int TYPE_ITEM = 0x01;
    private static final int TYPE_MORE = 0x02;

    private Context mContext;
    private List<HomeBean.AlbumBean> itemList;
    private int displayImgWidth;
    private int displayImgHeight;
    private HomeBean.HotAlbumBean hotAlbumBean;

    public HomeAlbumItemAdapter(Context context, HomeBean.HotAlbumBean _hotAlbumBean, int displayImgWidth, int displayImgHeight) {
        this.mContext = context;
        this.itemList = _hotAlbumBean.albumRelItemList;
        this.hotAlbumBean = _hotAlbumBean;
        this.displayImgWidth = displayImgWidth;
        this.displayImgHeight = displayImgHeight;
    }

    public void setData(List<HomeBean.AlbumBean> _itemList) {
        this.itemList = _itemList;
        notifyDataSetChanged();
    }

    @Override
    public HomeAlbumItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch (viewType) {
            case TYPE_ITEM:
                HomeAlbumItemView homeAlbumItemView = new HomeAlbumItemView(mContext);
                itemView = homeAlbumItemView;
                homeAlbumItemView.setDesplayViewLayoutParams(displayImgWidth, displayImgHeight);
                break;
            case TYPE_MORE:
                HomeMoreView moreView = new HomeMoreView(mContext);
                moreView.setDescTest(mContext.getResources().getString(R.string.home_more));
                ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(displayImgWidth / 2, displayImgHeight);
                moreView.setLayoutParams(params);
                itemView = moreView;
                break;
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeAlbumItemAdapter.MyViewHolder holder, int position) {
        if (position == getItemCount() -1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebInfoActivity.class);
                    intent.putExtra("web_url", hotAlbumBean.albumLinkUrl);
                    mContext.startActivity(intent);
                    SensorsUtils.onAppClick("首页","热门专辑","首页-热门专辑");
                }
            });
        } else {
            ((HbcViewBehavior) holder.itemView).update(itemList.get(position));
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
        return itemList == null ? 0 : itemList.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }
}
