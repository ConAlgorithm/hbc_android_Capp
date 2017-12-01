package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/9/7.
 */

public class CountDownLayout extends LinearLayout {

    @BindView(R.id.hour_l)
    CountDownItem hourL;
    @BindView(R.id.hour_r)
    CountDownItem hourR;
    @BindView(R.id.minute_l)
    CountDownItem minuteL;
    @BindView(R.id.minute_r)
    CountDownItem minuteR;
    @BindView(R.id.second_l)
    CountDownItem secondL;
    @BindView(R.id.second_r)
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


    int ss = 1;
    int mi = ss * 60;
    int hh = mi * 60;

    int hour;
    int minute;
    int second;

    private void genTime(int secondAll){
        hour = secondAll / hh;
        minute = (secondAll - hour * hh) / mi;
        second = (secondAll - hour * hh - minute * mi) / ss;
    }

    private void getSecond(){
        int l = second/10;
        int r = second%10;
        secondL.changeTime(l);
        secondR.changeTime(r);
    }

    private void getMinute(){
        int l = minute/10;
        int r = minute%10;
        minuteL.changeTime(l);
        minuteR.changeTime(r);
    }

    private void getHour(){
        int l = hour/10;
        int r = hour%10;
        hourL.changeTime(l);
        hourR.changeTime(r);
    }

    public void changeTime(int secondAll){
        genTime(secondAll);
        getSecond();
        getMinute();
        getHour();
    }
}

