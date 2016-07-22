package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
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
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.DailyBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForPickup;
import com.hugboga.custom.data.request.RequestGuideConflict;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.widget.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    TextView airTitle;
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
    TextView allMoneyText;
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
        bundle.putParcelable("carListBean", carListBean);
        bundle.putBoolean("isNetError", isNetError);
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
        cityId = flightBean.arrivalAirport.cityId;
        airportCode = flightBean.arrivalAirport.airportCode;
        //出发地，到达地经纬度
        startLocation = flightBean.arrivalAirport.location;
        termLocation = poiBean.location;
        serverDate = flightBean.arrDate + " " + flightBean.arrivalTime;
        needChildrenSeat = flightBean.arrivalAirport.childSeatSwitch;
        needBanner = flightBean.arrivalAirport.bannerSwitch;
        RequestCheckPriceForPickup requestCheckPriceForPickup = new RequestCheckPriceForPickup(getActivity(), mBusinessType, airportCode, cityId, startLocation, termLocation, serverDate);
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
            if (!TextUtils.isEmpty(carListBean.additionalServicePrice.pickupSignPrice)) {
                total += Integer.valueOf(carListBean.additionalServicePrice.pickupSignPrice);
            }
        }
        allMoneyText.setText("￥" + total);

        if(null != carListBean) {
            allJourneyText.setText("全程预估: " + carListBean.distance + "公里," + carListBean.interval + "分钟");
        }
    }

    @Override
    protected void inflateContent() {

    }


    ManLuggageBean manLuggageBean;
    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CAR_CHANGE_SMALL:
                confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
                confirmJourney.setOnClickListener(null);
                break;
            case CHANGE_GUIDE:
                collectGuideBean = (CollectGuideBean)action.getData();
                break;

            case GUIDE_DEL:
                collectGuideBean = null;
                confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
                confirmJourney.setOnClickListener(null);
                initCarFragment(true);
                break;
            case CHECK_SWITCH:
                checkInChecked = (boolean)action.getData();
                if(null != carBean) {
                    genBottomData(carBean);
                }
                break;

            case WAIT_SWITCH:
                checkInChecked = (boolean)action.getData();
                if(null != carBean) {
                    genBottomData(carBean);
                }
                break;

            case AIR_NO:
                flightBean = (FlightBean) action.getData();
                if (mBusinessType == Constants.BUSINESS_TYPE_SEND && flightBean != null) {
                } else {
                    String flightInfoStr = flightBean.flightNo + " ";
                    flightInfoStr += flightBean.depAirport.cityName + "-" + flightBean.arrivalAirport.cityName;
                    flightInfoStr += "\n当地时间" + flightBean.arrDate + " " + flightBean.depTime + " 降落";
                    infoTips.setVisibility(View.GONE);
                    airTitle.setVisibility(View.VISIBLE);
                    airDetail.setVisibility(View.VISIBLE);
                    airTitle.setText(flightBean.arrAirportName);
                    airDetail.setText(flightInfoStr);

                    poiBean = null;
                    addressTips.setVisibility(View.VISIBLE);
                    addressTitle.setVisibility(View.GONE);
                    addressDetail.setVisibility(View.GONE);

                    bottom.setVisibility(View.GONE);
//                    show_cars_layout_pick.setVisibility(View.GONE);

                }
                break;
            case CHANGE_CAR:
                carBean = (CarBean) action.getData();
                if(null != carBean) {
                    genBottomData(carBean);
                }
                break;
            case MAN_CHILD_LUUAGE:
                confirmJourney.setBackgroundColor(getContext().getResources().getColor(R.color.all_bg_yellow));
                manLuggageBean = (ManLuggageBean)action.getData();
                if(null != carBean) {
                    genBottomData(carBean);
                }
                genBottomData(carBean);
                confirmJourney.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(UserEntity.getUser().isLogin(getActivity())) {
                            if(null != collectGuideBean) {
                                if((carBean.carType == 1 && carBean.capOfPerson == 4
                                        && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 4)
                                        || (carBean.carType == 1 && carBean.capOfPerson == 6 && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 6)){
                                    AlertDialogUtils.showAlertDialog(getActivity(),getString(R.string.alert_car_full),
                                            "继续下单","更换车型",new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    checkGuide();
                                                    dialog.dismiss();
                                                }
                                            },new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                }else{
                                    checkGuide();
                                }

                            }else{
                                if(carBean.carType == 1 && carBean.capOfPerson == 4 && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 4
                                        || carBean.capOfPerson == 6 && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 6){
                                    AlertDialogUtils.showAlertDialog(getActivity(),getString(R.string.alert_car_full),
                                            "继续下单","更换车型",new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    goOrder();
                                                    dialog.dismiss();
                                                }
                                            },new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                }else{
                                    goOrder();
                                }
                            }
                        }else{
                            Bundle bundle = new Bundle();//用于统计
                            bundle.putString("source", "接机下单");
                            startFragment(new FgLogin(), bundle);
                        }
                    }
                });
                break;
            default:
                break;
        }
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
        FGOrderNew fgOrderNew = new FGOrderNew();
        Bundle bundle = new Bundle();
        bundle.putString("guideCollectId", collectGuideBean == null ? "" : collectGuideBean.guideId);
        bundle.putSerializable("collectGuideBean", collectGuideBean == null ? null : collectGuideBean);
        bundle.putString("source", source);
        bundle.putSerializable(FgCar.KEY_FLIGHT, flightBean);
        bundle.putSerializable(FgCar.KEY_ARRIVAL, poiBean);
        bundle.putString("serverTime", flightBean.arrivalTime);
        bundle.putParcelable("carListBean", carListBean);
        bundle.putString("price", carBean.price + "");
        bundle.putString("distance", carListBean.distance + "");
        carBean.expectedCompTime = carListBean.estTime;
        bundle.putParcelable("carBean", CarUtils.carBeanAdapter(carBean));
        bundle.putInt("type", 1);
        bundle.putString("orderType", "1");
        bundle.putParcelable("manLuggageBean", manLuggageBean);
        bundle.putString("adultNum", manLuggageBean.mans + "");
        bundle.putString("childrenNum", manLuggageBean.childs + "");
        bundle.putString("childseatNum", manLuggageBean.childSeats + "");
        bundle.putString("luggageNum", manLuggageBean.luggages + "");
        bundle.putParcelable("carListBean", carListBean);

        bundle.putBoolean("needCheckin", checkInChecked);

        fgOrderNew.setArguments(bundle);
        startFragment(fgOrderNew);
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
        bottom.setVisibility(View.GONE);
        carListBean = null;
        isNetError = true;
        confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
        confirmJourney.setOnClickListener(null);
        if (null != collectGuideBean) {
            initCarFragment(false);
        }else{
            initCarFragment(true);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCheckPrice) {
            bottom.setVisibility(View.GONE);
            isNetError = false;
            confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
            confirmJourney.setOnClickListener(null);
            RequestCheckPrice requestCheckPrice = (RequestCheckPrice) request;
            carListBean = (CarListBean) requestCheckPrice.getData();
            if (carListBean.carList.size() > 0) {
                if(null == collectGuideBean) {
                    carBean = CarUtils.initCarListData(carListBean.carList).get(0);//carListBean.carList.get(0);
                }else {
                    carBean = CarUtils.isMatchLocal(CarUtils.getNewCarBean(collectGuideBean), carListBean.carList);
                }
                if(null != carBean) {
                    bottom.setVisibility(View.VISIBLE);
                    confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
                    confirmJourney.setOnClickListener(null);
                    genBottomData(carBean);
                }else{
                    bottom.setVisibility(View.GONE);
                }
            } else {
                bottom.setVisibility(View.GONE);
            }
            initCarFragment(true);

        }
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgPoiSearch.class.getSimpleName().equals(from)) {
            poiBean = (PoiBean) bundle.getSerializable("arrival");
            addressTips.setVisibility(View.GONE);
            addressTitle.setVisibility(View.VISIBLE);
            addressDetail.setVisibility(View.VISIBLE);
            addressTitle.setText(poiBean.placeName);
            addressDetail.setText(poiBean.placeDetail);
            collapseSoftInputMethod();
            getData();

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

    @OnClick({R.id.info_tips, R.id.air_title, R.id.air_detail, R.id.rl_info, R.id.address_tips, R.id.address_title, R.id.address_detail, R.id.rl_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_tips:
            case R.id.air_title:
            case R.id.air_detail:
            case R.id.rl_info:
                FgChooseAir fgChooseAir = new FgChooseAir();
                startFragment(fgChooseAir);
                break;
            case R.id.address_tips:
            case R.id.address_title:
            case R.id.address_detail:
            case R.id.rl_address:
                if (airDetail.isShown()) {
                    FgPoiSearch fg = new FgPoiSearch();
                    Bundle bundle = new Bundle();
                    bundle.putString("source", "下单过程中");
                    bundle.putInt(FgPoiSearch.KEY_CITY_ID, flightBean.arrivalAirport.cityId);
                    bundle.putString(FgPoiSearch.KEY_LOCATION, flightBean.arrivalAirport.location);
                    startFragment(fg, bundle);
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
