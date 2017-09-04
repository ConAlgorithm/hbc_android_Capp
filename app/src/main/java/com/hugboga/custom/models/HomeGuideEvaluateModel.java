package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChoiceCommentActivity;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.adapter.HomeGuideEvaluateAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeCommentInfoVo;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import net.grobas.view.PolygonImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/2.
 */

public class HomeGuideEvaluateModel extends EpoxyModelWithHolder {
    Context context;
    HomeGuideEvaluateHolder homeGuideEvaluateHolder;
    int position;
    HomeCommentInfoVo homeCommentInfoVo;
    HomeGuideEvaluateAdapter homeGuideEvaluateAdapter;
    public HomeGuideEvaluateModel(Context context,HomeCommentInfoVo homeCommentInfoVo,int position){
        this.context = context;
        this.position = position;
        this.homeCommentInfoVo = homeCommentInfoVo;
    }
    @Override
    protected EpoxyHolder createNewHolder() {
        return new HomeGuideEvaluateHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_guide_evaluate;
    }
    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        homeGuideEvaluateHolder = (HomeGuideEvaluateModel.HomeGuideEvaluateHolder) holder;
        init();
    }
    static class HomeGuideEvaluateHolder extends EpoxyHolder{
        View itemView;
        @Bind(R.id.evaluate_content)
        TextView evaluateContent;
        @Bind(R.id.title_evaluate)
        RelativeLayout title;
        @Bind(R.id.avr)
        PolygonImageView polygonImageView;
        @Bind(R.id.userName)
        TextView userName;
        @Bind(R.id.location)
        TextView location;
        @Bind(R.id.serviceType)
        TextView serviceType;
        @Bind(R.id.guideName)
        TextView guideName;
        @Bind(R.id.evaluate_img)
        ImageView imageView;
        @Bind(R.id.img_count)
        TextView imgCount;
        @Bind(R.id.filter_guide_more)
        TextView filterGuideMore;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);


        }
    }

    private void init(){
        if(homeGuideEvaluateHolder != null){
            if(position == 0){
                homeGuideEvaluateHolder.title.setVisibility(View.VISIBLE);
            } else {
                homeGuideEvaluateHolder.title.setVisibility(View.GONE);
            }
            if(homeCommentInfoVo == null){
                return;
            }
            if (!TextUtils.isEmpty(homeCommentInfoVo.userAvatar)) {
                Tools.showRoundImage(homeGuideEvaluateHolder.polygonImageView, homeCommentInfoVo.userAvatar, UIUtils.dip2px(5),R.mipmap.icon_avatar_user);
            }
            homeGuideEvaluateHolder.userName.setText(homeCommentInfoVo.userName);
            homeGuideEvaluateHolder.location.setText(homeCommentInfoVo.serviceCityName);
            homeGuideEvaluateHolder.location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CityListActivity.Params params = new CityListActivity.Params();
                    params.id = homeCommentInfoVo.serviceCityId;
                    params.cityHomeType = CityListActivity.CityHomeType.CITY;
                    params.titleName = homeCommentInfoVo.serviceCityName;
                    intentActivity(context, CityListActivity.class, getEventSource(), params);
                    SensorsUtils.onAppClick(getEventSource(),"游客说","首页-游客说");
                }
            });
            homeGuideEvaluateHolder.serviceType.setText(homeCommentInfoVo.orderTypeName);
            homeGuideEvaluateHolder.guideName.setText(homeCommentInfoVo.guideName);
            homeGuideEvaluateHolder.guideName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
                    params.guideId = homeCommentInfoVo.guideId;
                    Intent intent = new Intent(context, GuideWebDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    context.startActivity(intent);
                    SensorsUtils.onAppClick(getEventSource(),"游客说","首页-游客说");
                }
            });
            homeGuideEvaluateHolder.evaluateContent.setText(homeCommentInfoVo.comment);
            if (homeCommentInfoVo.commentPic != null && homeCommentInfoVo.commentPic.size() > 0) {
                Tools.showImage(homeGuideEvaluateHolder.imageView, homeCommentInfoVo.commentPic.get(0));
                homeGuideEvaluateHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(homeCommentInfoVo.commentPicL != null && homeCommentInfoVo.commentPicL.size() > 0){
                            CommonUtils.showLargerImages(context, homeCommentInfoVo.commentPicL, 0);
                            SensorsUtils.onAppClick(getEventSource(),"游客说","首页-游客说");
                        }
                    }
                });
            }else if(homeCommentInfoVo.commentPic == null || homeCommentInfoVo.commentPic.size() == 0){
                //homeGuideEvaluateHolder.imageView.setImageResource(R.mipmap.icon_avatar_user);
            }
            if(homeCommentInfoVo.commentPic != null && homeCommentInfoVo.commentPic.size() >0){
                homeGuideEvaluateHolder.imgCount.setText(homeCommentInfoVo.commentPic.size() + "图");
                homeGuideEvaluateHolder.imageView.setVisibility(View.VISIBLE);
            }else if(homeCommentInfoVo.commentPic != null && homeCommentInfoVo.commentPic.size() == 0){
                homeGuideEvaluateHolder.imageView.setVisibility(View.GONE);
            }else if(homeCommentInfoVo.commentPic == null){
                homeGuideEvaluateHolder.imageView.setVisibility(View.GONE);
            }
            homeGuideEvaluateHolder.filterGuideMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentActivity(context,ChoiceCommentActivity.class,getEventSource(),null);
                    SensorsUtils.onAppClick(getEventSource(),"游客说","首页-游客说");
                }
            });
            homeGuideEvaluateHolder.evaluateContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentActivity(context,ChoiceCommentActivity.class,getEventSource(),null);
                    SensorsUtils.onAppClick(getEventSource(),"游客说","首页-游客说");
                }
            });
        }
    }
    public String getEventSource() {
        return "首页";
    }

    private void intentActivity(Context context, Class<?> cls, String eventId, CityListActivity.Params params) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        intent.putExtra(Constants.PARAMS_DATA, params);
        context.startActivity(intent);
        if (!TextUtils.isEmpty(eventId)) {
            MobClickUtils.onEvent(eventId);
        }
    }
}
