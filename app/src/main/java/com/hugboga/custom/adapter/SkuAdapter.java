package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.models.CityConfigModel;
import com.hugboga.custom.models.CityListModel;
import com.hugboga.custom.widget.city.CitySkuView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HONGBO on 2017/12/6 17:29.
 */

public class SkuAdapter extends EpoxyAdapter {

    Context mContext;
    DestinationHomeVo data; //数据
    CityConfigModel configModel; //第一个配置model，sku数据加载到其上方
    List<CityListModel> goodModels = new ArrayList<>(); //列表加载的SKU Model数据集合
    Map<String, Boolean> goodsFavious; //收藏线路数据

    public SkuAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addGoods(List<DestinationGoodsVo> data) {
        if (data == null) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            DestinationGoodsVo vo = data.get(i);
            CityListModel model = new CityListModel(mContext, vo, listener);
            addModel(model);
            goodModels.add(model);
        }
    }

    /**
     * 设置是否收藏数据，数据来源为查询最新数据所得
     *
     * @param goodsNos
     */
    public void resetFavious(ArrayList<String> goodsNos) {
        if (goodsNos == null) {
            return;
        }
        if (goodsFavious == null) {
            goodsFavious = new HashMap<>();
        }
        if (goodModels != null && goodModels.size() > 0) {
            for (CityListModel model : goodModels) {
                if (goodsNos.contains(model.getDestinationGoodsVo().goodsNo)) {
                    goodsFavious.put(model.getDestinationGoodsVo().goodsNo, true);
                    model.setFavious(true);
                }
            }
        }
    }

    /**
     * 重置Model收藏信息
     */
    private void resetModelFavious() {
        if (goodsFavious == null) {
            goodsFavious = new HashMap<>();
        }
        if (goodModels != null && goodModels.size() > 0) {
            for (CityListModel model : goodModels) {
                model.setFavious(goodsFavious.containsKey(model.getDestinationGoodsVo().goodsNo));
            }
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

    /**
     * 修改收藏信息后修改当前收藏数据（没有访问接口之前）
     */
    CitySkuView.OnChangeFaviousListener listener = new CitySkuView.OnChangeFaviousListener() {
        @Override
        public void onChange(DestinationGoodsVo destinationGoodsVo, boolean isFavious) {
            // 修改已有收藏数据集合
            if (goodsFavious == null) {
                goodsFavious = new HashMap<>();
            }
            if (isFavious) {
                goodsFavious.put(destinationGoodsVo.goodsNo, true);
            } else {
                goodsFavious.remove(destinationGoodsVo.goodsNo);
            }
            resetModelFavious();
        }
    };

    public List<CityListModel> getGoodModels() {
        return goodModels;
    }
}
