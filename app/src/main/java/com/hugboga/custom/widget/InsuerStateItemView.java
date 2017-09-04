package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/9/2.
 */
public class InsuerStateItemView extends RelativeLayout {

    @Bind(R.id.insuer_info_item_state_tv)
    TextView stateTV;
    @Bind(R.id.insuer_info_item_progress)
    CircularProgress progressView;

    public InsuerStateItemView(Context context) {
        this(context, null);
    }

    public InsuerStateItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_insuer_state_item, this);
        ButterKnife.bind(view);
    }

}
