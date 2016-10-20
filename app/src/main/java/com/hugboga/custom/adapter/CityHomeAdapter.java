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
    public static final String HEADLABLEFREE="超自由";
    public static final String HEADLABLEWORRY="超省心";

    public CityHomeAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getItemView(int position) {
        View view=null;
        switch (datas.get(position).headLable){
            case HEADLABLEWORRY:
               view=new CityHomeListItemWorry(getContext());
                break;
            case HEADLABLEFREE:
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
