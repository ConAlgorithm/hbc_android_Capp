package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/8.
 */

public class FakeAIWaitItemModel extends EpoxyModel<RelativeLayout> {


    @BindView(R.id.ai_wait_image1)
    ImageView aiWaitImage1;
    @BindView(R.id.ai_wait_image2)
    ImageView aiWaitImage2;
    @BindView(R.id.ai_wait_image3)
    ImageView aiWaitImage3;
    ImageView[] imageViews;
    public FakeAIWaitItemModel() {

    }

    protected int getDefaultLayout() {
        return R.layout.fake_item_wait;
    }

    @Override
    public void bind(RelativeLayout view) {
        super.bind(view);
        ButterKnife.bind(this, view);
    }

}


