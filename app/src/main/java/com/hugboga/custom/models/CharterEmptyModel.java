package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.charter.CharterEmptyView;

/**
 * Created by qingcha on 17/3/9.
 */
public class CharterEmptyModel extends EpoxyModel<CharterEmptyView> {

    private CharterEmptyView.OnRefreshDataListener listener;
    private int type;

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_charter_empty;
    }

    public void setOnRefreshDataListener(CharterEmptyView.OnRefreshDataListener listener) {
        this.listener = listener;
    }

    public void setEmptyType(int type) {
        this.type = type;
    }

    @Override
    public boolean shouldSaveViewState() {
        return true;
    }

    @Override
    public void bind(CharterEmptyView view) {
        super.bind(view);
        view.setOnRefreshDataListener(listener);
        view.setEmptyType(type);
    }
}
