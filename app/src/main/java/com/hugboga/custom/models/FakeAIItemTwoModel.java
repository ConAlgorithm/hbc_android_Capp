package com.hugboga.custom.models;

import android.support.v7.widget.AppCompatTextView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.FakeAIActivity;
import com.hugboga.custom.adapter.FakeAIAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/12/2.
 */

public class FakeAIItemTwoModel extends EpoxyModel<RelativeLayout> implements FakeAIAdapter.ChatItemInterface {

    @BindView(R.id.fake_item2_text)
    AppCompatTextView textView;
    private String fakeStr; //内容
    private boolean isPress = false;

    @Override
    protected int getDefaultLayout() {
        return R.layout.fake_item_two;
    }

    @Override
    public void bind(RelativeLayout view) {
        super.bind(view);
        ButterKnife.bind(this, view);
        textView.setText(fakeStr);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isPress = true;
                        textView.setSelected(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        isPress = false;
                        textView.setSelected(false);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void clearFocus() {
        if (textView != null && isPress) {
            textView.setSelected(false);
            isPress = false;
        }
    }

    public FakeAIItemTwoModel(String fakeStr) {
        this.fakeStr = fakeStr;
    }

}
