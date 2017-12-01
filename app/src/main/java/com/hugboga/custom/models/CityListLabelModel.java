package com.hugboga.custom.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelItemData;

/**
 * 目的地列表头部快速选择标签区
 * Created by HONGBO on 2017/12/1 18:24.
 */

public class CityListLabelModel extends EpoxyModelWithHolder<CityListLabelModel.LabelHolder> {

    List<LabelItemData> labels;
    FilterView.OnSelectListener onSelectListener1;

    public CityListLabelModel(List<LabelItemData> labels,FilterView.OnSelectListener onSelectListener1) {
        this.labels = labels;
        this.onSelectListener1 = onSelectListener1;
    }

    @Override
    protected CityListLabelModel.LabelHolder createNewHolder() {
        return new LabelHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_list_label_model;
    }

    @Override
    public void bind(LabelHolder holder) {
        super.bind(holder);
        holder.init();
    }

    public class LabelHolder extends EpoxyHolder {

        @BindView(R.id.city_list_label_model_view)
        FilterView filterView;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        public void init(){
            filterView.setData(labels, onSelectListener1);
        }
    }
}
