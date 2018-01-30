package com.hugboga.custom.widget.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.QueryCityActivity;
import com.hugboga.custom.adapter.SearchAdapter;
import com.hugboga.custom.adapter.SearchAfterAdapter;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.SearchUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.MultipleTextViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索历史内容
 * Created by HONGBO on 2017/12/16 10:19.
 */

public class SearchHistoryView extends LinearLayout {

    @BindView(R.id.searchHistoryTagLayout)
    RelativeLayout searchHistoryTagLayout; //历史快速选择标签区域
    @BindView(R.id.history_layout)
    LinearLayout historyLayout; //历史记录容器
    @BindView(R.id.searchHistoryOld)
    MultipleTextViewGroup searchHistoryOld; //历史搜索记录标签部分
    @BindView(R.id.searchHistoryHotitem)
    MultipleTextViewGroup searchHistoryHotitem; //热门搜索标签部分
    @BindView(R.id.searchListLayout)
    FrameLayout searchListLayout; //列表容器
    @BindView(R.id.searchHistoryFirstList)
    RecyclerView searchHistoryFirstList;
    @BindView(R.id.searchHistoryAfterList)
    RecyclerView searchHistoryAfterList;

    List<SearchGroupBean> listAll;
    List<SearchGroupBean> listfirst;
    List<SearchGroupBean> listAfter;

    SearchAdapter searchAdapter;
    SearchAfterAdapter searchAfterAdapter;

    QueryCityActivity mActivity;

    public static boolean queryGuideRun = false; //是否真该查询司导
    public static boolean queryLineRun = false; //是否真该查询线路
    boolean stopQequest = false;

    public SearchHistoryView(Context context) {
        this(context, null);
    }

    public SearchHistoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.search_history_layout, this);
        ButterKnife.bind(this, view);
    }

    public void clearAdapter() {
        if (searchAdapter != null) {
            searchAdapter.removeModels();
        }
        if (searchAfterAdapter != null) {
            searchAfterAdapter.removeModels();
        }
    }

    public void init(QueryCityActivity activity) {
        this.mActivity = activity;
        initSearchAdapter();
        initSearchAfterAdapter();
        initResetUI(); //默认展示历史标签部分，隐藏结果

        if (searchAdapter != null) {
            searchAdapter.removeModels();
        }
        if (searchAfterAdapter != null) {
            searchAfterAdapter.removeModels();
        }
    }

    public void searchText(String searchStr) {
        if (!TextUtils.isEmpty(searchStr)) {
            // 埋点-增加显示搜索结果埋点
            if (getContext() != null && (getContext() instanceof QueryCityActivity)
                    && (searchAdapter.getItemCount() == 0 || searchListLayout.getVisibility() == View.GONE)) {
                ((QueryCityActivity) getContext()).addQueryResultView("搜索结果",
                        "搜索结果", "搜索");
            }
            showUI();
            List<SearchGroupBean> list = CityUtils.search(mActivity, searchStr);
            clearAdapter();
            showLocalResult(list, searchStr); //展示关联词结果
        } else {
            initResetUI();
        }
    }

    public void showResultQuery(final String searchStr) {
        if (queryGuideRun || queryLineRun) {
            return;
        }
        showMoreQuery(searchStr);
    }

    private void showMoreQuery(String searchStr) {
        if (stopQequest) {
            stopQequest = false;
            return;
        }
        showAfterUI(searchStr);
        if (!TextUtils.isEmpty(searchStr)) {
            SearchUtils.addCityHistorySearch(searchStr);
        }
        SearchUtils.isHistory = false;
        SearchUtils.isRecommend = false;
    }

    /**
     * 重置为初始化搜索状态
     */
    private void initResetUI() {
        searchHistoryTagLayout.setVisibility(VISIBLE);
        searchListLayout.setVisibility(View.GONE);
        searchHistoryFirstList.setVisibility(GONE);
        searchHistoryAfterList.setVisibility(GONE);
        changHistory(); //重新展示历史标签
    }

    /**
     * 展示本地关联词结果
     */
    private void showUI() {
        searchHistoryTagLayout.setVisibility(GONE);
        searchListLayout.setVisibility(View.VISIBLE);
        searchHistoryFirstList.setVisibility(VISIBLE);
        searchHistoryAfterList.setVisibility(GONE);
    }

    /**
     * 标签搜索
     *
     * @param searchStr
     */
    private void showAfterUI2(String searchStr, boolean isHotLabel) {
        mActivity.hideSoft(searchStr);
        searchHistoryTagLayout.setVisibility(GONE);
        searchListLayout.setVisibility(View.VISIBLE);
        searchHistoryFirstList.setVisibility(GONE);
        searchHistoryAfterList.setVisibility(VISIBLE);
        searchAdapter.removeModels();
        searchAfterAdapter.removeModels();
        List<SearchGroupBean> list = CityUtils.search(mActivity, searchStr);
        addAfterSearchDestinationModel(list, searchStr);
        SearchUtils.isHistory = !isHotLabel;
        SearchUtils.isRecommend = isHotLabel;
    }

    private void showAfterUI(String msg) {
        searchAdapter.removeModels();
        searchAfterAdapter.removeModels();
        searchHistoryAfterList.setVisibility(VISIBLE);
        searchHistoryFirstList.setVisibility(GONE);
        addAfterSearchDestinationModel(listAll, msg);
    }

    private void showLocalResult(List<SearchGroupBean> listAll, String searchStr) {
        this.listAll = listAll;
        if (listAll.size() >= 5) {
            listfirst = listAll.subList(0, 5);
        } else {
            listfirst = listAll;
        }
        if (listAll.size() >= 3) {
            listAfter = listAll.subList(0, 3);
        } else {
            listAfter = listAll;
        }
        addSearchDestinationModel(listAll, searchStr);
    }

    /**
     * 展示所有关于搜索词的结果
     */
    public void showAfterAllData() {
        searchHistoryAfterList.setVisibility(VISIBLE);
        searchHistoryFirstList.setVisibility(GONE);
        if (searchHistoryAfterList.getChildCount() > 0) {
            searchHistoryAfterList.removeAllViews();
        }
        searchAfterAdapter.showAllData();
    }

    /**
     * 重新添加搜索内容搜索结果
     *
     * @param list
     * @param searchStr
     */
    public void addSearchDestinationModel(List<SearchGroupBean> list, String searchStr) {
        if (searchHistoryFirstList.getChildCount() > 0) {
            searchHistoryFirstList.removeAllViews();
        }
        searchAdapter.addSearchDestinationModel(mActivity, list, searchStr);
    }

    /**
     * 重新添加搜索词搜索结果
     *
     * @param list
     * @param keyword
     */
    public void addAfterSearchDestinationModel(List<SearchGroupBean> list, String keyword) {
        if (searchHistoryAfterList.getChildCount() > 0) {
            searchHistoryAfterList.removeAllViews();
        }
        searchAfterAdapter.addAfterSearchDestinationModel(mActivity, list, keyword);
    }

    public void initSearchAdapter() {
        searchAdapter = new SearchAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(mActivity);
        layoutManager.setOrientation(WrapContentLinearLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        searchHistoryFirstList.setLayoutManager(layoutManager);
        searchHistoryFirstList.setHasFixedSize(true);
        searchHistoryFirstList.setAdapter(searchAdapter);
        searchHistoryFirstList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mActivity.hideSoftInput();
                    mActivity.removeQuery();
                    stopQequest = true;
                }
            }

//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                //滑动页面则关闭键盘
//                if (dy != 0) {
//                    mActivity.hideSoftInput();
//                    mActivity.removeQuery();
//                }
//            }
        });
    }

    public void initSearchAfterAdapter() {
        searchAfterAdapter = new SearchAfterAdapter();
        WrapContentLinearLayoutManager layoutManager1 = new WrapContentLinearLayoutManager(mActivity);
        layoutManager1.setOrientation(WrapContentLinearLayoutManager.VERTICAL);
        layoutManager1.setAutoMeasureEnabled(true);
        searchHistoryAfterList.setLayoutManager(layoutManager1);
        searchHistoryAfterList.setHasFixedSize(true);
        searchHistoryAfterList.setAdapter(searchAfterAdapter);
        searchHistoryAfterList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mActivity.hideSoftInput();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滑动页面则关闭键盘
                if (dy != 0) {
                    mActivity.hideSoftInput();
                    mActivity.removeQuery();
                }
            }
        });
    }

    /**
     * 展示热门搜索标签部分结果展示
     *
     * @param dataList
     */
    public void showHistorySearchResult(final ArrayList<String> dataList) {
        if (dataList != null && dataList.size() > 0) {
            //通过接口
            searchHistoryTagLayout.setVisibility(VISIBLE);
            searchHistoryHotitem.setVisibility(VISIBLE);
            searchHistoryHotitem.setTextViews(dataList);
            searchHistoryHotitem.setOnMultipleTVItemClickListener(new MultipleTextViewGroup.OnMultipleTVItemClickListener() {
                @Override
                public void onMultipleTVItemClick(View view, int position) {
                    String searchStr = dataList.get(position);
                    //热门搜索过的标签需要添加到历史记录
                    SearchUtils.addCityHistorySearch(searchStr);
                    showAfterUI2(searchStr, true); //展示After列表
                }
            });
        } else {
            searchHistoryHotitem.setVisibility(GONE);
        }
    }

    /**
     * 本地查询并展示历史搜索标签
     */
    private void changHistory() {
        //倒序展示
        final List<String> dataList = showHistory(SearchUtils.getSaveHistorySearch());
        if (dataList != null && dataList.size() > 0) {
            historyLayout.setVisibility(VISIBLE);
            searchHistoryOld.setTextViews(dataList);
            searchHistoryOld.setOnMultipleTVItemClickListener(new MultipleTextViewGroup.OnMultipleTVItemClickListener() {
                @Override
                public void onMultipleTVItemClick(View view, int position) {
                    //历史标签点击事件
                    showAfterUI2(dataList.get(position), false); //展示After列表
                }
            });
        } else {
            historyLayout.setVisibility(GONE);
        }
    }

    /**
     * 对历史搜索变迁进行倒序排序
     */
    private List<String> showHistory(List<String> history) {
        List<String> showHistory = new ArrayList<>();
        if (history != null && history.size() > 0) {
            for (int i = history.size() - 1; i >= 0; i--) {
                showHistory.add(history.get(i));
            }
        }
        return showHistory;
    }

    @OnClick({R.id.searchHistoryRemove, R.id.searchHistoryTagLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchHistoryRemove:
                SearchUtils.clearHistorySearch();
                changHistory(); //删除历史标签后重新查询展示历史标签
                break;
            case R.id.searchHistoryTagLayout:
                //屏蔽点击项
                break;
        }
    }
}
