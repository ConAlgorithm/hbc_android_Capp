package com.hugboga.custom.models;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.FakeAIBean;

/**
 * Created by Administrator on 2017/12/2.
 */

public class FakeAIItemOneModel extends EpoxyModel<RelativeLayout> {
    private FakeAIBean fakeAIBean;

    @Override
    protected int getDefaultLayout() {
        return R.layout.fake_iem_one;
    }

    @Override
    public void bind(RelativeLayout view) {
        super.bind(view);
        TextView textView = (TextView) view.findViewById(R.id.fake_item1_text);
        textView.setText(fakeAIBean.DuoDuoSaid.questionValue);
    }
    public void setData(FakeAIBean fakeAIBean) {
        this.fakeAIBean = fakeAIBean;
    }
}
