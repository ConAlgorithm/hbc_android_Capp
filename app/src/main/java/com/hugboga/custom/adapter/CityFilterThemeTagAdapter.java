package com.hugboga.custom.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityHomeBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class CityFilterThemeTagAdapter extends BaseAdapter {

    List<CityHomeBean.GoodsThemes> goodsThemesList;
    private int selectedTextColor;
    private int selectedTextBackground;

    private int normalTextColor;
    private int normalTextBackground;

    public CityFilterThemeTagAdapter(List<CityHomeBean.GoodsThemes> goodsThemesList) {
        this.goodsThemesList = goodsThemesList;

        initSourceFile();
    }

    private void initSourceFile() {
        Resources resources = MyApplication.getAppContext().getResources();
        selectedTextColor = resources.getColor(R.color.hbc_common_text_color_yellow);
        selectedTextBackground = R.drawable.city_filter_griditem_focus_bg;
        normalTextColor = resources.getColor(R.color.hbc_common_text_color_black);
        normalTextBackground = R.drawable.city_filter_griditem_normal_bg;
    }


    public void setDatas(List<CityHomeBean.GoodsThemes> goodsThemes){
        this.goodsThemesList = goodsThemes;
         notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return goodsThemesList == null ? 0 : goodsThemesList.size();
    }

    @Override
    public CityHomeBean.GoodsThemes getItem(int position) {
        return goodsThemesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityHomeBean.GoodsThemes goodsThemes = goodsThemesList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(MyApplication.getAppContext())
                    .inflate(R.layout.city_filter_griditem, null);
            viewHolder.textView = (TextView)convertView.findViewById(R.id.city_filter_theme_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(goodsThemes.themeName);
        if(goodsThemes.selected){
            viewHolder.textView.setTextColor(selectedTextColor);
            viewHolder.textView.setBackgroundResource(selectedTextBackground);
        }else {
            viewHolder.textView.setTextColor(normalTextColor);
            viewHolder.textView.setBackgroundResource(normalTextBackground);
        }
        return convertView;
    }

    public void updateSelectedStauts(int index){
        if(goodsThemesList==null){
            return;
        }
        for(int i=0;i<goodsThemesList.size();i++){
            CityHomeBean.GoodsThemes data = goodsThemesList.get(i);
            data.selected = i == index;
        }
        this.notifyDataSetChanged();
    }

    class ViewHolder {
        TextView textView;
    }
}
