package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.CityListVH;

import java.util.List;

/**
 * 目的地城市列表
 * Created by HONGBO on 2017/11/27 16:50.
 */

public class CityAdapter extends RecyclerView.Adapter<CityListVH> {

    private Context mContext;
    private List data;

    public CityAdapter(Context context, List data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public CityListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityListVH(LayoutInflater.from(mContext).inflate(R.layout.city_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CityListVH holder, int position) {
        holder.init(position);
        holder.line.setVisibility(position == data.size() - 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
