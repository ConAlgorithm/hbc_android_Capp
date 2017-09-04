package com.hugboga.custom.widget.guideview;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

public class MutiComponent implements Component {

    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = new LinearLayout(inflater.getContext());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(param);
        ImageView imageView = new ImageView(inflater.getContext());
        imageView.setImageResource(R.mipmap.icon_pop_points_guided);
        ll.removeAllViews();
        ll.addView(imageView, UIUtils.dip2px(200), UIUtils.dip2px(118));
        return ll;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_RIGHT;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override
    public int getXOffset() {
        return -20;
    }

    @Override
    public int getYOffset() {
        return 100;
    }
}