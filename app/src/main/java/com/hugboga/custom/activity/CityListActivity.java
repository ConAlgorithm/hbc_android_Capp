package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/4/14.
 */
public class CityListActivity extends BaseActivity {

    @Bind(R.id.city_list_recyclerview)
    RecyclerView recyclerView;

    private CityListActivity.Params paramsData;

    public enum CityHomeType {
        CITY, ROUTE, COUNTRY
    }

    public static class Params implements Serializable {
        public int id;
        public CityListActivity.CityHomeType cityHomeType;
        public String titleName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (CityListActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (CityListActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        initView();
        //RequestCityHomeList
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    public void initView() {
        initDefaultTitleBar();
    }
}
