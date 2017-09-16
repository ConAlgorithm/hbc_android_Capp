package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCollectGuidesId;
import com.hugboga.custom.data.request.RequestUncollectGuidesId;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/2.
 */

public class FilterGuideItemView extends LinearLayout implements HbcViewBehavior ,HttpRequestListener {
    @Bind(R.id.filter_guide_avr)
    ImageView imageView;
    @Bind(R.id.save_guild)
    ImageView saveGuild;
    @Bind(R.id.save_guild_layout)
    LinearLayout save_guide_layout;
    @Bind(R.id.star)
    ImageView star;
    @Bind(R.id.evaluate)
    TextView evaluate;
    //@Bind(R.id.guide_des)
    //TextView guideDes;
    @Bind(R.id.filter_guide_name)
    TextView name;
    @Bind(R.id.filter_guide_location)
    TextView location;
    @Bind(R.id.yuding)
    TextView yuDing;
    Context context;
    public FilterGuideItemView(Context context) {
        this(context,null);
        this.context = context;
    }
    public FilterGuideItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.home_filter_guide_item, this);
        ButterKnife.bind(view);
    }
     FilterGuideBean filterGuideBean= null;
    @Override
    public void update(Object data) {
        filterGuideBean = (FilterGuideBean)data;
        Tools.showImageForHomePage(imageView, filterGuideBean.guideCover, R.mipmap.empty_home_guide);

        evaluate.setText(getResources().getString(R.string.home_guide_evaluate) +filterGuideBean.commentNum);
        SpannableString spannableString = new SpannableString(filterGuideBean.guideName + " " +filterGuideBean.getGuideDesc());
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_151515)), 0, filterGuideBean.guideName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, filterGuideBean.guideName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        name.setText(spannableString);

        if(!UserEntity.getUser().isLogin(getContext())){
            saveGuild.setSelected(false);
        }else if(filterGuideBean.isCollected == 1){
            saveGuild.setSelected(true);

        }else if(filterGuideBean.isCollected == 0){
            saveGuild.setSelected(false);
        }
        save_guide_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonUtils.isLogin(context,getEventSource())) {
                    save_guide_layout.setEnabled(false);
                    if(saveGuild.isSelected()){
                        filterGuideBean.isCollected = 0;
                        saveGuild.setSelected(false);
                        HttpRequestUtils.request(getContext(),new RequestUncollectGuidesId(getContext(), filterGuideBean.guideId),FilterGuideItemView.this,false);
                    }else{
                        //saveGuild.setSelected(true);
                        //data.isCollected= 1;
                        HttpRequestUtils.request(getContext(),new RequestCollectGuidesId(getContext(), filterGuideBean.guideId),FilterGuideItemView.this,false);
                    }
                }
            }
        });
        /*if (TextUtils.isEmpty(filterGuideBean.getGuideDesc())) {
            guideDes.setVisibility(View.GONE);
        } else {
            guideDes.setVisibility(View.VISIBLE);
            guideDes.setText(filterGuideBean.getGuideDesc());
        }*/

        double serviceStar = filterGuideBean.getServiceStar();

        /*if (serviceStar <= 0) {
            star.setText("暂无星级");
            star.setTextSize(12);
            star.setTextColor(0xFF929292);
        } else {
//            String services = serviceStar+"星";
//            SpannableString spannableString = new SpannableString(services);
//            spannableString.setSpan(new AbsoluteSizeSpan(30), services.length()-1, services.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.guildsaved)), services.length()-1, services.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            levelTV.setTextColor(0xffffc100);
//            levelTV.setTextSize(16);
//            levelTV.setText(spannableString);
            star.setText(filterGuideBean.getServiceStar() + "星");
        }*/


//        GuideItemUtils.setTag(tagGroup, data.skillLabelNames);
//
        boolean isShowCity = false;
        if (!TextUtils.isEmpty(filterGuideBean.cityName)) {
            isShowCity = true;
        }

        if (isShowCity) {
            location.setVisibility(View.VISIBLE);
            location.setText(filterGuideBean.cityName);
        } else {
            location.setVisibility(View.GONE);
        }
        yuDing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, filterGuideBean.orderUrl);
                context.startActivity(intent);
                SensorsUtils.onAppClick(getEventSource(),"选择心仪的司导服务","首页-选择心仪的司导服务");
            }
        });
//
//        String serviceType = data.getServiceType();
//        if (TextUtils.isEmpty(serviceType)) {
//            serviceTypeTV.setVisibility(View.GONE);
//        } else {
//            serviceTypeTV.setVisibility(View.VISIBLE);
//            serviceTypeTV.setText(serviceType);
//        }
//
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
                params.guideId = filterGuideBean.guideId;
                Intent intent = new Intent(getContext(), GuideWebDetailActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                intent.putExtra(Constants.PARAMS_SOURCE, "首页");
                getContext().startActivity(intent);
                SensorsUtils.onAppClick(getEventSource(),"选择心仪的司导服务","首页-选择心仪的司导服务");
            }
        });
    }

    public void setImageBound(int displayImgWidth, int displayImgHeight) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(displayImgWidth, displayImgHeight);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if(request instanceof RequestCollectGuidesId){
            saveGuild.setSelected(true);
            filterGuideBean.isCollected= 1;
            CommonUtils.showToast(getResources().getString(R.string.collect_succeed));
            setSensorsShareEvent(filterGuideBean.guideId);
        }else if(request instanceof RequestUncollectGuidesId){
            CommonUtils.showToast(getResources().getString(R.string.collect_cancel));
        }
        save_guide_layout.setEnabled(true);
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }
    private ErrorHandler errorHandler;
    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if(request instanceof RequestCollectGuidesId){
            saveGuild.setSelected(false);
            filterGuideBean.isCollected= 0;
            if (errorHandler == null) {
                errorHandler = new ErrorHandler((Activity) getContext(), this);
            }
            errorHandler.onDataRequestError(errorInfo, request);
        }
        save_guide_layout.setEnabled(true);
    }
    //收藏司导埋点
    public static void setSensorsShareEvent(String guideId) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("guideId", guideId);
            properties.put("favoriteType", "司导");
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("favorite", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getEventSource(){
        return "首页";
    }
}
