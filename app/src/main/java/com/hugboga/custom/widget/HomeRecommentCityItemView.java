package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
import com.hugboga.custom.R;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeCityGoodsVo;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestCollectLineNo;
import com.hugboga.custom.data.request.RequestUncollectLinesNo;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

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
    @Bind(R.id.tiyan)
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

        int imageWidth = UIUtils.getScreenWidth() - 2 * UIUtils.dip2px(16);
        int imageHeight = imageWidth * 189 / 330;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(imageWidth,imageHeight);
        imageView.setLayoutParams(lp);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageView.getLayoutParams().width,imageView.getLayoutParams().height + UIUtils.dip2px(256));
        linearLayout.setLayoutParams(layoutParams);
    }

    public HomeRecommentCityItemView(Context context) {
        this(context,null);
    }
    HomeCityGoodsVo homeCityGoodsVo;
    @Override
    public void update(Object _data) {
        homeCityGoodsVo= (HomeCityGoodsVo)_data;
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
                if(isLogin()) {
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
        cityName.setText(cityNameString + "司导推荐");
        guidesNum.setText(homeCityGoodsVo.guidesNum + "位中文司导可服务");
        des2.setText(homeCityGoodsVo.recommendedReason);
        tiyan.setText("已体验" + homeCityGoodsVo.purchases);
        perPrice.setText("¥" + homeCityGoodsVo.perPrice + "起/人");
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
            CommonUtils.showToast("收藏成功");
            //setSensorsShareEvent(filterGuideBean.guideId);
        }else if(request instanceof RequestUncollectLinesNo){
            CommonUtils.showToast("已取消收藏");
        }
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
    }
    /**
     * 判断是否登录
     */
    private boolean isLogin() {
        if (UserEntity.getUser().isLogin(mContext)) {
            return true;
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            //intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
            mContext.startActivity(intent);
            return false;
        }
    }

    private void interClick(HomeCityGoodsVo homeCityGoodsVo) {
        Intent intent = new Intent(mContext, SkuDetailActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, homeCityGoodsVo.goodsDetailUrl);
        intent.putExtra(Constants.PARAMS_ID, homeCityGoodsVo.goodsNo);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        mContext.startActivity(intent);
        SensorsUtils.onAppClick(getEventSource(), "推荐线路", "首页-推荐线路");
    }
    public String getEventSource() {
        return "首页";
    }
}
