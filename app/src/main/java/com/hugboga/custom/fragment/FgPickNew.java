package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForPickup;
import com.hugboga.custom.utils.ToastUtils;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.hugboga.custom.R.id.driver_layout;
import static com.hugboga.custom.R.id.driver_name;

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
    @Bind(R.id.show_cars_layout)
    LinearLayout showCarsLayout;
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
    }

    FragmentManager fm;
    FgCarNew fgCarNew;

    private void initCarFragment() {
        fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
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
        fgCarNew.setArguments(bundle);
        transaction.add(R.id.show_cars_layout, fgCarNew);
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

    private void genBottomData(CarBean carBean) {
        allMoneyText.setText("￥ " + carBean.price);
        if(null != carListBean) {
            allJourneyText.setText("全程预估:" + carListBean.distance + "公里," + carListBean.interval + "分钟");
        }
    }

    @Override
    protected void inflateContent() {

    }


    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case AIR_NO:
                FlightBean bean = (FlightBean) action.getData();
                if (mBusinessType == Constants.BUSINESS_TYPE_SEND && bean != null) {
                } else {
                    flightBean = bean;
                    String flightInfoStr = bean.flightNo + " ";
                    flightInfoStr += bean.depAirport.cityName + "-" + bean.arrivalAirport.cityName;
                    flightInfoStr += "\n当地时间" + bean.arrDate + " " + bean.depTime + " 降落";
                    infoTips.setVisibility(View.GONE);
                    airTitle.setVisibility(View.VISIBLE);
                    airDetail.setVisibility(View.VISIBLE);
                    airTitle.setText(bean.arrAirportName);
                    airDetail.setText(flightInfoStr);
                }
                break;
            case CHANGE_CAR:
                CarBean carBean = (CarBean) action.getData();
                genBottomData(carBean);
                break;
            default:
                break;
        }
    }

    @Override
    public int getBusinessType() {
        mBusinessType = Constants.BUSINESS_TYPE_PICK;
        setGoodsType(mBusinessType);
        return mBusinessType;
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
                    ToastUtils.showShort("请先选择航班");
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
