package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hugboga.custom.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dyt on 16/4/14.
 * TODO;有时间了抽取出基类
 */
public class OrderSelectCityAdapter extends BaseAdapter {


    Context context;
    public OrderSelectCityAdapter(Context context) {
        initList(50);
        this.context = context;
    }

    List<Integer> list = new ArrayList<>();

    public void initList(int value){
        for(int i = 0;i< value;i++){
            list.add(i);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Integer getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.order_select_people_item,null);
        }
        TextView text = (TextView)convertView.findViewById(R.id.nums);
        text.setText((position+1)+"");
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        return text;
    }
}
