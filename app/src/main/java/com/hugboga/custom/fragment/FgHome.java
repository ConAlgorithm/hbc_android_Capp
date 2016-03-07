package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeAdapter;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.request.RequestHome;
import com.hugboga.custom.utils.MLog;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * 首页
 * Created by admin on 2016/3/1.
 */

@ContentView(R.layout.fg_home)
public class FgHome extends BaseFragment {


    @ViewInject(android.R.id.list)
    ListView listView;


    @ViewInject(R.id.header_left_btn)
    protected View leftBtn;
    @ViewInject(R.id.header_right_btn)
    protected View rightBtn;


    private ArrayList<HomeBean> dataList;
    private HomeAdapter adapter;


    @Override
    protected void initHeader() {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.fg_home_header, null);
        listView.addHeaderView(header);
    }

    @Override
    protected void initView() {
        adapter = new HomeAdapter(getActivity());
        listView.setAdapter(adapter);
    }

    private ArrayList<String> loadItems() {
        ArrayList<String> countries = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            countries.add("index = " + i);
        }
        return countries;
    }

    @Override
    protected Callback.Cancelable requestData() {
        RequestHome requestHome = new RequestHome(getActivity());
        return requestData(requestHome);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestHome) {
            RequestHome requestHome = (RequestHome) request;
            dataList = requestHome.getData();
            inflateContent();
        }
    }

    @Override
    public void inflateContent() {
        MLog.e("dataList = " + dataList.get(0).mainTitle);
        adapter.setList(dataList);
    }

    @Event({R.id.header_left_btn, R.id.header_right_btn, R.id.fg_home_menu1, R.id.fg_home_menu2, R.id.fg_home_menu3})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn://左侧按钮
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.header_right_btn://右侧搜索

                break;
            case R.id.fg_home_menu1://中文接送机

                break;
            case R.id.fg_home_menu2://按天包车

                break;
            case R.id.fg_home_menu3://单次接送

                break;

        }

    }

}
