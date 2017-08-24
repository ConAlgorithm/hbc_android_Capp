package com.hugboga.custom.models;

import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/24.
 */

public class LoadingModel extends EpoxyModelWithHolder<LoadingModel.LoadingHolder> {
    @Override
    protected LoadingHolder createNewHolder() {
        return new LoadingHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.loading_search_guide_line;
    }

    @Override
    public void bind(LoadingHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
    }

    static class LoadingHolder extends EpoxyHolder {
        View itemView;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
        }
    }
}
