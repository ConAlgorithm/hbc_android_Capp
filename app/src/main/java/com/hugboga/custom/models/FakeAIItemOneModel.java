package com.hugboga.custom.models;

import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FakeAIAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/2.
 */

public class FakeAIItemOneModel extends EpoxyModel<RelativeLayout> implements FakeAIAdapter.ChatItemInterface {

    @BindView(R.id.fake_item1_text)
    TextView fakeItem1Text;
    private String fakeAIBean;
    private boolean isPress = false;

    @Override
    protected int getDefaultLayout() {
        return R.layout.fake_iem_one;
    }

    @Override
    public void bind(RelativeLayout view) {
        super.bind(view);
        ButterKnife.bind(this, view);
        if (fakeAIBean != null) {
            fakeItem1Text.setText(fakeAIBean);
        }
        fakeItem1Text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isPress = true;
                        fakeItem1Text.setSelected(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        isPress = false;
                        fakeItem1Text.setSelected(false);
                        break;
                }
                return false;
            }
        });
    }

    public void setData(String fakeAIBean) {
        this.fakeAIBean = fakeAIBean;
    }

    @Override
    public void clearFocus() {
        if (fakeItem1Text != null && isPress) {
            fakeItem1Text.setSelected(false);
            isPress = false;
        }
    }
}
