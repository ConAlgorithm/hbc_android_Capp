package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;

/**
 * Created by qingcha on 16/6/21.
 */
public class HomeSearchView extends RelativeLayout {

    private ImageView serviceIV;

    public HomeSearchView(Context context) {
        this(context, null);
    }

    public HomeSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_home_search, this);
        setBackgroundColor(0xFF000000);
        serviceIV = (ImageView) findViewById(R.id.home_search_service_iv);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goChooseCity();
            }
        });

        serviceIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showDefaultServiceDialog(getContext(), "首页搜索框");
            }
        });
    }

    private void goChooseCity() {
        Intent intent = new Intent(this.getContext(), ChooseCityNewActivity.class);
        intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
        intent.putExtra("isHomeIn", true);
        intent.putExtra("source", "首页搜索框");
        this.getContext().startActivity(intent);
        StatisticClickEvent.click(StatisticConstant.SEARCH_LAUNCH, "首页");
    }

    public void setServiceViewVisibility(int visibility) {
        serviceIV.setVisibility(visibility);
    }
}
