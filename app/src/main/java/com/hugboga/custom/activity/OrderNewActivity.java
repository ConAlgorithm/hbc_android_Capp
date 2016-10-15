package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.DeductionBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.MostFitAvailableBean;
import com.hugboga.custom.data.bean.MostFitBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderContact;
import com.hugboga.custom.data.bean.OrderInfoBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCancleTips;
import com.hugboga.custom.data.request.RequestDeduction;
import com.hugboga.custom.data.request.RequestMostFit;
import com.hugboga.custom.data.request.RequestPayNo;
import com.hugboga.custom.data.request.RequestSubmitBase;
import com.hugboga.custom.data.request.RequestSubmitDaily;
import com.hugboga.custom.data.request.RequestSubmitLine;
import com.hugboga.custom.data.request.RequestSubmitPick;
import com.hugboga.custom.data.request.RequestSubmitRent;
import com.hugboga.custom.data.request.RequestSubmitSend;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.bean.EventPayBean;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.event.EventPay;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.LogUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.LuggageItemLayout;
import com.hugboga.custom.widget.MoneyTextView;
import com.hugboga.custom.widget.TopTipsLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;

import static android.view.View.GONE;
import static com.hugboga.custom.R.id.man_name;
import static com.hugboga.custom.R.id.up_address_right;
import static com.hugboga.custom.R.id.up_right;

/**
 * Created on 16/8/3.
 */

public class OrderNewActivity extends BaseActivity {
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
    @Bind(R.id.header_right_image)
    ImageView headerRightImage;

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
    MoneyTextView allMoneyLeftText;
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

    @Bind(R.id.up_right)
    TextView upRight;//上车时间

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
    TextView upAddressRight;//上车地点
    @Bind(R.id.dream_right)
    MoneyTextView dreamRight;
    @Bind(R.id.airpost_name_left)
    TextView airpostNameLeft;

    @Bind(R.id.airport_name_layout)
    RelativeLayout airportNameLayout;
    @Bind(R.id.single_no_show_time)
    RelativeLayout singleNoShowTime;
    @Bind(R.id.single_no_show_address)
    RelativeLayout singleNoShowAddress;

    @Bind(R.id.hospital_layout)
    LinearLayout hospital_layout;
    String userName;
    @Bind(R.id.top_tips_layout)
    TopTipsLayout topTipsLayout;
    @Bind(R.id.pick_name)
    EditText pickName;
    @Bind(R.id.airport_name)
    EditText airportName;
    @Bind(R.id.luggage_item_layout)
    LuggageItemLayout luggageItemLayout;

    @Bind(R.id.agree_text)
    TextView agreeText;

    /**
     * 基于原来代码修改,有时间了优化
     */
    protected void initHeader() {
        headerRightTxt.setVisibility(View.GONE);
        headerTitle.setText("确认订单");
        source = getIntent().getStringExtra("source");

        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputMethod(pickName);
                finish();
            }
        });

//        headerRightTxt.setText(R.string.noraml_question);
//        headerRightTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(activity, WebInfoActivity.class);
//                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_PROBLEM);
//                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
//                startActivity(intent);
//            }
//        });
        headerRightImage.setVisibility(View.VISIBLE);
        headerRightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.getInstance(activity).showCallDialog();
            }
        });

        contactUsersBean = new ContactUsersBean();
        userName = UserEntity.getUser().getUserName(activity);
        if (TextUtils.isEmpty(userName)) {
            userName = UserEntity.getUser().getNickname(activity);
        }
        String userPhone = UserEntity.getUser().getPhone(activity);
        contactUsersBean.userName = userName;
        contactUsersBean.userPhone = userPhone;
        manName.setText(userName);
        manPhone.setText(userPhone);
        topTipsLayout.setText(R.string.order_detail_top2_tips);
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
    CarBean carBean;

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

    CollectGuideBean collectGuideBean;
    private OrderInfoBean orderInfoBean;

    String goodsVersion = "";
    String goodsNo = "";
    String goodsType = "";
    String cancleTipsId = "";
    String cancleTipsTime = "";

    protected void initView() {
        passCityList = (ArrayList<CityBean>) getIntent().getSerializableExtra("passCityList");

        manLuggageBean = (ManLuggageBean) getIntent().getSerializableExtra("manLuggageBean");
        carListBean = (CarListBean)this.getIntent().getSerializableExtra("carListBean");
        guideCollectId = this.getIntent().getStringExtra("guideCollectId");

        collectGuideBean = (CollectGuideBean) this.getIntent().getSerializableExtra("collectGuideBean");

        carBean = (CarBean)this.getIntent().getSerializableExtra("carBean");
        if(null != collectGuideBean) {
            guideCollectId = collectGuideBean.guideId;
        }

        startCityId = this.getIntent().getStringExtra("startCityId");
        endCityId = this.getIntent().getStringExtra("endCityId");
        startDate = this.getIntent().getStringExtra("startDate");
        endDate = this.getIntent().getStringExtra("endDate");
        halfDay = this.getIntent().getStringExtra("halfDay");
        adultNum = this.getIntent().getStringExtra("adultNum");
        childrenNum = this.getIntent().getStringExtra("childrenNum");
        childseatNum = this.getIntent().getStringExtra("childseatNum");
        luggageNum = this.getIntent().getStringExtra("luggageNum");
        passCities = this.getIntent().getStringExtra("passCities");


        luggageItemLayout.setText(luggageNum+"件");
        startCityName = this.getIntent().getStringExtra("startCityName");
        dayNums = this.getIntent().getStringExtra("dayNums");

        startBean = (CityBean) this.getIntent().getSerializableExtra("startBean");
        endBean = (CityBean) this.getIntent().getSerializableExtra("endBean");

        inNum = this.getIntent().getIntExtra("innum",0);
        outNum = this.getIntent().getIntExtra("outnum",0);
        isHalfTravel = this.getIntent().getBooleanExtra("isHalfTravel",false);

        orderType = this.getIntent().getStringExtra("orderType");

        type = this.getIntent().getIntExtra("type",0);

        skuBean = (SkuItemBean) getIntent().getSerializableExtra("web_sku");
        cityBean = (CityBean) getIntent().getSerializableExtra("web_city");
        serverDayTime = this.getIntent().getStringExtra("serverDayTime");


        distance = this.getIntent().getStringExtra("distance");
        if (null == distance) {
            distance = "0";
        }
        OrderUtils.genAgreeMent(activity,agreeText);
        genType(type);

        requestMostFit();
        requestTravelFund();
        getCancleTips();
        LogUtils.e(cancleTipsTime);
        couponLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dreamLeft.setChecked(false);
                    if (null == mostFitBean || null == mostFitBean.actualPrice  ||  mostFitBean.actualPrice == 0) {
                        int showPrice = 0;
                        if (null != couponBean) {
                            showPrice = couponBean.actualPrice.intValue();
                        } else {
                            showPrice = carBean.price;
                        }
                        //其他费用总和
                        int otherPriceTotal = checkInOrPickupPrice + hotelPrice + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean)
                                + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean);
                         allMoneyLeftText.setText(Tools.getRMB(activity) + (showPrice + otherPriceTotal));

                    } else {
                        int price = 0;
                        if(null != mostFitBean && null != mostFitBean.actualPrice){
                            price = mostFitBean.actualPrice.intValue();
                        }
                        allMoneyLeftText.setText(Tools.getRMB(activity) +price);
                    }
                }
            }
        });
        dreamLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    couponLeft.setChecked(false);
                    int otherPriceTotal =  hotelPrice + checkInOrPickupPrice
                            + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean) + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean);
                    if (null == deductionBean || null == deductionBean.priceToPay) {
                        allMoneyLeftText.setText(Tools.getRMB(activity) + (carBean.price + otherPriceTotal));
                    } else {
                        allMoneyLeftText.setText(Tools.getRMB(activity) + (carBean.price - money + otherPriceTotal));
                    }
                }
            }
        });
    }

    String cancleTips = "";

    private void getCancleTips() {
        RequestCancleTips requestCancleTips = new RequestCancleTips(activity,
                carBean,
                cancleTipsId,
                goodsType + "",
                carBean.carType + "",
                carBean.seatCategory + "",
                cancleTipsTime, halfDay,
                goodsVersion, goodsNo, orderType + "");
        HttpRequestUtils.request(activity, requestCancleTips, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                List<String> datas = (List<String>) request.getData();
                for (String str : datas) {
                    cancleTips += str + "\n";
                }
                changeDetail.setText(cancleTips);
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

            }
        });
    }

    //1,包车 2,接机,3,送机,4,单次接送,5,sku
    private void genType(int type) {
        switch (type) {
            case 1:
                genPick();
                StatisticClickEvent.showOrderNewPage(1,StatisticConstant.LAUNCH_J2,getIntentSource(),
                        carBean.carDesc,
                        EventUtil.getInstance().sourceDetail,isCheckIn,(adultNum+childrenNum)+"",
                        null ==collectGuideBean?false:true);
                break;
            case 2:
                genSend();
                StatisticClickEvent.showOrderNewPage(2,StatisticConstant.LAUNCH_S2,getIntentSource(),
                        carBean.carDesc,
                        EventUtil.getInstance().sourceDetail,isCheckIn,(adultNum+childrenNum)+"",
                        null ==collectGuideBean?false:true);
                break;
            case 3:
                genDairy();
                StatisticClickEvent.showOrderNewPage(3,StatisticConstant.LAUNCH_R2,getIntentSource(),
                        carBean.carDesc,
                        EventUtil.getInstance().sourceDetail,isCheckIn,(adultNum+childrenNum)+"",
                        null ==collectGuideBean?false:true);
                break;
            case 4:
                genSingle();
                StatisticClickEvent.showOrderNewPage(4,StatisticConstant.LAUNCH_C2,getIntentSource(),
                        carBean.carDesc,
                        EventUtil.getInstance().sourceDetail,isCheckIn,(adultNum+childrenNum)+"",
                        null ==collectGuideBean?false:true);
                break;
            case 5:
                genSKU();
                StatisticClickEvent.showOrderNewPage(5,StatisticConstant.LAUNCH_RG2,getIntentSource(),
                        carBean.carDesc,
                        EventUtil.getInstance().sourceDetail,isCheckIn,(adultNum+childrenNum)+"",
                        null ==collectGuideBean?false:true);
                break;
            case 6:
                genSKU();
                StatisticClickEvent.showOrderNewPage(6,StatisticConstant.LAUNCH_RT2,getIntentSource(),
                        carBean.carDesc,
                        EventUtil.getInstance().sourceDetail,isCheckIn,(adultNum+childrenNum)+"",
                        null ==collectGuideBean?false:true);
                break;
        }
    }


    private String getCarDesc() {
//        if (null != collectGuideBean) {
//            return collectGuideBean.carDesc;
//        } else {
            return carBean.carDesc;
//        }
    }


    AirPort airPort;
    boolean isCheckIn = false;
    String serverDate;

    //乘车总人数
    int allMansNum = 0;
    private void genCarInfoText() {
        StringBuffer carInfo = new StringBuffer();
        allMansNum = (Integer.valueOf(adultNum) + Integer.valueOf(childrenNum));
        carInfo.append("(" + "乘坐" + allMansNum + "人");
        if (!"0".equalsIgnoreCase(childseatNum)) {
            carInfo.append(",儿童座椅" + childseatNum + "个");
        }
        carInfo.append(")");

        carSeatTips.setText(carInfo.toString());
    }

    int checkInOrPickupPrice = 0;//协助登机 接机举牌

    //送机
    private void genSend() {
        airPort = (AirPort) this.getIntent().getSerializableExtra("airPortBean");
        poiBean = (PoiBean) this.getIntent().getSerializableExtra("poiBean");

        hotelPhoneTextCodeClick.setText("+" + airPort.areaCode);

        cancleTipsId = airPort.cityId+"";

        adultNum = this.getIntent().getStringExtra("adultNum");
        childrenNum = this.getIntent().getStringExtra("childrenNum");
        childseatNum = this.getIntent().getStringExtra("childseatNum");
        luggageNum = this.getIntent().getStringExtra("luggageNum");
        manLuggageBean = (ManLuggageBean) this.getIntent().getSerializableExtra("manLuggageBean");
        type = this.getIntent().getIntExtra("type",0);

        isCheckIn = this.getIntent().getBooleanExtra("needCheckin",true);

        serverDate = this.getIntent().getStringExtra("serverDate");
        serverTime = this.getIntent().getStringExtra("serverTime");
        carBean = (CarBean) this.getIntent().getSerializableExtra("carBean");

        citysLineTitle.setText("当地时间" + serverDate + "(" + DateUtils.getWeekOfDate(serverDate) + ")" + "  " + serverTime);
        citys_line_title_tips.setVisibility(GONE);

        cancleTipsTime = serverDate + " " + serverTime + ":00";

        startHospitalTitle.setText(poiBean.placeName);
        startHospitalTitleTips.setVisibility(View.VISIBLE);
        startHospitalTitleTips.setText(poiBean.placeDetail);

        endHospitalTitle.setText(airPort.airportName);
        endHospitalTitleTips.setVisibility(GONE);

        carSeat.setText(getCarDesc());
        genCarInfoText();
        airportNameLayout.setVisibility(View.VISIBLE);

        singleNoShowTime.setVisibility(GONE);
        singleNoShowAddress.setVisibility(GONE);

        allMoneyLeftText.setText(Tools.getRMB(activity) + (carBean.price + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean)
                + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean) + hotelPrice));

        checkin.setText("协助办理登机check in");
        checkin.setVisibility(View.VISIBLE);
        pick_name_layout.setVisibility(GONE);

        if (isCheckIn) {
            if (null != carListBean && null != carListBean.additionalServicePrice && null != carListBean.additionalServicePrice.checkInPrice) {
                checkin.setVisibility(View.VISIBLE);
                checkInOrPickupPrice = Integer.valueOf(carListBean.additionalServicePrice.checkInPrice);
                allMoneyLeftText.setText(Tools.getRMB(activity) + (carBean.price + checkInOrPickupPrice + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean) + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean)) + hotelPrice);
            } else {
                checkin.setVisibility(View.GONE);
            }
        } else {
            checkInOrPickupPrice = 0;
            checkin.setVisibility(GONE);
        }
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

    //接机
    private void genPick() {
        flightBean = (FlightBean) this.getIntent().getSerializableExtra(KEY_FLIGHT);
        poiBean = (PoiBean) this.getIntent().getSerializableExtra(KEY_ARRIVAL);

        hotelPhoneTextCodeClick.setText("+" + flightBean.arrivalAirport.areaCode);

        cancleTipsId = flightBean.arrivalAirport.cityId + "";


        carBean = (CarBean) this.getIntent().getSerializableExtra("carBean");

        adultNum = this.getIntent().getStringExtra("adultNum");
        childrenNum = this.getIntent().getStringExtra("childrenNum");
        childseatNum = this.getIntent().getStringExtra("childseatNum");
        luggageNum = this.getIntent().getStringExtra("luggageNum");

        cancleTipsTime = flightBean.depDate + " " + flightBean.arrivalTime + ":00";

        citysLineTitle.setText("当地时间" + flightBean.arrivalTime + "(" + DateUtils.getWeekOfDate(flightBean.depDate) + ")");
        citys_line_title_tips.setText("航班" + flightBean.flightNo + " " + flightBean.depAirport.cityName + "-" + flightBean.arrivalAirport.cityName);


        startHospitalTitle.setText(flightBean.arrivalAirport.airportName);
        startHospitalTitleTips.setVisibility(GONE);
        endHospitalTitle.setText(poiBean.placeName);
        endHospitalTitleTips.setText(poiBean.placeDetail);

        isCheckIn = this.getIntent().getBooleanExtra("needCheckin",false);

        carSeat.setText(getCarDesc());

        genCarInfoText();


        singleNoShowTime.setVisibility(GONE);
        singleNoShowAddress.setVisibility(GONE);
        allMoneyLeftText.setText(Tools.getRMB(activity) + (carBean.price + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean) + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean)));

        if (isCheckIn) {
            if (null != carListBean && null != carListBean.additionalServicePrice && null != carListBean.additionalServicePrice.pickupSignPrice) {
                checkin.setVisibility(View.VISIBLE);
                checkInOrPickupPrice = Integer.valueOf(carListBean.additionalServicePrice.pickupSignPrice);
                allMoneyLeftText.setText(Tools.getRMB(activity) + (carBean.price + checkInOrPickupPrice + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean) + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean)));
            } else {
                checkin.setVisibility(View.GONE);
                pick_name_layout.setVisibility(GONE);
                isCheckIn = false;
            }
        } else {
            checkInOrPickupPrice = 0;
            checkin.setVisibility(GONE);
            pick_name_layout.setVisibility(GONE);
        }
    }

    PoiBean endPoi;
    PoiBean startPoi;
    //单次接送
    private void genSingle() {
        endPoi = (PoiBean) this.getIntent().getSerializableExtra("KEY_ARRIVAL");
        startPoi = (PoiBean) this.getIntent().getSerializableExtra("KEY_START");
        startBean = (CityBean) this.getIntent().getSerializableExtra("KEY_CITY");

        cancleTipsId = startBean.cityId + "";


        serverDate = this.getIntent().getStringExtra("serverDate");
        serverTime = this.getIntent().getStringExtra("serverTime");


        adultNum = this.getIntent().getStringExtra("adultNum");
        childrenNum = this.getIntent().getStringExtra("childrenNum");
        childseatNum = this.getIntent().getStringExtra("childseatNum");
        luggageNum = this.getIntent().getStringExtra("luggageNum");

        cancleTipsTime = startDate + " " + serverTime + ":00";

        citysLineTitle.setText("当地时间" + startDate + "(" + DateUtils.getWeekOfDate(startDate) + ")");

        startHospitalTitle.setText(startPoi.placeName);
        startHospitalTitleTips.setText(startPoi.placeDetail);
        endHospitalTitle.setText(endPoi.placeName);
        endHospitalTitleTips.setText(endPoi.placeDetail);

        singleNoShowTime.setVisibility(GONE);
        singleNoShowAddress.setVisibility(GONE);

        carSeat.setText(getCarDesc());
        genCarInfoText();
        allMoneyLeftText.setText(Tools.getRMB(activity) + (carBean.price + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean) + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean)));
        citys_line_title_tips.setVisibility(GONE);
        checkin.setVisibility(GONE);
        pick_name_layout.setVisibility(GONE);
        hospital_layout.setVisibility(GONE);
    }


    int hotelPrice = 0;
    int hourseNum = 1;

    //线路包车
    private void genSKU() {
        skuTitle.setText(skuBean.goodsName);
        skuDay.setText(getString(R.string.sku_days, skuBean.daysCount));
        skuCityLine.setText(skuBean.places);
        skuLayout.setVisibility(View.VISIBLE);



        cancleTipsTime = startDate + " " + serverTime + ":00";

        citysLineTitle.setText("当地时间" + startDate + "(" + DateUtils.getWeekOfDate(startDate) + ")");
        citys_line_title_tips.setVisibility(GONE);
        goodsVersion = skuBean.goodsVersion + "";
        goodsNo = skuBean.goodsNo + "";
        goodsType = skuBean.goodsType + "";

        startBean = (CityBean) this.getIntent().getSerializableExtra("startBean");
        adultNum = this.getIntent().getStringExtra("adultNum");
        childrenNum = this.getIntent().getStringExtra("childrenNum");
        childseatNum = this.getIntent().getStringExtra("childseatNum");
        luggageNum = this.getIntent().getStringExtra("luggageNum");



        if (null != startBean) {
            hotelPhoneTextCodeClick.setText("+" + startBean.areaCode);
            cancleTipsId = startBean.cityId+"";
        }

        startHospitalTitle.setVisibility(GONE);
        startHospitalTitleTips.setVisibility(GONE);
        endHospitalTitle.setVisibility(GONE);
        endHospitalTitleTips.setVisibility(GONE);
        checkin.setVisibility(GONE);
        pick_name_layout.setVisibility(GONE);

        if (carListBean.showHotel = true) {
            hourseNum = carListBean.hourseNum;
            hotelPrice = carListBean.hotelPrice * hourseNum;
        }
        allMoneyLeftText.setText(Tools.getRMB(activity)
                + (carBean.price + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean)
                + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean)
                + hotelPrice));

        carSeat.setText(getCarDesc());
        genCarInfoText();
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
            dayView = LayoutInflater.from(activity).inflate(R.layout.day_order_item, null);
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

            if (dayNUms == 1) {
                bottom_line.setVisibility(View.INVISIBLE);
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
        upAddressLeft.setText("上车地点");
        show_day_layout.setVisibility(View.VISIBLE);
        if (isHalfTravel) {
            citysLineTitle.setText(startBean.name + "-0.5天包车");
            day_show_all.setVisibility(GONE);
        } else {
            citysLineTitle.setText(startBean.name + "-" + dayNums + "天包车");
        }



        String startWeekDay = "";
        startWeekDay = DateUtils.getWeekOfDate(startDate);
        String endWeekDay = "";
        endWeekDay = DateUtils.getWeekOfDate(endDate);
        citys_line_title_tips.setText("当地时间" + startDate + "(" + startWeekDay + ") 至" + "  " + endDate + " (" + endWeekDay + ")");
        if (isHalfTravel) {
            dayView = LayoutInflater.from(activity).inflate(R.layout.day_order_item, null);
            top_line = dayView.findViewById(R.id.top_line);
            bottom_line = dayView.findViewById(R.id.bottom_line);
            textView = (TextView) dayView.findViewById(R.id.right_text);
            top_line.setVisibility(View.INVISIBLE);
            bottom_line.setVisibility(View.INVISIBLE);
            textView.setText("半天: " + startCityName + "市内");
            day_layout.addView(dayView);
        }

        cancleTipsTime = startDate + " " + serverTime + ":00";

        if (serverTime.equalsIgnoreCase("00:00")) {
            upRight.setVisibility(View.GONE);
        } else {
            upRight.setVisibility(View.VISIBLE);
        }

        hotelPhoneTextCodeClick.setText("+" + startBean.areaCode);

        if (!isHalfTravel && null != passCityList) {
            if (passCityList.size() <= 3) {
                day_show_all.setVisibility(GONE);
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
        adultNum = this.getIntent().getStringExtra("adultNum");
        childrenNum = this.getIntent().getStringExtra("childrenNum");
        childseatNum = this.getIntent().getStringExtra("childseatNum");
        luggageNum = this.getIntent().getStringExtra("luggageNum");


        startBean = (CityBean) this.getIntent().getSerializableExtra("startBean");
        endBean = (CityBean)this.getIntent().getSerializableExtra("endBean");

        cancleTipsId = endBean.cityId + "";

        carSeat.setText(getCarDesc());
        genCarInfoText();
        startHospitalTitle.setVisibility(GONE);
        startHospitalTitleTips.setVisibility(GONE);

        endHospitalTitle.setVisibility(GONE);
        endHospitalTitleTips.setVisibility(GONE);
        checkin.setVisibility(GONE);
        pick_name_layout.setVisibility(GONE);

        allMoneyLeftText.setText(Tools.getRMB(activity) + (carBean.price + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean)
                + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean)));


    }


    //旅游基金
    String travelFund = "0";
    int money = 0;//旅游基金int
    DeductionBean deductionBean;

    private void requestTravelFund() {
        final int totalPrice =  OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean)
                + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean) + hotelPrice;
        RequestDeduction requestDeduction = new RequestDeduction(activity, (carBean.price + totalPrice) + "");
        HttpRequestUtils.request(activity, requestDeduction, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                deductionBean = ((RequestDeduction) request).getData();

                travelFund = deductionBean.deduction;
                money = Integer.valueOf(travelFund);
                if (0 == money) {
                    dream_right_tips.setVisibility(View.GONE);
                    dream_right_tips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(activity, TravelFundActivity.class));
                        }
                    });
                    dreamRight.setText(Tools.getRMB(activity)+"0");
                } else {
                    dreamRight.setText(Tools.getRMB(activity) + (Integer.valueOf(deductionBean.deduction) + Integer.valueOf(deductionBean.leftAmount)));
                    if (dreamLeft.isChecked()) {
                        allMoneyLeftText.setText(Tools.getRMB(activity) + (Integer.valueOf(deductionBean.priceToPay) + totalPrice));
                    }
                    dream_right_tips.setVisibility(View.VISIBLE);
                    dream_right_tips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(activity, TravelFundActivity.class));
                        }
                    });
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
    String idStr = "";

    int totalPrice = 0;
    //优惠券
    private void requestMostFit() {
        switch (type) {
            case 1:
                startCityId4MostFit = flightBean.arrivalAirport.cityId + "";
                date4MostFit = flightBean.arrDate + " " + flightBean.arrivalTime + ":00";
                areaCode4MostFit = flightBean.arrivalAirport.areaCode;
                break;
            case 2:
                startCityId4MostFit = airPort.cityId + "";
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
                startCityId4MostFit = startBean.cityId + "";
                areaCode4MostFit = startBean.areaCode;
                break;
            case 5:
                startCityId4MostFit = startCityId;
                date4MostFit = startDate + " 00:00:00";
                areaCode4MostFit = startBean.areaCode + "";
                break;
            case 6:
                startCityId4MostFit = startCityId;
                date4MostFit = startDate + " 00:00:00";
                areaCode4MostFit = startBean.areaCode + "";
                break;

        }

        totalPrice = checkInOrPickupPrice + hotelPrice + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean) + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean);

        RequestMostFit requestMostFit = new RequestMostFit(activity, carBean.price + totalPrice +"", carBean.price  +"",
                date4MostFit,
                carBean.carType + "",
                carBean.seatCategory + "",
                startCityId4MostFit,
                areaCode4MostFit,
                (null == dayNums ? "0" : dayNums) + "",
                distance, inNum + "", outNum + "", (null == dayNums ? "0" : dayNums) + "", orderType,carBean.carId+"");
        HttpRequestUtils.request(activity, requestMostFit, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                RequestMostFit requestMostFit1 = (RequestMostFit) request;
                mostFitBean = requestMostFit1.getData();
                if (null == mostFitBean.priceInfo) {
                    couponRight.setText("还没有优惠券");
                    allMoneyLeftText.setText(Tools.getRMB(activity) + (carBean.price + totalPrice));
                } else {
                    couponRight.setText((mostFitBean.priceInfo) + "优惠券");
                    int price = 0;
                    if(null != mostFitBean && null != mostFitBean.actualPrice){
                        price = mostFitBean.actualPrice.intValue();
                    }
                    allMoneyLeftText.setText(Tools.getRMB(activity) + price);
                }
                couponRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        MostFitAvailableBean mostFitAvailableBean = new MostFitAvailableBean();

                        mostFitAvailableBean.carSeatNum = carBean.seatCategory + "";
                        mostFitAvailableBean.carTypeId = carBean.carType + "";
                        mostFitAvailableBean.distance = distance;
                        mostFitAvailableBean.expectedCompTime = (null == carBean.expectedCompTime) ? "" : carBean.expectedCompTime + "";
                        mostFitAvailableBean.limit = 20 + "";
                        mostFitAvailableBean.offset = 0 + "";
                        mostFitAvailableBean.priceChannel = carBean.price + "";
                        mostFitAvailableBean.useOrderPrice = carBean.price + "";
                        mostFitAvailableBean.serviceCityId = startCityId4MostFit + "";
                        mostFitAvailableBean.serviceCountryId = areaCode4MostFit;
                        mostFitAvailableBean.serviceLocalDays = inNum + "";
                        mostFitAvailableBean.serviceNonlocalDays = outNum + "";
                        mostFitAvailableBean.serviceTime = date4MostFit;
                        mostFitAvailableBean.userId = UserEntity.getUser().getUserId(activity);
                        mostFitAvailableBean.totalDays = (null == dayNums) ? "0" : dayNums + "";
                        mostFitAvailableBean.orderType = orderType;
                        mostFitAvailableBean.carModelId = carBean.carId+"";
                        bundle.putSerializable(Constants.PARAMS_DATA, mostFitAvailableBean);
                        if (null != mostFitBean) {
                            idStr = mostFitBean.couponId;
                            bundle.putString("idStr", mostFitBean.couponId);
                        } else if (null != couponBean) {
                            idStr = couponBean.couponID;
                            bundle.putString("idStr", couponBean.couponID);
                        } else {
                            bundle.putString("idStr", "");
                        }
                        Intent intent = new Intent(activity,CouponActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {
                LogUtils.e("========");
            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                LogUtils.e("====onDataRequestError====");
            }
        });

    }


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_order_new);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initHeader();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    String serverTime = "09:00";
    TimePicker picker;
    public void showYearMonthDayTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        picker = new TimePicker(activity, TimePicker.HOUR_OF_DAY);
        picker.setTitle("请选择上车时间");
        picker.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                serverTime = hour + ":" + minute;
                upRight.setText(serverTime + "(当地时间)");
                picker.dismiss();
            }
        });

        picker.show();
    }


    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestSubmitBase) {
            orderInfoBean = ((RequestSubmitBase) request).getData();
            String couponId = null;
            if (couponLeft.isChecked()) {
                if (null == couponBean && null != mostFitBean) {
                    couponId = mostFitBean.couponId;
                } else if (null != couponBean && null == mostFitBean) {
                    couponId = couponBean.couponID;
                }
            }

            EventPayBean eventPayBean = getChoosePaymentStatisticParams();
            if (orderInfoBean.getPriceActual() == 0) {
                RequestPayNo pequestPayNo = new RequestPayNo(this, orderInfoBean.getOrderno(), 0, Constants.PAY_STATE_ALIPAY, couponId);
                requestData(pequestPayNo);
                MobClickUtils.onEvent(new EventPay(eventPayBean));
            } else {
                ChoosePaymentActivity.RequestParams requestParams = new ChoosePaymentActivity.RequestParams();
                requestParams.couponId = couponId;
                requestParams.orderId = orderInfoBean.getOrderno();
                requestParams.shouldPay = orderInfoBean.getPriceActual();
                requestParams.payDeadTime = orderInfoBean.getPayDeadTime();
                requestParams.source = source;
                requestParams.needShowAlert = true;
                requestParams.eventPayBean = eventPayBean;

                Intent intent = new Intent(activity,ChoosePaymentActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, requestParams);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);
            }
        }  else if (request instanceof RequestPayNo) {
            RequestPayNo mParser = (RequestPayNo) request;
            if (mParser.payType == Constants.PAY_STATE_ALIPAY) {
                if ("travelFundPay".equals(mParser.getData()) || "couppay".equals(mParser.getData())) {
                    PayResultActivity.Params params = new PayResultActivity.Params();
                    params.payResult = true;
                    params.orderId =  orderInfoBean.getOrderno();
                    Intent intent = new Intent(OrderNewActivity.this, PayResultActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    startActivity(intent);
                }
            }
        }

    }

    private EventPayBean getChoosePaymentStatisticParams() {
        EventPayBean eventPayBean = new EventPayBean();
        eventPayBean.carType = carBean.carDesc;
        eventPayBean.seatCategory = carBean.seatCategory;
        eventPayBean.guestcount = carBean.capOfPerson+"";
        eventPayBean.isFlightSign = orderBean.isFlightSign;
        eventPayBean.isCheckin = orderBean.isCheckin;
        eventPayBean.guideCollectId = null != collectGuideBean ? collectGuideBean.guideId + "" : "";
        eventPayBean.orderStatus = orderBean.orderStatus;
        eventPayBean.orderType = type;
        eventPayBean.forother = contactUsersBean.isForOther;
        eventPayBean.paysource = "下单过程中";
        return eventPayBean;
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

    List<OrderContact> contact = new ArrayList<OrderContact>();

    private void checkData() {
        contact.clear();
        if (TextUtils.isEmpty(manName.getText())) {
            CommonUtils.showToast("联系人姓名不能为空!");
            return;
        }
        if (TextUtils.isEmpty(manPhone.getText())) {
            CommonUtils.showToast("联系人电话不能为空!");
            return;
        }
        if (type == 5 || type == 6) {
            if (TextUtils.isEmpty(upAddressRight.getText())) {
                CommonUtils.showToast("上车地点不能为空!");
                return;
            }
        }

        if (type == 1) {
            if(isCheckIn) {
                if (TextUtils.isEmpty(pickName.getText())) {
                    CommonUtils.showToast("接机牌姓名不能为空!");
                    return;
                } else {
                    String text = pickName.getText().toString();
                    for (int i = 0; i < text.length(); i++) {
                        if (!Tools.isEmojiCharacter(text.charAt(i))) {
                            CommonUtils.showToast("接机牌姓名不能含有表情!");
                            return;
                        }
                    }
                }
            }
        }


        if (UserEntity.getUser().isLogin(activity)) {
            switch (type) {
                case 1:
                    RequestSubmitPick requestSubmitPick = new RequestSubmitPick(activity, getOrderByInput());
                    requestData(requestSubmitPick);
                    break;
                case 2:
                    RequestSubmitSend requestSubmitSend = new RequestSubmitSend(activity, getOrderByInput());
                    requestData(requestSubmitSend);
                    break;
                case 3:
                case 5:
                    RequestSubmitDaily requestSubmitBase = new RequestSubmitDaily(activity, getOrderByInput());
                    requestData(requestSubmitBase);
                    break;
                case 6:
                    RequestSubmitLine requestSubmitLine = new RequestSubmitLine(activity, getOrderByInput());
                    requestData(requestSubmitLine);
                    break;
                case 4:
                    RequestSubmitRent requestSubmitRent = new RequestSubmitRent(activity, getOrderByInput());
                    requestData(requestSubmitRent);
                    break;
            }
        } else {
            Intent intent = new Intent(activity,LoginActivity.class);
            startActivity(intent);
        }
    }


    ContactUsersBean contactUsersBean = null;
    CouponBean couponBean;

    @Subscribe
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
            if (couponBean.couponID.equalsIgnoreCase(idStr)) {
                idStr = null;
                couponBean = null;
                couponRight.setText("");
                if (couponLeft.isChecked()) {
                    allMoneyLeftText.setText(Tools.getRMB(activity) + (carBean.price + checkInOrPickupPrice + hotelPrice + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean) + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean)));
                }
            } else {
                couponRight.setText(couponBean.price + "优惠券");
                if (couponLeft.isChecked()) {
                    allMoneyLeftText.setText(Tools.getRMB(activity) + (couponBean.actualPrice.intValue() + checkInOrPickupPrice + hotelPrice + OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean) + OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean)));
                }
            }
            mostFitBean = null;

        }else if(action.getType() == EventType.CHOOSE_POI_BACK){

                poiBean = (PoiBean) action.getData();
                upAddressRight.setText(poiBean.placeName + "\n" + poiBean.placeDetail);

        }else if(action.getType() == EventType.CHOOSE_COUNTRY_BACK){
            AreaCodeBean areaCodeBean = (AreaCodeBean)action.getData();
            hotelPhoneTextCodeClick.setText("+" + areaCodeBean.getCode());
        }
    }

    //SKU参数
    private OrderBean getSKUOrderByInput() {
        StatisticClickEvent.commitClick(StatisticConstant.SUBMITORDER_RG,getIntentSource(),carBean.carDesc+"",allMansNum,contactUsersBean.isForOther);

        return new OrderUtils().getSKUOrderByInput(guideCollectId, skuBean,
                startDate, serverTime, distance,
                carBean, adultNum, childrenNum,
                startBean, getPassCityStr(),
                contactUsersBean, mark.getText().toString(), manName.getText().toString(), poiBean, dreamLeft.isChecked(),
                travelFund, couponBean, mostFitBean, carListBean, manLuggageBean, hourseNum, hotelPrice, 5,luggageNum);

    }

    //推荐线路
    private OrderBean getLineOrderByInput() {
        StatisticClickEvent.commitClick(StatisticConstant.SUBMITORDER_RT,getIntentSource(),carBean.carDesc+"",allMansNum,contactUsersBean.isForOther);

        return new OrderUtils().getSKUOrderByInput(guideCollectId, skuBean,
                startDate, serverTime, distance,
                carBean, adultNum, childrenNum,
                startBean, getPassCityStr(),
                contactUsersBean, mark.getText().toString(), manName.getText().toString(), poiBean, dreamLeft.isChecked(),
                travelFund, couponBean, mostFitBean, carListBean, manLuggageBean, hourseNum, hotelPrice, 6,luggageNum);

    }

    private String getPassCityStr() {
        String passCity = "";
        passCity += skuBean.depCityId + "-0";
        if (null != skuBean.passCityList) {
            for (CityBean city : skuBean.passCityList) {
                passCity += "," + city.cityId + "-0";
            }
        }
        passCity += "," + skuBean.arrCityId + "-0";
        return passCity;
    }


    OrderBean orderBean;
    ArrayList<CityBean> passCityList;

    String guideCollectId = null;

    //包车参数
    private OrderBean getDayOrderByInput() {
        StatisticClickEvent.commitClick(StatisticConstant.SUBMITORDER_R,getIntentSource(),EventUtil.getInstance().sourceDetail,collectGuideBean,carBean.carDesc+"",allMansNum,contactUsersBean.isForOther);

        return new OrderUtils().getDayOrderByInput(adultNum, carBean,
                childrenNum, endCityId,
                endBean, contact,
                serverTime, startDate,
                endDate, outNum, inNum,
                hotelPhoneText.getText().toString(), hotelPhoneTextCodeClick.getText().toString(),
                startBean, isHalfTravel,
                startBean.name, endBean.placeName,
                manName.getText().toString(), passCities, mark.getText().toString(),
                childseatNum, luggageNum,
                contactUsersBean,
                dreamLeft.isChecked(), travelFund,
                couponBean, mostFitBean,
                guideCollectId, poiBean);
    }

    private OrderBean getPickOrderByInput() {
        StatisticClickEvent.pickClick(StatisticConstant.SUBMITORDER_J,getIntentSource(),carBean.carDesc+"",isCheckIn,allMansNum);

        return new OrderUtils().getPickOrderByInput(flightBean, poiBean,
                carBean, pickName.getText().toString(),
                carListBean, pickName.getText().toString(),
                adultNum, childrenNum, distance,
                hotelPhoneText.getText().toString(), hotelPhoneTextCodeClick.getText().toString(),
                manName.getText().toString(), passCities, mark.getText().toString(),
                serverTime, childseatNum, luggageNum,
                contactUsersBean, dreamLeft.isChecked(),
                travelFund, couponBean, mostFitBean,
                guideCollectId, manLuggageBean, isCheckIn);
    }


    private OrderBean getSingleOrderByInput() {
        StatisticClickEvent.commitClick(StatisticConstant.SUBMITORDER_C,getIntentSource(),carBean.carDesc+"",allMansNum,contactUsersBean.isForOther);

        return new OrderUtils().getSingleOrderByInput(adultNum, carBean,
                childrenNum, endCityId,
                startCityId, contact,
                serverTime, startCityName,
                serverDate,
                startPoi, endPoi, distance,
                hotelPhoneText.getText().toString(), hotelPhoneTextCodeClick.getText().toString(),
                carListBean,
                manLuggageBean,
                manName.getText().toString(), passCities, mark.getText().toString(),
                childseatNum, luggageNum,
                contactUsersBean,
                dreamLeft.isChecked(), travelFund,
                couponBean, mostFitBean,
                guideCollectId);
    }

    private OrderBean getSendOrderByInput() {
        StatisticClickEvent.sendClick(StatisticConstant.SUBMITORDER_S,getIntentSource(),carBean.carDesc+"",isCheckIn,allMansNum);

        return new OrderUtils().getSendOrderByInput(poiBean,
                carBean, manName.getText().toString(),
                isCheckIn, airportName.getText().toString(), airPort,
                carListBean, serverDate,
                dreamLeft.isChecked(),
                adultNum, childrenNum,
                hotelPhoneText.getText().toString(), hotelPhoneTextCodeClick.getText().toString(), mark.getText().toString(),
                serverTime, childseatNum, luggageNum,
                contactUsersBean,
                travelFund, couponBean, mostFitBean,
                guideCollectId, manLuggageBean);
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
            case 6:
                orderBean = getLineOrderByInput();
                break;
        }
        return orderBean;
    }


    private void startArrivalSearch(int cityId, String location) {
        if (location != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(PoiSearchActivity.KEY_CITY_ID, cityId);
            bundle.putString(PoiSearchActivity.KEY_LOCATION, location);
            Intent intent = new Intent(activity,PoiSearchActivity.class);
            intent.putExtras(bundle);
            intent.putExtra("mBusinessType",type);
            startActivity(intent);
        }
    }

    Intent intent;
    @OnClick({R.id.all_money_submit_click, R.id.other_phone_layout, R.id.other_phone_name, R.id.for_other_man, man_name, R.id.man_phone, R.id.man_phone_layout, up_right, up_address_right, R.id.hotel_phone_text_code_click, R.id.hotel_phone_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.man_phone_layout:
            case R.id.for_other_man:
            case R.id.other_phone_layout:
            case R.id.man_name:
            case R.id.man_phone:
                Bundle bundle = new Bundle();
                bundle.putSerializable("contactUsersBean", contactUsersBean);
                intent = new Intent(activity,ChooseOtherActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case up_right:
                showYearMonthDayTimePicker();
                break;
            case up_address_right:
                startArrivalSearch(Integer.valueOf((null == startCityId) ? poiBean.id + "" : startCityId), (null == startBean) ? poiBean.location : startBean.location);
                break;
            case R.id.hotel_phone_text_code_click:
                Bundle bundleCode = new Bundle();
                bundleCode.putInt("airportCode", view.getId());
                intent = new Intent(activity,ChooseCountryActivity.class);
                intent.putExtras(bundleCode);
                startActivity(intent);
                break;
            case R.id.all_money_submit_click:
                checkData();
                break;
        }
    }
}