package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.utils.Tools;
import com.hugboga.tools.NetImg;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HONGBO on 2017/11/30 11:15.
 */

public class CityListModel extends EpoxyModelWithHolder<CityListModel.CityListVH> {

    Context mContext;
    DestinationGoodsVo destinationGoodsVo;

    public CityListModel(Context mContext, DestinationGoodsVo destinationGoodsVo) {
        this.mContext = mContext;
        this.destinationGoodsVo = destinationGoodsVo;
    }

    @Override
    protected CityListVH createNewHolder() {
        return new CityListVH();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_item;
    }

    @Override
    public void bind(CityListVH holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        init(holder, destinationGoodsVo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SkuDetailActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, destinationGoodsVo.skuDetailUrl);
                intent.putExtra(Constants.PARAMS_ID, destinationGoodsVo.goodsNo);
                intent.putExtra(Constants.PARAMS_SOURCE, "目的地首页-玩法");
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 显示数据
     */
    public void init(CityListVH holder, DestinationGoodsVo destinationGoodsVo) {
        Tools.showImageNotCenterCrop(holder.city_item_img, destinationGoodsVo.goodsImageUrl, R.mipmap.home_default_route_item);
        holder.city_item_title.setText(destinationGoodsVo.goodsName);
        holder.city_item_price.setText(String.format(mContext.getString(R.string.city_sku_item_price),
                String.valueOf(destinationGoodsVo.perPrice)));
        NetImg.showCircleImage(mContext, holder.city_item_guide, destinationGoodsVo.guideHeadImageUrl);
        holder.city_item_tip.setText(String.format(mContext.getString(R.string.city_sku_title1),
                String.valueOf(destinationGoodsVo.userFavorCount), String.valueOf(destinationGoodsVo.dayCount),
                destinationGoodsVo.depCityName, destinationGoodsVo.arrCityName));
        holder.city_item_tip2.setText(String.format(mContext.getString(R.string.city_sku_title2),
                String.valueOf(destinationGoodsVo.guideCount)));
    }

    public class CityListVH extends EpoxyHolder {

        @BindView(R.id.city_item_img)
        ImageView city_item_img;
        @BindView(R.id.city_item_img_price)
        TextView city_item_price; //玩法价格
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

        View itemView;

        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
