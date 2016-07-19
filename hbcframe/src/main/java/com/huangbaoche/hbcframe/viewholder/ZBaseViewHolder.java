package com.huangbaoche.hbcframe.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.xutils.x;

/**
 * Created by ZHZEPHI on 2015/10/15.
 */
public class ZBaseViewHolder extends RecyclerView.ViewHolder {

    public ZBaseViewHolder(View itemView) {
        super(itemView);
        x.view().inject(this, itemView);
    }

}
