package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.LuggageInfoActivity;
import com.hugboga.custom.adapter.CarViewpagerAdapter;
import com.hugboga.custom.constants.ChooseCarTypeEnum;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarInfoBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.DayQuoteBean;
import com.hugboga.custom.data.bean.SelectCarBean;
import com.hugboga.custom.data.bean.ServiceQuoteSumBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestGetCarInfo;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.PhoneInfo;
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

import static com.hugboga.custom.R.id.all_money_left_text;
import static com.hugboga.custom.R.id.all_money_right;
import static com.hugboga.custom.R.id.average_money;
import static com.hugboga.custom.R.id.child_right;
import static com.hugboga.custom.R.id.days_left;
import static com.hugboga.custom.R.id.days_right;
import static com.hugboga.custom.R.id.man_have;
import static com.hugboga.custom.R.id.man_right;
import static com.hugboga.custom.R.id.max_luggage_content;

/**
 * Created  on 16/4/16.
 */
@ContentView(R.layout.activity_select_car_new)
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
    TextView nextBtnClick;
    @Bind(R.id.coupon_listview_empty)
    LinearLayout coupon_listview_empty;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.mans_serviceCityNote)
    TextView mans_serviceCityNote;
    @Bind(R.id.cars_serviceCityNote)
    TextView cars_serviceCityNote;
    @Bind(R.id.in_phone)
    TextView inPhone;
    @Bind(R.id.out_phone)
    TextView outPhone;
    @Bind(R.id.viewpager_layout)
    LinearLayout viewpagerLayout;
    @Bind(R.id.people_img)
    ImageView peopleImg;
    @Bind(R.id.order_style_img)
    ImageView orderStyleImg;
    @Bind(R.id.all_charge_yuan)
    TextView allChargeYuan;
    @Bind(R.id.per_charge_yuan)
    TextView perChargeYuan;
    @Bind(R.id.man_left)
    TextView manLeft;
    @Bind(man_right)
    TextView manRight;
    @Bind(man_have)
    TextView manHave;
    @Bind(R.id.child_left)
    TextView childLeft;
    @Bind(child_right)
    TextView childRight;
    @Bind(R.id.max_luggage_tv)
    TextView maxLuggageTv;
    @Bind(max_luggage_content)
    TextView maxLuggageContent;
    @Bind(R.id.max_luggage_img)
    ImageView maxLuggageImg;
    @Bind(R.id.max_luggage_tips)
    TextView maxLuggageTips;
    @Bind(days_left)
    TextView daysLeft;
    @Bind(days_right)
    TextView daysRight;
    @Bind(R.id.all_money)
    TextView allMoney;
    @Bind(all_money_right)
    TextView allMoneyRight;
    @Bind(R.id.average_left)
    TextView averageLeft;
    @Bind(average_money)
    TextView averageMoney;
    @Bind(R.id.all_money_left)
    TextView allMoneyLeft;
    @Bind(all_money_left_text)
    TextView allMoneyLeftText;
    @Bind(R.id.all_money_info)
    TextView allMoneyInfo;

    @Override
    protected void initHeader() {
        fgRightBtn.setVisibility(View.VISIBLE);
        fgTitle.setText(R.string.select_city_title);
        source = getArguments().getString("source");
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public int getBusinessType() {
        mBusinessType = Constants.BUSINESS_TYPE_DAILY;
        setGoodsType(mBusinessType);
        return mBusinessType;
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

    public String serverTime = "";

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
                startCityId, endCityId, startDate + " " + serverTime + ":00", endDate + " " + serverTime + ":00", halfDay, adultNum,
                childrenNum, childseatNum, luggageNum, passCities, channelId, null);
        HttpRequestUtils.request(this.getActivity(), requestGetCarInfo, this);
        jazzyPager.setState(null);
        jazzyPager.setOffscreenPageLimit(3);
        jazzyPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Tablet);
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
    boolean isHalfTravel = false;

    String orderType = "";

    private void getArgs() {
        passCityList = (ArrayList<CityBean>) getArguments().getSerializable("passCityList");

        startCityId = this.getArguments().getString("startCityId");
        endCityId = this.getArguments().getString("endCityId");
        startDate = this.getArguments().getString("startDate");
        serverTime = this.getArguments().getString("serverTime");
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
        isHalfTravel = this.getArguments().getBoolean("isHalfTravel");

        orderType = this.getArguments().getString("orderType");
    }

    int selctIndex = 0;
    List<SelectCarBean> cars;

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        setProgressState(0);
        if (request instanceof RequestGetCarInfo) {
            CarInfoBean bean = (CarInfoBean) request.getData();
            if (null != bean) {
                cars = bean.cars;
                halfDay = bean.halfDay == 1 ? "1" : "0";
                if (cars.size() == 0) {
                    coupon_listview_empty.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                    nextBtnClick.setVisibility(View.GONE);
                } else {
                    initListData();
                    getMatchCarIndex();
                    showContent();
                    if (carBean.match == 0) {
                        jazzyPager.setCurrentItem(cars.size() - 1);
                    }
                }
            }
        }

    }

    private void getMatchCarIndex() {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).match == 1) {
                selctIndex = i;
                break;
            }
        }
        jazzyPager.setCurrentItem(selctIndex);
    }

    SelectCarBean carBean;

    public void showContent() {
//        changeLeftRightStatus();
        try {
            jazzyPager.setCurrentItem(selctIndex);
            carBean = cars.get(selctIndex);
            carType.setText(carBean.carDesc);
            carContent.setText(carBean.models);
            if (carBean.match == 0) {
                nextBtnClick.setBackgroundColor(Color.parseColor("#d5dadb"));
                nextBtnClick.setText("人数已超限，请更换车型");
                nextBtnClick.setClickable(false);
            } else {
                nextBtnClick.setClickable(true);
                nextBtnClick.setBackgroundColor(Color.parseColor("#fbd003"));
                nextBtnClick.setText("下一步");
            }
            genServiceInfo(false);
            genCarsInfo(false);
            genTotal();
            genNewContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void genNewContent(){
        int mans = Integer.valueOf(adultNum) + Integer.valueOf(childrenNum);
        manHave.setText(mans+"人");
        manRight.setText(" / "+carBean.capOfPerson+"人");
        childRight.setText(childseatNum+"个");

        int maxLuuages = (carBean.capOfLuggage+ carBean.capOfPerson)
                - Integer.valueOf(adultNum) - Math.round(Integer.valueOf(childseatNum) * 1.5f)
                - (Integer.valueOf(childrenNum) - Integer.valueOf(childseatNum));

        maxLuggageContent.setText(maxLuuages+"件");

        daysLeft.setText(dayNums+"天包车+司导");
        daysRight.setText("用车费用￥"+ carBean.vehiclePrice+"\n+司导费用￥"+ carBean.servicePrice);
        allMoneyRight.setText("￥"+(carBean.vehiclePrice+carBean.servicePrice));
        averageMoney.setText("￥"+(carBean.vehiclePrice+carBean.servicePrice)/mans);
        allMoneyLeftText.setText("￥"+(carBean.vehiclePrice+carBean.servicePrice));
    }

    public void genTotal() {
        carBean = cars.get(selctIndex);
        mans.setText(String.format(getString(R.string.have_mas), carBean.capOfPerson));
        baggages.setText(String.format(getString(R.string.have_baggages), carBean.capOfLuggage));
        carType.setText(carBean.carDesc);
        carContent.setText(carBean.models);
        if (TextUtils.isEmpty(carBean.serviceCityNote)) {
            mans_serviceCityNote.setVisibility(View.GONE);
        } else {
            mans_serviceCityNote.setVisibility(View.VISIBLE);
            mans_serviceCityNote.setText(carBean.serviceCityNote);
        }

        if (TextUtils.isEmpty(carBean.serviceCityNote)) {
            cars_serviceCityNote.setVisibility(View.GONE);
        } else {
            cars_serviceCityNote.setVisibility(View.VISIBLE);
            cars_serviceCityNote.setText(carBean.serviceCityNote);
        }
        String days = "";
        if (halfDay.equalsIgnoreCase("1")) {
            days = "0.5";
        } else {
            days = carBean.totalDays + "";
        }
        allDayNum.setText(days + "天 X " + carBean.numOfPerson + "人");
        allCharge.setText(carBean.price + "");
        perCharge.setText(carBean.avgSpend + "");
    }

    View view = null;
    TextView day_all_money_left, day_all_money_right;
    TextView day_line2_money_left, day_line2_money_right;
    TextView day_line3_money_left, day_line3_money_right;
    TextView day_line4_money_left, day_line4_money_right;
    TextView day_line2_money_middle, day_line3_money_middle, day_line4_money_middle;

    public void genCarsInfo(boolean isMando) {
        try {
            carsMoneyAllInfo.removeAllViews();
            if (isMando) {
                if (carsMoneyAllInfo.isShown()) {
                    carsMoneyAllInfo.setVisibility(View.GONE);
                    carsMoneyShowInfo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_unfold, 0, 0, 0);
                    carsMoneyShowInfo.setText("展开详情");
                } else {
                    carsMoneyAllInfo.setVisibility(View.VISIBLE);
                    carsMoneyShowInfo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_withdraw, 0, 0, 0);
                    carsMoneyShowInfo.setText("收起详情");
                }
            }
            carBean = cars.get(selctIndex);
            carsCharge.setText(carBean.vehiclePrice + "元");
            carsDayNum.setText(halfDay.equalsIgnoreCase("1") ? "x 0.5天" : "x" + carBean.totalDays + "天");
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
                    if (dayQuoteBean.busySeason == 1) {
                        day_line2_money_left.setText(getString(R.string.vehiclePrice) + ",旺季");
                    } else {
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
                    if (dayQuoteBean.busySeason == 1) {
                        day_line3_money_left.setText(getString(R.string.emptyDrivePrice) + ",旺季");
                    } else {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void genServiceInfo(boolean isMando) {
        try {
            mansMoneyAllInfo.removeAllViews();
            if (isMando) {
                if (mansMoneyAllInfo.isShown()) {
                    mansMoneyAllInfo.setVisibility(View.GONE);
                    mansMoneyShowInfo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_unfold, 0, 0, 0);
                    mansMoneyShowInfo.setText("展开详情");
                } else {
                    mansMoneyAllInfo.setVisibility(View.VISIBLE);
                    mansMoneyShowInfo.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_withdraw, 0, 0, 0);
                    mansMoneyShowInfo.setText("收起详情");
                }
            }
            carBean = cars.get(selctIndex);
            mansCharge.setText(carBean.servicePrice + "元");
            mansDayNum.setText(halfDay.equalsIgnoreCase("1") ? "x 0.5天" : "x" + carBean.totalDays + "天");
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
                    if (dayQuoteBean.busySeason == 1) {
                        day_line2_money_left.setText(getString(R.string.service_money) + ",旺季");
                    } else {
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
                    if (dayQuoteBean.busySeason == 1) {
                        day_line3_money_left.setText(getString(R.string.stayPrice) + ",旺季");
                    } else {
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
        } catch (Exception e) {
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

    private void changeLeftRightStatus() {
        if (selctIndex == 0) {
            left.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
        } else if (selctIndex < (cars.size() - 1)) {
            right.setVisibility(View.VISIBLE);
            left.setVisibility(View.VISIBLE);
        } else if (selctIndex == (cars.size() - 1)) {
            right.setVisibility(View.GONE);
            left.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.all_money_info,R.id.max_luggage_img,R.id.in_phone, R.id.out_phone, R.id.befer48_tips, R.id.left, R.id.right, R.id.mans_money_show_info, R.id.cars_money_show_info, R.id.next_btn_click})
    public void onClick(View view) {
        HashMap<String, String> map = new HashMap<String, String>();
        switch (view.getId()) {
            case R.id.all_money_info:
                Bundle bundleInfo = new Bundle();
                bundleInfo.putParcelable("carBean",carBean);
                bundleInfo.putString("halfDay",halfDay);
                startFragment(new FgOrderInfo(),bundleInfo);
                break;
            case R.id.max_luggage_img:
                startActivity(new Intent(getActivity(), LuggageInfoActivity.class));
                break;
            case R.id.in_phone:
                PhoneInfo.CallDial(this.getActivity(), Constants.CALL_NUMBER_IN);
                break;
            case R.id.out_phone:
                PhoneInfo.CallDial(this.getActivity(), Constants.CALL_NUMBER_IN);
                break;
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
                if (selctIndex >= 1) {
                    --selctIndex;
                    jazzyPager.setCurrentItem(selctIndex);
                }
                break;
            case R.id.right:
                if (selctIndex < cars.size() - 1) {
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
                if (UserEntity.getUser().isLogin(getActivity())) {
                    if ((carBean.carType == 1 && carBean.capOfPerson == 4 && (Integer.valueOf(adultNum) + Integer.valueOf(childrenNum)) == 4)
                            || (carBean.carType == 1 && carBean.capOfPerson == 6 && (Integer.valueOf(adultNum) + Integer.valueOf(childrenNum)) == 6)) {
                        AlertDialogUtils.showAlertDialog(getActivity(), getString(R.string.alert_car_full),
                                "继续下单", "更换车型", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        goNext();
                                        dialog.dismiss();
                                    }
                                }, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                    } else {
                        goNext();
                    }


                    map.put("source", source);
                    map.put("begincity", startBean.name);
                    map.put("carstyle", carBean.carDesc);
//                map.put("guestcount", adultNum + childrenNum + "");
//                map.put("luggagecount", luggageNum + "");
//                map.put("drivedays", dayNums + "");
//                map.put("payableamount", carBean.price + "");
                    MobclickAgent.onEventValue(getActivity(), "carnext_oneday", map, carBean.price);
                } else {
                    Bundle bundle1 = new Bundle();//用于统计
                    bundle1.putString("source", "包车下单");
                    startFragment(new FgLogin(), bundle1);
                }
                break;
        }
    }


    private void goNext() {
        FGOrderNew fgOrderNew = new FGOrderNew();
        Bundle bundleCar = new Bundle();
        bundleCar.putString("source", source);
        bundleCar.putString("startCityId", startCityId);
        bundleCar.putString("endCityId", endCityId);
        bundleCar.putString("startDate", startDate);
        bundleCar.putString("endDate", endDate);
        bundleCar.putString("halfDay", halfDay);
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
        bundleCar.putBoolean("isHalfTravel", isHalfTravel);
        bundleCar.putInt("type", 3);
        bundleCar.putString("orderType", "3");
        fgOrderNew.setArguments(bundleCar);
        startFragment(fgOrderNew);
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
                ChooseCarTypeEnum carTypeEnum = ChooseCarTypeEnum.getCarType(bean.carType, bean.carSeat);
                if (carTypeEnum != null) {
                    bean.imgRes = carTypeEnum.imgRes;
                }
                if (isMatchLocal(bean)) {
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

    private boolean isMatchLocal(CarBean bean) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).carType == bean.carType && cars.get(i).seatCategory == bean.carSeat) {
                bean.desc = cars.get(i).carDesc;
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
        showContent();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
