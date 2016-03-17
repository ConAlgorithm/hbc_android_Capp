package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;

import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by admin on 2016/3/2.
 */
public class HomeAdapter extends BaseAdapter<HomeBean> {

    private final ImageOptions options;

    public HomeAdapter(Context context) {
        super(context);
        options = new ImageOptions.Builder().setFailureDrawableId(R.mipmap.img_undertext).build();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_home, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.item_home_title);
            viewHolder.tvSubTitle = (TextView) view.findViewById(R.id.item_home_sub_title);
            viewHolder.imgBg = (ImageView) view.findViewById(R.id.item_home_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        HomeBean bean = getItem(position);
        if(bean!=null){
            viewHolder.tvTitle.setText(bean.mainTitle);
            viewHolder.tvSubTitle.setText(bean.subTitle);
            x.image().bind(viewHolder.imgBg,bean.picture,options);
        }

        return view;
    }

    final static class ViewHolder {
        TextView tvTitle;
        TextView tvSubTitle;
        ImageView imgBg;
    }

}
