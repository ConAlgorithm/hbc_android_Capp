package com.hugboga.custom.adapter;

import android.view.View;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.models.CharterItemModel;
import com.hugboga.custom.models.CharterModelBehavior;
import com.hugboga.custom.models.CharterPickupModel;
import com.hugboga.custom.models.CharterSendModel;
import com.hugboga.custom.models.CharterSubtitleModel;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.widget.charter.CharterSubtitleView;

import java.util.List;

/**
 * Created by qingcha on 17/2/24.
 */
public class CityRouteAdapter extends EpoxyAdapter implements CharterSubtitleView.OnPickUpOrSendSelectedListener {

    private static final int PICKUP_MODEL_TAG = -100;
    private static final int SEND_MODEL_TAG = -101;
    private static final int NO_CHARTER_TAG = -102;

    private CharterSubtitleModel charterSubtitleModel = new CharterSubtitleModel();
    private CharterPickupModel pickupModel;
    private CharterSendModel sendModel;
    private CharterItemModel noCharterModel;
    public EpoxyModel lastSelectedModel;

    public CityRouteAdapter() {
        charterSubtitleModel.setOnPickUpOrSendSelectedListener(this);
        addModel(charterSubtitleModel);
    }

    public void setCityRouteBean(CityRouteBean cityRouteBean) {
        if (cityRouteBean == null || cityRouteBean.cityRouteList == null) {
            return;
        }
        List<CityRouteBean.CityRouteScope> cityRouteList = cityRouteBean.cityRouteList;
        final int size = cityRouteList.size();
        for (int i = 0; i < size; i++) {
            CharterItemModel charterItemModel = new CharterItemModel();
            charterItemModel.setCityRouteScope(cityRouteList.get(i));
            if (i == 0) {
                charterItemModel.setSelected(true);
            } else {
                lastSelectedModel = charterItemModel;
            }
            charterItemModel.setOnClickListener(itemListener);
            charterItemModel.setTag(i);
            addModel(charterItemModel);
        }
    }

    public void insertPickupModel() {
        if (pickupModel == null) {
            pickupModel = new CharterPickupModel();
            pickupModel.setOnClickListener(itemListener);
            pickupModel.setTag(PICKUP_MODEL_TAG);
            insertModelAfter(pickupModel, charterSubtitleModel);
        }
    }

    public void insertSendModel() {
        if (sendModel == null) {
            sendModel = new CharterSendModel();
            sendModel.setOnClickListener(itemListener);
            sendModel.setTag(SEND_MODEL_TAG);
            insertModelAfter(sendModel, charterSubtitleModel);
        }
    }

    public void insertNoCharterModel() {
        if (noCharterModel == null) {
            noCharterModel = new CharterItemModel();
            noCharterModel.setOnClickListener(itemListener);
            noCharterModel.setTag(NO_CHARTER_TAG);
            CityRouteBean.CityRouteScope cityRouteScope = new CityRouteBean.CityRouteScope();
            cityRouteScope.routeType = CityRouteBean.RouteType.AT_WILL;
            noCharterModel.setCityRouteScope(cityRouteScope);
            addModel(noCharterModel);
        }
    }

    public void updatePickupModel() {
        notifyModelChanged(pickupModel);
    }

    public void updateSendModel() {
        notifyModelChanged(sendModel);
    }

    public void updateSubtitleModel() {
        notifyModelChanged(charterSubtitleModel);
    }

    public void updateSelectedModel() {
        notifyModelChanged(lastSelectedModel);
    }

    @Override
    protected void showModel(EpoxyModel<?> model, boolean show) {
        if (model == null) {
            return;
        }
        super.showModel(model, show);
    }

    private final View.OnClickListener itemListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            setSelectedItem(v, null);
        }
    };

    public void setSelectedItem(View v, EpoxyModel epoxyModel) {
        List<EpoxyModel<?>> modelList = getAllModelsAfter(charterSubtitleModel);
        for (int i = 0; i < modelList.size(); i++) {
            if (modelList.get(i) instanceof CharterModelBehavior) {
                CharterModelBehavior charterModelBehavior = (CharterModelBehavior) modelList.get(i);
                boolean isView = v != null && v.getTag() instanceof Integer && (int)v.getTag() == charterModelBehavior.getTag();
                boolean isModel = modelList.get(i) == epoxyModel;
                if (isModel || isView) {
                    lastSelectedModel = modelList.get(i);
                    charterModelBehavior.setSelected(true);
                } else {
                    charterModelBehavior.setSelected(false);
                }
                notifyModelChanged(modelList.get(i));
            } else {
                continue;
            }
        }
    }

    public void notifyAllModelsChanged(CityRouteBean cityRouteBean, int selectedRouteType) {
        if (cityRouteBean == null || cityRouteBean.cityRouteList == null) {
            return;
        }

        updateSubtitleModel();

        CharterDataUtils charterDataUtils = CharterDataUtils.getInstance();

        if (pickupModel != null) {
            if (charterDataUtils.isFirstDay() && charterDataUtils.flightBean != null) {
                pickupModel.show();
                pickupModel.setSelected(selectedRouteType == CityRouteBean.RouteType.PICKUP);
            } else {
                pickupModel.hide();
            }
            notifyModelChanged(pickupModel);
        }


        if (sendModel != null) {
            if (charterDataUtils.isLastDay() && charterDataUtils.airPortBean != null) {
                sendModel.show();
                pickupModel.setSelected(selectedRouteType == CityRouteBean.RouteType.SEND);
            } else {
                sendModel.hide();
            }
            notifyModelChanged(sendModel);
        }

        if (!charterDataUtils.isFirstDay() && !charterDataUtils.isLastDay()) {//随便转转，不包车
            insertNoCharterModel();
            if (noCharterModel != null) {
                noCharterModel.show();
                noCharterModel.setSelected(selectedRouteType == CityRouteBean.RouteType.AT_WILL);
                notifyModelChanged(noCharterModel);
            }
        } else {
            if (noCharterModel != null) {
                noCharterModel.hide();
                notifyModelChanged(noCharterModel);
            }
        }

        List<CityRouteBean.CityRouteScope> cityRouteList = cityRouteBean.cityRouteList;
        final int cityRouteListSize = cityRouteList.size();

        List<EpoxyModel<?>> modelList = getAllModelsAfter(charterSubtitleModel);
        for (int i = 0; i < modelList.size(); i++) {
            if (modelList.get(i) instanceof CharterItemModel) {
                CharterItemModel charterItemModel = (CharterItemModel) modelList.get(i);
                int modelTag = charterItemModel.getTag();
                if (modelTag < 0) {
                    continue;
                } else if (modelTag < cityRouteListSize) {
                    charterItemModel.setCityRouteScope(cityRouteList.get(modelTag));
                    charterItemModel.setSelected(selectedRouteType == charterItemModel.getRouteType());
                    notifyModelChanged(modelList.get(i));
                } else {
                    hideModel(charterItemModel);
                }
            } else {
                continue;
            }
        }
    }

    @Override
    public void onPickUpOrSendSelected(boolean isPickUp, boolean isSelected) {
        EpoxyModel<?> model = null;
        if (isPickUp) {//接机
            model = pickupModel;
        } else {//送机
            model = sendModel;
        }
        showModel(model, isSelected);
        notifyModelChanged(model);
        if (model instanceof CharterModelBehavior) { //更改选中状态
            boolean isSelectedModel = ((CharterModelBehavior) model).isSelected();
            if (isSelectedModel) {
                setSelectedItem(null, getAllModelsAfter(model).get(0));
            }
        }
    }
}
