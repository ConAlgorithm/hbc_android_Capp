package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.data.bean.city.PageQueryDestinationGoodsVo;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCity;
import com.hugboga.custom.data.request.RequestQuerySkuList;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CityDataTools;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.city.CityFilterContentView;
import com.hugboga.custom.widget.city.CityFilterView;
import com.hugboga.tools.HLog;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnreadCountChangeListener;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import tk.hongbo.label.FilterView;
import tk.hongbo.label.data.LabelBean;

import static com.hugboga.custom.activity.CityActivity.CityHomeType.COUNTRY;

public class CityActivity extends BaseActivity {

    @BindView(R.id.city_toolbar_root)
    LinearLayout city_toolbar_root; //toolbar和筛选层
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    private int destinationGoodsCount; //玩法总数量
    private int page = 1; //sku页数
    private MenuItem item;

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
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.top_back_black);
        if (savedInstanceState != null) {
            paramsData = (CityActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (CityActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        isFromHome = getIntent().getBooleanExtra("isFromHome", false);
        isFromDestination = getIntent().getBooleanExtra("isFromDestination", false);
        EventBus.getDefault().register(this);

        //初始化首页内容
        if (paramsData != null && paramsData.cityHomeType != null) {
            RequestCity requestCity = new RequestCity(this, paramsData.id, paramsData.cityHomeType.getType());
            HttpRequestUtils.request(this, requestCity, this);
        }

        //初始化埋点
        SensorsUtils.setPageEvent(getEventSource(), null, getIntentSource());

        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //展示线路数据
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                onScrollFloat(dy);
                //在这里进行第二次滚动（最后的距离）
                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int n = mIndex - linearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < recyclerView.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = recyclerView.getChildAt(n).getTop() - toolbar.getHeight() * 2;
                        //最后的移动
                        recyclerView.scrollBy(0, top);
                    } else if (n < 0) {
                        linearLayoutManager.scrollToPositionWithOffset(mIndex, toolbar.getHeight() * 2);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (destinationGoodsCount == 0) {
                        return;
                    }
                    int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount() - 2; //有隐藏model，加大判断力度
                    int visibleItemCount = layoutManager.getChildCount();
                    if (visibleItemCount > 0 && lastVisibleItem >= totalItemCount && adapter.getGoodModels().size() < destinationGoodsCount) {
                        // 滚动到底部加载更多
                        page++;
                        flushSkuList();
                    }
                }
            }
        });
        if (UserEntity.getUser().isLogin(this)) {
            try {
                if (Unicorn.isServiceAvailable()) {
                    Unicorn.addUnreadCountChangeListener(listener, true);
                }
            } catch (Exception e) {
                HLog.e("CityActivity:添加客服监听失败");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSensorsViewCityBeginEvent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setSensorsViewCityEndEvent();
    }

    private int scrolledDistance = 0; //滚动距离
    private boolean toolbarVisible = true;
    private boolean fitlerViewVisible = true;

    /**
     * 滚动效果修改
     *
     * @param dy
     */
    private void onScrollFloat(int dy) {
        if (adapter.cityHeaderModel.getView() == null) {
            //Bugly空指针判断
            return;
        }
        scrolledDistance += dy;
        int view1Height = adapter.cityHeaderModel.getView().getHeight();
        int toolbarHeight = toolbar.getHeight();
//        HLog.d("=======> scrolledDistance：" + scrolledDistance + "，View1：" + view1Height + "，toolbar：" + toolbarHeight);
        if (dy > 0) { //向上滑动
            if (scrolledDistance >= (view1Height - toolbarHeight) && filterContentView.getVisibility() == View.GONE && fitlerViewVisible) {
                fitlerViewVisible = false;
                filterContentView.setVisibility(View.VISIBLE);
                fitlerViewVisible = true;
            } else if (scrolledDistance >= view1Height && toolbar.getVisibility() == View.VISIBLE && toolbarVisible) {
                toolbarVisible = false;
                translate(false);
            }
        } else {
            if (scrolledDistance < (view1Height + toolbarHeight) && filterContentView.getVisibility() == View.VISIBLE && fitlerViewVisible) {
                fitlerViewVisible = false;
                filterContentView.setVisibility(View.GONE);
                fitlerViewVisible = true;
            } else if (toolbar.getVisibility() == View.GONE && toolbarVisible) {
                toolbarVisible = false;
                translate(true);
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
                toolbar.setVisibility(isShow ? View.VISIBLE : View.GONE);
                toolbarVisible = true;
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
            scrollTop(); // 滑动锚点
        }
    };

    CityFilterContentView.FilterConSelect filterConSelect2 = new CityFilterContentView.FilterConSelect() {

        @Override
        public void onSelect(FilterView filterView, LabelBean labelBean) {
            labelBeanCity = labelBean;
            page = 1; //筛选条件后重置页数为首页
            flushSkuList();
            scrollTop(); // 滑动锚点
        }
    };

    CityFilterContentView.FilterConSelect filterConSelect3 = new CityFilterContentView.FilterConSelect() {

        @Override
        public void onSelect(FilterView filterView, LabelBean labelBean) {
            labelBeanDay = labelBean;
            page = 1; //筛选条件后重置页数为首页
            flushSkuList();
            scrollTop(); // 滑动锚点
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
        item = menu.findItem(R.id.action_settings);
        chatMessageListener(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(CityActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.PARAMS_PAGE_INDEX, 2);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
            case SKU_PUTH_MESSAGE:
                int totalCount = (int) action.getData();
                if (totalCount != 0) {
                    chatMessageListener(true);
                }
            case CLICK_USER_LOGIN:
            case CLICK_USER_LOOUT:
            case LINE_UPDATE_COLLECT:
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCity) {
            //首页初始化数据
            data = ((RequestCity) request).getData();
            if (data != null) {
                destinationGoodsCount = data.destinationGoodsCount;
                //修改标题
                city_toolbar_title.setText(data.destinationName);
                //设置过滤条件筛选中的数据
                filterContentView.setData(CityActivity.this, data, filterConSelect1, filterConSelect2, filterConSelect3);
                // 设置玩法列表初始化数据
                if (data.destinationGoodsList != null && data.destinationGoodsList.size() > 0) {
                    flushSkuList(data.destinationGoodsList);
                } else {
                    page = 1; //无条件查询玩法
                    flushSkuList();
                }
            }
        } else if (request instanceof RequestQuerySkuList) {
            //条件筛选玩法
            PageQueryDestinationGoodsVo vo = (PageQueryDestinationGoodsVo) request.getData();
            if (vo != null) {
                destinationGoodsCount = vo.goodsCount;
                flushSkuList(vo.destinationGoodsList);
            }
        }
    }

    private void resetFilterView() {
        //设置标签部分
        if (data.destinationGoodsCount > 0) {
            //有玩法数据则显示筛选器
            adapter.showFilterModel(true);
            filterContentView.show(true);
        } else {
            //无玩法数据隐藏筛选器
            adapter.showFilterModel(false);
            filterContentView.show(false);
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
                    cityDataTools.getTagData(data.destinationTagGroupList), filterContentView.onSelectListener1,
                    paramsData);
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

    /**
     * 滑动到头部
     */
    private void scrollTop() {
        moveToPosition(3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbar.setVisibility(View.VISIBLE);
                filterContentView.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    private boolean move = false;
    private int mIndex = 3;

    private void moveToPosition(int index) {
        this.mIndex = index;
        //获取当前recycleView屏幕可见的第一项和最后一项的Position
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (index <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            recyclerView.scrollToPosition(index);
            move = true;
        } else if (index <= lastItem) {
            //当要置顶的项已经在屏幕上显示时，计算它离屏幕原点的距离
            int top = recyclerView.getChildAt(index - firstItem).getTop() - toolbar.getHeight() * 2;
            recyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerView.scrollToPosition(index);
            //记录当前需要在RecyclerView滚动监听里面继续第二次滚动
            move = true;
        }
    }

    @Override
    public String getEventSource() {
        String result = "目的地";
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

    private void setSensorsViewCityBeginEvent() {
        try {
            SensorsDataAPI.sharedInstance(this).trackTimerBegin("viewCity");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //神策统计_浏览城市/国家页
    private void setSensorsViewCityEndEvent() {
        if (paramsData == null) {
            return;
        }
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
            SensorsDataAPI.sharedInstance(this).trackTimerEnd("viewCity", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加点击玩法标签埋点
     *
     * @param tagLevel
     * @param tagName
     */
    public void setSensorsClickTag(int tagLevel, String tagName) {
        SensorsUtils.setSensorsClickTag(paramsData, String.valueOf(tagLevel), tagName);
    }

    /**
     * 添加聊天消息监听
     */
    private void chatMessageListener(boolean b) {
        item.setIcon(b ? R.drawable.city_menu_cion : R.drawable.city_menu_cion_two);

        if (SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT, 0) > 0 || SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.IM_CHAT_COUNT, 0) > 0) {
            item.setIcon(R.drawable.city_menu_cion);
        }
    }

    public UnreadCountChangeListener listener = new UnreadCountChangeListener() { // 声明一个成员变量
        @Override
        public void onUnreadCountChange(int count) {
            if (count > 0) {
                chatMessageListener(true);
            }
        }
    };
}
