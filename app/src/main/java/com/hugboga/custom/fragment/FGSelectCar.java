package com.hugboga.custom.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import com.hugboga.custom.data.bean.DayQuoteBean;
import com.hugboga.custom.data.bean.SelectCarBean;
import com.hugboga.custom.data.bean.ServiceQuoteSumBean;
import com.hugboga.custom.data.request.RequestGetCarInfo;
import com.hugboga.custom.widget.JazzyViewPager;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyt on 16/4/16.
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

    @Override
    protected void initHeader() {
        fgTitle.setText(R.string.select_city_title);
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
    public String startDate = "2016-05-12 00:00:00";
    public String endDate = "2016-05-13 00:00:00";
    public String halfDay = "0";
    public String adultNum = "4";
    public String childrenNum = "1";
    public String childseatNum = "0";
    public String luggageNum = "0";
    public String passCities = "204-1-1,204-1-2";
    public String channelId = "1925283890";

    CarViewpagerAdapter mAdapter;
    private ArrayList<CarBean> carList = new ArrayList<CarBean>();
    @Override
    protected void initView() {
        getArgs();
        RequestGetCarInfo requestGetCarInfo = new RequestGetCarInfo(this.getActivity(),
                startCityId, endCityId, startDate, endDate, halfDay, adultNum, childrenNum, childseatNum, luggageNum, passCities,channelId);
        HttpRequestUtils.request(this.getActivity(), requestGetCarInfo, this);
        jazzyPager.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);
        mAdapter = new CarViewpagerAdapter(getActivity(), jazzyPager);
//        initListData();
//        mAdapter.setList(carList);
//        jazzyPager.setAdapter(mAdapter);
//        jazzyPager.setOffscreenPageLimit(5);
//        jazzyPager.addOnPageChangeListener(this);
    }

    private void getArgs(){
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
                if(cars.size() == 0){
                    coupon_listview_empty.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }else {
                    initListData();
                    showContent();
                }
            }
        }

    }

    SelectCarBean carBean;
    public void showContent(){
        try {
            carBean = cars.get(selctIndex);
            carType.setText(carBean.carDesc);
            carContent.setText("此车型包括:" + carBean.models);
            if(carBean.match == 0){
                nextBtnClick.setBackgroundColor(Color.parseColor("#d5dadb"));
                nextBtnClick.setClickable(false);
            }else{
                nextBtnClick.setClickable(true);
                nextBtnClick.setBackgroundColor(Color.parseColor("#fbd003"));
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

        allDayNum.setText(carBean.totalDays+"天 x"+carBean.numOfPerson+"人");
        allCharge.setText(carBean.price+"元");
        perCharge.setText(carBean.avgSpend+"元");
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
                } else {
                    carsMoneyAllInfo.setVisibility(View.VISIBLE);
                    carsMoneyShowInfo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_withdraw,0,0,0);
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
                    day_line2_money_left.setText(getString(R.string.service_money));
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
                    day_line3_money_left.setText(getString(R.string.emptyDrivePrice));
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
                } else {
                    mansMoneyAllInfo.setVisibility(View.VISIBLE);
                    mansMoneyShowInfo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_withdraw,0,0,0);
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
                    day_line2_money_left.setText(getString(R.string.service_money));
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
                    day_line3_money_left.setText(getString(R.string.stayPrice));
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

    @OnClick({R.id.left, R.id.right, R.id.mans_money_show_info, R.id.cars_money_show_info, R.id.next_btn_click})
    public void onClick(View view) {
        switch (view.getId()) {
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
                startFragment(new FGOrderNew());
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
