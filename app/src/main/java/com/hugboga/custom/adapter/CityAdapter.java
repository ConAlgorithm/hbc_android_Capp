package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.models.CityListLabelModel;
import com.hugboga.custom.models.CityListModel;
import com.hugboga.custom.models.CitySkuNoModel;
import com.hugboga.custom.models.CityWhatModel;
import com.hugboga.custom.models.city.CityFilterModel;
import com.hugboga.custom.models.city.CityHeaderModel;

import java.util.List;
import java.util.Map;

import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelItemData;

/**
 * 目的地城市列表
 * Created by HONGBO on 2017/11/27 16:50.
 */

public class CityAdapter extends SkuAdapter {

    CityHeaderModel cityHeaderModel; //头部广告部分
    public CityFilterModel cityFilterModel; //过滤器部分
    CityListLabelModel cityListLabelModel; //快速选择标签区
    CitySkuNoModel citySkuNoModel; //筛选没有玩法显示
    CityWhatModel cityWhatModel; //我要咨询入口

    List<ServiceConfigVo> serviceConfigList;

    public CityAdapter(Context context, List<DestinationGoodsVo> data, List<ServiceConfigVo> serviceConfigList,
                       List<LabelItemData> labels, FilterView.OnSelectListener onSelectListener1) {
        super(context);
        this.serviceConfigList = serviceConfigList;
        cityHeaderModel = new CityHeaderModel();
        cityFilterModel = new CityFilterModel();
        cityListLabelModel = new CityListLabelModel(labels, onSelectListener1);
        citySkuNoModel = new CitySkuNoModel();
        cityWhatModel = new CityWhatModel(mContext);
        //业务添加Model
        addModel(cityHeaderModel);
        addModel(cityFilterModel);
        addModel(cityListLabelModel);
        addConfig(serviceConfigList);
        addModel(cityWhatModel);
        addModelConfig(citySkuNoModel);
    }

    public void addGoods(List<DestinationGoodsVo> data) {
        if (data == null) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            DestinationGoodsVo vo = data.get(i);
            CityListModel model = new CityListModel(mContext, vo, listener);
            addModelConfig(model);
            goodModels.add(model);
        }
    }

    private void addModelConfig(EpoxyModel model) {
        if (configModel != null) {
            insertModelBefore(model, configModel);
        } else {
            insertModelBefore(model, cityWhatModel);
        }
    }

    public void load(List<DestinationGoodsVo> data) {
        clearGoodeMode();
        if (data != null && data.size() > 0) {
            cityListLabelModel.show();
            citySkuNoModel.hide();
            addGoods(data);
        } else {
            cityListLabelModel.hide();
            citySkuNoModel.show();
        }
    }

    public void addMoreGoods(List<DestinationGoodsVo> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        addGoods(data);
    }

    /**
     * 删除所有玩法数据，并重新加载玩法数据
     */
    private void clearGoodeMode() {
        if (goodModels != null && goodModels.size() > 0) {
            for (CityListModel model : goodModels) {
                removeModel(model);
            }
            goodModels.clear();
        }
    }

    /**
     * 设置已选中标签序列
     *
     * @param ids
     */
    public void setSelectIds(Map<String, Boolean> ids) {
        if (cityListLabelModel != null) {
            cityListLabelModel.setSelectIds(ids);
        }
    }
}
