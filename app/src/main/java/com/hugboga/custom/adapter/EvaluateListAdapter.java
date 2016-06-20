package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.data.bean.EvaluateItemData;
import com.hugboga.custom.widget.EvaluateListItemView;

/**
 * Created by qingcha on 16/6/18.
 */
public class EvaluateListAdapter extends BaseAdapter<EvaluateItemData> {

    public EvaluateListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            EvaluateListItemView itemView = new EvaluateListItemView(mContext);
            convertView = itemView;
            holder = new ViewHolder();
            holder.itemView = itemView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.itemView.setData(getItem(position));
        return convertView;
    }

    class ViewHolder {
        private EvaluateListItemView itemView;
    }
}
