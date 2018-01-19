package com.hugboga.custom.models;

import android.support.v7.widget.AppCompatTextView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/12/2.
 */

public class FakeAIItemTwoModel extends EpoxyModelWithHolder<FakeAIItemTwoModel.ChatMessageVH> {

    String fakeStr; //内容


    public FakeAIItemTwoModel(String fakeStr) {
        this.fakeStr = fakeStr;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.fake_item_two;
    }

    @Override
    protected ChatMessageVH createNewHolder() {
        return new ChatMessageVH();
    }

    @Override
    public void bind(final ChatMessageVH holder) {
        super.bind(holder);
        holder.textView.setText(fakeStr);
        holder.textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.textView.setSelected(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        holder.textView.setSelected(false);
                        break;
                }
                return false;
            }
        });
    }

    class ChatMessageVH extends EpoxyHolder {

        @BindView(R.id.fake_item2_text)
        AppCompatTextView textView;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
