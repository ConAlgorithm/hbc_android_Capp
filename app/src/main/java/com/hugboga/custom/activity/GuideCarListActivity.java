package com.hugboga.custom.activity;

import android.os.Bundle;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.GuideCarListAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.request.RequestCars;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/10/8.
 */
public class GuideCarListActivity extends BaseActivity{
    public static final String PARAMS_GUIDE_CAR_ID = "guideCarId";

    @Bind(R.id.guide_car_list_listview)
    ZListView listView;

    private String guideId;
    private String guideCarId;
    private GuideCarListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            guideId = savedInstanceState.getString(Constants.PARAMS_ID);
            guideCarId = savedInstanceState.getString(PARAMS_GUIDE_CAR_ID);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                guideId = bundle.getString(Constants.PARAMS_ID);
                guideCarId = bundle.getString(PARAMS_GUIDE_CAR_ID);
            }
        }

        setContentView(R.layout.activity_guide_car_list);
        ButterKnife.bind(this);
        initDefaultTitleBar();

        initView();
        requestData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (guideId != null) {
            outState.putString(Constants.PARAMS_ID, guideId);
            outState.putString(PARAMS_GUIDE_CAR_ID, guideCarId);
        }
    }

    private Callback.Cancelable loadData(int pageIndex) {
        return requestData(new RequestCars(this, guideId, guideCarId, Constants.DEFAULT_PAGESIZE, pageIndex));
    }

    private void requestData() {
        if (adapter != null) {
            adapter = null;
        }
        loadData(0);
    }

    private void initView() {
        fgTitle.setText("车辆信息");
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);
    }

    ZListView.OnRefreshListener onRefreshListener = new ZListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (adapter != null) {
                adapter = null;
            }
            loadData(0);
        }
    };

    ZListView.OnLoadListener onLoadListener = new ZListView.OnLoadListener() {
        @Override
        public void onLoad() {
            if (adapter.getCount() > 0) {
                loadData(adapter == null ? 0 : adapter.getCount());
            }
        }
    };

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCars) {
            RequestCars request = (RequestCars) _request;
            ArrayList<GuideCarBean> list = request.getData();
            if (list != null) {
                if (adapter == null) {
                    adapter = new GuideCarListAdapter(this);
                    listView.setAdapter(adapter);
                    adapter.setList(list);
                } else {
                    adapter.addList(list);
                }
            }
            if (list != null && list.size() < Constants.DEFAULT_PAGESIZE) {
                listView.onLoadCompleteNone();
            } else {
                listView.onLoadComplete();
            }
            listView.onRefreshComplete();
        }
    }
}
