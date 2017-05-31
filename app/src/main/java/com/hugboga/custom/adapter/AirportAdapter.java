package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.utils.UIUtils;

import java.util.List;

public class AirportAdapter extends BaseAdapter {
    private List<AirPort> list = null;
    private Context mContext;

    public AirportAdapter(Context mContext, List<AirPort> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<AirPort> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<AirPort> list) {
        this.list.addAll(0,list);
        notifyDataSetChanged();
    }

    public int getCount() {
        return list == null ? 0 : this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_sortlist_city, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.lineView = view.findViewById(R.id.line_view);
            viewHolder.yellowLineView = view.findViewById(R.id.line_yellow_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        AirPort model = list.get(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (model.isFirst) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            if ("热门机场".equals(model.cityFirstLetter) || "搜索历史".equals(model.cityFirstLetter)) {
                viewHolder.lineView.setVisibility(View.VISIBLE);
                viewHolder.yellowLineView.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(model.cityFirstLetter);
                viewHolder.tvLetter.setTextSize(13);
                viewHolder.tvLetter.setTextColor(0xFF7F7F7F);
                viewHolder.tvLetter.setBackgroundColor(0x00000000);
                viewHolder.tvLetter.setPadding(UIUtils.dip2px(12), UIUtils.dip2px(12), 0, UIUtils.dip2px(6));
            } else {
                viewHolder.lineView.setVisibility(View.GONE);
                viewHolder.yellowLineView.setVisibility(View.GONE);
                viewHolder.tvLetter.setText("  " + model.cityFirstLetter);
                viewHolder.tvLetter.setTextColor(mContext.getResources().getColor(R.color.default_yellow));
                viewHolder.tvLetter.setTextSize(15);
                viewHolder.tvLetter.setBackgroundColor(0xFFF7F7F7);
                viewHolder.tvLetter.setPadding(UIUtils.dip2px(12), UIUtils.dip2px(6), 0, UIUtils.dip2px(6));
            }
        } else {
            viewHolder.lineView.setVisibility(View.GONE);
            viewHolder.yellowLineView.setVisibility(View.GONE);
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        viewHolder.tvTitle.setText(model.cityName +(TextUtils.isEmpty(model.airportName)?"":"-") + model.airportName);

        return view;

    }


    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        View lineView;
        View yellowLineView;
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(String section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).cityFirstLetter;
            if (sortStr.contains(section)) {
                return i;
            }
        }

        return -1;
    }
}