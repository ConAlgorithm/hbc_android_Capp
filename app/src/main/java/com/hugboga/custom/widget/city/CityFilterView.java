package com.hugboga.custom.widget.city;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 筛选条件部分
 * Created by HONGBO on 2017/11/22 11:56.
 */

public class CityFilterView extends FrameLayout {

    @BindView(R.id.city_header_filter_tab1)
    TabView tabView1;
    @BindView(R.id.city_header_filter_tab2)
    TabView tabView2;
    @BindView(R.id.city_header_filter_tab3)
    TabView tabView3;

    public CityFilterView(@NonNull Context context) {
        this(context, null);
    }

    public CityFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_header_filter, this);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.city_header_filter_tab1, R.id.city_header_filter_tab2, R.id.city_header_filter_tab3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city_header_filter_tab1:
                selectPosition(0);
                if (filterSeeListener != null) {
                    filterSeeListener.onShowFilter(0, tabView1.isSelected());
                }
                break;
            case R.id.city_header_filter_tab2:
                selectPosition(1);
                if (filterSeeListener != null) {
                    filterSeeListener.onShowFilter(1, tabView2.isSelected());
                }
                break;
            case R.id.city_header_filter_tab3:
                selectPosition(2);
                if (filterSeeListener != null) {
                    filterSeeListener.onShowFilter(2, tabView3.isSelected());
                }
                break;
        }
    }

    private void selectPosition(int position) {
        if (tabView1 != null) {
            if (position != 0) {
                tabView1.setSelected(false);
            } else {
                tabView1.setSelected(!tabView1.isSelected());
            }
        }
        if (tabView2 != null) {
            if (position != 1) {
                tabView2.setSelected(false);
            }else{
                tabView2.setSelected(!tabView2.isSelected());
            }
        }
        if (tabView3 != null) {
            if (position != 2) {
                tabView3.setSelected(false);
            }else{
                tabView3.setSelected(!tabView3.isSelected());
            }
        }
    }

    public void clear(){
        if (tabView1 != null) {
            tabView1.setSelected(false);
        }
        if (tabView2 != null) {
            tabView2.setSelected(false);
        }
        if (tabView3 != null) {
            tabView3.setSelected(false);
        }
    }

    private FilterSeeListener filterSeeListener;

    public interface FilterSeeListener {
        void onShowFilter(int position, boolean isSelect);
    }

    public void setFilterSeeListener(FilterSeeListener filterSeeListener) {
        this.filterSeeListener = filterSeeListener;
    }
}
