package com.hugboga.custom.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.FakeAIBean;
import com.hugboga.custom.models.FakeAIHeaderModel;
import com.hugboga.custom.models.FakeAIItemOneModel;
import com.hugboga.custom.models.FakeAIItemTwoModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/29.
 */

public class FakeAIAdapter extends EpoxyAdapter {
    private FakeAIHeaderModel fake_ai_headerModel;
    final ArrayList<FakeAIBean> fakeAIBeans = new ArrayList<FakeAIBean>();

    public FakeAIAdapter() {
        fake_ai_headerModel = new FakeAIHeaderModel();
        fake_ai_headerModel.setData(null);
        addModel(fake_ai_headerModel);

    }

    public void setData_All(FakeAIBean data_all) {
        fake_ai_headerModel.setData(data_all);
        setData_ItemOne(data_all);
    }

    public void setData_ItemOne(FakeAIBean data_all) {
        FakeAIItemOneModel fake_ai_item_one = new FakeAIItemOneModel();
        fake_ai_item_one.setData(data_all);
        addModel(fake_ai_item_one);
        fake_ai_headerModel.imageAnimatorStop();
    }

    public void setData_ItemTwo(String data_all) {

        FakeAIItemTwoModel item_twoModel = new FakeAIItemTwoModel();
        item_twoModel.setData(data_all);
        addModel(item_twoModel);
        fake_ai_headerModel.imageAnimatorStart();
    }


}
