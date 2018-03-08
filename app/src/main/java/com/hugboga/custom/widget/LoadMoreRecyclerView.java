package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.LoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;

import java.util.List;

public class LoadMoreRecyclerView extends RecyclerView {
    private boolean isLoadingData = false;
    private boolean isNoMore = false;
    private int mLoadingMoreProgressStyle = ProgressStyle.SysProgress;
    private WrapAdapter mWrapAdapter;
    private LoadingListener mLoadingListener;
    private static final int TYPE_FOOTER = 10001;
    private View mFootView;

    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LoadingMoreFooter footView = new LoadingMoreFooter(getContext());
        footView.setProgressStyle(mLoadingMoreProgressStyle);
        mFootView = footView;
        mFootView.setVisibility(GONE);
    }

    private boolean isReservedItemViewType(int itemViewType) {
        if(itemViewType == TYPE_FOOTER) {
            return true;
        } else {
            return false;
        }
    }

    public void setFootView(final View view) {
        mFootView = view;
    }

    public void loadMoreComplete() {
        isLoadingData = false;
        if (mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootView).setState(LoadingMoreFooter.STATE_COMPLETE);
        } else {
            mFootView.setVisibility(View.GONE);
        }
    }

    public void setNoMore(boolean noMore){
        isLoadingData = false;
        isNoMore = noMore;
        if (mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootView).setState(isNoMore ? LoadingMoreFooter.STATE_NOMORE : LoadingMoreFooter.STATE_COMPLETE);
        } else {
            mFootView.setVisibility(View.GONE);
        }
    }

    public void reset() {
        setNoMore(false);
        loadMoreComplete();
    }

    public void setLoadingMoreProgressStyle(int style) {
        mLoadingMoreProgressStyle = style;
        if (mFootView instanceof LoadingMoreFooter) {
            ((LoadingMoreFooter) mFootView).setProgressStyle(style);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
    }

    //避免用户自己调用getAdapter() 引起的ClassCastException
    @Override
    public Adapter getAdapter() {
        if(mWrapAdapter != null)
            return mWrapAdapter.getOriginalAdapter();
        else
            return null;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            if (layoutManager.getChildCount() > 0 && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 && layoutManager.getItemCount() > layoutManager.getChildCount() && !isNoMore) {
                isLoadingData = true;
                if (mFootView instanceof LoadingMoreFooter) {
                    ((LoadingMoreFooter) mFootView).setState(LoadingMoreFooter.STATE_LOADING);
                } else {
                    mFootView.setVisibility(View.VISIBLE);
                }
                mLoadingListener.onLoadMore();
            }
        }
    }

    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter adapter;

        public WrapAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        public RecyclerView.Adapter getOriginalAdapter(){
            return this.adapter;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_FOOTER) {
                return new WrapAdapter.SimpleViewHolder(mFootView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (adapter != null && position < adapter.getItemCount()) {
                adapter.onBindViewHolder(holder, position);
            }
        }

        // some times we need to override this
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
            if (adapter != null && position < adapter.getItemCount()) {
                if (payloads.isEmpty()) {
                    adapter.onBindViewHolder(holder, position);
                } else {
                    adapter.onBindViewHolder(holder, position, payloads);
                }
            }
        }

        @Override
        public int getItemCount() {
            return adapter != null ? adapter.getItemCount() + 1 : 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            }
            if (adapter != null && position < adapter.getItemCount()) {
                int type =  adapter.getItemViewType(position);
                if(isReservedItemViewType(type)) {
                    throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 " );
                }
                return type;
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null) {
                if (position < adapter.getItemCount()) {
                    return adapter.getItemId(position);
                } else {
                    return 100000;
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public interface LoadingListener {
        void onLoadMore();
    }
}