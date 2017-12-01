package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.models.CityConfigModel;
import com.hugboga.custom.models.CityListLabelModel;
import com.hugboga.custom.models.CityListModel;
import com.hugboga.custom.models.CityWhatModel;

import java.util.List;

import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelItemData;

/**
 * 目的地城市列表
 * Created by HONGBO on 2017/11/27 16:50.
 */

public class CityAdapter extends EpoxyAdapter {

    private Context mContext;
    List<ServiceConfigVo> serviceConfigList;
    EpoxyModelWithHolder goodeMode; //最后一个model
    public List<LabelItemData> labels;
    public FilterView.OnSelectListener onSelectListener1;
    private boolean isFinish = false; //是否加载到最后

    public CityAdapter(Context context, List<DestinationGoodsVo> data, List<ServiceConfigVo> serviceConfigList) {
        this.mContext = context;
        this.serviceConfigList = serviceConfigList;
        isFinish = false;
        addLabelView();
        addGoods(data);
    }

    private void addGoods(List<DestinationGoodsVo> data) {
        if (data == null) {
            return;
        }
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
        if (serviceConfigList != null) {
            for (ServiceConfigVo vo : serviceConfigList) {
                addModel(new CityConfigModel(mContext, vo));
            }
        }
    }

    private void addWhat() {
        addModel(new CityWhatModel(mContext));
    }

    public void load(List<DestinationGoodsVo> data) {
        removeAllModels();
        isFinish = false;
        addLabelView();
        addGoods(data);
    }

    public void addMoreGoods(List<DestinationGoodsVo> data) {
        if (data == null || data.size() == 0) {
            if (!isFinish) {
                isFinish = true;
                addConfig();
                addWhat();
            }
            return;
        }
        if (goodeMode != null) {
            removeAllAfterModel(goodeMode);
        } else {
            removeAllModels();
            addLabelView();
        }
        addGoods(data);
    }

    /**
     * 添加快速标签选择区
     */
    public void addLabelView() {
        addModel(new CityListLabelModel(labels, onSelectListener1));
    }
}
