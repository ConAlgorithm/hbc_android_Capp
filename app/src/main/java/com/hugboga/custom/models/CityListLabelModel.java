package com.hugboga.custom.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelBean;
import tk.hongbo.label.data.LabelItemData;

/**
 * 目的地列表头部快速选择标签区
 * Created by HONGBO on 2017/12/1 18:24.
 */

public class CityListLabelModel extends EpoxyModelWithHolder<CityListLabelModel.LabelHolder> {

    List<LabelItemData> labels;
    FilterView.OnSelectListener onSelectListener1;
    LabelHolder holder;

    List<LabelBean> selectIds; //快速选择已选中数据
    List<String> enableIds; //快速选择启用数据

    public CityListLabelModel(List<LabelItemData> labels, FilterView.OnSelectListener onSelectListener1) {
        this.labels = labels;
        this.onSelectListener1 = onSelectListener1;
    }

    @Override
    protected CityListLabelModel.LabelHolder createNewHolder() {
        return new LabelHolder();
    }

    @Override
    public void bind(LabelHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        this.holder = holder;
        holder.filterView.setData(labels, onSelectListener1);
        if (selectIds != null) {
            setSelectIds(selectIds);
        }
        if (enableIds != null) {
            setEnableIds(enableIds);
        }
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_list_label_model;
    }

    public class LabelHolder extends EpoxyHolder {

        @BindView(R.id.city_list_label_model_view)
        FilterView filterView;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 设置已选中标签序列
     *
     * @param ids
     */
    public void setSelectIds(List<LabelBean> ids) {
        this.selectIds = ids;
        if (holder != null) {
            holder.filterView.setSelectIds(ids);
        }
    }

    public void setEnableIds(List<String> ids) {
        this.enableIds = ids;
        if (holder != null) {
            holder.filterView.setEnableClickIds(ids);
        }
    }
}
