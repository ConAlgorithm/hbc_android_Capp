package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.hugboga.custom.R;

/**
 * Created by qingcha on 16/6/24.
 */
public class SkuListItem extends LinearLayout implements HbcViewBehavior{

    public SkuListItem(Context context) {
        this(context, null);
    }

    public SkuListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_sku_list_item, this);
    }

    @Override
    public void update(Object _data) {

    }
}
