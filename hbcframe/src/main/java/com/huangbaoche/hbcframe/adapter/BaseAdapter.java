package com.huangbaoche.hbcframe.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * adapter 基类
 *
 * Created by liwei on 2016/2/24.
 */
public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    private List<T> dataList;

    public void setList(List<T> list){
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void addList(List<T> list){
        if(this.dataList==null){
            this.dataList = list;
        }else{
            this.dataList.addAll(list);
        }
        notifyDataSetChanged();
    }
    public int getCount() {
        return dataList==null?0:this.dataList.size();
    }

    public Object getItem(int position) {
        return dataList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


}
