package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.MediaPlayerActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleNewActivity;
import com.hugboga.custom.activity.TravelPurposeFormActivity;
import com.hugboga.custom.activity.TravelPurposeFormListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestHasCreatedTravelForm;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.home.HomeSearchTabView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.nineoldandroids.view.ViewHelper;

import org.xutils.http.annotation.HttpRequest;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/9.
 */
public class HomeHeaderModel extends EpoxyModelWithHolder implements View.OnClickListener{

    HomeBeanV2.HomeHeaderInfo homeHeaderInfo;
    HomeSearchTabView.HomeTabClickListener homeTabClickListener;

    private HomeSearchTabView tabView;
    private View animateServiceView;
    private View animateBaseLineView;
    private View animateServiceInnerView;

    public HomeHeaderModel(HomeBeanV2.HomeHeaderInfo homeHeaderInfo,HomeSearchTabView.HomeTabClickListener homeTabClickListener) {
        this.homeHeaderInfo = homeHeaderInfo;
        this.homeTabClickListener = homeTabClickListener;
    }

    @Override
    protected HomeHeaderHolder createNewHolder() {
        return new HomeHeaderHolder();
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        if(holder==null){
            return;
        }
        HomeHeaderHolder homeHeaderHolder = (HomeHeaderHolder) holder;
        if(homeHeaderInfo.dynamicPic!=null){
            homeHeaderHolder.headerImage.getLayoutParams().height = ScreenUtil.screenWidth * (810 - ScreenUtil.statusbarheight) / 750;
            Tools.showImage(homeHeaderHolder.headerImage, homeHeaderInfo.dynamicPic.videoUrl);
            setPlaceAmmountText(homeHeaderHolder.placeAmmout);
            setGuideAmmountText(homeHeaderHolder.gideAmmountText);
            homeHeaderHolder.chaterView.setOnClickListener(this);
            homeHeaderHolder.pickSendView.setOnClickListener(this);
            homeHeaderHolder.singleView.setOnClickListener(this);
            homeHeaderHolder.homeHelp.setOnClickListener(this);
            homeHeaderHolder.homeVideoPage.setOnClickListener(this);
        }else{
            homeHeaderHolder.headerImage.getLayoutParams().height = ScreenUtil.screenWidth * (810 - ScreenUtil.statusbarheight) / 750;
        }


        homeHeaderHolder.homeSearchTabView.setHomeTabClickListener(homeTabClickListener);
        tabView = homeHeaderHolder.homeSearchTabView;
        animateServiceView = homeHeaderHolder.serviceLayout;
        animateBaseLineView = homeHeaderHolder.homeHelp;
        animateServiceInnerView = homeHeaderHolder.serviceInnerLayout;
    }

    public void locationTab(int index){
        if(tabView!=null){
            tabView.tabIndex(index);
        }
    }

    public void animateServiceView(int destAnimationWidth){
        if(animateServiceView==null){
            return;
        }
        int viewMaxHeight = animateServiceView.getMeasuredHeight();
        int viewMinHeight = UIUtils.dip2px(36);
        float finalScaleY = viewMinHeight/(float)viewMaxHeight;

        int viewMaxWidth = animateServiceView.getMeasuredWidth();
        int viewMinWidth = destAnimationWidth;
        float finalScaleX = viewMinWidth/((float)viewMaxWidth);

        int max = UIUtils.statusBarHeight + UIUtils.dip2px(48) + viewMaxHeight;
        int min = UIUtils.statusBarHeight + UIUtils.dip2px(42);

        int animateViewTop = getServiceViewTop() - UIUtils.dip2px(12)/*- viewMaxHeight - UIUtils.dip2px(12)*/;

        ViewHelper.setPivotX(animateServiceView,(float)(viewMaxWidth/2));
        ViewHelper.setPivotY(animateServiceView,(float)(viewMaxHeight));
        if(animateViewTop>max){
            ViewHelper.setScaleX(animateServiceView,1.0f);
            ViewHelper.setScaleY(animateServiceView,1.0f);
            ViewHelper.setAlpha(animateServiceInnerView,1f);
            ViewHelper.setAlpha(animateServiceView,1f);
        }else if(animateViewTop<min){
            ViewHelper.setScaleX(animateServiceView,finalScaleX);
            ViewHelper.setScaleY(animateServiceView,finalScaleY);
            ViewHelper.setAlpha(animateServiceInnerView,0f);
            ViewHelper.setAlpha(animateServiceView,0f);
        }else{
            float scaleX = 1.0f-(1.0f-finalScaleX)*((max-animateViewTop)/((float)(max-min)));
            float scaleY = 1.0f-(1.0f-finalScaleY)*((max-animateViewTop)/((float)(max-min)));
            ViewHelper.setScaleX(animateServiceView,scaleX);
            ViewHelper.setScaleY(animateServiceView,scaleY);
            float alphaInner = 1.0f-2.5f*((max-animateViewTop)/((float)(max-min)));
            float alphaOuter = 1.0f-0.4f*((max-animateViewTop)/((float)(max-min)));
            if(alphaInner<0f){
                alphaInner = 0;
            }
            ViewHelper.setAlpha(animateServiceInnerView,alphaInner);
            ViewHelper.setAlpha(animateServiceView,alphaOuter);
            //Log.i("anim","scaleX:" + scaleX + " scaleY:" + scaleY);
        }
        //Log.i("anim","max:" + max + " min:" + min+" meWidth:"+ viewMaxWidth+ " animateViewTop:"+animateViewTop+ " finalYScale:" + finalScaleY);
    }

    private int getServiceViewTop(){
        if(animateBaseLineView!=null){
            return UIUtils.getViewTop(animateBaseLineView);
        }
        return 0;
    }

    public int getTabViewTop(){
        if(tabView!=null){
            return  UIUtils.getViewTop(tabView);
        }
        return 0;
    }



    private void setPlaceAmmountText(TextView textView){
        String countryStr = homeHeaderInfo.countryNum+"+";
        String cityStr = homeHeaderInfo.cityNum+"+";
        String showStr = countryStr+"个国家  "+ cityStr+"个城市";
        SpannableString spannableString = new SpannableString(showStr);
        spannableString.setSpan(new ForegroundColorSpan(0xffFFC110),0,countryStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SuperscriptSpan(),countryStr.length()-1,countryStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new  AbsoluteSizeSpan(25),countryStr.length()-1,countryStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(0xffFFC110),countryStr.length()+5,countryStr.length()+cityStr.length()+5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SuperscriptSpan(),countryStr.length()+cityStr.length()+4,countryStr.length()+cityStr.length()+5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new  AbsoluteSizeSpan(25),countryStr.length()+cityStr.length()+4,countryStr.length()+cityStr.length()+5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
    }

    private void setGuideAmmountText(TextView textView){
        String guideAmmountStr = homeHeaderInfo.guideNum+"万";
        String showStr = guideAmmountStr+"认证华人司导";
        SpannableString spannableString = new SpannableString(showStr);
        spannableString.setSpan(new ForegroundColorSpan(0xffFFC110),0,guideAmmountStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_header_view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_chater:
                intentActivity(v.getContext(),CharterFirstStepActivity.class, StatisticConstant.LAUNCH_DETAIL_R);
                break;
            case R.id.home_picksend_layout:
                intentActivity(v.getContext(),PickSendActivity.class, StatisticConstant.LAUNCH_J);
                break;
            case R.id.home_single_layout:
                intentActivity(v.getContext(),SingleNewActivity.class, StatisticConstant.LAUNCH_C);
                break;
            case R.id.home_help:
                if (CommonUtils.isLogin(v.getContext())){
                    gotoTravelPurposeForm(v);
                }
                break;
            case R.id.home_video_page:
                Intent intent = new Intent(v.getContext(), MediaPlayerActivity.class);
                intent.putExtra(MediaPlayerActivity.KEY_URL,homeHeaderInfo.headVideo.videoUrl);
                v.getContext().startActivity(intent);
                break;
        }
    }

    private void gotoTravelPurposeForm(final View view){
        RequestHasCreatedTravelForm requestHasCreate = new RequestHasCreatedTravelForm(view.getContext(), UserEntity.getUser().getUserId(view.getContext()));
        HttpRequestUtils.request(view.getContext(), requestHasCreate, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                Intent intent = null;
                RequestHasCreatedTravelForm.HasWork hasWork = ((RequestHasCreatedTravelForm) request).getData();
                if (hasWork.getHasWorkorder()){
                    intent = new Intent(view.getContext(), TravelPurposeFormListActivity.class);
                }else {
                    intent = new Intent(view.getContext(),TravelPurposeFormActivity.class);
                }
                view.getContext().startActivity(intent);
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {
            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            }
        }, false);
    }

    static class HomeHeaderHolder extends EpoxyHolder {

        View itemView;
        @Bind(R.id.home_header_image)
        ImageView headerImage;
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
        @Bind(R.id.home_help)
        View homeHelp;
        @Bind(R.id.home_header_tab_layout)
        HomeSearchTabView homeSearchTabView;
        @Bind(R.id.home_service_layout)
        View serviceLayout;
        @Bind(R.id.home_search_service_inner_layout)
        View serviceInnerLayout;

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
