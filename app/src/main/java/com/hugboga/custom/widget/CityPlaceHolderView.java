package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

/**
 * Created by Administrator on 2016/10/27.
 */
public class CityPlaceHolderView extends LinearLayout {

    public CityPlaceHolderView(Context context) {
        this(context,null);
    }

    TextView placeHolerView;

    public CityPlaceHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.city_place_viewholder,this);
        placeHolerView = (TextView)findViewById(R.id.place_holder_view);
    }

    public void hide(){
        this.setVisibility(View.GONE);
        placeHolerView.setVisibility(View.GONE);
    }

    public void show(){
        this.setVisibility(View.VISIBLE);
        placeHolerView.setVisibility(View.VISIBLE);
    }
}
