package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.models.TravelAddItemModel;
import com.hugboga.custom.models.TravelItemModel;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.widget.TravelAddItemView;
import com.hugboga.custom.widget.charter.TravelItemView;

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

    public static class TravelListAdapter extends EpoxyAdapter implements TravelItemView.OnEditClickListener, TravelAddItemView.OnAddTravelListener{
        private CharterDataUtils charterDataUtils;
        private TravelAddItemModel travelAddItemModel;
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
                travelItemModel.setPosition(i);
                travelItemModel.setOnEditClickListener(this);
                addModel(travelItemModel);
            }
        }

        public void insertAddModel() {
            if (travelAddItemModel == null) {
                travelAddItemModel = new TravelAddItemModel();
                travelAddItemModel.setOnAddTravelListener(this);
                addModel(travelAddItemModel);
            }
        }

        @Override
        public void onEditClick(int position) {

        }

        @Override
        public void onDelClick(int position) {

        }

        @Override
        public void onAddTravel() {

        }
    }
}
