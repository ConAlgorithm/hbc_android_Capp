package com.hugboga.custom.adapter;

import android.os.Handler;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.models.FakeAIHeaderModel;
import com.hugboga.custom.models.FakeAIItemOneModel;
import com.hugboga.custom.models.FakeAIItemTwoModel;
import com.hugboga.custom.models.FakeAIWaitItemModel;
import java.util.List;


/**
 * Created by Administrator on 2017/11/29.
 */

public class FakeAIAdapter extends EpoxyAdapter {

    private FakeAIHeaderModel fake_ai_headerModel;
    private FakeAIWaitItemModel item_WaitModel;

    public FakeAIAdapter() {
        addHeader(); //添加头部Hi模块信息

    }

    private void addHeader() {
        fake_ai_headerModel = new FakeAIHeaderModel();
        addModel(fake_ai_headerModel);
    }
    public void resetHeaderInfo(List<String> hiList) {
        fake_ai_headerModel.bindData(hiList);
    }

    /**
     * 添加自己的问答
     *
     * @param data_all
     */
    public void addMyselfMessage(String data_all) {
        FakeAIItemTwoModel item_twoModel = new FakeAIItemTwoModel(data_all);
        addModel(item_twoModel);
        if (item_WaitModel == null) {
            item_WaitModel = new FakeAIWaitItemModel();
            addModel(item_WaitModel);
        }

    }

    /**
     * 添加服务端问题
     *
     * @param message
     */
    public void addServerMessage(String message) {
        if (item_WaitModel != null) {
        removeModel(item_WaitModel);
        item_WaitModel = null;
    }
        FakeAIItemOneModel model = new FakeAIItemOneModel();
        model.setData(message);
        addModel(model);


    }

}
