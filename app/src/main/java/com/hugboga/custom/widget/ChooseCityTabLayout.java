package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/7/23.
 */
public class ChooseCityTabLayout extends LinearLayout{

    @Bind(R.id.choose_city_tab_inland_line)
    View inlandLineView;
    @Bind(R.id.choose_city_tab_foreign_line)
    View foreignLineView;
    @Bind(R.id.choose_city_tab_inland_tv)
    TextView inlandTV;
    @Bind(R.id.choose_city_tab_foreign_tv)
    TextView foreignTV;

    private OnChangeListener listener;
    private boolean isInland = true;

    public ChooseCityTabLayout(Context context) {
        this(context, null);
    }

    public ChooseCityTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View rootView = inflate(context, R.layout.view_choosecity_tab, this);
        ButterKnife.bind(this, rootView);
        isInland = true;
    }

    @OnClick({R.id.choose_city_tab_inland_layout, R.id.choose_city_tab_foreign_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_city_tab_inland_layout:
                inlandLineView.setVisibility(View.VISIBLE);
                foreignLineView.setVisibility(View.GONE);
                inlandTV.setTextColor(getResources().getColor(R.color.basic_daily_toolbar_color));
                foreignTV.setTextColor(getResources().getColor(R.color.basic_black));
                isInland = true;
                break;
            case R.id.choose_city_tab_foreign_layout:
                inlandLineView.setVisibility(View.GONE);
                foreignLineView.setVisibility(View.VISIBLE);
                inlandTV.setTextColor(getResources().getColor(R.color.basic_black));
                foreignTV.setTextColor(getResources().getColor(R.color.basic_daily_toolbar_color));
                isInland = false;
                break;
        }
        if (listener != null) {
            listener.OnChangeListener(isInland);
        }
    }

    public boolean isInland() {
        return isInland;
    }

    public void setInland(boolean inland) {
        isInland = inland;
    }

    public interface OnChangeListener {
        public void OnChangeListener(boolean inland);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        this.listener = listener;
    }
}
