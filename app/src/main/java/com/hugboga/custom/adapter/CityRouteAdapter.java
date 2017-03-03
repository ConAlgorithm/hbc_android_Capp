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

import java.util.ArrayList;
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
    private ArrayList<CharterItemModel> itemModelList;

    private OnCharterItemClickListener listener;

    public CityRouteAdapter() {
        itemModelList = new ArrayList<CharterItemModel>();
        charterSubtitleModel.setOnPickUpOrSendSelectedListener(this);
        addModel(charterSubtitleModel);
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
            CityRouteBean.CityRouteScope cityRouteScope = new CityRouteBean.CityRouteScope(CityRouteBean.RouteType.AT_WILL);
            cityRouteScope.routeTitle = "自己转转，不包车";
            noCharterModel.setCityRouteScope(cityRouteScope);
            insertModelAfter(noCharterModel, itemModelList.get(itemModelList.size() - 1));
        }
    }

    public void showPickupModel() {
        showModel(pickupModel);
    }

    public void updatePickupModel() {
        notifyModelChanged(pickupModel);
    }

    public void showSendModel() {
        showModel(sendModel);
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
        if (lastSelectedModel instanceof CharterModelBehavior) {
            CityRouteBean.CityRouteScope cityRouteScope = ((CharterModelBehavior) lastSelectedModel).getCityRouteScope();
            if (listener != null) {
                listener.onCharterItemClick(cityRouteScope);
            }
        }
    }

    public void notifyAllModelsChanged(CityRouteBean cityRouteBean, int selectedRouteType) {
        if (cityRouteBean == null || cityRouteBean.cityRouteList == null) {
            return;
        }
        CharterDataUtils charterDataUtils = CharterDataUtils.getInstance();

        updateSubtitleModel();

        CharterModelBehavior selectedModel = null;

        insertPickupModel();
        if (pickupModel != null) {
            if (charterDataUtils.isFirstDay() && charterDataUtils.flightBean != null) {
                pickupModel.show();
                if (selectedRouteType == CityRouteBean.RouteType.PICKUP) {
                    pickupModel.setSelected(true);
                    selectedModel = pickupModel;
                } else {
                    pickupModel.setSelected(false);
                }
            } else {
                pickupModel.hide();
            }
            notifyModelChanged(pickupModel);
        }

        insertSendModel();
        if (sendModel != null) {
            if (charterDataUtils.isLastDay() && charterDataUtils.airPortBean != null) {
                sendModel.show();
                if (selectedRouteType == CityRouteBean.RouteType.SEND) {
                    sendModel.setSelected(true);
                    selectedModel = sendModel;
                } else {
                    sendModel.setSelected(false);
                }
            } else {
                sendModel.hide();
            }
            notifyModelChanged(sendModel);
        }


        List<CityRouteBean.CityRouteScope> cityRouteList = cityRouteBean.cityRouteList;
        final int cityRouteListSize = cityRouteList.size();
        final int itemModelSize = itemModelList.size();
        if (itemModelSize == 0) {
            for (int i = 0; i < cityRouteListSize; i++) {
                CharterItemModel charterItemModel = new CharterItemModel();
                CityRouteBean.CityRouteScope cityRouteScope = cityRouteList.get(i);
                cityRouteScope.fenceSwitch = cityRouteBean.fenceSwitch;
                charterItemModel.setCityRouteScope(cityRouteScope);
                charterItemModel.setPosition(i);
                itemModelList.add(charterItemModel);
                if (i == 0) {
                    charterItemModel.setSelected(true);
                    lastSelectedModel = charterItemModel;
                    if (listener != null) {
                        listener.onCharterItemClick(cityRouteScope);
                    }
                }
                charterItemModel.setOnClickListener(itemListener);
                addModel(charterItemModel);
            }
        } else {
            for (int i = itemModelSize; i < cityRouteListSize; i++) {
                CharterItemModel charterItemModel = new CharterItemModel();
                CityRouteBean.CityRouteScope cityRouteScope = cityRouteList.get(i);
                cityRouteScope.fenceSwitch = cityRouteBean.fenceSwitch;
                charterItemModel.setCityRouteScope(cityRouteScope);
                charterItemModel.setPosition(i);
                itemModelList.add(charterItemModel);
                charterItemModel.setOnClickListener(itemListener);
                insertModelAfter(charterItemModel, itemModelList.get(itemModelSize));
            }
        }

        insertNoCharterModel();
        if (!charterDataUtils.isFirstDay() && !charterDataUtils.isLastDay()) {//随便转转，不包车
            if (noCharterModel != null) {
                noCharterModel.show();
                if (selectedRouteType == CityRouteBean.RouteType.AT_WILL) {
                    noCharterModel.setSelected(true);
                    selectedModel = noCharterModel;
                } else {
                    noCharterModel.setSelected(false);
                }
                notifyModelChanged(noCharterModel);
            }
        } else {
            if (noCharterModel != null) {
                noCharterModel.hide();
                notifyModelChanged(noCharterModel);
            }
        }

        List<EpoxyModel<?>> modelList = getAllModelsAfter(charterSubtitleModel);
        for (int i = 0; i < modelList.size(); i++) {
            if (modelList.get(i) instanceof CharterItemModel) {
                CharterItemModel charterItemModel = (CharterItemModel) modelList.get(i);
                int modelTag = charterItemModel.getRouteType();
                if (modelTag < 0) {
                    continue;
                } else if (charterItemModel.getPosition() < cityRouteListSize) {
                    charterItemModel.setCityRouteScope(cityRouteList.get(charterItemModel.getPosition()));
                    if (selectedRouteType == charterItemModel.getRouteType()) {
                        charterItemModel.setSelected(true);
                        selectedModel = charterItemModel;
                    } else {
                        charterItemModel.setSelected(false);
                    }
                    notifyModelChanged(modelList.get(i));
                } else {
                    hideModel(charterItemModel);
                }
            } else {
                continue;
            }
        }
        if (listener != null && selectedModel != null) {
            listener.onCharterItemClick(selectedModel.getCityRouteScope());
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
