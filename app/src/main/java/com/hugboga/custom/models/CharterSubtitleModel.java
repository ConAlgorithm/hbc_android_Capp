package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.charter.CharterSubtitleView;

/**
 * Created by qingcha on 17/2/25.
 */

public class CharterSubtitleModel extends EpoxyModel<CharterSubtitleView> {

    private CharterSubtitleView.OnPickUpOrSendSelectedListener listener;

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_charter_subtitle;
    }

    @Override
    public boolean shouldSaveViewState() {
        return true;
    }

    public void setOnPickUpOrSendSelectedListener(CharterSubtitleView.OnPickUpOrSendSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void bind(CharterSubtitleView view) {
        super.bind(view);
        view.update();
        view.setOnPickUpOrSendSelectedListener(listener);
    }
}
