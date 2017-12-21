package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.QueryCityActivity;
import com.hugboga.custom.adapter.SearchHotCityAdapter;
import com.hugboga.custom.data.bean.DestinationHotItemBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索城市的默认热门城市列表
 */
public class QueryHotCity extends LinearLayout {

    @BindView(R.id.hotCityList)
    RecyclerView hotCityList;

    public QueryHotCity(Context context) {
        this(context, null);
    }

    public QueryHotCity(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.query_hot_city, this);
        ButterKnife.bind(view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        hotCityList.setLayoutManager(layoutManager);
    }

    public void setHotCitys(List<DestinationHotItemBean> cityList) {
        hotCityList.setAdapter(new SearchHotCityAdapter(getContext(), cityList));
        hotCityList.addOnScrollListener(onScrollListener);
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0 && (getContext() instanceof QueryCityActivity)) {
                ((QueryCityActivity) getContext()).scrollQuickChange();
            }
        }
    };
}
