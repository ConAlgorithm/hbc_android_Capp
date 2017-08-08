package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChoiceCommentActivity;
import com.hugboga.custom.adapter.HomeGuideEvaluateAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeCommentInfoVo;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;

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
                Tools.showImage(homeGuideEvaluateHolder.polygonImageView, homeCommentInfoVo.userAvatar, R.mipmap.icon_avatar_user);
            }
            homeGuideEvaluateHolder.userName.setText(homeCommentInfoVo.userName);
            homeGuideEvaluateHolder.location.setText(homeCommentInfoVo.serviceCityName);
            homeGuideEvaluateHolder.serviceType.setText(homeCommentInfoVo.orderTypeName);
            homeGuideEvaluateHolder.guideName.setText(homeCommentInfoVo.guideName);
            homeGuideEvaluateHolder.evaluateContent.setText(homeCommentInfoVo.comment);
            if (homeCommentInfoVo.commentPics != null && homeCommentInfoVo.commentPics.size() > 0) {
                Tools.showImage(homeGuideEvaluateHolder.imageView, homeCommentInfoVo.commentPics.get(0));
                homeGuideEvaluateHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(homeCommentInfoVo.commentPicsL != null && homeCommentInfoVo.commentPicsL.size() > 0){
                            CommonUtils.showLargerImages(context, homeCommentInfoVo.commentPicsL, 0);
                        }
                    }
                });
            }else if(homeCommentInfoVo.commentPics == null || homeCommentInfoVo.commentPics.size() == 0){
                //homeGuideEvaluateHolder.imageView.setImageResource(R.mipmap.icon_avatar_user);
            }
            if(homeCommentInfoVo.commentPics != null && homeCommentInfoVo.commentPics.size() >0){
                homeGuideEvaluateHolder.imgCount.setText(homeCommentInfoVo.commentPics.size() + "图");
            }
            homeGuideEvaluateHolder.filterGuideMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChoiceCommentActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    context.startActivity(intent);
                }
            });
        }
    }
    public String getEventSource() {
        return "首页";
    }
}
