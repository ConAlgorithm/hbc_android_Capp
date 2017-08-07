package com.hugboga.custom.models;

import android.content.Context;
import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/2.
 */

public class HomeH5Model extends EpoxyModelWithHolder {

    Context context;
    HomeH5Holder homeH5Holder;
    public HomeH5Model(Context context){
        this.context = context;
    }
    @Override
    protected EpoxyHolder createNewHolder() {
        return new HomeH5Holder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_h5;
    }

    static class HomeH5Holder extends EpoxyHolder{
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
        homeH5Holder = (HomeH5Model.HomeH5Holder) holder;
        init();
    }
    public void update() {
//        if(homeServiceHolder != null){
//            homeServiceHolder
//        }
    }
    private void init(){
//        if(homeServiceHolder != null){
//
//        }
    }
}
