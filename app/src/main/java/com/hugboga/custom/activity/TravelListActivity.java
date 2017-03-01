package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.models.TravelItemModel;
import com.hugboga.custom.utils.CharterDataUtils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/2/28.
 */

public class TravelListActivity extends BaseActivity {

    @Bind(R.id.travel_list_recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_list);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        TravelListAdapter adapter = new TravelListAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public static class TravelListAdapter extends EpoxyAdapter {
        private CharterDataUtils charterDataUtils;
        public TravelListAdapter() {
            charterDataUtils = CharterDataUtils.getInstance();
            setData();
        }

        public void setData() {
            if (charterDataUtils == null || charterDataUtils.params == null || charterDataUtils.params.chooseDateBean == null) {
                return;
            }
            final int dayNums = charterDataUtils.params.chooseDateBean.dayNums;
            for (int i = 0; i < dayNums; i++) {
                TravelItemModel travelItemModel = new TravelItemModel();
                travelItemModel.setPosition(0);
                addModel(travelItemModel);
            }
        }
    }
}
