package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.CityListVH;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 目的地城市列表
 * Created by HONGBO on 2017/11/27 16:50.
 */

public class CityAdapter extends RecyclerView.Adapter<CityListVH> {

    private Context mContext;
    private List<DestinationGoodsVo> data;

    public CityAdapter(Context context, List<DestinationGoodsVo> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public CityListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityListVH(mContext, LayoutInflater.from(mContext).inflate(R.layout.city_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CityListVH holder, int position) {
        holder.init(data.get(position));
        holder.line.setVisibility(position == data.size() - 1 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return data.size();
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
}
