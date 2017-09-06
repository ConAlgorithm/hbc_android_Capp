package com.hugboga.custom.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.LevelCityAdapter;
import com.hugboga.custom.adapter.SearchNewAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.LogUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.FlowLayout;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ChooseCityNewActivity extends BaseActivity {

    /*@Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.head_text_right)
    TextView headTextRight;*/
    @Bind(R.id.head_search)
    EditText headSearch;
    @Bind(R.id.head_search_clean)
    ImageView headSearchClean;
    /*@Bind(R.id.activity_head_layout)
    RelativeLayout activityHeadLayout;*/
    @Bind(R.id.history_city_layout)
    FlowLayout historyCityLayout;
    @Bind(R.id.history_layout)
    LinearLayout historyLayout;
    @Bind(R.id.left_list)
    ListView leftList;
    @Bind(R.id.middle_list)
    ListView middleList;
    @Bind(R.id.right_list)
    ListView rightList;
    @Bind(R.id.search_list)
    ExpandableListView expandableListView;
    @Bind(R.id.empty_layout_text)
    TextView emptyLayoutText;
    @Bind(R.id.empty_layout)
    LinearLayout emptyLayout;

    //boolean isHomeIn = false;
    boolean isFromTravelPurposeForm = false;

    public void initHeader() {
        //isHomeIn = this.getIntent().getBooleanExtra("isHomeIn",false);
        isFromTravelPurposeForm = this.getIntent().getBooleanExtra("isFromTravelPurposeForm", false);
        /*if(isHomeIn){
            headerLeftBtn.setImageResource(top_back_black);
        }else {
            headerLeftBtn.setImageResource(top_close);
        }*/
        //headTextRight.setText("取消");
        headSearch.setHint("请输入目的地名称");
        //headTextRight.setVisibility(GONE);
        //headerLeftBtn.setVisibility(VISIBLE);
        initPop();
        setSensorsDefaultEvent("搜索目的地页", SensorsConstant.SEARCH);
    }

    @Override
    public String getEventId() {
        return StatisticConstant.SEARCH_LAUNCH;
    }

    SearchNewAdapter searchNewAdapter;
//    PopupWindow popupWindow = null;


    private void initPop() {
//        View view = LayoutInflater.from(activity).inflate(R.layout.search_layout_new, null);
//        expandableListView = (ExpandableListView) view.findViewById(R.id.search_list);
        expandableListView.setChildIndicator(null);
        expandableListView.setGroupIndicator(null);
        expandableListView.setChildDivider(new ColorDrawable());
        searchNewAdapter = new SearchNewAdapter(activity);
        expandableListView.setAdapter(searchNewAdapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                ToastUtils.showShort(groupPosition+"======");
                if(isFromTravelPurposeForm){
                    if(list.get(groupPosition).sub_city_name != null && !list.get(groupPosition).sub_city_name.equals("")){
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,list.get(groupPosition).sub_city_name));
                    }else if(list.get(groupPosition).sub_place_name != null && !list.get(groupPosition).sub_place_name.equals("")){
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,list.get(groupPosition).sub_place_name));
                    }else if(list.get(groupPosition).group_name != null && !list.get(groupPosition).group_name.equals("")){
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,list.get(groupPosition).group_name));
                    }

                    finish();
                }else{
                    goCityList(list.get(groupPosition));
                }
                Map map = new HashMap();
                map.put("source", getIntentSource());
                map.put("searchinput", "输入内容后联想");
                MobClickUtils.onEvent(StatisticConstant.SEARCH, map);
                if(getIntentSource().equals("首页")){
                    setSensorsShareEvent(headSearch.getText().toString(),false,true,true);
                }
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_id != -100
                        && searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_id != -200) {
//                    ToastUtils.showShort(groupPosition + "======" + childPosition);
                    if(isFromTravelPurposeForm){
                        if(searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_city_name != null &&
                                !searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_city_name.equals("")){
                            EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_city_name));
                        }else if(searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_place_name != null &&
                                !searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_place_name.equals("")){
                            EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,searchNewAdapter.getChildList().get(groupPosition).get(childPosition).sub_place_name));
                        }else if(searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_name != null &&
                                !searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_name.equals("")){
                            EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_name));
                        }
                        finish();
                    }else{
                        goCityList(searchNewAdapter.getChildList().get(groupPosition).get(childPosition));
                    }

                    Map map = new HashMap();
                    map.put("source", getIntentSource());
                    map.put("searchinput", "输入内容后联想");
                    MobClickUtils.onEvent(StatisticConstant.SEARCH, map);
                    if(getIntentSource().equals("首页")){
                        setSensorsShareEvent(headSearch.getText().toString(),false,true,true);
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
        initView();
    }

    private void showSearchPop(List<SearchGroupBean> list) {
        //headTextRight.setVisibility(VISIBLE);
        //headerLeftBtn.setVisibility(GONE);
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

//        popupWindow.showAsDropDown(activityHeadLayout);
    }


    @OnClick({R.id.head_search, R.id.header_left_btn, R.id.head_search_clean})
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
//                if(!isHomeIn) {
//                    overridePendingTransition(R.anim.push_buttom_out, 0);
//                }
                break;
            case R.id.head_search_clean:
                headSearch.setText("");
                break;
            default:
                break;
        }
    }


    LevelCityAdapter levelCityAdapterLeft, levelCityAdapterMiddle, levelCityAdapterRight;
    List<SearchGroupBean> groupList;
    List<SearchGroupBean> groupList2;
    List<SearchGroupBean> groupList3;

    List<SearchGroupBean> list;

    public void initView() {
        initHeader();
        rightList.setVisibility(GONE);
        if(isFromTravelPurposeForm){
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
                    if(list!= null && list.size() <= 0){
                        if(getIntentSource().equals("首页")){
                            setSensorsShareEvent(headSearch.getText().toString(),false,false,false);
                        }
                    }
                } else {
                    headSearchClean.setVisibility(GONE);
                    expandableListView.setVisibility(GONE);
                    //headTextRight.setVisibility(GONE);
                    //headerLeftBtn.setVisibility(VISIBLE);
                    emptyLayout.setVisibility(GONE);
                }
            }
        });

        headSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(headSearch.getText())) {
                    list = CityUtils.search(activity, headSearch.getText().toString());
                    LogUtils.e(list.size() + "====" + headSearch.getText().toString());
                    showSearchPop(list);
                }
            }
        });

        leftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rightList.setVisibility(GONE);
                for (SearchGroupBean lineGroupBean : groupList) {
                    lineGroupBean.isSelected = false;
                }
                groupList.get(position).isSelected = true;
                levelCityAdapterLeft.notifyDataSetChanged();
                showMiddleData(position);


            }
        });

        middleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rightList.setVisibility(GONE);
                levelCityAdapterMiddle.setMiddleLineShow(true);

                if (groupList2.get(position).spot_id == -1) {
                    Intent intent = new Intent(activity, PickSendActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
                    startActivity(intent);

                } else if (groupList2.get(position).spot_id == -2) {
                    Intent intent = new Intent(activity, SingleActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
                    startActivity(intent);

                } else if (groupList2.get(position).spot_id == -3) {
                    Intent intent = new Intent(activity, CharterFirstStepActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    startActivity(intent);
                } else {
                    if (CityUtils.canGoCityList(groupList2.get(position))) {
                        if(isFromTravelPurposeForm){
                            if(groupList2.get(position).spot_name != null && !groupList2.get(position).spot_name.equals("")){
                                EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,groupList2.get(position).spot_name));
                            }else if(groupList2.get(position).sub_city_name != null && !groupList2.get(position).sub_city_name.equals("")){
                                EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,groupList2.get(position).sub_city_name));
                            }else if(groupList2.get(position).sub_place_name != null && !groupList2.get(position).sub_place_name.equals("")){
                                EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,groupList2.get(position).sub_place_name));
                            }else if(groupList2.get(position).group_name != null && !groupList2.get(position).group_name.equals("")){
                                EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,groupList2.get(position).group_name));
                            }

                            finish();
                        }else{
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

        rightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (SearchGroupBean lineGroupBean : groupList3) {
                    lineGroupBean.isSelected = false;
                }
                groupList3.get(position).isSelected = true;
                levelCityAdapterRight.notifyDataSetChanged();
                if(isFromTravelPurposeForm){
                    if(groupList3.get(position).spot_name != null && !groupList3.get(position).spot_name.equals("")){
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,groupList3.get(position).spot_name));
                    }else if(groupList3.get(position).sub_city_name != null && !groupList3.get(position).sub_city_name.equals("")){
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,groupList3.get(position).sub_city_name));
                    }else if(groupList3.get(position).sub_place_name != null &&!groupList3.get(position).sub_place_name.equals("")){
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,groupList3.get(position).sub_place_name));
                    }else if(groupList3.get(position).group_name != null &&!groupList3.get(position).group_name.equals("")){
                        EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,groupList3.get(position).group_name));
                    }
                    finish();
                }else{
                    goCityList(groupList3.get(position));
                }
                Map map = new HashMap();
                map.put("source", getIntentSource());
                map.put("searchinput", "筛选");
                MobClickUtils.onEvent(StatisticConstant.SEARCH, map);
            }
        });


        levelCityAdapterLeft = new LevelCityAdapter(activity, 1);
        SearchGroupBean lineGroupBean = new SearchGroupBean();
        lineGroupBean.group_id = 0;
        lineGroupBean.flag = 1;
        lineGroupBean.type = 1;
        lineGroupBean.sub_city_name = "";
        lineGroupBean.group_name = "热门";
        lineGroupBean.isSelected = true;
        try {
            groupList = new ArrayList<>();
            groupList.add(0, lineGroupBean);
            groupList.addAll(CityUtils.getLevel1City(activity));
            levelCityAdapterLeft.setList(groupList);
            leftList.setAdapter(levelCityAdapterLeft);
            levelCityAdapterLeft.notifyDataSetChanged();

            showMiddleData(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showRightData(int position) {
        levelCityAdapterRight = new LevelCityAdapter(activity, 3);
        List<SearchGroupBean> list3 = CityUtils.getCountrySearch(activity,groupList2.get(position).sub_place_id);
        list3.addAll(CityUtils.getLevel3City(activity, groupList2.get(position).sub_place_id));

        if (null == list3 || list3.size() == 0) {
            if(isFromTravelPurposeForm){
                if(groupList2.get(position).sub_place_name != null){
                    EventBus.getDefault().post(new EventAction(EventType.PURPOSER_CITY,groupList2.get(position).sub_place_name));
                }
                finish();
            }else{
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
            levelCityAdapterRight.setList(groupList3);
            rightList.setAdapter(levelCityAdapterRight);
            levelCityAdapterRight.notifyDataSetChanged();
            levelCityAdapterMiddle.setMiddleLineShow(false);
        }
    }

    private void showMiddleData(int position) {
        levelCityAdapterMiddle = new LevelCityAdapter(activity, 2);
        if (position == 0) {
            groupList2 = new ArrayList<>();
            if (isFromTravelPurposeForm) {
                groupList2.addAll(CityUtils.getHotCity(activity));
            } else {
                groupList2.addAll(CityUtils.getHotCityWithHead(activity));
            }
        } else {
            groupList2 = new ArrayList<>();
            groupList2.addAll(CityUtils.getLevel2City(activity, groupList.get(position).group_id));
        }
        levelCityAdapterMiddle.setList(groupList2);
        levelCityAdapterMiddle.setMiddleLineShow(true);
        levelCityAdapterMiddle.notifyDataSetChanged();
        middleList.setAdapter(levelCityAdapterMiddle);
    }


    private void genHistoryCity() {
        if (null != historyCityLayout) {
            historyCityLayout.removeAllViews();
            List<SearchGroupBean> list = CityUtils.getSaveCity();
            if (null != list) {
                TextView view = null;
                for (int i = 0; i < list.size(); i++) {
                    view = new TextView(activity);
                    final String name = CityUtils.getShowName(list.get(i));
                    view.setTag(list.get(i));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goCityList((SearchGroupBean) v.getTag());
                            if(getIntentSource().equals("首页")){
                                setSensorsShareEvent(name,true,true,true);
                            }
                        }
                    });
                    view.setGravity(Gravity.CENTER_VERTICAL);
                    LogUtils.e(name);
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
        CityListActivity.Params params = new CityListActivity.Params();

        if (searchGroupBean.flag == 1) {
            params.id = searchGroupBean.group_id;
            params.cityHomeType = CityListActivity.CityHomeType.ROUTE;
            params.titleName = searchGroupBean.group_name;
        } else if (searchGroupBean.flag == 2) {
            if (searchGroupBean.type == 1) {
                params.id = searchGroupBean.group_id;
                params.cityHomeType = CityListActivity.CityHomeType.ROUTE;
                params.titleName = searchGroupBean.group_name;
            } else if (searchGroupBean.type == 2) {
                params.id = searchGroupBean.sub_place_id;
                params.titleName = searchGroupBean.sub_place_name;
                params.cityHomeType = CityListActivity.CityHomeType.COUNTRY;
            } else {
                params.id = searchGroupBean.sub_city_id;
                params.cityHomeType = CityListActivity.CityHomeType.CITY;
                params.titleName = searchGroupBean.sub_city_name;
            }
        } else if (searchGroupBean.flag == 3) {
            if (searchGroupBean.sub_city_name.equalsIgnoreCase("全境")) {
                params.id = searchGroupBean.sub_city_id;
                params.cityHomeType = CityListActivity.CityHomeType.COUNTRY;
                params.titleName = searchGroupBean.sub_place_name;
            } else if (searchGroupBean.type == 1) {
                params.id = searchGroupBean.group_id;
                params.cityHomeType = CityListActivity.CityHomeType.ROUTE;
                params.titleName = searchGroupBean.group_name;
            } else if (searchGroupBean.type == 2) {
                params.id = searchGroupBean.sub_place_id;
                params.titleName = searchGroupBean.sub_place_name;
                params.cityHomeType = CityListActivity.CityHomeType.COUNTRY;
            } else {
                params.id = searchGroupBean.sub_city_id;
                params.cityHomeType = CityListActivity.CityHomeType.CITY;
                params.titleName = searchGroupBean.sub_city_name;
            }
        } else if (searchGroupBean.flag == 4) {
            params.id = searchGroupBean.spot_id;
            if (searchGroupBean.type == 1) {
                params.cityHomeType = CityListActivity.CityHomeType.CITY;
                params.titleName = searchGroupBean.spot_name;
            } else if (searchGroupBean.type == 2) {
                params.cityHomeType = CityListActivity.CityHomeType.COUNTRY;
                params.titleName = searchGroupBean.spot_name;
            }
        }
        Intent intent = new Intent(this, CityListActivity.class);
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
        if(!isFromTravelPurposeForm){
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
        return "目的地";
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
