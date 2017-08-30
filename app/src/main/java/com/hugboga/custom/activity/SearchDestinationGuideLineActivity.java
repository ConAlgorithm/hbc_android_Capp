package com.hugboga.custom.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.adapter.SearchAdapter;
import com.hugboga.custom.adapter.SearchAfterAdapter;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.data.request.RequestHotSearch;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.LogUtils;
import com.hugboga.custom.utils.SearchUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.GuideSearchListItem;
import com.hugboga.custom.widget.LineSearchListItem;
import com.hugboga.custom.widget.MultipleTextViewGroup;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by zhangqiang on 17/8/19.
 */

public class SearchDestinationGuideLineActivity extends BaseActivity implements HttpRequestListener {

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.head_search)
    EditText headSearch;
    @Bind(R.id.head_search_clean)
    ImageView headSearchClean;
    @Bind(R.id.history)
    MultipleTextViewGroup history;
    @Bind(R.id.history_layout)
    LinearLayout historyLayout;
    @Bind(R.id.below_history)
    View belowHistory;
    @Bind(R.id.hotitem)
    MultipleTextViewGroup hotitem;

    List<SearchGroupBean> listAll;
    List<SearchGroupBean> listfirst;
    List<SearchGroupBean> listAfter;
    int showListType = 0;//1:first 2:after 3:all
    @Bind(R.id.firstEnter)
    RelativeLayout firstEnter;


//    @Bind(R.id.guide_layout)
//    XRecyclerView guideLayout;
//    HbcRecyclerSingleTypeAdpater guideLayoutAdapter;
//    @Bind(R.id.line_layout)
//    XRecyclerView lineLayout;
    HbcRecyclerSingleTypeAdpater lineLayoutAdapter;
    @Bind(R.id.search_first_list)
    public RecyclerView search_first_list;
    public SearchAdapter searchAdapter;
    @Bind(R.id.search_after_list)
    public RecyclerView search_after_list;
    @Bind(R.id.search_remove)
    ImageView search_remove;
    public SearchAfterAdapter searchAfterAdapter;

    Handler handler;
    @Override
    public int getContentViewId() {
        return R.layout.search_destination_guide_line;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initHeader();
        initView();
        requestHotSearch();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void initView() {
        firstEnter.setVisibility(VISIBLE);
        search_first_list.setVisibility(GONE);
        search_after_list.setVisibility(GONE);
        if(searchAdapter != null){
            searchAdapter.removeModels();
        }
        if(searchAfterAdapter!= null){
            searchAfterAdapter.removeModels();

        }
        initSearchAdapter();
        initSearchAfterAdapter();


        //tv_footer = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.search_destination_footer_layout, expandableListView,false);
        headSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchAdapter.removeModels();
                searchAfterAdapter.removeModels();

                if (!TextUtils.isEmpty(headSearch.getText())) {
                    firstEnter.setVisibility(GONE);
                    search_first_list.setVisibility(VISIBLE);
                    search_after_list.setVisibility(GONE);
                    headSearchClean.setVisibility(VISIBLE);
                    listAll = CityUtils.search(activity, headSearch.getText().toString());
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
                    addSearchDestinationModel(listAll);
                    LogUtils.e(listfirst.size() + "====" + headSearch.getText().toString());

                } else {
                    firstEnter.setVisibility(VISIBLE);
                    search_first_list.setVisibility(GONE);
                    search_after_list.setVisibility(GONE);
                    //每次关键字发生变化,都要重新展示历史记录
                    changHistory();
                    requestHotSearch();
                }

            }
        });
        search_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchUtils.clearHistorySearch();
                changHistory();
            }
        });
        headSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_DOWN:
                            if(headSearch.getText().toString().length() >0){
                                searchAdapter.removeModels();
                                searchAfterAdapter.removeModels();
                                search_after_list.setVisibility(VISIBLE);
                                search_first_list.setVisibility(GONE);
                                addAfterSearchDestinationModel(listAll,headSearch.getText().toString());
                                SearchUtils.addCityHistorySearch(headSearch.getText().toString());
                            }
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
//        headSearch.setOnEditorActionListener(new TextView.OnEditorActionListener(){
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == EditorInfo.IME_ACTION_SEARCH){
//                    searchAdapter.removeModels();
//                    searchAfterAdapter.removeModels();
//                    search_after_list.setVisibility(VISIBLE);
//                    search_first_list.setVisibility(GONE);
//                    addAfterSearchDestinationModel(listAll);
//                    return true;
//                }
//                return false;
//            }
//        });

        headSearchClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headSearch.setText("");
            }
        });

        changHistory();

//        List<String> dataList1 = new ArrayList<String>();
//
//
//        dataList1.add("天盟天盟天盟天盟");
//        dataList1.add("Mason Mason Mason");
//
//        dataList1.add("Mason Liu 天盟天盟天盟 天盟天");
//        dataList1.add("天盟");
//        dataList1.add("天盟天盟天盟");
//        dataList1.add("Mason Mason");
//
//        dataList1.add("Mason");
//        dataList1.add("天adsf");
//        dataList1.add("天adf");
//
//
//        //通过接口
//        hotitem.setTextViews(dataList1);
//        hotitem.setOnMultipleTVItemClickListener(new MultipleTextViewGroup.OnMultipleTVItemClickListener() {
//            @Override
//            public void onMultipleTVItemClick(View view, int position) {
//                //点击事件 // TODO: 17/8/19
//            }
//        });
    }

    public void initHeader() {
        headSearch.setHint("请输入目的地名称");
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                finish();
            }
        });

    }

    public void addSearchDestinationModel(List<SearchGroupBean> list) {
        if(search_first_list.getChildCount()>0){
            search_first_list.removeAllViews();
        }
        searchAdapter.addSearchDestinationModel(this,list,headSearch.getText().toString());

    }

    public void addAfterSearchDestinationModel(List<SearchGroupBean> list,String keyword) {
        if(search_after_list.getChildCount()>0){
            search_after_list.removeAllViews();
        }
        searchAfterAdapter.addAfterSearchDestinationModel(this,list,keyword);

    }

    public void showAllSearchDestination(){
        if(search_after_list.getChildCount()>0){
            search_after_list.removeAllViews();
        }
        searchAfterAdapter.showAllData();
    }

//    private void initGuideView() {
//        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
//        guideLayout.setLayoutManager(layoutManager);
//        guideLayoutAdapter = new HbcRecyclerSingleTypeAdpater(this, GuideSearchListItem.class);
//        guideLayout.setAdapter(guideLayoutAdapter);
//        guideLayout.setLoadingMoreEnabled(false);
//        guideLayout.setPullRefreshEnabled(false);
//        guideLayout.addHeaderView(getGuideLineHeaderView(guideLayout));
//        guideLayout.setVisibility(GONE);
//        //guideLayoutAdapter.addData();
//
//    }
//
//    private void initLineView() {
//        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
//        lineLayout.setLayoutManager(layoutManager);
//        lineLayoutAdapter = new HbcRecyclerSingleTypeAdpater(this, LineSearchListItem.class);
//        lineLayout.setAdapter(lineLayoutAdapter);
//        lineLayout.setLoadingMoreEnabled(false);
//        lineLayout.setPullRefreshEnabled(false);
//        lineLayout.addHeaderView(getGuideLineHeaderView(lineLayout));
//        lineLayout.setVisibility(GONE);
//    }

//    private void requestGuideData() {
//        guideLayout.setVisibility(VISIBLE);
//    }
//
//    private void requestLineData() {
//        lineLayout.setVisibility(VISIBLE);
//    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if(request instanceof RequestHotSearch){
            final ArrayList<String> dataList = (ArrayList<String>) request.getData();
            if(dataList!= null && dataList.size()>0){
                //通过接口
                firstEnter.setVisibility(VISIBLE);
                hotitem.setVisibility(VISIBLE);
                hotitem.setTextViews(dataList);
                hotitem.setOnMultipleTVItemClickListener(new MultipleTextViewGroup.OnMultipleTVItemClickListener() {
                    @Override
                    public void onMultipleTVItemClick(View view, int position) {
                        searchAdapter.removeModels();
                        searchAfterAdapter.removeModels();
                        firstEnter.setVisibility(GONE);
                        search_after_list.setVisibility(VISIBLE);
                        search_first_list.setVisibility(GONE);
                        List<SearchGroupBean> list = CityUtils.search(activity, dataList.get(position));
                        addAfterSearchDestinationModel(list,dataList.get(position));
                    }
                });
            }else{
                hotitem.setVisibility(GONE);
            }
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {
        super.onDataRequestCancel(request);
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

    private View getGuideLineHeaderView(RecyclerView recyclerView) {
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout headerView = (RelativeLayout) inflater.inflate(R.layout.search_guide_line_header, recyclerView, false);
        return headerView;
    }

//    private void requestGuideAndLine() {
//        //请求司导和线路接口
//        requestGuideData();
//        requestLineData();
//    }

//    private void initGuideAndLineView() {
//        initGuideView();
//        initLineView();
//    }

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

    private void changHistory() {
        //倒序展示
        final List<String> dataList = showHistory(SearchUtils.getSaveHistorySearch());
        if (dataList != null && dataList.size() > 0) {
            historyLayout.setVisibility(VISIBLE);
            belowHistory.setVisibility(VISIBLE);
            history.setIsOnlyOneLine(true);
            history.setTextViews(dataList);
            history.setOnMultipleTVItemClickListener(new MultipleTextViewGroup.OnMultipleTVItemClickListener() {
                @Override
                public void onMultipleTVItemClick(View view, int position) {
                    //点击事件
                    searchAdapter.removeModels();
                    searchAfterAdapter.removeModels();
                    firstEnter.setVisibility(GONE);
                    search_after_list.setVisibility(VISIBLE);
                    search_first_list.setVisibility(GONE);
                    List<SearchGroupBean> list = CityUtils.search(activity, dataList.get(position));
                    addAfterSearchDestinationModel(list,dataList.get(position));
                }
            });
        } else {
            historyLayout.setVisibility(GONE);
            belowHistory.setVisibility(GONE);
        }
    }

    public void initSearchAdapter(){
        searchAdapter = new SearchAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        layoutManager.setOrientation(WrapContentLinearLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        search_first_list.setLayoutManager(layoutManager);
        search_first_list.setHasFixedSize(true);
        search_first_list.setAdapter(searchAdapter);
    }

    public void initSearchAfterAdapter(){
        searchAfterAdapter = new SearchAfterAdapter();
        WrapContentLinearLayoutManager layoutManager1 = new WrapContentLinearLayoutManager(this);
        layoutManager1.setOrientation(WrapContentLinearLayoutManager.VERTICAL);
        layoutManager1.setAutoMeasureEnabled(true);
        search_after_list.setLayoutManager(layoutManager1);
        search_after_list.setHasFixedSize(true);
        search_after_list.setAdapter(searchAfterAdapter);
    }

    private void requestHotSearch(){
        RequestHotSearch requestHotSearch = new RequestHotSearch(this);
        HttpRequestUtils.request(this,requestHotSearch,this,false);
    }
    //搜索埋点
    public static void setSensorsShareEvent(String keyWord,boolean isHistory,boolean isRecommend,boolean hasResult) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("keyWord", keyWord);
            properties.put("isHistory", isHistory);
            properties.put("isRecommend", isRecommend);
            properties.put("hasResult", hasResult);
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("searchResult", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
