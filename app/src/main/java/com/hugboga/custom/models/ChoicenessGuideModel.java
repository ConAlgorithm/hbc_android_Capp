package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.charter.CharterSubtitleView;

public class ChoicenessGuideModel extends EpoxyModel<CharterSubtitleView> {


    @Override
    protected int getDefaultLayout() {
        return R.layout.model_charter_subtitle;
    }

    @Override
    public boolean shouldSaveViewState() {
        return true;
    }

    @Override
    public void bind(CharterSubtitleView view) {
        super.bind(view);
        view.update();
    }
}