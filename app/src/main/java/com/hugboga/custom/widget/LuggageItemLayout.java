package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.LuggageInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LuggageItemLayout extends LinearLayout {
    @BindView(R.id.luggage_num)
    TextView luggageNum;
    @BindView(R.id.luggage_icon)
    ImageView luggageIcon;

    public LuggageItemLayout(Context context) {
        this(context, null);
    }

    View view;

    public LuggageItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.luggage_max_item, this);
        ButterKnife.bind(this, view);
    }

    public void setText(String text){
        luggageNum.setText(text);
    }

    public void setText(int text){
        luggageNum.setText(text);
    }

    public void hide(){
        view.setVisibility(GONE);
    }

    public void show(){
        view.setVisibility(VISIBLE);
    }

    @OnClick(R.id.luggage_icon)
    public void onClick() {
        getContext().startActivity(new Intent(getContext(), LuggageInfoActivity.class));
    }
}
