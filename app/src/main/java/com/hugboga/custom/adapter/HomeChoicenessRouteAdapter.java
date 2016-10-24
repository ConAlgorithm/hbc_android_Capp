package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.HomeRouteFreeItemView;
import com.hugboga.custom.widget.HomeRouteItemView;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeChoicenessRouteAdapter extends RecyclerView.Adapter<HomeChoicenessRouteAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<HomeBean.TraveLineItem> itemList;
    private ViewGroup.LayoutParams params;
    private int type; // 1、包车线路游(超自由); 2、包车畅游(超省心)

    public HomeChoicenessRouteAdapter(Context context, int _type) {
        this.mContext = context;
        this.type = _type;

        double displayImgWidth = UIUtils.getScreenWidth() * (620 / 720.0);
        params = new ViewGroup.LayoutParams((int)displayImgWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setData(ArrayList<HomeBean.TraveLineItem> _itemList) {
        this.itemList = _itemList;
        notifyDataSetChanged();
    }

    @Override
    public HomeChoicenessRouteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (type == 1) {
            itemView = new HomeRouteItemView(mContext);
        } else {
            itemView = new HomeRouteFreeItemView(mContext);
        }
        itemView.setLayoutParams(params);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeChoicenessRouteAdapter.MyViewHolder holder, int position) {
        ((HbcViewBehavior) holder.itemView).update(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }
}
