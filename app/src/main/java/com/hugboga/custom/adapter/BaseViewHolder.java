package com.hugboga.custom.adapter;

import android.view.View;

import org.xutils.x;

/**
 * ViewHolder模板
 * Created by ZHZEPHI on 2016/3/25.
 */
public class BaseViewHolder {

    public BaseViewHolder(View itemView) {
        x.view().inject(this, itemView);
    }
}
