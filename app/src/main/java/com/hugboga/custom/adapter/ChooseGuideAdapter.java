package com.hugboga.custom.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CanServiceGuideListActivity;
import com.hugboga.custom.activity.GuideDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CanServiceGuideBean;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.SimpleRatingBar;

/**
 * Created on 16/9/9.
 */

public class ChooseGuideAdapter extends BaseAdapter<CanServiceGuideBean.GuidesBean> {


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
            viewHolder.auth = (ImageView) view.findViewById(R.id.auth);
            viewHolder.ratingBar = (SimpleRatingBar)view.findViewById(R.id.ratingview);
            viewHolder.choose = (TextView)view.findViewById(R.id.choose);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final CanServiceGuideBean.GuidesBean model = (CanServiceGuideBean.GuidesBean) getItem(position);
        viewHolder.name.setText(model.getGuideName());
        String cityStr = model.getCityName();
        if (!TextUtils.isEmpty(model.getCarBrandName())) {//+ model.getCarName());
            cityStr += "  " + model.getCarBrandName();
        }
        viewHolder.city.setText(cityStr);
        if(model.getServiceStar() == 0 || model.getOrderCounts() == 0) {
            viewHolder.score.setText("暂无评价");
            viewHolder.ratingBar.setVisibility(View.GONE);
        }else{
            viewHolder.ratingBar.setVisibility(View.VISIBLE);
            viewHolder.ratingBar.setRating((float)model.getServiceStar());
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

        viewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof CanServiceGuideListActivity) {
                    ((CanServiceGuideListActivity) context).intentGuideDetail(model);
                }
            }
        });
        viewHolder.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof CanServiceGuideListActivity) {
                    ((CanServiceGuideListActivity) context).chooseGuide(model);
                }
            }
        });
        return view;

    }


    final static class ViewHolder {
        TextView name;
        TextView city;
        TextView score;
        ImageView auth;
        TextView choose;
        ImageView head;
        SimpleRatingBar ratingBar;
    }

}