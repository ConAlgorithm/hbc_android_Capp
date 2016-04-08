package com.huangbaoche.hbcframe.widget.recycler;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.page.IPageList;
import com.huangbaoche.hbcframe.page.Page;

import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class ZListPageView extends ZListRecyclerView implements IPageList {

    ZSwipeRefreshLayout zSwipeRefreshLayout;
    ZBaseAdapter adapter;
    BaseRequest requestData;
    ZBaseAdapter.OnItemClickListener onItemClickListener;
    View emptyLayout;
    RelativeLayout networkErrorLayout;
    Page page;
    ZListHttpUtils zListHttpUtils;
    private boolean isLoading = false;

    public ZListPageView(Context context) {
        super(context);
        addListener();
    }

    public ZListPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addListener();
    }

    public ZListPageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addListener();
    }

    public void setOnItemClickListener(ZBaseAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setRequestData(BaseRequest requestData) {
        this.requestData = requestData;
    }

    public void setNetworkErrorLayout(RelativeLayout networkErrorLayout) {
        this.networkErrorLayout = networkErrorLayout;
    }

    public void setAdapter(ZBaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void setzSwipeRefreshLayout(ZSwipeRefreshLayout zSwipeRefreshLayout) {
        this.zSwipeRefreshLayout = zSwipeRefreshLayout;
        addSwipeRefreshListener();
    }

    public void setEmptyLayout(View emptyLayout) {
        this.emptyLayout = emptyLayout;
    }

    private void addListener() {
        if (page == null) {
            page = new Page();
        }
        addLoadNextListener();
    }

    private void addLoadNextListener() {
        if (onLoadListener == null) {
            addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LayoutManager layoutManager = getLayoutManager();
                    int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();
                    if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                        showPageNext();
                    }
                }
            });
        }
    }

    private void addSwipeRefreshListener() {
        if (zSwipeRefreshLayout != null) {
            zSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    isLoading = false;
                    showPageFirst();
                }
            });
        }
    }

    @Override
    public void showPageFirst() {
        if (!isLoading) {
            loadData(Page.pageType.FIRST);
        } else {
            zSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showPageNext() {
        if (!isLoading) {
            loadData(Page.pageType.NEXT);
        }
    }

    @Override
    protected void checkIfEmpty() {
        if (emptyLayout == null || getAdapter() == null) {
            return;
        }
        if (adapter.getDatas() == null) {
            emptyLayout.setVisibility(View.VISIBLE);
            return;
        }
        if (adapter.getDatas().size() < adapter.getItemCount()) {
            if (getAdapter().getItemCount() > 2) {
                emptyLayout.setVisibility(View.GONE);
            } else {
                emptyLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (getAdapter().getItemCount() > 0) {
                emptyLayout.setVisibility(View.GONE);
            } else {
                emptyLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadData(Page.pageType pageType) {
        isLoading = true; //正在加载中
        if (zListHttpUtils == null) {
            zListHttpUtils = new ZListHttpUtils(getContext(), this, requestData, pageLoadListener, adapter, zSwipeRefreshLayout);
//            zListHttpUtils.setNetworkLayout(networkErrorLayout);
//            zListHttpUtils.setIsShowLoading(); //RecyclerView缺陷
        }
        int pageIndex = 0;
        if (pageType == Page.pageType.FIRST) {
            page.initFirstPage();
            adapter.removeAll(); //清除现有数据
        }
        pageIndex = page.getNextPage();
        zListHttpUtils.exec(String.valueOf(page.getPageSize()), String.valueOf(pageIndex));
    }

    HttpRequestListener pageLoadListener = new HttpRequestListener() {

        AnimationAdapter myAdapter;

        @Override
        public void onDataRequestSucceed(BaseRequest request) {
            isLoading = false;
            if (page.getPageIndex() < 0) {
                adapter.removeAll(); //清楚现有数据
                adapter.setOnItemClickListener(onItemClickListener);
                myAdapter = new AlphaInAnimationAdapter(adapter);
                setEmptyView(emptyLayout);
                setAdapter(myAdapter);
            }
            Object[] obj = null;
            if (request.getData() != null) {
                obj = (Object[]) request.getData();
            }
            if (obj != null && obj[0] != null && !obj[0].toString().isEmpty()) {
                adapter.setDataCount(Integer.parseInt(obj[0].toString()));
            }
            List datas = null;
            if (obj != null && obj[1] != null) {
                datas = (List) obj[1];
            }
            if (datas != null && datas.size() > 0) {
                page.setPageIndex();
                adapter.addDatas(datas);
            }
            if (myAdapter != null) {
                myAdapter.notifyDataSetChanged();
            }
            if (zSwipeRefreshLayout != null) {
                zSwipeRefreshLayout.setRefreshing(false);
            }
            //通知界面层
            if (noticeViewTask != null) {
                noticeViewTask.notice(request.getData());
            }
        }

        @Override
        public void onDataRequestCancel(BaseRequest request) {
            isLoading = false;
            if (zSwipeRefreshLayout != null) {
                zSwipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            isLoading = false;
            if (zSwipeRefreshLayout != null) {
                zSwipeRefreshLayout.setRefreshing(false);
            }
            ErrorHandler handler = new ErrorHandler((Activity) getContext(), this);
            handler.onDataRequestError(errorInfo, request);
            if (noticeViewTask != null) {
                noticeViewTask.error(errorInfo,request);
            }
        }

    };

    /**
     * 界面通知工具
     */
    NoticeViewTask noticeViewTask;

    public interface NoticeViewTask {
        void notice(Object object);
        void error(ExceptionInfo errorInfo, BaseRequest request);

    }

    public void setNoticeViewTask(NoticeViewTask noticeViewTask) {
        this.noticeViewTask = noticeViewTask;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
