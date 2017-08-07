package com.hugboga.custom.models;

import android.content.Context;
import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/1.
 */

public class HomeServiceModel extends EpoxyModelWithHolder {

    Context context;
    HomeServiceHolder homeServiceHolder;
    @Override
    protected EpoxyHolder createNewHolder() {
        return new HomeServiceHolder();
    }


    public HomeServiceModel(Context context){
        this.context = context;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_service;
    }

    static class HomeServiceHolder extends EpoxyHolder{
        View itemView;

        @Bind(R.id.view1)
        View view;
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
        homeServiceHolder = (HomeServiceHolder) holder;
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
    public int getViewTop() {
        if (homeServiceHolder.view != null) {
            return UIUtils.getViewTop(homeServiceHolder.view);
        }
        return 0;
    }
}
