package com.hugboga.custom.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.hugboga.custom.widget.OrderGuidanceView;
import com.hugboga.custom.widget.monthpicker.model.CalendarDay;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.listener.MonthChangeListener;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.view.MonthSwitchView;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.view.MonthView;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseAirActivity;
import com.hugboga.custom.activity.ChooseCityActivity;
import com.hugboga.custom.activity.PickFlightListActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.SaveStartEndCity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created  on 16/5/13.
 */
public class FgChooseAirAddress extends BaseFragment implements MonthView.OnDayClickListener{

    @BindView(R.id.from_city)
    TextView fromCity;
    @BindView(R.id.end_city)
    TextView endCity;
    @BindView(R.id.from_city_tips)
    TextView fromCityTips;
    @BindView(R.id.end_city_tips)
    TextView endCityTips;
    /*@BindView(R.id.address_left)
    TextView addressLeft;
    @BindView(R.id.address_tips)
    TextView addressTips;
    @BindView(R.id.rl_address)
    LinearLayout rlAddress;*/
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.history_layout)
    LinearLayout historyLayout;
    @BindView(R.id.clean_all_history)
    TextView cleanAllHistory;
    @BindView(R.id.show_history)
    LinearLayout showHistory;
    @BindView(R.id.exchange)
    ImageView exchange;
    @BindView(R.id.start_layout)
    LinearLayout startLayout;
    @BindView(R.id.end_layout)
    LinearLayout endLayout;
    @BindView(R.id.view_month)
    MonthSwitchView mMonthPagerView;
    @BindView(R.id.air_address_guidance_layout)
    OrderGuidanceView guidanceLayout;
    String dateFormat="";
    FlightBean flightBean;
    @Override
    public int getContentViewId() {
        return R.layout.fg_choose_air_address;
    }

    @Override
    protected void initHeader() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getSaveInfo();
    }

    @Override
    protected void initView() {
    }

    public void chooseAirFragment() {
        EventBus.getDefault().post(new EventAction(EventType.CHOOSE_AIR_FRAGMENT, 1));
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity() instanceof ChooseAirActivity){
            flightBean = ((ChooseAirActivity) getActivity()).getFlightBean();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().register(this);
        if(flightBean!= null){
            if(fromCity!= null && endCity!= null){
                fromCity.setText(flightBean.depCityName);
                endCity.setText(flightBean.arrCityName);
                fromCityName = flightBean.depCityName;
                endCityName = flightBean.arrCityName;
                cityFromId = flightBean.depCityId;
                cityToId = flightBean.arrCityId;
                updateDate(flightBean.depDate);
            }

        }else{
            initDate();
        }
        checkNextBtnStatus();
        return rootView;
    }
    private void initDate() {
        Calendar beginDate= Calendar.getInstance();
        int year = beginDate.get(Calendar.YEAR);
        int month = beginDate.get(Calendar.MONTH)+1;
        int day = beginDate.get(Calendar.DAY_OF_MONTH);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 6);
        int endYear = endDate.get(Calendar.YEAR);
        int endMonth = endDate.get(Calendar.MONTH)+1;
        int endDay = endDate.get(Calendar.DAY_OF_MONTH);

        mMonthPagerView.setData(new CalendarDay(year, month, day), new CalendarDay(endYear, endMonth, endDay));
        mMonthPagerView.setOnDayClickListener(this);
        mMonthPagerView.setSelectDay(new CalendarDay(year, month, day));
        dateFormat = new CalendarDay(year, month, day).getDayString();

        setGuidanceLayout();
    }

    private void updateDate(String date) {
        if(date.isEmpty()){
            return;
        }
        Calendar beginDate= Calendar.getInstance();
        int year = beginDate.get(Calendar.YEAR);
        int month = beginDate.get(Calendar.MONTH)+1;
        int day = beginDate.get(Calendar.DAY_OF_MONTH);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 6);
        int endYear = endDate.get(Calendar.YEAR);
        int endMonth = endDate.get(Calendar.MONTH)+1;
        int endDay = endDate.get(Calendar.DAY_OF_MONTH);

        String[] temp = date.split("-");
        if(temp.length == 3){
            int lastYear = Integer.valueOf(temp[0]);
            int lastMonth = Integer.valueOf(temp[1]);
            int lastDay = Integer.valueOf(temp[2]);
            mMonthPagerView.setData(new CalendarDay(year, month, day), new CalendarDay(endYear, endMonth, endDay));
            mMonthPagerView.setOnDayClickListener(this);
            mMonthPagerView.setSelectDay(new CalendarDay(lastYear, lastMonth, lastDay));
            mMonthPagerView.setMonthChangeListener(new MonthChangeListener() {
                @Override
                public void onMonthChange(Date date) {
                    SensorsUtils.onAppClick(getEventSource(), "起飞日期", getIntentSource());
                }
            });
            dateFormat = date;
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    private int getMaxId() {
        return cityList.size() == 0 ? 0 : cityList.get(0).id + 1;
    }

    ArrayList<SaveStartEndCity> cityList = new ArrayList<>();

    private void getSaveInfo() {
        try {
            Type resultType = new TypeToken<List<SaveStartEndCity>>() {
            }.getType();
            if (null != resultType) {
                cityList = Reservoir.get("savedHistoryCityBean", resultType);
            }
            if (null != cityList) {
                genHistoryList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void genHistoryList() {
        historyLayout.removeAllViews();
        if(null != cityList && cityList.size() > 0){
            showHistory.setVisibility(View.VISIBLE);
            for(int i = 0;i < cityList.size();i++) {
                addHistoryItemView(cityList.get(i));
            }
        }
    }

    public void addHistoryItemView(SaveStartEndCity saveStartEndCity) {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.air_history_item, null);
        TextView historyText = (TextView) view.findViewById(R.id.history_text);
        //ImageView historyDel = (ImageView) view.findViewById(R.id.history_del);
        historyText.setText(saveStartEndCity.startCityName + " - " + saveStartEndCity.endCityName);
        historyText.setTag(saveStartEndCity.id);
        //historyDel.setTag(saveStartEndCity.id);
        view.setTag(saveStartEndCity.id);
        historyText.setOnClickListener(new HistoryTextClick());
        //historyDel.setOnClickListener(new HistoryTextDelClick());
        historyLayout.addView(view);
    }

    @Override
    public void onDayClick(CalendarDay calendarDay) {
        dateFormat = calendarDay.getDayString();
        checkNextBtnStatus();
        SensorsUtils.onAppClick(getEventSource(), "起飞日期", getIntentSource());
    }

    private class HistoryTextClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int vId = Integer.valueOf(v.getTag().toString()).intValue();
            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).id == vId) {
                    cityFromId = cityList.get(i).startCityId;
                    cityToId = cityList.get(i).endCityId;
                    fromCityName = cityList.get(i).startCityName;
                    endCityName = cityList.get(i).endCityName;
                    fromCity.setText(fromCityName);
                    endCity.setText(endCityName);
                    break;
                }
            }
            checkNextBtnStatus();
            if (historyLayout.indexOfChild(v) != 0) {
                addHistoryData();
                genHistoryList();
            }
            SensorsUtils.onAppClick("接机", "历史航班号", getIntentSource());
        }
    }

    /*private class HistoryTextDelClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int vId = Integer.valueOf(v.getTag().toString()).intValue();
            for (int i = 0; i < historyLayout.getChildCount(); i++) {
                if (vId == Integer.valueOf(historyLayout.getChildAt(i).getTag().toString())) {
                    historyLayout.removeViewAt(i);
                }
            }

            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).id == vId) {
                    cityList.remove(i);
                }
            }

            try {
                Reservoir.put("savedHistoryCityBean", cityList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (historyLayout.getChildCount() == 0) {
                showHistory.setVisibility(View.GONE);
            }
        }
    }*/


    private void addHistoryData() {
        SaveStartEndCity savedCityBean = new SaveStartEndCity();
        savedCityBean.startCityId = cityFromId;
        savedCityBean.startCityName = fromCityName;
        savedCityBean.endCityId = cityToId;
        savedCityBean.endCityName = endCityName;
        savedCityBean.id = getMaxId();
        for (int i = 0; i < cityList.size(); i++) {
            if (cityFromId == cityList.get(i).startCityId
                    && fromCityName.equalsIgnoreCase(cityList.get(i).startCityName)
                    && endCityName.equalsIgnoreCase(cityList.get(i).endCityName)
                    && cityToId == cityList.get(i).endCityId
                    ) {
                cityList.remove(i);
                break;
            }

        }
        if (cityList.size() == 15) {
            cityList.remove(14);
        }
        cityList.add(0, savedCityBean);
        try {
            Reservoir.put("savedHistoryCityBean", cityList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    int cityFromId, cityToId;
    String fromCityName, endCityName;

//    @Override
//    public void onFragmentResult(Bundle bundle) {
//        MLog.w(this + " onFragmentResult " + bundle);
//        String from = bundle.getString(KEY_FROM);
//        if ("startAddress".equals(from)) {
//            CityBean city = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
//            if (city != null) {
//                fromCityName = city.name;
//                fromCity.setText(fromCityName);
//                cityFromId = city.cityId;
//            }
//        } else if ("end".equals(from)) {
//            CityBean city = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
//            if (city != null) {
//                endCityName = city.name;
//                endCity.setText(endCityName);
//                cityToId = city.cityId;
//            }
//        } else if ("FlightList".equals(from)) {
//            EventBus.getDefault().post(new EventAction(EventType.AIR_NO, bundle.getSerializable(FgPickFlight.KEY_AIRPORT)));
//            finish();
//        }
//        checkNextBtnStatus();
//    }

    @OnClick({R.id.start_layout,R.id.end_layout, R.id.from_city_tips, R.id.from_city
            , R.id.end_city, R.id.search, R.id.clean_all_history, R.id.exchange, R.id.end_city_tips_layout})
    public void onClick(View view) {
//        FgChooseCity city = new FgChooseCity();
        Intent intent = new Intent(getActivity(), ChooseCityActivity.class);
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.start_layout:
            case R.id.from_city_tips:
            case R.id.from_city:
                bundle.putString(KEY_FROM, "startAddress");
                bundle.putInt(ChooseCityActivity.KEY_SHOW_TYPE, ChooseCityActivity.ShowType.PICK_UP);
                bundle.putInt(ChooseCityActivity.KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_PICK);
//                city.setArguments(bundle);
//                startFragment(city);

                intent.putExtras(bundle);
                getActivity().startActivity(intent);

                break;
            case R.id.end_layout:
            case R.id.end_city:
                bundle.putString(KEY_FROM, "end");
                bundle.putInt(ChooseCityActivity.KEY_SHOW_TYPE, ChooseCityActivity.ShowType.PICK_UP);
                bundle.putInt(ChooseCityActivity.KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_PICK);
//                city.setArguments(bundle);
//                startFragment(city);

                intent.putExtras(bundle);
                getActivity().startActivity(intent);

                break;
            case R.id.search:
                addHistoryData();
                startFlightByCity();
                SensorsUtils.onAppClick(getEventSource(), "查询航班", source);
                break;
            case R.id.clean_all_history:
                historyLayout.removeAllViews();
                cityList.clear();
                try {
                    Reservoir.put("savedHistoryCityBean", cityList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (historyLayout.getChildCount() == 0) {
                    showHistory.setVisibility(View.GONE);
                }
                break;
            case R.id.exchange:
                exChangeCity();
                break;
            /*case R.id.address_tips:
            case R.id.rl_address:
//                showDaySelect(addressTips);
                intent = new Intent(getActivity(),DatePickerActivity.class);
                intent.putExtra("type",3);
                intent.putExtra("title","请选择出发日期");
                intent.putExtra("chooseDateBean",chooseDateBean);
                getActivity().startActivity(intent);
                break;*/
            case R.id.end_city_tips_layout:
                chooseAirFragment();
                break;
            default:
                break;
        }
    }

    ChooseDateBean chooseDateBean;
    CityBean city;
    @Subscribe
    public void onEventMainThread(final EventAction action) {

        switch (action.getType()) {
            case CHOOSE_DATE:
                /*chooseDateBean = (ChooseDateBean) action.getData();
                if (chooseDateBean.type == 3) {
                    time2Str = chooseDateBean.halfDateStr;
                    addressTips.setText(time2Str);
                    checkNextBtnStatus();
                }*/
                break;
            case CHOOSE_START_CITY_BACK:
                city =  (CityBean)action.getData();
                if (city != null) {
                    fromCityName = city.name;
                    fromCity.setText(fromCityName);
                    cityFromId = city.cityId;
                }
                checkNextBtnStatus();
                break;
            case CHOOSE_END_CITY_BACK:
                CityBean endCityBean =  (CityBean)action.getData();
                if (endCityBean != null && ("中国".equals(endCityBean.placeName) || "中国大陆".equals(endCityBean.placeName))) {
                    CommonUtils.showToast("航班降落地点应该选在境外哦");
                    break;
                }
                this.city = endCityBean;
                if (city != null) {
                    endCityName = city.name;
                    endCity.setText(endCityName);
                    cityToId = city.cityId;
                }
                checkNextBtnStatus();
                break;



        }
    }

    private void checkNextBtnStatus() {
        String from = fromCity.getText().toString();
        String to = endCity.getText().toString();
        //String time1Str = addressTips.getText().toString();
        if (!TextUtils.isEmpty(from) && !TextUtils.isEmpty(to) && !TextUtils.isEmpty(dateFormat)) {
            search.setEnabled(true);
        } else {
            search.setEnabled(false);
        }

    }

    String time2Str = "";

    /**
     * 根据城市查
     */
    private void startFlightByCity() {
//        FgPickFlightList fragment = new FgPickFlightList();
        Bundle bundle = new Bundle();
        bundle.putInt(PickFlightListActivity.KEY_FLIGHT_FROM, cityFromId);
        bundle.putInt(PickFlightListActivity.KEY_FLIGHT_TO, cityToId);
        bundle.putInt(PickFlightListActivity.KEY_FLIGHT_TYPE, 2);
        bundle.putString(PickFlightListActivity.KEY_FLIGHT_DATE, dateFormat);
        bundle.putString(Constants.PARAMS_TAG, getSourceTag());
        bundle.putInt("mBusinessType",1);
//        fragment.setArguments(bundle);
//        startFragment(fragment);
        Intent intent = new Intent(getActivity(),PickFlightListActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    private void exChangeCity() {
        int tmpId = cityFromId;
        cityFromId = cityToId;
        cityToId = tmpId;

        fromCityName = endCityName;
        endCityName = fromCity.getText().toString();
        fromCity.setText(fromCityName);
        endCity.setText(endCityName);
    }

    @Override
    public String getEventSource() {
        if (getContext() instanceof PickSendActivity) {
            return "接机";
        } else {
            return "选择航班";
        }
    }

    public String getIntentSource() {
        if (getContext() instanceof PickSendActivity) {
            return ((PickSendActivity) getContext()).getIntentSource();
        } else if (getContext() instanceof ChooseAirActivity) {
            return ((ChooseAirActivity) getContext()).getIntentSource();
        } else {
            return "";
        }
    }

    public String getSourceTag() {
        if (getContext() instanceof ChooseAirActivity) {
            return ((ChooseAirActivity) getContext()).getSourceTag();
        }
        return null;
    }

    public void setGuidanceLayout() {
        if (getContext() instanceof PickSendActivity) {
            setSensorsBuyFlightEvent();
            PickSendActivity.Params params = ((PickSendActivity) getContext()).getParams();
            if (params == null || params.guidesDetailData != null) {
                guidanceLayout.setVisibility(View.GONE);
                return;
            } else {
                guidanceLayout.setVisibility(View.VISIBLE);
                String cityId = "";
                String cityName = "";
                if (params.flightBean != null) {
                    cityId = "" + params.flightBean.arrCityId;
                    cityName = params.flightBean.arrCityName;
                } else if (params.airPortBean != null) {//航班信息为空，默认显示送机机场所在城市
                    cityId = "" +  params.airPortBean.cityId;
                    cityName = params.airPortBean.cityName;
                } else if (!TextUtils.isEmpty(params.cityId) && !TextUtils.isEmpty(params.cityName)) {
                    guidanceLayout.setData("" +  params.cityId, params.cityName);
                    cityId = "" +  params.cityId;
                    cityName = params.cityName;
                }
                guidanceLayout.setData(cityId, cityName);
            }
        }
    }

    private void setSensorsBuyFlightEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_refer", getIntentSource());
            SensorsDataAPI.sharedInstance(getContext()).track("buy_flight", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
