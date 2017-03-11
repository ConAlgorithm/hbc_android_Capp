package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.DateUtils;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/9.
 */
public class CombinationOrderDescriptionView extends LinearLayout{

    @Bind(R.id.combination_order_des_date_tv)
    TextView dateTV;
    @Bind(R.id.combination_order_des_address_tv)
    TextView addressTV;

    public CombinationOrderDescriptionView(Context context) {
        this(context, null);
    }

    public CombinationOrderDescriptionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_combination_order_description, this);
        ButterKnife.bind(view);
    }

    public void update(CharterDataUtils charterDataUtils) {
        ChooseDateBean chooseDateBean = charterDataUtils.chooseDateBean;
        try {
            dateTV.setText(String.format("%1$s - %2$s (%3$s天)", DateUtils.getDateFromSimpleStr(chooseDateBean.start_date),
                    chooseDateBean.showEndDateStr, chooseDateBean.dayNums));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CityBean endCityBean = null;
        if (charterDataUtils.travelList != null && charterDataUtils.travelList.get(charterDataUtils.travelList.size() - 1).routeType == CityRouteBean.RouteType.OUTTOWN) {
            endCityBean = charterDataUtils.getEndCityBean(chooseDateBean.dayNums);
        } else {
            endCityBean = charterDataUtils.getStartCityBean(chooseDateBean.dayNums);
        }
        addressTV.setText(String.format("%1$s - %2$s", charterDataUtils.getStartCityBean(1).name, endCityBean.name));
    }
}
