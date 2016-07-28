package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionErrorCode;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.ServerException;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CarViewpagerAdapter;
import com.hugboga.custom.constants.CarTypeEnum;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.DailyBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForDaily;
import com.hugboga.custom.data.request.RequestCheckPriceForPickup;
import com.hugboga.custom.data.request.RequestCheckPriceForSingle;
import com.hugboga.custom.data.request.RequestCheckPriceForTransfer;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.JazzyViewPager;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by admin on 2015/7/17.
 */
@ContentView(R.layout.fg_car)
public class FgCar extends BaseFragment implements ViewPager.OnPageChangeListener {


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


    private JazzyViewPager mJazzy;

    @ViewInject(R.id.fg_car_tip)
    private TextView carTip;//泰国提示
    @ViewInject(R.id.fg_car_name)
    private TextView carInfoText;//乘坐%d人 | 行李%d件
    @ViewInject(R.id.fg_car_intro)
    private TextView carInfoIntro;//此车型包括
    @ViewInject(R.id.car_trip_layout)
    private View carTripLayout;
    @ViewInject(R.id.car_trip_title)
    private TextView carTripTitle;//行程标题
    @ViewInject(R.id.car_trip_value)
    private TextView carTripValue;//公里
    @ViewInject(R.id.car_time_value)
    private TextView carTimeValue;//时间
    @ViewInject(R.id.car_time_title)
    private TextView carTimeTitle;//时间标题
    @ViewInject(R.id.car_total_value)
    private TextView carTotalValue;//总金额
    @ViewInject(R.id.bottom_bar_btn)
    private TextView bottomBtn;//按钮

    @ViewInject(R.id.viewpager_layout)
    private View carViewpagerLayout;//选车viewpager
    @ViewInject(R.id.car_split_layout)
    private View carSplitLayout;//分割线
    @ViewInject(R.id.car_info_layout)
    private View carInfoLayout;//车信息
    @ViewInject(R.id.car_empty_layout)
    private View carEmptyLayout;//车信息empty

    @ViewInject(R.id.car_mask)
    private View carMask;//引导层

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
        fgRightBtn.setVisibility(View.VISIBLE);
        setProgressState(1);
        fgTitle.setText(getString(Constants.TitleMap.get(mGoodsType)));
        mDialogUtil = DialogUtil.getInstance(getActivity());
        if(getArguments() != null){
            source = getArguments().getString("source","");
        }
    }

    @Override
    protected void initView() {
        initView(getView());
    }

//    @Override
//    protected Callback.Cancelable requestData() {
//        return null;
//    }

    private void initView(View view) {
        View flGalleryContainer = view.findViewById(R.id.viewpager_layout);
        flGalleryContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mJazzy.dispatchTouchEvent(event);
            }
        });
        mJazzy = (JazzyViewPager) view.findViewById(R.id.jazzy_pager);
        mJazzy.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);
        mAdapter = new CarViewpagerAdapter(getActivity(), mJazzy);
        initListData();
        mAdapter.setList(carList);
        mJazzy.setAdapter(mAdapter);
        mJazzy.setOffscreenPageLimit(5);
        mJazzy.addOnPageChangeListener(this);
    }

    private void initListData() {
        int id = 1;
        CarBean bean;
        carList = new ArrayList<CarBean>(16);
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                bean = new CarBean();
                bean.id = id;
                bean.carType = i;
                bean.carSeat = Constants.CarSeatMap.get(j);
                bean.originalPrice = 0;
                bean.models = Constants.CarDescInfoMap.get(i).get(j);
                CarTypeEnum carTypeEnum = CarTypeEnum.getCarType(bean.carType, bean.carSeat);
                if (carTypeEnum != null) {
                    bean.imgRes = carTypeEnum.imgRes;
                }
                carList.add(bean);
                id++;
            }
        }
    }

    private void sortListDataImage() {
        for (CarBean bean : carList) {
            CarTypeEnum carTypeEnum = CarTypeEnum.getCarType(bean.carType, bean.carSeat);
            if (carTypeEnum != null) {
                bean.imgRes = carTypeEnum.imgRes;
            }
        }
    }
//    @Override
//    protected String fragmentTitle() {
//        rightText.setVisibility(View.VISIBLE);
//        setProgressState(1);
//        return getString(Constants.TitleMap.get(mGoodsType));
//    }

    @Override
    protected Callback.Cancelable requestData() {
        initMaskView();
        Bundle bundle = getArguments();
//        bottomBtn.setBackgroundColor(getResources().getColor(Constants.BgColors.get(mBusinessType)));
        if (bundle != null) {
            flightBean = (FlightBean) bundle.getSerializable(KEY_FLIGHT);
            airPortBean = (AirPort) bundle.getSerializable(KEY_AIRPORT);
            startBean = (PoiBean) bundle.getSerializable(KEY_START);
            cityBean = (CityBean) bundle.getSerializable(KEY_CITY);
            poiBean = (PoiBean) bundle.getSerializable(KEY_ARRIVAL);
            dailyBean = (DailyBean) bundle.getSerializable(KEY_DAILY);
            String startLocation, termLocation;
            switch (mBusinessType) {
                case Constants.BUSINESS_TYPE_PICK:
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
                    break;
                case Constants.BUSINESS_TYPE_SEND:
                    cityId = airPortBean.cityId;
                    airportCode = airPortBean.airportCode;
                    //出发地，到达地经纬度
                    startLocation = poiBean.location;
                    termLocation = airPortBean.location;
                    serverDate = bundle.getString(KEY_TIME);
                    needChildrenSeat = airPortBean.childSeatSwitch;
                    needBanner = airPortBean.bannerSwitch;
                    RequestCheckPriceForTransfer requestCheckPriceForTransfer = new RequestCheckPriceForTransfer(getActivity(), mBusinessType, airportCode, cityId, startLocation, termLocation, serverDate);
                    requestData(requestCheckPriceForTransfer);
                    break;
                case Constants.BUSINESS_TYPE_DAILY:
                    needChildrenSeat = dailyBean.childSeatSwitch;
//                    if("泰国".equals(placeName))carTip.setVisibility(View.VISIBLE);
                    carTripLayout.setVisibility(View.GONE);
                    carTimeTitle.setText("包车天数");
                    carTimeValue.setText("共" + dailyBean.totalDay + "天");
                    RequestCheckPriceForDaily requestCheckPriceForDaily = new RequestCheckPriceForDaily(getActivity(), dailyBean);
                    requestData(requestCheckPriceForDaily);
                    break;
                case Constants.BUSINESS_TYPE_RENT:
                    cityId = cityBean.cityId;
                    needChildrenSeat = cityBean.childSeatSwitch;
                    startLocation = startBean.location;
                    termLocation = poiBean.location;
                    serverDate = bundle.getString(KEY_TIME);
                    RequestCheckPriceForSingle requestCheckPriceForSingle = new RequestCheckPriceForSingle(getActivity(), mBusinessType, airportCode, cityId, startLocation, termLocation, serverDate);
                    requestData(requestCheckPriceForSingle);
                    break;
                default:
                    return null;
            }
        } else {
            CommonUtils.showToast("缺少参数");
        }
        return null;
    }

    private void initMaskView() {
        SharedPre shared = new SharedPre(getActivity());
        boolean maskClick = shared.getBooleanValue(KEY_MASK);
        if (maskClick) {
            carMask.setVisibility(View.GONE);
        } else {
            carMask.setVisibility(View.VISIBLE);
        }
    }


    @Event({R.id.bottom_bar_btn, R.id.car_price_info, R.id.car_mask})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.bottom_bar_btn:
                if (carBean == null || carBean.originalPrice == 0) return;
                FgSubmit fg = new FgSubmit();
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_FLIGHT, flightBean);
                bundle.putSerializable(KEY_AIRPORT, airPortBean);
                bundle.putSerializable(KEY_START, startBean);
                bundle.putSerializable(KEY_ARRIVAL, poiBean);
                bundle.putSerializable(KEY_DAILY, dailyBean);
                bundle.putSerializable(KEY_CAR, carBean);
                bundle.putSerializable(KEY_CITY, cityBean);
                bundle.putString(KEY_TIME, serverDate);
                bundle.putInt(KEY_CITY_ID, cityId);
                bundle.putInt(KEY_COM_TIME, interval);
                bundle.putInt(KEY_URGENT_FLAG, carBean.urgentFlag);
                bundle.putDouble(KEY_DISTANCE, distance);
                bundle.putBoolean(KEY_NEED_CHILDREN_SEAT, needChildrenSeat);
                bundle.putBoolean(KEY_NEED_BANNER, needBanner);
                bundle.putString("source", source);
                startFragment(fg, bundle);

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("source", source);
                map.put("carstyle", carBean.desc);
                String type = "";
                switch (mBusinessType) {
                    case Constants.BUSINESS_TYPE_PICK:
                        type = "carnext_pickup";
                        break;
                    case Constants.BUSINESS_TYPE_SEND:
                        type = "carnext_dropoff";
                        break;
                    case Constants.BUSINESS_TYPE_DAILY:
                        type = "carnext_oneday";
                        break;
                    case Constants.BUSINESS_TYPE_RENT:
                        type = "carnext_oneway";
                        break;
                }
                MobclickAgent.onEvent(getActivity(), type, map);
                break;
            case R.id.car_price_info:
                FgWebInfo fgWebInfo = new FgWebInfo();
                bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_PRICE);
                startFragment(fgWebInfo, bundle);
                break;
            case R.id.car_mask:
                SharedPre shared = new SharedPre(getActivity());
                shared.saveBooleanValue(KEY_MASK, true);
                carMask.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCheckPrice) {
            RequestCheckPrice requestCheckPrice = (RequestCheckPrice) request;
            this.distance = ((CarListBean) requestCheckPrice.getData()).distance;
            this.interval = ((CarListBean) requestCheckPrice.getData()).interval;
//            processCarList(mParser.carList);
            carList = ((CarListBean) requestCheckPrice.getData()).carList;
            sortListDataImage();
            mAdapter = new CarViewpagerAdapter(getActivity(), mJazzy);
            mAdapter.setList(carList);
            mJazzy.setState(null);
            mJazzy.setAdapter(mAdapter);
            onPageSelected(0);
            inflateContent();
            if (carList == null || carList.size() == 0) {
                carEmptyLayout.setVisibility(View.VISIBLE);
            } else {
                carEmptyLayout.setVisibility(View.GONE);
            }
        }
    }

    private void processCarList(ArrayList<CarBean> list) {
        if (carList == null) {
            carList = list;
        } else {
            int i = 0;
            Iterator<CarBean> carIterator = carList.iterator();
            while (carIterator.hasNext()) {
                CarBean bean = carIterator.next();
                if (i >= list.size()) {
                    carIterator.remove();
                } else {
                    CarBean tmpBean = list.get(i);
                    if (tmpBean != null) {
                        bean.originalPrice = tmpBean.originalPrice;
                        if (tmpBean.originalPrice == 0)
                            carIterator.remove();
                    } else {
                        carIterator.remove();
                    }
                }
                i++;
            }
        }
    }

    @Override
    protected void inflateContent() {
        if (mBusinessType == Constants.BUSINESS_TYPE_DAILY) {
            carTimeTitle.setText("包车天数");
            carTimeValue.setText("共" + (dailyBean.inTownDays + dailyBean.outTownDays) + "天");
        } else {
            carTimeValue.setText(interval + "分钟");
            carTripValue.setText(distance + "公里");
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (errorInfo.state == ExceptionErrorCode.ERROR_CODE_SERVER) {
            ServerException exception = (ServerException) errorInfo.exception;
            if (exception.getCode() != 10011 && exception.getCode() != 10012 && exception.getCode() != 10013) {
                mDialogUtil.showCustomFinishDialog(exception.getMessage(), this);
                return;
            }
        }
        super.onDataRequestError(errorInfo, request);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (carList == null || carList.size() == 0) return;
        carBean = carList.get(position);
        Integer[] carInfo = Constants.CarSeatInfoMap.get(carBean.carSeat);
        carInfoText.setText(String.format("乘坐%d人 | 行李%d件", carInfo[0], carInfo[1]));
        carTotalValue.setText(carBean.originalPrice + "元");
        carInfoIntro.setText(carBean.models);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_right_txt:
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("source", "选车页面");

                switch (mBusinessType) {
                    case Constants.BUSINESS_TYPE_PICK:
                        MobclickAgent.onEvent(getActivity(), "callcenter_pickup", map);
                        v.setTag("选车页面,calldomestic_pickup,calldomestic_pickup");
                        break;
                    case Constants.BUSINESS_TYPE_SEND:
                        MobclickAgent.onEvent(getActivity(), "callcenter_dropoff", map);
                        v.setTag("选车页面,calldomestic_dropoff,calloverseas_dropoff");
                        break;
                    case Constants.BUSINESS_TYPE_DAILY:
                        MobclickAgent.onEvent(getActivity(), "callcenter_oneday", map);
                        v.setTag("选车页面,calldomestic_oneday,calloverseas_oneday");
                        break;
                    case Constants.BUSINESS_TYPE_RENT:
                        MobclickAgent.onEvent(getActivity(), "callcenter_oneway", map);
                        v.setTag("选车页面,calldomestic_oneway,calloverseas_oneway");
                        break;
                }
                break;
        }
        super.onClick(v);
    }
}
