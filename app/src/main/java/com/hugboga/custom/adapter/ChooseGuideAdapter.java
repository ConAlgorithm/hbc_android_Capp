package com.hugboga.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CanServiceGuideBean;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.SimpleRatingBar;

/**
 * Created on 16/9/9.
 */

public class ChooseGuideAdapter extends BaseAdapter {


    Context context;
    public ChooseGuideAdapter(Activity context) {
        super(context);
        this.context = context;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.canservice_guide_item, null);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.city = (TextView) view.findViewById(R.id.city);
            viewHolder.head = (ImageView) view.findViewById(R.id.head);
            viewHolder.score = (TextView) view.findViewById(R.id.score);
            viewHolder.noScore = (TextView) view.findViewById(R.id.no_score);
            viewHolder.tips = (TextView) view.findViewById(R.id.tips);
            viewHolder.auth = (ImageView) view.findViewById(R.id.auth);
            viewHolder.carType = (TextView) view.findViewById(R.id.cartype);
            viewHolder.ratingBar = (SimpleRatingBar)view.findViewById(R.id.ratingview);
            viewHolder.choose = (TextView)view.findViewById(R.id.choose);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CanServiceGuideBean.GuidesBean model = (CanServiceGuideBean.GuidesBean) getItem(position);
        viewHolder.name.setText(model.getGuideName());
        viewHolder.city.setText(model.getCityName());
        viewHolder.carType.setText(model.getCarBrandName()+ model.getCarName());
        viewHolder.ratingBar.setRating((float)model.getServiceStar());
        if(model.getOrderCounts() == 0) {
            viewHolder.score.setText(model.getServiceStar() + "分");
        }else{
            viewHolder.score.setText(model.getServiceStar() + "分/"+model.getOrderCounts()+"单");
        }
        if(model.getGender() == 1){
            viewHolder.name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.man_icon,0);
            viewHolder.choose.setText("选他服务");
        }else{
            viewHolder.name.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.woman_icon,0);
            viewHolder.choose.setText("选她服务");
        }

        Tools.showCircleImage(context,viewHolder.head,model.getAvatarS());

        return view;

    }


    final static class ViewHolder {
        TextView name;
        TextView city;
        TextView score;
        TextView noScore;
        TextView tips;
        ImageView auth;
        TextView carType;
        TextView choose;
        ImageView head;
        SimpleRatingBar ratingBar;
    }

}