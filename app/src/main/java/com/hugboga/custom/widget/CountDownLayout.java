package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created on 16/9/7.
 */

public class CountDownLayout extends LinearLayout {

    @Bind(R.id.hour_l)
    CountDownItem hourL;
    @Bind(R.id.hour_r)
    CountDownItem hourR;
    @Bind(R.id.minute_l)
    CountDownItem minuteL;
    @Bind(R.id.minute_r)
    CountDownItem minuteR;
    @Bind(R.id.second_l)
    CountDownItem secondL;
    @Bind(R.id.second_r)
    CountDownItem secondR;

    public CountDownLayout(Context context) {
        this(context, null);
    }

    View view;

    public CountDownLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.countdown_layout, this);
        ButterKnife.bind(this, view);
    }

    private void getSecond(int secondAll){
        int second = (secondAll % 60);
        int l = second/10;
        int r = second%10;
        secondL.changeTime(l);
        secondR.changeTime(r);
    }

    private void getMinute(int secondAll){
        int minute = (secondAll % (60 * 60)) / 60;
        int l = minute/10;
        int r = minute%10;
        minuteL.changeTime(l);
        minuteR.changeTime(r);
    }

    private void getHour(int secondAll){
        int hour = (secondAll % (60 * 60 * 24)) / (60 * 60);
        int l = hour/10;
        int r = hour%10;
        hourL.changeTime(l);
        hourR.changeTime(r);
    }

    public void changeTime(int secondAll){
        getSecond(secondAll);
        getMinute(secondAll);
        getHour(secondAll);
    }
}

