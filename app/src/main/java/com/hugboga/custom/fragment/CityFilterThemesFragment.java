package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityFilterThemeTagAdapter;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/22.
 */

public class CityFilterThemesFragment extends BaseFragment implements AbsListView.OnItemClickListener{

    @Bind(R.id.city_filter_theme_gridview)
    GridView gridView;
    List<CityHomeBean.GoodsThemes> goodsThemesList;

    CityFilterThemeTagAdapter adapter;

    @Bind(R.id.filter_content_view)
    View filterContentView;


    @Override
    public int getContentViewId() {
        return R.layout.city_filter_theme_fragment;
    }

    public void setDatas(List<CityHomeBean.GoodsThemes> goodsThemes){
        this.goodsThemesList = goodsThemes;
        updateGridViewSize();
        if(adapter!=null){
            adapter.setDatas(goodsThemes);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    protected void initView() {
        adapter = new CityFilterThemeTagAdapter(goodsThemesList);
        updateGridViewSize();
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(adapter);
        filterContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventAction(EventType.CITY_FILTER_CLOSE,
                        null));
            }
        });
    }

    private void updateGridViewSize(){
        if(goodsThemesList!=null && goodsThemesList.size()>10 && gridView!=null){
            gridView.getLayoutParams().height = UIUtils.screenFullHeight/2;
        }
    }

    public void resetData(){
        if(adapter!=null){
            adapter.updateSelectedStauts(0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.updateSelectedStauts(position);
        EventBus.getDefault().post(new EventAction(EventType.CITY_FILTER_THEME,
                adapter.getItem(position)));
    }

}
