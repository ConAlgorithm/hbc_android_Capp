package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FlightAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestFlightByCity;
import com.hugboga.custom.data.request.RequestFlightByNo;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created on 16/8/3.
 */

public class PickFlightListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final String KEY_FLIGHT_NO = "flight_no";
    public static final String KEY_FLIGHT_DATE = "flight_date";
    public static final String KEY_FLIGHT_FROM = "flight_from";
    public static final String KEY_FLIGHT_TO = "flight_to";
    public static final String KEY_FLIGHT_TYPE = "flight_type";

    @Bind(R.id.flight_info)
    TextView flightInfo;
    @Bind(R.id.fromto)
    TextView fromto;
    @Bind(R.id.number_flite)
    TextView numberflite;
    @Bind(R.id.loading_layout)
    RelativeLayout loading;
    @Bind(R.id.flight_empty_layout)
    View emptyView;
    @Bind(R.id.empty_wifi_layout)
    View emptyWifi;
    @Bind(R.id.view)
    View view;
    /*@Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;*/
    @Bind(R.id.flight_list)
    ListView flightList;
    private LinearLayout footerLayout;
    private FlightAdapter mAdapter;
    private ArrayList<FlightBean> mListDate;
    private int flightType = 1;//1:按航班查询，2按城市查询
    private String flightNo;
    private String flightDate;
    private int flightFromCityId;
    private int flightToCityId;
    private String flightFromCity;
    private String flightToCity;
    private DbManager mDbManager;
    private String source = "";

    int mBusinessType = Constants.BUSINESS_TYPE_PICK;

    @Override
    public void onStart() {
        super.onStart();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("source", source);
        MobclickAgent.onEvent(activity, "search_launch", map);
    }

    public void initHeader() {
        //headerTitle.setText(getString(R.string.title_pick_flight_list));
        source = getIntent().getStringExtra("source");
        /*headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    public void initView() {
        mDbManager = new DBHelper(activity).getDbManager();
        ListView listView = (ListView) findViewById(R.id.flight_list);
        mAdapter = new FlightAdapter(activity);
        listView.setAdapter(mAdapter);
        listView.setEmptyView(emptyView);

        footerLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.flight_show_all, null);
        footerLayout.setVisibility(View.GONE);
        listView.addFooterView(footerLayout,null,false);

        listView.setOnItemClickListener(this);
        emptyView.setVisibility(View.GONE);
        flightNo = getIntent().getStringExtra(KEY_FLIGHT_NO);
        flightDate = getIntent().getStringExtra(KEY_FLIGHT_DATE);
        flightFromCityId = getIntent().getIntExtra(KEY_FLIGHT_FROM, -1);
        flightToCityId = getIntent().getIntExtra(KEY_FLIGHT_TO, -1);
        flightType = getIntent().getIntExtra(KEY_FLIGHT_TYPE, -1);

        if(flightType == 2){
            flightFromCity = DBHelper.findCityById(String.valueOf(flightFromCityId)).name;
            flightToCity = DBHelper.findCityById(String.valueOf(flightToCityId)).name;
            fromto.setText(flightFromCity+"-"+flightToCity);
        }else{
            fromto.setText(flightNo);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        emptyWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyWifi.setVisibility(View.GONE);
                requestData();
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_pick_flight_list;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mBusinessType = this.getIntent().getIntExtra("mBusinessType", -1);
        initView();
        initHeader();
        requestData();
    }

    protected Callback.Cancelable requestData() {
        try {
            String tFlightDate = DateUtils.getWeekStrByDate(flightDate, DateUtils.dateDateFormat, DateUtils.dateWeekFormat2Only);
            flightInfo.setVisibility(View.VISIBLE);
            flightInfo.setText(tFlightDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BaseRequest request;
        if (flightType == 1) {
            request = new RequestFlightByNo(activity, flightNo, flightDate, mBusinessType);
        } else {
            request = new RequestFlightByCity(activity, flightFromCityId, flightToCityId, flightDate);
        }
        loading.setVisibility(View.VISIBLE);
        requestData(request,false);
        return null;
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        loading.setVisibility(View.GONE);
        if (request instanceof RequestFlightByNo) {
            RequestFlightByNo mParser = (RequestFlightByNo) request;
            mListDate = mParser.getData();
        } else if (request instanceof RequestFlightByCity) {
            RequestFlightByCity mParser = (RequestFlightByCity) request;
            mListDate = mParser.getData();
        }
        int count = 0;
        if (mListDate != null && !mListDate.isEmpty()) {
            addAirportInfo(mListDate);
            count = mListDate.size();
        }
        if(count == 0){
            numberflite.setVisibility(View.GONE);
        }else{
            numberflite.setVisibility(View.VISIBLE);
            numberflite.setText("共"+count+"趟"+" "+"请确认您的航班");
            if(footerLayout!=null){
                footerLayout.setVisibility(View.VISIBLE);
            }

        }
        /*try {
            String tFlightDate = DateUtils.getWeekStrByDate(flightDate, DateUtils.dateDateFormat, DateUtils.dateWeekFormat2);
            if (count != 0) {
                flightInfo.setText(tFlightDate + " (共" + count + "趟航班)");
            } else {
                flightInfo.setVisibility(View.INVISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        inflateContent();
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        loading.setVisibility(View.GONE);
        numberflite.setVisibility(View.GONE);
        footerLayout.setVisibility(View.GONE);
        if(!NetworkUtil.isNetAvailable(this)){
            emptyWifi.setVisibility(View.VISIBLE);
            return;
        }
    }

    protected void inflateContent() {
        mAdapter.setList(mListDate);
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FlightBean bean = mListDate.get(position);
        if (!bean.serviceStatus) {
            CommonUtils.showToast("机场信息未查到");
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("source", source);
        map.put("searchinput", flightNo);
        map.put("searchcity", bean.flightNo);
        MobclickAgent.onEvent(activity, "search", map);

        Bundle bundle = new Bundle();
        bundle.putSerializable("key_airport", bean);
        bundle.putString(KEY_FROM, "FlightList");

        EventBus.getDefault().post(new EventAction(EventType.PICK_FLIGHT_BACK, bean));
        finish();
//        finishForResult(bundle);
    }

    @OnClick({R.id.flight_empty_service_tv})
    public void onService() {
        DialogUtil.showServiceDialog(PickFlightListActivity.this, null, UnicornServiceActivity.SourceType.TYPE_DEFAULT, null, null, getEventSource());
    }

    private void addAirportInfo(ArrayList<FlightBean> listDate) {
        Set<String> sets = new TreeSet<String>();
        for (FlightBean bean : listDate) {
            sets.add(bean.depAirportCode);
            sets.add(bean.arrivalAirportCode);
        }

        Selector selector = null;
        try {
            selector = mDbManager.selector(AirPort.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        selector.where("airport_code", "IN", sets);
        try {
            List<AirPort> list = selector.findAll();
            if (null != list) {
                for (int i = listDate.size() - 1; i >= 0; i--) {
                    FlightBean flightBean = listDate.get(i);
                    for (AirPort airPort : list) {
                        if (airPort.airportCode.equals(flightBean.depAirportCode))
                            flightBean.depAirport = airPort;
                        else if (airPort.airportCode.equals(flightBean.arrivalAirportCode))
                            flightBean.arrivalAirport = airPort;
                    }
                    if (flightBean.depAirport == null) {
                        flightBean.depAirport = new AirPort();
                        flightBean.depAirport.airportName = flightBean.depAirportName;
                        flightBean.depAirport.cityName = flightBean.depCityName;
                        if (mBusinessType == Constants.BUSINESS_TYPE_SEND) {
                            flightBean.serviceStatus = false;
                            listDate.remove(i);
                        }
                    }
                    if (flightBean.arrivalAirport == null) {
                        flightBean.arrivalAirport = new AirPort();
                        flightBean.arrivalAirport.airportName = flightBean.arrAirportName;
                        flightBean.arrivalAirport.cityName = flightBean.arrCityName;
                        if (mBusinessType == Constants.BUSINESS_TYPE_PICK) {
                            flightBean.serviceStatus = false;
                            listDate.remove(i);
                        }

                    }
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        }

    }

}