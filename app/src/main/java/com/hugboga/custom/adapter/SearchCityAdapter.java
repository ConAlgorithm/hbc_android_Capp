package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public SearchCityVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_city_item, parent, false);
        return new SearchCityVH(view);
    }

    @Override
    public void onBindViewHolder(final SearchCityVH holder, final int position) {
        holder.init(flag, data.get(position), position, middleLineShow, cityParams);
        holder.setOnItemClickListener(new SearchCityVH.OnItemClickListener() {
            @Override
            public void onItemClick(SearchGroupBean bean) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(bean, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(SearchGroupBean bean, int position);
    }

    public void setData(List<SearchGroupBean> data) {
        this.data = data;
    }
}
