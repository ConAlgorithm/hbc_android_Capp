package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeHotDestination;
import com.hugboga.custom.utils.Tools;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/9/13.
 */

public class HomeHotDestinationItemView extends LinearLayout implements HbcViewBehavior{
    HomeHotDestination homeHotDestination;
    @Bind(R.id.hot_city_img)
    ImageView hotCityImg;
    @Bind(R.id.hotCityName)
    TextView hotCityName;
    @Bind(R.id.hotGuidesNum)
    TextView hotGuidesNum;
    public HomeHotDestinationItemView(Context context) {
        this(context,null);
    }

    public HomeHotDestinationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.home_hot_destination_item_view, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        homeHotDestination = (HomeHotDestination) _data;
        if(homeHotDestination == null){
            return;
        }
        Tools.showImage(hotCityImg,homeHotDestination.cityPicture,R.mipmap.empty_home_guide);
        hotCityName.setText(homeHotDestination.cityName);
        hotGuidesNum.setText(homeHotDestination.cityGuideAmount+getContext().getResources().getString(R.string.home_hot_destination_guide_num));

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CityListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                CityListActivity.Params params = new CityListActivity.Params();
                params.id = homeHotDestination.cityId;
                params.titleName = homeHotDestination.cityName;
                params.cityHomeType = CityListActivity.CityHomeType.CITY;
                intent.putExtra(Constants.PARAMS_DATA, params);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                getContext().startActivity(intent);
            }
        });
    }

    private String  getEventSource(){
        return "首页-热门目的地";
    }
}
