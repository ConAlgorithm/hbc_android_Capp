package com.hugboga.custom.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CarViewpagerAdapter;
import com.hugboga.custom.constants.CarTypeEnum;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarInfoBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.DayQuoteBean;
import com.hugboga.custom.data.bean.SelectCarBean;
import com.hugboga.custom.data.bean.ServiceQuoteSumBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestGetCarInfo;
import com.hugboga.custom.widget.JazzyViewPager;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created  on 16/4/16.
 */
@ContentView(R.layout.activity_select_car)
public class FGSelectCar extends BaseFragment implements ViewPager.OnPageChangeListener {

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.jazzy_pager)
    JazzyViewPager jazzyPager;
    @Bind(R.id.left)
    ImageView left;
    @Bind(R.id.right)
    ImageView right;
    @Bind(R.id.car_type)
    TextView carType;
    @Bind(R.id.mans)
    TextView mans;
    @Bind(R.id.baggages)
    TextView baggages;
    @Bind(R.id.car_content)
    TextView carContent;
    @Bind(R.id.mans_left)
    TextView mansLeft;
    @Bind(R.id.mans_day_num)
    TextView mansDayNum;
    @Bind(R.id.mans_charge)
    TextView mansCharge;
    @Bind(R.id.mans_charge_tips)
    TextView mansChargeTips;
    @Bind(R.id.mans_money_all_info)
    LinearLayout mansMoneyAllInfo;
    @Bind(R.id.mans_money_show_info)
    TextView mansMoneyShowInfo;
    @Bind(R.id.mans_charge_layout)
    RelativeLayout mansChargeLayout;
    @Bind(R.id.cars_left)
    TextView carsLeft;
    @Bind(R.id.cars_day_num)
    TextView carsDayNum;
    @Bind(R.id.cars_charge)
    TextView carsCharge;
    @Bind(R.id.cars_charge_tips)
    TextView carsChargeTips;
    @Bind(R.id.cars_money_all_info)
    LinearLayout carsMoneyAllInfo;
    @Bind(R.id.cars_money_show_info)
    TextView carsMoneyShowInfo;
    @Bind(R.id.cars_charge_layout)
    RelativeLayout carsChargeLayout;
    @Bind(R.id.all_left)
    TextView allLeft;
    @Bind(R.id.all_day_num)
    TextView allDayNum;
    @Bind(R.id.all_charge)
    TextView allCharge;
    @Bind(R.id.per_left)
    TextView perLeft;
    @Bind(R.id.per_charge)
    TextView perCharge;
    @Bind(R.id.befer48_tips)
    TextView befer48Tips;
    @Bind(R.id.next_btn_click)
    Button nextBtnClick;
    @Bind(R.id.coupon_listview_empty)
    RelativeLayout coupon_listview_empty;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.mans_serviceCityNote)
    TextView mans_serviceCityNote;
    @Bind(R.id.cars_serviceCityNote)
    TextView cars_serviceCityNote;

    @Override
    protected void initHeader() {
        fgRightBtn.setVisibility(View.VISIBLE);
        fgTitle.setText(R.string.select_city_title);
        source = getArguments().getString("source");
    }

    @Override
    protected void inflateContent() {

    }


    /**
     * http://api.dev.hbc.tech/price/v1.2/e/dailyPrice?
     * // endCityId=204&startCityId=204&channelId=1925283890
     * // &startDate=2016-05-12%2019:30:22&endDate=2016-05-13%2000:00:00
     * // &halfDay=0&adultNum=4&childrenNum=1&childseatNum=0
     * // &luggageNum=0&passCities=1_1_204,1_1_2042
     **/
    public String startCityId = "204";
    public String endCityId = "204";
    public String startDate = "2016-05-12";
    public String endDate = "2016-05-13";
    public String halfDay = "0";
    public String adultNum = "4";
    public String childrenNum = "1";
    public String childseatNum = "0";
    public String luggageNum = "0";
    public String passCities = "204-1-1,204-1-2";
    public String channelId = "18";

    public String startCityName = "";
    public String carTypeName = "";
    public String dayNums = "";

    public int inNum = 0;
    public int outNum = 0;

    CarViewpagerAdapter mAdapter;
    private ArrayList<CarBean> carList = new ArrayList<CarBean>();
    @Override
    protected void initView() {
        getArgs();
        RequestGetCarInfo requestGetCarInfo = new RequestGetCarInfo(this.getActivity(),
                startCityId, endCityId, startDate+" 00:00:00", endDate+" 00:00:00", halfDay, adultNum,
                childrenNum, childseatNum, luggageNum, passCities,channelId);
        HttpRequestUtils.request(this.getActivity(), requestGetCarInfo, this);
        jazzyPager.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);
        mAdapter = new CarViewpagerAdapter(getActivity(), jazzyPager);
//        initListData();
//        mAdapter.setList(carList);
//        jazzyPager.setAdapter(mAdapter);
//        jazzyPager.setOffscreenPageLimit(5);
//        jazzyPager.addOnPageChangeListener(this);
    }

    CityBean startBean;
    CityBean endBean;
    ArrayList<CityBean> passCityList;
    private void getArgs(){
        passCityList = (ArrayList<CityBean>) getArguments().getSerializable("passCityList");

        startCityId = this.getArguments().getString("startCityId");
        endCityId = this.getArguments().getString("endCityId");
        startDate = this.getArguments().getString("startDate");
        endDate = this.getArguments().getString("endDate");
        halfDay = this.getArguments().getString("halfDay");
        adultNum = this.getArguments().getString("adultNum");
        childrenNum = this.getArguments().getString("childrenNum");
        childseatNum = this.getArguments().getString("childseatNum");
        luggageNum = this.getArguments().getString("luggageNum");
        passCities = this.getArguments().getString("passCities");

        startCityName = this.getArguments().getString("startCityName");
        dayNums = this.getArguments().getString("dayNums");

        startBean = this.getArguments().getParcelable("startBean");
        endBean = this.getArguments().getParcelable("endBean");

        inNum = this.getArguments().getInt("innum");
        outNum = this.getArguments().getInt("outnum");
    }

    int selctIndex = 0;
    List<SelectCarBean> cars;
    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        setProgressState(0);
        if(request instanceof  RequestGetCarInfo) {
            CarInfoBean bean = (CarInfoBean) request.getData();
            if(null != bean) {
                cars = bean.cars;
                halfDay = bean.halfDay == 1?"1":"0";
                if(cars.size() == 0){
                    coupon_listview_empty.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                    nextBtnClick.setVisibility(View.GONE);
                }else {
                    initListData();
                    getMatchCarIndex();
                    showContent();
                }
            }
        }

    }

    private void getMatchCarIndex(){
        for(int i = 0;i<cars.size();i++){
            if(cars.get(i).match == 1){
                selctIndex = i;
                break;
            }
        }
    }

    SelectCarBean carBean;
    public void showContent(){
        changeLeftRightStatus();
        try {
            jazzyPager.setCurrentItem(selctIndex);
            carBean = cars.get(selctIndex);
            carType.setText(carBean.carDesc);
            carContent.setText("此车型包括:" + carBean.models);
            if(carBean.match == 0){
                nextBtnClick.setBackgroundColor(Color.parseColor("#d5dadb"));
                nextBtnClick.setText("人数已超限，请更换车型");
                nextBtnClick.setClickable(false);
            }else{
                nextBtnClick.setClickable(true);
                nextBtnClick.setBackgroundColor(Color.parseColor("#fbd003"));
                nextBtnClick.setText("下一步");
            }
            genServiceInfo(false);
            genCarsInfo(false);
            genTotal();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void genTotal(){
        carBean = cars.get(selctIndex);
        mans.setText(String.format(getString(R.string.have_mas),carBean.capOfPerson));
        baggages.setText(String.format(getString(R.string.have_baggages),carBean.capOfLuggage));
        carType.setText(carBean.carDesc);
        carContent.setText("此车型包括:"+carBean.models);
        if(TextUtils.isEmpty(carBean.serviceCityNote)){
            mans_serviceCityNote.setVisibility(View.GONE);
        }else{
            mans_serviceCityNote.setVisibility(View.VISIBLE);
            mans_serviceCityNote.setText(carBean.serviceCityNote);
        }

        if(TextUtils.isEmpty(carBean.serviceCityNote)){
            cars_serviceCityNote.setVisibility(View.GONE);
        }else{
            cars_serviceCityNote.setVisibility(View.VISIBLE);
            cars_serviceCityNote.setText(carBean.serviceCityNote);
        }

        allDayNum.setText(carBean.totalDays+"天 / "+carBean.numOfPerson+"人");
        allCharge.setText(carBean.price+"");
        perCharge.setText(carBean.avgSpend+"");
    }

    View view = null;
    TextView day_all_money_left,day_all_money_right;
    TextView day_line2_money_left,day_line2_money_right;
    TextView day_line3_money_left,day_line3_money_right;
    TextView day_line4_money_left,day_line4_money_right;
    TextView day_line2_money_middle,day_line3_money_middle,day_line4_money_middle;
    public void genCarsInfo(boolean isMando){
        try {
            carsMoneyAllInfo.removeAllViews();
            if (isMando) {
                if (carsMoneyAllInfo.isShown()) {
                    carsMoneyAllInfo.setVisibility(View.GONE);
                    carsMoneyShowInfo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_unfold,0,0,0);
                    carsMoneyShowInfo.setText("展开详情");
                } else {
                    carsMoneyAllInfo.setVisibility(View.VISIBLE);
                    carsMoneyShowInfo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_withdraw,0,0,0);
                    carsMoneyShowInfo.setText("收起详情");
                }
            }
            carBean = cars.get(selctIndex);
            carsCharge.setText(carBean.vehiclePrice+"元");
            carsDayNum.setText("x"+carBean.totalDays+"天");
            ServiceQuoteSumBean serviceQuoteSumBean = carBean.vehicleQuoteSum;
            List<DayQuoteBean> dayQuotes = serviceQuoteSumBean.dayQuotes;
            DayQuoteBean dayQuoteBean = null;
            for (int i = 0; i < dayQuotes.size(); i++) {
                dayQuoteBean = dayQuotes.get(i);
                view = LayoutInflater.from(this.getActivity()).inflate(R.layout.car_quote_item, null);
                day_all_money_left = (TextView) view.findViewById(R.id.day_all_money_left);
                day_all_money_right = (TextView) view.findViewById(R.id.day_all_money_right);
                day_line2_money_left = (TextView) view.findViewById(R.id.day_line2_money_left);
                day_line2_money_right = (TextView) view.findViewById(R.id.day_line2_money_right);
                day_line3_money_left = (TextView) view.findViewById(R.id.day_line3_money_left);
                day_line3_money_right = (TextView) view.findViewById(R.id.day_line3_money_right);
                day_line4_money_left = (TextView) view.findViewById(R.id.day_line4_money_left);
                day_line4_money_right = (TextView) view.findViewById(R.id.day_line4_money_right);
                day_line2_money_middle = (TextView) view.findViewById(R.id.day_line2_money_middle);
                day_line3_money_middle = (TextView) view.findViewById(R.id.day_line3_money_middle);
                day_line4_money_middle = (TextView) view.findViewById(R.id.day_line4_money_middle);

                day_all_money_left.setText(String.format(getString(R.string.day_all_money), dayQuoteBean.day));
                day_all_money_right.setText(dayQuoteBean.totalPrice + "元");

                if (dayQuoteBean.vehiclePrice != 0) {
                    day_line2_money_middle.setVisibility(View.VISIBLE);
                    day_line2_money_left.setVisibility(View.VISIBLE);
                    day_line2_money_right.setVisibility(View.VISIBLE);
                    if(dayQuoteBean.busySeason == 1){
                        day_line2_money_left.setText(getString(R.string.vehiclePrice)+",旺季");
                    }else{
                        day_line2_money_left.setText(getString(R.string.vehiclePrice));
                    }

                    day_line2_money_right.setText(dayQuoteBean.vehiclePrice + "元");
                } else {
                    day_line2_money_middle.setVisibility(View.GONE);
                    day_line2_money_left.setVisibility(View.GONE);
                    day_line2_money_right.setVisibility(View.GONE);
                }


                if (dayQuoteBean.emptyDrivePrice != 0) {
                    day_line3_money_middle.setVisibility(View.VISIBLE);
                    day_line3_money_left.setVisibility(View.VISIBLE);
                    day_line3_money_right.setVisibility(View.VISIBLE);
                    if(dayQuoteBean.busySeason == 1){
                        day_line3_money_left.setText(getString(R.string.emptyDrivePrice)+",旺季");
                    }else{
                        day_line3_money_left.setText(getString(R.string.emptyDrivePrice));
                    }
                    day_line3_money_right.setText(dayQuoteBean.emptyDrivePrice + "元");
                } else {
                    day_line3_money_middle.setVisibility(View.GONE);
                    day_line3_money_left.setVisibility(View.GONE);
                    day_line3_money_right.setVisibility(View.GONE);
                }

                if (dayQuoteBean.longDisPrice != 0) {
                    day_line4_money_middle.setVisibility(View.VISIBLE);
                    day_line4_money_left.setVisibility(View.VISIBLE);
                    day_line4_money_right.setVisibility(View.VISIBLE);
                    day_line4_money_left.setText(getString(R.string.longDisPrice));
                    day_line4_money_right.setText(dayQuoteBean.longDisPrice + "元");
                } else {
                    day_line4_money_middle.setVisibility(View.GONE);
                    day_line4_money_left.setVisibility(View.GONE);
                    day_line4_money_right.setVisibility(View.GONE);
                }

                carsMoneyAllInfo.addView(view);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void genServiceInfo(boolean isMando){
        try {
            mansMoneyAllInfo.removeAllViews();
            if (isMando) {
                if (mansMoneyAllInfo.isShown()) {
                    mansMoneyAllInfo.setVisibility(View.GONE);
                    mansMoneyShowInfo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_unfold,0,0,0);
                    mansMoneyShowInfo.setText("展开详情");
                } else {
                    mansMoneyAllInfo.setVisibility(View.VISIBLE);
                    mansMoneyShowInfo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_withdraw,0,0,0);
                    mansMoneyShowInfo.setText("收起详情");
                }
            }
            carBean = cars.get(selctIndex);
            mansCharge.setText(carBean.servicePrice+"元");
            mansDayNum.setText("x"+carBean.totalDays+"天");
            ServiceQuoteSumBean serviceQuoteSumBean = carBean.serviceQuoteSum;
            List<DayQuoteBean> dayQuotes = serviceQuoteSumBean.dayQuotes;
            DayQuoteBean dayQuoteBean = null;
            for (int i = 0; i < dayQuotes.size(); i++) {
                dayQuoteBean = dayQuotes.get(i);
                view = LayoutInflater.from(this.getActivity()).inflate(R.layout.car_quote_item, null);
                day_all_money_left = (TextView) view.findViewById(R.id.day_all_money_left);
                day_all_money_right = (TextView) view.findViewById(R.id.day_all_money_right);
                day_line2_money_left = (TextView) view.findViewById(R.id.day_line2_money_left);
                day_line2_money_right = (TextView) view.findViewById(R.id.day_line2_money_right);
                day_line3_money_left = (TextView) view.findViewById(R.id.day_line3_money_left);
                day_line3_money_right = (TextView) view.findViewById(R.id.day_line3_money_right);
                day_line4_money_left = (TextView) view.findViewById(R.id.day_line4_money_left);
                day_line4_money_right = (TextView) view.findViewById(R.id.day_line4_money_right);
                day_line2_money_middle = (TextView) view.findViewById(R.id.day_line2_money_middle);
                day_line3_money_middle = (TextView) view.findViewById(R.id.day_line3_money_middle);
                day_line4_money_middle = (TextView) view.findViewById(R.id.day_line4_money_middle);

                day_all_money_left.setText(String.format(getString(R.string.day_all_money), dayQuoteBean.day));
                day_all_money_right.setText(dayQuoteBean.totalPrice + "元");

                if (dayQuoteBean.guideServicePrice != 0) {
                    day_line2_money_middle.setVisibility(View.VISIBLE);
                    day_line2_money_left.setVisibility(View.VISIBLE);
                    day_line2_money_right.setVisibility(View.VISIBLE);
                    if(dayQuoteBean.busySeason == 1){
                        day_line2_money_left.setText(getString(R.string.service_money)+",旺季");
                    }else{
                        day_line2_money_left.setText(getString(R.string.service_money));
                    }
                    day_line2_money_right.setText(dayQuoteBean.guideServicePrice + "元");
                } else {
                    day_line2_money_middle.setVisibility(View.GONE);
                    day_line2_money_left.setVisibility(View.GONE);
                    day_line2_money_right.setVisibility(View.GONE);
                }

                if (dayQuoteBean.stayPrice != 0) {
                    day_line3_money_middle.setVisibility(View.VISIBLE);
                    day_line3_money_left.setVisibility(View.VISIBLE);
                    day_line3_money_right.setVisibility(View.VISIBLE);
                    if(dayQuoteBean.busySeason == 1){
                        day_line3_money_left.setText(getString(R.string.stayPrice)+",旺季");
                    }else{
                        day_line3_money_left.setText(getString(R.string.stayPrice));
                    }
                    day_line3_money_right.setText(dayQuoteBean.stayPrice + "元");
                } else {
                    day_line3_money_middle.setVisibility(View.GONE);
                    day_line3_money_left.setVisibility(View.GONE);
                    day_line3_money_right.setVisibility(View.GONE);
                }

                mansMoneyAllInfo.addView(view);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        coupon_listview_empty.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void changeLeftRightStatus(){
        if(selctIndex == 0){
            left.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
        }else if(selctIndex < (cars.size() -1)){
            right.setVisibility(View.VISIBLE);
            left.setVisibility(View.VISIBLE);
        }else if(selctIndex == (cars.size() -1)){
            right.setVisibility(View.GONE);
            left.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.befer48_tips,R.id.left, R.id.right, R.id.mans_money_show_info, R.id.cars_money_show_info, R.id.next_btn_click})
    public void onClick(View view) {
        HashMap<String,String> map = new HashMap<String,String>();
        switch (view.getId()) {
            case R.id.befer48_tips:
                Bundle bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_CANCEL);
                startFragment(new FgActivity(), bundle);
                break;
            case R.id.header_right_txt:
                map.put("source", "选车页面");
                MobclickAgent.onEvent(getActivity(), "callcenter_oneday", map);
                view.setTag("选车页面,calldomestic_oneday,calloverseas_oneday");
                super.onClick(view);
                break;
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.left:
                if(selctIndex >=1) {
                    --selctIndex;
                    jazzyPager.setCurrentItem(selctIndex);
                }
                break;
            case R.id.right:
                if(selctIndex < cars.size() -1) {
                    ++selctIndex;
                    jazzyPager.setCurrentItem(selctIndex);
                }
                break;
            case R.id.mans_money_show_info:
                genServiceInfo(true);
                break;
            case R.id.cars_money_show_info:
                genCarsInfo(true);
                break;
            case R.id.next_btn_click:
                FGOrderNew fgOrderNew = new FGOrderNew();
                Bundle bundleCar = new Bundle();
                bundleCar.putString("source",source);
                bundleCar.putString("startCityId",startCityId);
                bundleCar.putString("endCityId",endCityId);
                bundleCar.putString("startDate",startDate);
                bundleCar.putString("endDate",endDate);
                bundleCar.putString("halfDay",halfDay);
                bundleCar.putString("adultNum", adultNum);
                bundleCar.putString("childrenNum", childrenNum);
                bundleCar.putString("childseatNum", childseatNum);
                bundleCar.putString("luggageNum", luggageNum);
                bundleCar.putString("passCities", passCities);
                bundleCar.putString("carTypeName", carBean.carDesc);
                bundleCar.putString("startCityName", startCityName);
                bundleCar.putString("dayNums", dayNums);
                bundleCar.putParcelable("carBean", carBean);
                bundleCar.putParcelable("startBean", startBean);
                bundleCar.putParcelable("endBean", endBean);
                bundleCar.putInt("outnum", outNum);
                bundleCar.putInt("innum", inNum);
                bundleCar.putSerializable("passCityList", passCityList);
                fgOrderNew.setArguments(bundleCar);
                startFragment(fgOrderNew);

                map.put("source", source);
                map.put("begincity", startBean.name);
                map.put("carstyle", carBean.carDesc);
                map.put("guestcount", adultNum + childrenNum + "");
                map.put("luggagecount", luggageNum + "");
                map.put("drivedays", dayNums + "");
                map.put("payableamount", carBean.price + "");
                MobclickAgent.onEventValue(getActivity(), "carnext_oneday", map, 1);
                break;
        }
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
                if(isMatchLocal(bean)) {
                    carList.add(bean);
                }
                id++;
            }
        }
        mAdapter.setList(carList);
        jazzyPager.setAdapter(mAdapter);
        jazzyPager.setOffscreenPageLimit(5);
        jazzyPager.addOnPageChangeListener(this);
//        mAdapter.notifyDataSetChanged();
    }

    private boolean isMatchLocal(CarBean bean){
        for(int i = 0;i<cars.size();i++){
            if(cars.get(i).carType == bean.carType && cars.get(i).seatCategory == bean.carSeat ){
                        return true;
            }
        }
        return false;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        selctIndex = position;
        MLog.e("position========="+position);
        showContent();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
