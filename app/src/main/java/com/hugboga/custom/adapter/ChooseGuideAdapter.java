package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.data.bean.CanServiceGuideBean;
import com.hugboga.custom.widget.ChooseGuideView;

/**
 * Created on 16/9/9.
 */
public class ChooseGuideAdapter extends BaseAdapter<CanServiceGuideBean.GuidesBean> {

    public ChooseGuideAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChooseGuideAdapter.ViewHolder holder = null;

        if (convertView == null) {
            ChooseGuideView itemView = new ChooseGuideView(mContext);
            convertView = itemView;
            holder = new ChooseGuideAdapter.ViewHolder();
            holder.itemView = itemView;
            convertView.setTag(holder);
        } else {
            holder = (ChooseGuideAdapter.ViewHolder) convertView.getTag();
        }

        holder.itemView.update(getItem(position));
        return convertView;
    }

    class ViewHolder {
        private ChooseGuideView itemView;
    }
}