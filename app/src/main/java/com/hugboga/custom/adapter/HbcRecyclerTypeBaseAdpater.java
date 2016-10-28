package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.widget.HbcViewBehavior;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/28.
 */
public abstract class HbcRecyclerTypeBaseAdpater<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;

    private ArrayList<View> mHeaderViewInfos = null;
    private ArrayList<View> mFooterViewInfos = null;

    protected List<T> datas;
    private int curretPosition = -1;

    private OnItemClickListener onItemClickListener;

    private int typeCount = 1;

    public HbcRecyclerTypeBaseAdpater(Context context,int typeCount) {
        this.mContext = context;
        this.typeCount = typeCount;
        this.mHeaderViewInfos = new ArrayList<View>();
        this.mFooterViewInfos = new ArrayList<View>();
        this.datas = new ArrayList<T>();
    }

    public void addDatas(List<T> _data) {
        addDatas(_data, false);
    }

    public void addDatas(List<T> _data, boolean isNextPage) {
        if (_data == null) {
            return;
        }
        int lastCount = getItemCount();
        if (!isNextPage) {
            clearData();
        }
        datas.addAll(_data);
        if (isNextPage) {
            this.notifyItemRangeInserted(lastCount, lastCount + _data.size());
        } else {
            this.notifyDataSetChanged();
        }
    }

    public List<T> getDatas() {
        return datas;
    }

    public void clearData() {
        if (this.datas != null) {
            this.datas.clear();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int position = curretPosition;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_HEADER:
                HeaderOrFooterHolder headerHolder = new HeaderOrFooterHolder(mHeaderViewInfos.get(position));
                viewHolder = headerHolder;
                break;
            case TYPE_FOOTER:
                final int footerPosition = position - getListCount() - getHeadersCount();
                viewHolder = new HeaderOrFooterHolder(mFooterViewInfos.get(footerPosition));
                break;
            default:
                viewHolder =  new BaseItemHolder(getItemView(position-getHeadersCount()));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int _position) {
        if (_position >= getHeadersCount() && _position < getListCount() + getHeadersCount() && holder instanceof HbcRecyclerBaseAdapter.ItemHolder) {
            final BaseItemHolder itemHolder = (BaseItemHolder) holder;
            if (itemHolder == null) {
                return;
            }
            int position = _position - getHeadersCount();
            final Object itemData = datas.get(position);
            itemHolder.getItemView().update(itemData);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(itemHolder.itemView, _position, itemData);
                    }
                });
            }
        }
    }


    private static final int TYPE_HEADER = 0x01;
    private static final int TYPE_FOOTER = 0x02;

    @Override
    public int getItemViewType(int position) {
        curretPosition = position;
        if (position < getHeadersCount()) {
            return TYPE_HEADER;
        } else if (position < getListCount() + getHeadersCount()) {
            return getChildItemViewType(position);
        } else if (position < getItemCount()) {
            return TYPE_FOOTER;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return getListCount() + getHeadersCount() + getFootersCount();
    }

    public int getListCount() {
        return datas.size();
    }

    public int getHeadersCount() {
        return mHeaderViewInfos.size();
    }

    public int getFootersCount() {
        return mFooterViewInfos.size();
    }

    public void addHeaderView(View v) {
        mHeaderViewInfos.add(v);
        this.notifyDataSetChanged();
    }

    public void addFooterView(View v) {
        mFooterViewInfos.add(v);
        this.notifyDataSetChanged();
    }

    protected abstract View getItemView(int position);

    protected abstract int getChildItemViewType(int position);

    public Context getContext() {
        return mContext;
    }

    class BaseItemHolder extends RecyclerView.ViewHolder {

        private HbcViewBehavior viewBehavior;

        public BaseItemHolder(View view) {
            super(view);
            viewBehavior = (HbcViewBehavior) view;
        }

        public HbcViewBehavior getItemView() {
            return viewBehavior;
        }
    }

    class HeaderOrFooterHolder extends RecyclerView.ViewHolder {
        public HeaderOrFooterHolder(View view) {
            super(view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Object itemData);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
