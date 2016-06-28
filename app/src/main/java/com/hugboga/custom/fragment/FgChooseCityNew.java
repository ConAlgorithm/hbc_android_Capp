package com.hugboga.custom.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.LevelCityAdapter;
import com.hugboga.custom.adapter.SearchNewAdapter;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.LogUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.FlowLayout;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FgChooseCityNew extends BaseFragment {

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

    @Override
    protected void initHeader() {
        headTextRight.setText("取消");
    }
    SearchNewAdapter searchNewAdapter;
    PopupWindow popupWindow = null;

    ExpandableListView expandableListView;
    private void  initPop(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.search_layout_new,null);
        expandableListView = (ExpandableListView)view.findViewById(R.id.search_list);
        expandableListView.setChildIndicator(null);
        expandableListView.setGroupIndicator(null);
        expandableListView.setChildDivider(new ColorDrawable());
        searchNewAdapter = new SearchNewAdapter(getActivity());
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
                if(searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_id != -1
                        && searchNewAdapter.getChildList().get(groupPosition).get(childPosition).group_id != -2) {
//                    ToastUtils.showShort(groupPosition + "======" + childPosition);
                    goCityList(searchNewAdapter.getChildList().get(groupPosition).get(childPosition));
                }
                return true;
            }
        });
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        //设置后进行展示
        popupWindow.setBackgroundDrawable(new ColorDrawable());
//        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
    }


    private void showSearchPop(List<SearchGroupBean> list){
        if(null != list && list.size() != 0) {
            searchNewAdapter.setKey(headSearch.getText().toString().trim());
            searchNewAdapter.setGroupArray(list);
        }else{
            searchNewAdapter.clearList();
        }
        for(int i = 0;i< list.size();i++){
            expandableListView.expandGroup(i);
        }
        popupWindow.showAsDropDown(activityHeadLayout);
    }

    @Override
    protected void initHeader(Bundle savedInstanceState) {
        super.initHeader(savedInstanceState);
        initPop();
    }

    @Event(value = {R.id.head_search,R.id.header_left_btn,R.id.city_choose_btn, R.id.head_search_clean, R.id.head_text_right})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.head_search:
                showSoftInputMethod(headSearch);
                break;
            case R.id.header_left_btn:
                if(null != popupWindow) {
                    popupWindow.dismiss();
                }
                finish();
                break;
            case R.id.city_choose_btn:
                finish();
                break;
            case R.id.head_search_clean:
                headSearch.setText("");
                break;
            case R.id.head_text_right:
                 headSearch.setText("");
                 popupWindow.dismiss();
                 collapseSoftInputMethod();
                break;
        }

    }


    LevelCityAdapter levelCityAdapterLeft,levelCityAdapterMiddle,levelCityAdapterRight;
    List<SearchGroupBean> groupList;
    List<SearchGroupBean> groupList2;
    List<SearchGroupBean> groupList3;

    List<SearchGroupBean> list;
    @Override
    protected void initView() {
        genHistoryCity();

        headSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(headSearch.getText())) {
                   list =  CityUtils.search(getActivity(), headSearch.getText().toString());
                    LogUtils.e(list.size()+"===="+headSearch.getText().toString());
                    showSearchPop(list);
                }else{
                    if(null != popupWindow){
                        popupWindow.dismiss();
                    }
                }
            }
        });

        leftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rightList.setVisibility(View.GONE);
                for(SearchGroupBean lineGroupBean:groupList){
                    lineGroupBean.isSelected = false;
                }

                for(int i = 0;i< groupList.size();i++){
                    if(i == position) {
                        groupList.get(i).isSelected = true;
                        levelCityAdapterLeft.notifyDataSetChanged();
                    }
                }
                showMiddleData(position);


            }
        });

        middleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rightList.setVisibility(View.GONE);
                if(groupList2.get(position).spot_id == -1){
                    finish();
                    FgPickSend fgPickSend = new FgPickSend();
                    startFragment(fgPickSend);
                }else if(groupList2.get(position).spot_id == -2){
                    finish();
                    FgSingleNew fgSingleNew = new FgSingleNew();
                    startFragment(fgSingleNew);
                }else if(groupList2.get(position).spot_id == -3){
                    finish();
                    FgOrderSelectCity fgOrderSelectCity = new FgOrderSelectCity();
                    startFragment(fgOrderSelectCity);
                }else {
                    if(CityUtils.canGoCityList(groupList2.get(position))){
                        goCityList(groupList2.get(position));
                    }else {
                        showRightData(position);
                    }
                }
            }
        });

        rightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goCityList(groupList3.get(position));
            }
        });


        levelCityAdapterLeft = new LevelCityAdapter(getActivity(),1);
        SearchGroupBean lineGroupBean = new SearchGroupBean();
        lineGroupBean.group_id = 0;
        lineGroupBean.flag = 1;
        lineGroupBean.type = 1;
        lineGroupBean.sub_city_name = "";
        lineGroupBean.group_name = "热门";
        lineGroupBean.isSelected = true;
        groupList = new ArrayList<>();
        groupList.add(0,lineGroupBean);
        groupList.addAll(CityUtils.getLevel1City(getActivity()));
        levelCityAdapterLeft.setList(groupList);
        leftList.setAdapter(levelCityAdapterLeft);
        levelCityAdapterLeft.notifyDataSetChanged();

        showMiddleData(0);
    }


    private void showRightData(int position){
        for (SearchGroupBean lineGroupBean : groupList2) {
            lineGroupBean.isSelected = false;
        }

        for (int i = 0; i < groupList.size(); i++) {
            if (i == position) {
                groupList2.get(i).isSelected = true;
                levelCityAdapterMiddle.notifyDataSetChanged();
            }
        }
        levelCityAdapterRight = new LevelCityAdapter(getActivity(),3);
        List<SearchGroupBean> list3 = CityUtils.getLevel3City(getActivity(), groupList2.get(position).sub_place_id);
        if(null == list3 || list3.size() == 0){
            goCityList(groupList2.get(position));
        }else {

            SearchGroupBean lineGroupBean = new SearchGroupBean();
            lineGroupBean.group_id = groupList2.get(position).sub_place_id;
            lineGroupBean.flag = 3;
            lineGroupBean.type = 3;
            lineGroupBean.group_name = "";
            lineGroupBean.sub_city_name = "全境";
            lineGroupBean.isSelected = false;
            groupList3 = new ArrayList<>();
            groupList3.add(0, lineGroupBean);
            rightList.setVisibility(View.VISIBLE);
            groupList3.addAll(list3);
            levelCityAdapterRight.setList(groupList3);
            levelCityAdapterRight.notifyDataSetChanged();
            rightList.setAdapter(levelCityAdapterRight);
        }
    }

    private void showMiddleData(int position){
        levelCityAdapterMiddle = new LevelCityAdapter(getActivity(),2);
        if(position == 0) {
            groupList2 = new ArrayList<>();
            groupList2.addAll(CityUtils.getHotCityWithHead(getActivity()));
        }else{
            SearchGroupBean lineGroupBean = new SearchGroupBean();
            lineGroupBean.group_id = groupList.get(position).group_id;
            lineGroupBean.flag = 2;
            lineGroupBean.type = 1;
            lineGroupBean.group_name = "全境";
            lineGroupBean.sub_city_name="";
            lineGroupBean.isSelected = false;
            groupList2 = new ArrayList<>();
            groupList2.add(0, lineGroupBean);
            groupList2.addAll(CityUtils.getLevel2City(getActivity(), groupList.get(position).group_id));
        }
        levelCityAdapterMiddle.setList(groupList2);
        levelCityAdapterMiddle.notifyDataSetChanged();
        middleList.setAdapter(levelCityAdapterMiddle);
    }


    private void genHistoryCity(){
        historyCityLayout.removeAllViews();
        List<SearchGroupBean> list = CityUtils.getSaveCity();
        if(null != list){
            TextView view = null;
            for(int i = 0;i < list.size();i++){
                view = new TextView(getActivity());
                view.setTag(list.get(i));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goCityList((SearchGroupBean)v.getTag());
                    }
                });
                view.setGravity(Gravity.CENTER_VERTICAL);
                String name = CityUtils.getShowName(list.get(i));
                view.setText(name);
                view.setTextColor(Color.parseColor("#666666"));
                view.setHeight(UIUtils.dip2px(50f));
                historyCityLayout.addView(view,0);
            }
        }
    }

    private void goCityList(SearchGroupBean searchGroupBean){
        if(searchGroupBean.flag == 4 || !searchGroupBean.group_name.equalsIgnoreCase("全境")
                || !searchGroupBean.sub_city_name.equalsIgnoreCase("全境") ) {
            CityUtils.addCityHistoryData(searchGroupBean);
        }
        finish();
        if(null != popupWindow){
            popupWindow.dismiss();
        }
        FgSkuList.Params params = new FgSkuList.Params();
        if(searchGroupBean.type == 1){
            params.id = searchGroupBean.group_id;
            params.skuType = FgSkuList.SkuType.ROUTE;
        }else if(searchGroupBean.type == 2){
            params.id = searchGroupBean.sub_place_id;
            params.skuType = FgSkuList.SkuType.COUNTRY;
        }else if(searchGroupBean.type == 3){
            params.id = searchGroupBean.sub_city_id;
            params.skuType = FgSkuList.SkuType.CITY;
        }else if(searchGroupBean.type == 4){
            params.id = searchGroupBean.group_id;
            params.skuType = FgSkuList.SkuType.ROUTE;
        }
        startFragment(FgSkuList.newInstance(params));
    }


    private void addHistoryCity(SearchGroupBean lineGroupBean){
            TextView view = null;
            view = new TextView(getActivity());
            view.setText(lineGroupBean.group_name);
            view.setTag(lineGroupBean.group_id);
            view.setPadding(20,0,20,0);
            view.setHeight(UIUtils.dip2px(50f));
            view.setGravity(Gravity.CENTER_VERTICAL);
            historyCityLayout.addView(view,0);

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public boolean onBackPressed() {
        if(null != popupWindow) {
            popupWindow.dismiss();
        }
        return super.onBackPressed();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = inflater.inflate(R.layout.fg_city_new,null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
