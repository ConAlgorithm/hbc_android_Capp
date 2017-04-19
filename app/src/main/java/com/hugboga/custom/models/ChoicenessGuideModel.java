package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.widget.ChoicenessGuideView;

public class ChoicenessGuideModel extends EpoxyModel<ChoicenessGuideView> {

    private FilterGuideBean guideBean;

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_choiceness_guide;
    }

    public void setGuideData(FilterGuideBean guideBean) {
        this.guideBean = guideBean;
    }

    @Override
    public void bind(ChoicenessGuideView view) {
        super.bind(view);
        view.update(guideBean);
    }
}