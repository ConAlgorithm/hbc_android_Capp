package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
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
import butterknife.ButterKnife;

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
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.flight_list)
    ListView flightList;
    @Bind(R.id.flight_empty_no)
    TextView flightEmptyNo;
    @Bind(R.id.flight_empty_btn)
    Button flightEmptyBtn;

    private FlightAdapter mAdapter;
    private ArrayList<FlightBean> mListDate;
    private int flightType = 1;//1:按航班查询，2按城市查询
    private String flightNo;
    private String flightDate;
    private int flightFromCityId;
    private int flightToCityId;
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
        headerTitle.setText(getString(R.string.title_pick_flight_list));
        source = getIntent().getStringExtra("source");
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initView() {
        mDbManager = new DBHelper(activity).getDbManager();
        ListView listView = (ListView) findViewById(R.id.flight_list);
        mAdapter = new FlightAdapter(activity);
        listView.setAdapter(mAdapter);
        View emptyView = findViewById(R.id.flight_empty_layout);
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(this);
        emptyView.setVisibility(View.GONE);
        flightNo = getIntent().getStringExtra(KEY_FLIGHT_NO);
        flightDate = getIntent().getStringExtra(KEY_FLIGHT_DATE);
        flightFromCityId = getIntent().getIntExtra(KEY_FLIGHT_FROM, -1);
        flightToCityId = getIntent().getIntExtra(KEY_FLIGHT_TO, -1);
        flightType = getIntent().getIntExtra(KEY_FLIGHT_TYPE, -1);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_pick_flight_list);
        mBusinessType = this.getIntent().getIntExtra("mBusinessType", -1);
        ButterKnife.bind(this);
        initView();
        initHeader();
        requestData();
    }

    protected Callback.Cancelable requestData() {
        TextView emptyNo = (TextView) findViewById(R.id.flight_empty_no);

        try {
            String tFlightDate = DateUtils.getWeekStrByDate(flightDate, DateUtils.dateDateFormat, DateUtils.dateWeekFormat2);
            flightInfo.setVisibility(View.VISIBLE);
            flightInfo.setText(tFlightDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BaseRequest request;
        if (flightType == 1) {
//            emptyNo.setText(String.format(getString(R.string.flight_empty_no), flightNo));
            request = new RequestFlightByNo(activity, flightNo, flightDate, mBusinessType);
        } else {
            emptyNo.setText(R.string.flight_empty_no_city);
            request = new RequestFlightByCity(activity, flightFromCityId, flightToCityId, flightDate);
        }
        requestData(request);
        return null;
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
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
        try {
            String tFlightDate = DateUtils.getWeekStrByDate(flightDate, DateUtils.dateDateFormat, DateUtils.dateWeekFormat2);
            if (count != 0) {
                flightInfo.setText(tFlightDate + " (共" + count + "趟航班)");
            } else {
                flightInfo.setVisibility(View.INVISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        inflateContent();
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