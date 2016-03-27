package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FlightAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.request.RequestFlightByCity;
import com.hugboga.custom.data.request.RequestFlightByNo;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DateUtils;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 接机选择航班列表
 */
@ContentView(R.layout.fg_pick_flight_list)
public class FgPickFlightList extends BaseFragment implements AdapterView.OnItemClickListener {
    public static final String KEY_FLIGHT_NO = "flight_no";
    public static final String KEY_FLIGHT_DATE = "flight_date";
    public static final String KEY_FLIGHT_FROM = "flight_from";
    public static final String KEY_FLIGHT_TO = "flight_to";
    public static final String KEY_FLIGHT_TYPE = "flight_type";

    @ViewInject(R.id.flight_info)
    private TextView flightInfo;

    private FlightAdapter mAdapter;
    private ArrayList<FlightBean> mListDate;
    private int flightType = 1;//1:按航班查询，2按城市查询
    private String flightNo;
    private String flightDate;
    private int flightFromCityId;
    private int flightToCityId;
    private DbManager mDbManager;

    @Override
    protected void initHeader() {
        setProgressState(0);
        fgTitle.setText(getString(R.string.title_pick_flight_list));
    }

    @Override
    protected void initView() {
        mDbManager = new DBHelper(getActivity()).getDbManager();
        ListView listView = (ListView) getView().findViewById(R.id.flight_list);
        mAdapter = new FlightAdapter(getActivity());
        listView.setAdapter(mAdapter);
        View emptyView = getView().findViewById(R.id.flight_empty_layout);
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(this);
        emptyView.setVisibility(View.GONE);
        flightNo = getArguments().getString(KEY_FLIGHT_NO);
        flightDate = getArguments().getString(KEY_FLIGHT_DATE);
        flightFromCityId = getArguments().getInt(KEY_FLIGHT_FROM);
        flightToCityId = getArguments().getInt(KEY_FLIGHT_TO);
        flightType = getArguments().getInt(KEY_FLIGHT_TYPE);
    }

    @Override
    protected Callback.Cancelable requestData() {
        TextView emptyNo = (TextView) getView().findViewById(R.id.flight_empty_no);

        try {
            String tFlightDate = DateUtils.getWeekStrByDate(flightDate, DateUtils.dateDateFormat, DateUtils.dateWeekFormat2);
            flightInfo.setText(tFlightDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BaseRequest request;
        if (flightType == 1) {
            emptyNo.setText(String.format(getString(R.string.flight_empty_no), flightNo));
            request = new RequestFlightByNo(getActivity(), flightNo, flightDate, mBusinessType);
        } else {
            emptyNo.setText(R.string.flight_empty_no_city);
            request = new RequestFlightByCity(getActivity(), flightFromCityId, flightToCityId, flightDate);
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
            flightInfo.setText(tFlightDate + " (共" + count + "趟航班)");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        inflateContent();
    }

    @Override
    protected void inflateContent() {
        mAdapter.setList(mListDate);
        mAdapter.notifyDataSetChanged();
    }

    @Event({R.id.pick_btn, R.id.flight_empty_btn})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.pick_btn:
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FlightBean bean = mListDate.get(position);
        if (!bean.serviceStatus) {
            Toast.makeText(getActivity(), "机场信息未查到", Toast.LENGTH_LONG).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(FgPickFlight.KEY_AIRPORT, bean);
        bundle.putString(KEY_FROM, "FlightList");
        finishForResult(bundle);
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

        } catch (DbException e) {
            e.printStackTrace();
        }

    }

}
