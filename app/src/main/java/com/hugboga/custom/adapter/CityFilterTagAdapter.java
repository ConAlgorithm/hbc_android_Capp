package com.hugboga.custom.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityFilterData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public class CityFilterTagAdapter extends BaseAdapter {

    List<CityFilterData> datas = new ArrayList<>();

    int selectedColor;
    int normalColor;

    public CityFilterTagAdapter(List<CityFilterData> datas){
        this.datas = datas;
        selectedColor = MyApplication.getAppContext().getResources()
                .getColor(R.color.hbc_common_text_color_yellow);
        normalColor = MyApplication.getAppContext().getResources()
                .getColor(R.color.hbc_common_text_color_black);
    }

    @Override
    public int getCount() {
        return datas==null ? 0:datas.size();
    }

    @Override
    public CityFilterData getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityFilterData data = datas.get(position);
        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(MyApplication.getAppContext()).inflate(R.layout.city_filter_item,null);
            viewHolder.filterItemView = (TextView)convertView.findViewById(R.id.city_filter_item_value);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.filterItemView.setText(data.value);
        if(data.selected){
            viewHolder.filterItemView.setTextColor(selectedColor);
        }else {
            viewHolder.filterItemView.setTextColor(normalColor);
        }
        return convertView;
    }

    public class ViewHolder{
        TextView filterItemView;
    }

    public void updateSelectedStauts(int index){
        if(datas==null){
            return;
        }
        for(int i=0;i<datas.size();i++){
            CityFilterData data = datas.get(i);
            data.selected = i == index;
        }
        this.notifyDataSetChanged();
    }

    public static  List<CityFilterData> getTypeDatas(){
        List<CityFilterData> datas = new ArrayList<>();
        CityFilterData cityFilterData = new CityFilterData();
        cityFilterData.type = 0;
        cityFilterData.value = "不限";
        cityFilterData.label = "不限类型";
        cityFilterData.selected = true;

        CityFilterData cityFilterData1 = new CityFilterData();
        cityFilterData1.type = 1;
        cityFilterData1.label = "超省心";
        cityFilterData1.value = "超省心，按照固定线路游玩";

        CityFilterData cityFilterData2 = new CityFilterData();
        cityFilterData2.type = 2;
        cityFilterData2.label = "超自由";
        cityFilterData2.value = "超自由，预订后还可改行程";

        datas.add(cityFilterData);
        datas.add(cityFilterData1);
        datas.add(cityFilterData2);
        return datas;
    }

    public static  List<CityFilterData> getDaysDatas(){
        List<CityFilterData> datas = new ArrayList<>();
        CityFilterData cityFilterData = new CityFilterData();
        cityFilterData.type = 0;
        cityFilterData.value = "不限";
        cityFilterData.label = "不限天数";
        cityFilterData.selected = true;

        CityFilterData cityFilterData1 = new CityFilterData();
        cityFilterData1.type = 1;
        cityFilterData1.value = "1日";
        cityFilterData1.label = "1日";

        CityFilterData cityFilterData2 = new CityFilterData();
        cityFilterData2.type = 2;
        cityFilterData2.value = "多日";
        cityFilterData2.label = "多日";

        datas.add(cityFilterData);
        datas.add(cityFilterData1);
        datas.add(cityFilterData2);
        return datas;
    }

}
