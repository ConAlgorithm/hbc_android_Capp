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

    private CharterSubtitleModel charterSubtitleModel = new CharterSubtitleModel();
    private CharterPickupModel pickupModel;
    private CharterSendModel sendModel;
    private CharterItemModel noCharterModel;
    public EpoxyModel lastSelectedModel;

    private OnCharterItemClickListener listener;

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
            CityRouteBean.CityRouteScope cityRouteScope = cityRouteList.get(i);
            cityRouteScope.fenceSwitch = cityRouteBean.fenceSwitch;
            charterItemModel.setCityRouteScope(cityRouteScope);
            charterItemModel.setPosition(i);
            if (i == 0) {
                charterItemModel.setSelected(true);
            } else {
                lastSelectedModel = charterItemModel;
            }
            charterItemModel.setOnClickListener(itemListener);
            addModel(charterItemModel);
        }
    }

    public void insertPickupModel() {
        if (pickupModel == null) {
            pickupModel = new CharterPickupModel();
            pickupModel.setOnClickListener(itemListener);
            pickupModel.setCityRouteScope(new CityRouteBean.CityRouteScope(CityRouteBean.RouteType.PICKUP));
            insertModelAfter(pickupModel, charterSubtitleModel);
        }
    }

    public void insertSendModel() {
        if (sendModel == null) {
            sendModel = new CharterSendModel();
            sendModel.setOnClickListener(itemListener);
            sendModel.setCityRouteScope(new CityRouteBean.CityRouteScope(CityRouteBean.RouteType.SEND));
            insertModelAfter(sendModel, charterSubtitleModel);
        }
    }

    public void insertNoCharterModel() {
        if (noCharterModel == null) {
            noCharterModel = new CharterItemModel();
            noCharterModel.setOnClickListener(itemListener);
            noCharterModel.setCityRouteScope(new CityRouteBean.CityRouteScope(CityRouteBean.RouteType.AT_WILL));
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
                boolean isView = v != null && v.getTag() instanceof Integer && (int)v.getTag() == charterModelBehavior.getRouteType();
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
        if (lastSelectedModel instanceof CharterItemModel) {
            CityRouteBean.CityRouteScope cityRouteScope = ((CharterItemModel) lastSelectedModel).getCityRouteScope();
            if (listener != null) {
                listener.onCharterItemClick(cityRouteScope);
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
                int modelTag = charterItemModel.getRouteType();
                if (modelTag < 0) {
                    continue;
                } else if (charterItemModel.getPosition() < cityRouteListSize) {
                    charterItemModel.setCityRouteScope(cityRouteList.get(charterItemModel.getPosition()));
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

    public interface OnCharterItemClickListener {
        public void onCharterItemClick(CityRouteBean.CityRouteScope cityRouteScope);
    }

    public void setOnCharterItemClickListener(OnCharterItemClickListener listener) {
        this.listener = listener;
    }
}
