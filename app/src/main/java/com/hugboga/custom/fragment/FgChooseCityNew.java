package com.hugboga.custom.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.LevelCityAdapter;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.ToastUtils;
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

    }

    @Override
    protected void initHeader(Bundle savedInstanceState) {
        super.initHeader(savedInstanceState);
    }

    @Event(value = {R.id.header_left_btn,R.id.city_choose_btn, R.id.head_search_clean, R.id.head_text_right})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.city_choose_btn:
                finish();
                break;
            case R.id.head_search_clean:
                if(TextUtils.isEmpty(headSearch.getText().toString().trim())){
                    break;
                }
                headSearch.setText("");
                break;
            case R.id.head_text_right:
                String keyword = headSearch.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    ToastUtils.showLong("请输入搜索内容");
                    return;
                }
                collapseSoftInputMethod();
//                requestDataByKeyword(getBusinessType(), groupId, keyword, false); //进行点击搜索
                break;
        }

    }


    LevelCityAdapter levelCityAdapterLeft,levelCityAdapterMiddle,levelCityAdapterRight;
    List<SearchGroupBean> groupList;
    List<SearchGroupBean> groupList2;
    List<SearchGroupBean> groupList3;
    @Override
    protected void initView() {
        genHistoryCity();

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


        levelCityAdapterLeft = new LevelCityAdapter(getActivity());
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
        levelCityAdapterRight = new LevelCityAdapter(getActivity());
        List<SearchGroupBean> list3 = CityUtils.getLevel3City(getActivity(), groupList2.get(position).sub_place_id);

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

    private void showMiddleData(int position){
        levelCityAdapterMiddle = new LevelCityAdapter(getActivity());
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
                if(i == 0){
                    view.setPadding(30,0,15,0);
                }else{
                    view.setPadding(15,0,15,0);
                }
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
        FgSkuList fgSkuList = new FgSkuList();
        startFragment(fgSkuList);
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
