package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityFilterTagAdapter;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/22.
 */
@ContentView(R.layout.city_filiter_list_fragment)
public class CityFilterDaysFragment extends BaseFragment implements AbsListView.OnItemClickListener {

    @Bind(R.id.city_filter_listview)
    ListView listView;

    CityFilterTagAdapter adapter;
    @Bind(R.id.filter_content_view)
    View filterContentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView() {
        adapter = new CityFilterTagAdapter(CityFilterTagAdapter.getDaysDatas());
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        filterContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventAction(EventType.CITY_FILTER_CLOSE,
                        null));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.updateSelectedStauts(position);
        EventBus.getDefault().post(new EventAction(EventType.CITY_FILTER_DAY,
                adapter.getItem(position)));
    }

}
