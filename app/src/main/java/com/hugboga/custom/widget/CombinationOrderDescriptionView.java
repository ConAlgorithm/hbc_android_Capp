package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.DateUtils;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/9.
 */
public class CombinationOrderDescriptionView extends LinearLayout{

    @BindView(R.id.combination_order_des_date_tv)
    TextView dateTV;
    @BindView(R.id.combination_order_des_people_tv)
    TextView peopleTV;
    @BindView(R.id.combination_order_des_address_tv)
    TextView addressTV;
    @BindView(R.id.combination_order_travel_view)
    OrderTravelView travelView;

    public CombinationOrderDescriptionView(Context context) {
        this(context, null);
    }

    public CombinationOrderDescriptionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_combination_order_description, this);
        ButterKnife.bind(view);
    }

    public void update(CharterDataUtils charterDataUtils) {
        if (charterDataUtils == null || charterDataUtils.chooseDateBean == null) {
            setVisibility(GONE);
            return;
        }
        ChooseDateBean chooseDateBean = charterDataUtils.chooseDateBean;
        try {
            dateTV.setText(String.format("%1$s - %2$s (%3$s天)", DateUtils.getDateFromSimpleStr(chooseDateBean.start_date),
                    chooseDateBean.showEndDateStr, chooseDateBean.dayNums));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String travellerCount = String.format("成人%1$s位", charterDataUtils.adultCount);
        if (charterDataUtils.childCount > 0) {
            travellerCount += String.format("，儿童%1$s位", charterDataUtils.childCount);
        }
        peopleTV.setText(travellerCount);

        String endCityName = "";
        if (charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null) {
            endCityName = charterDataUtils.airPortBean.cityName;
        } else if (charterDataUtils.travelList != null && charterDataUtils.travelList.get(charterDataUtils.travelList.size() - 1).routeType == CityRouteBean.RouteType.OUTTOWN) {
            endCityName = charterDataUtils.getEndCityBean(chooseDateBean.dayNums).name;
        } else {
            endCityName = charterDataUtils.getStartCityBean(chooseDateBean.dayNums).name;
        }
        addressTV.setText(String.format("%1$s - %2$s", charterDataUtils.getStartCityBean(1).name, endCityName));

        travelView.update(charterDataUtils);
    }
}
