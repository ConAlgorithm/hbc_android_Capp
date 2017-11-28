package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.FavoriteGuideSaved;
import com.hugboga.custom.data.request.FavoriteLinesaved;
import com.hugboga.custom.data.request.RequestCity;
import com.hugboga.custom.data.request.RequestCityHomeList;
import com.hugboga.custom.data.request.RequestCountryGroup;
import com.hugboga.custom.widget.city.CityFilterView;
import com.hugboga.custom.widget.city.CityHeaderFilterView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelBean;
import tk.hongbo.label.data.LabelItemData;
import tk.hongbo.label.data.LabelParentBean;

public class CityListActivity extends BaseActivity {

    Toolbar toolbar;

    @BindView(R.id.city_toolbar_title)
    TextView city_toolbar_title; //Toolbar标题
    @BindView(R.id.city_header_filter_img_root)
    CityHeaderFilterView city_header_filter_img_root; //头部城市信息

    @BindView(R.id.content_city_filte_view1)
    FilterView content_city_filte_view1; //筛选条件内容，游玩线路
    @BindView(R.id.content_city_filte_view2)
    FilterView content_city_filte_view2; //筛选条件内容，出发城市
    @BindView(R.id.content_city_filte_view3)
    FilterView content_city_filte_view3; //筛选条件内容，游玩天数
    @BindView(R.id.city_filter_view)
    CityFilterView cityFilterView; //选择筛选项
    @BindView(R.id.city_list_listview)
    RecyclerView recyclerView; //筛选线路列表

    boolean isFromHome;
    boolean isFromDestination;

    @Override
    public int getContentViewId() {
        return R.layout.activity_city;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.topbar_back);
        if (savedInstanceState != null) {
            paramsData = (CityListActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (CityListActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        EventBus.getDefault().register(this);
        isFromHome = getIntent().getBooleanExtra("isFromHome", false);
        isFromDestination = getIntent().getBooleanExtra("isFromDestination", false);

        //监听筛选项变化
        cityFilterView.setFilterSeeListener(filterSeeListener);

        //初始化数据
        content_city_filte_view1.setData(testData(), new FilterView.OnSelectListener() {
            @Override
            public void onSelect(LabelBean labelBean) {
                content_city_filte_view1.hide();
                cityFilterView.clear();
                //TODO 具体选中的标签
                Snackbar.make(toolbar, "选中内容ID：" + labelBean.id + "，Title：" + labelBean.name, Snackbar.LENGTH_SHORT).show();
            }
        });
        content_city_filte_view2.setData(testData2(), new FilterView.OnSelectListener() {
            @Override
            public void onSelect(LabelBean labelBean) {
                content_city_filte_view2.hide();
                cityFilterView.clear();
                //TODO 具体选中的出发城市
                Snackbar.make(toolbar, "选中内容ID：" + labelBean.id + "，Title：" + labelBean.name, Snackbar.LENGTH_SHORT).show();
            }
        });
        content_city_filte_view3.setData(testData3(), new FilterView.OnSelectListener() {
            @Override
            public void onSelect(LabelBean labelBean) {
                cityFilterView.clear();
                content_city_filte_view3.hide();
                //TODO 具体选中的游玩天数
                Snackbar.make(toolbar, "选中内容ID：" + labelBean.id + "，Title：" + labelBean.name, Snackbar.LENGTH_SHORT).show();
            }
        });

        //初始化首页内容
        RequestCity requestCity = new RequestCity(this, paramsData.id, paramsData.cityHomeType.getType());
        HttpRequestUtils.request(this, requestCity, this);

        showList(); //展示线路数据
    }

    private void showList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CityAdapter adapter = new CityAdapter(this, getSkuTest());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private List getSkuTest() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("Test" + i);
        }
        return data;
    }

    private List<LabelItemData> testData3() {
        List<LabelItemData> data = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            LabelItemData itemData = new LabelItemData();
            List<LabelParentBean> parents = new ArrayList<>();
            //1
            LabelParentBean bean = new LabelParentBean();
            LabelBean beanL = new LabelBean();
            beanL.id = i;
            beanL.name = "1天" + i;
            bean.parentLabel = beanL;
            parents.add(bean);
            //2
            LabelParentBean bean2 = new LabelParentBean();
            LabelBean beanC = new LabelBean();
            beanC.id = i;
            beanC.name = "2~3天" + i;
            bean2.parentLabel = beanC;
            parents.add(bean2);
            //3
            LabelParentBean bean3 = new LabelParentBean();
            LabelBean beanR = new LabelBean();
            beanR.id = i;
            beanR.name = "7天及以上" + i;
            bean3.parentLabel = beanR;
            parents.add(bean3);

            itemData.parent = parents;
            data.add(itemData);
        }
        return data;
    }

    private List<LabelItemData> testData2() {
        List<LabelItemData> data = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            LabelItemData itemData = new LabelItemData();
            List<LabelParentBean> parents = new ArrayList<>();
            //1
            LabelParentBean bean = new LabelParentBean();
            LabelBean beanL = new LabelBean();
            beanL.id = i;
            beanL.name = "东京" + i;
            bean.parentLabel = beanL;
            parents.add(bean);
            //2
            LabelParentBean bean2 = new LabelParentBean();
            LabelBean beanC = new LabelBean();
            beanC.id = i;
            beanC.name = "箱根" + i;
            bean2.parentLabel = beanC;
            parents.add(bean2);
            //3
            LabelParentBean bean3 = new LabelParentBean();
            LabelBean beanR = new LabelBean();
            beanR.id = i;
            beanR.name = "镰仓" + i;
            bean3.parentLabel = beanR;
            parents.add(bean3);

            itemData.parent = parents;
            data.add(itemData);
        }
        return data;
    }

    private List<LabelItemData> testData() {
        List<LabelItemData> data = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            LabelItemData itemData = new LabelItemData();
            List<LabelParentBean> parents = new ArrayList<>();
            //1
            LabelParentBean bean = new LabelParentBean();
            LabelBean beanL = new LabelBean();
            beanL.id = i;
            beanL.name = "关西地区" + i;
            bean.parentLabel = beanL;
            bean.childs = getChild(i);
            parents.add(bean);
            //2
            LabelParentBean bean2 = new LabelParentBean();
            LabelBean beanC = new LabelBean();
            beanC.id = i;
            beanC.name = "北海道地区" + i;
            bean2.parentLabel = beanC;
//            bean2.childs = getChild(i);
            parents.add(bean2);
            //3
            LabelParentBean bean3 = new LabelParentBean();
            LabelBean beanR = new LabelBean();
            beanR.id = i;
            beanR.name = "冲绳地区" + i;
            bean3.parentLabel = beanR;
            bean3.childs = getChild(i);
            parents.add(bean3);

            itemData.parent = parents;
            data.add(itemData);
        }
        return data;
    }

    private List<LabelBean> getChild(int i) {
        //Child
        List<LabelBean> child = new ArrayList<>();
        for (int j = 1; j < 10; j++) {
            LabelBean label = new LabelBean();
            label.id = j + 100;
            label.name = i + "子" + label.id;
            child.add(label);
        }
        return child;
    }

    @OnClick({R.id.city_toolbar_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city_toolbar_title:
                //点击标题
                Intent intent = new Intent(this, ChooseCityNewActivity.class);
                intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_RECOMMEND);
                intent.putExtra("isHomeIn", false);
                intent.putExtra("source", getEventSource());
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Snackbar.make(toolbar, "点击了联系方式按钮！！！", Snackbar.LENGTH_SHORT).show();
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    CityFilterView.FilterSeeListener filterSeeListener = new CityFilterView.FilterSeeListener() {
        @Override
        public void onShowFilter(int position, boolean isSelect) {
            clearContentCityFilteViews();
            switch (position) {
                case 0:
                    if (content_city_filte_view1 != null) {
                        content_city_filte_view1.setVisibility(isSelect ? View.VISIBLE : View.GONE);
                    }
                    break;
                case 1:
                    if (content_city_filte_view2 != null) {
                        content_city_filte_view2.setVisibility(isSelect ? View.VISIBLE : View.GONE);
                    }
                    break;
                case 2:
                    if (content_city_filte_view3 != null) {
                        content_city_filte_view3.setVisibility(isSelect ? View.VISIBLE : View.GONE);
                    }
                    break;
            }
        }
    };

    private void clearContentCityFilteViews() {
        if (content_city_filte_view1 != null) {
            content_city_filte_view1.setVisibility(View.GONE);
        }
        if (content_city_filte_view2 != null) {
            content_city_filte_view2.setVisibility(View.GONE);
        }
        if (content_city_filte_view3 != null) {
            content_city_filte_view3.setVisibility(View.GONE);
        }
    }


    /**************** Old codes *****************************/
    public static final int GUIDE_LIST_COUNT = 8;//精选司导显示的条数

    public enum CityHomeType {
        CITY(202), ROUTE(101), COUNTRY(201), ALL(0);

        int type;

        CityHomeType(int i) {
            this.type = i;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class Params implements Serializable {
        public int id;
        public CityListActivity.CityHomeType cityHomeType;
        public String titleName;
    }

    public CityListActivity.Params paramsData;

    public boolean isShowCity() {
        if (paramsData.cityHomeType == CityHomeType.ROUTE || paramsData.cityHomeType == CityHomeType.COUNTRY) {
            return true;
        } else {
            return false;
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                StringBuilder tempUploadGuilds = new StringBuilder();
                String uploadGuilds = "";
//                if(filterGuideListBean != null && filterGuideListBean.listData != null && filterGuideListBean.listData.size() > 0){
//                    for (FilterGuideBean guild : filterGuideListBean.listData) {
//                        tempUploadGuilds.append(guild.guideId).append(",");
//                    }
//                    if (tempUploadGuilds.length() > 0) {
//                        if (tempUploadGuilds.charAt(tempUploadGuilds.length() - 1) == ',') {
//                            uploadGuilds = (String) tempUploadGuilds.subSequence(0, tempUploadGuilds.length() - 1);
//                        }
//                    }
//                    Log.d("uploadGuilds",uploadGuilds.toString());
//                    FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(this, UserEntity.getUser().getUserId(this),uploadGuilds);
//                    HttpRequestUtils.request(this,favoriteGuideSaved,this,false);
//                }
                break;
            case CLICK_USER_LOOUT:
//                if(filterGuideListBean!= null){
//                    for(int i=0;i<filterGuideListBean.listData.size();i++){
//                        filterGuideListBean.listData.get(i).isCollected = 0;
//                    }
//                    cityListAdapter.notifyDataSetChanged();
//                }
                break;
            case ORDER_DETAIL_UPDATE_COLLECT:
                FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(this, UserEntity.getUser().getUserId(this), null);
                HttpRequestUtils.request(this, favoriteGuideSaved, this, false);
                break;
            case LINE_UPDATE_COLLECT:
                if (UserEntity.getUser().isLogin(this)) {
                    FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(this, UserEntity.getUser().getUserId(this));
                    HttpRequestUtils.request(this, favoriteLinesaved, this, false);
                }
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCity) {
            //首页初始化数据
            DestinationHomeVo data = ((RequestCity) request).getData();
            if (data != null) {
                //修改标题
                city_toolbar_title.setText(data.destinationName);
                if (city_header_filter_img_root != null) {
                    city_header_filter_img_root.init(this, data);
                }
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
        if (paramsData != null) {
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

    /**
     * 打开更多司导界面
     */
    public void clickMoreGuide() {
        Intent intent = new Intent(this, FilterGuideListActivity.class);
        if (paramsData != null) {
            FilterGuideListActivity.Params params = new FilterGuideListActivity.Params();
            params.id = paramsData.id;
            params.cityHomeType = paramsData.cityHomeType;
            params.titleName = paramsData.titleName;
            intent.putExtra(Constants.PARAMS_DATA, params);
            intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        }
        startActivity(intent);
    }

    /**
     * 打开更多SKU界面
     * TODO 有可能不需要点击，@圆确定
     */
    public void clickMoreSku() {
        FilterSkuListActivity.Params params = new FilterSkuListActivity.Params();
        if (paramsData != null) {
            params.id = paramsData.id;
            params.cityHomeType = paramsData.cityHomeType;
            params.titleName = paramsData.titleName;
            params.days = "1,2"; //TODO 游玩天数需要动态获取
        }
        Intent intent = new Intent(this, FilterSkuListActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }

    private void setEmptyLayout(boolean isShow, boolean isDataNull) {
//        emptyLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
//        if (!isShow) {
//            return;
//        }
//        if (isDataNull) {
//            emptyIV.setBackgroundResource(R.drawable.empty_city);
//            emptyHintTV.setText("很抱歉该地区还未开通服务");
//            emptyLayout.setEnabled(false);
//        } else {
//            emptyIV.setBackgroundResource(R.drawable.empty_wifi);
//            emptyHintTV.setText("似乎与网络断开，点击屏幕重试");
//            emptyLayout.setEnabled(true);
//            emptyLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    requestCityList();
//                }
//            });
//        }
    }
}
