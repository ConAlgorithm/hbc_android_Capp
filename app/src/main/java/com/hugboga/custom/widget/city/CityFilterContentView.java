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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelBean;
import tk.hongbo.label.data.LabelItemData;

/**
 * 筛选条件内容
 * Created by HONGBO on 2017/11/22 16:26.
 */

public class CityFilterContentView extends FrameLayout {

    @BindView(R.id.city_content_filter_view)
    public CityFilterView city_content_filter_view; //筛选头部标题选择
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

    public CityFilterContentView(@NonNull Context context) {
        this(context, null);
    }

    public CityFilterContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.city_content_filter, this);
        ButterKnife.bind(this, view);
    }

    public void reset(List<LabelItemData> labelTag, List<LabelItemData> labelCity, List<LabelItemData> labelDay,
                      FilterConSelect filterConSelect1, FilterConSelect filterConSelect2, FilterConSelect filterConSelect3) {
        this.filterConSelect1 = filterConSelect1;
        this.filterConSelect2 = filterConSelect2;
        this.filterConSelect3 = filterConSelect3;
        content_city_filte_view1.setData(labelTag, onSelectListener1);
        //出发城市数据
        content_city_filte_view2.setData(labelCity, onSelectListener2);
        //游玩天数数据
        content_city_filte_view3.setData(labelDay, onSelectListener3);
    }

    public void setAdapter(CityAdapter adapter) {
        this.adapter = adapter;
    }

    public void setData(DestinationHomeVo data) {
        this.data = data;
    }

    /**
     * 游玩线路标签选中处理
     */
    public FilterView.OnSelectListener onSelectListener1 = new FilterView.OnSelectResultListener() {
        @Override
        public void onParentSelect(FilterView filterView, LabelBean labelBean) {
            super.onParentSelect(filterView, labelBean);
            //关联重设，两个Tag布局内容
            if (filterView == content_city_filte_view1) {
                adapter.setSelectIds(filterView.getSelectIds());
            } else {
                content_city_filte_view1.setSelectIds(filterView.getSelectIds());
            }
            // 关联城市联动
            linkCity(labelBean);
        }

        @Override
        public void onSelect(FilterView filterView, LabelBean labelBean) {
            content_city_filte_view1.hide();
            //关联重设，两个Tag布局内容
            if (filterView == content_city_filte_view1) {
                adapter.setSelectIds(filterView.getSelectIds());
            } else {
                content_city_filte_view1.setSelectIds(filterView.getSelectIds());
            }
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
            linkTag(labelBean);
        }
    };

    /**
     * 游玩天数选中处理
     */
    FilterView.OnSelectListener onSelectListener3 = new FilterView.OnSelectResultListener() {
        @Override
        public void onSelect(FilterView filterView, LabelBean labelBean) {
            content_city_filte_view3.hide();
        }
    };

    public interface FilterConSelect {
        void onSelect(FilterView filterView, LabelBean labelBean);
    }
}
