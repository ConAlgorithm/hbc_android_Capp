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
import com.hugboga.custom.activity.ChooseAirActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.OrderNewActivity;
import com.hugboga.custom.activity.PoiSearchActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.DailyBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.GuideCarEventData;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForPickup;
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
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * Created  on 16/5/13.
 */

@ContentView(R.layout.fg_picknew)
public class FgPickNew extends BaseFragment implements View.OnTouchListener{
    @Bind(R.id.info_left)
    TextView infoLeft;
    @Bind(R.id.info_tips)
    TextView infoTips;
    @Bind(R.id.air_title)
    public TextView airTitle;
    @Bind(R.id.air_detail)
    TextView airDetail;
    @Bind(R.id.rl_info)
    LinearLayout rlInfo;
    @Bind(R.id.address_left)
    TextView addressLeft;
    @Bind(R.id.address_tips)
    TextView addressTips;
    @Bind(R.id.address_title)
    TextView addressTitle;
    @Bind(R.id.address_detail)
    TextView addressDetail;
    @Bind(R.id.rl_address)
    LinearLayout rlAddress;

    @Bind(R.id.show_cars_layout_pick)
    LinearLayout show_cars_layout_pick;


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


    private FlightBean flightBean;//航班信息 接机
    private AirPort airPortBean;//机场信息 送机
    private DailyBean dailyBean;// 日租
    private CityBean cityBean;//起始目的地 次租
    private PoiBean startBean;//起始目的地 次租
    private PoiBean poiBean;//达到目的地

    private CarBean carBean;//车
    private String serverDate;


    private int cityId;
    private String airportCode;
    private int urgentFlag;//是否急单，1是，0非
    private boolean needChildrenSeat = false;//是否需要儿童座椅
    private boolean needBanner = true;//是否可以展示接机牌
    private DialogUtil mDialogUtil;

    @Override
    protected void initHeader() {

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

    FragmentManager fm;
    FgCarNew fgCarNew;
    FragmentTransaction transaction;
    private void initCarFragment(boolean isDataBack) {
        show_cars_layout_pick.setVisibility(View.VISIBLE);
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        if(null != fgCarNew) {
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
        bundle.putInt("orderType",1);
        if(isDataBack && null !=carListBean) {
            String sTime = serverDate +":00";
            bundle.putInt("cityId", cityId);
            bundle.putString("startTime", sTime);
            if(TextUtils.isEmpty(carListBean.estTime)){
                bundle.putString("endTime", DateUtils.getToTime(sTime, 0));
            }else {
                bundle.putString("endTime", DateUtils.getToTime(sTime, Integer.valueOf(carListBean.estTime)));
            }
        }
        bundle.putString(Constants.PARAMS_SOURCE, getEventSource());

        fgCarNew.setArguments(bundle);
        transaction.add(R.id.show_cars_layout_pick, fgCarNew);
        transaction.commit();
    }


    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    String startLocation, termLocation;

    protected void getData() {
        cityId = flightBean.arrCityId;//.arrivalAirport.cityId;
        airportCode = flightBean.arrAirportCode;//.arrivalAirport.airportCode;
        //出发地，到达地经纬度
        startLocation = flightBean.arrLocation;//.arrivalAirport.location;
        termLocation = poiBean.location;
        serverDate = flightBean.arrDate + " " + flightBean.arrivalTime;
        needChildrenSeat = flightBean.arrivalAirport.childSeatSwitch;
        needBanner = flightBean.arrivalAirport.bannerSwitch;

        RequestCheckPriceForPickup requestCheckPriceForPickup = new RequestCheckPriceForPickup(getActivity(), 1, airportCode, cityId, startLocation, termLocation, serverDate,carIds);
        requestData(requestCheckPriceForPickup);
    }

    CarListBean carListBean;

    boolean checkInChecked = true;
    boolean waitChecked = false;

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

        if(checkInChecked) {
            if (!TextUtils.isEmpty(carListBean.additionalServicePrice.pickupSignPrice) && null != carListBean.additionalServicePrice) {
                total += Integer.valueOf(carListBean.additionalServicePrice.pickupSignPrice);
            }
        }
        allMoneyText.setText(Tools.getRMB(this.getActivity()) + total);

        if(null != carListBean) {
            allJourneyText.setText("全程预估: " + carListBean.distance + "公里," + carListBean.interval + "分钟");
        }
    }

    @Override
    protected void inflateContent() {

    }

    private boolean checkParams(){
        if(null == manLuggageBean) {
            CommonUtils.showToast(R.string.add_man_toast);
            return false;
        }
        return true;
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
    int maxLuuages = 0;
    GuideCarEventData eventData;
    ArrayList<GuideCarBean> guideCars;
    String carIds = null;
    //报价返回carlist 删除司导后显示使用
    public ArrayList<CarBean> carListBak;

    @Subscribe
    public void onEventMainThread(EventAction action) {
        if(fragmentShow) {
            switch (action.getType()) {
                case CARIDS:
                    eventData = (GuideCarEventData) action.getData();
                    carIds = eventData.carIds;
                    guideCars = eventData.guideCars;
                    break;
                case CHOOSE_POI_BACK:
                    poiBean = (PoiBean) action.getData();
                    addressTips.setVisibility(GONE);
                    addressTitle.setVisibility(View.VISIBLE);
                    addressDetail.setVisibility(View.VISIBLE);
                    addressTitle.setText(poiBean.placeName);
                    addressDetail.setText(poiBean.placeDetail);
                    collapseSoftInputMethod();
                    getData();
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
                        show_cars_layout_pick.setVisibility(GONE);
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

                case WAIT_SWITCH:
                    checkInChecked = (boolean) action.getData();
                    if (null != carBean) {
                        genBottomData(carBean);
                    }
                    break;

                case AIR_NO:
                    flightBean = (FlightBean) action.getData();
                    if (mBusinessType == Constants.BUSINESS_TYPE_SEND && flightBean != null) {
                    } else {
                        String flightInfoStr = flightBean.flightNo + " ";
                        flightInfoStr += flightBean.depAirportName + "-" + flightBean.arrAirportName;
                        flightInfoStr += "\n当地时间" + flightBean.arrDate + " " + flightBean.arrivalTime + " 降落";
                        infoTips.setVisibility(GONE);
                        airTitle.setVisibility(View.VISIBLE);
                        airDetail.setVisibility(View.VISIBLE);
                        airTitle.setText(flightBean.arrAirportName);
                        airDetail.setText(flightInfoStr);

                        poiBean = null;
                        addressTips.setVisibility(View.VISIBLE);
                        addressTitle.setVisibility(GONE);
                        addressDetail.setVisibility(GONE);

                        bottom.setVisibility(GONE);
                        show_cars_layout_pick.setVisibility(View.GONE);

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
//                genBottomData(carBean);
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
                                        if (carBean.carType == 1 && carBean.capOfPerson == 4 && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 4
                                                || carBean.capOfPerson == 6 && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 6) {
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
        return StatisticConstant.LAUNCH_J;
    }

    @Override
    public String getEventSource() {
        return "接机订单";
    }

    @Override
    public Map getEventMap() {
        return super.getEventMap();
    }

    private void checkGuide(){
        String sTime = serverDate+":00";
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
        Bundle bundle = new Bundle();
        bundle.putString("guideCollectId", collectGuideBean == null ? "" : collectGuideBean.guideId);
        bundle.putSerializable("collectGuideBean", collectGuideBean == null ? null : collectGuideBean);
        bundle.putString("source", source);
        bundle.putSerializable(KEY_FLIGHT, flightBean);
        bundle.putSerializable(KEY_ARRIVAL, poiBean);
        bundle.putString("serverTime", flightBean.arrivalTime);
        bundle.putSerializable("carListBean", carListBean);
        bundle.putString("price", carBean.price + "");
        bundle.putString("distance", carListBean.distance + "");
        carBean.expectedCompTime = carListBean.estTime;
        bundle.putSerializable("carBean", carBean);
        bundle.putInt("type", 1);
        bundle.putString("orderType", "1");
        bundle.putSerializable("manLuggageBean", manLuggageBean);
        bundle.putString("adultNum", manLuggageBean.mans + "");
        bundle.putString("childrenNum", manLuggageBean.childs + "");
        bundle.putString("childseatNum", manLuggageBean.childSeats + "");
        bundle.putString("luggageNum", maxLuuages+"");//manLuggageBean.luggages + "");
        bundle.putSerializable("carListBean", carListBean);

        bundle.putBoolean("needCheckin", checkInChecked);

        Intent intent = new Intent(getActivity(),OrderNewActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE,source);
        intent.putExtras(bundle);
        startActivity(intent);

        StatisticClickEvent.pickClick(StatisticConstant.CONFIRM_J,source,carBean.desc+"",checkInChecked,(manLuggageBean.mans + manLuggageBean.childs));
        setSensorsConfirmEvent();
    }

    @Override
    public int getBusinessType() {
        mBusinessType = Constants.BUSINESS_TYPE_PICK;
        setGoodsType(mBusinessType);
        return mBusinessType;
    }

    boolean isNetError = false;
    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        bottom.setVisibility(GONE);
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
            bottom.setVisibility(GONE);
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
                    bottom.setVisibility(View.VISIBLE);
                    genBottomData(carBean);
                }else{
                    bottom.setVisibility(GONE);
                }
            } else {
                bottom.setVisibility(GONE);
            }
            initCarFragment(true);

        }
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
        setSensorsEvent();
        return rootView;
    }

    //神策统计_初始页浏览
    private void setSensorsEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", "接机");
            properties.put("hbc_refer", source);
            SensorsDataAPI.sharedInstance(getActivity()).track("buy_view", properties);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //神策统计_确认行程
    private void setSensorsConfirmEvent() {
        try {
            int total = carBean.price;
            if(null != manLuggageBean){
                int seat1Price = OrderUtils.getSeat1PriceTotal(carListBean,manLuggageBean);
                int seat2Price = OrderUtils.getSeat2PriceTotal(carListBean,manLuggageBean);
                total += seat1Price + seat2Price;
            }

            if(checkInChecked) {
                if (!TextUtils.isEmpty(carListBean.additionalServicePrice.pickupSignPrice) && null != carListBean.additionalServicePrice) {
                    total += Integer.valueOf(carListBean.additionalServicePrice.pickupSignPrice);
                }
            }
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", "接机");
            properties.put("hbc_is_appoint_guide", null != collectGuideBean ? true : false);// 指定司导下单
            properties.put("hbc_adultNum", manLuggageBean.mans);// 出行成人数
            properties.put("hbc_childNum", manLuggageBean.childs);// 出行儿童数
            properties.put("hbc_childseatNum", manLuggageBean.childSeats);// 儿童座椅数
            properties.put("hbc_car_type", carBean.desc);//车型选择
            properties.put("hbc_price_total", total);//费用总计
            properties.put("hbc_distance", carListBean.distance);// 全程公里数
            properties.put("hbc_flight_no", flightBean.flightNo);// 航班
            properties.put("hbc_airport", flightBean.arrAirportName);// 机场
            properties.put("hbc_geton_time", flightBean.arrDate + " " + flightBean.arrivalTime);// 出发时间
            properties.put("hbc_dest_location", poiBean.placeName);// 送达地
            properties.put("hbc_is_pickUp", checkInChecked);// 是否接机举牌等待
            SensorsDataAPI.sharedInstance(getActivity()).track("buy_confirm", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    Intent intent;
    @OnClick({R.id.info_tips, R.id.air_title, R.id.air_detail, R.id.rl_info, R.id.address_tips, R.id.address_title, R.id.address_detail, R.id.rl_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_tips:
            case R.id.air_title:
            case R.id.air_detail:
            case R.id.rl_info:
                intent = new Intent(getActivity(),ChooseAirActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.address_tips:
            case R.id.address_title:
            case R.id.address_detail:
            case R.id.rl_address:
                if (airDetail.isShown()) {

                    Bundle bundle = new Bundle();
                    bundle.putString("source", "下单过程中");
                    bundle.putInt(PoiSearchActivity.KEY_CITY_ID, flightBean.arrCityId);//.arrivalAirport.cityId);
                    bundle.putString(PoiSearchActivity.KEY_LOCATION, flightBean.arrLocation);//.arrivalAirport.location);
                    intent = new Intent(getActivity(),PoiSearchActivity.class);
                    intent.putExtras(bundle);
                    intent.putExtra("mBusinessType",1);
                    getActivity().startActivity(intent);
                } else {
                    CommonUtils.showToast("请先选择航班");
                }
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }


}
