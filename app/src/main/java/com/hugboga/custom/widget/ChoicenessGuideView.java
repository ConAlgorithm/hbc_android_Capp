package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;

import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/4/14.
 */
public class ChoicenessGuideView extends LinearLayout {

    public ChoicenessGuideView(Context context) {
        this(context, null);
    }

    public ChoicenessGuideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_choiceness_guide, this);
        ButterKnife.bind(view);
    }
}
