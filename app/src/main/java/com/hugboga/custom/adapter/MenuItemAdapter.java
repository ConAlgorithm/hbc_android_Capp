package com.hugboga.custom.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.LvMenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/3/9.
 */
public class MenuItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<LvMenuItem> mItems;

    public MenuItemAdapter(Context context,List<LvMenuItem> mItems)
    {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mItems = mItems;
    }

    @Override
    public int getCount()
    {
        return mItems.size();
    }


    @Override
    public Object getItem(int position)
    {
        return mItems.get(position);
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.slide_menu_item, parent,false);
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.icon);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.tips = (TextView)convertView.findViewById(R.id.tips);
            convertView.setTag(viewHolder);
        }else{
            convertView.getTag();
        }

        LvMenuItem item = mItems.get(position);
        viewHolder.icon.setImageResource(item.icon);
        viewHolder.title.setText(item.name);
        if(!TextUtils.isEmpty(item.tips)){
            viewHolder.tips.setText(item.tips);
        }

        return convertView;
    }

    final static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView tips;
    }

}
