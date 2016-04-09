package com.huangbaoche.hbcframe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.huangbaoche.hbcframe.viewholder.FooterViewHolder;
import com.huangbaoche.hbcframe.viewholder.HeaderViewHolder;
import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;

/**
 * 带Header和Footer的Adapter
 * Created by ZHZEPHI on 2015/11/21 16:09.
 */
public abstract class ZHeadFootAdapter<T, V> extends ZBaseAdapter<T,V> {

    public ZHeadFootAdapter(Context context) {
        super(context);
    }

    @Override
    public ZBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1){
            return new HeaderViewHolder(getHeaderView());
        }
        if(viewType==2){
            return new FooterViewHolder(getFooterView());
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 1; //Header
        }
        if(datas==null || position>=datas.size()+1){
            return 2; //Footer
        }
        return super.getItemViewType(position);
    }

    protected abstract View getHeaderView();

    protected abstract View getFooterView();

    @Override
    public void onBindViewHolder(ZBaseViewHolder holder, int position) {
        if(datas!=null && position>0 && position<=datas.size()){
            super.onBindViewHolder(holder, position-1);
        }
    }

    @Override
    public int getItemCount() {
        return datas==null?2:datas.size()+2;
    }
}
