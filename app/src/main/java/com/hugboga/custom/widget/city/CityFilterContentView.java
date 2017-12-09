package com.hugboga.custom.widget.city;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityAdapter;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.utils.CityDataTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelBean;

/**
 * 筛选条件内容
 * Created by HONGBO on 2017/11/22 16:26.
 */

public class CityFilterContentView extends FrameLayout {

    @BindView(R.id.city_content_filter_view)
    CityFilterView city_content_filter_view; //筛选头部标题选择
    @BindView(R.id.city_content_filter_con)
    FrameLayout city_content_filter_con; //过滤内容容器
    @BindView(R.id.content_city_filte_view1)
    FilterView content_city_filte_view1; //筛选条件内容，游玩线路
    @BindView(R.id.content_city_filte_view2)
    FilterView content_city_filte_view2; //筛选条件内容，出发城市
    @BindView(R.id.content_city_filte_view3)
    FilterView content_city_filte_view3; //筛选条件内容，游玩天数

    CityAdapter adapter;
    DestinationHomeVo data;

    FilterConSelect filterConSelect1;
    FilterConSelect filterConSelect2;
    FilterConSelect filterConSelect3;

    CityDataTools cityDataTools = new CityDataTools();

    public CityFilterContentView(@NonNull Context context) {
        this(context, null);
    }

    public CityFilterContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_content_filter, this);
        ButterKnife.bind(this, view);
    }

    public void setAdapter(CityAdapter adapter) {
        this.adapter = adapter;
    }

    public void setData(DestinationHomeVo data, FilterConSelect filterConSelect1, FilterConSelect filterConSelect2, FilterConSelect filterConSelect3) {
        this.data = data;
        this.filterConSelect1 = filterConSelect1;
        this.filterConSelect2 = filterConSelect2;
        this.filterConSelect3 = filterConSelect3;
        //标签数据
        content_city_filte_view1.setData(cityDataTools.getTagData(data.destinationTagGroupList), onSelectListener1);
        //出发城市数据
        content_city_filte_view2.setData(cityDataTools.getCityData(data.depCityList), onSelectListener2);
        //游玩天数数据
        content_city_filte_view3.setData(cityDataTools.getDayData(data.dayCountList), onSelectListener3);
        //筛选项点击事件
        city_content_filter_view.setFilterSeeListener(new CityFilterView.FilterSeeListener() {
            @Override
            public void onShowFilter(int position, boolean isSelect) {
                showFilterItem(position, isSelect);
            }
        });
    }

    /**
     * 游玩线路标签选中处理
     */
    public FilterView.OnSelectListener onSelectListener1 = new FilterView.OnSelectResultListener() {
        @Override
        public void onParentSelect(FilterView filterView, LabelBean labelBean) {
            super.onParentSelect(filterView, labelBean);
            //关联重设，两个Tag布局内容
            if (adapter != null && filterView != null) {
                adapter.setSelectIds(filterView.getSelectIds());
            }
            content_city_filte_view1.setSelectIds(filterView.getSelectIds());
            // 关联城市联动
            linkCity(labelBean);
        }

        @Override
        public void onSelect(FilterView filterView, LabelBean labelBean) {
            content_city_filte_view1.hide();
            checkFilterConSee();
            city_content_filter_view.clear();
            city_content_filter_view.setTextTag(labelBean.name);
            //关联重设，两个Tag布局内容
            adapter.setSelectIds(filterView.getSelectIds());
            content_city_filte_view1.setSelectIds(filterView.getSelectIds());
            // 关联城市联动
            linkCity(labelBean);
            if (filterConSelect1 != null) {
                filterConSelect1.onSelect(filterView, labelBean);
            }
        }
    };

    /**
     * 关联城市联动
     *
     * @param labelBean
     */
    private void linkCity(LabelBean labelBean) {
        content_city_filte_view2.setEnableClickIds(new CityDataTools().getDepCityIds(labelBean.depIdSet));
    }

    /**
     * 关联标签联动
     *
     * @param labelBean
     */
    private void linkTag(LabelBean labelBean) {
        content_city_filte_view1.setEnableClickIds(new CityDataTools().getDepTagIds(data.destinationTagGroupList, labelBean.id));
    }

    /**
     * 出发城市选中处理
     */
    FilterView.OnSelectListener onSelectListener2 = new FilterView.OnSelectResultListener() {
        @Override
        public void onSelect(FilterView filterView, LabelBean labelBean) {
            content_city_filte_view2.hide();
            checkFilterConSee();
            city_content_filter_view.clear();
            city_content_filter_view.setTextCity(labelBean.name);
            content_city_filte_view2.setSelectIds(filterView.getSelectIds());
            linkTag(labelBean);
            if (filterConSelect2 != null) {
                filterConSelect2.onSelect(filterView, labelBean);
            }
        }
    };

    /**
     * 游玩天数选中处理
     */
    FilterView.OnSelectListener onSelectListener3 = new FilterView.OnSelectResultListener() {
        @Override
        public void onSelect(FilterView filterView, LabelBean labelBean) {
            content_city_filte_view3.hide();
            checkFilterConSee();
            city_content_filter_view.clear();
            city_content_filter_view.setTextDay(labelBean.name);
            content_city_filte_view3.setSelectIds(filterView.getSelectIds());
            if (filterConSelect3 != null) {
                filterConSelect3.onSelect(filterView, labelBean);
            }
        }
    };

    public interface FilterConSelect {
        void onSelect(FilterView filterView, LabelBean labelBean);
    }

    /**
     * 检查过滤器内容是否展示
     */
    private void checkFilterConSee() {
        if (content_city_filte_view1.getVisibility() == View.VISIBLE
                || content_city_filte_view2.getVisibility() == View.VISIBLE
                || content_city_filte_view3.getVisibility() == View.VISIBLE) {
            city_content_filter_con.setVisibility(View.VISIBLE);
        } else {
            city_content_filter_con.setVisibility(View.GONE);
        }
    }

    /**
     * 触发打开筛选项内容
     * Adapter中筛选项点击
     *
     * @param position
     * @param isSelect
     */
    public void showFilterItem(int position, boolean isSelect) {
        clearAllFilterCon();
        switch (position) {
            case 0:
                city_content_filter_view.tabView1.setSelected(isSelect);
                content_city_filte_view1.setVisibility(isSelect ? View.VISIBLE : View.GONE);
                break;
            case 1:
                city_content_filter_view.tabView2.setSelected(isSelect);
                content_city_filte_view2.setVisibility(isSelect ? View.VISIBLE : View.GONE);
                break;
            case 2:
                city_content_filter_view.tabView3.setSelected(isSelect);
                content_city_filte_view3.setVisibility(isSelect ? View.VISIBLE : View.GONE);
                break;
        }
        checkFilterConSee();
    }

    private void clearAllFilterCon() {
        content_city_filte_view1.hide();
        content_city_filte_view2.hide();
        content_city_filte_view3.hide();
    }

    @OnClick(R.id.city_content_filter_con)
    public void unClick(View view) {
        //关闭当前筛选内容框
        city_content_filter_view.clear();
        content_city_filte_view1.hide();
        content_city_filte_view2.hide();
        content_city_filte_view3.hide();
        checkFilterConSee();
    }
}
