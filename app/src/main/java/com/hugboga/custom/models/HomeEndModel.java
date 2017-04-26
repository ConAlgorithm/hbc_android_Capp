package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.activity.FilterGuideListActivity;
import com.hugboga.custom.activity.FilterSkuListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.fragment.FgHomePage;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;

/**
 * Created by SPW on 2017/3/11.
 */
public class HomeEndModel extends EpoxyModel<LinearLayout> {

    private int currentTab;

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_page_footer;
    }

    public void setCurrentTab(int tab) {
        this.currentTab = tab;
    }

    @Override
    public void bind(LinearLayout view) {
        super.bind(view);
        TextView textView = (TextView) view.findViewById(R.id.home_bottom_end_tv);
        switch (currentTab) {
            case FgHomePage.TAB_HOTEXPLORE:
                textView.setText("查看更多包车线路");
                break;
            case FgHomePage.TAB_DESTION:
                textView.setText("搜索目的地");
                break;
            case FgHomePage.TAB_GUIDE:
                textView.setText("查看更多精选司导");
                break;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = null;
                switch (currentTab) {
                    case FgHomePage.TAB_HOTEXPLORE:
                        context.startActivity(new Intent(context, FilterSkuListActivity.class));
                        break;
                    case FgHomePage.TAB_DESTION:
                        intent = new Intent(context, ChooseCityNewActivity.class);
                        intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
                        intent.putExtra("isHomeIn", true);
                        intent.putExtra("source", "首页搜索框");
                        context.startActivity(intent);
                        StatisticClickEvent.click(StatisticConstant.SEARCH_LAUNCH, "首页");
                        break;
                    case FgHomePage.TAB_GUIDE:
                        context.startActivity(new Intent(context, FilterGuideListActivity.class));
                        break;
                }
            }
        });
    }
}
