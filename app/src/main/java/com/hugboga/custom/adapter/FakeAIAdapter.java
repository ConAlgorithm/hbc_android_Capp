package com.hugboga.custom.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.models.FakeAIHeaderModel;
import com.hugboga.custom.models.FakeAIItemOneModel;
import com.hugboga.custom.models.FakeAIItemTwoModel;

import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */

public class FakeAIAdapter extends EpoxyAdapter {

    private FakeAIHeaderModel fake_ai_headerModel;

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
    }

    /**
     * 添加服务端问题
     * @param message
     */
    public void addServerMessage(String message) {
        FakeAIItemOneModel model = new FakeAIItemOneModel();
        model.setData(message);
        addModel(model);
    }

}
