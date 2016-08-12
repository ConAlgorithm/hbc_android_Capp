package com.huangbaoche.hbcframe.widget.recycler;

import android.content.Context;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;

import java.util.Map;

public class ZListHttpUtils extends HttpRequestUtils {

    Context context;
    ZListPageView zListPageView;
    BaseRequest request;
    HttpRequestListener pageListListener;
    ZBaseAdapter adapter;
    ZSwipeRefreshLayout zSwipeRefreshLayout;

    public ZListHttpUtils(Context context, ZListPageView zListPageView, BaseRequest request, HttpRequestListener pageListListener, ZBaseAdapter adapter, ZSwipeRefreshLayout zSwipeRefreshLayout) {
        this.context = context;
        this.zListPageView = zListPageView;
        this.request = request;
        this.pageListListener = pageListListener;
        this.adapter = adapter;
        this.zSwipeRefreshLayout = zSwipeRefreshLayout;
    }

    public void setRequest(BaseRequest request) {
        this.request = request;
    }

    public void exec(String limit, String offset) {
        int dataCount = adapter.getDataCount();
        int itemCount = adapter.getDatas() == null ? 0 : adapter.getDatas().size();
        if (offset.equals("0") || adapter.getDataCount() == 0 || dataCount > itemCount) {
            setPageParams(request.getDataMap(), limit, offset);
            request.needRebuild(); //重构请求参数
            HttpRequestUtils.request(context, request, pageListListener,false); //开始请求数据
        } else {
            zListPageView.setLoading(false);
            if (zSwipeRefreshLayout != null && zSwipeRefreshLayout.isRefreshing()) {
                zSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    /**
     * 设置分页参数
     *
     * @param dataMap
     * @param limit
     * @param offset
     */
    private void setPageParams(Map<String, Object> dataMap, String limit, String offset) {
        dataMap.put("limit", limit);
        dataMap.put("offset", offset);
    }
}
