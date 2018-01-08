package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.models.city.CityConfigModel;
import com.hugboga.custom.models.city.CityListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HONGBO on 2017/12/6 17:29.
 */

public class SkuAdapter extends EpoxyAdapter {

    Context mContext;
    DestinationHomeVo data; //数据
    CityConfigModel configModel; //第一个配置model，sku数据加载到其上方
    List<CityListModel> goodModels = new ArrayList<>(); //列表加载的SKU Model数据集合

    public SkuAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addGoods(List<DestinationGoodsVo> data) {
        if (data == null) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            DestinationGoodsVo vo = data.get(i);
            CityListModel model = new CityListModel(mContext, vo);
            addModel(model);
            goodModels.add(model);
        }
    }

    public void addConfig(List<ServiceConfigVo> serviceConfigList) {
        if (serviceConfigList != null) {
            for (ServiceConfigVo vo : serviceConfigList) {
                CityConfigModel model = new CityConfigModel(mContext, vo, data);
                if (configModel == null) {
                    configModel = model;
                }
                addModel(model);
            }
        }
    }

    public List<CityListModel> getGoodModels() {
        return goodModels;
    }
}
