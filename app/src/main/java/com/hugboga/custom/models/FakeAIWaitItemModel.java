
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
    ImageView imageViews[];
    boolean aBoolean = false;
    int count = 1;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (aBoolean && msg.what == 1) {
                return;
            }
            upData();
            aBoolean = true;
        }
    };

    protected int getDefaultLayout() {
        return R.layout.fake_item_wait;
    }

    @Override
    public void bind(RelativeLayout view) {
        super.bind(view);
        ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        ImageView[] imageViews = {aiWaitImage1, aiWaitImage2, aiWaitImage3};
        this.imageViews = imageViews;
        handler.sendEmptyMessage(1);
    }

    private void upData() {
        for (int i = 0; i < imageViews.length; i++) {
            if (count % imageViews.length == i) {
                imageViews[i].setImageResource(R.mipmap.ai_wait_icon_one);
            } else {
                imageViews[i].setImageResource(R.mipmap.ai_wait_icon_two);
            }

        }
        count++;
        handler.sendEmptyMessageDelayed(2, 100);
    }

    @Override
    public EpoxyModel<RelativeLayout> reset() {
        aBoolean = false;
        handler = null;
        return super.reset();
    }
}


