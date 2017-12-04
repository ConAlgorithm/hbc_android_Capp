package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.models.CityConfigModel;
import com.hugboga.custom.models.CityListLabelModel;
import com.hugboga.custom.models.CityListModel;
import com.hugboga.custom.models.CityWhatModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelItemData;

/**
 * 目的地城市列表
 * Created by HONGBO on 2017/11/27 16:50.
 */

public class CityAdapter extends EpoxyAdapter {

    private Context mContext;

    private List<CityListModel> goodModels = new ArrayList<>(); //列表加载的SKU Model数据集合
    CityListLabelModel cityListLabelModel; //快速选择标签区
    CityConfigModel configModel; //第一个配置model，sku数据加载到其上方

    public CityAdapter(Context context, List<DestinationGoodsVo> data, List<ServiceConfigVo> serviceConfigList,
                       List<LabelItemData> labels, FilterView.OnSelectListener onSelectListener1) {
        this.mContext = context;
        cityListLabelModel = new CityListLabelModel(labels, onSelectListener1);
        addModel(cityListLabelModel);
        addConfig(serviceConfigList);
        addModel(new CityWhatModel(mContext));
        addGoods(data);
    }

    private void addGoods(List<DestinationGoodsVo> data) {
        if (data == null) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            DestinationGoodsVo vo = data.get(i);
            CityListModel model = new CityListModel(mContext, vo);
            insertModelBefore(model, configModel);
            goodModels.add(model);
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
        addGoods(data);
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
    public void setSelectIds(Map<Integer, Boolean> ids) {
        if (cityListLabelModel != null) {
            cityListLabelModel.setSelectIds(ids);
        }
    }
}
