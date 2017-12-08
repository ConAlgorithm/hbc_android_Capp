package com.hugboga.custom.models.ai;

import android.app.Activity;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.city.BeginnerDirectionVo;
import com.hugboga.custom.widget.city.CityBannerView;

/**
 * Created by HONGBO on 2017/12/7 11:40.
 */

public class AiResultBannerModel extends EpoxyModel<CityBannerView> {

    Activity activity;
    BeginnerDirectionVo beginnerDirectionVo;

    public AiResultBannerModel(Activity activity, BeginnerDirectionVo beginnerDirectionVo) {
        this.activity = activity;
        this.beginnerDirectionVo = beginnerDirectionVo;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.ai_result_banner_model_layout;
    }

    @Override
    public void bind(CityBannerView view) {
        super.bind(view);
        view.init(activity, beginnerDirectionVo);
    }
}
