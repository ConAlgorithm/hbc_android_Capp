package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseAirPortActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.OrderNewActivity;
import com.hugboga.custom.activity.PoiSearchActivity;
import com.hugboga.custom.adapter.CarViewpagerAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.GuideCarEventData;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForTransfer;
import com.hugboga.custom.data.request.RequestGuideConflict;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.MoneyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

import static android.view.View.GONE;
import static com.hugboga.custom.utils.CommonUtils.showToast;

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
    public TextView addressTips;
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
    MoneyTextView allMoneyText;
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

        if(null == carBean){
            return;
        }
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

        allMoneyText.setText(Tools.getRMB(getActivity())+ total);

        if(null != carListBean) {
            allJourneyText.setText("全程预估: " + carListBean.distance + "公里," + carListBean.interval + "分钟");
        }
    }


    private void initCarFragment(boolean isDataBack) {
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
        bundle.putSerializable("carListBean", carListBean);
        bundle.putBoolean("isNetError", isNetError);
        bundle.putInt("orderType",2);
        if(isDataBack && null !=carListBean) {
            String sTime = serverDate +" " + serverTime +":00";
            bundle.putInt("cityId", cityId);
            bundle.putString("startTime", sTime);
            if(TextUtils.isEmpty(carListBean.estTime)){
                bundle.putString("endTime", DateUtils.getToTime(sTime, 0));
            }else {
                bundle.putString("endTime", DateUtils.getToTime(sTime, Integer.valueOf(carListBean.estTime)));
            }
        }
        fgCarNew.setArguments(bundle);
        transaction.add(R.id.show_cars_layout_send, fgCarNew);
        transaction.commit();
    }

    private boolean checkParams(){
        if(null == manLuggageBean) {
            showToast(R.string.add_man_toast);
            return false;
        }
        return true;
    }


    CollectGuideBean collectGuideBean;
    @Override
    protected void initView() {
        collectGuideBean = (CollectGuideBean)this.getArguments().getSerializable("collectGuideBean");
        if(null != collectGuideBean){
            initCarFragment(false);
        }
        confirmJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkParams();
            }
        });
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

    //报价返回carlist 删除司导后显示使用
    public ArrayList<CarBean> carListBak;

    private void checkInput() {
        if (!TextUtils.isEmpty(timeText.getText())
                && !TextUtils.isEmpty(addressTips.getText())
                && !TextUtils.isEmpty(airTitle.getText())) {
            getData();
        }
    }

    //当前fragment 是否显示
    private boolean fragmentShow = true;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            fragmentShow = false;
        } else {
            fragmentShow = true;
        }
    }


    ManLuggageBean manLuggageBean;
    boolean checkInChecked = true;
    boolean waitChecked = true;
    int maxLuuages = 0;
    GuideCarEventData eventData;
    ArrayList<GuideCarBean> guideCars;
    String carIds = null;

    @Subscribe
    public void onEventMainThread(EventAction action) {
        if(fragmentShow) {
            switch (action.getType()) {
                case CARIDS:
                    eventData = (GuideCarEventData) action.getData();
                    carIds = eventData.carIds;
                    guideCars = eventData.guideCars;
                    break;
                case AIR_PORT_BACK:
                    airPortBean = (AirPort) action.getData();
                    String airPortName = airPortBean.cityName + " " + airPortBean.airportName;
                    if (airPortName != null && airPortName.equals(addressTips.getText())) {
                        return;
                    }
                    showCarsLayoutSend.setVisibility(View.GONE);
                    addressTips.setText(airPortBean.cityName + " " + airPortBean.airportName);
                    poiBean = null;
                    airTitle.setText("");
                    airDetail.setText("");
                    infoTips.setVisibility(View.VISIBLE);
                    airTitle.setVisibility(View.GONE);
                    airDetail.setVisibility(View.GONE);
                    timeText.setText("");
                    bottom.setVisibility(View.GONE);
                    checkInput();
                    break;
                case CHOOSE_POI_BACK:
                    poiBean = (PoiBean) action.getData();
                    infoTips.setVisibility(View.GONE);
                    airTitle.setVisibility(View.VISIBLE);
                    airDetail.setVisibility(View.VISIBLE);
                    airTitle.setText(poiBean.placeName);
                    airDetail.setText(poiBean.placeDetail);
                    collapseSoftInputMethod();
                    checkInput();
                    break;
                case MAX_LUGGAGE_NUM:
                    maxLuuages = (int) action.getData();
                    break;
                case CAR_CHANGE_SMALL:
                    manLuggageBean = null;
                    break;
                case CHANGE_GUIDE:
                    collectGuideBean = (CollectGuideBean) action.getData();
                    break;
                case GUIDE_DEL:
                    collectGuideBean = null;
                    manLuggageBean = null;
                    carListBean.carList = carListBak;
                    if (null == carListBean) {
                        showCarsLayoutSend.setVisibility(GONE);
                    } else {
                        if (null != carListBean.carList && carListBean.carList.size() > 0) {
                            bottom.setVisibility(View.VISIBLE);
                            carBean = carListBean.carList.get(0);
                            genBottomData(carBean);
                        }
                        initCarFragment(true);
                    }
                    break;
                case CHECK_SWITCH:
                    checkInChecked = (boolean) action.getData();
                    if (null != carBean) {
                        genBottomData(carBean);
                    }
                    break;
                case CHANGE_CAR:
                    carBean = (CarBean) action.getData();
                    if (null != carBean) {
                        genBottomData(carBean);
                    }
                    break;
                case MAN_CHILD_LUUAGE:
                    confirmJourney.setBackgroundColor(getContext().getResources().getColor(R.color.all_bg_yellow));
                    manLuggageBean = (ManLuggageBean) action.getData();
                    if (null != carBean) {
                        genBottomData(carBean);
                    }
                    genBottomData(carBean);
                    confirmJourney.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (checkParams()) {
                                if (UserEntity.getUser().isLogin(getActivity())) {

                                    if (null != collectGuideBean) {


                                        if ((carBean.carType == 1 && carBean.capOfPerson == 4
                                                && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 4)
                                                || (carBean.carType == 1 && carBean.capOfPerson == 6 && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 6)) {
                                            AlertDialogUtils.showAlertDialog(getActivity(), getString(R.string.alert_car_full),
                                                    "继续下单", "更换车型", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            checkGuide();
                                                            dialog.dismiss();
                                                        }
                                                    }, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                        } else {
                                            checkGuide();
                                        }

                                    } else {
                                        if ((carBean.carType == 1 && carBean.capOfPerson == 4
                                                && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 4)
                                                || (carBean.carType == 1 && carBean.capOfPerson == 6 && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 6)) {
                                            AlertDialogUtils.showAlertDialog(getActivity(), getString(R.string.alert_car_full),
                                                    "继续下单", "更换车型", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            goOrder();
                                                            dialog.dismiss();
                                                        }
                                                    }, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                        } else {
                                            goOrder();
                                        }
                                    }
                                } else {
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.putExtra("source", getEventSource());
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_S;
    }

    @Override
    public String getEventSource() {
        return "送机订单";
    }

    @Override
    public Map getEventMap() {
        return super.getEventMap();
    }

    private void checkGuide(){
        String sTime = serverDate + " " + serverTime+":00";
        OrderUtils.checkGuideCoflict(getContext(), 1, cityId,
                null != collectGuideBean ? collectGuideBean.guideId : null, sTime,
                DateUtils.getToTime(sTime,Integer.valueOf(carListBean.estTime)),
                cityId + "", 0, carBean.carType, carBean.carSeat,
                new HttpRequestListener() {
                    @Override
                    public void onDataRequestSucceed(BaseRequest request) {
                        ApiReportHelper.getInstance().addReport(request);
                        RequestGuideConflict requestGuideConflict = (RequestGuideConflict)request;
                        List<String> list = requestGuideConflict.getData();
                        if(list.size() > 0) {
                            goOrder();
                        }else{
                            EventBus.getDefault().post(new EventAction(EventType.GUIDE_ERROR_TIME));
                        }
                    }

                    @Override
                    public void onDataRequestCancel(BaseRequest request) {

                    }

                    @Override
                    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

                    }
                });
    }

    private void goOrder(){
//        FGOrderNew fgOrderNew = new FGOrderNew();
        Bundle bundle = new Bundle();
        bundle.putString("guideCollectId", collectGuideBean == null ? "" : collectGuideBean.guideId);
        bundle.putSerializable("collectGuideBean", collectGuideBean == null ? null : collectGuideBean);
        bundle.putString("source", source);
        carBean.expectedCompTime = carListBean.estTime;
        bundle.putSerializable("carBean", carBean);
        bundle.putSerializable("carListBean", carListBean);
        bundle.putSerializable("airPortBean", airPortBean);
        bundle.putSerializable("poiBean", poiBean);
        bundle.putString("serverTime", serverTime);
        bundle.putString("serverDate", serverDate);
        bundle.putString("adultNum", manLuggageBean.mans + "");
        bundle.putString("childrenNum", manLuggageBean.childs + "");
        bundle.putString("childseatNum", manLuggageBean.childSeats + "");
        bundle.putString("luggageNum", maxLuuages+"");//manLuggageBean.luggages + "");
        bundle.putSerializable("carListBean", carListBean);
        bundle.putInt("type", 2);
        bundle.putString("orderType", "2");
        bundle.putBoolean("needCheckin", checkInChecked);
        bundle.putSerializable("manLuggageBean", manLuggageBean);
        StatisticClickEvent.sendClick(StatisticConstant.CONFIRM_S,source,carBean.desc+"",checkInChecked,(manLuggageBean.mans + manLuggageBean.childs));
        Intent intent = new Intent(getActivity(),OrderNewActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE,source);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void getData() {
        cityId = airPortBean.cityId;
        airportCode = airPortBean.airportCode;
        //出发地，到达地经纬度
        startLocation = poiBean.location;
        termLocation = airPortBean.location;
        needChildrenSeat = airPortBean.childSeatSwitch;
        needBanner = airPortBean.bannerSwitch;

        RequestCheckPriceForTransfer requestCheckPriceForTransfer = new RequestCheckPriceForTransfer(getActivity(), mBusinessType,
                airportCode, cityId, startLocation, termLocation, serverDate + " " + serverTime,carIds);
        requestData(requestCheckPriceForTransfer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        source = getArguments().getString("source");
        Map map = new HashMap();
        map.put("source",source);
        MobClickUtils.onEvent(StatisticConstant.LAUNCH_J,map);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    Intent intent;
    @OnClick({R.id.address_layout, R.id.air_send_layout, R.id.time_layout,R.id.info_tips, R.id.air_title, R.id.air_detail, R.id.rl_info, R.id.address_tips, R.id.rl_address, R.id.time_text, R.id.rl_starttime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_tips:
            case R.id.air_title:
            case R.id.air_detail:
            case R.id.air_send_layout://从哪里出发
                if (airPortBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(PoiSearchActivity.KEY_CITY_ID, airPortBean.cityId);
                    bundle.putString(PoiSearchActivity.KEY_LOCATION, airPortBean.location);
                    intent = new Intent(getActivity(), PoiSearchActivity.class);
                    intent.putExtras(bundle);
                    intent.putExtra("mBusinessType",2);
                    getActivity().startActivity(intent);

                } else {
                    showToast("先选择机场");
                }
                break;
            case R.id.address_layout:
            case R.id.address_tips://选择机场
                Intent intent = new Intent(getActivity(),ChooseAirPortActivity.class);
                getActivity().startActivity(intent);

                break;
            case R.id.time_layout:
            case R.id.time_text://出发时间
                if (airPortBean == null) {
                    showToast("先选择机场");
                    return;
                }
                showYearMonthDayTimePicker();
                break;
            case R.id.rl_starttime:
                break;
        }
    }

    DateTimePicker picker;
    public void showYearMonthDayTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        picker = new DateTimePicker(getActivity(), DateTimePicker.HOUR_OF_DAY);
        picker.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR)+1);
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                String tmpDate = year + "-" + month + "-" + day;
                String startDate = calendar.get(Calendar.YEAR) +"-"+ (calendar.get(Calendar.MONTH) +1)+"-"+ calendar.get(Calendar.DAY_OF_MONTH);

                if(DateUtils.getDateByStr(tmpDate).before(DateUtils.getDateByStr(startDate))){
                    CommonUtils.showToast("不能选择今天之前的时间");
                    return;
                }

                if(DateUtils.getDistanceDays(startDate,tmpDate)>180){
                    CommonUtils.showToast(R.string.time_out_180);
                }else {
                    serverDate = year + "-" + month + "-" + day;
                    serverTime = hour + ":" + minute;
                    timeText.setText(serverDate + " " + serverTime);
                    checkInput();
                    picker.dismiss();
                }
            }
        });
        picker.show();
    }

    boolean isNetError = false;
    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        bottom.setVisibility(View.GONE);
        carListBean = null;
        isNetError = true;
        if (null != collectGuideBean) {
            initCarFragment(false);
        }else{
            initCarFragment(true);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCheckPrice) {
            bottom.setVisibility(View.GONE);
            isNetError = false;
            manLuggageBean = null;
            RequestCheckPrice requestCheckPrice = (RequestCheckPrice) request;
            carListBean = (CarListBean) requestCheckPrice.getData();
            if (carListBean.carList.size() > 0) {
                if(null != collectGuideBean) {
                    carListBak = (ArrayList<CarBean>)carListBean.carList.clone();
                    carListBean.carList = CarUtils.getSingleCarBeanList(carListBean.carList, eventData.guideCars);
                }
                if (null == collectGuideBean) {
                    carBean = CarUtils.initCarListData(carListBean.carList).get(0);
                } else {
                    carBean = carListBean.carList.get(0);
                }

                if(null != carBean) {
                    genBottomData(carBean);
                    bottom.setVisibility(View.VISIBLE);
                }else{
                    bottom.setVisibility(View.GONE);
                    showToast(R.string.no_price_error);
                }
            } else {
                bottom.setVisibility(View.GONE);
            }
            initCarFragment(true);

        }
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

}
