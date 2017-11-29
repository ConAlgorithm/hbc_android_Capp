package com.hugboga.custom.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * 目的地玩法下单入口配置
 * Created by HONGBO on 2017/11/29 16:48.
 */

public class CityConfigVH extends RecyclerView.ViewHolder {

    public CityConfigVH(Context mContext, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
