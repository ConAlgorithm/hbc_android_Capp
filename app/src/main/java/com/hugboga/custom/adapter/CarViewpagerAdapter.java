package com.hugboga.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.JazzyViewPager;
import com.hugboga.custom.widget.OutlineContainer;

import java.util.List;

/**
 * Created by admin on 2015/7/17.
 */
public class CarViewpagerAdapter extends PagerAdapter {
    private final LayoutInflater mInflater;
    private Activity activity;
    private JazzyViewPager mJazzy;
    private List<CarBean> mList;

    public CarViewpagerAdapter(Activity activity, JazzyViewPager mJazzy) {
        mInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.mJazzy = mJazzy;
    }

    public void setList(List<CarBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void addList(List<CarBean> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.item_viewpage_car, null);
        CarBean bean = mList.get(position);
        ImageView image = (ImageView) view.findViewById(R.id.item_car_img);
        int resId = bean.imgRes;
        if (resId != 0)
            image.setImageResource(resId);

        if(bean.special == 1 && null != bean.carPictures){
            Tools.showImage(image,bean.carPictures.get(0),R.mipmap.car_default);
        }
        //416  143
        TextView tv = (TextView) view.findViewById(R.id.item_car_title);
        tv.setText(bean.carDesc);
        container.addView(view);
        mJazzy.setObjectForPosition(view, position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        View view = mJazzy.findViewFromObject(position);
        container.removeView(view);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        if (view instanceof OutlineContainer) {
            return ((OutlineContainer) view).getChildAt(0) == obj;
        } else {
            return view == obj;
        }
    }

}
