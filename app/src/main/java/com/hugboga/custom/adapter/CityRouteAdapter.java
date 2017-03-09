package com.hugboga.custom.adapter;

import android.view.View;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.models.CharterEmptyModel;
import com.hugboga.custom.models.CharterFooterModel;
import com.hugboga.custom.models.CharterItemModel;
import com.hugboga.custom.models.CharterModelBehavior;
import com.hugboga.custom.models.CharterPickupModel;
import com.hugboga.custom.models.CharterSendModel;
import com.hugboga.custom.models.CharterSubtitleModel;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.widget.charter.CharterEmptyView;
import com.hugboga.custom.widget.charter.CharterSubtitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/2/24.
 */
public class CityRouteAdapter extends EpoxyAdapter implements CharterSubtitleView.OnPickUpOrSendSelectedListener {

    private CharterSubtitleModel charterSubtitleModel = new CharterSubtitleModel();
    private CharterFooterModel charterFooterModel;
    private CharterPickupModel pickupModel;
    private CharterSendModel sendModel;
    private CharterItemModel noCharterModel;
    private CharterEmptyModel charterEmptyModel = new CharterEmptyModel();

    private ArrayList<CharterItemModel> itemModelList;
    public EpoxyModel lastSelectedModel;
    private CharterDataUtils charterDataUtils;

    private OnCharterItemClickListener onCharterItemClickListener;
    private CharterSubtitleView.OnPickUpOrSendSelectedListener onPickUpOrSendSelectedListener;

    public CityRouteAdapter() {
        charterDataUtils = CharterDataUtils.getInstance();
        itemModelList = new ArrayList<CharterItemModel>();
        charterSubtitleModel.setOnPickUpOrSendSelectedListener(this);
        addModel(charterSubtitleModel);
        addModel(charterEmptyModel);
        charterEmptyModel.hide();
        notifyModelChanged(charterEmptyModel);
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

    public void showEmpty(int type, boolean isShow) {
        if (isShow) {
            charterEmptyModel.setEmptyType(type);
            showModel(charterEmptyModel);
            hideModels(getAllModelsAfter(charterEmptyModel));
            if (noCharterModel != null) {
                hideModel(noCharterModel);
            }
            charterSubtitleModel.setPickupLayoutVisibility(View.GONE);
        } else {
            hideModel(charterEmptyModel);
            showModels(getAllModelsAfter(charterEmptyModel));
            if (noCharterModel != null) {
                showModel(noCharterModel);
            }
            charterSubtitleModel.setPickupLayoutVisibility(View.VISIBLE);
        }
    }

    public void insertCharterFooterModel() {
        if (charterFooterModel == null) {
            charterFooterModel = new CharterFooterModel();
            addModel(charterFooterModel);
        }
    }

    public void showPickupModel() {
        showModel(pickupModel);
    }

    public void updatePickupModel() {
        notifyModelChanged(pickupModel);
    }

    public void hidePickupModel() {
        hideModel(pickupModel);
    }

    public void updatePickupModelVisibility() {
        if (pickupModel == null) {
            return;
        }
        if (charterDataUtils.currentDay == 1
                && charterDataUtils.chooseDateBean.dayNums > 1
                && charterDataUtils.isSelectedPickUp
                && charterDataUtils.flightBean != null) {
            pickupModel.show();
            notifyModelChanged(pickupModel);
        } else {
            hidePickupModel();
        }
    }

    public void updateNoCharterModelVisibility() {
        if (noCharterModel == null) {
            return;
        }
        charterDataUtils = CharterDataUtils.getInstance();
        if (!charterEmptyModel.isShown() && charterDataUtils.currentDay > 1 && charterDataUtils.currentDay < charterDataUtils.chooseDateBean.dayNums) {
            noCharterModel.show();
            notifyModelChanged(noCharterModel);
        } else {
            hideModel(noCharterModel);
        }
    }

    public void updateModelFenceSwitch(int fenceSwitch) {
        if (pickupModel != null && pickupModel.getCityRouteScope() != null) {
            pickupModel.getCityRouteScope().fenceSwitch = fenceSwitch;
        }
        if (sendModel != null && sendModel.getCityRouteScope() != null) {
            sendModel.getCityRouteScope().fenceSwitch = fenceSwitch;
        }
        if (noCharterModel != null && noCharterModel.getCityRouteScope() != null) {
            noCharterModel.getCityRouteScope().fenceSwitch = fenceSwitch;
        }
    }

    public void showSendModel() {
        showModel(sendModel);
        setOuttownModelVisibility();
    }

    public void updateSendModel() {
        notifyModelChanged(sendModel);
    }

    public void hideSendModel() {
        hideModel(sendModel);
        setOuttownModelVisibility();
    }

    public void updateSubtitleModel() {
        notifyModelChanged(charterSubtitleModel);
    }

    public void updateSelectedModel() {
        notifyModelChanged(lastSelectedModel);
    }

    public void setOuttownModelVisibility() {
        if (!charterDataUtils.isLastDay()) {
            return;
        }
        boolean isHide = sendModel.isShown() && charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null;
        int size = itemModelList.size();
        for (int i = 0; i < size; i++) {
            CharterItemModel charterItemModel = itemModelList.get(i);
            if (charterItemModel.getRouteType() == CityRouteBean.RouteType.OUTTOWN) {
                if (isHide) {
                    hideModel(charterItemModel);
                    if (charterItemModel.isSelected()) {
                        setSelectedItem(null, itemModelList.get(0));
                    }
                } else {
                    showModel(charterItemModel);
                }
            }
        }
    }

    public CharterModelBehavior getSelectedModel() {
        if (lastSelectedModel instanceof CharterModelBehavior) {
            return (CharterModelBehavior) lastSelectedModel;
        } else {
            return null;
        }
    }

    @Override
    protected void showModel(EpoxyModel<?> model, boolean show) {
        if (model == null) {
            return;
        }
        super.showModel(model, show);
    }

    public int getLastSelectedPosition() {
        return getModelPosition(lastSelectedModel);
    }

    private final View.OnClickListener itemListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            setSelectedItem(v, null);
        }
    };

    public void setSelectedItem(View v, EpoxyModel epoxyModel) {
        List<EpoxyModel<?>> modelList = getAllModelsAfter(charterSubtitleModel);
        final int size = modelList.size();
        for (int i = 0; i < size; i++) {
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
            if (onCharterItemClickListener != null) {
                onCharterItemClickListener.onCharterItemClick(cityRouteScope);
            }
        }
    }

    public void notifyAllModelsChanged(CityRouteBean cityRouteBean, int selectedRouteType) {
        if (cityRouteBean == null || cityRouteBean.cityRouteList == null) {
            return;
        }
        CharterDataUtils charterDataUtils = CharterDataUtils.getInstance();

        updateSubtitleModel();

        EpoxyModel selectedModel = null;

        insertPickupModel();
        if (pickupModel != null) {
            if (charterDataUtils.chooseDateBean.dayNums > 1 && charterDataUtils.isFirstDay() && charterDataUtils.flightBean != null && charterDataUtils.isSelectedPickUp) {
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
            if (charterDataUtils.isLastDay() && charterDataUtils.airPortBean != null && charterDataUtils.isSelectedSend) {
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
                addModel(getNewCharterItemModel(cityRouteBean, cityRouteList, i));
            }
        } else {
            for (int i = itemModelSize; i < cityRouteListSize; i++) {
                insertModelAfter(getNewCharterItemModel(cityRouteBean, cityRouteList, i), itemModelList.get(itemModelSize));
            }
        }

        insertNoCharterModel();
        if (!charterDataUtils.isFirstDay() && !charterDataUtils.isLastDay()) {//随便转转，不包车
            if (!charterEmptyModel.isShown() && noCharterModel != null) {
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
        int fenceSwitch = 0;

        List<EpoxyModel<?>> modelList = getAllModelsAfter(charterSubtitleModel);
        final int modelListSize = modelList.size();
        for (int i = 0; i < modelListSize; i++) {
            if (modelList.get(i) instanceof CharterItemModel) {
                CharterItemModel charterItemModel = (CharterItemModel) modelList.get(i);
                int modelTag = charterItemModel.getRouteType();
                if (modelTag < 0) {
                    continue;
                } else if (charterItemModel.getPosition() < cityRouteListSize) {
                    if (sendModel.isShown() && charterItemModel.getRouteType() == CityRouteBean.RouteType.OUTTOWN) {
                        charterItemModel.show(false);
                    } else {
                        charterItemModel.show(true);
                    }
                    CityRouteBean.CityRouteScope cityRouteScope = cityRouteList.get(charterItemModel.getPosition());
                    cityRouteScope.fenceSwitch = cityRouteBean.fenceSwitch;
                    fenceSwitch = cityRouteBean.fenceSwitch;
                    charterItemModel.setCityRouteScope(cityRouteScope);
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
        lastSelectedModel = selectedModel;
        if (onCharterItemClickListener != null && selectedModel != null) {
            onCharterItemClickListener.onCharterItemClick(((CharterModelBehavior)selectedModel).getCityRouteScope());
        }
        insertCharterFooterModel();

        updateModelFenceSwitch(fenceSwitch);
    }

    @Override
    public void onPickUpOrSendSelected(boolean isPickUp, boolean isSelected) {
        EpoxyModel<?> model = null;
        if (isPickUp) {//接机
            model = pickupModel;
        } else {//送机
            model = sendModel;
        }
        if (charterDataUtils.chooseDateBean.dayNums > 1) {
            showModel(model, isSelected);
            if (model instanceof CharterModelBehavior) { //更改选中状态
                boolean isSelectedModel = ((CharterModelBehavior) model).isSelected();
                if (isSelectedModel) {
                    setSelectedItem(null, itemModelList.get(0));
                }
            }
        }
        setOuttownModelVisibility();
        if (onPickUpOrSendSelectedListener != null) {
            onPickUpOrSendSelectedListener.onPickUpOrSendSelected(isPickUp, isSelected);
        }
    }

    public CharterItemModel getNewCharterItemModel(CityRouteBean cityRouteBean, List<CityRouteBean.CityRouteScope> cityRouteList, int i) {
        CharterItemModel charterItemModel = new CharterItemModel();
        CityRouteBean.CityRouteScope cityRouteScope = cityRouteList.get(i);
        cityRouteScope.fenceSwitch = cityRouteBean.fenceSwitch;
        charterItemModel.setCityRouteScope(cityRouteScope);
        charterItemModel.setPosition(i);
        itemModelList.add(charterItemModel);
        charterItemModel.setOnClickListener(itemListener);
        return charterItemModel;
    }

    public interface OnCharterItemClickListener {
        public void onCharterItemClick(CityRouteBean.CityRouteScope cityRouteScope);
    }

    public void setOnCharterItemClickListener(OnCharterItemClickListener listener) {
        this.onCharterItemClickListener = listener;
    }

    public void setOnPickUpOrSendSelectedListener(CharterSubtitleView.OnPickUpOrSendSelectedListener listener) {
        this.onPickUpOrSendSelectedListener = listener;
    }

    public void setOnRefreshDataListener(CharterEmptyView.OnRefreshDataListener listener) {
        charterEmptyModel.setOnRefreshDataListener(listener);
        notifyModelChanged(charterEmptyModel);
    }
}
