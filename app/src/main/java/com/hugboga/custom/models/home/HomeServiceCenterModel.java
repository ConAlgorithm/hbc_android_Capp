package com.hugboga.custom.models.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.statistic.sensors.SensorsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/11/23.
 */
public class HomeServiceCenterModel extends EpoxyModel<LinearLayout> {

    final static String url1 = "https://act.huangbaoche.com/h5/cactivity/serPromise/index.html?type=0";
    final static String url2 = "https://act.huangbaoche.com/h5/cactivity/serPromise/index.html?type=1";
    final static String url3 = "https://act.huangbaoche.com/h5/cactivity/serPromise/index.html?type=2";
    final static String url4 = "https://act.huangbaoche.com/h5/cactivity/serPromise/index.html?type=3";

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_home_service_center;
    }

    @Override
    public void bind(LinearLayout view) {
        super.bind(view);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.view1_layout, R.id.view2_layout, R.id.view3_layout, R.id.view4_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view1_layout:
                intentWebActivity(view.getContext(), url1);
                SensorsUtils.onAppClick(getEventSource(), "一价全包", "首页-一价全包");
                break;
            case R.id.view2_layout:
                intentWebActivity(view.getContext(), url2);
                SensorsUtils.onAppClick(getEventSource(), "服务保障", "首页-服务保障");
                break;
            case R.id.view3_layout:
                intentWebActivity(view.getContext(), url3);
                SensorsUtils.onAppClick(getEventSource(), "先行赔付", "首页-先行赔付");
                break;
            case R.id.view4_layout:
                intentWebActivity(view.getContext(), url4);
                SensorsUtils.onAppClick(getEventSource(), "免费保险", "首页-免费保险");
                break;
        }
    }

    private void intentWebActivity(Context context, String url) {
        Intent intent = new Intent(context, WebInfoActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        intent.putExtra(WebInfoActivity.WEB_URL, url);
        context.startActivity(intent);
    }

    public String getEventSource() {
        return "首页";
    }
}
