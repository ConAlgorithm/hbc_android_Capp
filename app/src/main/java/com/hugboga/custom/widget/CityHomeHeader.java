package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hugboga.custom.MyApplication;
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
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

import static com.hugboga.custom.R.id.cityHome_toolbar_custom_car;
import static com.hugboga.custom.R.id.cityHome_toolbar_home_pick_send_airport;
import static com.hugboga.custom.R.id.cityHome_toolbar_single_send;
import static com.hugboga.custom.R.id.swipe;

/**
 * Created by wbj on 2016/10/17.
 */

public class CityHomeHeader extends LinearLayout implements HbcViewBehavior,View.OnClickListener{


    private TextView cityNameTV,guideAmountTV,goodsCount;     //城市名称，司导数量,货物数量
    private LinearLayout customCar,pickSendAir,singleSend,guideAvatarsLay,gooodsCountLay;      //定制包车，接送机，单次接送,司导头像,线路玩法数量
    private ImageView bgIV;                                     //背景图片
    private FrameLayout unlimitType,unlimitDays,unlimitTheme;


    FrameLayout cityHomeFilterTab;

    LinearLayout cityHeaderContentLayout;

    private int displayLayoutHeight;
    private CityHomeBean cityHomeBean;

    public CityHomeHeader(Context context) {
        this(context,null);
    }

    public CityHomeHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        inflate(context, R.layout.city_home_header,this);

        cityHeaderContentLayout = (LinearLayout)this.findViewById(R.id.cityHome_header_content_layout);
        cityHomeFilterTab = (FrameLayout) this.findViewById(R.id.city_home_header_filter_tab_layout);

        cityNameTV=(TextView)findViewById(R.id.city_home_header_city_name_tv);
        guideAmountTV=(TextView)findViewById(R.id.city_home_header_guides_count_tv);
        goodsCount=(TextView)findViewById(R.id.cityHome_header_play_count) ;

        customCar=(LinearLayout)findViewById(cityHome_toolbar_custom_car);
        pickSendAir=(LinearLayout)findViewById(cityHome_toolbar_home_pick_send_airport);
        singleSend=(LinearLayout)findViewById(cityHome_toolbar_single_send);
        guideAvatarsLay=(LinearLayout)findViewById(R.id.cityHome_header_guides_avatar_layout);
        gooodsCountLay=(LinearLayout)findViewById(R.id.cityHome_header_play_count_layout) ;

        unlimitType=(FrameLayout)findViewById(R.id.cityHome_unlimited_type_lay) ;
        unlimitDays=(FrameLayout)findViewById(R.id.cityHome_unlimited_days_lay);
        unlimitTheme=(FrameLayout)findViewById(R.id.cityHome_unlimited_theme_lay);

        bgIV=(ImageView)findViewById(R.id.city_home_header_bg_iv);

        customCar.setOnClickListener(this);
        pickSendAir.setOnClickListener(this);
        singleSend.setOnClickListener(this);
        unlimitType.setOnClickListener(this);
        unlimitDays.setOnClickListener(this);
        unlimitTheme.setOnClickListener(this);

        displayLayoutHeight=(int)((385 / 750.0) * UIUtils.getScreenWidth());
        findViewById(R.id.city_home_header_bg_iv).setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, displayLayoutHeight));
    }

    public void setFilterTypeTabValue(String value){
        if(unlimitType!=null){
            ((TextView)unlimitType.getChildAt(0)).setText(value);
        }
    }

    public void setFilterDayTabValue(String value){
        if(unlimitDays!=null){
            ((TextView)unlimitDays.getChildAt(0)).setText(value);
        }
    }

    public void setFilterThemeTabValue(String value){
        if(unlimitTheme!=null){
            ((TextView)unlimitTheme.getChildAt(0)).setText(value);
        }
    }


    public void hideHeaderContent(){
        if(cityHeaderContentLayout!=null){
            cityHeaderContentLayout.setVisibility(View.GONE);
        }
    }

    public int getFilterTabTop(){
        if(cityHomeFilterTab!=null){
            int[] location = new int[2];
            cityHomeFilterTab.getLocationOnScreen(location);
            return location[1];
        }
        return 0;
    }

    @Override
    public void update(Object _data) {
        if (_data == null || !(_data instanceof CityHomeBean) || cityHomeBean != null) {
            return;
        }
        cityHomeBean=(CityHomeBean)_data;
        if(cityHomeBean.cityContent!=null){
            if(cityHomeBean.cityContent.cityPicture!=null){
                Tools.showImage(bgIV,cityHomeBean.cityContent.cityPicture);
            }
            if(cityHomeBean.cityContent.cityName!=null){
                cityNameTV.setText(cityHomeBean.cityContent.cityName);
            }
        }
        if(cityHomeBean.cityGuides!=null){
            guideAmountTV.setText(cityHomeBean.cityGuides.guideAmount+"位当地中文司导");
        }
        //司导头像,引用
        if (cityHomeBean.cityGuides!=null && cityHomeBean.cityGuides.guideAvatars != null && cityHomeBean.cityGuides.guideAvatars .size() > 0) {
            guideAvatarsLay.removeAllViews();
            int size = cityHomeBean.cityGuides.guideAvatars.size();
            int viewWidth = UIUtils.dip2px(30);
            j:for (int i = 0; i < size && i<6; i++) {
                viewWidth +=  UIUtils.dip2px(10) + UIUtils.dip2px(38);
                if (viewWidth > UIUtils.getScreenWidth()) {
                    break j;
                }

                CircleImageView circleImageView = new CircleImageView(getContext());
                int paddingvalue = UIUtils.dip2px(1);
                circleImageView.setPadding(paddingvalue,paddingvalue,paddingvalue,paddingvalue);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2px(38), UIUtils.dip2px(38));
                params.rightMargin = UIUtils.dip2px(10);
                if(cityHomeBean.cityGuides.guideAmount>5 && i==5){
                    circleImageView.setBackgroundResource(R.mipmap.city_filter_more_bg);
                }else{
                    circleImageView.setBackgroundResource(R.mipmap.journey_head_portrait);
                    Tools.showImage(circleImageView, cityHomeBean.cityGuides.guideAvatars.get(i));
                }
                if (circleImageView==null){
                    guideAvatarsLay.removeView(circleImageView);
                    continue ;
                }
                guideAvatarsLay.addView(circleImageView, params);
            }
            if(cityHomeBean.cityGuides.guideAmount>5){
                CircleImageView circleImageView = new CircleImageView(getContext());
                int paddingvalue = UIUtils.dip2px(1);
                circleImageView.setPadding(paddingvalue,paddingvalue,paddingvalue,paddingvalue);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2px(38), UIUtils.dip2px(38));
                params.rightMargin = UIUtils.dip2px(10);
                circleImageView.setBackgroundResource(R.mipmap.city_filter_more_bg);
                guideAvatarsLay.addView(circleImageView, params);
            }
        }else {
            guideAvatarsLay.setVisibility(View.GONE);
        }
        judgeServiceType(cityHomeBean);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
             case R.id.cityHome_toolbar_custom_car:
                 goDairy();
                 StatisticClickEvent.click(StatisticConstant.LAUNCH_DETAIL_R, "城市页");
                 break;
            case R.id.cityHome_toolbar_home_pick_send_airport:
                goPickSend();
                StatisticClickEvent.click(StatisticConstant.LAUNCH_J, "城市页");
                break;
            case  R.id.cityHome_toolbar_single_send:
                goSingle();
                StatisticClickEvent.click(StatisticConstant.LAUNCH_C, "城市页");
                break;
            case R.id.cityHome_unlimited_type_lay:
                if(headerTabClickListener!=null){
                    headerTabClickListener.headerTabClick(0);
                    Map<String,String> map=new HashMap<>();
                    map.put("screentype","类型");
                    MobClickUtils.onEvent(StatisticConstant.GSCREEN_TRIGGER,map);
                }
                break;
            case R.id.cityHome_unlimited_days_lay:
                if(headerTabClickListener!=null){
                    headerTabClickListener.headerTabClick(1);
                    Map<String,String> map=new HashMap<>();
                    map.put("screentype","天数");
                    MobClickUtils.onEvent(StatisticConstant.GSCREEN_TRIGGER,map);
                }
                break;
            case R.id.cityHome_unlimited_theme_lay:
                if(headerTabClickListener!=null){
                    headerTabClickListener.headerTabClick(2);
                    Map<String,String> map=new HashMap<>();
                    map.put("screentype","主题");
                    MobClickUtils.onEvent(StatisticConstant.GSCREEN_TRIGGER,map);
                }
                break;
        }

    }

    private void judgeServiceType(CityHomeBean cityHomeBean){
        if(cityHomeBean.cityService!=null){
            if (!cityHomeBean.cityService.hasDailyservice()){
                customCar.setVisibility(View.GONE);
            }
            if (!cityHomeBean.cityService.hasAirporService()){
                pickSendAir.setVisibility(View.GONE);
            }
            if (!cityHomeBean.cityService.hasSingleService()){
                singleSend.setVisibility(View.GONE);
            }
            if (!cityHomeBean.cityService.hasDailyservice()&&!cityHomeBean.cityService.hasAirporService()&&!cityHomeBean.cityService.hasSingleService()){
                findViewById(R.id.cityHome_header_choose_layout).setVisibility(View.GONE);
            }
        }
        if (cityHomeBean.goodsCount<=0){
            gooodsCountLay.setVisibility(View.GONE);
            cityHomeFilterTab.setVisibility(View.GONE);
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
        intent.putExtra("goodtype","包车");
        this.getContext().startActivity(intent);
        map.put("source", "城市页");
        MobclickAgent.onEvent(getContext(), "chose_oneday", map);
    }

    /**
     * 以下代码copy自旧版本首页
     * */
    private void goPickSend(){
        Intent intent = new Intent(getContext(), PickSendActivity.class);
        intent.putExtra("source","首页");
        getContext().startActivity(intent);
    }

    private void goSingle(){
        Intent intent = new Intent(getContext(),SingleNewActivity.class);
        intent.putExtra("source","首页");
        getContext().startActivity(intent);
    }

    public int getDisplayLayoutHeight() {
        return displayLayoutHeight;
    }


    private HeaderTabClickListener headerTabClickListener;

    public void setHeaderTabClickListener(HeaderTabClickListener headerTabClickListener) {
        this.headerTabClickListener = headerTabClickListener;
    }

    public interface HeaderTabClickListener{
        void headerTabClick(int position);
    }
}
