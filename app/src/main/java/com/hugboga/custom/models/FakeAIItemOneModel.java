package com.hugboga.custom.models;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;

/**
 * Created by Administrator on 2017/12/2.
 */

public class FakeAIItemOneModel extends EpoxyModel<RelativeLayout> {

    private String fakeAIBean;

    @Override
    protected int getDefaultLayout() {
        return R.layout.fake_iem_one;
    }

    @Override
    public void bind(RelativeLayout view) {
        super.bind(view);
        TextView textView = (TextView) view.findViewById(R.id.fake_item1_text);
        textView.setText(fakeAIBean);
    }

    public void setData(String fakeAIBean) {
        this.fakeAIBean = fakeAIBean;
    }
}
