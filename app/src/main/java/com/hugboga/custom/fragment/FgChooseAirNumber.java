package com.hugboga.custom.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.widget.OrderGuidanceView;
import com.hugboga.custom.widget.monthpicker.model.CalendarDay;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.listener.MonthChangeListener;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.view.MonthSwitchView;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.view.MonthView;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseAirActivity;
import com.hugboga.custom.activity.PickFlightListActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.SaveStartEndCity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.widget.calendar.CalendarUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
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
public class FgChooseAirNumber extends BaseFragment implements MonthView.OnDayClickListener{

    @BindView(R.id.number_left)
    TextView numberLeft;
    @BindView(R.id.number_tips)
    EditText numberTips;
    @BindView(R.id.rl_number)
    LinearLayout rlNumber;
    @BindView(R.id.address_left)
    TextView addressLeft;
    /*@BindView(R.id.address_tips)
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
    /*@BindView(R.id.calendar)
    ZGridRecyclerView gridView;*/
    /*@BindView(R.id.reserve_calendar_title)
    TextView reserve_calendar_title;
    @BindView(R.id.reserve_calendar_prover)
    ImageView prover;
    @BindView(R.id.reserve_calendar_next)
    ImageView next;*/
    @BindView(R.id.view_month)
    MonthSwitchView mMonthPagerView;
    @BindView(R.id.air_num_guidance_layout)
    OrderGuidanceView guidanceLayout;

    //CalendarAdapter calAdapter;
    Calendar thisCalendar = Calendar.getInstance(); //日历当前显示日期
    String dateFormat="";
    FlightBean flightBean;

    private boolean isOperated = true;//在页面有任意点击操作就记录下来，只记录第一次，统计需要
    @Override
    public int getContentViewId() {
        return R.layout.fg_choose_air_number;
    }

    @Override
    protected void initHeader() {

    }

    @Override
    protected void initView() {
        numberTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsUtils.onAppClick("接机", "航班号", getIntentSource());
            }
        });
        numberTips.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkNextBtnStatus();
            }
        });
        setGuidanceLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(calAdapter!= null && calAdapter.getDatas()!= null && calAdapter.getDatas().size()>0){
            calAdapter.removeAll();
        }
        calAdapter.addDatas(CalendarUtils.getCalendarData(thisCalendar));*/
        //reserve_calendar_title.setText(new SimpleDateFormat("yyyy年M月").format(thisCalendar.getTime()));
        getSaveInfo();
    }

    @OnClick(R.id.air_address_layout)
    public void chooseFragment() {
        EventBus.getDefault().post(new EventAction(EventType.CHOOSE_AIR_FRAGMENT, 2));
    }

    private int getMaxId(){
        return cityList.size() == 0?0:cityList.get(0).id + 1 ;
    }
    ArrayList<SaveStartEndCity> cityList = new ArrayList<>();

    private void getSaveInfo(){
        try {
            Type resultType = new TypeToken<List<SaveStartEndCity>>() {}.getType();
            cityList = Reservoir.get("savedHistoryCityBeanNo", resultType);
            genHistoryList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity() instanceof ChooseAirActivity){
            flightBean = ((ChooseAirActivity) getActivity()).getFlightBean();
        }
    }

    private void genHistoryList(){
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
        historyText.setText(saveStartEndCity.airNo);
        historyText.setTag(saveStartEndCity.id);
        //historyDel.setTag(saveStartEndCity.id);
        view.setTag(saveStartEndCity.id);
        historyText.setOnClickListener(new HistoryTextClick());
        //historyDel.setOnClickListener(new HistoryTextDelClick());
        historyLayout.addView(view);
    }

    @Override
    public void onDayClick(CalendarDay calendarDay) {
        setSensorsOnOperated();
        dateFormat = calendarDay.getDayString();
        checkNextBtnStatus();
        //CommonUtils.showToast(calendarDay.getDayString());
        SensorsUtils.onAppClick(getEventSource(), "起飞日期", getIntentSource());
    }

    private class HistoryTextClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int vId = Integer.valueOf(v.getTag().toString()).intValue();
            for(int i = 0;i< cityList.size();i++) {
                if(cityList.get(i).id == vId) {
                    noStr = cityList.get(i).airNo;
                    numberTips.setText(noStr);
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
            for(int i =0 ;i< historyLayout.getChildCount();i++){
                if(vId == Integer.valueOf(historyLayout.getChildAt(i).getTag().toString())){
                    historyLayout.removeViewAt(i);
                }
            }

            for(int i = 0;i< cityList.size();i++) {
                if(cityList.get(i).id == vId) {
                    cityList.remove(i);
                }
            }

            try {
                Reservoir.put("savedHistoryCityBeanNo", cityList);
            }catch (Exception e){
                e.printStackTrace();
            }
            if(historyLayout.getChildCount() == 0){
                showHistory.setVisibility(View.GONE);
            }
        }
    }*/

    private void addHistoryData(){
        SaveStartEndCity savedCityBean = new SaveStartEndCity();
        savedCityBean.airNo = noStr.toUpperCase();
        savedCityBean.id = getMaxId();
        for(int i = 0;i< cityList.size();i++){
            if(noStr.toUpperCase().equalsIgnoreCase(cityList.get(i).airNo)){
                cityList.remove(i);
                break;
            }

        }
        if(cityList.size() ==15 ){
            cityList.remove(14);
        }
        cityList.add(0,savedCityBean);
        try {
            Reservoir.put("savedHistoryCityBeanNo", cityList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        //EventBus.getDefault().register(this);
        /*gridView.setColumn(7);
        calAdapter = new CalendarAdapter(getContext());
        gridView.setAdapter(calAdapter);
        calAdapter.setOnItemClickListener(onItemClickListener);*/
        if(flightBean!= null){
            if(numberTips!= null){
                numberTips.setText(flightBean.flightNo);
                updateDate(flightBean.depDate);
            }

        }else{
            initDate();
        }
        checkNextBtnStatus();
        return rootView;
    }
    /*ZBaseAdapter.OnItemClickListener onItemClickListener = new ZBaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            CalendarCell cell = calAdapter.getDatas().get(position);
            dateFormat= new SimpleDateFormat("yyyy-MM-dd").format(thisCalendar.getTime());
            calAdapter.setSelectItem(position);
            calAdapter.notifyDataSetChanged();
            Log.d("zq","adfa");
        }

    };*/
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
        mMonthPagerView.setMonthChangeListener(new MonthChangeListener() {
            @Override
            public void onMonthChange(Date date) {
                SensorsUtils.onAppClick(getEventSource(), "起飞日期", getIntentSource());
            }
        });
        dateFormat = new CalendarDay(year, month, day).getDayString();
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
            dateFormat = date;
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.search, R.id.clean_all_history,/*R.id.address_tips,R.id.reserve_calendar_prover,R.id.reserve_calendar_next*/})
    public void onClick(View view) {
        setSensorsOnOperated();
        switch (view.getId()) {
            case R.id.search:
                startFlightByNo();
                addHistoryData();
                SensorsUtils.onAppClick(getEventSource(), "查询航班", source);
                break;
            case R.id.clean_all_history:
                historyLayout.removeAllViews();
                cityList.clear();
                try {
                    Reservoir.put("savedHistoryCityBeanNo", cityList);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(historyLayout.getChildCount() == 0){
                    showHistory.setVisibility(View.GONE);
                }
                break;
            /*case R.id.address_tips:
//                showDaySelect(addressTips);
                Intent intent = new Intent(getActivity(),DatePickerActivity.class);
                intent.putExtra("type",3);
                intent.putExtra("title","请选择出发日期");
                intent.putExtra("chooseDateBean",chooseDateBean);
                getActivity().startActivity(intent);
                break;
            case R.id.reserve_calendar_prover:
                thisCalendar.add(Calendar.MONTH, -1);
                checkUpMonth(true);
                break;
            case R.id.reserve_calendar_next:
                thisCalendar.add(Calendar.MONTH, 1);
                checkDownMonth(true);
                break;*/
        }
    }
    @SuppressLint("WrongConstant")
    private void checkUpMonth(boolean isload) {
        setSensorsOnOperated();
        Calendar startDate = Calendar.getInstance();
        Integer resultCode = CalendarUtils.isLostMonth(thisCalendar, startDate);
        switch (resultCode) {
            case 0:
                //prover.setVisibility(View.INVISIBLE);
                //next.setVisibility(View.VISIBLE);
                reloadDataOfMonth();
                break;
            case 1:
                //prover.setVisibility(View.VISIBLE);
                //next.setVisibility(View.VISIBLE);
                reloadDataOfMonth();
                break;
            case -1:
                thisCalendar.add(Calendar.MONTH, 1);
                break;
        }
    }

    @SuppressLint("WrongConstant")
    private void checkDownMonth(boolean isload) {
        setSensorsOnOperated();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 6);
        Integer resultCodeN = CalendarUtils.isMastMonth(thisCalendar, endDate);
        switch (resultCodeN) {
            case 0:
                //prover.setVisibility(View.VISIBLE);
                //next.setVisibility(View.INVISIBLE);
                reloadDataOfMonth();
                break;
            case 1:
                //prover.setVisibility(View.VISIBLE);
                //next.setVisibility(View.VISIBLE);
                reloadDataOfMonth();
                break;
            case -1:
                thisCalendar.add(Calendar.MONTH, -1);
                break;
        }
    }
    /**
     * 重新加载月份数据
     */
    private void reloadDataOfMonth() {
        //reserve_calendar_title.setText(new SimpleDateFormat("yyyy年M月").format(thisCalendar.getTime()));
        /*if(calAdapter != null){
            calAdapter.removeAll();
            calAdapter.addDatas(CalendarUtils.getCalendarData(thisCalendar));
//            calAdapter.notifyDataSetChanged();
        }*/
    }
    String noStr= "";
    /**
     * 根据航班查
     */
    private void startFlightByNo() {
        noStr = numberTips.getText().toString();
        //String time1Str = addressTips.getText().toString();
        if (TextUtils.isEmpty(noStr)) {
            CommonUtils.showToast("请填写航班号");
            return;
        }
        if (TextUtils.isEmpty(dateFormat)) {
            CommonUtils.showToast("请选择航班时间");
            return;
        }
        noStr = noStr.replaceAll(" ", "");
        Bundle bundle = new Bundle();
        bundle.putString(PickFlightListActivity.KEY_FLIGHT_NO, noStr.toUpperCase());
        bundle.putString(PickFlightListActivity.KEY_FLIGHT_DATE, dateFormat);
        bundle.putInt(PickFlightListActivity.KEY_FLIGHT_TYPE, 1);
        bundle.putString(Constants.PARAMS_TAG, getSourceTag());
        bundle.putString("source",source);
        bundle.putInt("mBusinessType",1);
        Intent intent = new Intent(getActivity(),PickFlightListActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    /*ChooseDateBean chooseDateBean;
    @Subscribe
    public void onEventMainThread(final EventAction action) {

        switch (action.getType()) {
            case CHOOSE_DATE:
                chooseDateBean = (ChooseDateBean) action.getData();
                if (chooseDateBean.type == 3) {
                    String serverDate = chooseDateBean.halfDateStr;
                    //addressTips.setText(serverDate);
                    checkNextBtnStatus();
                }
                break;
        }
    }*/



    private void checkNextBtnStatus(){
        String noStr = numberTips.getText().toString();
        //String time1Str = addressTips.getText().toString();
        if (!TextUtils.isEmpty(noStr) && !TextUtils.isEmpty(dateFormat)) {
            numberTips.setTextColor(getResources().getColor(R.color.common_font_color_black));
            search.setEnabled(true);
        }else{
            search.setEnabled(false);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        hideInputMethod(numberTips);
    }

    //神策统计_下单-有操作
    private void setSensorsOnOperated() {
        if (isOperated && getContext() instanceof PickSendActivity) {
            isOperated = false;
            SensorsUtils.onOperated(((PickSendActivity) getContext()).getIntentSource(), getEventSource());
        }
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
