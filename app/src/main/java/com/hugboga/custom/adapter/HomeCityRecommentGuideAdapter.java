package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.activity.FilterGuideListActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeCityContentVo2;
import com.hugboga.custom.data.bean.HomeCityItemVo;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.HomeRecommentGuideItemView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;

/**
 * Created by zhangqiang on 17/9/15.
 */

public class HomeCityRecommentGuideAdapter extends RecyclerView.Adapter<HomeCityRecommentGuideAdapter.MyViewHolder>{

    private static final int TYPE_ITEM = 0x01;
    private static final int TYPE_MORE = 0x02;
    private Context mContext;
    private int displayImgWidth;
    private int displayImgHeight;
    HomeCityContentVo2 homeCityContentVo2;
    ArrayList<HomeCityItemVo> guideBeanList;
    public HomeCityRecommentGuideAdapter(Context context, int displayImgWidth, int displayImgHeight, HomeCityContentVo2 homeCityContentVo2, ArrayList<HomeCityItemVo> guideBeanList){
        this.mContext = context;
        this.displayImgWidth = displayImgWidth;
        this.displayImgHeight = displayImgHeight;
        this.homeCityContentVo2 = homeCityContentVo2;
        this.guideBeanList = guideBeanList;
    }
    @Override
    public HomeCityRecommentGuideAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case TYPE_ITEM:
                HomeRecommentGuideItemView homeRecommentGuideItemView = new HomeRecommentGuideItemView(mContext);
                homeRecommentGuideItemView.setImageBound(displayImgWidth, displayImgHeight);
                itemView = homeRecommentGuideItemView;
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(displayImgWidth, displayImgHeight + ScreenUtil.dip2px(175));
                itemView.setLayoutParams(params);
                break;
            case TYPE_MORE:
                LinearLayout linearLayout = new LinearLayout(mContext);
                TextView moreIV = new TextView(mContext);
                moreIV.setText(mContext.getResources().getString(R.string.home_more));
                moreIV.setTextSize(14);
                moreIV.setTextColor(mContext.getResources().getColor(R.color.color_151515));
                Drawable image = mContext.getResources().getDrawable(R.mipmap.personalcenter_right);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                moreIV.setCompoundDrawables(null,null,image,null);
                moreIV.setCompoundDrawablePadding(5);
                moreIV.setGravity(Gravity.CENTER);
                linearLayout.addView(moreIV);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_f8f8f8));
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(UIUtils.dip2px(100), UIUtils.dip2px(175)));
                itemView = linearLayout;
                break;
        }

        return new HomeCityRecommentGuideAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeCityRecommentGuideAdapter.MyViewHolder holder, final int position) {
        if (position == getItemCount() -1) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, FilterGuideListActivity.class);
                    FilterGuideListActivity.Params params = new FilterGuideListActivity.Params();
                    if(homeCityContentVo2.placeType == 1){
                        params.id = homeCityContentVo2.countryId;
                        params.cityHomeType = CityListActivity.CityHomeType.COUNTRY;
                        params.titleName = homeCityContentVo2.countryName;
                    }else if(homeCityContentVo2.placeType == 2){
                        params.id = homeCityContentVo2.cityId;
                        params.cityHomeType = CityListActivity.CityHomeType.CITY;
                        params.titleName = homeCityContentVo2.cityName;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.PARAMS_DATA, params);
                    intent.putExtras(bundle);
                    //intent.putExtra(Constants.PARAMS_DATA, params);
                    //mContext.startActivity(intent);
                    intentActivity(mContext, intent,FilterGuideListActivity.class,null);
                   // SensorsUtils.onAppClick(getEventSource(),"选择心仪的司导服务","首页-选择心仪的司导服务");
                }
            });
        }else{
            ((HbcViewBehavior) holder.itemView).update(guideBeanList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return guideBeanList==null ? 0 : guideBeanList.size()+1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }

    //分页加载更新数据
    public void add(ArrayList<HomeCityItemVo> guideBeanList) {
        guideBeanList.addAll(guideBeanList);
        notifyDataSetChanged();
    }

    public void setData(ArrayList<HomeCityItemVo> guideBeanList) {
        guideBeanList = guideBeanList;
        notifyDataSetChanged();
    }
    public String getEventSource() {
        return "首页-目的地推荐司导";
    }

    private void intentActivity(Context context, Intent intent,Class<?> cls, String eventId) {
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        context.startActivity(intent);
        if (!TextUtils.isEmpty(eventId)) {
            MobClickUtils.onEvent(eventId);
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
}
