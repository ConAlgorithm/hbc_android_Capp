package com.hugboga.custom.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.SearchCityAdapter;
import com.hugboga.custom.adapter.SearchNewAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.DestinationHotItemBean;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.DestinationHot;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.IntentUtils;
import com.hugboga.custom.utils.LogUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.FlowLayout;
import com.hugboga.custom.widget.SearchHotCity;
import com.hugboga.custom.widget.search.SearchShortcut;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ChooseCityNewActivity extends BaseActivity {

    @BindView(R.id.head_search)
    EditText headSearch;
    @BindView(R.id.head_search_clean)
    TextView headSearchClean;
    @BindView(R.id.history_city_layout)
    FlowLayout historyCityLayout;
    @BindView(R.id.history_layout)
    LinearLayout historyLayout;

    @BindView(R.id.left_list)
    RecyclerView leftList; //左侧list
    @BindView(R.id.middle_list)
    RecyclerView middleList; //中间list
    @BindView(R.id.right_list)
    RecyclerView rightList; //右侧list

    @BindView(R.id.middle_layout)
    RelativeLayout middleLayout;
    @BindView(R.id.search_hot)
    SearchHotCity searchHotCity; //热门城市列表
    @BindView(R.id.search_list)
    ExpandableListView expandableListView;
    @BindView(R.id.empty_layout_text)
    TextView emptyLayoutText;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    @BindView(R.id.search_shortcut)
    SearchShortcut searchShortcut; //快捷下单区域

    //    LevelCityAdapter levelCityAdapterLeft, levelCityAdapterMiddle, levelCityAdapterRight;
    SearchCityAdapter levelCityAdapterLeft, levelCityAdapterMiddle, levelCityAdapterRight;
    List<SearchGroupBean> groupList;
    List<SearchGroupBean> groupList2;
    List<SearchGroupBean> groupList3;

    List<SearchGroupBean> list;

    //======快捷选择区=====================
    @BindView(R.id.searchCityNewLabelLayout)
    RelativeLayout searchCityNewLabelLayout; //搜索快捷选择区域

    boolean isFromTravelPurposeForm = false;

    public void initHeader() {
        headSearch.setHint(R.string.choose_city_new_hint);
        initPop();
        setSensorsPageViewEvent("搜索目的地页", SensorsConstant.SEARCH);
        isFromTravelPurposeForm = this.getIntent().getBooleanExtra("isFromTravelPurposeForm", false);
        searchShortcut.init(isFromTravelPurposeForm);
    }

    @Override
    public String getEventId() {
        return StatisticConstant.SEARCH_LAUNCH;
    }

    SearchNewAdapter searchNewAdapter;
//    PopupWindow popupWindow = null;


    private void initPop() {
        expandableListView.setChildIndicator(null);
        expandableListView.setGroupIndicator(null);
        expandableListView.setChildDivider(new ColorDrawable());
        searchNewAdapter = new SearchNewAdapter(activity);
        expandableListView.setAdapter(searchNewAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (isFromTravelPurposeForm) {
                    if (list.get(groupPosition).sub_city_name != null && !list.get(groupPosition).sub_city_name.equals("")) {
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, list.get(groupPosition).sub_city_name));
                    } else if (list.get(groupPosition).sub_place_name != null && !list.get(groupPosition).sub_place_name.equals("")) {
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, list.get(groupPosition).sub_place_name));
                    } else if (list.get(groupPosition).group_name != null && !list.get(groupPosition).group_name.equals("")) {
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, list.get(groupPosition).group_name));
                    }

                    finish();
                } else {
                    goCityList(list.get(groupPosition));
                }
                Map map = new HashMap();
                map.put("source", getIntentSource());
                map.put("searchinput", "输入内容后联想");
                MobClickUtils.onEvent(StatisticConstant.SEARCH, map);
                if (getIntentSource().equals("首页")) {
                    setSensorsShareEvent(headSearch.getText().toString(), false, true, true);
                }
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_id != -100
                        && searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_id != -200) {
                    if (isFromTravelPurposeForm) {
                        if (searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_city_name != null &&
                                !searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_city_name.equals("")) {
                            EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_city_name));
                        } else if (searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_place_name != null &&
                                !searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_place_name.equals("")) {
                            EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_place_name));
                        } else if (searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_name != null &&
                                !searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_name.equals("")) {
                            EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY, searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_name));
                        }
                        finish();
                    } else {
                        goCityList(searchNewAdapter.getChildList().get(groupPosition).get(childPosition));
                    }

                    Map map = new HashMap();
                    map.put("source", getIntentSource());
                    map.put("searchinput", "输入内容后联想");
                    MobClickUtils.onEvent(StatisticConstant.SEARCH, map);
                    if (getIntentSource().equals("首页")) {
                        setSensorsShareEvent(headSearch.getText().toString(), false, true, true);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_city_new;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        getHotInfo(); //获取热门城市信息
        initHeader();
        initView();
        headSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchCityNewLabelLayout.setVisibility(b ? View.VISIBLE : View.GONE);
                headSearchClean.setVisibility(b ? View.VISIBLE : View.GONE);
            }
        });

    }

    private void showSearchPop(List<SearchGroupBean> list) {
        if (null != list && list.size() != 0) {
            searchNewAdapter.setKey(headSearch.getText().toString().trim());
            searchNewAdapter.setGroupArray(list);
            emptyLayout.setVisibility(GONE);
        } else {
            searchNewAdapter.clearList();
            emptyLayout.setVisibility(VISIBLE);
        }
        for (int i = 0; i < list.size(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setVisibility(VISIBLE);
    }

    @OnClick({R.id.head_search, R.id.header_left_btn, R.id.head_search_clean, R.id.searchCityNewLabelLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_search:
                showSoftInputMethod(headSearch);
                break;
            case R.id.header_left_btn:
                expandableListView.setVisibility(GONE);
                hideSoftInput();
                finish();
                StatisticClickEvent.click(StatisticConstant.SEARCH_CLOSE, getIntentSource());
                break;
            case R.id.head_search_clean:
            case R.id.searchCityNewLabelLayout:  //点击隐藏搜索模块
                hideSoftInput();
                headSearch.setText("");
                headSearch.clearFocus();
                break;
            default:
                break;
        }
    }

    public void initView() {
        rightList.setVisibility(GONE);
        if (isFromTravelPurposeForm) {
            historyLayout.setVisibility(GONE);
        }
        headSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(headSearch.getText())) {
                    headSearchClean.setVisibility(VISIBLE);
                    list = CityUtils.search(activity, headSearch.getText().toString());
                    LogUtils.e(list.size() + "====" + headSearch.getText().toString());
                    showSearchPop(list);
                    if (list != null && list.size() <= 0) {
                        if (getIntentSource().equals("首页")) {
                            setSensorsShareEvent(headSearch.getText().toString(), false, false, false);
                        }
                    }
                } else {
                    headSearchClean.setVisibility(GONE);
                    expandableListView.setVisibility(GONE);
                    emptyLayout.setVisibility(GONE);
                }
            }
        });

        headSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(headSearch.getText())) {
                    list = CityUtils.search(activity, headSearch.getText().toString());
                    showSearchPop(list);
                }
            }
        });

        leftList.setLayoutManager(new LinearLayoutManager(this));
        levelCityAdapterLeft = new SearchCityAdapter(activity, 1);
        levelCityAdapterLeft.setData(getLevel1Data());
        leftList.setAdapter(levelCityAdapterLeft);
        showMiddleData(0);
        levelCityAdapterLeft.setOnItemClickListener(new SearchCityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SearchGroupBean bean, int position) {
                rightList.setVisibility(GONE);
                for (SearchGroupBean lineGroupBean : groupList) {
                    lineGroupBean.isSelected = false;
                }
                groupList.get(position).isSelected = true;
                levelCityAdapterLeft.notifyDataSetChanged();
                showMiddleData(position);
            }
        });
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


    private void genHistoryCity() {
        if (null != historyCityLayout) {
            historyCityLayout.removeAllViews();
            List<SearchGroupBean> list = CityUtils.getSaveCity();
            if (list == null || list.size() == 0) {
                historyLayout.setVisibility(GONE);
            }
            if (null != list && list.size() > 0) {
                TextView view = null;
                historyLayout.setVisibility(VISIBLE);
                for (int i = 0; i < list.size(); i++) {
                    view = new TextView(activity);
                    final String name = CityUtils.getShowName(list.get(i));
                    view.setTag(list.get(i));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goCityList((SearchGroupBean) v.getTag());
                            if (getIntentSource().equals("首页")) {
                                setSensorsShareEvent(name, true, true, true);
                            }
                        }
                    });
                    view.setGravity(Gravity.CENTER_VERTICAL);
                    view.setText(name);
                    view.setTextColor(Color.parseColor("#666666"));
                    view.setHeight(UIUtils.dip2px(50f));
                    historyCityLayout.addView(view, 0);
                }
            }
        }
    }

    private void goCityList(SearchGroupBean searchGroupBean) {
        CityUtils.addCityHistoryData(searchGroupBean);
//        finish();
        expandableListView.setVisibility(GONE);
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
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        startActivity(intent);
    }


    public void onBackPressed() {
        expandableListView.setVisibility(GONE);
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFromTravelPurposeForm) {
            genHistoryCity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideInputMethod(headSearch);
    }

    @Override
    public String getEventSource() {
        return "目的地搜索";
    }

    //搜索埋点
    public static void setSensorsShareEvent(String keyWord, boolean isHistory, boolean isRecommend, boolean hasResult) {
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

    private void getHotInfo() {
        DestinationHot destinationHot = new DestinationHot(this);
        HttpRequestUtils.request(this, destinationHot, this, false);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof DestinationHot) {
            ArrayList<DestinationHotItemBean> hotCityData = ((DestinationHot) request).getData();
            searchHotCity.setHotCitys(hotCityData);
        }
    }
}
