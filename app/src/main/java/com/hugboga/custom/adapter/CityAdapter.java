package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.models.CityConfigModel;
import com.hugboga.custom.models.CityListLabelModel;
import com.hugboga.custom.models.CityListModel;
import com.hugboga.custom.models.CitySkuNoModel;
import com.hugboga.custom.models.CityWhatModel;
import com.hugboga.custom.widget.city.CitySkuView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelItemData;

/**
 * 目的地城市列表
 * Created by HONGBO on 2017/11/27 16:50.
 */

public class CityAdapter extends SkuAdapter {

    List<ServiceConfigVo> serviceConfigList;

    CityListLabelModel cityListLabelModel; //快速选择标签区
    CityConfigModel configModel; //第一个配置model，sku数据加载到其上方
    CityWhatModel cityWhatModel; //我要咨询入口
    CitySkuNoModel citySkuNoModel; //筛选没有玩法显示

    public CityAdapter(Context context, List<DestinationGoodsVo> data, List<ServiceConfigVo> serviceConfigList,
                       List<LabelItemData> labels, FilterView.OnSelectListener onSelectListener1) {
        super(context);
        this.serviceConfigList = serviceConfigList;
        cityListLabelModel = new CityListLabelModel(labels, onSelectListener1);
        citySkuNoModel = new CitySkuNoModel();
        cityWhatModel = new CityWhatModel(mContext);
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

    private void addConfig(List<ServiceConfigVo> serviceConfigList) {
        if (serviceConfigList != null) {
            for (ServiceConfigVo vo : serviceConfigList) {
                CityConfigModel model = new CityConfigModel(mContext, vo);
                if (configModel == null) {
                    configModel = model;
                }
                addModel(model);
            }
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
