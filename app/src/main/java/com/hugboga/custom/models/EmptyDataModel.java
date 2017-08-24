package com.hugboga.custom.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

/**
 * Created by zhangqiang on 17/8/24.
 */

public class EmptyDataModel extends EpoxyModelWithHolder<EmptyDataModel.EmptyDataHolder> {
    @Override
    protected EmptyDataHolder createNewHolder() {
        return new EmptyDataHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.empty_data_search;
    }

    @Override
    public void bind(EmptyDataHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
    }

    static class EmptyDataHolder extends EpoxyHolder {
        View itemView;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
        }
    }
}
