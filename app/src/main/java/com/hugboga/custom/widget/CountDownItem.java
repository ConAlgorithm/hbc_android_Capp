package com.hugboga.custom.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

/**
 * Created on 16/9/6.
 */

public class CountDownItem extends LinearLayout {
    public CountDownItem(Context context) {
        this(context,null);
    }

    View view;
    TextView timeView;
    public CountDownItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.countdown_item,this);
        timeView = (TextView)view.findViewById(R.id.time);
    }

    private int oldTime = 0;
    public void changeTime(int time){
        if(time >= 0 && time <=9) {
            if(time != oldTime) {
                oldTime = time;
//                int resId = getResources().getIdentifier("t" + time, "mipmap", this.getContext().getPackageName());
//                timeView.setImageResource(resId);
                timeView.setText("" + oldTime);
            }
        }
    }


}
