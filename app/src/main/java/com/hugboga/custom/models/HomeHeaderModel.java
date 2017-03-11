package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleNewActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.home.HomeSearchTabView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/9.
 */
public class HomeHeaderModel extends EpoxyModelWithHolder implements View.OnClickListener{

    HomeBeanV2.HomeHeaderInfo homeHeaderInfo;
    HomeSearchTabView.HomeTabClickListener homeTabClickListener;
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
        HomeHeaderHolder homeHeaderHolder = (HomeHeaderHolder) holder;
        homeHeaderHolder.headerImage.getLayoutParams().height = ScreenUtil.screenWidth * (810 - ScreenUtil.statusbarheight) / 750;
        Tools.showImage(homeHeaderHolder.headerImage, homeHeaderInfo.dynamicPic.videoUrl);

        setPlaceAmmountText(homeHeaderHolder.placeAmmout);
        setGuideAmmountText(homeHeaderHolder.gideAmmountText);

        homeHeaderHolder.chaterView.setOnClickListener(this);
        homeHeaderHolder.pickSendView.setOnClickListener(this);
        homeHeaderHolder.singleView.setOnClickListener(this);
        homeHeaderHolder.homeHelp.setOnClickListener(this);
        homeHeaderHolder.homeVideoPage.setOnClickListener(this);

        homeHeaderHolder.homeSearchTabView.setHomeTabClickListener(homeTabClickListener);
    }


    private void setPlaceAmmountText(TextView textView){
        String countryStr = homeHeaderInfo.countryNum+"+";
        String cityStr = homeHeaderInfo.cityNum+"+";
        String showStr = countryStr+"个国家  "+ cityStr+"个城市";
        SpannableString spannableString = new SpannableString(showStr);
        spannableString.setSpan(new ForegroundColorSpan(0xffFFC110),0,countryStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SuperscriptSpan(),countryStr.length()-1,countryStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new  AbsoluteSizeSpan(20),countryStr.length()-1,countryStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(0xffFFC110),countryStr.length()+5,countryStr.length()+cityStr.length()+5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new SuperscriptSpan(),countryStr.length()+cityStr.length()+4,countryStr.length()+cityStr.length()+5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new  AbsoluteSizeSpan(20),countryStr.length()+cityStr.length()+4,countryStr.length()+cityStr.length()+5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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
                //// TODO: 2017/3/11  打开意向单页面
                Toast.makeText(MyApplication.getAppContext(),"意向单页面",Toast.LENGTH_SHORT).show();
                break;
            case R.id.home_video_page:
                //// TODO: 2017/3/11  打开视频页面    homeHeaderInfo.headVideo.videoUrl
                Toast.makeText(MyApplication.getAppContext(),"视频页面",Toast.LENGTH_SHORT).show();
                break;
        }
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
