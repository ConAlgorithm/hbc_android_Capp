package com.hugboga.custom.widget.city;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HONGBO on 2018/1/29 20:14.
 */

public class CityItemView extends FrameLayout implements HbcViewBehavior {

    @BindView(R.id.city_item_sku_view)
    CitySkuView city_item_sku_view;

    public CityItemView(@NonNull Context context) {
        this(context, null);
    }

    public CityItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_item_view, this);
        ButterKnife.bind(this, view);
    }

    @Override
    public void update(Object _data) {
        if (city_item_sku_view != null && (_data instanceof DestinationGoodsVo)) {
            city_item_sku_view.init((DestinationGoodsVo) _data);
            int size = UIUtils.getScreenWidth() - UIUtils.dip2px(32);
            city_item_sku_view.setCityView(size); //重新设置宽高
        }
    }
}
