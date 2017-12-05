package com.hugboga.custom.widget.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/11/27.
 */

public class HomeMoreView extends LinearLayout {

    @BindView(R.id.home_more_desc_tv)
    TextView descTV;

    public HomeMoreView(Context context) {
        this(context, null);
    }

    public HomeMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
        setBackgroundColor(0xFFF8F8F8);
        inflate(context, R.layout.view_home_more, this);
        ButterKnife.bind(this);
    }

    public void setDescTest(String dest) {
        this.descTV.setText(dest);
    }

}
