package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.adapter.viewholder.SearchCityVH;
import com.hugboga.custom.data.bean.SearchGroupBean;

import java.util.List;

/**
 * 搜索城市列表
 * Created by HONGBO on 2017/12/15 15:39.
 */

public class SearchCityAdapter extends RecyclerView.Adapter<SearchCityVH> {

    Context mContext;
    int flag;

    List<SearchGroupBean> data; //数据

    boolean middleLineShow;
    boolean isFilter;
    CityActivity.Params cityParams;

    OnItemClickListener onItemClickListener;

    public SearchCityAdapter(Context mContext, int flag) {
        this.mContext = mContext;
        this.flag = flag;
    }

    public void setCityParams(CityActivity.Params cityParams) {
        this.cityParams = cityParams;
        notifyDataSetChanged();
    }

    public void setMiddleLineShow(boolean middleLineShow) {
        this.middleLineShow = middleLineShow;
    }

    public void isFilter(boolean isFilter) {
        this.isFilter = isFilter;
    }

    @Override
    public SearchCityVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchCityVH(LayoutInflater.from(mContext).inflate(R.layout.search_city_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchCityVH holder, int position) {
        holder.init(flag, data.get(position), position, middleLineShow, isFilter, cityParams);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    public void setData(List<SearchGroupBean> data) {
        this.data = data;
    }
}
