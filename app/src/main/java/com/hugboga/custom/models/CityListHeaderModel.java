package com.hugboga.custom.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 17/4/14.
 */
public class CityListHeaderModel extends EpoxyModel<View> {

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_city_list_header;
    }

    @Override
    public void bind(View view) {
        super.bind(view);
        ImageView bgIV = (ImageView) view.findViewById(R.id.city_list_header_bg_iv);//720 400
        TextView descriptionTV = (TextView) view.findViewById(R.id.city_list_header_description_tv);
        TextView cityTV = (TextView) view.findViewById(R.id.city_list_header_city_tv);
        RelativeLayout.LayoutParams bgParams = new RelativeLayout.LayoutParams(UIUtils.getScreenWidth(), (int)((720 / 400.0f) * UIUtils.getScreenWidth()));
        bgIV.setLayoutParams(bgParams);
    }
}
