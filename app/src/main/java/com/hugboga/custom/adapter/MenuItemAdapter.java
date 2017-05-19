package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.LvMenuItem;

import java.util.List;

/**
 * Created by Administrator on 2016/3/9.
 */
public class MenuItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<LvMenuItem> mItems;

    public MenuItemAdapter(Context context, List<LvMenuItem> mItems) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mItems = mItems;
    }

    public enum ItemType {
        DEFAULT, SPACE, SERVICE;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public int getViewTypeCount() {
        return ItemType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems != null && mItems.get(position) != null) {
            return mItems.get(position).itemType.ordinal();
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (mItems == null || mItems.get(position) == null || mItems.get(position).itemType == null) {
            return new View(mContext);
        }

        switch (mItems.get(position).itemType) {
            case DEFAULT:
                ViewHolder viewHolder = null;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.slide_menu_item, parent, false);
                    viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
                    viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                LvMenuItem item = mItems.get(position);
                viewHolder.icon.setImageResource(item.icon);
                viewHolder.title.setText(item.name);
                break;
            case SPACE:
                SpaceViewHolder spaceViewHolder = null;
                if (convertView == null) {
                    spaceViewHolder = new SpaceViewHolder();
                    convertView = new View(mContext);
                    spaceViewHolder.spaceView = convertView;
                    convertView.setTag(spaceViewHolder);
                } else {
                    spaceViewHolder = (SpaceViewHolder) convertView.getTag();
                }
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT
                        , mContext.getResources().getDimensionPixelOffset(R.dimen.basic_margin_large));
                spaceViewHolder.spaceView.setLayoutParams(params);
                spaceViewHolder.spaceView.setFocusable(true);
                break;
            case SERVICE:
                ViewHolder serviceViewHolder = null;
                if (convertView == null) {
                    serviceViewHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.slide_menu_item_service, parent, false);
                    serviceViewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
                    serviceViewHolder.title = (TextView) convertView.findViewById(R.id.title);
                    convertView.setTag(serviceViewHolder);
                } else {
                    serviceViewHolder = (ViewHolder) convertView.getTag();
                }
                LvMenuItem serviceItem = mItems.get(position);
                serviceViewHolder.icon.setImageResource(serviceItem.icon);
                serviceViewHolder.title.setText(serviceItem.name);
                break;
            default:
                break;
        }


        return convertView;
    }

    final static class ViewHolder {
        ImageView icon;
        TextView title;
    }

    final static class SpaceViewHolder {
        View spaceView;
    }
}
