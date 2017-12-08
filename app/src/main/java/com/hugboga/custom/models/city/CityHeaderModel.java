package com.hugboga.custom.models.city;

import android.app.Activity;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.widget.city.CityHeaderView;

/**
 * Created by HONGBO on 2017/12/8 19:07.
 */

public class CityHeaderModel extends EpoxyModel<CityHeaderView> {

    Activity activity;
    DestinationHomeVo vo;

    public CityHeaderModel(Activity activity, DestinationHomeVo vo) {
        super();
        this.activity = activity;
        this.vo = vo;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_header_model_layout;
    }

    @Override
    public void bind(CityHeaderView view) {
        super.bind(view);
        if (view == null) {
            return;
        }
        view.init(activity, vo);
    }
}
