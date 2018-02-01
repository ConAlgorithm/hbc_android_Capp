package com.hugboga.custom.widget.city;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.tools.NetImg;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 线路列表Item头像部分显示
 * Created by HONGBO on 2018/1/31 11:43.
 */
public class CityGuidesView extends RelativeLayout {

    @BindView(R.id.city_guides_layout_guide1)
    ImageView city_guides_layout_guide1;
    @BindView(R.id.city_guides_layout_guide2)
    ImageView city_guides_layout_guide2;
    @BindView(R.id.city_guides_guide22)
    ImageView city_guides_guide22;

    public CityGuidesView(Context context) {
        this(context, null);
    }

    public CityGuidesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_guides_layout, this);
        ButterKnife.bind(this, view);
    }

    public void init(DestinationGoodsVo destinationGoodsVo) {
        if (destinationGoodsVo.guideAvatars != null && destinationGoodsVo.guideAvatars.size() > 0) {
            if (destinationGoodsVo.guideAvatars.size() > 1) {
                //显示两个头像
                showOrHideImg2(true);
                //第一个头像放上面
                NetImg.showCircleImage(getContext(), city_guides_layout_guide1, destinationGoodsVo.guideAvatars.get(1));
                NetImg.showCircleImage(getContext(), city_guides_layout_guide2, destinationGoodsVo.guideAvatars.get(0));
            } else {
                //只显示一个头像
                showOrHideImg2(false);
                NetImg.showCircleImage(getContext(), city_guides_layout_guide1, destinationGoodsVo.guideAvatars.get(0));
            }
        } else {
            showOrHideImg2(false);
            NetImg.showCircleImage(getContext(), city_guides_layout_guide1, destinationGoodsVo.guideHeadImageUrl);
        }
    }

    private void showOrHideImg2(boolean isShow) {
        if (isShow) {
            city_guides_layout_guide2.setVisibility(View.VISIBLE);
            city_guides_guide22.setVisibility(View.VISIBLE);
        } else {
            city_guides_layout_guide2.setVisibility(View.GONE);
            city_guides_guide22.setVisibility(View.GONE);
        }
    }

}
