package com.hugboga.custom.models;

import android.widget.LinearLayout;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;

/**
 * Created by qingcha on 17/3/8.
 */
public class CharterFooterModel extends EpoxyModel<LinearLayout>{
    @Override
    protected int getDefaultLayout() {
        return R.layout.view_charter_footer;
    }
}
