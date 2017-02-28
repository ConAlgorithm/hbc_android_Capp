package com.hugboga.custom.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.PoiSearchAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.NewPoiBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestPoiSearch;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.ZListView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/8/4.
 */

public class PoiSearchActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnKeyListener, ZListView.OnRefreshListener{

    public static final String KEY_ARRIVAL = "arrival";


    @Bind(R.id.country_lvcountry)
    ZListView sortListView;

    @Bind(R.id.head_search)
    EditText editSearch;

    @Bind(R.id.arrival_empty_layout)
    View emptyView;
    @Bind(R.id.arrival_empty_layout_text)
    TextView emptyViewText;
    @Bind(R.id.arrival_tip)
    TextView arrivalTip;

    public static final String KEY_CITY_ID = "key_city_id";
    public static final String KEY_LOCATION = "location";

    public PoiSearchAdapter adapter;
    private long t = 0;
    private List<PoiBean> sourceDateList;
    private int cityId;
    private String location;
    private int PAGESIZE = 50;
    private String searchWord = "";
    private SharedPre sharedPre;
    private ArrayList<String> placeHistoryArray = new ArrayList<String>();
    private String source = "";

    private boolean isLoading = false;

    int mBusinessType = 1;

    protected void initHeader() {
        cityId = getIntent().getIntExtra(KEY_CITY_ID, -1);
        location = getIntent().getStringExtra(KEY_LOCATION);
        source = getIntent().getStringExtra("source");
        sharedPre = new SharedPre(activity);
        mBusinessType = getIntent().getIntExtra("mBusinessType", 1);
//        fgTitle.setText("搜索目的地");
    }


    public void onBackPressed() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("source", source);
        map.put("searchinput", editSearch.getText().toString().trim());
        MobclickAgent.onEvent(activity, "search_close", map);
        super.onBackPressed();
    }

    String type;
    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_arrival_search);
        type = this.getIntent().getStringExtra("key_from");
        ButterKnife.bind(this);
        initHeader();
        initData();
    }

    protected void initData() {
        initSearchTip(mBusinessType);
        editSearch.setOnKeyListener(this);
        editSearch.requestFocus();
        showSoftInputMethod(editSearch);
        adapter = new PoiSearchAdapter(activity);
        sortListView.setAdapter(adapter);
        sortListView.setOnItemClickListener(this);
//        sortListView.setonLoadListener(this);
        sortListView.setonRefreshListener(this);
        sortListView.getHeadView().setVisibility(View.GONE);
        sortListView.onLoadCompleteNone();
        sortListView.setVisibility(View.VISIBLE);
        initHistoryData();
    }

    /**
     * 初始化搜索框提示内容
     *
     * @param type
     */

    public void initSearchTip(Integer type) {
        switch (type) {
            case Constants.BUSINESS_TYPE_PICK:
                //接机
                editSearch.setHint(getResources().getString(R.string.search_hint_pick));
                break;
            case Constants.BUSINESS_TYPE_SEND:
                //送机
                editSearch.setHint(getResources().getString(R.string.search_hint_send));
                break;
            case Constants.BUSINESS_TYPE_DAILY:
                //日租
                break;
            case Constants.BUSINESS_TYPE_RENT:
                //次租
                String from = getIntent().getStringExtra(KEY_FROM);
                if (from.equals("from")) {
                    editSearch.setHint(getResources().getString(R.string.search_hint_send));
                } else if (from.equals("to")) {
                    editSearch.setHint(getResources().getString(R.string.search_hint_pick));
                }
                break;
            default:
                break;
        }
    }


    /**
     * 初始化搜索历史记录
     */
    private void initHistoryData() {
//        @color/item_title_bg
        String placeHistoryStr = sharedPre.getStringValue(mBusinessType + SharedPre.RESOURCES_PLACE_HISTORY);
        ArrayList<PoiBean> historyList = new ArrayList<PoiBean>();
        PoiBean bean;
        if (placeHistoryStr != null) {
            placeHistoryArray.clear();
            for (String place : placeHistoryStr.split(",")) {
                bean = new PoiBean();
                bean.placeName = place;
                bean.isHistory = true;
                historyList.add(bean);
                placeHistoryArray.add(place);
            }
            adapter.setList(historyList);
            if (historyList.size() > 0)
                arrivalTip.setTextColor(0xff242424);
            arrivalTip.setText("搜索历史");
            arrivalTip.setVisibility(View.VISIBLE);
            return;
        }
    }

    int listCount = 0;
    private void requestKeyword(int offset) {
        if (sortListView.state == ZListView.LOADING_MORE && adapter.getCount() >= listCount) {
            return;
        }
        if (isLoading) {
            return;
        } else {
            isLoading = true;
        }
        Map map = new HashMap();
        map.put(Constants.PARAMS_SOURCE,getIntentSource());
        map.put("searchinput",searchWord);
        MobClickUtils.onEvent(StatisticConstant.SEARCH,map);

        RequestPoiSearch requestPoiSearch = new RequestPoiSearch(activity, cityId, location, searchWord, offset, PAGESIZE);
        requestData(requestPoiSearch);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestPoiSearch) {
            RequestPoiSearch requestPoiSearch = (RequestPoiSearch) request;
            NewPoiBean newPoiBean = (requestPoiSearch.getData());//listDate;
            listCount = newPoiBean.count;
            ArrayList<PoiBean> dateList = newPoiBean.listDate;
            sortListView.setEmptyView(emptyView);
            if (TextUtils.isEmpty(editSearch.getText())) {
                dateList = null;
            }
            sortListView.getMoreView().setVisibility(View.VISIBLE);
            if (dateList != null && dateList.size() < PAGESIZE) {
                sortListView.onLoadCompleteNone();
            } else {
                sortListView.onLoadComplete();
            }
            if (sortListView.state == ZListView.LOADING_MORE) {
                if (dateList != null) {
                    adapter.addList(dateList);
                }
            } else {
//                adapter = new PoiSearchAdapter(activity);
                adapter.setList(dateList);
//                sortListView.setAdapter(adapter);
                sortListView.onRefreshComplete();
                sortListView.setSelection(0);

                arrivalTip.setTextColor(activity.getResources().getColor(R.color.basic_rent_toolbar_color));
                arrivalTip.setText(R.string.arrival_tip_hotel);
                arrivalTip.setVisibility(View.GONE);
            }
//            emptyViewText.setText(getString(R.string.arrival_empty_text,searchWord));
            isLoading = false;
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        isLoading = false;
    }

    @OnClick({R.id.header_left_btn, R.id.head_search_clean, R.id.head_text_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                hideInputMethod(editSearch);
                finish();
                break;
            case R.id.head_search_clean:
                editSearch.setText("");
                break;
            case R.id.head_text_right:
                search(); //进行点击搜索
                break;
            default:
//                super.onClick(view);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PoiBean bean = (PoiBean) adapter.getItem(position - 1);
        if (bean != null) {
            if (bean.isHistory) {
                editSearch.setText(bean.placeName);
                searchWord = editSearch.getText().toString().trim();
                search();
            } else {

                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_ARRIVAL, bean);

//                finishForResult(bundle);
                hideSoftInput();
                finish();
                if(type != null){
                    if(null != bean){
                        bean.type = type;
                    }
                }
                EventBus.getDefault().post(new EventAction(EventType.CHOOSE_POI_BACK, bean));
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER)) {
            search();
            return true;
        }
        return false;
    }

    /**
     * 根据输入字符进行搜索
     */
    private void search() {
        searchWord = editSearch.getText().toString();
        if (!TextUtils.isEmpty(searchWord) && !TextUtils.isEmpty(searchWord.trim())) {
            searchWord = searchWord.trim();
            saveHistoryDate(searchWord);
            onRefresh();
        } else {
            arrivalTip.setVisibility(View.GONE);
            sourceDateList = null;
            editSearch.setText("");
            adapter.setList(sourceDateList);
            initHistoryData();
        }

    }

    /**
     * 存储历史
     *
     * @param keyword
     */
    private void saveHistoryDate(String keyword) {
        synchronized (this) {
            placeHistoryArray.remove(keyword);//排重
            placeHistoryArray.add(0, keyword);
            for (int i = placeHistoryArray.size() - 1; i > 2; i--) {
                placeHistoryArray.remove(i);
            }
            sharedPre.saveStringValue(mBusinessType + SharedPre.RESOURCES_PLACE_HISTORY, TextUtils.join(",", placeHistoryArray));
        }
    }

    @Override
    public void onRefresh() {
        sortListView.state = ZListView.RELEASE_To_REFRESH;
        if(!TextUtils.isEmpty(searchWord)) {
            requestKeyword(0);
        }else{
            sortListView.onRefreshComplete();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("source", source);
        MobclickAgent.onEvent(activity, "search_launch", map);
    }
}
