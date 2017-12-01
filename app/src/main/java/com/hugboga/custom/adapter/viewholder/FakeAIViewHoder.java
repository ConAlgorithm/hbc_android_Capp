package com.hugboga.custom.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FakeAIAdapter;
import com.hugboga.custom.adapter.FlightAdapter;

/**
 * Created by Administrator on 2017/11/29.
 */

public class FakeAIViewHoder extends RecyclerView.ViewHolder{

    public  TextView  textView;
    public FakeAIViewHoder(View itemView, int type) {
        super(itemView);
        switch (type){
            case FakeAIAdapter.FAKEADAPTERTYPE_ONE:
                textView = (TextView) itemView.findViewById(R.id.fake_item1_text);
                break;
            case  FakeAIAdapter.FAKEADAPTERTYPE_TWO:
                textView = (TextView) itemView.findViewById(R.id.fake_item2_text);
                break;
        }

    }
}
