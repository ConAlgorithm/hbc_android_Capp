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
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.action.ActionController;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.HomeAggregationVo4;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.ScreenUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;

import static com.hugboga.im.ImHelper.mContext;

/**
 * Created by zhangqiang on 17/8/5.
 */

public class HomeBannerAdapter extends PagerAdapter {
    ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activityPageSettings;
    Context context;
    ImageView imageView;
    ImageView color_on_img;
    LinearLayout linearLayout;
    TextView name_album;
    TextView purchse_album;
    public HomeBannerAdapter(Context context,ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activityPageSettings){
        this.context = context;
        this.activityPageSettings = activityPageSettings;

        //最后一页,添加更多
        HomeAggregationVo4.ActivityPageSettingVo activityPageSettingVo = new HomeAggregationVo4.ActivityPageSettingVo();
        activityPageSettings.add(activityPageSettingVo);
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
        name_album = (TextView) linearLayout.findViewById(R.id.name_album);
        purchse_album = (TextView) linearLayout.findViewById(R.id.purchse_album);
        imageView = (ImageView) linearLayout.findViewById(R.id.home_banner_img);
        imageView.getLayoutParams().width = UIUtils.getScreenWidth()-2*UIUtils.dip2px(16);
        imageView.getLayoutParams().height = imageView.getLayoutParams().width * 160 /328;
        color_on_img = (ImageView) linearLayout.findViewById(R.id.color_on_img);
        color_on_img.getLayoutParams().width = UIUtils.getScreenWidth()-2*UIUtils.dip2px(16);
        color_on_img.getLayoutParams().height = color_on_img.getLayoutParams().width * 160 /328;

        if(position == activityPageSettings.size()-1){
            imageView.setImageResource(R.mipmap.home_banner_more);
            name_album.setText("");
            //color_on_img.setVisibility(View.GONE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openActivitesPage();
                }
            });
        }else if(position < activityPageSettings.size() -1){
            Tools.showImageForHomePage(imageView,activityPageSettingVo.getPicture(),R.mipmap.morentu_02);
            name_album.setText(activityPageSettings.get(position).activityName);
            //color_on_img.setVisibility(View.VISIBLE);
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
        }
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

    /**
     * 打开活动页面
     */
    private void openActivitesPage() {
        MobClickUtils.onEvent(StatisticConstant.LAUNCH_ACTLIST, (Map)new HashMap<>().put("source", "首页"));
        Intent intent = new Intent(context, WebInfoActivity.class);
        if(UserEntity.getUser().isLogin(context)){
            intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_ACTIVITY + UserEntity.getUser().getUserId(context) + "&t=" + new Random().nextInt(100000));
        }else{
            intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_ACTIVITY );
        }
        context.startActivity(intent);
        setSensorsDefaultEvent("活动列表", SensorsConstant.ACTLIST);
    }

    protected void setSensorsDefaultEvent(String webTitle, String webUrl) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_web_title", webTitle);
            properties.put("hbc_web_url", webUrl);
            properties.put("hbc_refer", "首页");
            SensorsDataAPI.sharedInstance(context).track("page_view", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
