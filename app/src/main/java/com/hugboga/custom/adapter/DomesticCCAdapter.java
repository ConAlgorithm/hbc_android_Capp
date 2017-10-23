package com.hugboga.custom.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.DomesticVH;

import java.util.List;

/**
 * 国内信用卡列表
 * Created by HONGBO on 2017/10/23 15:31.
 */

public class DomesticCCAdapter extends RecyclerView.Adapter<DomesticVH> {

    OnItemClickListener onItemClickListener;
    private List data;

    public DomesticCCAdapter(List data) {
        this.data = data;
    }

    @Override
    public DomesticVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DomesticVH(LayoutInflater.from(MyApplication.getAppContext()).inflate(R.layout.domestic_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(DomesticVH holder, final int position) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
        holder.init();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
