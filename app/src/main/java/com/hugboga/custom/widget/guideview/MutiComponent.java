package com.hugboga.custom.widget.guideview;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

public class MutiComponent implements Component {

    private int imgResId;

    public MutiComponent(boolean isPickup) {
        imgResId = isPickup ? R.mipmap.air_guide_arrow : R.mipmap.carcase_guide_arrow;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = new LinearLayout(inflater.getContext());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(param);
        ll.setGravity(Gravity.CENTER);
        ImageView imageView = new ImageView(inflater.getContext());
        imageView.setImageResource(imgResId);
        ll.removeAllViews();
        ll.addView(imageView, UIUtils.dip2px(260), UIUtils.dip2px(82));
        return ll;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override
    public int getXOffset() {
        return -100;
    }

    @Override
    public int getYOffset() {
        return 12;
    }
}