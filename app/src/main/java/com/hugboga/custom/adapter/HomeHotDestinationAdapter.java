package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeHotDestination;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.HomeHotDestinationItemView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by zhangqiang on 17/9/13.
 */

public class HomeHotDestinationAdapter extends RecyclerView.Adapter {
    private static final int TYPE_ITEM = 0x01;
    private static final int TYPE_MORE = 0x02;
    private Context mContext;
    private int displayImgWidth;
    private int displayImgHeight;
    ArrayList<HomeHotDestination> hotCities;
    public HomeHotDestinationAdapter(Context context,int displayImgWidth,int displayImgHeight,ArrayList<HomeHotDestination> hotCities){
        this.mContext = context;
        this.displayImgWidth = displayImgWidth;
        this.displayImgHeight = displayImgHeight;
        this.hotCities = hotCities;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case TYPE_ITEM:
                HomeHotDestinationItemView homeHotDestinationItemView = new HomeHotDestinationItemView(mContext);
                itemView = homeHotDestinationItemView;
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(displayImgWidth, displayImgHeight + ScreenUtil.dip2px(100));
                itemView.setLayoutParams(params);
                break;
            case TYPE_MORE:
                LinearLayout linearLayout = new LinearLayout(mContext);
                TextView moreIV = new TextView(mContext);
                moreIV.setText(mContext.getResources().getString(R.string.home_more));
                moreIV.setTextSize(14);
                //moreIV.setBackgroundColor(0x33ff0000);
                moreIV.setTextColor(mContext.getResources().getColor(R.color.color_151515));
                Drawable image = mContext.getResources().getDrawable(R.mipmap.personalcenter_right);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                moreIV.setCompoundDrawables(null,null,image,null);
                moreIV.setCompoundDrawablePadding(3);
                moreIV.setGravity(Gravity.CENTER);
                linearLayout.addView(moreIV);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_f8f8f8));
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(displayImgWidth,displayImgHeight));
                itemView = linearLayout;
                break;
        }

        return new HomeHotDestinationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == getItemCount() -1) {
            LinearLayout linearLayout = (LinearLayout) holder.itemView;
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                    EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 1));
                    SensorsUtils.setPageEvent("目的地","目的地","首页-热门目的地");
                }
            });
        }else{
            ((HbcViewBehavior) holder.itemView).update(hotCities.get(position));
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() -1) {
            return TYPE_MORE;
        } else {
            return TYPE_ITEM;
        }
    }
    @Override
    public int getItemCount() {
        if(hotCities!= null){

        }
        return hotCities.size() == 0?0:hotCities.size()+1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }

    public void setData(ArrayList<HomeHotDestination> hotCities){
        this.hotCities = hotCities;
    }
}
