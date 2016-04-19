package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityBean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/2.
 */
public class HotCityGridViewAdapter extends BaseAdapter {

    private List<CityBean> hotList = null;
    private Context mContext;

    public HotCityGridViewAdapter(Context mContext, List<CityBean> list) {
        this.mContext = mContext;
        hotList = list;
    }

    @Override
    public int getCount() {
        return hotList == null ? 0 : hotList.size();
    }

    @Override
    public Object getItem(int position) {
        return hotList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
//        Integer integer = Integer.valueOf(position);
        CityBean cityBean = hotList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.hot_city_gridview_item, null);
            viewHolder.tv_hot_city_item = (TextView) convertView.findViewById(R.id.tv_hot_city_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (cityBean != null && !TextUtils.isEmpty(cityBean.name)) {
            viewHolder.tv_hot_city_item.setText(cityBean.name);
        }
        return convertView;
    }

    final static class ViewHolder {
        TextView tv_hot_city_item;
    }
}
