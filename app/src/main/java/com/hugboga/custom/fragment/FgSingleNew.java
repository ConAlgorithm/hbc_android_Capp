package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CarViewpagerAdapter;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.DailyBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForSingle;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created  on 16/5/20.
 */
@ContentView(R.layout.fg_single_new)
public class FgSingleNew extends BaseFragment {
    @Bind(R.id.city_use_car_left)
    TextView cityUseCarLeft;
    @Bind(R.id.use_city_tips)
    TextView useCityTips;
    @Bind(R.id.city_layout)
    LinearLayout cityLayout;
    @Bind(R.id.start_left)
    TextView startLeft;
    @Bind(R.id.start_tips)
    TextView startTips;
    @Bind(R.id.start_title)
    TextView startTitle;
    @Bind(R.id.start_detail)
    TextView startDetail;
    @Bind(R.id.start_layout)
    LinearLayout startLayout;
    @Bind(R.id.end_left)
    TextView endLeft;
    @Bind(R.id.end_tips)
    TextView endTips;
    @Bind(R.id.end_title)
    TextView endTitle;
    @Bind(R.id.end_detail)
    TextView endDetail;
    @Bind(R.id.end_layout)
    LinearLayout endLayout;
    @Bind(R.id.time_left)
    TextView timeLeft;
    @Bind(R.id.time_text)
    TextView timeText;
    @Bind(R.id.time_layout)
    LinearLayout timeLayout;
    @Bind(R.id.rl_starttime)
    RelativeLayout rlStarttime;
    @Bind(R.id.show_cars_layout_single)
    LinearLayout showCarsLayoutSingle;
    @Bind(R.id.confirm_journey)
    TextView confirmJourney;
    @Bind(R.id.all_money_left)
    TextView allMoneyLeft;
    @Bind(R.id.all_money_text)
    TextView allMoneyText;
    @Bind(R.id.all_journey_text)
    TextView allJourneyText;
    @Bind(R.id.bottom)
    RelativeLayout bottom;

    private PoiBean arrivalBean;//达到目的地
    private String serverTime;

    private FlightBean flightBean;//航班信息 接机
    private AirPort airPortBean;//机场信息 送机
    private DailyBean dailyBean;// 日租
    private CityBean cityBean;//起始目的地 次租
    private PoiBean startBean;//起始目的地 次租
    private PoiBean poiBean;//达到目的地

    private CarBean carBean;//车
    private String serverDate;

    private double distance;//预估路程（单位：公里）
    private int interval;//预估时间（单位：分钟）
    private ArrayList<CarBean> carList = new ArrayList<CarBean>();
    ;
    private CarViewpagerAdapter mAdapter;
    private int cityId;
    private String airportCode;
    private int urgentFlag;//是否急单，1是，0非
    private boolean needChildrenSeat = false;//是否需要儿童座椅
    private boolean needBanner = true;//是否可以展示接机牌
    private DialogUtil mDialogUtil;


    @Override
    protected void initHeader() {
        fgTitle.setText("单次接送");
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setText("常见问题");
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_PROBLEM);
                startFragment(new FgWebInfo(), bundle);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("source", "填写行程页面");
                MobclickAgent.onEvent(getActivity(), "callcenter_oneway", map);
                v.setTag("填写行程页面,calldomestic_oneway,calloverseas_oneway");

            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    private void checkInput() {
        if (!TextUtils.isEmpty(timeText.getText()) && !TextUtils.isEmpty(useCityTips.getText())
                && !TextUtils.isEmpty(startTitle.getText())
                && !TextUtils.isEmpty(endTitle.getText())) {
            getData();
        }
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCity.class.getSimpleName().equals(from)) {
            cityBean = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
            useCityTips.setText(cityBean.name);
            startBean = null;
            arrivalBean = null;
            startTips.setVisibility(View.VISIBLE);
            startTitle.setVisibility(View.GONE);
            startDetail.setVisibility(View.GONE);
            startTitle.setText("");
            startDetail.setText("");

            endTips.setVisibility(View.VISIBLE);
            endTitle.setVisibility(View.GONE);
            endDetail.setVisibility(View.GONE);
            endTitle.setText("");
            endDetail.setText("");
        } else if (FgPoiSearch.class.getSimpleName().equals(from)) {
            String fromKey = bundle.getString(KEY_FROM);
            if ("from".equals(fromKey)) {
                startBean = (PoiBean) bundle.getSerializable(FgPoiSearch.KEY_ARRIVAL);
                startTips.setVisibility(View.GONE);
                startTitle.setVisibility(View.VISIBLE);
                startDetail.setVisibility(View.VISIBLE);
                startTitle.setText(startBean.placeName);
                startDetail.setText(startBean.placeDetail);
            } else if ("to".equals(fromKey)) {
                arrivalBean = (PoiBean) bundle.getSerializable(FgPoiSearch.KEY_ARRIVAL);
                endTips.setVisibility(View.GONE);
                endTitle.setVisibility(View.VISIBLE);
                endDetail.setVisibility(View.VISIBLE);
                endTitle.setText(arrivalBean.placeName);
                endDetail.setText(arrivalBean.placeDetail);
            }
            collapseSoftInputMethod();
            checkInput();
        }
    }

    String startLocation, termLocation;

    protected void getData() {
        cityId = cityBean.cityId;
        needChildrenSeat = cityBean.childSeatSwitch;
        startLocation = startBean.location;
        termLocation = arrivalBean.location;
        RequestCheckPriceForSingle requestCheckPriceForSingle = new RequestCheckPriceForSingle(getActivity(), mBusinessType, airportCode, cityId, startLocation, termLocation, serverDate + " " + serverTime);
        requestData(requestCheckPriceForSingle);
    }

    CarListBean carListBean;

    private void genBottomData(CarBean carBean) {
        allMoneyText.setText("￥ " + carBean.originalPrice);
        allJourneyText.setText("全程预估:" + carListBean.distance + "公里," + carListBean.interval + "分钟");
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCheckPrice) {
            RequestCheckPrice requestCheckPrice = (RequestCheckPrice) request;
            carListBean = (CarListBean) requestCheckPrice.getData();
            if (carListBean.carList.size() > 0) {
                bottom.setVisibility(View.VISIBLE);
                genBottomData(carListBean.carList.get(0));
            } else {
                bottom.setVisibility(View.GONE);
            }

            initCarFragment();
        }
    }

    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHANGE_CAR:
                CarBean carBean = (CarBean) action.getData();
                genBottomData(carBean);
                break;
            default:
                break;
        }
    }

    FragmentManager fm;
    FgCarNew fgCarNew;

    private void initCarFragment() {
        fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (null != fgCarNew) {
            transaction.remove(fgCarNew);
        }

        fgCarNew = new FgCarNew();
        Bundle bundle = new Bundle();
        if (getArguments() != null) {
            bundle.putAll(getArguments());
        }
        bundle.putParcelable("carListBean", carListBean);
        fgCarNew.setArguments(bundle);
        transaction.add(R.id.show_cars_layout_single, fgCarNew);
        transaction.commit();
    }


    public void showDaySelect() {
        Calendar cal = Calendar.getInstance();
        MyDatePickerListener myDatePickerDialog = new MyDatePickerListener(timeText);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                myDatePickerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal = Calendar.getInstance();
        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        dpd.setMaxDate(cal);
        dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框

    }

    class MyDatePickerListener implements DatePickerDialog.OnDateSetListener {
        TextView mTextView;

        MyDatePickerListener(TextView textView) {
            this.mTextView = textView;
        }

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear + 1;
            String monthStr = String.format("%02d", month);
            String dayOfMonthStr = String.format("%02d", dayOfMonth);
            serverDate = year + "-" + monthStr + "-" + dayOfMonthStr;
            showTimeSelect();
        }
    }

    public void showTimeSelect() {
        Calendar cal = Calendar.getInstance();
        MyTimePickerDialogListener myTimePickerDialog = new MyTimePickerDialogListener();
        TimePickerDialog datePickerDialog = TimePickerDialog.newInstance(myTimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        datePickerDialog.show(this.getActivity().getFragmentManager(), "TimePickerDialog");                //显示日期设置对话框
    }


    class MyTimePickerDialogListener implements TimePickerDialog.OnTimeSetListener {


        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            String hour = String.format("%02d", hourOfDay);
            String minuteStr = String.format("%02d", minute);
            serverTime = hour + ":" + minuteStr;
            timeText.setText(serverDate + " " + serverTime);
            checkInput();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.city_layout, R.id.start_layout, R.id.end_layout, R.id.time_layout, R.id.confirm_journey ,R.id.start_tips, R.id.start_title, R.id.start_detail, R.id.end_tips, R.id.end_title, R.id.end_detail})
    public void onClick(View view) {
        HashMap<String, String> map = new HashMap<String, String>();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.city_layout:
                bundle.putString("source", "下单过程中");
                startFragment(new FgChooseCity(), bundle);
                map.put("source", "下单过程中");
                MobclickAgent.onEvent(getActivity(), "search_trigger", map);
                break;
            case R.id.start_tips:
            case R.id.start_title:
            case R.id.start_detail:
            case R.id.start_layout:
                if (cityBean != null) {
                    FgPoiSearch fg = new FgPoiSearch();
                    bundle.putString("source", "下单过程中");
                    bundle.putString(KEY_FROM, "from");
                    bundle.putInt(FgPoiSearch.KEY_CITY_ID, cityBean.cityId);
                    bundle.putString(FgPoiSearch.KEY_LOCATION, cityBean.location);
                    startFragment(fg, bundle);
                    map.put("source", "下单过程中");
                    MobclickAgent.onEvent(getActivity(), "search_trigger", map);
                } else {
                    Toast.makeText(getActivity(), "先选择城市", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.end_tips:
            case R.id.end_title:
            case R.id.end_detail:
            case R.id.end_layout:
                if (cityBean != null) {
                    FgPoiSearch fg = new FgPoiSearch();
                    bundle.putString("source", "下单过程中");
                    bundle.putString(KEY_FROM, "to");
                    bundle.putInt(FgPoiSearch.KEY_CITY_ID, cityBean.cityId);
                    bundle.putString(FgPoiSearch.KEY_LOCATION, cityBean.location);
                    startFragment(fg, bundle);
                    map.put("source", "下单过程中");
                    MobclickAgent.onEvent(getActivity(), "search_trigger", map);
                } else {
                    Toast.makeText(getActivity(), "先选择城市", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.time_layout:
                if (startBean == null) {
                    Toast.makeText(getActivity(), "请先选择城市", Toast.LENGTH_LONG).show();
                    return;
                }
                showDaySelect();
                break;
            case R.id.confirm_journey:
                break;
        }
    }
}
