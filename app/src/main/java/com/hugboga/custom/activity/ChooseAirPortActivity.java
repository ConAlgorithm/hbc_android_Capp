package com.hugboga.custom.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
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
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.GPSBean;
import com.hugboga.custom.data.bean.LocationData;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestAirPort;
import com.hugboga.custom.data.request.RequestUploadLocationV11;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.SideBar;

import org.greenrobot.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created on 16/8/5.
 */

public class ChooseAirPortActivity extends BaseActivity implements SideBar.OnTouchingLetterChangedListener, AdapterView.OnItemClickListener, View.OnKeyListener, TextWatcher {

    public static final String KEY_BUNDLE = "bundle";
    public static final String KEY_AIRPORT = "key_airport";

    public AirportAdapter adapter;
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
    @Bind(R.id.city_choose_head_text)
    TextView cityChooseHeadText;
    @Bind(R.id.choose_city_head_layout)
    LinearLayout chooseCityHeadLayout;
    @Bind(R.id.arrival_empty_layout)
    LinearLayout arrivalEmptyLayout;
    @Bind(R.id.country_lvcountry)
    ListView countryLvcountry;
    @Bind(R.id.dialog)
    TextView dialog;
    @Bind(R.id.sidrbar)
    SideBar sidrbar;
    @Bind(R.id.city_choose_btn)
    Button cityChooseBtn;
    private ListView sortListView;
    private long t = 0;
    private List<AirPort> sourceDateList;
    private SideBar sideBar;
    private View emptyView;
    @Bind(R.id.empty_layout_text)
    TextView emptyViewText;
    private TextView editSearch;
    private DbManager mDbManager;
    private SharedPre sharedPer;
    private ArrayList<String> airportHistory;


    protected void initHeader() {
        mDbManager = new DBHelper(activity).getDbManager();
        sharedPer = new SharedPre(activity);
//		fgTitle.setText(getString(R.string.title_choose_airport));
        headTextRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editSearch.getText().toString().trim();
                hideInputMethod(editSearch);
                if(!TextUtils.isEmpty(keyword)) {
                    requestDate(keyword); //进行点击搜索
                }else{
                    CommonUtils.showToast("请输入城市或机场");
                }
            }
        });
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod(editSearch);
                finish();
            }
        });
    }

    protected void initView() {
        //实例化汉字转拼音类
        emptyView = findViewById(R.id.arrival_empty_layout);
        emptyViewText.setText(getString(R.string.empty_text));
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        TextView dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(this);
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(this);
        editSearch = (TextView) findViewById(R.id.head_search);
        editSearch.setHint("输入城市或机场");
        editSearch.addTextChangedListener(this);
        editSearch.setOnKeyListener(this);
        editSearch.requestFocus();

        adapter = new AirportAdapter(activity, sourceDateList);
        sortListView.setAdapter(adapter);
        sideBar.setVisibility(View.VISIBLE);
        initSideBar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideInputMethod(headSearch);
    }

    private void getGPSAirport() {
        if (null != GPSBean.lat && null != GPSBean.lng) {
            RequestUploadLocationV11 requestUploadLocationV11 = new RequestUploadLocationV11(activity);
            HttpRequestUtils.request(activity, requestUploadLocationV11, this, false);
        }
    }

    protected Callback.Cancelable requestData() {
        requestDate(null);
        requestHotDate();
        requestHistoryDate();
        getGPSAirport();
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
                if (letter.equals("热门机场")) {
                    letter = "热门";
                } else if(letter.equals("搜索历史")) {
                    letter = "历史";
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
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_city);
        ButterKnife.bind(this);
        initView();
        initHeader();
        inflateContent();
        requestData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
        selector.orderBy("hot_weight");
        selector.limit(3);
        try {
            List<AirPort> hotAirportDate = selector.findAll();
            for (AirPort bean : hotAirportDate) {
                bean.cityFirstLetter = "热门机场";
            }
            if (hotAirportDate.size() > 0l && hotAirportDate.get(0) != null) {
                hotAirportDate.get(0).isFirst = true;
            }
            sourceDateList.addAll(0, hotAirportDate);
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
                    bean.cityFirstLetter = "搜索历史";
                }
            }
            if (historyAirportDate.size() > 0 && historyAirportDate.get(0) != null) {
                historyAirportDate.get(0).isFirst = true;
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
//		emptyViewText.setText(getString(R.string.arrival_empty_text, editSearch.getText().toString().trim()));
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
        finish();
        EventBus.getDefault().post(new EventAction(EventType.AIR_PORT_BACK, sourceDateList.get(position)));

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
            requestDate(editSearch.getText().toString().trim());
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
            requestData();
        } else {
//            requestDate(editSearch.getText().toString().trim());
        }
    }
}
