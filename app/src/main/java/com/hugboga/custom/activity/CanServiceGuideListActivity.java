package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.ChooseGuideAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CanServiceGuideBean;
import com.hugboga.custom.data.request.RequestAcceptGuide;
import com.hugboga.custom.utils.ApiReportHelper;
import com.netease.nim.uikit.common.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created on 16/9/9.
 */

public class CanServiceGuideListActivity extends BaseActivity implements View.OnKeyListener{

    @Bind(R.id.zlistview)
    ListView zlistview;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_choose_guide);
        ButterKnife.bind(this);
        initDefaultTitleBar();
        getIntentData();
        initView();
        getData();
    }

    private void getIntentData() {
        orderNo = this.getIntent().getStringExtra("orderNo");
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
                ApiReportHelper.getInstance().addReport(request);
                CanServiceGuideBean canServiceGuideBean = ((RequestAcceptGuide)request).getData();
                list.addAll(canServiceGuideBean.getGuides());
                total = canServiceGuideBean.getTotalSize();
                fgTitle.setText(String.format(getString(R.string.choose_guide_title),total));
                if(offset == 0) {
                    adapter.setList(list);
                }
                offset += limit;
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

    LinearLayout headView;
    private void initView() {
        headView = (LinearLayout)LayoutInflater.from(activity).inflate(R.layout.choose_guide_head,null);
        zlistview.addHeaderView(headView);
        adapter = new ChooseGuideAdapter(activity);
        zlistview.setAdapter(adapter);
        zlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GuideDetailActivity.Params params = new GuideDetailActivity.Params();
                params.guideId = list.get(position-1).getGuideId();
                params.isSelectedService = true;
                Intent intent = new Intent(activity, GuideDetailActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                startActivity(intent);
            }
        });


        zlistview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        if (offset < total) {
                            getData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public String getEventId() {
        String str="";
        switch (getIntent().getStringExtra("ordertype")){
            case "3":
                str="包车游";
            break;
            case "5":
                str="固定线路";
            break;
            case "6":
                str="推荐线路";
                break;
        }
        return str;
    }
}
