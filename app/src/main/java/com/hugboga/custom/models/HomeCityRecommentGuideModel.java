package com.hugboga.custom.models;

import android.content.Context;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeCityContentVo2;
import com.hugboga.custom.widget.HomeCityRecommentGuideView;
import com.hugboga.custom.widget.HomeFilterGuideView;

/**
 * Created by zhangqiang on 17/9/15.
 */

public class HomeCityRecommentGuideModel extends EpoxyModel<HomeCityRecommentGuideView> {
    Context context;
    HomeCityContentVo2 homeCityContentVo2;
    public HomeCityRecommentGuideModel(Context context,HomeCityContentVo2 homeCityContentVo2){
        this.context = context;
        this.homeCityContentVo2 = homeCityContentVo2;
    }
    @Override
    protected int getDefaultLayout() {
        return R.layout.model_recomment_guide;
    }
    @Override
    public void bind(HomeCityRecommentGuideView view) {
        super.bind(view);
        view.setGuideBeanList(homeCityContentVo2);
    }
}
