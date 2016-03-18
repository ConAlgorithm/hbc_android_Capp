package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeAdapter;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.request.RequestHome;
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
public class FgHome extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {


    public static final String FILTER_FLUSH = "com.hugboga.custom.home.flush";
    @ViewInject(android.R.id.list)
    ListView listView;



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
        listView.setOnItemClickListener(this);
        getView().findViewById(R.id.fg_home_menu3).setOnClickListener(this);
        getView().findViewById(R.id.header_left_btn).setOnClickListener(this);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position==0)return;
        FgSkuList fg = new FgSkuList();
        Bundle bundle = new Bundle();
        bundle.putString(FgSkuList.KEY_CITY_ID,dataList.get(position-1).cityId);
        startFragment(fg,bundle);
    }

    @Override
    public void onClick(View v) {
        MLog.e("onClick="+v);
        switch (v.getId()) {
            case R.id.header_left_btn:
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.fg_home_menu1://中文接送机

                break;
            case R.id.fg_home_menu2://按天包车

                break;
            case R.id.fg_home_menu3://单次接送
                MLog.e("FgSingle");
                startFragment(new FgSingle());
                break;

        }
    }


}
