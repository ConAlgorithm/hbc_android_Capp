package com.hugboga.custom.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.SearchCityAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.DestinationHotItemBean;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.DestinationHot;
import com.hugboga.custom.data.request.RequestHotSearch;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.IntentUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.QueryHotCity;
import com.hugboga.custom.widget.search.SearchHistoryView;
import com.hugboga.custom.widget.search.SearchShortcut;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.hugboga.custom.constants.Constants.QUERY_DELAY_MILLIS;
import static com.hugboga.custom.constants.Constants.QUERY_TXT_LENGTH_LIMIT;

public class QueryCityActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.head_search)
    EditText headSearch;
    @BindView(R.id.head_search_clean)
    TextView headSearchClean;
    @BindView(R.id.left_list)
    RecyclerView leftList; //左侧list
    @BindView(R.id.middle_list)
    RecyclerView middleList; //中间list
    @BindView(R.id.right_list)
    RecyclerView rightList; //右侧list

    @BindView(R.id.middle_layout)
    RelativeLayout middleLayout;
    @BindView(R.id.search_hot)
    QueryHotCity searchHotCity; //热门城市列表

    @BindView(R.id.search_shortcut)
    SearchShortcut searchShortcut; //快捷下单区域

    SearchCityAdapter levelCityAdapterLeft;
    SearchCityAdapter levelCityAdapterMiddle;
    SearchCityAdapter levelCityAdapterRight;
    List<SearchGroupBean> groupList;
    List<SearchGroupBean> groupList2;
    List<SearchGroupBean> groupList3;

    List<SearchGroupBean> list;

    //======快捷选择区=====================
    @BindView(R.id.searchCityNewLabelLayout)
    RelativeLayout searchCityNewLabelLayout; //搜索快捷选择区域
    @BindView(R.id.search_view)
    SearchHistoryView searchHistoryView; //搜索历史部分

    boolean isFromTravelPurposeForm = false;

    @Override
    public String getEventId() {
        return StatisticConstant.SEARCH_LAUNCH;
    }

    @Override
    public int getContentViewId() {
        return R.layout.query_city_layout;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.top_back_black);
        getSupportActionBar().setTitle("");
        getHotInfo(); //获取热门城市信息
        requestHotSearch(); //热词搜索
        setSensorsPageViewEvent("搜索目的地页", SensorsConstant.SEARCH);
        //根据参数是否显示快速下单区域
        isFromTravelPurposeForm = this.getIntent().getBooleanExtra("isFromTravelPurposeForm", false);
        searchShortcut.init(isFromTravelPurposeForm);

        //初始化搜索初始内容
        if (searchHistoryView != null) {
            searchHistoryView.init(this);
        }
        initSearchInput(); //初始化搜索事件
        initView();
    }

    @OnClick({R.id.head_search, R.id.head_search_clean, R.id.searchCityNewLabelLayout, R.id.head_search_remove})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_search:
                showSoftInputMethod(headSearch);
                break;
            case R.id.head_search_clean:
            case R.id.searchCityNewLabelLayout:  //点击隐藏搜索模块
                hideSoftInput();
                headSearch.setText("");
                headSearch.clearFocus();
                break;
            case R.id.head_search_remove:
                headSearch.setText("");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                hideSoftInput();
                finish();
                StatisticClickEvent.click(StatisticConstant.SEARCH_CLOSE, getIntentSource());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSearchInput() {
        //根据参数默认不显示搜索区域
        if (isFromTravelPurposeForm) {
            searchCityNewLabelLayout.setVisibility(GONE);
        }
        headSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchCityNewLabelLayout.setVisibility(b ? View.VISIBLE : View.GONE);
                headSearchClean.setVisibility(b ? View.VISIBLE : View.GONE);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) headSearch.getLayoutParams();
                layoutParams.rightMargin = UIUtils.dip2px(b ? 4 : 12);
                headSearch.setLayoutParams(layoutParams);
            }
        });
        headSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchStr = headSearch.getText().toString().trim();
                if (searchHistoryView != null) {
                    searchHistoryView.searchText(searchStr);
                }
                headSearchClean.setVisibility(TextUtils.isEmpty(searchStr) ? View.GONE : View.VISIBLE);
                startQueryForTxtChange(searchStr); //进入触发搜索流程
            }
        });

        headSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == keyEvent.KEYCODE_ENTER) {
                    switch (keyEvent.getAction()) {
                        case KeyEvent.ACTION_DOWN:
                            hideInputMethod(headSearch);
                            startQuery();
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    Handler queryHander = new Handler();
    Runnable queryRunnable = new Runnable() {
        @Override
        public void run() {
//            HLog.d("满足字符大于等于2并且等待了1s，开始执行搜索任务");
            startQuery();
        }
    };

    /**
     * 触发搜索流程
     *
     * @param searchStr
     */
    private void startQueryForTxtChange(String searchStr) {
        queryHander.removeCallbacks(queryRunnable);
        if (searchStr.length() >= QUERY_TXT_LENGTH_LIMIT) {
            queryHander.postDelayed(queryRunnable, QUERY_DELAY_MILLIS);
        }
    }

    /**
     * 移除自动搜索
     */
    public void removeQuery() {
        queryHander.removeCallbacks(queryRunnable);
    }

    /**
     * 开始进行搜索
     */
    private void startQuery() {
        if (searchHistoryView != null) {
            String searchStr = headSearch.getText().toString().trim();
            searchHistoryView.showResultQuery(searchStr);
        }
    }

    public void initView() {
        rightList.setVisibility(GONE);
        leftList.setLayoutManager(new LinearLayoutManager(this));
        levelCityAdapterLeft = new SearchCityAdapter(activity, 1);
        levelCityAdapterLeft.setData(getLevel1Data());
        leftList.setAdapter(levelCityAdapterLeft);
        showMiddleData(0);
        levelCityAdapterLeft.setOnItemClickListener(new SearchCityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SearchGroupBean bean, int position) {
                scrollQuickChange(); //触发下单入口变化
                rightList.setVisibility(GONE);
                for (SearchGroupBean lineGroupBean : groupList) {
                    lineGroupBean.isSelected = false;
                }
                groupList.get(position).isSelected = true;
                levelCityAdapterLeft.notifyDataSetChanged();
                showMiddleData(position);
            }
        });
        leftList.addOnScrollListener(onScrollListenerLeft); //滑动监听
    }

    private List<SearchGroupBean> getLevel1Data() {
        groupList = new ArrayList<>();
        groupList.add(getLevel1TopBeam());
        groupList.addAll(CityUtils.getLevel1City(activity));
        return groupList;
    }

    private SearchGroupBean getLevel1TopBeam() {
        SearchGroupBean lineGroupBean = new SearchGroupBean();
        lineGroupBean.group_id = 0;
        lineGroupBean.flag = 1;
        lineGroupBean.type = 1;
        lineGroupBean.sub_city_name = "";
        lineGroupBean.group_name = "热门";
        lineGroupBean.isSelected = true;
        return lineGroupBean;
    }

    private void showRightData(int position) {
        levelCityAdapterRight = new SearchCityAdapter(activity, 3);
        List<SearchGroupBean> list3 = CityUtils.getCountrySearch(activity, groupList2.get(position).sub_place_id);
        list3.addAll(CityUtils.getLevel3City(activity, groupList2.get(position).sub_place_id));

        if (null == list3 || list3.size() == 0) {
            if (isFromTravelPurposeForm) {
                if (groupList2.get(position).sub_place_name != null) {
                    EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, groupList2.get(position).sub_place_name));
                }
                finish();
            } else {
                goCityList(groupList2.get(position));
            }
            Map map = new HashMap();
            map.put("source", getIntentSource());
            map.put("searchinput", "筛选");
            MobClickUtils.onEvent(StatisticConstant.SEARCH, map);
        } else {
            SearchGroupBean lineGroupBean;
            SearchGroupBean searchGroupBean = groupList2.get(position);
            lineGroupBean = (SearchGroupBean) searchGroupBean.clone();
            lineGroupBean.isSelected = false;

            groupList3 = new ArrayList<>();
            groupList3.add(0, lineGroupBean);
            rightList.setVisibility(VISIBLE);
            groupList3.addAll(list3);
            levelCityAdapterRight.setData(groupList3);
            rightList.setLayoutManager(new WrapContentLinearLayoutManager(this));
            rightList.setAdapter(levelCityAdapterRight);
            levelCityAdapterRight.notifyDataSetChanged();
            levelCityAdapterMiddle.setMiddleLineShow(false);
        }
        levelCityAdapterRight.setOnItemClickListener(new SearchCityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SearchGroupBean bean, int position) {
                for (SearchGroupBean lineGroupBean : groupList3) {
                    lineGroupBean.isSelected = false;
                }
                groupList3.get(position).isSelected = true;
                levelCityAdapterRight.notifyDataSetChanged();
                if (isFromTravelPurposeForm) {
                    if (groupList3.get(position).spot_name != null && !groupList3.get(position).spot_name.equals("")) {
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, groupList3.get(position).spot_name));
                    } else if (groupList3.get(position).sub_city_name != null && !groupList3.get(position).sub_city_name.equals("")) {
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, groupList3.get(position).sub_city_name));
                    } else if (groupList3.get(position).sub_place_name != null && !groupList3.get(position).sub_place_name.equals("")) {
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, groupList3.get(position).sub_place_name));
                    } else if (groupList3.get(position).group_name != null && !groupList3.get(position).group_name.equals("")) {
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, groupList3.get(position).group_name));
                    }
                    finish();
                } else {
                    goCityList(groupList3.get(position));
                }
                Map map = new HashMap();
                map.put("source", getIntentSource());
                map.put("searchinput", "筛选");
                MobClickUtils.onEvent(StatisticConstant.SEARCH, map);
            }
        });
    }

    private void showMiddleData(int position) {
        levelCityAdapterMiddle = new SearchCityAdapter(activity, 2);
        if (position == 0) {
            searchHotCity.setVisibility(VISIBLE);
            middleLayout.setVisibility(GONE);
        } else {
            searchHotCity.setVisibility(GONE);
            middleLayout.setVisibility(VISIBLE);
            groupList2 = new ArrayList<>();
            groupList2.addAll(CityUtils.getLevel2City(activity, groupList.get(position).group_id));
            levelCityAdapterMiddle.setData(groupList2);
            levelCityAdapterMiddle.setMiddleLineShow(true);
            levelCityAdapterMiddle.notifyDataSetChanged();
            middleList.setLayoutManager(new WrapContentLinearLayoutManager(this));
            middleList.setAdapter(levelCityAdapterMiddle);
        }
        levelCityAdapterMiddle.setOnItemClickListener(new SearchCityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SearchGroupBean bean, int position) {
                rightList.setVisibility(GONE);
                levelCityAdapterMiddle.setMiddleLineShow(true);

                if (groupList2.get(position).spot_id == -1) {
                    IntentUtils.intentPickupActivity(activity, getEventSource());
                } else if (groupList2.get(position).spot_id == -2) {
                    IntentUtils.intentSingleActivity(activity, getEventSource());
                } else if (groupList2.get(position).spot_id == -3) {
                    IntentUtils.intentCharterActivity(activity, getEventSource());
                } else {
                    if (CityUtils.canGoCityList(groupList2.get(position))) {
                        if (isFromTravelPurposeForm) {
                            if (groupList2.get(position).spot_name != null && !groupList2.get(position).spot_name.equals("")) {
                                EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, groupList2.get(position).spot_name));
                            } else if (groupList2.get(position).sub_city_name != null && !groupList2.get(position).sub_city_name.equals("")) {
                                EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, groupList2.get(position).sub_city_name));
                            } else if (groupList2.get(position).sub_place_name != null && !groupList2.get(position).sub_place_name.equals("")) {
                                EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, groupList2.get(position).sub_place_name));
                            } else if (groupList2.get(position).group_name != null && !groupList2.get(position).group_name.equals("")) {
                                EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, groupList2.get(position).group_name));
                            }

                            finish();
                        } else {
                            goCityList(groupList2.get(position));
                        }

                        Map map = new HashMap();
                        map.put("source", getIntentSource());
                        map.put("searchinput", "筛选");
                        MobClickUtils.onEvent(StatisticConstant.SEARCH, map);
                    } else {
                        showRightData(position);
                    }
                }
                for (SearchGroupBean lineGroupBean : groupList2) {
                    lineGroupBean.isSelected = false;
                }
                groupList2.get(position).isSelected = true;
                levelCityAdapterMiddle.notifyDataSetChanged();
            }
        });

    }

    private void goCityList(SearchGroupBean searchGroupBean) {
        CityUtils.addCityHistoryData(searchGroupBean);
        CityActivity.Params params = new CityActivity.Params();

        if (searchGroupBean.flag == 1) {
            params.id = searchGroupBean.group_id;
            params.cityHomeType = CityActivity.CityHomeType.ROUTE;
            params.titleName = searchGroupBean.group_name;
        } else if (searchGroupBean.flag == 2) {
            if (searchGroupBean.type == 1) {
                params.id = searchGroupBean.group_id;
                params.cityHomeType = CityActivity.CityHomeType.ROUTE;
                params.titleName = searchGroupBean.group_name;
            } else if (searchGroupBean.type == 2) {
                params.id = searchGroupBean.sub_place_id;
                params.titleName = searchGroupBean.sub_place_name;
                params.cityHomeType = CityActivity.CityHomeType.COUNTRY;
            } else {
                params.id = searchGroupBean.sub_city_id;
                params.cityHomeType = CityActivity.CityHomeType.CITY;
                params.titleName = searchGroupBean.sub_city_name;
            }
        } else if (searchGroupBean.flag == 3) {
            if (searchGroupBean.sub_city_name.equalsIgnoreCase("全境")) {
                params.id = searchGroupBean.sub_city_id;
                params.cityHomeType = CityActivity.CityHomeType.COUNTRY;
                params.titleName = searchGroupBean.sub_place_name;
            } else if (searchGroupBean.type == 1) {
                params.id = searchGroupBean.group_id;
                params.cityHomeType = CityActivity.CityHomeType.ROUTE;
                params.titleName = searchGroupBean.group_name;
            } else if (searchGroupBean.type == 2) {
                params.id = searchGroupBean.sub_place_id;
                params.titleName = searchGroupBean.sub_place_name;
                params.cityHomeType = CityActivity.CityHomeType.COUNTRY;
            } else {
                params.id = searchGroupBean.sub_city_id;
                params.cityHomeType = CityActivity.CityHomeType.CITY;
                params.titleName = searchGroupBean.sub_city_name;
            }
        } else if (searchGroupBean.flag == 4) {
            params.id = searchGroupBean.spot_id;
            if (searchGroupBean.type == 1) {
                params.cityHomeType = CityActivity.CityHomeType.CITY;
                params.titleName = searchGroupBean.spot_name;
            } else if (searchGroupBean.type == 2) {
                params.cityHomeType = CityActivity.CityHomeType.COUNTRY;
                params.titleName = searchGroupBean.spot_name;
            }
        }
        Intent intent = new Intent(this, CityActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, "搜索结果");
        startActivity(intent);
    }

    @Override
    public String getEventSource() {
        return "搜索";
    }

    public void hideSoft(String searchStr) {
        headSearch.setText(searchStr);
        hideInputMethod(headSearch);
    }

    private void getHotInfo() {
        DestinationHot destinationHot = new DestinationHot(this);
        HttpRequestUtils.request(this, destinationHot, this, false);
    }

    /**
     * 热词搜索
     */
    private void requestHotSearch() {
        RequestHotSearch requestHotSearch = new RequestHotSearch(this);
        HttpRequestUtils.request(this, requestHotSearch, this, false);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof DestinationHot) {
            ArrayList<DestinationHotItemBean> hotCityData = ((DestinationHot) request).getData();
            searchHotCity.setHotCitys(hotCityData);
        } else if (request instanceof RequestHotSearch) {
            ArrayList<String> dataList = (ArrayList<String>) request.getData();
            if (searchHistoryView != null) {
                searchHistoryView.showHistorySearchResult(dataList);
            }
        }
    }

    /**
     * 展开更多结果
     */
    public void showMoreSearchDestination() {
        if (searchHistoryView != null) {
            searchHistoryView.showAfterAllData();
        }
    }

    /**
     * 显示所有地点关联数据
     *
     * @param searchStr
     */
    public void searchAllResult(String searchStr) {
        if (searchHistoryView != null) {
            searchHistoryView.showMoreQuery(searchStr);
        }
    }

    /**
     * 列表滚动或者点击触发下单入口变化
     */
    public void scrollQuickChange() {
        searchShortcut.scrollChange();
    }

    RecyclerView.OnScrollListener onScrollListenerLeft = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) {
                scrollQuickChange(); //触发下单入口变化
            }
        }
    };

    public void addQueryResultView(String eventSource, String pageTitle, String intentSource) {
        SensorsUtils.setPageEvent(eventSource, pageTitle, intentSource);
    }
}
