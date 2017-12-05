package com.hugboga.custom.models;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.airbnb.epoxy.EpoxyModelWithView;
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
    public void bind(ChatMessageVH holder) {
        super.bind(holder);
        holder.textView.setText(fakeStr);
    }

    class ChatMessageVH extends EpoxyHolder{

        @BindView(R.id.fake_item2_text)
        TextView textView;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
