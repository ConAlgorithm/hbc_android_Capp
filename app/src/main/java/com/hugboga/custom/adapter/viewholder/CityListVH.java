package com.hugboga.custom.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 目的地线路显示
 * Created by HONGBO on 2017/11/27 16:49.
 */

public class CityListVH extends RecyclerView.ViewHolder {

    @BindView(R.id.city_item_img)
    ImageView city_item_img;
    @BindView(R.id.city_item_title)
    TextView city_item_title; //标题
    @BindView(R.id.city_item_guide)
    ImageView city_item_guide; //司导头像
    @BindView(R.id.city_item_tip)
    TextView city_item_tip; //提示语1
    @BindView(R.id.city_item_tip2)
    TextView city_item_tip2; //提示语2
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
