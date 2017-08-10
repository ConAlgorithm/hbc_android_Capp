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
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.FilterSkuListActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleActivity;
import com.hugboga.custom.activity.TravelPurposeFormActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 17/8/1.
 */

public class HomeServiceModel extends EpoxyModelWithHolder implements View.OnClickListener{

    Context context;
    HomeServiceHolder homeServiceHolder;
    @Override
    protected EpoxyHolder createNewHolder() {
        return new HomeServiceHolder();
    }


    public HomeServiceModel(Context context){
        this.context = context;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_service;
    }

    static class HomeServiceHolder extends EpoxyHolder{
        View itemView;
        @Bind(R.id.charter_id_img)
        ImageView charterImg;
        @Bind(R.id.view1)
        View view;
        @Bind(R.id.daystravel)
        TextView daystravel;
        @Bind(R.id.single_pick_send)
        RelativeLayout singlePickSend;
        @Bind(R.id.pick_send)
        RelativeLayout pickSend;
        @Bind(R.id.private_ordering)
        TextView privateOrdering;
        @Bind(R.id.home_line)
        TextView homeLine;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        homeServiceHolder = (HomeServiceHolder) holder;
        init();
    }
    public void update() {
//        if(homeServiceHolder != null){
//            homeServiceHolder
//        }
    }
    private void init(){
        if(homeServiceHolder != null){
            homeServiceHolder.pickSend.setOnClickListener(this);
            homeServiceHolder.daystravel.setOnClickListener(this);
            homeServiceHolder.singlePickSend.setOnClickListener(this);
            homeServiceHolder.privateOrdering.setOnClickListener(this);
            homeServiceHolder.homeLine.setOnClickListener(this);
            homeServiceHolder.charterImg.setOnClickListener(this);
        }
    }
    public int getViewTop() {
        if (homeServiceHolder.view != null) {
            return UIUtils.getViewTop(homeServiceHolder.view);
        }
        return 0;
    }
    @OnClick({R.id.daystravel,R.id.single_pick_send,R.id.pick_send,R.id.private_ordering,R.id.home_line,R.id.charter_id_img})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.daystravel:
                intentActivity(context, CharterFirstStepActivity.class, StatisticConstant.LAUNCH_DETAIL_R);
                break;
            case R.id.single_pick_send:
                intentActivity(context, SingleActivity.class, StatisticConstant.LAUNCH_C);
                break;
            case R.id.pick_send:
                intentActivity(context, PickSendActivity.class, StatisticConstant.LAUNCH_J);
                break;
            case R.id.private_ordering:
                intentActivity(context, TravelPurposeFormActivity.class, StatisticConstant.YI_XIANG);
                break;
            case R.id.home_line:
                intentActivity(context, FilterSkuListActivity.class,null);
                break;
            case R.id.charter_id_img:
                String url = "https://act.huangbaoche.com/h5/cactivity/chineseGuide/index.html";
                intentWebInfoActivity(context, url);
                break;
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

    private void intentWebInfoActivity(Context context,String _url) {
        if (context == null || TextUtils.isEmpty(_url)) {
            return;
        }
        Intent intent = new Intent(context, WebInfoActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, _url);
        context.startActivity(intent);
    }
    public String getEventSource() {
        return "首页";
    }
}
