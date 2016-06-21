package com.hugboga.custom.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.LineGroupBean;

import java.util.List;

public class LevelCityAdapter extends BaseAdapter<LineGroupBean> {

    Context mContext;
    List<LineGroupBean> list;
    public LevelCityAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void setList(List list) {
        this.list = list;
    }

    @Override
    public void addList(List list) {
        this.list.addAll(list);
    }

    @Override
    public int getCount() {
        if(null == list) return  0;
        return list.size();
    }

    @Override
    public LineGroupBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(null == convertView){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.level_city_layout,null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.city_name);
            viewHolder.image = (ImageView)convertView.findViewById(R.id.right_img);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.name.setText(getItem(position).group_name);
        if(getItem(position).isSelected){
            convertView.setBackgroundColor(Color.parseColor("#fcd633"));
            viewHolder.image.setVisibility(View.VISIBLE);
        }else{
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            viewHolder.image.setVisibility(View.GONE);
        }


        return convertView;
    }

    private static class ViewHolder{
        TextView name;
        ImageView image;
    }
}
