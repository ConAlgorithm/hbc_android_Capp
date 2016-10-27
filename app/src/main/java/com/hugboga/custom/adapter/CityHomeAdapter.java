package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.View;

import com.hugboga.custom.data.bean.GoodsSec;
import com.hugboga.custom.widget.CityHomeListItemFree;
import com.hugboga.custom.widget.CityHomeListItemWorry;

import java.util.List;


/**
 * Created by Administrator on 2016/10/18.
 */

public class CityHomeAdapter extends  HbcRecyclerBaseAdapter<GoodsSec> {
    public static final int HEAD_LABLE_FREE_TYPE=2;
    public static final int HEAD_LABLE_WORRY_TYPE=1;

    public CityHomeAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getItemView(int position) {
        View view=null;
        switch (datas.get(position).goodsClass){
            case HEAD_LABLE_WORRY_TYPE:
                view=new CityHomeListItemWorry(getContext());
                break;
            case HEAD_LABLE_FREE_TYPE:
                view=new CityHomeListItemFree(getContext());
                break;
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
