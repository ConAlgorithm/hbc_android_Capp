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

public class GuideLineItemHeaderModel extends EpoxyModelWithHolder<GuideLineItemHeaderModel.GuideLineItemHeaderHolder> {
    GuideLineItemHeaderHolder guideLineItemHeaderHolder;
    @Override
    protected GuideLineItemHeaderHolder createNewHolder() {
        return new GuideLineItemHeaderHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.search_guide_line_header;
    }

    @Override
    public void bind(GuideLineItemHeaderHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        guideLineItemHeaderHolder =holder;
        init();
    }

    static class GuideLineItemHeaderHolder extends EpoxyHolder {
        View itemView;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    private void init(){

    }
}
