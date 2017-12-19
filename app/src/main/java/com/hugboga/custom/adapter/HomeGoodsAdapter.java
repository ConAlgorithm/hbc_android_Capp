package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.R;
import com.hugboga.custom.models.home.HomeGoodsModel;
import com.hugboga.custom.utils.IntentUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.home.HomeGoodsItemView;
import com.hugboga.custom.widget.home.HomeMoreView;

import java.util.List;

/**
 * Created by qingcha on 17/11/24.
 */

public class HomeGoodsAdapter<T> extends RecyclerView.Adapter<HomeGoodsAdapter.MyViewHolder>{

    private static final int TYPE_ITEM = 0x01;
    private static final int TYPE_MORE = 0x02;

    private Context mContext;
    private List<T> itemList;
    private int type;
    private int displayImgWidth;
    private int displayImgHeight;

    public HomeGoodsAdapter(Context context, List<T> _itemList, int _type, int displayImgWidth, int displayImgHeight) {
        this.mContext = context;
        this.itemList = _itemList;
        this.type = _type;
        this.displayImgWidth = displayImgWidth;
        this.displayImgHeight = displayImgHeight;
    }

    public void setData(List<T> _itemList) {
        this.itemList = _itemList;
        notifyDataSetChanged();
    }

    @Override
    public HomeGoodsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch (viewType) {
            case TYPE_ITEM:
                HomeGoodsItemView homeGoodsView = new HomeGoodsItemView(mContext);
                itemView = homeGoodsView;
                homeGoodsView.setDesplayViewLayoutParams(displayImgWidth, displayImgHeight);
                break;
            case TYPE_MORE:
                HomeMoreView moreView = new HomeMoreView(mContext);
                moreView.setDescTest(mContext.getResources().getString(R.string.home_more));
                ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(displayImgWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                moreView.setLayoutParams(params);
                itemView = moreView;
                break;
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeGoodsAdapter.MyViewHolder holder, int position) {
        if (position == getItemCount() -1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == HomeGoodsModel.TYPE_TRANSFER) {
                        IntentUtils.intentPickupActivity(mContext, "首页");
                    } else {
                        IntentUtils.intentCharterActivity(mContext, "首页");
                    }
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

    public String getEventSource() {
        return "首页";
    }
}