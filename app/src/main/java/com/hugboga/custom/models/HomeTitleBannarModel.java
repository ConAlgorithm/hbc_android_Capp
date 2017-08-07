package com.hugboga.custom.models;

import android.content.Context;
import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeAggregationVo4;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HomeActivitiesView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/1.
 */

public class HomeTitleBannarModel extends EpoxyModelWithHolder {

    HomeTitleBannarHolder homeTitleBannarHolder;
    Context context;
    ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activityPageSettings;
    @Override
    protected EpoxyHolder createNewHolder() {
        return new HomeTitleBannarHolder();
    }

    public HomeTitleBannarModel(Context context,ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activityPageSettings){
        this.context = context;
        this.activityPageSettings = activityPageSettings;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_title_bannar;
    }

    static class HomeTitleBannarHolder extends EpoxyHolder{
        View itemView;
        @Bind(R.id.home_activities_view)
        HomeActivitiesView activitiesView;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            int imageWidth = UIUtils.getScreenWidth()-2*UIUtils.dip2px(16);
            int imageHeight = imageWidth * 296 /328;
            activitiesView.getLayoutParams().height = imageHeight;
        }
    }
    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        homeTitleBannarHolder = (HomeTitleBannarHolder) holder;
        init();
    }
    public void update() {
        if(homeTitleBannarHolder != null){
            homeTitleBannarHolder.activitiesView.update(activityPageSettings);
        }
    }
    private void init(){
        if(homeTitleBannarHolder != null){
            homeTitleBannarHolder.activitiesView.update(activityPageSettings);
        }
    }

    public int getActivitysTop() {
        if (homeTitleBannarHolder.activitiesView != null) {
            return UIUtils.getViewTop(homeTitleBannarHolder.activitiesView);
        }
        return 0;
    }
}
