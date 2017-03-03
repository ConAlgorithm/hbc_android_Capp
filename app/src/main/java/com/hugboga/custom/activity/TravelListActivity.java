package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.models.TravelAddItemModel;
import com.hugboga.custom.models.TravelItemModel;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.widget.TravelAddItemView;
import com.hugboga.custom.widget.charter.TravelItemView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

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
        TravelListAdapter adapter = new TravelListAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public static class TravelListAdapter extends EpoxyAdapter implements TravelItemView.OnEditClickListener, TravelAddItemView.OnAddTravelListener{

        private TravelListActivity activity;
        private CharterDataUtils charterDataUtils;
        private TravelAddItemModel travelAddItemModel;

        public TravelListAdapter(TravelListActivity activity) {
            this.activity = activity;
            charterDataUtils = CharterDataUtils.getInstance();
            setData();
        }

        public void setData() {
            if (charterDataUtils == null || charterDataUtils.chooseDateBean == null) {
                return;
            }
            final int dayNums = charterDataUtils.chooseDateBean.dayNums;
            for (int i = 0; i < dayNums; i++) {
                TravelItemModel travelItemModel = new TravelItemModel();
                travelItemModel.setPosition(i);
                travelItemModel.setOnEditClickListener(this);
                addModel(travelItemModel);
            }
            ArrayList<CityRouteBean.CityRouteScope> travelList = charterDataUtils.travelList;
            if (dayNums == travelList.size() && charterDataUtils.isSelectedSend) {//最后一天选了送机，不显示添加
                updateAddModel(false);
            } else {
                updateAddModel(true);
            }
        }

        public void updateAddModel(boolean isShow) {
            if (travelAddItemModel == null) {
                travelAddItemModel = new TravelAddItemModel();
                travelAddItemModel.setOnAddTravelListener(this);
                addModel(travelAddItemModel);
            }
            showModel(travelAddItemModel, isShow);
            notifyModelChanged(travelAddItemModel);
        }

        @Override
        public void onEditClick(int position) {
            EventBus.getDefault().post(new EventAction(EventType.CHARTER_LIST_REFRESH, position + 1));
            activity.finish();
        }

        @Override
        public void onDelClick(int position) {

        }

        @Override
        public void onAddTravel() {

        }
    }
}
