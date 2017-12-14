package com.hugboga.custom.adapter;

import android.app.Activity;
import android.content.Context;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;
import com.hugboga.custom.models.city.CityListLabelModel;
import com.hugboga.custom.models.city.CityListModel;
import com.hugboga.custom.models.city.CitySkuNoModel;
import com.hugboga.custom.models.city.CityWhatModel;
import com.hugboga.custom.models.city.CityFilterModel;
import com.hugboga.custom.models.city.CityHeaderModel;

import java.util.List;

import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelBean;
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
    CityActivity.Params params; //页面参数

    public CityAdapter(Context context, DestinationHomeVo vo, List<DestinationGoodsVo> data, List<ServiceConfigVo> serviceConfigList,
                       List<LabelItemData> labels, FilterView.OnSelectListener onSelectListener1, CityActivity.Params params) {
        super(context);
        this.data = vo;
        this.serviceConfigList = serviceConfigList;
        this.params = params;
        cityHeaderModel = new CityHeaderModel((Activity) context, vo, params);
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
        //添加筛选为空sku展示位，默认隐藏
        addModelConfig(citySkuNoModel);
    }

    private void noticeWahtModel() {
        cityWhatModel.noteicModel(goodModels.size() == 0);
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
        noticeWahtModel(); //通知咨询入口是否显示
    }

    private void addModelConfig(EpoxyModel model) {
        if (configModel != null) {
            insertModelBefore(model, configModel);
        } else {
            insertModelBefore(model, cityWhatModel);
        }
    }

    public void load(List<DestinationGoodsVo> data, boolean isInit) {
        clearGoodeMode();
        if (data != null && data.size() > 0) {
            cityListLabelModel.show();
            citySkuNoModel.hide();
            addGoods(data);
        } else {
            if (isInit) {
                cityListLabelModel.hide();
                citySkuNoModel.hide();
            } else {
                cityListLabelModel.show();
                citySkuNoModel.show();
            }
        }
        noticeWahtModel(); //通知咨询入口是否显示
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
    public void setSelectIds(List<LabelBean> ids) {
        if (cityListLabelModel != null) {
            cityListLabelModel.setSelectIds(ids);
        }
    }

    /**
     * 设置已启用标签
     *
     * @param ids
     */
    public void setEnableIds(List<String> ids) {
        if (cityListLabelModel != null) {
            cityListLabelModel.setEnableIds(ids);
        }
    }

    public void showFilterModel(boolean isShow) {
        if (isShow) {
            cityFilterModel.show();
        } else {
            cityFilterModel.hide();
        }
    }

    public int getTop(int padding) {
        return cityHeaderModel.getView().getBottom() + cityListLabelModel.holder.filterView.getHeight() - padding;
    }
}
