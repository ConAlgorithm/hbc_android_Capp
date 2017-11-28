package com.hugboga.custom.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.tools.NetImg;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 目的地线路显示
 * Created by HONGBO on 2017/11/27 16:49.
 */

public class CityListVH extends RecyclerView.ViewHolder {

    Context mContext;

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

    public CityListVH(Context mContext, View itemView) {
        super(itemView);
        this.mContext = mContext;
        ButterKnife.bind(this, itemView);
    }

    /**
     * 显示数据
     */
    public void init(DestinationGoodsVo destinationGoodsVo) {
        NetImg.showImage(mContext, city_item_img, destinationGoodsVo.goodsImageUrl);
        city_item_title.setText(destinationGoodsVo.goodsName);
        NetImg.showCircleImage(mContext, city_item_guide, destinationGoodsVo.guideHeadImageUrl);
        city_item_tip.setText(String.format(mContext.getString(R.string.city_sku_title1),
                String.valueOf(destinationGoodsVo.userFavorCount), String.valueOf(destinationGoodsVo.dayCount),
                destinationGoodsVo.depCityName, destinationGoodsVo.arrCityName));
        city_item_tip2.setText(String.format(mContext.getString(R.string.city_sku_title2),
                String.valueOf(destinationGoodsVo.guideCount)));
    }
}
