package com.hugboga.custom.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.fragment.GuideFilterSortFragment;

import java.util.ArrayList;

public class GuideFilterTagAdapter extends BaseAdapter {

    ArrayList<GuideFilterSortFragment.SortTypeBean> sortTypeList;
    int selectedColor;
    int normalColor;

    public GuideFilterTagAdapter(ArrayList<GuideFilterSortFragment.SortTypeBean> sortTypeList) {
        this.sortTypeList = sortTypeList;
        selectedColor = MyApplication.getAppContext().getResources().getColor(R.color.hbc_common_text_color_yellow);
        normalColor = MyApplication.getAppContext().getResources().getColor(R.color.hbc_common_text_color_black);
    }

    @Override
    public int getCount() {
        return sortTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return sortTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GuideFilterSortFragment.SortTypeBean data = sortTypeList.get(position);
        GuideFilterTagAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new GuideFilterTagAdapter.ViewHolder();
            convertView = LayoutInflater.from(MyApplication.getAppContext()).inflate(R.layout.guide_filter_sort_item, null);
            viewHolder.filterItemTV = (TextView) convertView.findViewById(R.id.guide_filter_sort_item_tv);
            viewHolder.selectedIV = (ImageView) convertView.findViewById(R.id.guide_filter_sort_selected_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GuideFilterTagAdapter.ViewHolder)convertView.getTag();
        }
        viewHolder.filterItemTV.setText(data.typeStr);
        if (data.selected) {
            viewHolder.filterItemTV.setTextColor(selectedColor);
            viewHolder.selectedIV.setVisibility(View.VISIBLE);
        } else {
            viewHolder.filterItemTV.setTextColor(normalColor);
            viewHolder.selectedIV.setVisibility(View.GONE);
        }
        return convertView;
    }

    public static class ViewHolder{
        TextView filterItemTV;
        ImageView selectedIV;
    }
}