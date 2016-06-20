package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeData;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HomeRouteItemView;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeChoicenessRouteAdapter extends RecyclerView.Adapter<HomeChoicenessRouteAdapter.MyViewHolder>{

    private FgHome frgment;

    private Context mContext;
    private ArrayList<HomeData.CityContentItem> itemList;
    private ViewGroup.LayoutParams params;

    public HomeChoicenessRouteAdapter(Context context) {
        this.mContext = context;
        final int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);

        int itemWidth = UIUtils.getScreenWidth() - paddingLeft * 2 - UIUtils.dip2px(20);
        int displayImgHeight = (int)((287 / 620.0) * (UIUtils.getScreenWidth() - context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left) * 2 - UIUtils.dip2px(20)));
        int itemHeight = displayImgHeight + UIUtils.dip2px(76) * 3 + 3 + UIUtils.dip2px(44);
        params = new ViewGroup.LayoutParams(itemWidth, itemHeight);
    }

    public void setData(FgHome _frgment, ArrayList<HomeData.CityContentItem> _itemList) {
        this.frgment = _frgment;
        this.itemList = _itemList;
        notifyDataSetChanged();
    }

    @Override
    public HomeChoicenessRouteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeRouteItemView imageView = new HomeRouteItemView(mContext);
        imageView.setFgHomeContext(frgment);
        imageView.setLayoutParams(params);
        return new MyViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(HomeChoicenessRouteAdapter.MyViewHolder holder, int position) {
        ((HomeRouteItemView)holder.itemView).update(itemList.get(position));
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
