package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.adapter.LevelCityAdapter;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@ContentView(R.layout.fragment_filter_city)
public class CityFilterFragment extends BaseFragment {

    @Bind(R.id.filter_left_list)
    ListView leftList;
    @Bind(R.id.filter_middle_list)
    ListView middleList;
    @Bind(R.id.filter_right_list)
    ListView rightList;

    @Bind(R.id.filter_left_layout)
    RelativeLayout leftLayout;

    LevelCityAdapter levelCityAdapterLeft, levelCityAdapterMiddle, levelCityAdapterRight;
    List<SearchGroupBean> groupList;
    List<SearchGroupBean> groupList2;
    List<SearchGroupBean> groupList3;

    private CityListActivity.Params cityParams;

    public void setCityParams(CityListActivity.Params cityParams) {
        if (cityParams == null) {
            return;
        }
        if (levelCityAdapterLeft != null) {
            levelCityAdapterLeft.setCityParams(cityParams);
        }
        this.cityParams = cityParams;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.getScreenWidth()/3, LinearLayout.LayoutParams.MATCH_PARENT);
        leftLayout.setLayoutParams(params);

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
                if (groupList2.get(position).spot_id == -4) {//全部目的地
                    CityListActivity.Params params = new CityListActivity.Params();
                    params.id = groupList2.get(position).spot_id;
                    params.cityHomeType = CityListActivity.CityHomeType.ALL;
                    params.titleName = groupList2.get(position).spot_name;
                    EventBus.getDefault().post(new EventAction(EventType.GUIDE_FILTER_CITY, params));
                    cityParams = params;
                    if (levelCityAdapterMiddle != null) {
                        levelCityAdapterMiddle.setCityParams(params);
                    }
                    if (levelCityAdapterRight != null) {
                        levelCityAdapterRight.setCityParams(params);
                    }
                } else {
                    if (CityUtils.canGoCityList(groupList2.get(position))) {
                        goCityList(groupList2.get(position));
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
                goCityList(groupList3.get(position));
                for (SearchGroupBean lineGroupBean : groupList3) {
                    lineGroupBean.isSelected = false;
                }
                groupList3.get(position).isSelected = true;
                levelCityAdapterRight.notifyDataSetChanged();
            }
        });

        levelCityAdapterLeft = new LevelCityAdapter(getActivity(), 1);
        levelCityAdapterLeft.setCityParams(cityParams);
        SearchGroupBean lineGroupBean = new SearchGroupBean();
        lineGroupBean.group_id = 0;
        lineGroupBean.flag = 1;
        lineGroupBean.type = 1;
        lineGroupBean.sub_city_name = "";
        lineGroupBean.group_name = "全部及热门";
        lineGroupBean.isSelected = true;
        try {
            groupList = new ArrayList<>();
            groupList.add(0, lineGroupBean);
            groupList.addAll(CityUtils.getLevel1City(getActivity()));
            levelCityAdapterLeft.setList(groupList);
            leftList.setAdapter(levelCityAdapterLeft);
            levelCityAdapterLeft.notifyDataSetChanged();

            showMiddleData(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showRightData(int position) {
        levelCityAdapterRight = new LevelCityAdapter(getActivity(), 3);
        levelCityAdapterRight.isFilter(true);
        List<SearchGroupBean> list3 = CityUtils.getLevel3City(getActivity(), groupList2.get(position).sub_place_id);
        if (null == list3 || list3.size() == 0) {
            goCityList(groupList2.get(position));
            Map map = new HashMap();
            map.put("source", "筛选");
            map.put("searchinput","筛选");
            MobClickUtils.onEvent(StatisticConstant.SEARCH,map);
        } else {

            SearchGroupBean lineGroupBean;
            SearchGroupBean searchGroupBean = groupList2.get(position);
            lineGroupBean = (SearchGroupBean) searchGroupBean.clone();
            lineGroupBean.isSelected = false;

            groupList3 = new ArrayList<>();
            groupList3.add(0, lineGroupBean);
            rightList.setVisibility(VISIBLE);
            groupList3.addAll(list3);
            levelCityAdapterRight.setCityParams(cityParams);
            levelCityAdapterRight.setList(groupList3);
            rightList.setAdapter(levelCityAdapterRight);
            levelCityAdapterMiddle.setMiddleLineShow(false);
        }
    }

    private void showMiddleData(int position) {
        levelCityAdapterMiddle = new LevelCityAdapter(getActivity(), 2);
        levelCityAdapterMiddle.isFilter(true);
        if (position == 0) {
            groupList2 = new ArrayList<>();
            groupList2.addAll(CityUtils.getHotCityWithAllCityHead(getActivity()));
        } else {
            groupList2 = new ArrayList<>();
            groupList2.addAll(CityUtils.getLevel2City(getActivity(), groupList.get(position).group_id));
        }
        levelCityAdapterMiddle.setCityParams(cityParams);
        levelCityAdapterMiddle.setList(groupList2);
        levelCityAdapterMiddle.setMiddleLineShow(true);
        middleList.setAdapter(levelCityAdapterMiddle);
    }

    private CityListActivity.Params goCityList(SearchGroupBean searchGroupBean) {
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
        cityParams = params;
        if (levelCityAdapterMiddle != null) {
            levelCityAdapterMiddle.setCityParams(params);
        }
        if (levelCityAdapterRight != null) {
            levelCityAdapterRight.setCityParams(params);
        }
        EventBus.getDefault().post(new EventAction(EventType.GUIDE_FILTER_CITY, params));
        return params;
    }

}
