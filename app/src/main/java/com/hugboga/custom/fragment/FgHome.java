package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.net.ExceptionErrorCode;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.recycler.ZDefaultDivider;
import com.huangbaoche.hbcframe.widget.recycler.ZListPageView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestHome;
import com.hugboga.custom.widget.HomeBannerView;
import com.hugboga.custom.widget.HomeBottomLayout;
import com.hugboga.custom.widget.HomeChoicenessRouteView;
import com.hugboga.custom.widget.HomeDynamicView;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
/**
 * 首页
 */
@ContentView(R.layout.fg_home)
public class FgHome extends BaseFragment {

    @Bind(R.id.home_banner_view)
    HomeBannerView bannerView;

    @Bind(R.id.home_dynamic_view)
    HomeDynamicView dynamicView;

    @Bind(R.id.home_bottom_layout)
    HomeBottomLayout bottomLayout;

    @Bind(R.id.home_choiceness_route_view)
    HomeChoicenessRouteView routeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initHeader() {
        ArrayList<String> urlList = new ArrayList<String>();
        urlList.add("http://img2.imgtn.bdimg.com/it/u=3284258022,2153387292&fm=21&gp=0.jpg");
        urlList.add("http://img2.imgtn.bdimg.com/it/u=3528047515,581472853&fm=21&gp=0.jpg");
        urlList.add("http://img3.imgtn.bdimg.com/it/u=221995782,2823567885&fm=21&gp=0.jpg");
        bannerView.update(urlList);
        bottomLayout.setFragment(FgHome.this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }
}
