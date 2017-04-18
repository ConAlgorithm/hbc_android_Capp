package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.HotLinesItemView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/4/17.
 */
public class CityListHotAdapter extends RecyclerView.Adapter<CityListHotAdapter.MyViewHolder>{

    private static final int TYPE_ITEM = 0x01;
    private static final int TYPE_MORE = 0x02;

    private Context mContext;
    private List<SkuItemBean> itemList;
    private int displayImgWidth;
    private int displayImgHeight;

    public CityListHotAdapter(Context context, List<SkuItemBean> _itemList, int displayImgWidth, int displayImgHeight) {
        this.mContext = context;
        this.itemList = _itemList;
        this.displayImgWidth = displayImgWidth;
        this.displayImgHeight = displayImgHeight;
    }

    public void setData(ArrayList<SkuItemBean> _itemList) {
        this.itemList = _itemList;
        notifyDataSetChanged();
    }

    @Override
    public CityListHotAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch (viewType) {
            case TYPE_ITEM:
                HotLinesItemView hotLinesItemView = new HotLinesItemView(mContext);
                hotLinesItemView.setImageBound(displayImgWidth, displayImgHeight);
                itemView = hotLinesItemView;
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(displayImgWidth, displayImgHeight + ScreenUtil.dip2px(84));
                itemView.setLayoutParams(params);
                break;
            case TYPE_MORE:
                FrameLayout frameLayout = new FrameLayout(mContext);
                ImageView moreIV = new ImageView(mContext);
                moreIV.setBackgroundResource(R.mipmap.home_more);
                FrameLayout.LayoutParams moreIVParams= new FrameLayout.LayoutParams(displayImgWidth, displayImgHeight);
                frameLayout.addView(moreIV, moreIVParams);
                itemView = frameLayout;
                break;
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CityListHotAdapter.MyViewHolder holder, int position) {
        if (position == getItemCount() -1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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