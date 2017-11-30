package com.hugboga.custom.models;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 目的地想怎么玩入口
 * Created by HONGBO on 2017/11/30 11:04.
 */

public class CityWhatModel extends EpoxyModelWithHolder<CityWhatModel.CityWhatVH> {

    Context mContext;

    public CityWhatModel(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected CityWhatModel.CityWhatVH createNewHolder() {
        return new CityWhatVH();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_item_what_layout;
    }

    @Override
    public void bind(CityWhatVH holder) {
        super.bind(holder);
    }

    public class CityWhatVH extends EpoxyHolder {

        @BindView(R.id.city_item_what_do_layout)
        LinearLayout city_item_what_do_layout; //下单入口

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
