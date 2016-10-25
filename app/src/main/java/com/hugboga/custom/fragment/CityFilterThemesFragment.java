package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityFilterTagAdapter;
import com.hugboga.custom.adapter.CityFilterThemeTagAdapter;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/22.
 */
@ContentView(R.layout.city_filter_theme_fragment)
public class CityFilterThemesFragment extends BaseFragment implements AbsListView.OnItemClickListener{

    @Bind(R.id.city_filter_theme_gridview)
    GridView gridView;
    List<CityHomeBean.GoodsThemes> goodsThemesList;

    CityFilterThemeTagAdapter adapter;


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
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView() {
        adapter = new CityFilterThemeTagAdapter(goodsThemesList);
        updateGridViewSize();
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(adapter);
    }

    private void updateGridViewSize(){
        if(goodsThemesList!=null && goodsThemesList.size()>10){
            gridView.getLayoutParams().height = UIUtils.screenFullHeight/2;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.updateSelectedStauts(position);
        EventBus.getDefault().post(new EventAction(EventType.CITY_FILTER_THEME,
                new Integer(adapter.getItem(position).themeId)));
    }

}