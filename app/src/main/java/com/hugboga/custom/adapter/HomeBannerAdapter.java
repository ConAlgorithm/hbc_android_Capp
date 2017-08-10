package com.hugboga.custom.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.action.ActionController;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.HomeAggregationVo4;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.ScreenUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.Bind;

import static com.hugboga.im.ImHelper.mContext;

/**
 * Created by zhangqiang on 17/8/5.
 */

public class HomeBannerAdapter extends PagerAdapter {
    ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activityPageSettings;
    Context context;
    ImageView imageView;
    LinearLayout linearLayout;
    public HomeBannerAdapter(Context context,ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activityPageSettings){
        this.context = context;
        this.activityPageSettings = activityPageSettings;
    }
    @Override
    public int getCount() {
        return activityPageSettings.size() == 0 ? 0:activityPageSettings.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final HomeAggregationVo4.ActivityPageSettingVo activityPageSettingVo = activityPageSettings.get(position);
        linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.home_banner_itemm, null);
        imageView = (ImageView) linearLayout.findViewById(R.id.home_banner_img);
        imageView.getLayoutParams().width = UIUtils.getScreenWidth()-2*UIUtils.dip2px(16);
        imageView.getLayoutParams().height = imageView.getLayoutParams().width * 160 /328;
        Tools.showImage(imageView,activityPageSettingVo.getPicture(),R.mipmap.evaluate_dafault);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityPageSettingVo.pushScheme == null) {
                    if (!TextUtils.isEmpty(activityPageSettingVo.urlAddress)) {
                        Intent intent = new Intent(context, WebInfoActivity.class);
                        intent.putExtra(WebInfoActivity.WEB_URL, activityPageSettingVo.urlAddress);
                        context.startActivity(intent);
                    }
                } else {
                    ActionController actionFactory = ActionController.getInstance();
                    actionFactory.doAction(mContext, activityPageSettingVo.pushScheme);
                }
                SensorsUtils.onAppClick(getEventSource(),"广告","首页-广告");
            }
        });
        container.addView(linearLayout);
        linearLayout.getLayoutParams().height = imageView.getLayoutParams().height +UIUtils.dip2px(56);
        return linearLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LinearLayout view = (LinearLayout) object;
        container.removeView(view);
    }

    public void setData(ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activityPageSettings){
        this.activityPageSettings = activityPageSettings;
        notifyDataSetChanged();
    }

    public String getEventSource() {
        return "首页";
    }
}
