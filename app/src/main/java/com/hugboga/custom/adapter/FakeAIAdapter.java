package com.hugboga.custom.adapter;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.ai.ServiceType;
import com.hugboga.custom.models.FakeAIHeaderModel;
import com.hugboga.custom.models.FakeAIItemCardModel;
import com.hugboga.custom.models.FakeAIItemOneModel;
import com.hugboga.custom.models.FakeAIItemTwoModel;
import com.hugboga.custom.models.FakeAIWaitItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by Administrator on 2017/11/29.
 */

public class FakeAIAdapter extends EpoxyAdapter {

    private FakeAIHeaderModel fakeAIHeaderModel;
    private FakeAIWaitItemModel fakeAIWaitItemModel;
    private ArrayList<FakeAIItemCardModel> fakeAIItemCardModels;

    public FakeAIAdapter() {
        addHeader(); //添加头部Hi模块信息
    }

    private void addHeader() {
        fakeAIItemCardModels = new ArrayList<FakeAIItemCardModel>();
        fakeAIHeaderModel = new FakeAIHeaderModel();
        addModel(fakeAIHeaderModel);
    }

    public void resetHeaderInfo(List<String> hiList) {
        fakeAIHeaderModel.bindData(hiList);
    }

    /**
     * 添加自己的问答
     *
     * @param data_all
     */
    public void addMyselfMessage(String data_all) {
        FakeAIItemTwoModel item_twoModel = new FakeAIItemTwoModel(data_all);
        addModel(item_twoModel);
        if (fakeAIWaitItemModel == null) {
            fakeAIWaitItemModel = new FakeAIWaitItemModel();
            addModel(fakeAIWaitItemModel);
        }

    }

    /**
     * 添加服务端问题
     *
     * @param message
     */
    public void addServerMessage(String message) {
        if (fakeAIWaitItemModel != null) {
            removeModel(fakeAIWaitItemModel);
            fakeAIWaitItemModel = null;
        }
        FakeAIItemOneModel model = new FakeAIItemOneModel();
        model.setData(message);
        addModel(model);
    }

    /**
     * 添加服务选项卡片
     *
     * @param serviceTypes
     */
    public void addServerCard(List<ServiceType> serviceTypes) {
        FakeAIItemCardModel fakeAIItemCardModel = new FakeAIItemCardModel();
        fakeAIItemCardModel.bindData(serviceTypes);
        addModel(fakeAIItemCardModel);
        fakeAIItemCardModels.add(fakeAIItemCardModel);
    }

    public void settingCardClick(boolean b) {
        if (fakeAIItemCardModels != null && fakeAIItemCardModels.size() != 0) {
            for (int i = 0; i < fakeAIItemCardModels.size(); i++) {
                fakeAIItemCardModels.get(i).cardClickState(b);
            }
        }
    }

    /**
     * 清除等待的提示条
     */
    public void clearWaitView() {
        if (fakeAIWaitItemModel != null) {
            removeModel(fakeAIWaitItemModel);
            fakeAIWaitItemModel = null;
        }
    }

}
