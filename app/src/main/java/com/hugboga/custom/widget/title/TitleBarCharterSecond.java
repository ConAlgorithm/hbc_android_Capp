package com.hugboga.custom.widget.title;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/3/7.
 */

public class TitleBarCharterSecond extends TitleBarBase{

    @Bind(R.id.titlebar_charter_second_right_tv)
    TextView rightTV;
    @Bind(R.id.titlebar_charter_second_subtitle_tv)
    TextView subtitleTV;

    public TitleBarCharterSecond(Context context) {
        this(context, null);
    }

    public TitleBarCharterSecond(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_titlebar_charter_second, this);
        ButterKnife.bind(view);
    }

    @OnClick({R.id.titlebar_charter_second_left_tv})
    public void onBack(View view) {
        Context context = getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            CommonUtils.hideSoftInput(activity);
            activity.finish();
        }
    }

    public TextView getRightView() {
        return rightTV;
    }


    public void updateSubtitle(String subtitle) {
        subtitleTV.setText(subtitle);
    }
}
