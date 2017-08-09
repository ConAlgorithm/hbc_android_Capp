package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.statistic.MobClickUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/2.
 */

public class HomeH5Model extends EpoxyModelWithHolder implements View.OnClickListener{

    Context context;
    HomeH5Holder homeH5Holder;
    public HomeH5Model(Context context){
        this.context = context;
    }
    @Override
    protected EpoxyHolder createNewHolder() {
        return new HomeH5Holder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_h5;
    }

    static class HomeH5Holder extends EpoxyHolder{
        View itemView;
        @Bind(R.id.view1)
        TextView view1;
        @Bind(R.id.view2)
        TextView view2;
        @Bind(R.id.view3)
        TextView view3;
        @Bind(R.id.view4)
        TextView view4;

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
        homeH5Holder = (HomeH5Model.HomeH5Holder) holder;
        init();
    }
    public void update() {
//        if(homeServiceHolder != null){
//            homeServiceHolder
//        }
    }
    private void init(){
        if(homeH5Holder != null){
            homeH5Holder.view1.setOnClickListener(this);
            homeH5Holder.view2.setOnClickListener(this);
            homeH5Holder.view3.setOnClickListener(this);
            homeH5Holder.view4.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view1:
                intentActivity(context, WebInfoActivity.class,getEventSource(),"");
            break;
            case R.id.view2:
                intentActivity(context, WebInfoActivity.class,getEventSource(),"");
                break;
            case R.id.view3:
                intentActivity(context, WebInfoActivity.class,getEventSource(),"");
                break;
            case R.id.view4:
                intentActivity(context, WebInfoActivity.class,getEventSource(),"");
                break;
        }

    }
    private void intentActivity(Context context, Class<?> cls, String eventId,String Url) {
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
