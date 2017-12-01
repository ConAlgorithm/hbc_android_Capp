package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangbaoche.hbcframe.viewholder.FooterViewHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.FakeAIViewHoder;

/**
 * Created by Administrator on 2017/11/29.
 */

public abstract  class FakeAIAdapter extends RecyclerView.Adapter<FakeAIViewHoder> {
    public  static final  int FAKEADAPTERTYPE_ONE = 1;
    public  static final  int FAKEADAPTERTYPE_TWO = 2;
    private  Context context;
    public  FakeAIAdapter(Context context){
        this.context=context;
    }
    public  abstract  int itemCount();
    public  abstract  int getViewType(int position);
    public  abstract  void  bindData(FakeAIViewHoder holder, int position);
    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    @Override
    public FakeAIViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == FAKEADAPTERTYPE_ONE) {

            return new FakeAIViewHoder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.fake_iem_one, parent, false),viewType);
        }else {

            View view = LayoutInflater.from(context).inflate(R.layout.fake_item_two, parent, false);
            return new FakeAIViewHoder(view,viewType);
        }
    }

    @Override
    public void onBindViewHolder(FakeAIViewHoder holder, int position) {
        bindData(holder,position);
    }

    @Override
    public int getItemCount() {
        return itemCount();
    }
}
