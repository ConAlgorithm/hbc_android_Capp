package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.GuideFilterTagAdapter;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

@ContentView(R.layout.fragment_guide_filiter_sort)
public class GuideFilterSortFragment extends BaseFragment implements AbsListView.OnItemClickListener{

    @Bind(R.id.guide_filter_listview)
    ListView listView;

    private ArrayList<SortTypeBean> sortTypeList;
    private GuideFilterTagAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView() {
        sortTypeList = new ArrayList<>();
        sortTypeList.add(new GuideFilterSortFragment.SortTypeBean(true, 0, "默认排序", "排序"));
        sortTypeList.add(new GuideFilterSortFragment.SortTypeBean(false, 1, "星级从高到低", "星级"));
        sortTypeList.add(new GuideFilterSortFragment.SortTypeBean(false, 2, "评价从多到少", "评价"));
        sortTypeList.add(new GuideFilterSortFragment.SortTypeBean(false, 3, "接单数从多到少", "接单数"));
        adapter = new GuideFilterTagAdapter(sortTypeList);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        updateSelectedStauts(position);
        EventBus.getDefault().post(new EventAction(EventType.GUIDE_FILTER_SORT, sortTypeList.get(position)));
    }

    @OnClick({R.id.guide_filter_outside_view})
    public void onOutsideClick() {
        EventBus.getDefault().post(new EventAction(EventType.FILTER_CLOSE));
    }

    private void updateSelectedStauts(int index){
        if (sortTypeList == null) {
            return;
        }
        int size = sortTypeList.size();
        for(int i = 0; i < size; i++){
            SortTypeBean data = sortTypeList.get(i);
            data.selected = i == index;
        }
        adapter.notifyDataSetChanged();
    }

    public void resetFilter() {
        updateSelectedStauts(0);
    }

    public static class SortTypeBean implements Serializable{
        public int type;//0-默认排序;1-按星级排序;2-按评分排序;3-按单数排序
        public String typeStr;
        public String typeFilterStr;
        public boolean selected;

        public SortTypeBean(boolean selected, int type, String typeStr, String typeFilterStr) {
            this.selected = selected;
            this.type = type;
            this.typeStr = typeStr;
            this.typeFilterStr = typeFilterStr;
        }
    }
}
