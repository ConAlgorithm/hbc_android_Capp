package com.hugboga.custom.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 目的地线路显示
 * Created by HONGBO on 2017/11/27 16:49.
 */

public class CityListVH extends RecyclerView.ViewHolder {

    @BindView(R.id.city_item_line)
    public ImageView line;

    public CityListVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * 显示数据
     */
    public void init(int position) {

    }
}
