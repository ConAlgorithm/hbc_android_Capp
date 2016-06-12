package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CarViewpagerAdapter;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForTransfer;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.ToastUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hugboga.custom.R.id.driver_layout;
import static com.hugboga.custom.R.id.driver_name;
import static u.aly.au.W;

import de.greenrobot.event.EventBus;

/**
 * Created  on 16/5/13.
 */
@ContentView(R.layout.fg_sendnew)
public class FgSendNew extends BaseFragment implements View.OnTouchListener {

    @Bind(R.id.info_left)
    TextView infoLeft;
    @Bind(R.id.info_tips)
    TextView infoTips;
    @Bind(R.id.air_title)
    TextView airTitle;
    @Bind(R.id.air_detail)
    TextView airDetail;
    @Bind(R.id.rl_info)
    RelativeLayout rlInfo;
    @Bind(R.id.address_left)
    TextView addressLeft;
    @Bind(R.id.address_tips)
    TextView addressTips;
    @Bind(R.id.rl_address)
    RelativeLayout rlAddress;
    @Bind(R.id.time_left)
    TextView timeLeft;
    @Bind(R.id.time_text)
    TextView timeText;
    @Bind(R.id.rl_starttime)
    RelativeLayout rlStarttime;
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
    @Bind(R.id.address_layout)
    LinearLayout addressLayout;
    @Bind(R.id.time_layout)
    LinearLayout timeLayout;
    @Bind(R.id.show_cars_layout_send)
    LinearLayout showCarsLayoutSend;
    @Bind(R.id.all_money_left_sku)
    TextView allMoneyLeftSku;
    @Bind(R.id.all_money_text_sku)
    TextView allMoneyTextSku;

    private AirPort airPortBean;//航班信息
    private PoiBean poiBean;//达到目的地
    private String serverTime;
    private String serverDate;


    @Override
    protected void initHeader() {

    }

    FragmentManager fm;
    FgCarNew fgCarNew;
    CarListBean carListBean;

    private void genBottomData(CarBean carBean) {
        int total = carBean.price;
        if(null != manLuggageBean){
            int seat1Price = OrderUtils.getSeat1PriceTotal(carListBean,manLuggageBean);
            int seat2Price = OrderUtils.getSeat2PriceTotal(carListBean,manLuggageBean);
            total += seat1Price + seat2Price;
        }
        if(checkInChecked){
            if (!TextUtils.isEmpty(carListBean.additionalServicePrice.checkInPrice)) {
                total += Integer.valueOf(carListBean.additionalServicePrice.checkInPrice);
            }
        }

        if(waitChecked) {
            if (!TextUtils.isEmpty(carListBean.additionalServicePrice.pickupSignPrice)) {
                total += Integer.valueOf(carListBean.additionalServicePrice.pickupSignPrice);
            }
        }
        allMoneyText.setText("￥ " + total);

        if(null != carListBean) {
            allJourneyText.setText("全程预估:" + carListBean.distance + "公里," + carListBean.interval + "分钟");
        }
    }


    private void initCarFragment() {
        showCarsLayoutSend.setVisibility(View.VISIBLE);
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
        bundle.putSerializable("collectGuideBean",collectGuideBean);
        bundle.putParcelable("carListBean", carListBean);
        fgCarNew.setArguments(bundle);
        transaction.add(R.id.show_cars_layout_send, fgCarNew);
        transaction.commit();
    }


    CollectGuideBean collectGuideBean;
    @Override
    protected void initView() {
        collectGuideBean = (CollectGuideBean)this.getArguments().getSerializable("collectGuideBean");
        if(null != collectGuideBean){
            initCarFragment();
        }

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    public static final String KEY_CITY_ID = "KEY_CITY_ID";
    public static final String KEY_CITY = "KEY_CITY";
    public static final String KEY_FLIGHT = "KEY_FLIGHT";
    public static final String KEY_AIRPORT = "KEY_AIRPORT";
    public static final String KEY_START = "KEY_START";
    public static final String KEY_ARRIVAL = "KEY_ARRIVAL";
    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_CAR = "KEY_CAR";
    public static final String KEY_DAILY = "KEY_DAILY";
    public static final String KEY_MASK = "KEY_MASK";
    public static final String KEY_DISTANCE = "KEY_DISTANCE";
    public static final String KEY_COM_TIME = "KEY_EXPECTED_COMP_TIME";
    public static final String KEY_URGENT_FLAG = "KEY_URGENT_FLAG";
    public static final String KEY_NEED_CHILDREN_SEAT = "KEY_NEED_CHILDREN_SEAT";
    public static final String KEY_NEED_BANNER = "KEY_NEED_BANNER";

    private CarBean carBean;//车

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
    String startLocation, termLocation;

    private void checkInput() {
        if (!TextUtils.isEmpty(timeText.getText())
                && !TextUtils.isEmpty(addressTips.getText())
                && !TextUtils.isEmpty(airTitle.getText())) {
            getData();
        }
    }

    ManLuggageBean manLuggageBean;
    boolean checkInChecked = false;
    boolean waitChecked = false;
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHECK_SWITCH:
                checkInChecked = (boolean)action.getData();
                genBottomData(carBean);
                break;
            case CHANGE_CAR:
                carBean = (CarBean) action.getData();
                genBottomData(carBean);
                break;
            case MAN_CHILD_LUUAGE:
                confirmJourney.setBackgroundColor(getContext().getResources().getColor(R.color.all_bg_yellow));
                manLuggageBean = (ManLuggageBean)action.getData();
                confirmJourney.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(UserEntity.getUser().isLogin(getActivity())) {

                            if(null != collectGuideBean) {
                                String sTime = serverDate + " " + serverTime+":00";
                                OrderUtils.checkGuideCoflict(getContext(), 1, cityId,
                                        null != collectGuideBean ? collectGuideBean.guideId : null, sTime,
                                        DateUtils.getToTime(sTime,Integer.valueOf(carListBean.estTime)),
                                        cityId + "", 0, carBean.carType, carBean.carSeat,
                                        new HttpRequestListener() {
                                            @Override
                                            public void onDataRequestSucceed(BaseRequest request) {
                                                goOrder();
                                            }

                                            @Override
                                            public void onDataRequestCancel(BaseRequest request) {

                                            }

                                            @Override
                                            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

                                            }
                                        });
                            }else{
                                goOrder();
                            }
                        }else{
                            Bundle bundle = new Bundle();//用于统计
                            bundle.putString("source", "送机下单");
                            startFragment(new FgLogin(), bundle);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private void goOrder(){
        FGOrderNew fgOrderNew = new FGOrderNew();
        Bundle bundle = new Bundle();
        bundle.putString("guideCollectId", collectGuideBean == null ? "" : collectGuideBean.guideId);
        bundle.putSerializable("collectGuideBean", collectGuideBean == null ? null : collectGuideBean);
        bundle.putString("source", source);
        carBean.expectedCompTime = carListBean.estTime;
        bundle.putParcelable("carBean", CarUtils.carBeanAdapter(carBean));
        bundle.putParcelable("carListBean", carListBean);
        bundle.putParcelable("airPortBean", airPortBean);
        bundle.putParcelable("poiBean", poiBean);
        bundle.putString("serverTime", serverTime);
        bundle.putString("serverDate", serverDate);
        bundle.putString("adultNum", manLuggageBean.mans + "");
        bundle.putString("childrenNum", manLuggageBean.childs + "");
        bundle.putString("childseatNum", manLuggageBean.childSeats + "");
        bundle.putString("luggageNum", manLuggageBean.luggages + "");
        bundle.putSerializable("carListBean", carListBean);
        bundle.putInt("type", 2);
        bundle.putString("orderType", "2");
        bundle.putBoolean("needCheckin", checkInChecked);
        bundle.putParcelable("manLuggageBean", manLuggageBean);
        fgOrderNew.setArguments(bundle);
        startFragment(fgOrderNew);
    }


    private void getData() {
        cityId = airPortBean.cityId;
        airportCode = airPortBean.airportCode;
        //出发地，到达地经纬度
        startLocation = poiBean.location;
        termLocation = airPortBean.location;
        needChildrenSeat = airPortBean.childSeatSwitch;
        needBanner = airPortBean.bannerSwitch;
        RequestCheckPriceForTransfer requestCheckPriceForTransfer = new RequestCheckPriceForTransfer(getActivity(), mBusinessType, airportCode, cityId, startLocation, termLocation, serverDate + " " + serverTime);
        requestData(requestCheckPriceForTransfer);
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

    @OnClick({R.id.address_layout, R.id.air_send_layout, R.id.time_layout,R.id.info_tips, R.id.air_title, R.id.air_detail, R.id.rl_info, R.id.address_tips, R.id.rl_address, R.id.time_text, R.id.rl_starttime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_tips:
            case R.id.air_title:
            case R.id.air_detail:
            case R.id.air_send_layout://从哪里出发
                if (airPortBean != null) {
                    FgPoiSearch fg = new FgPoiSearch();
                    Bundle bundle = new Bundle();
                    bundle.putInt(FgPoiSearch.KEY_CITY_ID, airPortBean.cityId);
                    bundle.putString(FgPoiSearch.KEY_LOCATION, airPortBean.location);
                    fg.setArguments(bundle);
                    startFragment(fg);
                } else {
                    ToastUtils.showShort("先选择机场");
                }
                break;
            case R.id.address_layout:
            case R.id.address_tips://选择机场
                startFragment(new FgChooseAirport());
                break;
//            case R.id.air_send_layout:
//                FgChooseAir fgChooseAir = new FgChooseAir();
//                startFragment(fgChooseAir);
//                break;
            case R.id.time_layout:
            case R.id.time_text://出发时间
                if (airPortBean == null) {
                    ToastUtils.showShort("先选择机场");
                    return;
                }
                showDaySelect();
                break;
            case R.id.rl_starttime:
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCheckPrice) {
            RequestCheckPrice requestCheckPrice = (RequestCheckPrice) request;
            carListBean = (CarListBean) requestCheckPrice.getData();
            if (carListBean.carList.size() > 0) {
                carBean = CarUtils.initCarListData(carListBean.carList).get(0);
                bottom.setVisibility(View.VISIBLE);
                genBottomData(carBean);
            } else {
                bottom.setVisibility(View.GONE);
            }

            initCarFragment();
        }
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseAirport.class.getSimpleName().equals(from)) {
            airPortBean = (AirPort) bundle.getSerializable(FgChooseAirport.KEY_AIRPORT);
            addressTips.setText(airPortBean.cityName + " " + airPortBean.airportName);
            poiBean = null;
            airTitle.setText("");
            airDetail.setText("");
            infoTips.setVisibility(View.VISIBLE);
            airTitle.setVisibility(View.GONE);
            airDetail.setVisibility(View.GONE);
            timeText.setText("");
            showCarsLayoutSend.setVisibility(View.GONE);
            bottom.setVisibility(View.GONE);

        } else if (FgPoiSearch.class.getSimpleName().equals(from)) {
            poiBean = (PoiBean) bundle.getSerializable("arrival");
            infoTips.setVisibility(View.GONE);
            airTitle.setVisibility(View.VISIBLE);
            airDetail.setVisibility(View.VISIBLE);
            airTitle.setText(poiBean.placeName);
            airDetail.setText(poiBean.placeDetail);
            collapseSoftInputMethod();
        }
        checkInput();
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

}
