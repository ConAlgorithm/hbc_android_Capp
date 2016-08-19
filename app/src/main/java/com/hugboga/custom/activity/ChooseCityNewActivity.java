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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.LevelCityAdapter;
import com.hugboga.custom.adapter.SearchNewAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.LogUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.hugboga.custom.R.mipmap.closed_btn;
import static com.hugboga.custom.R.mipmap.top_back_white;

public class ChooseCityNewActivity extends BaseActivity {

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.head_text_right)
    TextView headTextRight;
    @Bind(R.id.head_search)
    EditText headSearch;
    @Bind(R.id.head_search_clean)
    ImageView headSearchClean;
    @Bind(R.id.activity_head_layout)
    RelativeLayout activityHeadLayout;
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

    boolean isHomeIn = false;
    public void initHeader() {
        isHomeIn = this.getIntent().getBooleanExtra("isHomeIn",false);
        if(isHomeIn){
            headerLeftBtn.setImageResource(top_back_white);
        }else {
            headerLeftBtn.setImageResource(closed_btn);
        }
        headTextRight.setText("取消");
        headSearch.setHint(R.string.home_search_hint);
        headTextRight.setVisibility(GONE);
        initPop();
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
                goCityList(list.get(groupPosition));
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_id != -100
                        && searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_id != -200) {
//                    ToastUtils.showShort(groupPosition + "======" + childPosition);
                    goCityList(searchNewAdapter.getChildList().get(groupPosition).get(childPosition));
                }
                return true;
            }
        });

    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_city_new);
        ButterKnife.bind(this);

    }

    private void showSearchPop(List<SearchGroupBean> list) {
        headTextRight.setVisibility(View.VISIBLE);
        if (null != list && list.size() != 0) {
            searchNewAdapter.setKey(headSearch.getText().toString().trim());
            searchNewAdapter.setGroupArray(list);
            emptyLayout.setVisibility(GONE);
        } else {
            searchNewAdapter.clearList();
            emptyLayout.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < list.size(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setVisibility(View.VISIBLE);

//        popupWindow.showAsDropDown(activityHeadLayout);
    }



    @OnClick({R.id.head_search, R.id.header_left_btn, R.id.head_search_clean, R.id.head_text_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_search:
                showSoftInputMethod(headSearch);
                break;
            case R.id.header_left_btn:
                expandableListView.setVisibility(GONE);
                finish();
                if(!isHomeIn) {
                    overridePendingTransition(R.anim.push_buttom_out, 0);
                }
                break;
            case R.id.head_search_clean:
                headSearch.setText("");
                break;
            case R.id.head_text_right:
                expandableListView.setVisibility(GONE);
                headTextRight.setVisibility(GONE);
                headSearch.setText("");
                collapseSoftInputMethod(headSearch);
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
                    list = CityUtils.search(activity, headSearch.getText().toString());
                    LogUtils.e(list.size() + "====" + headSearch.getText().toString());
                    showSearchPop(list);
                } else {
                    expandableListView.setVisibility(GONE);
                    headTextRight.setVisibility(GONE);
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
                for (SearchGroupBean lineGroupBean : groupList2) {
                    lineGroupBean.isSelected = false;
                }
                groupList2.get(position).isSelected = true;
                levelCityAdapterMiddle.notifyDataSetChanged();

                if (groupList2.get(position).spot_id == -1) {
                    finish();
                    Intent intent = new Intent(activity, PickSendActivity.class);
                    startActivity(intent);

                } else if (groupList2.get(position).spot_id == -2) {
                    finish();
                    Intent intent = new Intent(activity, SingleNewActivity.class);
                    startActivity(intent);

                } else if (groupList2.get(position).spot_id == -3) {
                    finish();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_DAIRY);
//                    startFragment(new FgDailyWeb(), bundle);
                    String userId = UserEntity.getUser().getUserId(activity);
                    String params = "";
                    if(!TextUtils.isEmpty(userId)){
                        params += "?userId=" + userId;
                    }
                    Intent intent = new Intent(activity, DailyWebInfoActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_DAIRY +params);
                    startActivity(intent);

//                    FgOrderSelectCity fgOrderSelectCity = new FgOrderSelectCity();
//                    startFragment(fgOrderSelectCity);
                } else {
                    if (CityUtils.canGoCityList(groupList2.get(position))) {
                        goCityList(groupList2.get(position));
                    } else {
                        showRightData(position);
                    }
                }
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
                goCityList(groupList3.get(position));
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
        groupList = new ArrayList<>();
        groupList.add(0, lineGroupBean);
        groupList.addAll(CityUtils.getLevel1City(activity));
        levelCityAdapterLeft.setList(groupList);
        leftList.setAdapter(levelCityAdapterLeft);
        levelCityAdapterLeft.notifyDataSetChanged();

        showMiddleData(0);
    }


    private void showRightData(int position) {
        levelCityAdapterRight = new LevelCityAdapter(activity, 3);
        List<SearchGroupBean> list3 = CityUtils.getLevel3City(activity, groupList2.get(position).sub_place_id);
        if (null == list3 || list3.size() == 0) {
            goCityList(groupList2.get(position));
        } else {

            SearchGroupBean lineGroupBean;
            SearchGroupBean searchGroupBean = groupList2.get(position);
            lineGroupBean = (SearchGroupBean) searchGroupBean.clone();
            lineGroupBean.isSelected = false;


            groupList3 = new ArrayList<>();
            groupList3.add(0, lineGroupBean);
            rightList.setVisibility(View.VISIBLE);
            groupList3.addAll(list3);
            levelCityAdapterRight.setList(groupList3);
            rightList.setAdapter(levelCityAdapterRight);
            levelCityAdapterRight.notifyDataSetChanged();
        }
    }

    private void showMiddleData(int position) {
        levelCityAdapterMiddle = new LevelCityAdapter(activity, 2);
        if (position == 0) {
            groupList2 = new ArrayList<>();
            groupList2.addAll(CityUtils.getHotCityWithHead(activity));
        } else {
            SearchGroupBean lineGroupBean;
            SearchGroupBean searchGroupBean = groupList.get(position);
            lineGroupBean = (SearchGroupBean) searchGroupBean.clone();
            lineGroupBean.isSelected = false;
            groupList2 = new ArrayList<>();
            groupList2.add(0, lineGroupBean);
            groupList2.addAll(CityUtils.getLevel2City(activity, groupList.get(position).group_id));
        }
        levelCityAdapterMiddle.setList(groupList2);
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
                    view.setTag(list.get(i));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goCityList((SearchGroupBean) v.getTag());
                        }
                    });
                    view.setGravity(Gravity.CENTER_VERTICAL);
                    String name = CityUtils.getShowName(list.get(i));
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
        SkuListActivity.Params params = new SkuListActivity.Params();

        if (searchGroupBean.flag == 1) {
            params.id = searchGroupBean.group_id;
            params.skuType = SkuListActivity.SkuType.ROUTE;
            params.titleName = searchGroupBean.group_name;
        } else if (searchGroupBean.flag == 2) {
            if (searchGroupBean.type == 1) {
                params.id = searchGroupBean.sub_place_id;
                params.skuType = SkuListActivity.SkuType.ROUTE;
                params.titleName = searchGroupBean.sub_place_name;
            } else if (searchGroupBean.type == 2) {
                params.id = searchGroupBean.sub_place_id;
                params.titleName = searchGroupBean.sub_place_name;
                params.skuType = SkuListActivity.SkuType.COUNTRY;
            } else {
                params.id = searchGroupBean.sub_place_id;
                params.skuType = SkuListActivity.SkuType.COUNTRY;
                params.titleName = searchGroupBean.sub_place_name;
            }
        } else if (searchGroupBean.flag == 3) {
            if (searchGroupBean.sub_city_name.equalsIgnoreCase("全境")) {
                params.id = searchGroupBean.sub_city_id;
                params.skuType = SkuListActivity.SkuType.COUNTRY;
                params.titleName = searchGroupBean.sub_place_name;
            } else {
                params.id = searchGroupBean.sub_city_id;
                params.skuType = SkuListActivity.SkuType.CITY;
                params.titleName = searchGroupBean.sub_place_name;
            }
        } else if (searchGroupBean.flag == 4) {
            params.id = searchGroupBean.spot_id;
            if (searchGroupBean.type == 1) {
                params.skuType = SkuListActivity.SkuType.CITY;
                params.titleName = searchGroupBean.spot_name;
            } else if (searchGroupBean.type == 2) {
                params.skuType = SkuListActivity.SkuType.COUNTRY;
                params.titleName = searchGroupBean.spot_name;
            }
        }
//        startFragment(FgSkuList.newInstance(params));
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Constants.PARAMS_DATA, params);
//        bundle.putString(KEY_FRAGMENT_NAME, this.getClass().getSimpleName());
//        bringToFront(FgSkuList.class, bundle);

        Intent intent = new Intent(this, SkuListActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);

        StatisticClickEvent.click(StatisticConstant.SEARCH,getIntentSource());
    }


    public void onBackPressed() {
        expandableListView.setVisibility(GONE);
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        genHistoryCity();
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
