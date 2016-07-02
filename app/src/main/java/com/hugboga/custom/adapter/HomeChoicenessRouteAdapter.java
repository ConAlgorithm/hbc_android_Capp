package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
public class HomeChoicenessRouteAdapter extends PagerAdapter {

    private FgHome frgment;

    private Context mContext;
    private ArrayList<HomeData.CityContentItem> itemList;

    public HomeChoicenessRouteAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(FgHome _frgment, ArrayList<HomeData.CityContentItem> _itemList) {
        this.frgment = _frgment;
        this.itemList = _itemList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        HomeRouteItemView itemView = new HomeRouteItemView(mContext);
        itemView.setFgHomeContext(frgment);
        itemView.update(itemList.get(position));
        container.addView(itemView, 0);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
