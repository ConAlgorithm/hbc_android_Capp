package com.hugboga.custom.fragment;

import android.os.Bundle;
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

import org.xutils.common.Callback;

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
    LinearLayout historyCityLayout;
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


    LevelCityAdapter levelCityAdapterLeft,levelCityAdapterMiddle,levelCityAdapterRight;
    List<SearchGroupBean> groupList;
    List<SearchGroupBean> groupList2;
    List<SearchGroupBean> groupList3;
    @Override
    protected void initView() {
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

                levelCityAdapterMiddle = new LevelCityAdapter(getActivity());
                SearchGroupBean lineGroupBean = new SearchGroupBean();
                lineGroupBean.group_id = 0;
                lineGroupBean.flag = 2;
                lineGroupBean.group_name = "全境";
                lineGroupBean.isSelected = true;
                groupList2 = new ArrayList<>();
                groupList2.add(0,lineGroupBean);
                groupList2.addAll(CityUtils.getLevel2City(getActivity(),groupList.get(position).group_id));
                levelCityAdapterMiddle.setList(groupList2);
                levelCityAdapterMiddle.notifyDataSetChanged();
                middleList.setAdapter(levelCityAdapterMiddle);


            }
        });

        middleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rightList.setVisibility(View.GONE);
                for(SearchGroupBean lineGroupBean:groupList2){
                    lineGroupBean.isSelected = false;
                }

                for(int i = 0;i< groupList.size();i++){
                    if(i == position) {
                        groupList2.get(i).isSelected = true;
                        levelCityAdapterMiddle.notifyDataSetChanged();
                    }
                }

                levelCityAdapterRight = new LevelCityAdapter(getActivity());
                List<SearchGroupBean> list3 = CityUtils.getLevel3City(getActivity(),groupList.get(position).group_id);
                if(null == list3){
                    rightList.setVisibility(View.GONE);
                }else{
                    SearchGroupBean lineGroupBean = new SearchGroupBean();
                    lineGroupBean.group_id = 0;
                    lineGroupBean.flag = 3;
                    lineGroupBean.group_name = "全境";
                    lineGroupBean.isSelected = true;
                    groupList3 = new ArrayList<>();
                    groupList3.add(0,lineGroupBean);
                    rightList.setVisibility(View.VISIBLE);
                    groupList3.addAll(list3);
                    levelCityAdapterRight.setList(groupList3);
                    levelCityAdapterRight.notifyDataSetChanged();
                    rightList.setAdapter(levelCityAdapterRight);
                }
            }
        });


        levelCityAdapterLeft = new LevelCityAdapter(getActivity());
        SearchGroupBean lineGroupBean = new SearchGroupBean();
        lineGroupBean.group_id = 0;
        lineGroupBean.flag = 1;
        lineGroupBean.group_name = "热门";
        lineGroupBean.isSelected = true;
        groupList = new ArrayList<>();
        groupList.add(0,lineGroupBean);
        groupList.addAll(CityUtils.getLevel1City(getActivity()));
        levelCityAdapterLeft.setList(groupList);
        leftList.setAdapter(levelCityAdapterLeft);
        levelCityAdapterLeft.notifyDataSetChanged();
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
