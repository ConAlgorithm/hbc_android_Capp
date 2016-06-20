package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.LoopViewPager;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeBannerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> imgList;
    private ViewGroup.LayoutParams itemParams;

    public HomeBannerAdapter(Context mContext, ArrayList<String> _imgList) {
        this.mContext = mContext;
        this.imgList = _imgList;
        itemParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setData(imgList, null);
    }

    public void setData(ArrayList<String> _imgList, LoopViewPager viewPager) {
        if (_imgList != null) {
            imgList = _imgList;
        }
        if (viewPager != null && viewPager.getWrapperAdapter() != null) {
            viewPager.setAdapter(this);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (imgList == null) {
            return super.instantiateItem(container, position);
        }
        ImageView itemView = new ImageView(mContext);
//        Glide.with(mContext)
//                .load(imgList.get(position))
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(itemView);
        Tools.showImageCenterCrop(itemView, imgList.get(position));
        itemView.setLayoutParams(itemParams);
        container.addView(itemView, 0);
        return itemView;
    }

    @Override
    public int getCount() {
        return imgList == null ? 0 : imgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewGroup) container.getParent()).removeView((View)object);
    }
}
