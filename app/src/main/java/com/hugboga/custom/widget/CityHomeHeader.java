package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.activity.DailyWebInfoActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleNewActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.Bind;

import static com.hugboga.custom.R.id.cityHome_toolbar_custom_car;
import static com.hugboga.custom.R.id.cityHome_toolbar_home_pick_send_airport;
import static com.hugboga.custom.R.id.cityHome_toolbar_single_send;

/**
 * Created by wbj on 2016/10/17.
 */

public class CityHomeHeader extends LinearLayout implements HbcViewBehavior,View.OnClickListener{

    private TextView cityNameTV,guideAmountTV,goodsCount;     //城市名称，司导数量,货物数量
    private LinearLayout customCar,pickSendAir,singleSend,guideAvatarsLay,gooodsCountLay;      //定制包车，接送机，单次接送,司导头像,线路玩法数量
    private LinearLayout cityHomeFilterLay;
    private ImageView bgIV,searchIV;                                     //背景图片
    private RelativeLayout unlimitType,unlimitDays,unlimitTheme;

    @Bind(R.id.cityHome_filter_lay)
    CityHomeFilter cityHomeFilter;

    private int displayLayoutHeight;
    private CityHomeBean cityHomeBean;

    public CityHomeHeader(Context context) {
        this(context,null);
    }

    public CityHomeHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        inflate(context, R.layout.city_home_header,this);
        cityNameTV=(TextView)findViewById(R.id.city_home_header_city_name_tv);
        guideAmountTV=(TextView)findViewById(R.id.city_home_header_guides_count_tv);
        goodsCount=(TextView)findViewById(R.id.cityHome_header_play_count) ;

        customCar=(LinearLayout)findViewById(cityHome_toolbar_custom_car);
        pickSendAir=(LinearLayout)findViewById(cityHome_toolbar_home_pick_send_airport);
        singleSend=(LinearLayout)findViewById(cityHome_toolbar_single_send);
        guideAvatarsLay=(LinearLayout)findViewById(R.id.cityHome_header_guides_avatar_layout);
        gooodsCountLay=(LinearLayout)findViewById(R.id.cityHome_header_play_count_layout) ;
        cityHomeFilterLay=(LinearLayout)findViewById(R.id.cityHome_filter_lay);

        unlimitType=(RelativeLayout)findViewById(R.id.cityHome_unlimited_type_lay) ;
        unlimitDays=(RelativeLayout)findViewById(R.id.cityHome_unlimited_days_lay);
        unlimitTheme=(RelativeLayout)findViewById(R.id.cityHome_unlimited_theme_lay);

        bgIV=(ImageView)findViewById(R.id.city_home_header_bg_iv);
        searchIV=(ImageView)findViewById(R.id.cityHome_header_search_image);

        customCar.setOnClickListener(this);
        pickSendAir.setOnClickListener(this);
        singleSend.setOnClickListener(this);
        searchIV.setOnClickListener(this);
        unlimitType.setOnClickListener(this);
        unlimitDays.setOnClickListener(this);
        unlimitTheme.setOnClickListener(this);

        displayLayoutHeight=(int)((385 / 750.0) * UIUtils.getScreenWidth());
        findViewById(R.id.cityHome_header_cityname_layout).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, displayLayoutHeight));
    }

    @Override
    public void update(Object _data) {
        if (_data == null || !(_data instanceof CityHomeBean) || cityHomeBean != null) {
            return;
        }
        cityHomeBean=(CityHomeBean)_data;
        Tools.showImage(bgIV,cityHomeBean.cityContent.cityPicture);
        cityNameTV.setText(cityHomeBean.cityContent.cityName);
        guideAmountTV.setText(cityHomeBean.cityGuides.guideAmount+"位当地中文司导");

        //司导头像,引用
        if (cityHomeBean.cityGuides.guideAvatars != null && cityHomeBean.cityGuides.guideAvatars .size() > 0) {
            guideAvatarsLay.removeAllViews();
            int size = cityHomeBean.cityGuides.guideAvatars.size();
            int viewWidth = UIUtils.dip2px(30);
            j:for (int i = 0; i < size; i++) {
                viewWidth +=  UIUtils.dip2px(15) + UIUtils.dip2px(45);
                if (viewWidth > UIUtils.getScreenWidth()) {
                    break j;
                }
                CircleImageView circleImageView = new CircleImageView(getContext());
                circleImageView.setBackgroundResource(R.mipmap.journey_head_portrait);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2px(45), UIUtils.dip2px(45));
                params.rightMargin = UIUtils.dip2px(15);
                Tools.showImage(circleImageView, cityHomeBean.cityGuides.guideAvatars.get(i));
                guideAvatarsLay.addView(circleImageView, params);
            }
        }

        judgeServiceType(cityHomeBean);         //判断有没有三种服务类型，包车游，接送机，单次接送,线路有无
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
             case R.id.cityHome_toolbar_custom_car:
                 goDairy();
                 break;
            case R.id.cityHome_toolbar_home_pick_send_airport:
                goPickSend();
                break;
            case  R.id.cityHome_toolbar_single_send:
                goSingle();
                break;
            case R.id.cityHome_header_search_image:
                Intent intent = new Intent(this.getContext(), ChooseCityNewActivity.class);
                intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_RECOMMEND);
                intent.putExtra("isHomeIn",false);
                intent.putExtra("source","小搜索框");
                this.getContext().startActivity(intent);
                ((Activity)(this.getContext())).overridePendingTransition(R.anim.push_bottom_in,0);
                break;
            case R.id.cityHome_unlimited_type_lay:
                ImageView type=(ImageView)findViewById(R.id.city_home_unlimited_type);
                type.setImageResource(R.mipmap.share_unfold);
                findViewById(R.id.city_home_unlimited_type_tips).setVisibility(VISIBLE);
                break;
            case R.id.cityHome_unlimited_days_lay:
                ImageView type1=(ImageView)findViewById(R.id.city_home_unlimited_days);
                type1.setImageResource(R.mipmap.share_unfold);
                findViewById(R.id.city_home_unlimited_days_tips).setVisibility(VISIBLE);
                break;
            case R.id.cityHome_unlimited_theme_lay:
                ImageView type2=(ImageView)findViewById(R.id.city_home_unlimited_theme);
                type2.setImageResource(R.mipmap.share_unfold);
                findViewById(R.id.city_home_unlimited_theme_tips).setVisibility(VISIBLE);
                break;
        }

    }

    private void judgeServiceType(CityHomeBean cityHomeBean){
        if (!cityHomeBean.cityService.hasDailyservice()){
            customCar.setVisibility(View.GONE);
        }
        if (!cityHomeBean.cityService.hasAirporService()){
            customCar.setVisibility(View.GONE);
        }
        if (!cityHomeBean.cityService.hasSingleService()){
            customCar.setVisibility(View.GONE);
        }
        if (!cityHomeBean.cityService.hasDailyservice()&&!cityHomeBean.cityService.hasAirporService()&&!cityHomeBean.cityService.hasSingleService()){
            findViewById(R.id.cityHome_header_choose_layout).setVisibility(View.GONE);
        }
        if (cityHomeBean.goodsCount<=0){
            gooodsCountLay.setVisibility(View.GONE);
        }else {
            goodsCount.setText(cityHomeBean.goodsCount+"种包车游线路或玩法");
        }
    }

    private void goDairy(){
        Bundle bundle = new Bundle();
        HashMap<String,String> map = new HashMap<String,String>();
        bundle.putString("source","城市页");
        String userId = UserEntity.getUser().getUserId(this.getContext());
        String params = "";
        if(!TextUtils.isEmpty(userId)){
            params += "?userId=" + userId;
        }
        Intent intent = new Intent(this.getContext(), DailyWebInfoActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, "城市页");
        intent.putExtras(bundle);
        intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_DAIRY + params);
        this.getContext().startActivity(intent);
        map.put("source", "城市页");
        MobclickAgent.onEvent(getContext(), "chose_oneday", map);
    }

    /**
     * 以下代码copy自旧版本首页
     * */
    private void goPickSend(){
        Intent intent = new Intent(getContext(), PickSendActivity.class);
        intent.putExtra("source","城市页");
        getContext().startActivity(intent);
    }

    private void goSingle(){
        Intent intent = new Intent(getContext(),SingleNewActivity.class);
        intent.putExtra("source","城市页");
        getContext().startActivity(intent);
    }

    public int getDisplayLayoutHeight() {
        return displayLayoutHeight;
    }
}
