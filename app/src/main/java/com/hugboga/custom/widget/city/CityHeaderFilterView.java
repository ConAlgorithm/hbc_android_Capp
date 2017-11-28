package com.hugboga.custom.widget.city;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.tools.NetImg;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 头部筛选部分
 * Created by HONGBO on 2017/11/21 20:16.
 */

public class CityHeaderFilterView extends FrameLayout {

    Activity mActivity;

    @BindView(R.id.city_header_img)
    ImageView city_header_img; //头部背景图
    @BindView(R.id.cityHeaderCountView)
    CityHeaderCountView cityHeaderCountView; //统计部分信息
    @BindView(R.id.city_header_banner_view)
    CityBannerView city_header_banner_view; //头部广告部分

    public CityHeaderFilterView(@NonNull Context context) {
        this(context, null);
    }

    public CityHeaderFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_header_img, this);
        ButterKnife.bind(this, view);
    }

    public void init(Activity activity, DestinationHomeVo vo) {
        this.mActivity = activity;
        NetImg.showImage(getContext(), city_header_img, vo.destinationImageUrl);
        if (cityHeaderCountView != null) {
            cityHeaderCountView.init(mActivity, vo.destinationGoodsCount, vo.destinationAssociateGuideCount);
        }
        //广告部分
        if (city_header_banner_view != null) {
            city_header_banner_view.init(mActivity, vo.beginnerDirection);
        }
    }
}
