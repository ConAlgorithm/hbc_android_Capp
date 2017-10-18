package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
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
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeCityItemVo;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestCollectLineNo;
import com.hugboga.custom.data.request.RequestUncollectLinesNo;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import net.grobas.view.PolygonImageView;

import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/29.
 */

public class HomeRecommentCityItemView extends LinearLayout implements HbcViewBehavior,HttpRequestListener{
    String cityNameString;
    @Bind(R.id.pager_img)
    ImageView imageView;
    @Bind(R.id.des1)
    TextView des1;
    @Bind(R.id.cityName)
    TextView cityName;
    @Bind(R.id.guidesNum)
    TextView guidesNum;
    @Bind(R.id.des2)
    TextView des2;
    //@Bind(R.id.tiyan)
    TextView tiyan;
    @Bind(R.id.perPrice)
    TextView perPrice;
    @Bind(R.id.avr)
    PolygonImageView polygonImageView;
    @Bind(R.id.content_layout)
    LinearLayout contentLayout;
    @Bind(R.id.save_guild_layout)
    LinearLayout save_guild_layout;
    @Bind(R.id.save_line)
    ImageView saveLine;
    Context mContext;
    public HomeRecommentCityItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LinearLayout linearLayout  = (LinearLayout) LinearLayout.inflate(mContext,R.layout.layout_child, this);
        ButterKnife.bind(linearLayout);

        int imageWidth = UIUtils.getScreenWidth();
        int imageHeight = (int)(imageWidth * 210.0 / 375.0);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(imageWidth,imageHeight);
        imageView.setLayoutParams(lp);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageView.getLayoutParams().width,imageView.getLayoutParams().height + UIUtils.dip2px(256));
        linearLayout.setLayoutParams(layoutParams);
    }

    public HomeRecommentCityItemView(Context context) {
        this(context,null);
    }
    HomeCityItemVo homeCityGoodsVo;
    @Override
    public void update(Object _data) {
        homeCityGoodsVo= (HomeCityItemVo)_data;
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Tools.showImageForHomePage(imageView, homeCityGoodsVo.goodsPic,R.mipmap.tuijianxianlu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interClick(homeCityGoodsVo);
            }
        });

        if(!UserEntity.getUser().isLogin(mContext)){
            saveLine.setSelected(false);
        }else if(homeCityGoodsVo.isCollected == 1){
            saveLine.setSelected(true);

        }else if(homeCityGoodsVo.isCollected == 0){
            saveLine.setSelected(false);
        }
        save_guild_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonUtils.isLogin(mContext,getEventSource())) {
                    save_guild_layout.setEnabled(false);
                    ImageView saveLine = (ImageView) view.findViewById(R.id.save_line);
                    if(saveLine.isSelected()){
                        homeCityGoodsVo.isCollected = 0;
                        saveLine.setSelected(false);
                        RequestUncollectLinesNo requestUncollectLinesNo = new RequestUncollectLinesNo(mContext, homeCityGoodsVo.goodsNo);
                        HttpRequestUtils.request(mContext,requestUncollectLinesNo,HomeRecommentCityItemView.this,false);
                    }else{
                        saveLine.setSelected(true);
                        //data.isCollected= 1;
                        HttpRequestUtils.request(mContext,new RequestCollectLineNo(mContext, homeCityGoodsVo.goodsNo),HomeRecommentCityItemView.this,false);
                    }
                }
            }
        });
        des1.setText(homeCityGoodsVo.goodsName);
        cityName.setText(cityNameString + getContext().getResources().getString(R.string.home_recomment_guide_end));
        guidesNum.setText(homeCityGoodsVo.guidesNum + getContext().getResources().getString(R.string.home_recomment_service_china));
        des2.setText(homeCityGoodsVo.recommendedReason);
        //tiyan.setText(getContext().getResources().getString(R.string.home_recomment_experience) + homeCityGoodsVo.purchases);

        setPrice();
        if (!TextUtils.isEmpty(homeCityGoodsVo.goodsPic)) {
            Tools.showRoundImage(polygonImageView, homeCityGoodsVo.guidePic, UIUtils.dip2px(5), R.mipmap.home_head_portrait);
        }

        perPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interClick(homeCityGoodsVo);
            }
        });

        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interClick(homeCityGoodsVo);
            }
        });
    }

    public void setCityName(String cityName){
        this.cityNameString = cityName;
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if(request instanceof RequestCollectLineNo){
            saveLine.setSelected(true);
            homeCityGoodsVo.isCollected= 1;
            CommonUtils.showToast(getContext().getResources().getString(R.string.collect_succeed));
            setSensorsShareEvent(homeCityGoodsVo.goodsNo);
        }else if(request instanceof RequestUncollectLinesNo){
            CommonUtils.showToast(getContext().getResources().getString(R.string.collect_cancel));
        }
        save_guild_layout.setEnabled(true);
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }
    private ErrorHandler errorHandler;
    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if(request instanceof RequestCollectLineNo){
            saveLine.setSelected(false);
            homeCityGoodsVo.isCollected= 0;
            if (errorHandler == null) {
                errorHandler = new ErrorHandler((Activity) mContext, this);
            }
            errorHandler.onDataRequestError(errorInfo, request);
        }
        save_guild_layout.setEnabled(true);
    }

    private void interClick(HomeCityItemVo homeCityGoodsVo) {
        Intent intent = new Intent(mContext, SkuDetailActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, homeCityGoodsVo.goodsDetailUrl);
        intent.putExtra(Constants.PARAMS_ID, homeCityGoodsVo.goodsNo);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        mContext.startActivity(intent);
        SensorsUtils.onAppClick(getEventSource(), "推荐线路", "首页-推荐线路");
    }
    public String getEventSource() {
        return "首页-目的地推荐线路";
    }

    //收藏商品埋点
    public static void setSensorsShareEvent(String goodsNo) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("goodsNo", goodsNo);
            properties.put("favoriteType", "商品");
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("favorite", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPrice(){
        if(homeCityGoodsVo!= null ){
            String price = "";
            try {
                DecimalFormat df = new DecimalFormat("#,###");
                price = df.format(homeCityGoodsVo.perPrice);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
            String finalPerPrice = "¥" + price + getContext().getResources().getString(R.string.home_album_purchse);
            SpannableString spannableString = new SpannableString(finalPerPrice);
            spannableString.setSpan(new AbsoluteSizeSpan(12, true), finalPerPrice.length()-3, finalPerPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            perPrice.setText(spannableString);
        }
    }
}
