package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.HomeRouteFreeItemView;
import com.hugboga.custom.widget.HomeRouteItemView;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeChoicenessRouteAdapter extends RecyclerView.Adapter<HomeChoicenessRouteAdapter.MyViewHolder>{

    private static final int TYPE_ITEM = 0x01;
    private static final int TYPE_MORE = 0x02;

    private Context mContext;
    private ArrayList<SkuItemBean> itemList;
    private ViewGroup.LayoutParams params;
    private int type; // 1、包车线路游(超自由); 2、包车畅游(超省心)
    private double displayImgWidth;

    public HomeChoicenessRouteAdapter(Context context, int _type) {
        this.mContext = context;
        this.type = _type;

        displayImgWidth = UIUtils.getScreenWidth() * (620 / 720.0);
        params = new ViewGroup.LayoutParams((int)displayImgWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setData(ArrayList<SkuItemBean> _itemList) {
        this.itemList = _itemList;
        notifyDataSetChanged();
    }

    @Override
    public HomeChoicenessRouteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;
        switch (viewType) {
            case TYPE_ITEM:
                if (type == 1) {
                    itemView = new HomeRouteItemView(mContext);
                } else {
                    itemView = new HomeRouteFreeItemView(mContext);
                }
                itemView.setLayoutParams(params);
                break;
            case TYPE_MORE:
                FrameLayout frameLayout = new FrameLayout(mContext);
                ImageView moreIV = new ImageView(mContext);
                FrameLayout.LayoutParams moreIVParams = null;
                if (type == 1) {
                    moreIV.setBackgroundResource(R.mipmap.home_route_more);
                    double displayImgHeight = (330 / 620.0) * displayImgWidth;
                    moreIVParams= new FrameLayout.LayoutParams((int)displayImgHeight, (int)displayImgHeight);
                    moreIVParams.topMargin = UIUtils.dip2px(8);

                } else {
                    moreIV.setBackgroundResource(R.mipmap.home_route_free_more);
                    double displayImgHeight = (218 / 310.0) * (displayImgWidth / 2);
                    double moreImgWidth = (342 / 218.0) * displayImgHeight;
                    moreIVParams= new FrameLayout.LayoutParams((int)moreImgWidth, (int)displayImgHeight);
                    moreIVParams.topMargin = UIUtils.dip2px(92);
                }
                frameLayout.addView(moreIV, moreIVParams);
                itemView = frameLayout;
                break;
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeChoicenessRouteAdapter.MyViewHolder holder, int position) {
        if (position == getItemCount() -1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChooseCityNewActivity.class);
                    intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
                    intent.putExtra("isHomeIn", true);
                    intent.putExtra("source", type == 1 ? "首页-线路包车" : "首页-包车畅游");
                    v.getContext().startActivity(intent);
                    StatisticClickEvent.click(StatisticConstant.SEARCH_LAUNCH, "首页");
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
