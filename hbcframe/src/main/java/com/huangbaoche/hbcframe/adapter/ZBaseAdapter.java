package com.huangbaoche.hbcframe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class ZBaseAdapter<T, V> extends RecyclerView.Adapter<ZBaseViewHolder> {

    private static final int TYPE_HEADER = 0x01;
    private static final int TYPE_ITEM = 0x02;
    private static final int TYPE_FOOTER = 0x03;

    protected Context context;
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;
    protected List<T> datas;
    protected int dataCount;

    private View footerView = null;

    public ZBaseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ZBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (footerView != null) {
            ZBaseViewHolder viewHolder = null;
            switch (viewType) {
                case TYPE_ITEM:
                    viewHolder = getVH(View.inflate(context, initResource(), null));
                    break;
                case TYPE_FOOTER:
                    viewHolder = new HeaderOrFooterHolder(footerView);
                    break;
            }
            return viewHolder;
        } else {
            View view = View.inflate(context, initResource(), null);
            return getVH(view);
        }
    }

    @Override
    public void onBindViewHolder(final ZBaseViewHolder holder, final int position) {
        if (position < getListCount()) {
            //设置点击事件
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }

            if (onItemLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemLongClickListener.onItemLongClick(holder.itemView,position);
                        return true;
                    }
                });
            }

            getView(position, (V) holder);
        }
    }

    protected abstract int initResource();

    protected abstract ZBaseViewHolder getVH(View view);

    protected abstract void getView(int position, V vh);

    @Override
    public int getItemCount() {
        int count = getListCount();
        if (footerView != null && count > 0) {
            count++;
        }
        return count;
    }

    public int getListCount() {
        return datas == null ? 0 : datas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void addDatas(List<T> data) {
        if (datas == null) {
            datas = new ArrayList<T>();
        }
        datas.addAll(data);
        notifyDataSetChanged();
    }

    public void removeDatas(int position){
        datas.remove(position);
        notifyItemRemoved(position);
    }

    /*
    有些要求需要在刷新第一页数据的时候清空列表或者adapter数据
    这时候，只需要在请求网络的第一页数据时候，调用此方法，即可清空数据
     */
    public void removeAll() {
        if (datas != null) {
            datas.removeAll(datas);
        }
        notifyDataSetChanged();
    }

    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    public int getDataCount() {
        return dataCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getListCount()) {
            return TYPE_ITEM;
        } else if (footerView != null) {
            return TYPE_FOOTER;
        }
        return -1;
    }

    public void addFooterView(View view) {
        this.footerView = view;
        notifyDataSetChanged();
    }

    class HeaderOrFooterHolder extends ZBaseViewHolder {
        public HeaderOrFooterHolder(View view) {
            super(view);
        }
    }
}
