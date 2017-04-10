package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.CityHomeListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.utils.DatabaseManager;

/**
 * Created by Administrator on 2016/10/26.
 */
public class CityHomeFooter extends FrameLayout implements HbcViewBehavior{

    private View cityEmptyLayout;
    private View cityDescrView;

    private View otherEmptyLayout;

    private CityHomeBean cityHomeBean;

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
        findViewById(R.id.city_list_filter_footer_charter_tv).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CharterFirstStepActivity.class);
                if (cityHomeBean != null && cityHomeBean.cityContent != null) {
                    intent.putExtra(Constants.PARAMS_START_CITY_BEAN, DatabaseManager.getCityBean("" + cityHomeBean.cityContent.cityId));
                }
                if(v.getContext() instanceof CityHomeListActivity) {
                    intent.putExtra(Constants.PARAMS_SOURCE, ((CityHomeListActivity)v.getContext()).getEventSource());
                    intent.putExtra(Constants.PARAMS_SOURCE_DETAIL, ((CityHomeListActivity)v.getContext()).getIntentSource());
                }
                getContext().startActivity(intent);
            }
        });
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

    @Override
    public void update(Object _data) {
        if (_data == null || !(_data instanceof CityHomeBean) || cityHomeBean != null) {
            return;
        }
        cityHomeBean=(CityHomeBean)_data;
    }
}
