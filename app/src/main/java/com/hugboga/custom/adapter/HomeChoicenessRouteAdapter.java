package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HomeRouteItemView;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeChoicenessRouteAdapter extends RecyclerView.Adapter<HomeChoicenessRouteAdapter.MyViewHolder>{

    private Context mContext;
    private ArrayList<String> urlList;
    private ViewGroup.LayoutParams params;

    public HomeChoicenessRouteAdapter(Context context) {
        this.mContext = context;
        final int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        int itemWidth = UIUtils.getScreenWidth() - paddingLeft * 2 - UIUtils.dip2px(20);
        params = new ViewGroup.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public void setData(ArrayList<String> _urlList) {
        this.urlList = _urlList;
        notifyDataSetChanged();
    }

    @Override
    public HomeChoicenessRouteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeRouteItemView imageView = new HomeRouteItemView(mContext);
        imageView.setLayoutParams(params);
        return new MyViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(HomeChoicenessRouteAdapter.MyViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return urlList == null ? 0 : urlList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }
}
