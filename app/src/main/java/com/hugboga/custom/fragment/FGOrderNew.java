package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.MostFitAvailableBean;
import com.hugboga.custom.data.bean.MostFitBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderContact;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.SelectCarBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestMostFit;
import com.hugboga.custom.data.request.RequestSubmitBase;
import com.hugboga.custom.data.request.RequestSubmitDaily;
import com.hugboga.custom.data.request.RequestSubmitPick;
import com.hugboga.custom.data.request.RequestSubmitRent;
import com.hugboga.custom.data.request.RequestSubmitSend;
import com.hugboga.custom.data.request.TrequestTravelFundLogs;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.hugboga.custom.R.id.airport_name;
import static com.hugboga.custom.R.id.man_name;
import static com.hugboga.custom.R.id.pick_name;
import static com.hugboga.custom.R.id.up_address_right;
import static com.hugboga.custom.R.id.up_right;

/**
 * Created  on 16/4/18.
 */

@ContentView(R.layout.fg_order_new)
public class FGOrderNew extends BaseFragment {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.citys_line_title)
    TextView citysLineTitle;
    @Bind(R.id.day_layout)
    LinearLayout day_layout;
    @Bind(R.id.show_day_layout)
    LinearLayout show_day_layout;

    @Bind(R.id.day_show_all)
    TextView day_show_all;


    @Bind(R.id.checkin)
    TextView checkin;
    @Bind(R.id.man_phone_name)
    TextView manPhoneName;
    @Bind(R.id.for_other_man)
    TextView forOtherMan;
    @Bind(R.id.man_phone_layout)
    LinearLayout manPhoneLayout;
    @Bind(R.id.up_left)
    TextView upLeft;
    @Bind(R.id.up_address_left)
    TextView upAddressLeft;

    @Bind(R.id.hotel_phone_text_code_click)
    TextView hotelPhoneTextCodeClick;
    @Bind(R.id.hotel_phone_text)
    EditText hotelPhoneText;
    @Bind(R.id.mark)
    EditText mark;
    @Bind(R.id.coupon_left)
    RadioButton couponLeft;
    @Bind(R.id.coupon_right)
    TextView couponRight;
    @Bind(R.id.dream_left)
    RadioButton dreamLeft;
    @Bind(R.id.insure_left)
    TextView insureLeft;
    @Bind(R.id.insure_right)
    TextView insureRight;
    @Bind(R.id.change_title)
    TextView changeTitle;
    @Bind(R.id.change_detail)
    TextView changeDetail;
    @Bind(R.id.all_money_left)
    TextView allMoneyLeft;
    @Bind(R.id.all_money_left_text)
    TextView allMoneyLeftText;
    @Bind(R.id.all_money_submit_click)
    TextView allMoneySubmitClick;
    @Bind(R.id.all_money_info)
    TextView allMoneyInfo;
    @Bind(R.id.bottom)
    RelativeLayout bottom;
    @Bind(R.id.man_phone)
    TextView manPhone;
    @Bind(R.id.other_phone_name)
    TextView otherPhoneName;
    @Bind(R.id.other_phone)
    TextView otherPhone;
    @Bind(R.id.other_phone_layout)
    LinearLayout otherPhoneLayout;
    @Bind(R.id.pick_name_left)
    TextView pickNameLeft;
    @Bind(pick_name)
    EditText pickName;
    @Bind(R.id.up_right)
    TextView upRight;

    @Bind(R.id.citys_line_title_tips)
    TextView citys_line_title_tips;

    @Bind(R.id.pick_name_layout)
    RelativeLayout pick_name_layout;

    @Bind(R.id.dream_right_tips)
    TextView dream_right_tips;
    @Bind(R.id.sku_title)
    TextView skuTitle;
    @Bind(R.id.sku_day)
    TextView skuDay;
    @Bind(R.id.sku_city_line)
    TextView skuCityLine;
    @Bind(R.id.sku_layout)
    RelativeLayout skuLayout;
    @Bind(R.id.start_hospital_title)
    TextView startHospitalTitle;
    @Bind(R.id.start_hospital_title_tips)
    TextView startHospitalTitleTips;

    @Bind(R.id.end_hospital_title)
    TextView endHospitalTitle;
    @Bind(R.id.end_hospital_title_tips)
    TextView endHospitalTitleTips;
    @Bind(R.id.car_seat)
    TextView carSeat;
    @Bind(R.id.car_seat_tips)
    TextView carSeatTips;
    @Bind(R.id.man_name)
    TextView manName;
    @Bind(R.id.other_name)
    TextView otherName;
    @Bind(R.id.other_layout)
    RelativeLayout otherLayout;
    @Bind(R.id.up_address_right)
    TextView upAddressRight;
    @Bind(R.id.dream_right)
    TextView dreamRight;
    @Bind(R.id.airpost_name_left)
    TextView airpostNameLeft;
    @Bind(airport_name)
    EditText airportName;
    @Bind(R.id.airport_name_layout)
    RelativeLayout airportNameLayout;
    @Bind(R.id.single_no_show_time)
    RelativeLayout singleNoShowTime;
    @Bind(R.id.single_no_show_address)
    RelativeLayout singleNoShowAddress;

    @Override
    protected void initHeader() {
        fgRightBtn.setVisibility(View.VISIBLE);
        fgTitle.setText("确认订单");
        source = getArguments().getString("source");

        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        contactUsersBean = new ContactUsersBean();
        String userName = UserEntity.getUser().getNickname(this.getActivity());
        String userPhone = UserEntity.getUser().getPhone(this.getActivity());
        contactUsersBean.userName = userName;
        contactUsersBean.userPhone = userPhone;
        manName.setText(userName);
        manPhone.setText(userPhone);
    }

    String startCityId;
    String endCityId;
    String startDate;
    String endDate;
    String halfDay;
    String adultNum;
    String childrenNum;
    String childseatNum;
    String luggageNum;
    String passCities;
    String startCityName;
    String dayNums = "0";
    SelectCarBean carBean;

    CityBean startBean;
    CityBean endBean;

    public int inNum = 0;
    public int outNum = 0;

    String orderType = "";
    int type = 1;
    boolean isHalfTravel = false;

    SkuItemBean skuBean;
    String serverDayTime = "";

    String distance = "0";
    CarListBean carListBean;
    ManLuggageBean manLuggageBean;


    private FlightBean flightBean;//航班信息 接机
    private PoiBean poiBean;//达到目的地


    @Override
    protected void initView() {
        passCityList = (ArrayList<CityBean>) getArguments().getSerializable("passCityList");

        manLuggageBean = (ManLuggageBean) getArguments().getParcelable("manLuggageBean");
        carListBean = this.getArguments().getParcelable("carListBean");
        guideCollectId = this.getArguments().getString("guideCollectId");
        carBean = this.getArguments().getParcelable("carBean");

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
        isHalfTravel = this.getArguments().getBoolean("isHalfTravel");

        orderType = this.getArguments().getString("orderType");

        type = this.getArguments().getInt("type");

        skuBean = (SkuItemBean) getArguments().getSerializable("web_sku");
        cityBean = (CityBean) getArguments().getSerializable("web_city");
        serverDayTime = this.getArguments().getString("serverDayTime");

        distance = this.getArguments().getString("distance");
        if (null == distance) {
            distance = "0";
        }

        genType(type);

        requestMostFit();
        requestTravelFund();

        couponLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dreamLeft.setChecked(false);


                }
            }
        });
        dreamLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    couponLeft.setChecked(false);
                }
            }
        });
    }

    //1,包车 2,接机,3,送机,4,单次接送,5,sku
    private void genType(int type) {
        switch (type) {
            case 1:
                genPick();
                break;
            case 2:
                genSend();
                break;
            case 3:
                genDairy();
                break;
            case 4:
                genSingle();
                break;
            case 5:
                genSKU();
                break;
        }
    }


    AirPort airPort;
    boolean isCheckIn = false;
    String serverDate;

    private void genSend() {
        airPort = this.getArguments().getParcelable("airPortBean");
        poiBean = this.getArguments().getParcelable("poiBean");
        adultNum = this.getArguments().getString("adultNum");
        childrenNum = this.getArguments().getString("childrenNum");
        childseatNum = this.getArguments().getString("childseatNum");
        luggageNum = this.getArguments().getString("luggageNum");
        manLuggageBean = this.getArguments().getParcelable("manLuggageBean");
        carListBean = this.getArguments().getParcelable("carListBean");
        type = this.getArguments().getInt("type");
        orderType = this.getArguments().getString("orderType");
        isCheckIn = this.getArguments().getBoolean("needCheckin");
        serverDate = this.getArguments().getString("serverDate");
        serverTime = this.getArguments().getString("serverTime");
        carBean = this.getArguments().getParcelable("carBean");


        citysLineTitle.setText("当地时间" + serverDate + "(" + DateUtils.getWeekOfDate(serverDate) + ")" + "  " + serverTime);
        citys_line_title_tips.setVisibility(View.GONE);


        startHospitalTitle.setText(poiBean.placeName);
        startHospitalTitleTips.setVisibility(View.VISIBLE);
        startHospitalTitleTips.setText(poiBean.placeDetail);

        endHospitalTitle.setText(airPort.airportName);
        endHospitalTitleTips.setVisibility(View.GONE);

        carSeat.setText(carBean.carDesc);
        carSeatTips.setText("(" + "乘坐" + (Integer.valueOf(adultNum) + Integer.valueOf(childrenNum)) + "人,行李箱" + luggageNum + "件,儿童座椅" + childseatNum + "个)");

        airportNameLayout.setVisibility(View.VISIBLE);

        singleNoShowTime.setVisibility(View.GONE);
        singleNoShowAddress.setVisibility(View.GONE);

        checkin.setText("协助办理登机check in");
        checkin.setVisibility(View.VISIBLE);
        pick_name_layout.setVisibility(View.GONE);
        if (isCheckIn) {
            checkin.setVisibility(View.VISIBLE);
        } else {
            checkin.setVisibility(View.GONE);
        }
    }


    private void genPick() {
        flightBean = (FlightBean) this.getArguments().getSerializable(FgCar.KEY_FLIGHT);
        poiBean = (PoiBean) this.getArguments().getSerializable(FgCar.KEY_ARRIVAL);


        carBean = this.getArguments().getParcelable("carBean");
        carListBean = this.getArguments().getParcelable("carListBean");

        adultNum = this.getArguments().getString("adultNum");
        childrenNum = this.getArguments().getString("childrenNum");
        childseatNum = this.getArguments().getString("childseatNum");
        luggageNum = this.getArguments().getString("luggageNum");

        citysLineTitle.setText("当地时间" + flightBean.arrivalTime + "(" + DateUtils.getWeekOfDate(flightBean.depDate) + ")");
        citys_line_title_tips.setText("航班" + flightBean.arrivalAirport.airportCode + " " + flightBean.depAirport.cityName + "-" + flightBean.arrivalAirport.cityName);


        startHospitalTitle.setText(flightBean.arrivalAirport.airportName);
        startHospitalTitleTips.setVisibility(View.GONE);
        endHospitalTitle.setText(poiBean.placeName);
        endHospitalTitleTips.setText(poiBean.placeDetail);

        carSeat.setText(carBean.carDesc);
        carSeatTips.setText("(" + "乘坐" + (Integer.valueOf(adultNum) + Integer.valueOf(childrenNum)) + "人,行李箱" + luggageNum + "件,儿童座椅" + childseatNum + "个)");

        singleNoShowTime.setVisibility(View.GONE);
        singleNoShowAddress.setVisibility(View.GONE);

        if (isCheckIn) {
            checkin.setVisibility(View.VISIBLE);
        } else {
            checkin.setVisibility(View.GONE);
        }
    }

    PoiBean endPoi;
    PoiBean startPoi;

    private void genSingle() {
        endPoi = (PoiBean) this.getArguments().getSerializable("KEY_ARRIVAL");
        startPoi = (PoiBean) this.getArguments().getSerializable("KEY_START");
        startBean = (CityBean) this.getArguments().getSerializable("KEY_CITY");

        serverDate = this.getArguments().getString("serverDate");
        serverTime = this.getArguments().getString("serverTime");


        adultNum = this.getArguments().getString("adultNum");
        childrenNum = this.getArguments().getString("childrenNum");
        childseatNum = this.getArguments().getString("childseatNum");
        luggageNum = this.getArguments().getString("luggageNum");

        citysLineTitle.setText("当地时间" + startDate + "(" + DateUtils.getWeekOfDate(startDate) + ")");

        startHospitalTitle.setText(startPoi.placeName);
        startHospitalTitleTips.setText(startPoi.placeDetail);
        endHospitalTitle.setText(endPoi.placeName);
        endHospitalTitleTips.setText(endPoi.placeDetail);

        singleNoShowTime.setVisibility(View.GONE);
        singleNoShowAddress.setVisibility(View.GONE);

        carSeat.setText(carBean.carDesc);
        carSeatTips.setText("(" + "乘坐" + (Integer.valueOf(adultNum) + Integer.valueOf(childrenNum)) + "人,行李箱" + luggageNum + "件,儿童座椅" + childseatNum + "个)");


        citys_line_title_tips.setVisibility(View.GONE);
        checkin.setVisibility(View.GONE);
        pick_name_layout.setVisibility(View.GONE);
    }

    private void genSKU() {
        skuTitle.setText(skuBean.goodsName);
        skuDay.setText(getString(R.string.sku_days, skuBean.daysCount));
        skuCityLine.setText(skuBean.places);
        skuLayout.setVisibility(View.VISIBLE);
        citysLineTitle.setText("当地时间" + startDate + "(" + DateUtils.getWeekOfDate(startDate) + ")");
        citys_line_title_tips.setVisibility(View.GONE);

        adultNum = this.getArguments().getString("adultNum");
        childrenNum = this.getArguments().getString("childrenNum");
        childseatNum = this.getArguments().getString("childseatNum");
        luggageNum = this.getArguments().getString("luggageNum");

        startHospitalTitle.setVisibility(View.GONE);
        startHospitalTitleTips.setVisibility(View.GONE);
        endHospitalTitle.setVisibility(View.GONE);
        endHospitalTitleTips.setVisibility(View.GONE);
        checkin.setVisibility(View.GONE);
        pick_name_layout.setVisibility(View.GONE);


        carSeat.setText(carBean.carDesc);
        carSeatTips.setText("(" + "乘坐" + (Integer.valueOf(adultNum) + Integer.valueOf(childrenNum)) + "人,行李箱" + luggageNum + "件,儿童座椅" + childseatNum + "个)");

    }

    boolean showAll = false;
    View dayView = null;
    View top_line = null;
    View bottom_line = null;
    TextView textView = null;
    CityBean cityBean;

    private void genDayView(int dayNUms) {
        day_layout.removeAllViews();
        for (int i = 0; i < dayNUms; i++) {
//            public int cityType = 1;// 1 市内 2 周边 3,市外
            dayView = LayoutInflater.from(getContext()).inflate(R.layout.day_order_item, null);
            top_line = dayView.findViewById(R.id.top_line);
            bottom_line = dayView.findViewById(R.id.bottom_line);
            textView = (TextView) dayView.findViewById(R.id.right_text);
            cityBean = passCityList.get(i);
            if (i == 0) {
                top_line.setVisibility(View.INVISIBLE);
            } else if (i == dayNUms - 1) {
                bottom_line.setVisibility(View.INVISIBLE);
            } else {
                top_line.setVisibility(View.VISIBLE);
                bottom_line.setVisibility(View.VISIBLE);
            }
            if (cityBean.cityType == 1) {
                textView.setText("第" + (i + 1) + "天: 住在" + cityBean.name + ",市内游玩");
            } else if (passCityList.get(i).cityType == 2) {
                textView.setText("第" + (i + 1) + "天: 住在" + cityBean.name + ",周边游玩");
            } else if (passCityList.get(i).cityType == 3) {
                textView.setText("第" + (i + 1) + "天: 住在其它城市" + cityBean.name);
            }
            day_layout.addView(dayView);
        }
    }

    //包车界面
    private void genDairy() {
        show_day_layout.setVisibility(View.VISIBLE);

        if (isHalfTravel) {
            citysLineTitle.setText(startBean.name + "-0.5天包车");
            day_show_all.setVisibility(View.GONE);
        } else {
            citysLineTitle.setText(startBean.name + "-" + dayNums + "天包车");
        }
        String startWeekDay = "";
        startWeekDay = DateUtils.getWeekOfDate(startDate);


        String endWeekDay = "";
        endWeekDay = DateUtils.getWeekOfDate(endDate);
        citys_line_title_tips.setText("当地时间" + startDate + "(" + startWeekDay + ") 至" + "  " + endDate + " (" + endWeekDay + ")");


        if (isHalfTravel) {
            dayView = LayoutInflater.from(getContext()).inflate(R.layout.day_order_item, null);
            top_line = dayView.findViewById(R.id.top_line);
            bottom_line = dayView.findViewById(R.id.bottom_line);
            textView = (TextView) dayView.findViewById(R.id.right_text);
            top_line.setVisibility(View.INVISIBLE);
            bottom_line.setVisibility(View.INVISIBLE);
            textView.setText("半天: " + startCityName + "市内");
            day_layout.addView(dayView);
        }

        if (!isHalfTravel && null != passCityList) {
            if (passCityList.size() <= 3) {
                day_show_all.setVisibility(View.GONE);
                genDayView(passCityList.size());
            } else {
                day_show_all.setVisibility(View.VISIBLE);
                day_show_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAll = !showAll;
                        if (showAll) {
                            genDayView(passCityList.size());
                            day_show_all.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_withdraw, 0, 0, 0);
                            day_show_all.setText("收起详情");
                        } else {
                            genDayView(3);
                            day_show_all.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.journey_unfold, 0, 0, 0);
                            day_show_all.setText("展开详情");
                        }

                    }
                });
                genDayView(3);
            }
        }
        adultNum = this.getArguments().getString("adultNum");
        childrenNum = this.getArguments().getString("childrenNum");
        childseatNum = this.getArguments().getString("childseatNum");
        luggageNum = this.getArguments().getString("luggageNum");

        carSeat.setText(carBean.carDesc);
        carSeatTips.setText("(" + "乘坐" + (Integer.valueOf(adultNum) + Integer.valueOf(childrenNum)) + "人,行李箱" + luggageNum + "件,儿童座椅" + childseatNum + "个)");


        startHospitalTitle.setVisibility(View.GONE);
        startHospitalTitleTips.setVisibility(View.GONE);

        endHospitalTitle.setVisibility(View.GONE);
        endHospitalTitleTips.setVisibility(View.GONE);
        checkin.setVisibility(View.GONE);
        pick_name_layout.setVisibility(View.GONE);

    }


    //旅游基金
    String travelFund = "0";
    int money = 0;//旅游基金int
    private void requestTravelFund() {
        TrequestTravelFundLogs request = new TrequestTravelFundLogs(getActivity(), 0);
        HttpRequestUtils.request(getContext(), request, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                TrequestTravelFundLogs trequestTravelFundLogs = (TrequestTravelFundLogs) request;
                TravelFundData travelFundData = trequestTravelFundLogs.getData();
                travelFund = travelFundData.getFundAmount();
                money = Integer.valueOf(travelFund);
                if (0 == money) {
                    dream_right_tips.setVisibility(View.VISIBLE);
                    dream_right_tips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startFragment(new FgTravelFund());
                        }
                    });
                } else {
                    dreamRight.setText("￥" + money);
                    if (dreamLeft.isChecked()) {
                        allMoneyLeftText.setText("￥" + (carBean.price - money + seat1PriceTotal + seat2PriceTotal) + "");
                    }
                    dream_right_tips.setVisibility(View.GONE);
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

    //
//    String useOrderPrice;
//    String priceChannel;// 渠道价格   [必填]
//    String serviceTime; // 服务时间   [必填]
//    String carTypeId; // 1-经济 2-舒适 3-豪华 4-奢华    [必填]
//    String carSeatNum; // 车座数    [必填]
//    String serviceCityId; // 服务城市ID    [必填]
//    String serviceCountryId; // 服务所在国家ID   [必填]
//    String totalDays; // 日租天数，[日租必填]
//    String distance; // 预估路程公里数 [必填]
//    String serviceLocalDays;// 日租市内天数 [日租必填]
//    String serviceNonlocalDays;// 日租市外天数 [日租必填]
//    String expectedCompTime; // 接送机预计完成时间[非日租必填]
    MostFitBean mostFitBean;


    String date4MostFit = null;
    String startCityId4MostFit = null;
    String areaCode4MostFit = null;

    //优惠券
    private void requestMostFit() {
        switch (type) {
            case 1:
                startCityId4MostFit = flightBean.arrivalAirport.cityId + "";
                date4MostFit = flightBean.arrDate + " " + flightBean.arrivalTime + ":00";
                areaCode4MostFit = flightBean.arrivalAirport.areaCode;
                break;
            case 2:
                startCityId4MostFit = poiBean.id + "";
                date4MostFit = serverDate + " " + serverTime + ":00";
                areaCode4MostFit = airPort.areaCode + "";
                break;
            case 3:
                startCityId4MostFit = startCityId;
                date4MostFit = startDate + " 00:00:00";
                areaCode4MostFit = startBean.areaCode + "";
                break;
            case 4:
                date4MostFit = serverDate + " " + serverTime + ":00";
                startCityId4MostFit = startPoi.id + "";
                areaCode4MostFit = startBean.areaCode;
                break;
            case 5:
                startCityId4MostFit = startCityId;
                date4MostFit = startDate + " 00:00:00";
                areaCode4MostFit = startBean.areaCode + "";
                break;

        }

        RequestMostFit requestMostFit = new RequestMostFit(getContext(), carBean.price + "", carBean.price + "",
                date4MostFit,
                carBean.carType + "",
                carBean.seatCategory + "",
                startCityId4MostFit,
                areaCode4MostFit,
                (null == dayNums ? "0" : dayNums) + "",
                distance, inNum + "", outNum + "", (null == dayNums ? "0" : dayNums) + "", orderType);
        HttpRequestUtils.request(getContext(), requestMostFit, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                RequestMostFit requestMostFit1 = (RequestMostFit) request;
                mostFitBean = requestMostFit1.getData();
                if (null == mostFitBean.priceInfo) {
                    couponRight.setText("还没有优惠券");
                    allMoneyLeftText.setText("￥" + (carBean.price + seat1PriceTotal + seat2PriceTotal));
                } else {
                    couponRight.setText((mostFitBean.priceInfo) + "优惠券");
                    allMoneyLeftText.setText("￥" + (mostFitBean.actualPrice + seat1PriceTotal + seat2PriceTotal));
                }
                couponRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FgCoupon fgCoupon = new FgCoupon();
                        Bundle bundle = new Bundle();
                        MostFitAvailableBean mostFitAvailableBean = new MostFitAvailableBean();

                        mostFitAvailableBean.carSeatNum = carBean.seatCategory + "";
                        mostFitAvailableBean.carTypeId = carBean.carType + "";
                        mostFitAvailableBean.distance = distance;
                        mostFitAvailableBean.expectedCompTime = (null == carBean.expectedCompTime) ? "" : carBean.expectedCompTime + "";
                        mostFitAvailableBean.limit = 0 + "";
                        mostFitAvailableBean.offset = 20 + "";
                        mostFitAvailableBean.priceChannel = carBean.price + "";
                        mostFitAvailableBean.useOrderPrice = carBean.price + "";
                        mostFitAvailableBean.serviceCityId = startCityId4MostFit + "";
                        mostFitAvailableBean.serviceCountryId = areaCode4MostFit;
                        mostFitAvailableBean.serviceLocalDays = inNum + "";
                        mostFitAvailableBean.serviceNonlocalDays = outNum + "";
                        mostFitAvailableBean.serviceTime = date4MostFit;
                        mostFitAvailableBean.userId = UserEntity.getUser().getUserId(getContext());
                        mostFitAvailableBean.totalDays = (null == dayNums) ? "0" : dayNums + "";
                        mostFitAvailableBean.orderType = orderType;
                        bundle.putSerializable(Constants.PARAMS_DATA, mostFitAvailableBean);
                        fgCoupon.setArguments(bundle);
                        startFragment(fgCoupon);
                    }
                });
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {
                System.out.print("a发生大幅");
            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                System.out.print("a发生大幅");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
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

    @Override
    public void onFragmentResult(Bundle bundle) {
        String fragmentName = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCountry.class.getSimpleName().equals(fragmentName)) {
            int viewId = bundle.getInt("airportCode");
            TextView codeTv = (TextView) getView().findViewById(viewId);
            if (codeTv != null) {
                String areaCode = bundle.getString(FgChooseCountry.KEY_COUNTRY_CODE);
                codeTv.setText("+" + areaCode);
            }
        } else if (FgPoiSearch.class.getSimpleName().equals(fragmentName)) {
            PoiBean poiBean = (PoiBean) bundle.getSerializable(FgPoiSearch.KEY_ARRIVAL);
            upAddressRight.setText(poiBean.placeName + "\n" + poiBean.placeDetail);
        }
    }

    /**
     * 时间选择器
     */
    public void showTimeSelect() {
        Calendar cal = Calendar.getInstance();
        MyTimePickerDialogListener myTimePickerDialog = new MyTimePickerDialogListener();
        TimePickerDialog datePickerDialog = TimePickerDialog.newInstance(myTimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        datePickerDialog.setAccentColor(getActivity().getResources().getColor(R.color.all_bg_yellow));
        datePickerDialog.show(this.getActivity().getFragmentManager(), "TimePickerDialog");                //显示日期设置对话框
    }

    String serverTime = "09:00";

    class MyTimePickerDialogListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            String hour = String.format("%02d", hourOfDay);
            String minuteStr = String.format("%02d", minute);
            serverTime = hour + ":" + minuteStr;
            upRight.setText(serverTime + "(当地时间)");
        }
    }


    @Override
    public int getBusinessType() {
        mBusinessType = Constants.BUSINESS_TYPE_DAILY;
        setGoodsType(mBusinessType);
        return mBusinessType;
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestSubmitBase) {
//            bringToFront(FgTravel.class, new Bundle());
            String orderNo = ((RequestSubmitBase) request).getData();
            FgChoosePayment.RequestParams requestParams = new FgChoosePayment.RequestParams();
            requestParams.orderId = orderNo;
            if (couponLeft.isChecked()) {
                if (null == couponBean && null != mostFitBean) {
                    requestParams.couponId = mostFitBean.couponId;
                    requestParams.shouldPay = orderBean.orderPrice - Integer.valueOf(mostFitBean.couponPrice);
                } else if (null != couponBean && null == mostFitBean) {
                    requestParams.couponId = couponBean.couponID;
                    requestParams.shouldPay = orderBean.orderPrice - Integer.valueOf(couponBean.price);
                }

            } else {
                requestParams.couponId = "";
                if (TextUtils.isEmpty(travelFund)) {
                    requestParams.shouldPay = orderBean.orderPrice;
                } else {
                    requestParams.shouldPay = orderBean.orderPrice - Integer.valueOf(travelFund);
                }
            }
            requestParams.source = source;
            requestParams.needShowAlert = true;
            startFragment(FgChoosePayment.newInstance(requestParams));
        }

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        MLog.e(errorInfo.toString() + "===========error");
    }

    //TODO;时间太紧 文字先写代码里
    List<OrderContact> contact = new ArrayList<OrderContact>();

    private void checkData() {
        contact.clear();
        if (TextUtils.isEmpty(manName.getText())) {
            ToastUtils.showLong("联系人姓名不能为空!");
            return;
        }
        if (TextUtils.isEmpty(manPhone.getText())) {
            ToastUtils.showLong("联系人电话不能为空!");
            return;
        }
        if(type == 3 || type == 5) {
            if (TextUtils.isEmpty(upAddressRight.getText())) {
                ToastUtils.showLong("上车地点不能为空!");
                return;
            }
        }

        if(type == 1){
            if (TextUtils.isEmpty(pickName.getText())) {
                ToastUtils.showLong("接机牌姓名不能为空!");
                return;
            }
        }


        if (UserEntity.getUser().isLogin(getActivity())) {
            switch (type) {
                case 1:
                    RequestSubmitPick requestSubmitPick = new RequestSubmitPick(getActivity(), getOrderByInput());
                    requestData(requestSubmitPick);
                    break;
                case 2:
                    RequestSubmitSend requestSubmitSend = new RequestSubmitSend(getActivity(), getOrderByInput());
                    requestData(requestSubmitSend);
                    break;
                case 3:
                case 5:
                    RequestSubmitDaily requestSubmitBase = new RequestSubmitDaily(getActivity(), getOrderByInput());
                    requestData(requestSubmitBase);
                    break;
                case 4:
                    RequestSubmitRent requestSubmitRent = new RequestSubmitRent(getActivity(), getOrderByInput());
                    requestData(requestSubmitRent);
                    break;
            }
            doUMengStatistic();
        } else {
            Bundle bundle = new Bundle();//用于统计
            bundle.putString("source", "包车下单");
            startFragment(new FgLogin(), bundle);
        }
    }

    private void doUMengStatistic() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("source", source);
//        map.put("begincity", startBean.name);
//        map.put("carstyle", carBean.carDesc);
//        if (checkboxOther.isChecked()) {
//            map.put("forother", "是");
//        } else {
//            map.put("forother", "否");
//        }
//        map.put("guestcount", adultNum + childrenNum + "");
//        map.put("luggagecount", luggageNum + "");
//        map.put("drivedays", dayNums + "");
//        map.put("payableamount", carBean.price + "");
        MobclickAgent.onEventValue(getActivity(), "submitorder_oneday", map, carBean.price);
    }

    ContactUsersBean contactUsersBean = null;
    CouponBean couponBean;

    public void onEventMainThread(EventAction action) {
        if (action.getType() == EventType.CONTACT_BACK) {
            contactUsersBean = (ContactUsersBean) action.getData();
            if (!TextUtils.isEmpty(contactUsersBean.userName)) {
                manName.setText(contactUsersBean.userName);
                manPhone.setText(contactUsersBean.phoneCode + " " + contactUsersBean.userPhone);
            }

            if (contactUsersBean.isForOther) {
                otherLayout.setVisibility(View.VISIBLE);
                otherName.setText(contactUsersBean.otherName);
                otherPhone.setText(contactUsersBean.otherphoneCode + " " + contactUsersBean.otherPhone);
            }
        } else if (action.getType() == EventType.SELECT_COUPON_BACK) {
            couponBean = (CouponBean) action.getData();
            couponRight.setText(couponBean.price + "优惠券");
        }
    }

    private String getServiceEndTime(String date, int day) {
        try {
            String[] ymd = date.split("-");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(ymd[0]), Integer.valueOf(ymd[1]) - 1, Integer.valueOf(ymd[2]));
            calendar.add(Calendar.DAY_OF_YEAR, day);
            return DateUtils.dateDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            MLog.e("解析时间格式错误", e);
        }
        return null;
    }


    //TODO 需要后期优化
    //SKU参数
    private OrderBean getSKUOrderByInput() {
        orderBean = new OrderBean();//订单

        if(!TextUtils.isEmpty(guideCollectId)) {
            orderBean.guideCollectId = guideCollectId;
        }

        orderBean.orderType = 5;
        orderBean.goodsNo = skuBean.goodsNo;
        orderBean.lineSubject = skuBean.goodsName;
        orderBean.lineDescription = skuBean.salePoints;
        orderBean.orderGoodsType = skuBean.goodsType;
        orderBean.serviceTime = startDate;//日期
        orderBean.serviceStartTime = serverTime + ":00";//时间
        orderBean.serviceEndTime = getServiceEndTime(startDate, skuBean.daysCount - 1);
        orderBean.distance = distance;//距离
//        orderBean.expectedCompTime = 0;//耗时
        orderBean.carDesc = carBean.carDesc;//车型描述
        orderBean.carType = carBean.carType;//车型
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.orderPrice = carBean.price;
        orderBean.priceMark = carBean.pricemark;
        orderBean.urgentFlag = carBean.urgentFlag;
        orderBean.adult = Integer.valueOf(adultNum);//成人数
        orderBean.child = Integer.valueOf(childrenNum);//儿童数
        orderBean.contactName = "";
        orderBean.contact = new ArrayList<OrderContact>();
        OrderContact orderContact = new OrderContact();
        orderContact.areaCode = "+86";
        orderContact.tel = "";
        orderBean.contact.add(orderContact);
        orderBean.memo = mark.getText().toString().trim();
        if (startBean != null) {
            orderBean.startAddress = startBean.placeName;
            orderBean.startAddressDetail = "";
            orderBean.startLocation = startBean.location;
        }
        orderBean.serviceCityId = skuBean.depCityId;
        orderBean.serviceCityName = skuBean.depCityName;
        //出发地，到达地经纬度
        orderBean.terminalLocation = null;
        orderBean.destAddress = skuBean.arrCityName;
        orderBean.serviceEndCityid = skuBean.arrCityId;
        orderBean.serviceEndCityName = skuBean.arrCityName;
        orderBean.totalDays = skuBean.daysCount;
        orderBean.oneCityTravel = skuBean.goodsType == 3 ? 1 : 2;//1：市内畅游  2：跨城市
        orderBean.isHalfDaily = 0;
        orderBean.inTownDays = skuBean.goodsType == 3 ? skuBean.daysCount : 0;
        orderBean.outTownDays = skuBean.goodsType == 3 ? 0 : skuBean.daysCount;
        orderBean.skuPoi = "";
        orderBean.stayCityListStr = getPassCityStr();
        orderBean.priceChannel = carBean.price + "";
        orderBean.userName = manName.getText().toString();
        orderBean.userRemark = mark.getText().toString();


        StringBuffer userExJson = new StringBuffer();
        userExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.userPhone)) {
            userExJson.append("{name:\"" + contactUsersBean.userName + "\",areaCode:\"" + (null == contactUsersBean.phoneCode ? "+86" : contactUsersBean.phoneCode) + "\",mobile:\"" + contactUsersBean.userPhone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user1Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user1Name + "\",areaCode:\"" + (null == contactUsersBean.phone1Code ? "+86" : contactUsersBean.phone1Code) + "\",mobile:\"" + contactUsersBean.user1Phone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user2Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user2Name + "\",areaCode:\"" + (null == contactUsersBean.phone2Code ? "+86" : contactUsersBean.phone2Code) + "\",mobile:\"" + contactUsersBean.user2Phone + "\"}");
        }
        userExJson.append("]");
        orderBean.userEx = userExJson.toString();


        StringBuffer realUserExJson = new StringBuffer();
        realUserExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.otherName)) {
            realUserExJson.append("{name:\"" + contactUsersBean.otherName + "\",areaCode:\"" + contactUsersBean.otherphoneCode + "\",mobile:\"" + contactUsersBean.otherPhone + "\"}");
        }
        realUserExJson.append("]");
        orderBean.realUserEx = realUserExJson.toString();


        return orderBean;
    }

    private String getPassCityStr() {
        String passCity = "";
        passCity += skuBean.depCityId + "-0";
        for (CityBean city : skuBean.passCityList) {
            passCity += "," + city.cityId + "-0";
        }
        passCity += "," + skuBean.arrCityId + "-0";
        return passCity;
    }


    OrderBean orderBean;
    ArrayList<CityBean> passCityList;

    String guideCollectId = null;

    //包车参数
    private OrderBean getDayOrderByInput() {
        orderBean = new OrderBean();//订单

        orderBean.adult = Integer.valueOf(adultNum);
        orderBean.carDesc = carBean.carDesc;
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.carType = carBean.carType;
        orderBean.child = Integer.valueOf(childrenNum);

        orderBean.destAddress = endCityId;
        orderBean.destAddressDetail = endBean.placeName;

        orderBean.priceMark = carBean.pricemark;

        orderBean.contact = contact;
        orderBean.serviceStartTime = serverTime + ":00";
        orderBean.serviceTime = startDate;

        orderBean.serviceEndTime = endDate;
        orderBean.outTownDays = outNum;
        orderBean.inTownDays = inNum;

        orderBean.oneCityTravel = outNum == 0 ? 1 : 2;//1：市内畅游  2：跨城市

        orderBean.serviceAddressTel = hotelPhoneText.getText().toString();
        orderBean.serviceAreaCode = hotelPhoneTextCodeClick.getText().toString();

        orderBean.orderType = 1;

        orderBean.serviceCityId = startBean.cityId;
        orderBean.serviceEndCityid = endBean.cityId;
        orderBean.serviceCityName = startBean.name;
        orderBean.serviceEndCityName = endBean.name;

        orderBean.totalDays = (inNum + outNum);

        orderBean.isHalfDaily = isHalfTravel ? 1 : 0;


        orderBean.startAddress = upRight.getText().toString();

        orderBean.startAddressDetail = "";//upSiteText.getText().toString();


        orderBean.destAddressDetail = upRight.getText().toString();

        orderBean.userName = manName.getText().toString();
        orderBean.stayCityListStr = passCities;
        orderBean.userRemark = mark.getText().toString();

        orderBean.serviceDepartTime = serverTime;

        orderBean.priceChannel = carBean.price + "";
        orderBean.childSeatNum = childseatNum;
        orderBean.luggageNum = luggageNum;

        orderBean.realUserName = contactUsersBean.otherName;
        orderBean.realAreaCode = contactUsersBean.otherphoneCode;
        orderBean.realMobile = contactUsersBean.otherPhone;
        if (contactUsersBean.isForOther) {
            orderBean.isRealUser = "2";
        } else {
            orderBean.isRealUser = "1";
        }
        orderBean.realSendSms = contactUsersBean.isSendMessage ? "1" : "0";

        if (dreamLeft.isChecked()) {
            orderBean.travelFund = travelFund;
            orderBean.orderPrice = carBean.price - Integer.valueOf(travelFund);
        } else {
            if (null == couponBean && null != mostFitBean) {
                orderBean.coupId = mostFitBean.couponId;
                orderBean.coupPriceInfo = mostFitBean.couponPrice + "";
                orderBean.orderPrice = (carBean.price - mostFitBean.couponPrice);
            } else if (null != couponBean && null == mostFitBean) {
                orderBean.coupId = couponBean.couponID;
                orderBean.coupPriceInfo = couponBean.price;
                orderBean.orderPrice = carBean.price - Integer.valueOf(couponBean.price);
            }
        }

        if(!TextUtils.isEmpty(guideCollectId)) {
            orderBean.guideCollectId = guideCollectId;
        }

        StringBuffer userExJson = new StringBuffer();
        userExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.userPhone)) {
            userExJson.append("{name:\"" + contactUsersBean.userName + "\",areaCode:\"" + (null == contactUsersBean.phoneCode ? "+86" : contactUsersBean.phoneCode) + "\",mobile:\"" + contactUsersBean.userPhone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user1Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user1Name + "\",areaCode:\"" + (null == contactUsersBean.phone1Code ? "+86" : contactUsersBean.phone1Code) + "\",mobile:\"" + contactUsersBean.user1Phone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user2Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user2Name + "\",areaCode:\"" + (null == contactUsersBean.phone2Code ? "+86" : contactUsersBean.phone2Code) + "\",mobile:\"" + contactUsersBean.user2Phone + "\"}");
        }
        userExJson.append("]");
        orderBean.userEx = userExJson.toString();


        StringBuffer realUserExJson = new StringBuffer();
        realUserExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.otherName)) {
            realUserExJson.append("{name:\"" + contactUsersBean.otherName + "\",areaCode:\"" + contactUsersBean.otherphoneCode + "\",mobile:\"" + contactUsersBean.otherPhone + "\"}");
        }
        realUserExJson.append("]");
        orderBean.realUserEx = realUserExJson.toString();
        return orderBean;
    }


    int seat1PriceTotal;
    int seat2PriceTotal;

    private OrderBean getPickOrderByInput() {
        orderBean = new OrderBean();//订单

        orderBean.flight = flightBean.flightNo;
        orderBean.flightBean = flightBean;
        orderBean.startAddress = flightBean.arrivalAirport.airportName;
        //出发地，到达地经纬度
        orderBean.startLocation = flightBean.arrivalAirport.location;
        orderBean.terminalLocation = poiBean.location;

        orderBean.carDesc = carBean.carDesc;

        orderBean.destAddress = poiBean.placeName;
        orderBean.destAddressDetail = poiBean.placeDetail;

        orderBean.serviceCityId = flightBean.arrivalAirport.cityId;
        orderBean.serviceTime = flightBean.arrDate + " " + flightBean.arrivalTime + ":00";
        orderBean.brandSign = pickName.getText().toString();

        orderBean.flightNo = flightBean.flightNo;
        orderBean.flightFlyTimeL = flightBean.arrDate;
        orderBean.flightArriveTimeL = flightBean.arrivalTime;
        orderBean.flightAirportBuiding = flightBean.arrivalAirport.airportName;
        orderBean.flightAirportCode = flightBean.arrivalAirport.airportCode;
        orderBean.flightAirportName = flightBean.arrAirportName;
        orderBean.flightDestCode = flightBean.depAirportCode;
        orderBean.flightDestName = flightBean.depAirportName;

        if(null != carListBean.additionalServicePrice.pickupSignPrice) {
            orderBean.priceFlightBrandSign = carListBean.additionalServicePrice.pickupSignPrice;
        }


        orderBean.isFlightSign = carListBean.supportBanner ? "1" : "0";


        orderBean.flightBrandSign = pickName.getText().toString();

        orderBean.adult = Integer.valueOf(adultNum);
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.carType = carBean.carType;
        orderBean.child = Integer.valueOf(childrenNum);

        orderBean.distance = distance;

        orderBean.priceMark = carBean.pricemark;

        orderBean.serviceAddressTel = hotelPhoneText.getText().toString();
        orderBean.serviceAreaCode = hotelPhoneTextCodeClick.getText().toString();


        orderBean.userName = manName.getText().toString();
        orderBean.stayCityListStr = passCities;
        orderBean.userRemark = mark.getText().toString();

        orderBean.serviceDepartTime = serverTime;

        orderBean.priceChannel = carBean.price + "";
        orderBean.childSeatNum = childseatNum;
        orderBean.luggageNum = luggageNum;

        orderBean.realUserName = contactUsersBean.otherName;
        orderBean.realAreaCode = contactUsersBean.otherphoneCode;
        orderBean.realMobile = contactUsersBean.otherPhone;
        if (contactUsersBean.isForOther) {
            orderBean.isRealUser = "2";
        } else {
            orderBean.isRealUser = "1";
        }
        orderBean.realSendSms = contactUsersBean.isSendMessage ? "1" : "0";

        if (dreamLeft.isChecked()) {
            orderBean.travelFund = travelFund;
        } else {
            if (null == couponBean && null != mostFitBean) {
                orderBean.coupId = mostFitBean.couponId;
                orderBean.coupPriceInfo = mostFitBean.couponPrice + "";
                orderBean.priceActual = mostFitBean.actualPrice+"";
            } else if (null != couponBean && null == mostFitBean) {
                orderBean.coupId = couponBean.couponID;
                orderBean.coupPriceInfo = couponBean.price;
                orderBean.priceActual = couponBean.actualPrice+"";
            }
        }
        orderBean.expectedCompTime = carBean.expectedCompTime;

        if(!TextUtils.isEmpty(guideCollectId)) {
            orderBean.guideCollectId = guideCollectId;
        }

        StringBuffer userExJson = new StringBuffer();
        userExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.userPhone)) {
            userExJson.append("{name:\"" + contactUsersBean.userName + "\",areaCode:\"" + (null == contactUsersBean.phoneCode ? "+86" : contactUsersBean.phoneCode) + "\",mobile:\"" + contactUsersBean.userPhone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user1Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user1Name + "\",areaCode:\"" + (null == contactUsersBean.phone1Code ? "+86" : contactUsersBean.phone1Code) + "\",mobile:\"" + contactUsersBean.user1Phone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user2Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user2Name + "\",areaCode:\"" + (null == contactUsersBean.phone2Code ? "+86" : contactUsersBean.phone2Code) + "\",mobile:\"" + contactUsersBean.user2Phone + "\"}");
        }
        userExJson.append("]");
        orderBean.userEx = userExJson.toString();

        int seat1Count = (manLuggageBean.childSeats >= 1 ? 1 : 0);
        int seat2Count = (manLuggageBean.childSeats >= 1 ? (manLuggageBean.childSeats - 1) : 0);
        int seat1Price = 0;
        int seat2Price = 0;
        if (null == carListBean.additionalServicePrice.childSeatPrice1
                && null == carListBean.additionalServicePrice.childSeatPrice2) {
            orderBean.orderPrice  = carBean.price;
            orderBean.childSeatStr = "";
            orderBean.priceChannel = carBean.price + "";
        } else {
            if(manLuggageBean.childSeats != 0) {
                if (null != carListBean.additionalServicePrice.childSeatPrice1) {
                    seat1Price = Integer.valueOf(carListBean.additionalServicePrice.childSeatPrice1);
                }
                if (null != carListBean.additionalServicePrice.childSeatPrice2) {
                    seat2Price = Integer.valueOf(carListBean.additionalServicePrice.childSeatPrice2);
                }
                StringBuffer childSeat = new StringBuffer();
                childSeat.append("{");
                childSeat.append("\"").append("childSeatPrice1\":" + seat1Price + ",");
                childSeat.append("\"").append("childSeatPrice2\":" + seat2Price + ",");
                childSeat.append("\"").append("childSeatPrice1Count\":" + seat1Count + ",");
                childSeat.append("\"").append("childSeatPrice2Count\":" + seat2Count + "");
                childSeat.append("}");
                seat1PriceTotal = seat1Price * seat1Count;
                seat2PriceTotal = seat2Price * seat2Count;

                orderBean.orderPrice = carBean.price + seat1PriceTotal + seat2PriceTotal;
                orderBean.priceChannel = (carBean.price + seat1PriceTotal + seat2PriceTotal) + "";
                orderBean.childSeatStr = childSeat.toString();
            }else{
                orderBean.orderPrice  = carBean.price;
                orderBean.childSeatStr = "";
                orderBean.priceChannel = carBean.price + "";
            }
        }


        StringBuffer realUserExJson = new StringBuffer();
        realUserExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.otherName)) {
            realUserExJson.append("{name:\"" + contactUsersBean.otherName + "\",areaCode:\"" + contactUsersBean.otherphoneCode + "\",mobile:\"" + contactUsersBean.otherPhone + "\"}");
        }
        realUserExJson.append("]");
        orderBean.realUserEx = realUserExJson.toString();
        return orderBean;
    }


    private OrderBean getSingleOrderByInput() {
        orderBean = new OrderBean();//订单
        orderBean.adult = Integer.valueOf(adultNum);
        orderBean.carDesc = carBean.carDesc;
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.carType = carBean.carType;
        orderBean.child = Integer.valueOf(childrenNum);
        orderBean.destAddress = endCityId;
        orderBean.serviceCityId = Integer.valueOf(startCityId);
        orderBean.serviceEndCityid = Integer.valueOf(endCityId);
        orderBean.serviceCityName = startCityName;
        orderBean.serviceEndCityName = endCityId;
        orderBean.contact = contact;
        orderBean.serviceStartTime = serverTime + ":00";
        orderBean.serviceTime = serverDate + " " + serverTime + ":00";

        orderBean.startAddress = startPoi.placeName;
        orderBean.startAddressDetail = startPoi.placeDetail;
        orderBean.startLocation = startPoi.location;

        orderBean.destAddress = endPoi.placeName;
        orderBean.destAddressDetail = endPoi.placeDetail;
        orderBean.terminalLocation = endPoi.location;
        orderBean.distance = distance;

        orderBean.priceMark = carBean.pricemark;

        orderBean.serviceAddressTel = hotelPhoneText.getText().toString();
        orderBean.serviceAreaCode = hotelPhoneTextCodeClick.getText().toString();


        orderBean.userName = manName.getText().toString();
        orderBean.stayCityListStr = passCities;
        orderBean.userRemark = mark.getText().toString();

        orderBean.serviceDepartTime = serverTime;

        orderBean.priceChannel = (carBean.price + seat1PriceTotal + seat2PriceTotal) + "";
        orderBean.childSeatNum = childseatNum;
        orderBean.luggageNum = luggageNum;

        orderBean.realUserName = contactUsersBean.otherName;
        orderBean.realAreaCode = contactUsersBean.otherphoneCode;
        orderBean.realMobile = contactUsersBean.otherPhone;
        if (contactUsersBean.isForOther) {
            orderBean.isRealUser = "2";
        } else {
            orderBean.isRealUser = "1";
        }
        orderBean.realSendSms = contactUsersBean.isSendMessage ? "1" : "0";

        if (dreamLeft.isChecked()) {
            orderBean.travelFund = travelFund;
        } else {
            if (null == couponBean && null != mostFitBean) {
                orderBean.coupId = mostFitBean.couponId;
                orderBean.coupPriceInfo = mostFitBean.couponPrice + "";
                orderBean.priceActual = mostFitBean.actualPrice+"";
            } else if (null != couponBean && null == mostFitBean) {
                orderBean.coupId = couponBean.couponID;
                orderBean.coupPriceInfo = couponBean.price;
                orderBean.priceActual = couponBean.actualPrice+"";
            }
        }
        orderBean.expectedCompTime = carBean.expectedCompTime;
        if(!TextUtils.isEmpty(guideCollectId)) {
            orderBean.guideCollectId = guideCollectId;
        }

        StringBuffer userExJson = new StringBuffer();
        userExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.userPhone)) {
            userExJson.append("{name:\"" + contactUsersBean.userName + "\",areaCode:\"" + (null == contactUsersBean.phoneCode ? "+86" : contactUsersBean.phoneCode) + "\",mobile:\"" + contactUsersBean.userPhone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user1Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user1Name + "\",areaCode:\"" + (null == contactUsersBean.phone1Code ? "+86" : contactUsersBean.phone1Code) + "\",mobile:\"" + contactUsersBean.user1Phone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user2Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user2Name + "\",areaCode:\"" + (null == contactUsersBean.phone2Code ? "+86" : contactUsersBean.phone2Code) + "\",mobile:\"" + contactUsersBean.user2Phone + "\"}");
        }
        userExJson.append("]");
        orderBean.userEx = userExJson.toString();

        int seat1Count = (manLuggageBean.childSeats >= 1 ? 1 : 0);
        int seat2Count = (manLuggageBean.childSeats >= 1 ? (manLuggageBean.childSeats - 1) : 0);
        int seat1Price = 0;
        int seat2Price = 0;
        if (null == carListBean.additionalServicePrice.childSeatPrice1
                && null == carListBean.additionalServicePrice.childSeatPrice2) {
            orderBean.orderPrice  = carBean.price;
            orderBean.childSeatStr = "";
            orderBean.priceChannel = carBean.price + "";
        } else {
            if(manLuggageBean.childSeats != 0) {
                if (null != carListBean.additionalServicePrice.childSeatPrice1) {
                    seat1Price = Integer.valueOf(carListBean.additionalServicePrice.childSeatPrice1);
                }
                if (null != carListBean.additionalServicePrice.childSeatPrice2) {
                    seat2Price = Integer.valueOf(carListBean.additionalServicePrice.childSeatPrice2);
                }
                StringBuffer childSeat = new StringBuffer();
                childSeat.append("{");
                childSeat.append("\"").append("childSeatPrice1\":" + seat1Price + ",");
                childSeat.append("\"").append("childSeatPrice2\":" + seat2Price + ",");
                childSeat.append("\"").append("childSeatPrice1Count\":" + seat1Count + ",");
                childSeat.append("\"").append("childSeatPrice2Count\":" + seat2Count + "");
                childSeat.append("}");
                seat1PriceTotal = seat1Price * seat1Count;
                seat2PriceTotal = seat2Price * seat2Count;

                orderBean.orderPrice = carBean.price + seat1PriceTotal + seat2PriceTotal;
                orderBean.priceChannel = (carBean.price + seat1PriceTotal + seat2PriceTotal) + "";
                orderBean.childSeatStr = childSeat.toString();
            }else{
                orderBean.orderPrice  = carBean.price;
                orderBean.childSeatStr = "";
                orderBean.priceChannel = carBean.price + "";
            }
        }

        orderBean.orderPrice = carBean.price + seat1PriceTotal + seat2PriceTotal;

        orderBean.priceFlightBrandSign = "";


        StringBuffer realUserExJson = new StringBuffer();
        realUserExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.otherName)) {
            realUserExJson.append("{name:\"" + contactUsersBean.otherName + "\",areaCode:\"" + contactUsersBean.otherphoneCode + "\",mobile:\"" + contactUsersBean.otherPhone + "\"}");
        }
        realUserExJson.append("]");
        orderBean.realUserEx = realUserExJson.toString();
        return orderBean;
    }

    private OrderBean getSendOrderByInput() {
        orderBean = new OrderBean();//订单

        orderBean.serviceAreaCode = hotelPhoneTextCodeClick.getText().toString();
        orderBean.serviceAddressTel = hotelPhoneText.getText().toString();
        orderBean.urgentFlag = carBean.urgentFlag;

        orderBean.expectedCompTime = carBean.expectedCompTime;

        if(!TextUtils.isEmpty(guideCollectId)) {
            orderBean.guideCollectId = guideCollectId;
        }
        orderBean.orderType = type;
        orderBean.carType = carBean.carType;
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.carDesc = carBean.carDesc;
        orderBean.contactName = manName.getText().toString();
        orderBean.memo = mark.getText().toString().trim();
        orderBean.childSeatNum = childseatNum;
        orderBean.luggageNum = luggageNum;
        orderBean.isCheckin = isCheckIn ? "1" : "0";

        orderBean.flightNo = airportName.getText().toString();
        orderBean.expectedCompTime = carBean.expectedCompTime;

        orderBean.destAddressPoi = airPort.location;


        orderBean.startAddress = poiBean.placeName;
        orderBean.startAddressDetail = poiBean.placeDetail;
        //出发地，到达地经纬度
        orderBean.startLocation = poiBean.location;
        orderBean.terminalLocation = poiBean.location;
        orderBean.priceMark = carBean.pricemark;
        orderBean.destAddress = airPort.airportName;
        orderBean.flightAirportCode = airPort.airportCode;
        orderBean.serviceCityId = airPort.cityId;
        orderBean.serviceTime = serverDate + " " + serverTime + ":00";

        orderBean.adult = Integer.valueOf(adultNum);
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.carType = carBean.carType;
        orderBean.child = Integer.valueOf(childrenNum);

        orderBean.userRemark = mark.getText().toString();
        orderBean.distance = carListBean.distance+"";

        orderBean.realUserName = contactUsersBean.otherName;
        orderBean.realAreaCode = contactUsersBean.otherphoneCode;
        orderBean.realMobile = contactUsersBean.otherPhone;
        if (contactUsersBean.isForOther) {
            orderBean.isRealUser = "2";
        } else {
            orderBean.isRealUser = "1";
        }
        orderBean.realSendSms = contactUsersBean.isSendMessage ? "1" : "0";

        if (dreamLeft.isChecked()) {
            orderBean.travelFund = travelFund;
        } else {
            if (null == couponBean && null != mostFitBean) {
                orderBean.coupId = mostFitBean.couponId;
                orderBean.coupPriceInfo = mostFitBean.couponPrice + "";
                orderBean.priceActual = mostFitBean.actualPrice + "";
            } else if (null != couponBean && null == mostFitBean) {
                orderBean.coupId = couponBean.couponID;
                orderBean.coupPriceInfo = couponBean.price;
                orderBean.priceActual = couponBean.actualPrice + "";
            }
        }

        StringBuffer userExJson = new StringBuffer();
        userExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.userPhone)) {
            userExJson.append("{name:\"" + contactUsersBean.userName + "\",areaCode:\"" + (null == contactUsersBean.phoneCode ? "+86" : contactUsersBean.phoneCode) + "\",mobile:\"" + contactUsersBean.userPhone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user1Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user1Name + "\",areaCode:\"" + (null == contactUsersBean.phone1Code ? "+86" : contactUsersBean.phone1Code) + "\",mobile:\"" + contactUsersBean.user1Phone + "\"}");
        }

        if (!TextUtils.isEmpty(contactUsersBean.user2Phone)) {
            userExJson.append(",{name:\"" + contactUsersBean.user2Name + "\",areaCode:\"" + (null == contactUsersBean.phone2Code ? "+86" : contactUsersBean.phone2Code) + "\",mobile:\"" + contactUsersBean.user2Phone + "\"}");
        }
        userExJson.append("]");
        orderBean.userEx = userExJson.toString();

        int seat1Count = (manLuggageBean.childSeats >= 1 ? 1 : 0);
        int seat2Count = (manLuggageBean.childSeats >= 1 ? (manLuggageBean.childSeats - 1) : 0);
        int seat1Price = 0;
        int seat2Price = 0;
        if (null == carListBean.additionalServicePrice.childSeatPrice1
                && null == carListBean.additionalServicePrice.childSeatPrice2) {
            orderBean.orderPrice  = carBean.price;
            orderBean.childSeatStr = "";
            orderBean.priceChannel = carBean.price + "";
        } else {
            if(manLuggageBean.childSeats != 0) {
                if (null != carListBean.additionalServicePrice.childSeatPrice1) {
                    seat1Price = Integer.valueOf(carListBean.additionalServicePrice.childSeatPrice1);
                }
                if (null != carListBean.additionalServicePrice.childSeatPrice2) {
                    seat2Price = Integer.valueOf(carListBean.additionalServicePrice.childSeatPrice2);
                }
                StringBuffer childSeat = new StringBuffer();
                childSeat.append("{");
                childSeat.append("\"").append("childSeatPrice1\":" + seat1Price + ",");
                childSeat.append("\"").append("childSeatPrice2\":" + seat2Price + ",");
                childSeat.append("\"").append("childSeatPrice1Count\":" + seat1Count + ",");
                childSeat.append("\"").append("childSeatPrice2Count\":" + seat2Count + "");
                childSeat.append("}");
                seat1PriceTotal = seat1Price * seat1Count;
                seat2PriceTotal = seat2Price * seat2Count;

                orderBean.orderPrice = carBean.price + seat1PriceTotal + seat2PriceTotal;
                orderBean.priceChannel = (carBean.price + seat1PriceTotal + seat2PriceTotal) + "";
                orderBean.childSeatStr = childSeat.toString();
            }else{
                orderBean.orderPrice  = carBean.price;
                orderBean.childSeatStr = "";
                orderBean.priceChannel = carBean.price + "";
            }
        }

        orderBean.orderPrice = isCheckIn ? (carBean.price + Integer.valueOf(carListBean.additionalServicePrice.checkInPrice)) + seat1PriceTotal + seat2PriceTotal : carBean.price + seat1PriceTotal + seat2PriceTotal;
        orderBean.checkInPrice = isCheckIn ? Integer.valueOf(carListBean.additionalServicePrice.checkInPrice) : null;
        orderBean.priceChannel = isCheckIn ? "" + (carBean.price + Integer.valueOf(carListBean.additionalServicePrice.checkInPrice) + seat1PriceTotal + seat2PriceTotal) : "" + (carBean.price + seat1PriceTotal + seat2PriceTotal);

        orderBean.flightAirportCode = airPort.airportCode;
        orderBean.flightAirportName = airPort.airportName;


        StringBuffer realUserExJson = new StringBuffer();
        realUserExJson.append("[");

        if (!TextUtils.isEmpty(contactUsersBean.otherName)) {
            realUserExJson.append("{name:\"" + contactUsersBean.otherName + "\",areaCode:\"" + contactUsersBean.otherphoneCode + "\",mobile:\"" + contactUsersBean.otherPhone + "\"}");
        }
        realUserExJson.append("]");
        orderBean.realUserEx = realUserExJson.toString();
        return orderBean;
    }

    private OrderBean getOrderByInput() {
        switch (type) {
            case 1:
                orderBean = getPickOrderByInput();
                break;
            case 2:
                orderBean = getSendOrderByInput();
                break;
            case 3:
                orderBean = getDayOrderByInput();
                break;
            case 4:
                orderBean = getSingleOrderByInput();
                break;
            case 5:
                orderBean = getSKUOrderByInput();
                break;
        }
        return orderBean;
    }

    private void startArrivalSearch(int cityId, String location) {
        if (location != null) {
            FgPoiSearch fg = new FgPoiSearch();
            Bundle bundle = new Bundle();
            bundle.putInt(FgPoiSearch.KEY_CITY_ID, cityId);
            bundle.putString(FgPoiSearch.KEY_LOCATION, location);
            startFragment(fg, bundle);
        }
    }

    @OnClick({R.id.all_money_submit_click, R.id.other_phone_layout, R.id.other_phone_name, R.id.for_other_man, man_name, R.id.man_phone, R.id.man_phone_layout, up_right, up_address_right, R.id.hotel_phone_text_code_click, R.id.hotel_phone_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.man_phone_layout:
            case R.id.for_other_man:
            case R.id.other_phone_layout:
                FgChooseOther fgChooseOther = new FgChooseOther();
                Bundle bundle = new Bundle();
                bundle.putParcelable("contactUsersBean", contactUsersBean);
                fgChooseOther.setArguments(bundle);
                startFragment(fgChooseOther);
                break;
            case man_name:
                break;
            case R.id.man_phone:
                break;
            case up_right:
                showTimeSelect();
                break;
            case up_address_right:
                startArrivalSearch(Integer.valueOf((null == startCityId) ? poiBean.id + "" : startCityId), (null == startBean) ? poiBean.location : startBean.location);
                break;
            case R.id.hotel_phone_text_code_click:
                FgChooseCountry chooseCountry = new FgChooseCountry();
                Bundle bundleCode = new Bundle();
                bundleCode.putInt("airportCode", view.getId());
                startFragment(chooseCountry, bundleCode);
                break;
            case R.id.all_money_submit_click:
                checkData();
                break;
        }
    }
}
