package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.ChooseGuideAdapter;
import com.hugboga.custom.data.bean.CanServiceGuideBean;
import com.hugboga.custom.data.request.RequestAcceptGuide;
import com.hugboga.custom.widget.ZListView;
import com.netease.nim.uikit.common.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created on 16/9/9.
 */

public class CanServiceGuideListActivity extends BaseActivity {

    @Bind(R.id.zlistview)
    ZListView zlistview;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_choose_guide);
        ButterKnife.bind(this);
        initDefaultTitleBar();
        initView();
        getData();
    }

    ChooseGuideAdapter adapter;
    String orderNo = "Z190347971527";
    int limit = 20;
    int offset = 0;
    List<CanServiceGuideBean.GuidesBean> list = new ArrayList<>();
    int total = 0;
    private void getData(){
        RequestAcceptGuide requestAcceptGuide = new RequestAcceptGuide(activity,orderNo,limit,offset);
        HttpRequestUtils.request(activity, requestAcceptGuide, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                LogUtil.e("===","====");
                CanServiceGuideBean canServiceGuideBean = ((RequestAcceptGuide)request).getData();
                list.addAll(canServiceGuideBean.getGuides());
                total = canServiceGuideBean.getTotalSize();
                fgTitle.setText(String.format(getString(R.string.choose_guide_title),total));
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {
                LogUtil.e("===","====");
            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                LogUtil.e("===","===="+errorInfo.exception.getMessage());
            }
        });
    }

    private void initView() {

        adapter = new ChooseGuideAdapter(activity);
        zlistview.setAdapter(adapter);
        zlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        zlistview.setonRefreshListener(new ZListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        zlistview.getHeadView().setVisibility(View.GONE);
        zlistview.onLoadCompleteNone();
        zlistview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
