package com.hugboga.custom.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.AirportAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.GPSBean;
import com.hugboga.custom.data.bean.LocationData;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestAirPort;
import com.hugboga.custom.data.request.RequestUploadLocationV11;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.SideBar;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 16/8/5.
 */

public class ChooseAirPortActivity extends BaseActivity implements SideBar.OnTouchingLetterChangedListener, AdapterView.OnItemClickListener, View.OnKeyListener, TextWatcher, TextView.OnEditorActionListener {

    public static final String KEY_BUNDLE = "bundle";
    public static final String KEY_AIRPORT = "key_airport";

    public static final String KEY_GROUPID = "key_groupid";//线路圈id
    public static final String KEY_CITY_ID = "key_city_id";//城市id


    public AirportAdapter adapter;

    @BindView(R.id.activity_head_layout)
    RelativeLayout headLayout;
    @BindView(R.id.fg_city_bottom_line_view)
    View bottomLineView;
    @BindView(R.id.fg_city_header)
    View cityHeaderLayout;

    @BindView(R.id.head_left_layout)
    RelativeLayout headerLeftLayout;
    @BindView(R.id.head_search)
    EditText headSearch;
    @BindView(R.id.head_search_clean)
    ImageView headSearchClean;
    @BindView(R.id.choose_city_head_layout)
    LinearLayout chooseCityHeadLayout;
    @BindView(R.id.arrival_empty_layout)
    LinearLayout arrivalEmptyLayout;
    @BindView(R.id.country_lvcountry)
    ListView countryLvcountry;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.sidrbar)
    SideBar sidrbar;
    @BindView(R.id.city_choose_btn)
    Button cityChooseBtn;
    private ListView sortListView;
    private long t = 0;
    private List<AirPort> sourceDateList;
    private SideBar sideBar;
    private View emptyView;
    @BindView(R.id.empty_layout_text)
    TextView emptyViewText;
    private DbManager mDbManager;
    private SharedPre sharedPer;
    private ArrayList<String> airportHistory;
    private GuidanceOrderActivity.Params guidanceParams;

    private int groupId;
    private int cityId;
    CsDialog csDialog;

    protected void initHeader() {
        mDbManager = new DBHelper(activity).getDbManager();
        sharedPer = new SharedPre(activity);
        headerLeftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod(headSearch);
                finish();
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (TextUtils.isEmpty(headSearch.getText())
                    || TextUtils.isEmpty(headSearch.getText().toString())
                    || TextUtils.isEmpty(headSearch.getText().toString().trim())) {
                CommonUtils.showToast(R.string.choose_city_check_input);
            } else {
                requestDate(headSearch.getText().toString().trim());
            }
            hideSoftInput();
            return true;
        }
        return false;
    }

    protected void initView() {
        //实例化汉字转拼音类
        emptyView = findViewById(R.id.arrival_empty_layout);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        TextView dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(this);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(this);
        headSearch.setHint(R.string.airport_input_city);
        headSearch.addTextChangedListener(this);
        headSearch.setOnKeyListener(this);
        headSearch.setOnEditorActionListener(this);

        adapter = new AirportAdapter(activity, sourceDateList);
        sortListView.setAdapter(adapter);
        sideBar.setVisibility(View.VISIBLE);
        initSideBar();

        if (cityId != 0) {
            headLayout.setVisibility(View.GONE);
            bottomLineView.setVisibility(View.GONE);
            cityHeaderLayout.setVisibility(View.VISIBLE);
            ((TextView) cityHeaderLayout.findViewById(R.id.header_title)).setText(R.string.airport_choose);
            cityHeaderLayout.findViewById(R.id.header_left_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            cityHeaderLayout.setVisibility(View.GONE);
        }
        if (getIntent() != null) {
            guidanceParams = (GuidanceOrderActivity.Params) getIntent().getSerializableExtra(GuidanceOrderActivity.PARAMS_GUIDANCE);
        }
        setSensorsBuyAirportEvent();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideSoftInput();
    }

    @OnClick({R.id.arrival_empty_service_tv})
    public void onService() {
        //DialogUtil.showServiceDialog(ChooseAirPortActivity.this, null, UnicornServiceActivity.SourceType.TYPE_DEFAULT, null, null, getEventSource());
        csDialog = CommonUtils.csDialog(ChooseAirPortActivity.this, null, null, null, UnicornServiceActivity.SourceType.TYPE_DEFAULT, getEventSource(), new CsDialog.OnCsListener() {
            @Override
            public void onCs() {
                if (csDialog != null && csDialog.isShowing()) {
                    csDialog.dismiss();
                }
            }
        });

    }

    private void getGPSAirport() {
        if (null != GPSBean.lat && null != GPSBean.lng) {
            RequestUploadLocationV11 requestUploadLocationV11 = new RequestUploadLocationV11(activity);
            HttpRequestUtils.request(activity, requestUploadLocationV11, this, false);
        }
    }

    protected Callback.Cancelable requestData() {
        if (groupId != 0) {
            queryAirPortByGroupId(null);
        } else if (cityId != 0) {
            queryAirPortByCityId(null);
        } else {
            requestDate(null);
            requestHotDate();
            requestHistoryDate();
            getGPSAirport();
        }

        return null;
    }

    private void initSideBar() {
        if (sourceDateList == null || sourceDateList.size() <= 0) {
            sideBar.setVisibility(View.GONE);
            return;
        }
        sideBar.setVisibility(View.VISIBLE);
        ArrayList<String> sectionIndices = new ArrayList<String>();
        String lastFirstLetter = null;
        for (int i = 0; i < sourceDateList.size(); i++) {
            AirPort airPort = sourceDateList.get(i);
            if (airPort == null) {
                continue;
            }
            if (!TextUtils.equals(lastFirstLetter, airPort.cityFirstLetter)) {
                String letter = airPort.cityFirstLetter;
                if (letter.equals(CommonUtils.getString(R.string.poisearch_hot))) {
                    letter = CommonUtils.getString(R.string.poisearch_hot2);
                } else if (letter.equals(CommonUtils.getString(R.string.poisearch_history))) {
                    letter = CommonUtils.getString(R.string.poisearch_history2);
                }
                sectionIndices.add(letter);
                lastFirstLetter = airPort.cityFirstLetter;
            }
        }
        String[] sections = new String[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        sideBar.setHeightWrapContent(true);
        sideBar.setLetter(sections);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_city;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        groupId = getIntent().getIntExtra(KEY_GROUPID, 0);
        cityId = getIntent().getIntExtra(KEY_CITY_ID, 0);
        initView();
        initHeader();
        inflateContent();
        requestData();
    }


    private void setFirstWord(List<AirPort> sourceDateList) {
        String key = "";
        if (sourceDateList == null) return;
        for (int i = 0; i < sourceDateList.size(); i++) {
            AirPort model = sourceDateList.get(i);
            if (key.equals(model.cityFirstLetter)) {
                model.isFirst = false;
            } else {
                model.isFirst = true;
                key = model.cityFirstLetter;
            }
        }
    }

    protected void requestDate(String keyword) {
        if (groupId != 0) {
            queryAirPortByGroupId(keyword);
        } else if (cityId != 0) {
            queryAirPortByCityId(null);
        } else {
            Selector selector = null;
            try {
                selector = mDbManager.selector(AirPort.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(keyword))
                selector.where("airport_name", "LIKE", "%" + keyword + "%").or("city_name", "LIKE", "%" + keyword + "%");
            try {
                sourceDateList = selector.findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            selector.orderBy("city_initial");
        }
        inflateContent();
        initSideBar();
    }

    /**
     * 热门机场
     */
    private void requestHotDate() {
        Selector selector = null;
        try {
            selector = mDbManager.selector(AirPort.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("is_hot", "=", 1);
        selector.orderBy("hot_weight", true);
        selector.limit(3);
        try {
            List<AirPort> hotAirportDate = selector.findAll();
            if (hotAirportDate != null && hotAirportDate.size() > 0) {
                for (AirPort bean : hotAirportDate) {
                    bean.cityFirstLetter = CommonUtils.getString(R.string.poisearch_hot);
                }
                if (hotAirportDate.size() > 0l && hotAirportDate.get(0) != null) {
                    hotAirportDate.get(0).isFirst = true;
                }
                if (sourceDateList == null) {
                    sourceDateList = new ArrayList<>();
                }
                sourceDateList.addAll(0, hotAirportDate);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        initSideBar();
    }

    int mBusinessType = 0;

    /**
     * 搜索历史记录
     */
    private void requestHistoryDate() {
        String airportHistoryStr = sharedPer.getStringValue(mBusinessType + SharedPre.RESOURCES_AIRPORT_HISTORY);
        airportHistory = new ArrayList<String>();
        if (!TextUtils.isEmpty(airportHistoryStr)) {
            for (String airport : airportHistoryStr.split(",")) {
                airportHistory.add(airport);
            }
        }
        Selector selector = null;
        try {
            selector = mDbManager.selector(AirPort.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        WhereBuilder where = WhereBuilder.b();
        where.and("airport_id", "IN", airportHistory);
        selector.where(where);
        try {
            List<AirPort> tmpHistoryAirportDate = selector.findAll();
            ArrayList<AirPort> historyAirportDate = new ArrayList<AirPort>();
            for (String historyId : airportHistory) {
                for (AirPort bean : tmpHistoryAirportDate) {
                    if (historyId.equals(String.valueOf(bean.airportId))) {
                        historyAirportDate.add(bean);
                    }
                    bean.cityFirstLetter = CommonUtils.getString(R.string.poisearch_history);
                }
            }
            if (historyAirportDate.size() > 0 && historyAirportDate.get(0) != null) {
                historyAirportDate.get(0).isFirst = true;
            }
            if (sourceDateList == null) {
                sourceDateList = new ArrayList<>();
            }
            sourceDateList.addAll(0, historyAirportDate);
        } catch (DbException e) {
            e.printStackTrace();
        }
        initSideBar();
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestAirPort) {
            RequestAirPort mParser = (RequestAirPort) request;
            sourceDateList = mParser.getData();
            inflateContent();
        } else if (request instanceof RequestUploadLocationV11) {
            RequestUploadLocationV11 mParser = (RequestUploadLocationV11) request;
            LocationData data = mParser.getData();
            AirPort airPort;
            if (data.airports.size() > 0 && !data.city.countryId.equalsIgnoreCase(68 + "")) {
                List<AirPort> gpsDateList = new ArrayList<>();
                for (int i = 0; i < data.airports.size(); i++) {
                    airPort = new AirPort();
                    if (i == 0) {
                        airPort.isFirst = true;
                    } else {
                        airPort.isFirst = false;
                    }
                    airPort.cityFirstLetter = getString(R.string.location_airport);
                    airPort.cityName = data.city.cityName;
                    airPort.airportName = data.airports.get(i).airportName;
                    airPort.airportCode = data.airports.get(i).airportCode;
                    airPort.location = data.airports.get(i).airportLocation;
                    airPort.airportId = 0;
                    airPort.cityId = Integer.valueOf(data.city.cityId);
                    gpsDateList.add(airPort);
                }
                adapter.addList(gpsDateList);
            }
        }
    }

    protected void inflateContent() {
        // 设置key
        setFirstWord(sourceDateList);
        if (sourceDateList == null || sourceDateList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
            setFirstWord(sourceDateList);
        }
        adapter.updateListView(sourceDateList);
    }


    @Override
    public void onTouchingLetterChanged(String s) {
        //该字母首次出现的位置
        int position = adapter.getPositionForSection(s);
        if (position != -1) {
            sortListView.setSelection(position);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
//        AirPort airPort = sourceDateList.get(position);
        AirPort airPort = (AirPort) adapter.getItem(position);
        if (TextUtils.isEmpty(airPort.airportName)) {
            return;
        }
        saveHistoryDate(airPort);
        hideInputMethod(headSearch);
        if (guidanceParams != null) {
            Intent intent = new Intent(this, PickSendActivity.class);
            PickSendActivity.Params params = new PickSendActivity.Params();
            params.airPortBean = airPort;
            params.type = 1;
            intent.putExtra(Constants.PARAMS_DATA, params);
            intent.putExtra(Constants.PARAMS_SOURCE, guidanceParams.source);
            startActivity(intent);
        } else {
            finish();
            EventBus.getDefault().post(new EventAction(EventType.AIR_PORT_BACK, sourceDateList.get(position)));
        }
    }

    /**
     * 存储历史
     *
     * @param airPort 机场
     */
    private void saveHistoryDate(AirPort airPort) {
        if (airportHistory == null) airportHistory = new ArrayList<String>();
        airportHistory.remove(String.valueOf(airPort.airportId));//排重
        airportHistory.add(0, String.valueOf(airPort.airportId));
        for (int i = airportHistory.size() - 1; i > 2; i--) {
            airportHistory.remove(i);
        }
        sharedPer.saveStringValue(mBusinessType + SharedPre.RESOURCES_AIRPORT_HISTORY, TextUtils.join(",", airportHistory));
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        MLog.e("" + event.getAction() + " " + keyCode);
        if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER)) {
            if (groupId != 0) {
                queryAirPortByGroupId(headSearch.getText().toString().trim());
            } else if (cityId != 0) {
                queryAirPortByCityId(headSearch.getText().toString().trim());
            } else {
                requestDate(headSearch.getText().toString().trim());

            }

            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            if (groupId != 0) {
                queryAirPortByGroupId("");
            } else if (cityId != 0) {
                queryAirPortByCityId("");
            } else {
                requestData();

            }
        } else {
//            requestDate(editSearch.getText().toString().trim());
        }
    }

    private void queryAirPortByGroupId(String keywords) {
        List<AirPort> airPorts = new ArrayList<>();
        Selector<CityBean> selector;
        Cursor cursor = null;
        try {
            selector = mDbManager.selector(CityBean.class);
            selector.where("group_id", "=", groupId);
            List<CityBean> cityBeanList = selector.findAll();
            if (cityBeanList != null && cityBeanList.size() > 0) {
                for (CityBean cityBean : cityBeanList) {
                    String sql = "select * from airport where city_id=" + cityBean.cityId;
                    if (!TextUtils.isEmpty(keywords)) {
                        sql += " and (airport_name like '%" + keywords + "%'" + " or "
                                + "city_name like '%" + keywords + "%')";
                    }
                    Log.i("sql", sql);
                    cursor = mDbManager.execQuery(sql);
                    while (cursor != null && cursor.moveToNext()) {
                        AirPort airPort = new AirPort();
                        airPort.airportId = cursor.getInt(cursor.getColumnIndexOrThrow("airport_id"));
                        airPort.airportCode = cursor.getString(cursor.getColumnIndexOrThrow("airport_code"));
                        airPort.airportName = cursor.getString(cursor.getColumnIndexOrThrow("airport_name"));
                        airPort.areaCode = cursor.getString(cursor.getColumnIndexOrThrow("area_code"));
                        airPort.cityFirstLetter = cursor.getString(cursor.getColumnIndexOrThrow("city_initial"));
                        int bannerswitch = cursor.getInt(cursor.getColumnIndexOrThrow("banner_switch"));
                        airPort.bannerSwitch = bannerswitch == 0 ? false : true;
                        airPort.cityId = cursor.getInt(cursor.getColumnIndexOrThrow("city_id"));
                        airPort.cityName = cursor.getString(cursor.getColumnIndexOrThrow("city_name"));
                        airPort.location = cursor.getString(cursor.getColumnIndexOrThrow("airport_location"));
                        airPort.hotWeight = cursor.getInt(cursor.getColumnIndexOrThrow("hot_weight"));
                        int hot = cursor.getInt(cursor.getColumnIndexOrThrow("is_hot"));
                        airPort.isHot = hot == 0 ? false : true;
                        int visaSwitch = cursor.getInt(cursor.getColumnIndexOrThrow("landing_visa_switch"));
                        airPort.visaSwitch = visaSwitch == 0 ? false : true;
                        int childSwitch = cursor.getInt(cursor.getColumnIndexOrThrow("childseat_switch"));
                        airPort.childSeatSwitch = childSwitch == 0 ? false : true;
                        airPorts.add(airPort);
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        sourceDateList = airPorts;
        inflateContent();
        initSideBar();
    }

    private void queryAirPortByCityId(String keywords) {
        List<AirPort> airPorts = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = "select * from airport where city_id=" + cityId;
            if (!TextUtils.isEmpty(keywords)) {
                sql += " and (airport_name like '%" + keywords + "%'" + " or "
                        + "city_name like '%" + keywords + "%')";
            }
            Log.i("sql", sql);
            cursor = mDbManager.execQuery(sql);
            while (cursor != null && cursor.moveToNext()) {
                AirPort airPort = new AirPort();
                airPort.airportId = cursor.getInt(cursor.getColumnIndexOrThrow("airport_id"));
                airPort.airportCode = cursor.getString(cursor.getColumnIndexOrThrow("airport_code"));
                airPort.airportName = cursor.getString(cursor.getColumnIndexOrThrow("airport_name"));
                airPort.areaCode = cursor.getString(cursor.getColumnIndexOrThrow("area_code"));
                airPort.cityFirstLetter = cursor.getString(cursor.getColumnIndexOrThrow("city_initial"));
                int bannerswitch = cursor.getInt(cursor.getColumnIndexOrThrow("banner_switch"));
                airPort.bannerSwitch = bannerswitch == 0 ? false : true;
                airPort.cityId = cursor.getInt(cursor.getColumnIndexOrThrow("city_id"));
                airPort.cityName = cursor.getString(cursor.getColumnIndexOrThrow("city_name"));
                airPort.location = cursor.getString(cursor.getColumnIndexOrThrow("airport_location"));
                airPort.hotWeight = cursor.getInt(cursor.getColumnIndexOrThrow("hot_weight"));
                int hot = cursor.getInt(cursor.getColumnIndexOrThrow("is_hot"));
                airPort.isHot = hot == 0 ? false : true;
                int visaSwitch = cursor.getInt(cursor.getColumnIndexOrThrow("landing_visa_switch"));
                airPort.visaSwitch = visaSwitch == 0 ? false : true;
                int childSwitch = cursor.getInt(cursor.getColumnIndexOrThrow("childseat_switch"));
                airPort.childSeatSwitch = childSwitch == 0 ? false : true;
                airPorts.add(airPort);
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        sourceDateList = airPorts;
        inflateContent();
        initSideBar();
    }

    //来到选机场页
    private void setSensorsBuyAirportEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("refer", getIntentSource());
            SensorsDataAPI.sharedInstance(this).track("buy_airport");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
