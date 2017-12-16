package com.hugboga.custom.widget.search;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
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

/**
 * 搜索历史内容
 * Created by HONGBO on 2017/12/16 10:19.
 */

public class SearchHistoryView extends LinearLayout {

    @BindView(R.id.history)
    MultipleTextViewGroup history;
    @BindView(R.id.history_layout)
    LinearLayout historyLayout;
    @BindView(R.id.hotitem)
    MultipleTextViewGroup hotitem;
    @BindView(R.id.firstEnter)
    RelativeLayout firstEnter;
    @BindView(R.id.search_first_list)
    public RecyclerView search_first_list;
    @BindView(R.id.search_after_list)
    public RecyclerView search_after_list;
    @BindView(R.id.search_remove)
    ImageView search_remove; //删除历史搜索记录

    List<SearchGroupBean> listAll;
    List<SearchGroupBean> listfirst;
    List<SearchGroupBean> listAfter;
    public SearchAfterAdapter searchAfterAdapter;
    public SearchAdapter searchAdapter;

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

    public void showUI() {
        firstEnter.setVisibility(GONE);
        search_first_list.setVisibility(VISIBLE);
        search_after_list.setVisibility(GONE);
    }

    public void showResult(List<SearchGroupBean> listAll, String searchStr) {
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

    public void showEmptyResult() {
        firstEnter.setVisibility(VISIBLE);
        search_first_list.setVisibility(GONE);
        search_after_list.setVisibility(GONE);
        //每次关键字发生变化,都要重新展示历史记录
        changHistory();
    }

    public void showAfterUI(String msg) {
        searchAdapter.removeModels();
        searchAfterAdapter.removeModels();
        search_after_list.setVisibility(VISIBLE);
        search_first_list.setVisibility(GONE);
        addAfterSearchDestinationModel(listAll, msg);
    }

    public void init() {
        changHistory(); //展示历史标签

        firstEnter.setVisibility(VISIBLE);
        search_first_list.setVisibility(GONE);
        search_after_list.setVisibility(GONE);
        if (searchAdapter != null) {
            searchAdapter.removeModels();
        }
        if (searchAfterAdapter != null) {
            searchAfterAdapter.removeModels();
        }
        initSearchAdapter();
        initSearchAfterAdapter();

        search_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchUtils.clearHistorySearch();
                changHistory();
            }
        });
    }

    public void showResultService(final ArrayList<String> dataList) {
        if (dataList != null && dataList.size() > 0) {
            //通过接口
            firstEnter.setVisibility(VISIBLE);
            hotitem.setVisibility(VISIBLE);
            hotitem.setTextViews(dataList);
            hotitem.setOnMultipleTVItemClickListener(new MultipleTextViewGroup.OnMultipleTVItemClickListener() {
                @Override
                public void onMultipleTVItemClick(View view, int position) {
                    if (getContext() != null && (getContext() instanceof ChooseCityNewActivity)) {
                        List<SearchGroupBean> list = ((ChooseCityNewActivity) getContext()).getResulthideSoft(dataList.get(position));
                        addAfterSearchDestinationModel(list, dataList.get(position));
                        SearchUtils.addCityHistorySearch(dataList.get(position));
                    }
                    searchAdapter.removeModels();
                    searchAfterAdapter.removeModels();
                    firstEnter.setVisibility(GONE);
                    search_after_list.setVisibility(VISIBLE);
                    search_first_list.setVisibility(GONE);
                    SearchUtils.isRecommend = true;
                    SearchUtils.isHistory = false;
                }
            });
        } else {
            hotitem.setVisibility(GONE);
        }
    }

    public void addSearchDestinationModel(List<SearchGroupBean> list, String searchStr) {
        if (search_first_list.getChildCount() > 0) {
            search_first_list.removeAllViews();
        }
        searchAdapter.addSearchDestinationModel(getContext(), list, searchStr);
    }

    public void addAfterSearchDestinationModel(List<SearchGroupBean> list, String keyword) {
        if (search_after_list.getChildCount() > 0) {
            search_after_list.removeAllViews();
        }
        searchAfterAdapter.addAfterSearchDestinationModel(getContext(), list, keyword);

    }

    //倒序展示
    private List<String> showHistory(List<String> history) {
        List<String> showHistory = new ArrayList<>();
        if (history != null && history.size() > 0) {
            for (int i = history.size() - 1; i >= 0; i--) {
                showHistory.add(history.get(i));
            }
        }
        return showHistory;
    }

    /**
     * 展示历史搜索标签
     */
    private void changHistory() {
        //倒序展示
        final List<String> dataList = showHistory(SearchUtils.getSaveHistorySearch());
        if (dataList != null && dataList.size() > 0) {
            historyLayout.setVisibility(VISIBLE);
            history.setTextViews(dataList);
            history.setOnMultipleTVItemClickListener(new MultipleTextViewGroup.OnMultipleTVItemClickListener() {
                @Override
                public void onMultipleTVItemClick(View view, int position) {
                    //点击事件
                    if (getContext() != null && (getContext() instanceof ChooseCityNewActivity)) {
                        ((ChooseCityNewActivity) getContext()).hideSoft(dataList.get(position));
                    }
                    searchAdapter.removeModels();
                    searchAfterAdapter.removeModels();
                    firstEnter.setVisibility(GONE);
                    search_after_list.setVisibility(VISIBLE);
                    search_first_list.setVisibility(GONE);
                    List<SearchGroupBean> list = CityUtils.search((Activity) getContext(), dataList.get(position));
                    addAfterSearchDestinationModel(list, dataList.get(position));
                    SearchUtils.isHistory = true;
                    SearchUtils.isRecommend = false;
                }
            });
        } else {
            historyLayout.setVisibility(GONE);
        }
    }

    public void initSearchAdapter() {
        searchAdapter = new SearchAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(getContext());
        layoutManager.setOrientation(WrapContentLinearLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        search_first_list.setLayoutManager(layoutManager);
        search_first_list.setHasFixedSize(true);
        search_first_list.setAdapter(searchAdapter);
    }

    public void initSearchAfterAdapter() {
        searchAfterAdapter = new SearchAfterAdapter();
        WrapContentLinearLayoutManager layoutManager1 = new WrapContentLinearLayoutManager(getContext());
        layoutManager1.setOrientation(WrapContentLinearLayoutManager.VERTICAL);
        layoutManager1.setAutoMeasureEnabled(true);
        search_after_list.setLayoutManager(layoutManager1);
        search_after_list.setHasFixedSize(true);
        search_after_list.setAdapter(searchAfterAdapter);
    }
}
