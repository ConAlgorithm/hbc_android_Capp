package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.adapter.SearchNewAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.SearchUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/22.
 */

public class SearchDestinationModel extends EpoxyModelWithHolder<SearchDestinationModel.SearchDestinationHolder> {
    Context context;
    SearchDestinationHolder searchDestinationHolder;
    SearchNewAdapter searchNewAdapter;
    LinearLayout tv_footer;
    List<SearchGroupBean> listAll;
    List<SearchGroupBean> listfirst;
    List<SearchGroupBean> listAfter;
    String keyword;
    int showListType = 0;//1:first 2:after 3:all
    SearchGroupBean searchGroupBean;
    public SearchDestinationModel(Context context,SearchGroupBean searchGroupBean,String keyword) {
        this.context = context;
        this.searchGroupBean = searchGroupBean;
        this.keyword = keyword;
    }

    @Override
    protected SearchDestinationHolder createNewHolder() {
        return new SearchDestinationHolder();
    }

    @Override
    protected int getDefaultLayout() {
        //return R.layout.search_destination;
        return  R.layout.search_item_layou;
    }

    @Override
    public void bind(SearchDestinationHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        searchDestinationHolder =holder;
        init();
    }

    static class SearchDestinationHolder extends EpoxyHolder {
        View itemView;
        @Bind(R.id.left_name)
        TextView left_name;
        @Bind(R.id.right_name)
        TextView right_name;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    private void init(){
        if(searchDestinationHolder!= null){
            SpannableString leftName = SearchUtils.matcherSearchText(context,context.getResources().getColor(R.color.all_bg_yellow),CityUtils.getShowName(searchGroupBean),keyword);
            SpannableString rightName = SearchUtils.matcherSearchText(context,context.getResources().getColor(R.color.all_bg_yellow),CityUtils.getParentName(searchGroupBean),keyword);
            searchDestinationHolder.left_name.setText(leftName);
            searchDestinationHolder.right_name.setText(rightName);

            searchDestinationHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goCityList(searchGroupBean);
                }
            });
        }
    }
//    private void init() {
//        tv_footer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.search_destination_footer_layout, searchDestinationHolder.expandableListView, false);
//        searchDestinationHolder.expandableListView.setChildIndicator(null);
//        searchDestinationHolder.expandableListView.setGroupIndicator(null);
//        searchDestinationHolder.expandableListView.setChildDivider(new ColorDrawable());
//        searchNewAdapter = new SearchNewAdapter((Activity) context);
//        searchDestinationHolder.expandableListView.setAdapter(searchNewAdapter);
//        searchDestinationHolder.expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
////                ToastUtils.showShort(groupPosition+"======");
//                if (showListType == 1 && listfirst != null && listfirst.size() > 0) {
//                    goCityList(listfirst.get(groupPosition));
//                } else if (showListType == 2 && listAfter != null && listAfter.size() > 0) {
//                    goCityList(listAfter.get(groupPosition));
//                } else if (showListType == 3 && listAll != null && listAll.size() > 0) {
//                    goCityList(listAll.get(groupPosition));
//                }
//
//                Map map = new HashMap();
//                map.put("source", ((BaseActivity) context).getIntentSource());
//                map.put("searchinput", "输入内容后联想");
//                MobClickUtils.onEvent(StatisticConstant.SEARCH, map);
//                if (((BaseActivity) context).getIntentSource().equals("首页")) {
//                    setSensorsShareEvent(keyword, false, true, true);
//                }
//                return true;
//            }
//        });
//    }
//
//    public void setListAll(List<SearchGroupBean> listAll, String keyword) {
//        this.listAll = listAll;
//        this.keyword = keyword;
//    }
//
//    public void setListfirst(List<SearchGroupBean> listfirst, String keyword) {
//        this.listfirst = listfirst;
//        this.keyword = keyword;
//
//    }
//
//    public void setListAfter(List<SearchGroupBean> listAfter, String keyword) {
//        this.listAfter = listAfter;
//        this.keyword = keyword;
//    }
//
//    public void changeListFirst() {
//        searchNewAdapter.setKey(keyword);
//        searchNewAdapter.setGroupArray(listfirst);
//        showFirstFooter();
//    }
//
//    public void changeListAfter() {
//        searchNewAdapter.setKey(keyword);
//        searchNewAdapter.setGroupArray(listAfter);
//        showAfterFooter();
//    }
//
//    public void changeListAll(){
//        searchNewAdapter.setKey(keyword);
//        searchNewAdapter.setGroupArray(listAll);
//    }
//
//    public void setKeyWord(String keyword) {
//        this.keyword = keyword;
//    }
//
//    public void setListType(int showListType) {
//        this.showListType = showListType;
//    }
//
    private void goCityList(SearchGroupBean searchGroupBean) {
        SearchUtils.addCityHistorySearch(CityUtils.getShowName(searchGroupBean));

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
        Intent intent = new Intent(context, CityListActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra("source", "搜索");
        context.startActivity(intent);
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

}
