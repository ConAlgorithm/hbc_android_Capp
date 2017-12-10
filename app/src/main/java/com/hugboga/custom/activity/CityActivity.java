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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.UserFavoriteLineList;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.data.bean.city.PageQueryDestinationGoodsVo;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.FavoriteLinesaved;
import com.hugboga.custom.data.request.RequestCity;
import com.hugboga.custom.data.request.RequestQuerySkuList;
import com.hugboga.custom.utils.CityDataTools;
import com.hugboga.custom.widget.city.CityFilterContentView;
import com.hugboga.custom.widget.city.CityFilterView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelBean;

import static com.hugboga.custom.activity.CityActivity.CityHomeType.COUNTRY;

public class CityActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.city_toolbar_root)
    LinearLayout city_toolbar_root; //toolbar和筛选层
    @BindView(R.id.city_toolbar_title)
    TextView city_toolbar_title; //Toolbar标题
    @BindView(R.id.city_filter_con_view)
    CityFilterContentView filterContentView; //筛选弹出框

    @BindView(R.id.city_list_listview)
    RecyclerView recyclerView; //筛选线路列表

    boolean isFromHome;
    boolean isFromDestination;

    DestinationHomeVo data; //目的地初始化数据

    LabelBean labelBeanTag; //筛选项游玩线路标签
    LabelBean labelBeanCity; //筛选项出发城市
    LabelBean labelBeanDay; //筛选项游玩天数

    CityAdapter adapter;
    private int page = 1; //sku页数

    CityDataTools cityDataTools = new CityDataTools();

    @Override
    public int getContentViewId() {
        return R.layout.activity_city;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.topbar_back);
        if (savedInstanceState != null) {
            paramsData = (CityActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (CityActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        EventBus.getDefault().register(this);
        isFromHome = getIntent().getBooleanExtra("isFromHome", false);
        isFromDestination = getIntent().getBooleanExtra("isFromDestination", false);

        //初始化首页内容
        RequestCity requestCity = new RequestCity(this, paramsData.id, paramsData.cityHomeType.getType());
        HttpRequestUtils.request(this, requestCity, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //展示线路数据
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                onScrollFloat(dy);
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    // 滚动到底部加载更多
                    page++;
                    flushSkuList();
                }
            }
        });
    }

    /**
     * 滚动效果修改
     *
     * @param dy
     */
    private void onScrollFloat(int dy) {
        /*
        1. 默认，有toolbar，无filterview
        2. adapterFilterView滑动toolbar底部，动画去除toolbar，filterview滑动顶部
        3. 下滑toolbar动画出来，上滑toolbar去除
         */
        if (adapter.cityFilterModel.cityFilterView != null) {
            if (dy < 0) {
                //向下滑动
                if (city_toolbar_root.getTop() != 0) {
                    translate(true);
                }
                if (adapter.cityFilterModel.cityFilterView.getTop() >= toolbar.getBottom() && filterContentView.getVisibility() == View.VISIBLE) {
                    //filterView出来，toolbar退出
                    filterContentView.setVisibility(View.GONE);
                }
            } else if (dy > 0) {
                //向上滑动
                if (adapter.cityFilterModel.cityFilterView.getTop() <= toolbar.getBottom() && filterContentView.getVisibility() == View.GONE) {
                    //filterView出来，toolbar退出
                    filterContentView.setVisibility(View.VISIBLE);
                }
                if (adapter.cityFilterModel.cityFilterView.getTop() < 0 && city_toolbar_root.getTop() == 0) {
                    translate(false);
                }
            }
        }
    }

    private void translate(final boolean isShow) {
        int height = toolbar.getHeight();
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, height);
        if (!isShow) {
            translateAnimation = new TranslateAnimation(0, 0, 0, -height);
        }
        translateAnimation.setDuration(100);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                city_toolbar_root.clearAnimation();
                int top = isShow ? 0 : -toolbar.getHeight();
                city_toolbar_root.layout(0, top, city_toolbar_root.getWidth(), top + city_toolbar_root.getHeight());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        city_toolbar_root.startAnimation(translateAnimation);
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

    CityFilterContentView.FilterConSelect filterConSelect1 = new CityFilterContentView.FilterConSelect() {

        @Override
        public void onSelect(FilterView filterView, LabelBean labelBean) {
            labelBeanTag = labelBean;
            page = 1; //筛选条件后重置页数为首页
            flushSkuList();
        }
    };

    CityFilterContentView.FilterConSelect filterConSelect2 = new CityFilterContentView.FilterConSelect() {

        @Override
        public void onSelect(FilterView filterView, LabelBean labelBean) {
            labelBeanCity = labelBean;
            page = 1; //筛选条件后重置页数为首页
            flushSkuList();
        }
    };

    CityFilterContentView.FilterConSelect filterConSelect3 = new CityFilterContentView.FilterConSelect() {

        @Override
        public void onSelect(FilterView filterView, LabelBean labelBean) {
            labelBeanDay = labelBean;
            page = 1; //筛选条件后重置页数为首页
            flushSkuList();
        }
    };

    /**
     * 根据条件筛选玩法
     */
    private void flushSkuList() {
        RequestQuerySkuList requestQuerySkuList = new RequestQuerySkuList(this, paramsData.id,
                paramsData.cityHomeType.getType(), labelBeanDay, labelBeanTag, labelBeanCity, page);
        HttpRequestUtils.request(this, requestQuerySkuList, this, page == 1);
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

        public static CityHomeType getNew(int type) {
            switch (type) {
                case 101:
                    return ROUTE;
                case 201:
                    return COUNTRY;
                case 202:
                    return CITY;
                default:
                    return ALL;
            }
        }
    }

    public static class Params implements Serializable {
        public int id;
        public CityActivity.CityHomeType cityHomeType;
        public String titleName;
    }

    public CityActivity.Params paramsData;

    public boolean isShowCity() {
        if (paramsData.cityHomeType == CityHomeType.ROUTE || paramsData.cityHomeType == COUNTRY) {
            return true;
        } else {
            return false;
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case LINE_UPDATE_COLLECT:
                //查询已收藏线路
                queryFavoriteLineList();
                break;
            case CLICK_USER_LOGIN:
                queryFavoriteLineList();
                break;
        }
    }

    /**
     * 查询已收藏线路数据
     */
    private void queryFavoriteLineList() {
        if (UserEntity.getUser().isLogin(this)) {
            FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(this, UserEntity.getUser().getUserId(this));
            HttpRequestUtils.request(this, favoriteLinesaved, this, false);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCity) {
            //首页初始化数据
            data = ((RequestCity) request).getData();
            if (data != null) {
                //修改标题
                city_toolbar_title.setText(data.destinationName);
                //设置过滤条件筛选中的数据
                filterContentView.setData(data, filterConSelect1, filterConSelect2, filterConSelect3);
                // 设置玩法列表初始化数据
                if (data.destinationGoodsList != null && data.destinationGoodsList.size() > 0) {
                    flushSkuList(data.destinationGoodsList);
                } else {
                    page = 1; //无条件查询玩法
                    flushSkuList();
                }
            }
            //初始化已收藏线路数据
            queryFavoriteLineList();
        } else if (request instanceof RequestQuerySkuList) {
            //条件筛选玩法
            PageQueryDestinationGoodsVo vo = (PageQueryDestinationGoodsVo) request.getData();
            if (vo != null) {
                flushSkuList(vo.destinationGoodsList);
            }
        } else if (request instanceof FavoriteLinesaved) {
            //查询出已收藏线路信息
            if (request.getData() != null) {
                UserFavoriteLineList favoriteLine = (UserFavoriteLineList) request.getData();
                if (adapter != null && favoriteLine != null && favoriteLine.goodsNos != null) {
                    adapter.resetFavious(favoriteLine.goodsNos);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void resetFilterView() {
        //设置标签部分
        if (data.destinationGoodsCount > 0) {
            //有玩法数据则显示筛选器
            adapter.showFilterModel(true);
        } else {
            //无玩法数据隐藏筛选器
            adapter.showFilterModel(false);
        }
    }

    /**
     * 设置玩法列表数据
     *
     * @param destinationGoodsList
     */
    private void flushSkuList(List<DestinationGoodsVo> destinationGoodsList) {
        boolean isInit = false;
        if (adapter == null) {
            isInit = true; //初次加载数据
            adapter = new CityAdapter(this, data, destinationGoodsList, data.serviceConfigList,
                    cityDataTools.getTagData(data.destinationTagGroupList), filterContentView.onSelectListener1);
            recyclerView.setAdapter(adapter);
            adapter.cityFilterModel.filterSeeListener = filterSeeListener;
        }
        if (page == 1) {
            adapter.load(destinationGoodsList, isInit);
        } else {
            adapter.addMoreGoods(destinationGoodsList);
        }
        // 依赖Adapter都放在这里
        filterContentView.setAdapter(adapter); //Filter需要数据设置
        //构建筛选器
        resetFilterView();
    }

    /**
     * Adapter中FilterModel筛选项点击事件
     */
    CityFilterView.FilterSeeListener filterSeeListener = new CityFilterView.FilterSeeListener() {
        @Override
        public void onShowFilter(final int position, final boolean isSelect) {
            adapter.cityFilterModel.cityFilterView.clear();
            //展示滑动效果，滑动到顶部筛选模式
            filterContentView.setVisibility(View.VISIBLE);
            filterContentView.showFilterItem(position, isSelect);
            recyclerView.scrollBy(0, adapter.cityFilterModel.cityFilterView.getTop() - toolbar.getHeight());
        }
    };

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
}
