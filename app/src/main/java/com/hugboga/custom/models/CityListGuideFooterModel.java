package com.hugboga.custom.models;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 17/4/19.
 */
public class CityListGuideFooterModel extends EpoxyModel<LinearLayout> {

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_page_footer;
    }

    @Override
    public void bind(LinearLayout view) {
        super.bind(view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.dip2px(50));
        view.setLayoutParams(params);
        view.setPadding(0, 0, 0, 0);
        TextView textView = (TextView) view.findViewById(R.id.home_bottom_end_tv);
        textView.setText("查看更多司导");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}