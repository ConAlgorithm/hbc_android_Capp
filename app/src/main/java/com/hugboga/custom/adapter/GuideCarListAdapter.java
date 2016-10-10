package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.widget.GuideCarInfoItemView;

/**
 * Created by qingcha on 16/10/8.
 */
public class GuideCarListAdapter extends BaseAdapter<GuideCarBean> {

    public GuideCarListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            GuideCarInfoItemView itemView = new GuideCarInfoItemView(mContext);
            convertView = itemView;
            holder = new ViewHolder();
            holder.itemView = itemView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemView.update(getItem(position));
        return convertView;
    }

    class ViewHolder {
        private GuideCarInfoItemView itemView;
    }
}