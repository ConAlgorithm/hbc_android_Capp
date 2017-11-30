package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.models.CityConfigModel;
import com.hugboga.custom.models.CityListModel;
import com.hugboga.custom.models.CityWhatModel;

import java.util.List;

/**
 * 目的地城市列表
 * Created by HONGBO on 2017/11/27 16:50.
 */

public class CityAdapter extends EpoxyAdapter {

    private Context mContext;
    List<ServiceConfigVo> dailyServiceConfig;
    EpoxyModelWithHolder goodeMode; //最后一个model

    public CityAdapter(Context context, List<DestinationGoodsVo> data, List<ServiceConfigVo> dailyServiceConfig) {
        this.mContext = context;
        this.dailyServiceConfig = dailyServiceConfig;
        addGoods(data);
        addConfig();
        addWhat();
    }

    private void addGoods(List<DestinationGoodsVo> data) {
        for (int i = 0; i < data.size(); i++) {
            DestinationGoodsVo vo = data.get(i);
            CityListModel model = new CityListModel(mContext, vo);
            if (i == data.size() - 1) {
                goodeMode = model;
            }
            addModel(model);
        }
    }

    private void addConfig() {
        for (ServiceConfigVo vo : dailyServiceConfig) {
            addModel(new CityConfigModel(mContext, vo));
        }
    }

    private void addWhat() {
        addModel(new CityWhatModel(mContext));
    }

    public void load(List<DestinationGoodsVo> data) {
        removeAllModels();
        addGoods(data);
        addConfig();
        addWhat();
    }

    public void addMoreGoods(List<DestinationGoodsVo> data) {
        if (goodeMode != null) {
            removeAllAfterModel(goodeMode);
        }
        addGoods(data);
        addConfig();
        addWhat();
    }
}
