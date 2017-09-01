package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.SearchUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/22.
 */

public class SearchDestinationModel extends EpoxyModelWithHolder<SearchDestinationModel.SearchDestinationHolder> {
    Context context;
    SearchDestinationHolder searchDestinationHolder;
    String keyword;
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
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        context.startActivity(intent);
    }

    public String getEventSource(){
        return "搜索";
    }
}
