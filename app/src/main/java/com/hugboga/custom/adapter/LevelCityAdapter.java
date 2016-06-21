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
import com.hugboga.custom.data.bean.SearchGroupBean;

import java.util.List;

public class LevelCityAdapter extends BaseAdapter<SearchGroupBean> {

    Context mContext;
    List<SearchGroupBean> list;
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
    public SearchGroupBean getItem(int position) {
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

        if(getItem(position).flag == 1){
            viewHolder.name.setText(getName(position,getItem(position).type));
            if(getItem(position).isSelected){
                convertView.setBackgroundColor(Color.parseColor("#fcd633"));
                viewHolder.image.setVisibility(View.VISIBLE);
                viewHolder.image.setImageResource(R.mipmap.search_triangle);
            }else{
                convertView.setBackgroundColor(Color.parseColor("#edeeef"));
                viewHolder.image.setVisibility(View.GONE);
            }
        }else if(getItem(position).flag == 2){
            viewHolder.name.setText(getName(position,getItem(position).type));
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            if(getItem(position).isSelected){
                viewHolder.name.setTextColor(Color.parseColor("#fcd633"));
                viewHolder.image.setVisibility(View.VISIBLE);
                viewHolder.image.setImageResource(R.mipmap.search_triangle2);
            }else{
                viewHolder.name.setTextColor(Color.parseColor("#000000"));
                viewHolder.image.setVisibility(View.GONE);
            }
        }else if(getItem(position).flag == 3){
            viewHolder.name.setText(getName(position,getItem(position).type));
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            if(getItem(position).isSelected){
                viewHolder.name.setTextColor(Color.parseColor("#fcd633"));
            }else{
                viewHolder.name.setTextColor(Color.parseColor("#000000"));
            }
        }

        return convertView;
    }


    private String getName(int position,int type){
        if(type == 2){
            return getItem(position).sub_place_name;
        }else if(type == 3){
            return getItem(position).sub_city_name;
        }else{
            return getItem(position).group_name;
        }
    }

    private static class ViewHolder{
        TextView name;
        ImageView image;
    }
}
