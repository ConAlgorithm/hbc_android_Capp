package com.hugboga.custom.widget.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.city.CitySkuView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/11/23.
 * <p>
 * 收藏逻辑保留自旧代码
 */

public class HomeAlbumItemView extends LinearLayout implements HbcViewBehavior {

    @BindView(R.id.home_sku_view)
    CitySkuView home_sku_view; //SKU展示item

    private HomeBean.AlbumBean albumBean;

    public HomeAlbumItemView(Context context) {
        this(context, null);
    }

    public HomeAlbumItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_home_sku_item, this);
        ButterKnife.bind(this);
    }

    public void setDesplayViewLayoutParams(int displayImgWidth, int displayImgHeight) {
        if (home_sku_view != null) {
            home_sku_view.setDesplayViewLayoutParams(displayImgWidth, displayImgHeight);
        }
    }

    @Override
    public void update(Object _data) {
        if (!(_data instanceof HomeBean.AlbumBean) || _data == null) {
            return;
        }
        albumBean = (HomeBean.AlbumBean) _data;
        if (home_sku_view != null) {
            home_sku_view.init(transSkuInfo(albumBean));
            home_sku_view.line.setVisibility(View.GONE);
        }
    }

    /**
     * 数据转换
     *
     * @param albumBean
     * @return
     */
    public DestinationGoodsVo transSkuInfo(HomeBean.AlbumBean albumBean) {
        DestinationGoodsVo vo = new DestinationGoodsVo(null);
        vo.goodsNo = albumBean.goodsNo;
        vo.goodsImageUrl = albumBean.goodsPic;
        vo.goodsName = albumBean.getGoodsName();
        vo.perPrice = String.valueOf(albumBean.perPrice);
        vo.guideHeadImageUrl = albumBean.guideAvatar;
        vo.userFavorCount = albumBean.goodsFavoriteNum;
        vo.dayCount = albumBean.goodsServiceDayNum;
        vo.placeList = albumBean.routeCityDesc;
        //FIXME XX位当地司导可带你服务，缺少字段@吕博文
        vo.guideCount = albumBean.goodsFavoriteNum;
        return vo;
    }
}
