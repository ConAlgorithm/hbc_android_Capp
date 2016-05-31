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
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.CouponBean;
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

import static com.hugboga.custom.R.id.car_seat;
import static com.hugboga.custom.R.id.car_seat_tips;
import static com.hugboga.custom.R.id.dream_right;
import static com.hugboga.custom.R.id.end_hospital_title;
import static com.hugboga.custom.R.id.end_hospital_title_tips;
import static com.hugboga.custom.R.id.man_name;
import static com.hugboga.custom.R.id.other_layout;
import static com.hugboga.custom.R.id.other_name;
import static com.hugboga.custom.R.id.start_hospital_title;
import static com.hugboga.custom.R.id.start_hospital_title_tips;
import static com.hugboga.custom.R.id.up_address_right;
import static com.hugboga.custom.R.id.up_right;
import static com.hugboga.custom.R.string.remark;

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
    @Bind(R.id.pick_name)
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

    @Override
    protected void initHeader() {
        fgRightBtn.setVisibility(View.VISIBLE);
        fgTitle.setText(R.string.select_city_title);
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
    String carTypeName;
    String startCityName;
    String dayNums;
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
    @Override
    protected void initView() {
        passCityList = (ArrayList<CityBean>) getArguments().getSerializable("passCityList");

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
        carTypeName = this.getArguments().getString("carTypeName");
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
        if(null == distance){
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
                genDairy();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                genSKU();
                break;
        }
    }

    private void genSKU() {
        skuTitle.setText(skuBean.goodsName);
        skuDay.setText(getString(R.string.sku_days, skuBean.daysCount));
        skuCityLine.setText(skuBean.places);
        skuLayout.setVisibility(View.VISIBLE);
        citysLineTitle.setText("当地时间"+startDate+"("+DateUtils.getWeekOfDate(startDate)+")");
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


        carSeat.setText(carTypeName);
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

        carSeat.setText(carTypeName);
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

    private void requestTravelFund() {
        TrequestTravelFundLogs request = new TrequestTravelFundLogs(getActivity(), 0);
        HttpRequestUtils.request(getContext(), request, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                TrequestTravelFundLogs trequestTravelFundLogs = (TrequestTravelFundLogs) request;
                TravelFundData travelFundData = trequestTravelFundLogs.getData();
                travelFund = travelFundData.getFundAmount();
                int money = Integer.valueOf(travelFund);
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
                        allMoneyLeftText.setText("￥" + (carBean.price - money) + "");
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

    //优惠券
    private void requestMostFit() {
        RequestMostFit requestMostFit = new RequestMostFit(getContext(), carBean.price + "", carBean.price + "",
                startDate + " 00:00:00", carBean.carType + "", carBean.seatCategory + "", startCityId + "",
                startBean.areaCode + "", dayNums + "", distance, inNum + "", outNum + "", dayNums + "", orderType);
        HttpRequestUtils.request(getContext(), requestMostFit, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                RequestMostFit requestMostFit1 = (RequestMostFit) request;
                mostFitBean = requestMostFit1.getData();
                if(null == mostFitBean.priceInfo){
                    couponRight.setText("还没有优惠券");
                    allMoneyLeftText.setText("￥" + carBean.price);
                }else{
                    couponRight.setText((mostFitBean.priceInfo )+ "优惠券");
                    allMoneyLeftText.setText("￥" + mostFitBean.actualPrice);
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
                        mostFitAvailableBean.expectedCompTime = dayNums + "";
                        mostFitAvailableBean.limit = 0 + "";
                        mostFitAvailableBean.offset = 20 + "";
                        mostFitAvailableBean.priceChannel = carBean.price + "";
                        mostFitAvailableBean.useOrderPrice = carBean.price + "";
                        mostFitAvailableBean.serviceCityId = startCityId + "";
                        mostFitAvailableBean.serviceCountryId = startBean.areaCode;
                        mostFitAvailableBean.serviceLocalDays = inNum + "";
                        mostFitAvailableBean.serviceNonlocalDays = outNum + "";
                        mostFitAvailableBean.serviceTime = startDate + " 00:00:00";
                        mostFitAvailableBean.userId = UserEntity.getUser().getUserId(getContext());
                        mostFitAvailableBean.totalDays = dayNums + "";
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
            bringToFront(FgTravel.class, new Bundle());
            String orderNo = ((RequestSubmitBase) request).getData();
            FgChoosePayment.RequestParams requestParams = new FgChoosePayment.RequestParams();
            requestParams.orderId = orderNo;
            if (couponLeft.isChecked()) {
                requestParams.shouldPay = 0;
            } else {
                requestParams.couponId = "";
            }
            requestParams.shouldPay = orderBean.orderPrice;
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
        if (TextUtils.isEmpty(upAddressRight.getText())) {
            ToastUtils.showLong("上车地点不能为空!");
            return;
        }
        if (UserEntity.getUser().isLogin(getActivity())) {
            RequestSubmitDaily requestSubmitBase = new RequestSubmitDaily(getActivity(), getOrderByInput());
            requestData(requestSubmitBase);
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
        map.put("begincity", startBean.name);
        map.put("carstyle", carBean.carDesc);
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
            calendar.set(Integer.valueOf(ymd[0]), Integer.valueOf(ymd[1])-1, Integer.valueOf(ymd[2]));
            calendar.add(Calendar.DAY_OF_YEAR, day);
            return DateUtils.dateDateFormat.format(calendar.getTime());
        } catch (Exception e) {
            MLog.e("解析时间格式错误", e);
        }
        return null;
    }

    //SKU参数
    private OrderBean getSKUOrderByInput() {
        orderBean = new OrderBean();//订单
        orderBean.guideCollectId = guideCollectId;
        orderBean.orderType = 5;
        orderBean.goodsNo = skuBean.goodsNo;
        orderBean.lineSubject = skuBean.goodsName;
        orderBean.lineDescription = skuBean.salePoints;
        orderBean.orderGoodsType = skuBean.goodsType;
        orderBean.serviceTime = startDate;//日期
        orderBean.serviceStartTime = serverTime + ":00";//时间
        orderBean.serviceEndTime = getServiceEndTime(startDate, skuBean.daysCount - 1);
        orderBean.distance = distance;//距离
        orderBean.expectedCompTime = 0;//耗时
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
        orderBean.priceChannel = carBean.price+"";
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
    private OrderBean getDayOrderByInput(){
        orderBean = new OrderBean();//订单
        passCityList = new ArrayList<CityBean>();
        if (orderBean.passByCity != null)
            for (int i = 1; i < orderBean.passByCity.size() - 1; i++) {
                passCityList.add(orderBean.passByCity.get(i));
            }

        orderBean.adult = Integer.valueOf(adultNum);
        orderBean.carDesc = carTypeName;
        orderBean.seatCategory = carBean.seatCategory;
        orderBean.carType = carBean.carType;
        orderBean.child = Integer.valueOf(childrenNum);

        orderBean.destAddress = endCityId;

        orderBean.priceMark = carBean.pricemark;
        orderBean.serviceCityId = Integer.valueOf(startCityId);
        orderBean.serviceEndCityid = Integer.valueOf(endCityId);
        orderBean.serviceCityName = startCityName;
        orderBean.serviceEndCityName = endCityId;
        orderBean.isHalfDaily = Integer.valueOf(halfDay);
        orderBean.contact = contact;
        orderBean.serviceStartTime = serverTime + ":00";
        orderBean.serviceTime = startDate;

        if (halfDay.equalsIgnoreCase("0")) {
            orderBean.oneCityTravel = 2;
            orderBean.totalDays = Integer.valueOf(dayNums);
            orderBean.inTownDays = inNum;
            orderBean.outTownDays = outNum;
            orderBean.serviceEndTime = endDate;
            orderBean.startAddressPoi = startBean.location;
            orderBean.destAddressPoi = endBean.location;
        } else {
            orderBean.oneCityTravel = 1;
            orderBean.serviceEndTime = startDate;
            orderBean.startAddressPoi = startBean.location;
            orderBean.destAddressPoi = startBean.location;
            orderBean.totalDays = 1;
            orderBean.inTownDays = 1;
            orderBean.outTownDays = 0;
        }

//TODO;
        orderBean.serviceAddressTel = hotelPhoneText.getText().toString();
        orderBean.serviceAreaCode = hotelPhoneTextCodeClick.getText().toString();


        orderBean.startAddressPoi = startBean.location;
        orderBean.destAddressPoi = endBean.location;

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

        orderBean.guideCollectId = guideCollectId;

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

    private OrderBean getOrderByInput() {
        switch (type){
            case 1:
                orderBean = getDayOrderByInput();
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                orderBean = getSKUOrderByInput();
                break;
        }


        return orderBean;

/**
 "priceChannel": model.priceChannel!,                 // C端价格
 "priceMark": model.priceMark!,                       // 询价系统返回ID
 "adultNum": model.adultNum!,                         // 成人座位数
 "childNum": model.childNum!,                         // 小孩座位数

 "startCityId": model.startCityId!,                  // 服务地城市ID
 "destCityId": model.destCityId!,                    // 服务终止城市ID
 "startCityName": model.startCityName!,
 "destCityName": model.destCityName!,

 "serviceDate": model.serviceDate!,                  // 服务时间
 "serviceEndDate": model.serviceEndDate!,            // 服务终止时间
 "serviceRecTime": model.serviceRecTime!,            // 服务时间
 "serviceDepartTime": model.serviceDepartTime!,      // 出发时间

 "startAddressPoi": model.startAddressPoi!,           // 出发地poi(纬度,经度 : 36.524461,180.155223)
 "destAddressPoi": model.destAddressPoi!,             // 目的地poi(纬度,经度 : 36.524461,180.155223)
 "distance": model.distance!,                         // 服务距离
 "expectedCompTime": model.expectedCompTime!,         // 预计服务时间
 "carTypeId": model.carTypeId!,                       // 车类型：1-经济，2-舒适，3-豪华，4-奢华
 "carSeatNum": model.carSeatNum!,                     // 车容量，车座数
 "carDesc": model.carDesc!,                           // 车辆类型描述（所包含的车辆款式）
 "userAreaCode1": model.userAreaCode1!,               // 用户手机号（默认登录使用手机号）
 "userMobile1": model.userMobile1!,                   // 用户区号
 "userName": model.userName!,                         // 客人名字
 "urgentFlag": model.urgentFlag!,                     // 是否急单
 "orderChannel": channelId,                           // 渠道编号


 **/


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
                startArrivalSearch(Integer.valueOf(startCityId), startBean.location);
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

//
//    @OnClick({R.id.header_right_txt, R.id.all_money_info, R.id.header_left_btn, R.id.hotel_phone_text_code_click, R.id.all_money_submit_click})
//    public void onClick(View view) {
//        switch (view.getId()) {
////            case R.id.up_site_text:
////                startArrivalSearch(Integer.valueOf(startCityId), startBean.location);
////                break;
//            case R.id.header_right_txt:
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("source", "提交订单页面");
//                MobclickAgent.onEvent(getActivity(), "callcenter_oneday", map);
//                view.setTag("提交订单页面,calldomestic_oneday,calloverseas_oneday");
//                super.onClick(view);
//                break;
//            case R.id.all_money_info:
//                FgOrderInfo fgOrderInfo = new FgOrderInfo();
//                Bundle bundleCar = new Bundle();
//                bundleCar.putParcelable("carBean", carBean);
//                bundleCar.putString("halfDay", halfDay);
//                fgOrderInfo.setArguments(bundleCar);
//                startFragment(fgOrderInfo);
//                break;
//            case R.id.up_time:
//                showTimeSelect();
//                break;
//            case R.id.header_left_btn:
//                finish();
//                break;
//            case R.id.header_title:
//                break;
////            case R.id.add_other_phone_click:
////                if (!phone2Layout.isShown()) {
////                    phone2Layout.setVisibility(View.VISIBLE);
////                } else if (!phone3Layout.isShown()) {
////                    phone3Layout.setVisibility(View.VISIBLE);
////                    addOtherPhoneClick.setTextColor(Color.parseColor("#929394"));
////                }
//
////                break;
////            case R.id.area_code_click:
////            case R.id.area_code_2_click:
////            case R.id.area_code_3_click:
////            case R.id.area_code_other_click:
//            case R.id.hotel_phone_text_code_click:
//                FgChooseCountry chooseCountry = new FgChooseCountry();
//                Bundle bundleCode = new Bundle();
//                bundleCode.putInt("airportCode", view.getId());
//                startFragment(chooseCountry, bundleCode);
//                break;
//            case R.id.all_money_submit_click:
//                checkData();
//                break;
//        }
//    }
}
