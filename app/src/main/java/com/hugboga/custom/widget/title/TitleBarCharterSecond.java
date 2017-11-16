package com.hugboga.custom.widget.title;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/7.
 */

public class TitleBarCharterSecond extends TitleBarBase{

    @BindView(R.id.titlebar_charter_second_left_tv)
    TextView leftTV;
    @BindView(R.id.titlebar_charter_second_right_tv)
    TextView rightTV;
    @BindView(R.id.titlebar_charter_second_subtitle_tv)
    TextView subtitleTV;

    public TitleBarCharterSecond(Context context) {
        this(context, null);
    }

    public TitleBarCharterSecond(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_titlebar_charter_second, this);
        ButterKnife.bind(view);
    }

    public TextView getLeftView() {
    return leftTV;
}

    public TextView getRightView() {
        return rightTV;
    }


    public void updateSubtitle(String subtitle) {
        subtitleTV.setText(subtitle);
    }
}
