package com.hugboga.custom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityListAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityListBean;
import com.hugboga.custom.data.bean.CountryGroupBean;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.bean.FilterGuideListBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.UserFavoriteGuideListVo3;
import com.hugboga.custom.data.bean.UserFavoriteLineList;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.FavoriteGuideSaved;
import com.hugboga.custom.data.request.FavoriteLinesaved;
import com.hugboga.custom.data.request.RequestCityHomeList;
import com.hugboga.custom.data.request.RequestCountryGroup;
import com.hugboga.custom.data.request.RequestFilterGuide;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.GiftController;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.OnClick;

public class CityListActivity extends BaseActivity {

    public static final int GUIDE_LIST_COUNT = 8;//精选司导显示的条数

    @Bind(R.id.city_list_titlebar)
    RelativeLayout titlebar;
    @Bind(R.id.view_bottom)
    View titlebarBottomLineView;
    @Bind(R.id.city_list_recyclerview)
    RecyclerView recyclerView;

    @Bind(R.id.city_list_back_layout)
    FrameLayout backLayout;

    @Bind(R.id.city_list_empty_layout)
    LinearLayout emptyLayout;
    @Bind(R.id.city_list_empty_iv)
    ImageView emptyIV;
    @Bind(R.id.city_list_empty_hint_tv)
    TextView emptyHintTV;

    @Bind(R.id.city_list_service_layout)
    LinearLayout serviceLayout;
    @Bind(R.id.city_list_service_hint_tv)
    TextView serviceHintTV;

    public CityListActivity.Params paramsData;
    private CityListAdapter cityListAdapter;
    private CountryGroupBean countryGroupBean;
    private CityListBean cityListBean;
    CsDialog csDialog;
    boolean isFromHome;
    boolean isFromDestination;
    public enum CityHomeType {
        CITY, ROUTE, COUNTRY, ALL
    }

    public static class Params implements Serializable {
        public int id;
        public CityListActivity.CityHomeType cityHomeType;
        public String titleName;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_city_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (CityListActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (CityListActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        EventBus.getDefault().register(this);
        isFromHome = getIntent().getBooleanExtra("isFromHome",false);
        isFromDestination = getIntent().getBooleanExtra("isFromDestination",false);
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GiftController.getInstance(this).showGiftDialog();
    }

    @Override
    public void onPause() {
        super.onPause();
        GiftController.getInstance(this).abortion();
    }

    protected void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText(paramsData.titleName);
        fgTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.share_unfold, 0);
        fgTitle.setCompoundDrawablePadding(UIUtils.dip2px(3));
        if (paramsData.cityHomeType == CityListActivity.CityHomeType.CITY) {
            titlebar.setBackgroundColor(0x00000000);
            titlebarBottomLineView.setBackgroundColor(0x00000000);
            fgTitle.setAlpha(0);
            fgLeftBtn.setAlpha(0);
            backLayout.setVisibility(View.VISIBLE);
        } else {
            backLayout.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.topMargin = UIUtils.getActionBarSize();
            recyclerView.setLayoutParams(params);
        }
        fgTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ChooseCityNewActivity.class);
                intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_RECOMMEND);
                intent.putExtra("isHomeIn", false);
                intent.putExtra("source", getEventSource());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void initView() {
        initTitleBar();
        cityListAdapter = new CityListAdapter();
        cityListAdapter.setData(paramsData);
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cityListAdapter);
        requestCityList();
        setOnScrollListener();
        setSensorsEvent();
    }

    public void setOnScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (paramsData.cityHomeType == CityHomeType.CITY && cityListAdapter.cityListHeaderModel != null) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    int scrollY = Math.abs(recyclerView.getChildAt(0).getTop());
                    float showRegionHight = cityListAdapter.cityListHeaderModel.getDisplayLayoutHeight() / 2.0f;
                    if (firstVisibleItemPosition == 0 && scrollY <= showRegionHight) {
                        float alpha;
                        if (scrollY <= 0) {
                            alpha = 0.0f;
                        } else {
                            alpha = Math.min(1, scrollY / showRegionHight);
                        }
                        titlebar.setBackgroundColor(UIUtils.getColorWithAlpha(alpha, 0xFFFFFFFF));
                        titlebarBottomLineView.setBackgroundColor(UIUtils.getColorWithAlpha(alpha, 0xFFE5E5E5));
                        backLayout.setAlpha(1 - alpha);
                        fgLeftBtn.setAlpha(alpha);
                        fgTitle.setAlpha(0);
                        fgTitle.setEnabled(false);
                    } else {
                        showTitleBar();
                    }
                }
            }
        });
    }

    private void showTitleBar() {
        titlebar.setBackgroundColor(0xFFFFFFFF);
        titlebarBottomLineView.setBackgroundColor(0xFFE5E5E5);
        fgTitle.setEnabled(true);
        fgTitle.setAlpha(1);
        fgLeftBtn.setAlpha(1);
        backLayout.setAlpha(0);
    }

    public void requestCityList() {
        BaseRequest baseRequest = null;
        switch (paramsData.cityHomeType) {
            case CITY:
                baseRequest = new RequestCityHomeList(this, "" + paramsData.id);
                break;
            case ROUTE:
                baseRequest = new RequestCountryGroup(this, paramsData.id, "1");
                break;
            case COUNTRY:
                baseRequest = new RequestCountryGroup(this, paramsData.id, "2");
                break;
        }
        requestData(baseRequest);
    }

    public void requestGuideList() {
        RequestFilterGuide.Builder builder = new RequestFilterGuide.Builder();
        switch (paramsData.cityHomeType) {
            case CITY:
                builder.setCityIds("" + paramsData.id);
                break;
            case ROUTE:
                builder.setLineGroupId("" + paramsData.id);
                break;
            case COUNTRY:
                builder.setCountryId("" + paramsData.id);
                break;
        }
        builder.setLimit(GUIDE_LIST_COUNT);
        requestData(new RequestFilterGuide(this, builder));
    }
    FilterGuideListBean filterGuideListBean;
    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCityHomeList) {
            cityListBean = ((RequestCityHomeList) _request).getData();
            boolean isCanService = cityListBean != null && cityListBean.isCanService();
            if (isCanService) {
                setEmptyLayout(false, true);
                cityListAdapter.setCityData(cityListBean);
                fgTitle.setText(cityListBean.cityContent.cityName);
                requestGuideList();
            } else {
                setEmptyLayout(true, true);
            }
            if(UserEntity.getUser().isLogin(this)){
                FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(this,UserEntity.getUser().getUserId(this));
                HttpRequestUtils.request(this,favoriteLinesaved,this,false);
            }
        } else if (_request instanceof RequestCountryGroup) {
            setEmptyLayout(false, true);
            countryGroupBean = ((RequestCountryGroup) _request).getData();
            if (!countryGroupBean.isEmpty()) {
                cityListAdapter.setCountryGroupData(countryGroupBean);
            }
            if(UserEntity.getUser().isLogin(this)){
                FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(this,UserEntity.getUser().getUserId(this));
                HttpRequestUtils.request(this,favoriteLinesaved,this,false);
            }
            requestGuideList();
        } else if (_request instanceof RequestFilterGuide) {
            filterGuideListBean = ((RequestFilterGuide) _request).getData();
            if (paramsData.cityHomeType != CityHomeType.CITY && (countryGroupBean == null || countryGroupBean.isEmpty()) && filterGuideListBean.listCount == 0) {
                setEmptyLayout(true, true);
            } else {
                setEmptyLayout(false, true);
            }
            if (paramsData.cityHomeType == CityHomeType.CITY
                    && filterGuideListBean.listCount == 0
                    && cityListBean != null
                    && (cityListBean.hotLines == null || cityListBean.hotLines.size() <= 0)) {
                CityBean cityBean = DatabaseManager.getCityBean("" + paramsData.id);
                if (cityBean != null && !TextUtils.isEmpty(cityBean.placeName)) {
                    serviceLayout.setVisibility(View.VISIBLE);
                    serviceHintTV.setText(String.format("定制个性化行程，可咨询%1$s行程规划师", cityBean.placeName));
                }
            }
            if(UserEntity.getUser().isLogin(this)){
                FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(this,UserEntity.getUser().getUserId(this),null);
                HttpRequestUtils.request(this,favoriteGuideSaved,this,false);
            }
            cityListAdapter.setGuideListData(this,filterGuideListBean.listData, filterGuideListBean.listCount);
        }else if (_request instanceof FavoriteGuideSaved){
            if(_request.getData() instanceof UserFavoriteGuideListVo3){
                for(int j=0;j<filterGuideListBean.listData.size();j++){
                    filterGuideListBean.listData.get(j).isCollected = 0;
                }
                UserFavoriteGuideListVo3 favoriteGuideSavedBean = (UserFavoriteGuideListVo3)_request.getData();
                for(int i=0 ;i< favoriteGuideSavedBean.guides.size();i++){
                    for(int j=0;j<filterGuideListBean.listData.size();j++){
                        if(favoriteGuideSavedBean.guides.get(i).equals(filterGuideListBean.listData.get(j).guideId)){
                            filterGuideListBean.listData.get(j).isCollected = 1;
                        }
                    }
                }
                cityListAdapter.notifyDataSetChanged();
            }

        }else if(_request instanceof FavoriteLinesaved){
            if(_request.getData() instanceof UserFavoriteLineList){
                if(countryGroupBean!= null){
                    //国家页深度线路
                    if(countryGroupBean.deepLines != null && countryGroupBean.deepLines.size() >0){
                        for(int m = 0;m< countryGroupBean.deepLines.size();m++){
                            countryGroupBean.deepLines.get(m).favorited = 0;
                        }
                        UserFavoriteLineList userFavoriteLineList = (UserFavoriteLineList) _request.getData();
                        for(int n = 0;n<userFavoriteLineList.goodsNos.size();n++){
                            for(int q=0;q<countryGroupBean.deepLines.size();q++){
                                if(userFavoriteLineList.goodsNos.get(n).equals(countryGroupBean.deepLines.get(q).goodsNo)){
                                    countryGroupBean.deepLines.get(q).favorited =1;
                                }
                            }
                        }
                    }

                    //国家页浅度线路
                    if(countryGroupBean.shortLines != null && countryGroupBean.shortLines.size()>0){
                        for(int m = 0;m< countryGroupBean.shortLines.size();m++){
                            countryGroupBean.shortLines.get(m).favorited = 0;
                        }
                        UserFavoriteLineList userFavoriteLineList = (UserFavoriteLineList) _request.getData();
                        for(int n = 0;n<userFavoriteLineList.goodsNos.size();n++){
                            for(int q=0;q<countryGroupBean.shortLines.size();q++){
                                if(userFavoriteLineList.goodsNos.get(n).equals(countryGroupBean.shortLines.get(q).goodsNo)){
                                    countryGroupBean.shortLines.get(q).favorited =1;
                                }
                            }
                        }
                    }

                }

                //城市页热门线路
                if(cityListBean!= null){
                    if(cityListBean.hotLines!= null && cityListBean.hotLines.size()>0){
                        for(int x=0;x<cityListBean.hotLines.size();x++){
                            cityListBean.hotLines.get(x).favorited = 0;
                        }
                        UserFavoriteLineList userFavoriteLineList = (UserFavoriteLineList) _request.getData();
                        for(int n = 0;n<userFavoriteLineList.goodsNos.size();n++){
                            for(int q=0;q<cityListBean.hotLines.size();q++){
                                if(userFavoriteLineList.goodsNos.get(n).equals(cityListBean.hotLines.get(q).goodsNo)){
                                    cityListBean.hotLines.get(q).favorited =1;
                                }
                            }
                        }
                    }

                }
                cityListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (request instanceof RequestCityHomeList || request instanceof RequestCountryGroup) {
            setEmptyLayout(true, false);
        }
    }

    @Override
    public String getEventSource() {
        String result = "";
        if(isFromHome){
            return "全局搜索";
        }else if(isFromDestination){
            return "目的地搜索";
        }
        if(paramsData!= null){
            switch (paramsData.cityHomeType) {
                case CITY:
                    result = "城市";
                    break;
                case ROUTE:
                    result = "线路圈";
                    break;
                case COUNTRY:
                    result = "国家";
                    break;
            }
        }
        return result;
    }

    @Override
    protected String getPageTitle() {
        return paramsData != null ? paramsData.titleName : "";
    }

    private void setEmptyLayout(boolean isShow, boolean isDataNull) {
        emptyLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (!isShow) {
            return;
        }
        if (isDataNull) {
            emptyIV.setBackgroundResource(R.drawable.empty_city);
            emptyHintTV.setText("很抱歉该地区还未开通服务");
            emptyLayout.setEnabled(false);
        } else {
            emptyIV.setBackgroundResource(R.drawable.empty_wifi);
            emptyHintTV.setText("似乎与网络断开，点击屏幕重试");
            emptyLayout.setEnabled(true);
            emptyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestCityList();
                }
            });
        }
        showTitleBar();
    }

    public boolean isShowCity() {
        if (paramsData.cityHomeType == CityHomeType.ROUTE || paramsData.cityHomeType == CityHomeType.COUNTRY) {
            return true;
        } else {
            return false;
        }
    }

    @OnClick(R.id.city_list_service_tv)
    public void showServiceDialog() {
        //DialogUtil.showServiceDialog(CityListActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
        csDialog = CommonUtils.csDialog(CityListActivity.this, null, null, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, getEventSource(), new CsDialog.OnCsListener() {
            @Override
            public void onCs() {
                if (csDialog != null && csDialog.isShowing()) {
                    csDialog.dismiss();
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                StringBuilder tempUploadGuilds = new StringBuilder();
                String uploadGuilds = "";
                if(filterGuideListBean != null && filterGuideListBean.listData != null && filterGuideListBean.listData.size() > 0){
                    for (FilterGuideBean guild : filterGuideListBean.listData) {
                        tempUploadGuilds.append(guild.guideId).append(",");
                    }
                    if (tempUploadGuilds.length() > 0) {
                        if (tempUploadGuilds.charAt(tempUploadGuilds.length() - 1) == ',') {
                            uploadGuilds = (String) tempUploadGuilds.subSequence(0, tempUploadGuilds.length() - 1);
                        }
                    }
                    Log.d("uploadGuilds",uploadGuilds.toString());
                    FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(this,UserEntity.getUser().getUserId(this),uploadGuilds);
                    HttpRequestUtils.request(this,favoriteGuideSaved,this,false);
                }
                break;
            case CLICK_USER_LOOUT:
                if(filterGuideListBean!= null){
                    for(int i=0;i<filterGuideListBean.listData.size();i++){
                        filterGuideListBean.listData.get(i).isCollected = 0;
                    }
                    cityListAdapter.notifyDataSetChanged();
                }
                break;
            case ORDER_DETAIL_UPDATE_COLLECT:
                FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(this,UserEntity.getUser().getUserId(this),null);
                HttpRequestUtils.request(this,favoriteGuideSaved,this,false);
                break;
            case LINE_UPDATE_COLLECT:
                if(UserEntity.getUser().isLogin(this)){
                    FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(this,UserEntity.getUser().getUserId(this));
                    HttpRequestUtils.request(this,favoriteLinesaved,this,false);
                }
        }
    }

    //神策统计_浏览城市/国家页
    private void setSensorsEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("refer", getIntentSource());
            switch (paramsData.cityHomeType) {
                case CITY:
                    properties.put("cityId", paramsData.id);
                    properties.put("cityName", paramsData.titleName);
                    break;
                case ROUTE:
                    properties.put("lineGroupId", paramsData.id);
                    properties.put("lineGroupName", paramsData.titleName);
                    break;
                case COUNTRY:
                    properties.put("countryId", paramsData.id);
                    properties.put("countryName", paramsData.titleName);
                    break;
            }
            SensorsDataAPI.sharedInstance(this).track("viewCity", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
