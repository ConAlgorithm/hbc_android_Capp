package com.hugboga.custom.models;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.fragment.FgHomePage;

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
            case FgHomePage.TAB_TRAVEL_STORY:
                textView.setText("查看更多精选司导");
                break;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentTab) {
                    case FgHomePage.TAB_HOTEXPLORE:
                        Log.i("aa", "查看更多包车线路");
                        break;
                    case FgHomePage.TAB_DESTION:
                        Log.i("aa", "搜索目的地");
                        break;
                    case FgHomePage.TAB_TRAVEL_STORY:
                        Log.i("aa", "查看更多精选司导");
                        break;
                }
            }
        });
    }
}
