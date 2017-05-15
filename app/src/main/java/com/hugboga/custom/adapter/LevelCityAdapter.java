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
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.utils.UIUtils;

import java.util.List;

public class LevelCityAdapter extends BaseAdapter<SearchGroupBean> {

    Context mContext;
    List<SearchGroupBean> list;
    int flag;
    boolean middleLineShow;
    boolean isFilter;

    CityListActivity.Params cityParams;

    public LevelCityAdapter(Context context,int flag) {
        super(context);
        mContext = context;
        this.flag = flag;
    }

    public void setMiddleLineShow(boolean middleLineShow) {
        this.middleLineShow = middleLineShow;
    }

    public void isFilter(boolean isFilter) {
        this.isFilter = isFilter;
    }

    public void setCityParams(CityListActivity.Params cityParams) {
        this.cityParams = cityParams;
        notifyDataSetChanged();
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
            viewHolder.has_sub_img = (ImageView)convertView.findViewById(R.id.has_sub_img);
            viewHolder.city_selected_img = (ImageView)convertView.findViewById(R.id.city_selected_img);
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
                viewHolder.name.setTextColor(Color.parseColor("#ffffff"));
            }else{
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
                viewHolder.image.setVisibility(View.GONE);
                viewHolder.name.setTextColor(Color.parseColor("#666666"));
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
                    viewHolder.middle_line.setVisibility(middleLineShow ? View.VISIBLE : View.GONE);
                    viewHolder.cityImg.setVisibility(View.GONE);
                    viewHolder.name.setText(getName(position, getItem(position).flag));
                }
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
                if(getItem(position).isSelected){
                    viewHolder.name.setTextColor(Color.parseColor("#fcd633"));
                }else{
                    viewHolder.name.setTextColor(Color.parseColor("#111111"));
                }

            }else {
                viewHolder.middle_line.setVisibility(middleLineShow ? View.VISIBLE : View.GONE);
                viewHolder.name.setText(getName(position, flag));
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
                if (getItem(position).isSelected) {
                    viewHolder.name.setTextColor(Color.parseColor("#fcd633"));
                    viewHolder.image.setVisibility(View.VISIBLE);
                    viewHolder.image.setImageResource(R.mipmap.search_triangle2);
                } else {
                    viewHolder.name.setTextColor(Color.parseColor("#111111"));
                    viewHolder.image.setVisibility(View.GONE);
                }
            }
        }else if(flag == 3){
            viewHolder.middle_line.setVisibility(View.VISIBLE);
            if(position == 0){
                viewHolder.name.setText("全境");
            }else {
                viewHolder.name.setText(getName(position, flag));
            }
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            if(getItem(position).isSelected){
                viewHolder.name.setTextColor(Color.parseColor("#fcd633"));
            }else{
                viewHolder.name.setTextColor(Color.parseColor("#111111"));
            }
        }

        if(getItem(position).has_sub == 1 && flag == 2 && position != 0 && !getItem(position).isSelected){
            viewHolder.has_sub_img.setVisibility(View.VISIBLE);
        }else{
            viewHolder.has_sub_img.setVisibility(View.GONE);
        }

        boolean isShowSelectedImg = (flag == 2 || flag == 3) && (getItem(position).has_sub != 1 || flag == 3 && position == 0);

        boolean isSelectedItem = isSelectedItem(position, flag);
        if ((isShowSelectedImg && isFilter) || (isSelectedItem && isShowSelectedImg)) {
            if (isSelectedItem && isShowSelectedImg) {
                getItem(position).isSelected = true;
                viewHolder.name.setTextColor(Color.parseColor("#fcd633"));
            } else{
                getItem(position).isSelected = false;
                viewHolder.name.setTextColor(Color.parseColor("#111111"));
            }
            if (getItem(position).isSelected) {
                viewHolder.name.setPadding(UIUtils.dip2px(20), 0, UIUtils.dip2px(18), 0);
                viewHolder.city_selected_img.setVisibility(View.VISIBLE);
            } else {
                viewHolder.name.setPadding(UIUtils.dip2px(20), 0, UIUtils.dip2px(5), 0);
                viewHolder.city_selected_img.setVisibility(View.GONE);
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

    public boolean isSelectedItem(int position, int _flag) {
        if (cityParams == null) {
            return false;
        }
        int flag = getItem(position).flag == 4 ? 4 : _flag;
        SearchGroupBean searchGroupBean = getItem(position);
        if (flag == 1 && cityParams.cityHomeType == CityListActivity.CityHomeType.ROUTE) {
            return searchGroupBean.group_id == cityParams.id;
        } else if (flag == 2) {
            if (searchGroupBean.type == 1 && cityParams.cityHomeType == CityListActivity.CityHomeType.ROUTE) {
                return searchGroupBean.group_id == cityParams.id;
            } else if (searchGroupBean.type == 2 && cityParams.cityHomeType == CityListActivity.CityHomeType.COUNTRY) {
                return searchGroupBean.sub_place_id == cityParams.id;
            } else if (searchGroupBean.type == 3 && cityParams.cityHomeType == CityListActivity.CityHomeType.CITY) {
                return searchGroupBean.sub_city_id == cityParams.id;
            }
        } else if (flag == 3) {
            if (searchGroupBean.type == 1 && cityParams.cityHomeType == CityListActivity.CityHomeType.ROUTE) {
                return searchGroupBean.group_id == cityParams.id;
            } else if (searchGroupBean.type == 2 && cityParams.cityHomeType == CityListActivity.CityHomeType.COUNTRY) {
                return searchGroupBean.sub_place_id == cityParams.id;
            } else if (searchGroupBean.type == 3 && cityParams.cityHomeType == CityListActivity.CityHomeType.CITY){
                return searchGroupBean.sub_city_id == cityParams.id;
            }
        } else if (flag == 4) {
            if (searchGroupBean.type == 1 && cityParams.cityHomeType == CityListActivity.CityHomeType.CITY) {
                return searchGroupBean.spot_id == cityParams.id;
            } else if (searchGroupBean.type == 2 && cityParams.cityHomeType == CityListActivity.CityHomeType.COUNTRY) {
                return searchGroupBean.spot_id == cityParams.id;
            } else if (cityParams.cityHomeType == CityListActivity.CityHomeType.ALL) {
                return searchGroupBean.spot_id ==cityParams.id;
            }
        }
        return false;
    }

    private static class ViewHolder{
        TextView name;
        ImageView image;
        ImageView cityImg;
        TextView middle_line;
        ImageView has_sub_img;
        ImageView city_selected_img;
    }
}
