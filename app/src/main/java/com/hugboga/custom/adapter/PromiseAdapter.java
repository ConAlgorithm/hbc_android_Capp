package com.hugboga.custom.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.PromiseBean;

/**
 * Created by admin on 2015/8/3.
 */
public class PromiseAdapter extends BaseAdapter {

    public PromiseAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if(view==null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_promise, null);
            viewHolder.icon = (ImageView) view.findViewById(R.id.item_promise_icon);
            viewHolder.title = (TextView) view.findViewById(R.id.item_promise_title);
            viewHolder.content = (TextView) view.findViewById(R.id.item_promise_content);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        PromiseBean bean = (PromiseBean) getItem(position);
        viewHolder.icon.setImageResource(bean.icon);
        viewHolder.title.setText(mContext.getString(bean.title));
        viewHolder.content.setText(mContext.getString(bean.content));
        return view;
    }
    class ViewHolder{
        ImageView icon;
        TextView title;
        TextView content;

    }
}
