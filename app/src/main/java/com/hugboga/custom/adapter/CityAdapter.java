package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.CityListVH;
import com.hugboga.custom.adapter.viewholder.CityWhatVH;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.ServiceConfigVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 目的地城市列表
 * Created by HONGBO on 2017/11/27 16:50.
 */

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<DestinationGoodsVo> data = new ArrayList<>();
    private List<ServiceConfigVo> dailyServiceConfig;

    private static final int TYPE_SKU = 1; //玩法显示
    private static final int TYPE_CONFIG = 2; //下单配置显示

    public CityAdapter(Context context, List<DestinationGoodsVo> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SKU) {
            return new CityListVH(mContext, LayoutInflater.from(mContext).inflate(R.layout.city_item, parent, false));
        } else {
            return new CityWhatVH(mContext, LayoutInflater.from(mContext).inflate(R.layout.city_item_what_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CityListVH) {
            ((CityListVH) holder).init(data.get(position));
        } else if (holder instanceof CityWhatVH) {
            ((CityWhatVH) holder).init(dailyServiceConfig);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < data.size()) {
            return TYPE_SKU;
        } else {
            return TYPE_CONFIG;
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() + 1 : 1;
    }

    public void load(List<DestinationGoodsVo> datas) {
        this.data = datas;
        notifyDataSetChanged();
    }

    public void addData(List<DestinationGoodsVo> datas) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(datas);
        notifyDataSetChanged();
    }

    public void setDailyServiceConfig(List<ServiceConfigVo> dailyServiceConfig) {
        this.dailyServiceConfig = dailyServiceConfig;
    }
}
