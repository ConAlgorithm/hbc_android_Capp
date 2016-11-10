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

public class CityHomeAdapter extends  HbcRecyclerTypeBaseAdpater<GoodsSec> {
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
                return view;
            case HEAD_LABLE_FREE_TYPE:
                view=new CityHomeListItemFree(getContext());
               return view;
        }
        return view;
    }

    @Override
    protected int getChildItemViewType(int position) {
        switch (datas.get(position-getHeadersCount()).goodsClass){
            case HEAD_LABLE_WORRY_TYPE:
                return 0x03;
            case HEAD_LABLE_FREE_TYPE:
                return 0x04;
        }
        return -1;
    }

//    public void setCanAnimationFlag(int firstVisibleItemPosition,int lastVisibleItemPosition){
//        if(firstVisibleItemPosition<0){
//            return;
//        }
//        if(datas!=null && datas.size()>0){
//            for(int i=0;i<datas.size();i++){
//                if(i<firstVisibleItemPosition &&  i>lastVisibleItemPosition){
//                    datas.get(i).canAnimation = false;
//                }else{
//                    datas.get(i).canAnimation = true;
//                }
//            }
//        }
//    }


    public static int getViewTopOnScreen(View view){
        if(view==null){
            return -1;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }
}
