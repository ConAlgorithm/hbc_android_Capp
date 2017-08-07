package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.MediaPlayerActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleActivity;
import com.hugboga.custom.activity.TravelPurposeFormActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HomeActivitiesView;
import com.hugboga.custom.widget.home.HomeSearchTabView;
import com.nineoldandroids.view.ViewHelper;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/9.
 */
public class HomeHeaderModel extends EpoxyModelWithHolder implements View.OnClickListener {

    Context context;
    private Timer timer = new Timer(); //为了方便取消定时轮播，将 Timer 设为全局
    private static final int UPTATE_VIEWPAGER = 0;
    //设置当前 第几个图片 被选中
    private int autoCurrIndex = 0;

    HomeBeanV2.HomeHeaderInfo homeHeaderInfo;
    ArrayList<HomeBeanV2.ActivityPageSettingVo> activityPageSettings;
    HomeSearchTabView.HomeTabClickListener homeTabClickListener;

    private HomeSearchTabView tabView;
    private FrameLayout fastYudingLayout;
    private FrameLayout homeOtherService;
    private View animateServiceView;
    private View animateBaseLineView;
    private View animateServiceInnerView;

    public HomeHeaderModel(Context context, HomeBeanV2.HomeHeaderInfo homeHeaderInfo, ArrayList<HomeBeanV2.ActivityPageSettingVo> activityPageSettings, HomeSearchTabView.HomeTabClickListener homeTabClickListener) {
        this.context = context;
        this.homeHeaderInfo = homeHeaderInfo;
        this.activityPageSettings = activityPageSettings;
        this.homeTabClickListener = homeTabClickListener;

    }

    public void setHomeHeaderInfo(Context context, HomeBeanV2.HomeHeaderInfo homeHeaderInfo) {
        this.context = context;
        this.homeHeaderInfo = homeHeaderInfo;
    }

    public void setHomeActivityPageSetting(ArrayList<HomeBeanV2.ActivityPageSettingVo> activityPageSettings){
        this.activityPageSettings = activityPageSettings;
    }
    @Override
    protected HomeHeaderHolder createNewHolder() {
        return new HomeHeaderHolder();
    }

    HomeHeaderHolder homeHeaderHolder;

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        homeHeaderHolder = (HomeHeaderHolder) holder;
        init();
    }
    private void init(){

        if (activityPageSettings != null && activityPageSettings.size() == 0) {
            activityPageSettings = new ArrayList<HomeBeanV2.ActivityPageSettingVo>();
            HomeBeanV2.ActivityPageSettingVo activityPageSetting = new HomeBeanV2.ActivityPageSettingVo();
            activityPageSettings.add(activityPageSetting);

            homeHeaderHolder.activitiesView.update(activityPageSettings);
        }

        tabView = homeHeaderHolder.homeSearchTabView;
        fastYudingLayout = homeHeaderHolder.fastYudingLayout;
        setFastYudingWidth(fastYudingLayout);
        homeOtherService = homeHeaderHolder.homeOtherService;
    }

    private void setFastYudingWidth(FrameLayout fastYudingLayout){
        final ViewGroup.LayoutParams lp = fastYudingLayout.getLayoutParams();
        lp.width = (UIUtils.getScreenWidth() - UIUtils.dip2px(20))/2;
        fastYudingLayout.setLayoutParams(lp);
    }

    public void update() {
        if (homeHeaderHolder == null) {
            return;
        }

        homeHeaderHolder.activitiesView.update(activityPageSettings);

        if (homeHeaderInfo.dynamicPic != null) {
            //homeHeaderHolder.headerImage.getLayoutParams().height = ScreenUtil.screenWidth * (900 - ScreenUtil.statusbarheight) / 750;
            //Tools.showImageHasPlaceHolder(homeHeaderHolder.headerImage, homeHeaderInfo.dynamicPic.videoUrl,R.mipmap.home_banner);
            setPlaceAmmountText(homeHeaderHolder.placeAmmout);
            setGuideAmmountText(homeHeaderHolder.gideAmmountText);
            homeHeaderHolder.chaterView.setOnClickListener(this);
            homeHeaderHolder.pickSendView.setOnClickListener(this);
            homeHeaderHolder.singleView.setOnClickListener(this);
            homeHeaderHolder.homeHelp.setOnClickListener(this);
            homeHeaderHolder.homeVideoPage.setOnClickListener(this);
            homeHeaderHolder.huarenGuild.setOnClickListener(this);
        } else {
            //homeHeaderHolder.headerImage.getLayoutParams().height = ScreenUtil.screenWidth * (900 - ScreenUtil.statusbarheight) / 750;
        }

        homeHeaderHolder.homeSearchTabView.setHomeTabClickListener(homeTabClickListener);
        tabView = homeHeaderHolder.homeSearchTabView;
        fastYudingLayout = homeHeaderHolder.fastYudingLayout;
        fastYudingLayout.setOnClickListener(this);
        homeOtherService = homeHeaderHolder.homeOtherService;
        animateServiceView = homeHeaderHolder.serviceLayout;
        animateBaseLineView = homeHeaderHolder.homeHelp;
        animateServiceInnerView = homeHeaderHolder.serviceInnerLayout;
        //homeHeaderHolder.homeHeaderSearch.setOnClickListener(this);
    }

    public void locationTab(int index) {
        if (tabView != null) {
            tabView.tabIndex(index);
        }
    }

    public void animateServiceView(int destAnimationWidth) {
        if (animateServiceView == null) {
            return;
        }
        int viewMaxHeight = animateServiceView.getMeasuredHeight();
        int viewMinHeight = UIUtils.dip2px(36);
        float finalScaleY = viewMinHeight / (float) viewMaxHeight;

        int viewMaxWidth = animateServiceView.getMeasuredWidth();
        int viewMinWidth = destAnimationWidth;
        float finalScaleX = viewMinWidth / ((float) viewMaxWidth);

        int max = UIUtils.statusBarHeight + UIUtils.dip2px(48) + viewMaxHeight;
        int min = UIUtils.statusBarHeight + UIUtils.dip2px(42);

        int animateViewTop = getServiceViewTop() - UIUtils.dip2px(8);

        ViewHelper.setPivotX(animateServiceView, (float) (viewMaxWidth / 2));
        ViewHelper.setPivotY(animateServiceView, (float) (viewMaxHeight));
        if (animateViewTop > max) {
            ViewHelper.setScaleX(animateServiceView, 1.0f);
            ViewHelper.setScaleY(animateServiceView, 1.0f);
            ViewHelper.setAlpha(animateServiceInnerView, 1f);
            ViewHelper.setAlpha(animateServiceView, 1f);
        } else if (animateViewTop < min) {
            ViewHelper.setScaleX(animateServiceView, finalScaleX);
            ViewHelper.setScaleY(animateServiceView, finalScaleY);
            ViewHelper.setAlpha(animateServiceInnerView, 0f);
            ViewHelper.setAlpha(animateServiceView, 0f);

        } else {
            float scaleX = 1.0f - (1.0f - finalScaleX) * ((max - animateViewTop) / ((float) (max - min)));
            float scaleY = 1.0f - (1.0f - finalScaleY) * ((max - animateViewTop) / ((float) (max - min)));
            ViewHelper.setScaleX(animateServiceView, scaleX);
            ViewHelper.setScaleY(animateServiceView, scaleY);
            float alphaInner = 1.0f - 2.5f * ((max - animateViewTop) / ((float) (max - min)));
            float alphaOuter = 1.0f - 0.4f * ((max - animateViewTop) / ((float) (max - min)));
            if (alphaInner < 0f) {
                alphaInner = 0;
            }
            ViewHelper.setAlpha(animateServiceInnerView, alphaInner);
            ViewHelper.setAlpha(animateServiceView, alphaOuter);
            //Log.i("anim","scaleX:" + scaleX + " scaleY:" + scaleY);
        }
    }

    private int getServiceViewTop() {
        if (animateBaseLineView != null) {
            return UIUtils.getViewTop(animateBaseLineView);
        }
        return 0;
    }

    public int getTabViewTop() {
        if (tabView != null) {
            return UIUtils.getViewTop(tabView);
        }
        return 0;
    }

    public int getOtherServiceViewTop() {
        if (homeOtherService != null) {
            return UIUtils.getViewTop(homeOtherService);
        }
        return 0;
    }
    public int getOtherServiceViewHeight() {
        if (homeOtherService != null) {
            return homeOtherService.getHeight();
        }
        return 0;
    }
    public int getFastYudingViewTop() {
        if (fastYudingLayout != null) {
            return UIUtils.getViewTop(fastYudingLayout);
        }
        return 0;
    }

    public int getServiceLayout(){
        if(homeHeaderHolder.serviceLayout != null){
            return homeHeaderHolder.serviceLayout.getHeight();
        }
        return 0;
    }
    private void setPlaceAmmountText(TextView textView) {
        String countryStr = homeHeaderInfo.countryNum + "+";
        String cityStr = homeHeaderInfo.cityNum + "+";
        String firstString = "开车带你玩遍" + countryStr;
        String country = "国家、";
        String city = "城市";
        String showStr = firstString + country + cityStr + city;
        int cityNumber = firstString.length() + country.length() + cityStr.length();
        SpannableString spannableString = new SpannableString(showStr);
        //spannableString.setSpan(new ForegroundColorSpan(0xffffff),0,countryStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SuperscriptSpan(), firstString.length() - 1, firstString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(25), firstString.length() - 1, firstString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //spannableString.setSpan(new ForegroundColorSpan(0xffFFC110),countryStr.length()+5,countryStr.length()+cityStr.length()+5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SuperscriptSpan(), cityNumber - 1, cityNumber, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(25), cityNumber - 1, cityNumber, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
    }

    private void setGuideAmmountText(TextView textView) {
        String showStr = homeHeaderInfo.guideNum + "万" + homeHeaderInfo.guideDesc;
        //SpannableString spannableString = new SpannableString(showStr);
        //spannableString.setSpan(new ForegroundColorSpan(0xffFFC110),0,guideAmmountStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(showStr);
        textView.setShadowLayer(0.5f, 0.5f, 0.5f, 0x9a000b34);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_header_view_new;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_chater:
                intentActivity(v.getContext(), CharterFirstStepActivity.class, StatisticConstant.LAUNCH_DETAIL_R);
                break;
            case R.id.home_picksend_layout:
                intentActivity(v.getContext(), PickSendActivity.class, StatisticConstant.LAUNCH_J);
                break;
            case R.id.home_single_layout:
                intentActivity(v.getContext(), SingleActivity.class, StatisticConstant.LAUNCH_C);
                break;
            case R.id.home_help_layout:
                /*if (CommonUtils.isLogin(v.getContext())) {
                    gotoTravelPurposeForm(v);
                }*/
                Intent intentProPre = null;
                intentProPre = new Intent(context, TravelPurposeFormActivity.class);
                intentProPre.putExtra(Constants.PARAMS_SOURCE,getEventSource());
                context.startActivity(intentProPre);
                MobClickUtils.onEvent(StatisticConstant.YI_XIANG);
                break;
            case R.id.home_video_page:
                if (homeHeaderInfo == null || homeHeaderInfo.headVideo == null) {
                    break;
                }
                Intent intent = null;
                if (homeHeaderInfo.headVideo.type == 1) {
                    intent = new Intent(v.getContext(), MediaPlayerActivity.class);
                    intent.putExtra(MediaPlayerActivity.KEY_URL, homeHeaderInfo.headVideo.videoUrl);
                    v.getContext().startActivity(intent);
                    MobClickUtils.onEvent(StatisticConstant.PLAY_VIDEO);
                    SensorsUtils.track("play_video");
                } else {
                    intent = new Intent(v.getContext(), WebInfoActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, homeHeaderInfo.headVideo.videoUrl);
                    v.getContext().startActivity(intent);
                }
                break;
            /*case R.id.home_header_search:
                Intent intentSearch = new Intent(v.getContext(), ChooseCityNewActivity.class);
                intentSearch.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
                intentSearch.putExtra("isHomeIn", true);
                intentSearch.putExtra("source", "首页搜索框");
                v.getContext().startActivity(intentSearch);
                StatisticClickEvent.click(StatisticConstant.SEARCH_LAUNCH, "首页");
                break;*/
            case R.id.huaren_guild_layout:
                intent = new Intent(v.getContext(), WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, homeHeaderInfo.guideIntroUrl);
                v.getContext().startActivity(intent);
                break;
        }
    }

    static class HomeHeaderHolder extends EpoxyHolder {

        View itemView;
        @Bind(R.id.home_header_image)
        ViewPager mViewPager;
        @Bind(R.id.indicator)
        CirclePageIndicator mIndicator;
        @Bind(R.id.place_ammount_text)
        TextView placeAmmout;
        @Bind(R.id.guide_ammount_text)
        TextView gideAmmountText;
        @Bind(R.id.home_video_page)
        View homeVideoPage;
        @Bind(R.id.home_chater)
        View chaterView; //包车游
        @Bind(R.id.home_picksend_layout)
        View pickSendView; //接送机
        @Bind(R.id.home_single_layout)
        View singleView; //单次接送
        @Bind(R.id.home_help_layout)
        View homeHelp;
        @Bind(R.id.home_header_tab_layout)
        HomeSearchTabView homeSearchTabView;
        @Bind(R.id.home_service_layout)
        View serviceLayout;
        @Bind(R.id.home_search_service_inner_layout)
        View serviceInnerLayout;
        @Bind(R.id.huaren_guild_layout)
        View huarenGuild;
        @Bind(R.id.home_other_service)
        FrameLayout homeOtherService;
        @Bind(R.id.fast_yuding_layout)
        FrameLayout fastYudingLayout;
        /*@Bind(R.id.home_header_search)
        View homeHeaderSearch;//首页搜索*/
        @Bind(R.id.home_activities_view)
        HomeActivitiesView activitiesView;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    private void intentActivity(Context context, Class<?> cls, String eventId) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        context.startActivity(intent);
        if (!TextUtils.isEmpty(eventId)) {
            MobClickUtils.onEvent(eventId);
        }
    }

    public String getEventSource() {
        return "首页";
    }
}
