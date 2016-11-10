package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hugboga.custom.R;

/**
 * Created by Administrator on 2016/10/26.
 */
public class CityHomeFooter extends FrameLayout {

    private View cityEmptyLayout;
    private View cityDescrView;

    private View otherEmptyLayout;

    public CityHomeFooter(Context context) {
        this(context,null);

    }

    public CityHomeFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.city_home_footer,this);
        cityEmptyLayout= this.findViewById(R.id.city_list_footer);
        cityDescrView = this.findViewById(R.id.footer_text_descr);
        otherEmptyLayout = this.findViewById(R.id.city_list_filter_footer);
        this.setVisibility(View.GONE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void hideFooter(){
        this.setVisibility(GONE);
        cityEmptyLayout.setVisibility(GONE);
        otherEmptyLayout.setVisibility(GONE);
    }

    public void showCityEmpty(boolean hasDescr){
        this.setVisibility(VISIBLE);
        cityEmptyLayout.setVisibility(View.VISIBLE);
        if(hasDescr){
            cityDescrView.setVisibility(View.VISIBLE);
        }else{
            cityDescrView.setVisibility(View.GONE);
        }
        otherEmptyLayout.setVisibility(View.GONE);
    }

    public void showOtherEmpty(){
        this.setVisibility(VISIBLE);
        otherEmptyLayout.setVisibility(View.VISIBLE);
        cityEmptyLayout.setVisibility(View.GONE);
    }
}
