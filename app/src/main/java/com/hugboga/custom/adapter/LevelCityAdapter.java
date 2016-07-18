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
    int flag;
    public LevelCityAdapter(Context context,int flag) {
        super(context);
        mContext = context;
        this.flag = flag;
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
            viewHolder.cityImg = (ImageView)convertView.findViewById(R.id.city_img);
            viewHolder.middle_line = (TextView)convertView.findViewById(R.id.middle_line);
            viewHolder.right_line = (TextView)convertView.findViewById(R.id.right_line);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(flag == 1){
            viewHolder.name.setText(getName(position,flag));
            if(getItem(position).isSelected){
                convertView.setBackgroundColor(Color.parseColor("#fcd633"));
                viewHolder.image.setVisibility(View.VISIBLE);
                viewHolder.image.setImageResource(R.mipmap.search_triangle);
            }else{
                convertView.setBackgroundColor(Color.parseColor("#edeeef"));
                viewHolder.image.setVisibility(View.GONE);
            }
        }else if(flag == 2){

            if(getItem(position).flag == 4){
                viewHolder.middle_line.setVisibility(View.GONE);
                if(getItem(position).spot_id == -1){
                    viewHolder.name.setText("");
                    viewHolder.cityImg.setVisibility(View.VISIBLE);
                    viewHolder.cityImg.setImageResource(R.mipmap.search_transfer);
                }else if(getItem(position).spot_id == -2){
                    viewHolder.name.setText("");
                    viewHolder.cityImg.setVisibility(View.VISIBLE);
                    viewHolder.cityImg.setImageResource(R.mipmap.search_single);
                }else if(getItem(position).spot_id == -3){
                    viewHolder.name.setText("");
                    viewHolder.cityImg.setVisibility(View.VISIBLE);
                    viewHolder.cityImg.setImageResource(R.mipmap.search_car);
                }else {
                    viewHolder.middle_line.setVisibility(View.VISIBLE);
                    viewHolder.cityImg.setVisibility(View.GONE);
                    viewHolder.name.setText(getName(position, getItem(position).flag));
                }
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
                if(getItem(position).isSelected){
                    viewHolder.name.setTextColor(Color.parseColor("#fcd633"));
                }else{
                    viewHolder.name.setTextColor(Color.parseColor("#666666"));
                }

            }else {

                viewHolder.middle_line.setVisibility(View.VISIBLE);
                if(position == 0){
                    viewHolder.name.setText("全境");
                }else {
                    viewHolder.name.setText(getName(position, flag));
                }
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
                if (getItem(position).isSelected) {
                    viewHolder.name.setTextColor(Color.parseColor("#fcd633"));
                    viewHolder.image.setVisibility(View.VISIBLE);
                    viewHolder.image.setImageResource(R.mipmap.search_triangle2);
                } else {
                    viewHolder.name.setTextColor(Color.parseColor("#666666"));
                    viewHolder.image.setVisibility(View.GONE);
                }
            }
        }else if(flag == 3){
            viewHolder.middle_line.setVisibility(View.GONE);
            viewHolder.right_line.setVisibility(View.VISIBLE);
            if(position == 0){
                viewHolder.name.setText("全境");
            }else {
                viewHolder.name.setText(getName(position, flag));
            }
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            if(getItem(position).isSelected){
                viewHolder.name.setTextColor(Color.parseColor("#fcd633"));
            }else{
                viewHolder.name.setTextColor(Color.parseColor("#666666"));
            }
        }

        return convertView;
    }


    public  String getName(int position,int flag){
        if(flag == 2){
            if(getItem(position).type == 1){
                return getItem(position).group_name;
            }else if(getItem(position).type == 2){
                return getItem(position).sub_place_name;
            }else if(getItem(position).type == 3){
                return getItem(position).sub_city_name;
            }
        }else if(flag == 3){
            if(getItem(position).type == 1){
                return getItem(position).group_name;
            }else if(getItem(position).type == 2){
                return getItem(position).sub_place_name;
            }else if(getItem(position).type == 3){
                return getItem(position).sub_city_name;
            }
        }else if(flag == 1){
            return getItem(position).group_name;
        }else if(flag == 4){
            return getItem(position).spot_name;
        }
        return "";

    }

    private static class ViewHolder{
        TextView name;
        ImageView image;
        ImageView cityImg;
        TextView middle_line;
        TextView right_line;
    }
}
