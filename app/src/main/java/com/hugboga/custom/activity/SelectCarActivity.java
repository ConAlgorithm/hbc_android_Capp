package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
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
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.widget.JazzyViewPager;
import com.hugboga.custom.widget.MoneyTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
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
 * Created on 16/8/4.
 */

public class SelectCarActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

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
    @Bind(R.id.child_left)
    TextView childLeft;
    @Bind(R.id.max_luggage_tv)
    TextView maxLuggageTv;
    @Bind(R.id.max_luggage_img)
    ImageView maxLuggageImg;
    @Bind(R.id.max_luggage_tips)
    TextView maxLuggageTips;
    @Bind(R.id.all_money)
    TextView allMoney;
    @Bind(R.id.average_left)
    TextView averageLeft;
    @Bind(R.id.all_money_left)
    TextView allMoneyLeft;
    @Bind(R.id.all_money_info)
    TextView allMoneyInfo;
    @Bind(R.id.childseat_layout)
    RelativeLayout childseatLayout;

    @Bind(R.id.empty_text)
    TextView empty_text;
    @Bind(R.id.man_right)
    TextView manRight;
    @Bind(R.id.man_have)
    TextView manHave;
    @Bind(R.id.child_right)
    TextView childRight;
    @Bind(R.id.max_luggage_content)
    TextView maxLuggageContent;
    @Bind(R.id.days_left)
    TextView daysLeft;
    @Bind(R.id.days_right)
    TextView daysRight;
    @Bind(R.id.all_money_right)
    MoneyTextView allMoneyRight;
    @Bind(R.id.line)
    TextView line;
    @Bind(R.id.average_money)
    MoneyTextView averageMoney;
    @Bind(R.id.all_money_left_text)
    MoneyTextView allMoneyLeftText;
    @Bind(R.id.bottom)
    RelativeLayout bottom;
    @Bind(R.id.call_phone)
    LinearLayout callPhone;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_select_car_new);
        ButterKnife.bind(this);
        initView();
        initHeader();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected void initHeader() {
        headerTitle.setText(R.string.select_city_title);
        source = getIntent().getStringExtra("source");
        headerRightTxt.setVisibility(View.VISIBLE);
        headerRightTxt.setText("常见问题");
        headerRightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_PROBLEM);
                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                startActivity(intent);
            }
        });
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

    protected void initView() {
        getArgs();
        RequestGetCarInfo requestGetCarInfo = new RequestGetCarInfo(this.activity,
                startCityId, endCityId, startDate + " " + serverTime + ":00", endDate + " " + serverTime + ":00", halfDay, adultNum,
                childrenNum, childseatNum, luggageNum, passCities, channelId, null);
        HttpRequestUtils.request(this.activity, requestGetCarInfo, this);
        jazzyPager.setState(null);
        jazzyPager.setOffscreenPageLimit(3);
        jazzyPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Tablet);
        mAdapter = new CarViewpagerAdapter(activity, jazzyPager);
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
        passCityList = (ArrayList<CityBean>) getIntent().getSerializableExtra("passCityList");

        startCityId = this.getIntent().getStringExtra("startCityId");
        endCityId = this.getIntent().getStringExtra("endCityId");
        startDate = this.getIntent().getStringExtra("startDate");
        serverTime = this.getIntent().getStringExtra("serverTime");
        endDate = this.getIntent().getStringExtra("endDate");
        halfDay = this.getIntent().getStringExtra("halfDay");
        adultNum = this.getIntent().getStringExtra("adultNum");
        childrenNum = this.getIntent().getStringExtra("childrenNum");
        childseatNum = this.getIntent().getStringExtra("childseatNum");
        luggageNum = this.getIntent().getStringExtra("luggageNum");
        passCities = this.getIntent().getStringExtra("passCities");

        startCityName = this.getIntent().getStringExtra("startCityName");
        dayNums = this.getIntent().getStringExtra("dayNums");

        startBean = (CityBean) this.getIntent().getSerializableExtra("startBean");
        endBean = (CityBean) this.getIntent().getSerializableExtra("endBean");

        inNum = this.getIntent().getIntExtra("innum", 0);
        outNum = this.getIntent().getIntExtra("outnum", 0);
        isHalfTravel = this.getIntent().getBooleanExtra("isHalfTravel", false);

        orderType = this.getIntent().getStringExtra("orderType");
    }

    int selctIndex = 0;
    List<SelectCarBean> cars;

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestGetCarInfo) {
            CarInfoBean bean = (CarInfoBean) request.getData();
            if (null != bean) {
                cars = bean.cars;
                halfDay = bean.halfDay == 1 ? "1" : "0";
                if (bean.noneCarsState == 1) {
                    coupon_listview_empty.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(GONE);
                    nextBtnClick.setVisibility(GONE);
                    empty_text.setText(bean.noneCarsReason);
                    callPhone.setVisibility(GONE);
                } else if (bean.noneCarsState == 2) {
                    coupon_listview_empty.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(GONE);
                    nextBtnClick.setVisibility(GONE);
                    empty_text.setText(bean.noneCarsReason);
                    callPhone.setVisibility(View.VISIBLE);
                } else {
                    initListData();
                    getMatchCarIndex();
                    showContent();
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
//                nextBtnClick.setText("人数已超限，请更换车型");
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


    private void genNewContent() {
        if (Integer.valueOf(childseatNum) > 0) {
            childseatLayout.setVisibility(View.VISIBLE);
        }
        int mans = Integer.valueOf(adultNum) + Integer.valueOf(childrenNum);
        manHave.setText(mans + "人");
        manRight.setText(" / " + carBean.capOfPerson + "人");
        childRight.setText(childseatNum + "个");

        int maxLuuages = (carBean.capOfLuggage + carBean.capOfPerson)
                - Integer.valueOf(adultNum) - Math.round(Integer.valueOf(childseatNum) * 1.5f)
                - (Integer.valueOf(childrenNum) - Integer.valueOf(childseatNum));
        luggageNum = maxLuuages + "";
        maxLuggageContent.setText(maxLuuages + "件");

        if (halfDay.equalsIgnoreCase("1")) {
            daysLeft.setText("0.5天包车+司导");
        } else {
            daysLeft.setText(dayNums + "天包车+司导");
        }

        daysRight.setText("用车费用￥" + carBean.vehiclePrice + "\n+司导费用￥" + carBean.servicePrice);
        allMoneyRight.setText("￥" + (carBean.vehiclePrice + carBean.servicePrice));
        averageMoney.setText("￥" + (carBean.vehiclePrice + carBean.servicePrice) / mans);
        allMoneyLeftText.setText("￥" + (carBean.vehiclePrice + carBean.servicePrice));
    }

    public void genTotal() {
        carBean = cars.get(selctIndex);
        mans.setText(String.format(getString(R.string.have_mas), carBean.capOfPerson));
        baggages.setText(String.format(getString(R.string.have_baggages), carBean.capOfLuggage));
        carType.setText(carBean.carDesc);
        carContent.setText(carBean.models);
        if (TextUtils.isEmpty(carBean.serviceCityNote)) {
            mans_serviceCityNote.setVisibility(GONE);
        } else {
            mans_serviceCityNote.setVisibility(View.VISIBLE);
            mans_serviceCityNote.setText(carBean.serviceCityNote);
        }

        if (TextUtils.isEmpty(carBean.serviceCityNote)) {
            cars_serviceCityNote.setVisibility(GONE);
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
                    carsMoneyAllInfo.setVisibility(GONE);
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
                view = LayoutInflater.from(this.activity).inflate(R.layout.car_quote_item, null);
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
                    day_line2_money_middle.setVisibility(GONE);
                    day_line2_money_left.setVisibility(GONE);
                    day_line2_money_right.setVisibility(GONE);
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
                    day_line3_money_middle.setVisibility(GONE);
                    day_line3_money_left.setVisibility(GONE);
                    day_line3_money_right.setVisibility(GONE);
                }

                if (dayQuoteBean.longDisPrice != 0) {
                    day_line4_money_middle.setVisibility(View.VISIBLE);
                    day_line4_money_left.setVisibility(View.VISIBLE);
                    day_line4_money_right.setVisibility(View.VISIBLE);
                    day_line4_money_left.setText(getString(R.string.longDisPrice));
                    day_line4_money_right.setText(dayQuoteBean.longDisPrice + "元");
                } else {
                    day_line4_money_middle.setVisibility(GONE);
                    day_line4_money_left.setVisibility(GONE);
                    day_line4_money_right.setVisibility(GONE);
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
                    mansMoneyAllInfo.setVisibility(GONE);
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
                view = LayoutInflater.from(this.activity).inflate(R.layout.car_quote_item, null);
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
                    day_line2_money_middle.setVisibility(GONE);
                    day_line2_money_left.setVisibility(GONE);
                    day_line2_money_right.setVisibility(GONE);
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
                    day_line3_money_middle.setVisibility(GONE);
                    day_line3_money_left.setVisibility(GONE);
                    day_line3_money_right.setVisibility(GONE);
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
        scrollView.setVisibility(GONE);
    }


    private void changeLeftRightStatus() {
        if (selctIndex == 0) {
            left.setVisibility(GONE);
            right.setVisibility(View.VISIBLE);
        } else if (selctIndex < (cars.size() - 1)) {
            right.setVisibility(View.VISIBLE);
            left.setVisibility(View.VISIBLE);
        } else if (selctIndex == (cars.size() - 1)) {
            right.setVisibility(GONE);
            left.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.header_left_btn, R.id.all_money_info, R.id.max_luggage_img, R.id.in_phone, R.id.out_phone, R.id.befer48_tips, R.id.left, R.id.right, R.id.mans_money_show_info, R.id.cars_money_show_info, R.id.next_btn_click})
    public void onClick(View view) {
        HashMap<String, String> map = new HashMap<String, String>();
        switch (view.getId()) {
            case R.id.all_money_info:
                Bundle bundleInfo = new Bundle();
                bundleInfo.putSerializable("carBean", carBean);
                bundleInfo.putString("halfDay", halfDay);
//                startFragment(new FgOrderInfo(), bundleInfo);


                Intent intent = new Intent(activity, OrderInfoActivity.class);
                intent.putExtras(bundleInfo);
                startActivity(intent);
                break;
            case R.id.max_luggage_img:
                startActivity(new Intent(activity, LuggageInfoActivity.class));
                break;
            case R.id.in_phone:
                PhoneInfo.CallDial(this.activity, Constants.CALL_NUMBER_IN);
                break;
            case R.id.out_phone:
                PhoneInfo.CallDial(this.activity, Constants.CALL_NUMBER_IN);
                break;
            case R.id.befer48_tips:
//                Bundle bundle = new Bundle();
//                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_CANCEL);
//                startFragment(new FgActivity(), bundle);

                intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_CANCEL);
                startActivity(intent);

                break;
            case R.id.header_right_txt:
                map.put("source", "选车页面");
                MobclickAgent.onEvent(activity, "callcenter_oneday", map);
                view.setTag("选车页面,calldomestic_oneday,calloverseas_oneday");
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
                if (UserEntity.getUser().isLogin(activity)) {
                    if ((carBean.carType == 1 && carBean.capOfPerson == 4 && (Integer.valueOf(adultNum) + Integer.valueOf(childrenNum)) == 4)
                            || (carBean.carType == 1 && carBean.capOfPerson == 6 && (Integer.valueOf(adultNum) + Integer.valueOf(childrenNum)) == 6)) {
                        AlertDialogUtils.showAlertDialog(activity, getString(R.string.alert_car_full),
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
                } else {
                    startActivity(new Intent(activity, LoginActivity.class));
                }
                break;
        }
    }


    private void goNext() {
//        FGOrderNew fgOrderNew = new FGOrderNew();
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
        bundleCar.putSerializable("carBean", carBean);
        bundleCar.putSerializable("startBean", startBean);
        bundleCar.putSerializable("endBean", endBean);
        bundleCar.putInt("outnum", outNum);
        bundleCar.putInt("innum", inNum);
        bundleCar.putSerializable("passCityList", passCityList);
        bundleCar.putBoolean("isHalfTravel", isHalfTravel);
        bundleCar.putInt("type", 3);
        bundleCar.putString("orderType", "3");
//        fgOrderNew.setArguments(bundleCar);
//        startFragment(fgOrderNew);

        Intent intent = new Intent(activity, OrderNewActivity.class);
        intent.putExtras(bundleCar);
        startActivity(intent);
    }

    private void initListData() {
        carList = new ArrayList<>();
        for(SelectCarBean selectCarBean:cars){
            CarBean carBean = CarUtils.selectCarBeanAdapter(selectCarBean);
            ChooseCarTypeEnum carTypeEnum = ChooseCarTypeEnum.getCarType(carBean.carType, carBean.carSeat);
            if (carTypeEnum != null) {
                carBean.imgRes = carTypeEnum.imgRes;
            }
            if (isMatchLocal(carBean)) {
                carList.add(carBean);
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
