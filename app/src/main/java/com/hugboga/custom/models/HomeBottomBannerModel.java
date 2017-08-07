package com.hugboga.custom.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/5.
 */

public class HomeBottomBannerModel extends EpoxyModelWithHolder {
    @Override
    protected EpoxyHolder createNewHolder() {
        return new HomeBottomBannerHolder();
    }
    @Override
    protected int getDefaultLayout() {
        return R.layout.home_bottom_banner;
    }

    static class HomeBottomBannerHolder extends EpoxyHolder{
        View itemView;

        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        init(holder);
    }

    private void init(EpoxyHolder holder){

    }
}
