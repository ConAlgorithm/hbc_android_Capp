package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CarViewpagerAdapter;
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
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForSingle;
import com.hugboga.custom.data.request.RequestGuideConflict;
import com.hugboga.custom.fragment.FGOrderNew;
import com.hugboga.custom.fragment.FgCarNew;
import com.hugboga.custom.fragment.FgChooseCity;
import com.hugboga.custom.fragment.FgLogin;
import com.hugboga.custom.fragment.FgPoiSearch;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

import static android.view.View.GONE;
import static com.huangbaoche.hbcframe.fragment.BaseFragment.KEY_FRAGMENT_NAME;

/**
 * Created on 16/8/3.
 */

public class SingleNewActivity extends BaseActivity {

    @Bind(R.id.city_use_car_left)
    TextView cityUseCarLeft;
    @Bind(R.id.use_city_tips)
    TextView useCityTips;
    @Bind(R.id.city_layout)
    LinearLayout cityLayout;
    @Bind(R.id.start_left)
    TextView startLeft;
    @Bind(R.id.start_tips)
    TextView startTips;
    @Bind(R.id.start_title)
    TextView startTitle;
    @Bind(R.id.start_detail)
    TextView startDetail;
    @Bind(R.id.start_layout)
    LinearLayout startLayout;
    @Bind(R.id.end_left)
    TextView endLeft;
    @Bind(R.id.end_tips)
    TextView endTips;
    @Bind(R.id.end_title)
    TextView endTitle;
    @Bind(R.id.end_detail)
    TextView endDetail;
    @Bind(R.id.end_layout)
    LinearLayout endLayout;
    @Bind(R.id.time_left)
    TextView timeLeft;
    @Bind(R.id.time_text)
    TextView timeText;
    @Bind(R.id.time_layout)
    LinearLayout timeLayout;
    @Bind(R.id.rl_starttime)
    RelativeLayout rlStarttime;
    @Bind(R.id.show_cars_layout_single)
    LinearLayout showCarsLayoutSingle;
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
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.all_money_left_sku)
    TextView allMoneyLeftSku;
    @Bind(R.id.all_money_text_sku)
    TextView allMoneyTextSku;
    @Bind(R.id.money_pre)
    TextView moneyPre;

    private PoiBean arrivalBean;//达到目的地
    private String serverTime;

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
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_single_new);
        ButterKnife.bind(this);
        initView();
        initHeader();
    }

    public void initHeader() {
        headerTitle.setText("单次接送");
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(useCityTips.getText())) {
                    AlertDialogUtils.showAlertDialog(activity, getString(R.string.back_alert_msg), "离开", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    finish();
                }
            }
        });
        headerRightTxt.setVisibility(View.VISIBLE);
        headerRightTxt.setText("常见问题");
        headerRightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_PROBLEM);
//                bundle.putBoolean(FgWebInfo.CONTACT_SERVICE, true);
//                startFragment(new FgWebInfo(), bundle);

                Intent intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_PROBLEM);
                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                startActivity(intent);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("source", "填写行程页面");
                MobclickAgent.onEvent(activity, "callcenter_oneway", map);
                v.setTag("填写行程页面,calldomestic_oneway,calloverseas_oneway");

            }
        });

        cityBean = (CityBean) this.getIntent().getSerializableExtra("cityBean");
        if (null != cityBean) {
            useCityTips.setText(cityBean.name);
        }


    }


    CollectGuideBean collectGuideBean;

    public void initView() {
        collectGuideBean = (CollectGuideBean) this.getIntent().getSerializableExtra("collectGuideBean");
        if (null != collectGuideBean) {
            initCarFragment(false);
        }
        confirmJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkParams();
            }
        });
    }

    private boolean checkParams() {
        if (null == manLuggageBean) {
            CommonUtils.showToast(R.string.add_man_toast);
            return false;
        }
        return true;
    }


    private void checkInput() {
        if (!TextUtils.isEmpty(timeText.getText()) && !TextUtils.isEmpty(useCityTips.getText())
                && !TextUtils.isEmpty(startTitle.getText())
                && !TextUtils.isEmpty(endTitle.getText())) {
            getData();
        }
    }

    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCity.class.getSimpleName().equals(from)) {
            cityBean = (CityBean) bundle.getSerializable(ChooseCityActivity.KEY_CITY);
            useCityTips.setText(cityBean.name);
            startBean = null;
            arrivalBean = null;
            startTips.setVisibility(View.VISIBLE);
            startTitle.setVisibility(GONE);
            startDetail.setVisibility(GONE);
            startTitle.setText("");
            startDetail.setText("");

            endTips.setVisibility(View.VISIBLE);
            endTitle.setVisibility(GONE);
            endDetail.setVisibility(GONE);
            endTitle.setText("");
            endDetail.setText("");

            bottom.setVisibility(GONE);
            if (null == collectGuideBean) {
                showCarsLayoutSingle.setVisibility(GONE);
            }
            timeText.setText("");

        } else if (FgPoiSearch.class.getSimpleName().equals(from)) {
            String fromKey = bundle.getString(KEY_FROM);
            if ("from".equals(fromKey)) {
                startBean = (PoiBean) bundle.getSerializable(FgPoiSearch.KEY_ARRIVAL);
                startTips.setVisibility(GONE);
                startTitle.setVisibility(View.VISIBLE);
                startDetail.setVisibility(View.VISIBLE);
                startTitle.setText(startBean.placeName);
                startDetail.setText(startBean.placeDetail);
            } else if ("to".equals(fromKey)) {
                arrivalBean = (PoiBean) bundle.getSerializable(FgPoiSearch.KEY_ARRIVAL);
                endTips.setVisibility(GONE);
                endTitle.setVisibility(View.VISIBLE);
                endDetail.setVisibility(View.VISIBLE);
                endTitle.setText(arrivalBean.placeName);
                endDetail.setText(arrivalBean.placeDetail);
            }
//            collapseSoftInputMethod();
            checkInput();
        }
    }

    String startLocation, termLocation;

    protected void getData() {
        cityId = cityBean.cityId;
        needChildrenSeat = cityBean.childSeatSwitch;
        startLocation = startBean.location;
        termLocation = arrivalBean.location;
        RequestCheckPriceForSingle requestCheckPriceForSingle = new RequestCheckPriceForSingle(activity, 4, airportCode, cityId, startLocation, termLocation, serverDate + " " + serverTime);
        requestData(requestCheckPriceForSingle);
    }

    CarListBean carListBean;


    private void genBottomData(CarBean carBean) {

        if (null == carBean) {
            return;
        }
        int total = carBean.price;
        if (null != manLuggageBean) {
            int seat1Price = OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean);
            int seat2Price = OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean);
            total += seat1Price + seat2Price;
        }

        allMoneyText.setText("￥" + total);
        if (null != carListBean) {
            allJourneyText.setText("全程预估: " + carListBean.distance + "公里," + carListBean.interval + "分钟");
        }
    }

    boolean isNetError = false;

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        bottom.setVisibility(View.GONE);
        carListBean = null;
        isNetError = true;
//        confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
//        confirmJourney.setOnClickListener(null);
        if (null != collectGuideBean) {
            initCarFragment(false);
        } else {
            initCarFragment(true);
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {
        super.onDataRequestCancel(request);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCheckPrice) {
            manLuggageBean = null;
            bottom.setVisibility(View.GONE);
//            confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
//            confirmJourney.setOnClickListener(null);
            isNetError = false;
            RequestCheckPrice requestCheckPrice = (RequestCheckPrice) request;
            carListBean = (CarListBean) requestCheckPrice.getData();
            if (carListBean.carList.size() > 0) {
                if (null == collectGuideBean) {
                    carBean = CarUtils.initCarListData(carListBean.carList).get(0);//carListBean.carList.get(0);
                } else {
                    carBean = CarUtils.isMatchLocal(CarUtils.getNewCarBean(collectGuideBean), carListBean.carList);
                }
                if (null != carBean) {
                    genBottomData(carBean);
                    bottom.setVisibility(View.VISIBLE);
                } else {
                    bottom.setVisibility(View.GONE);
                    CommonUtils.showToast(R.string.no_price_error);
                }

            } else {
                bottom.setVisibility(GONE);
            }
            initCarFragment(true);

        }
    }

    ManLuggageBean manLuggageBean;
    int maxLuuages = 0;

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case MAX_LUGGAGE_NUM:
                maxLuuages = (int) action.getData();
                break;
            case CAR_CHANGE_SMALL:
//                confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
//                confirmJourney.setOnClickListener(null);
                manLuggageBean = null;
                break;
            case ONBACKPRESS:
//                    backPress();
                break;
            case CHANGE_GUIDE:
                collectGuideBean = (CollectGuideBean) action.getData();
                break;
            case GUIDE_DEL:
                collectGuideBean = null;
//                confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
//                confirmJourney.setOnClickListener(null);
                if (null == carListBean) {
                    showCarsLayoutSingle.setVisibility(GONE);
                } else {
                    if (null != carListBean.carList && carListBean.carList.size() > 0) {
                        bottom.setVisibility(View.VISIBLE);
                        genBottomData(carListBean.carList.get(0));
                    }
                    initCarFragment(true);
//                confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
//                confirmJourney.setOnClickListener(null);
                }
                carBean = (CarBean) action.getData();
                if (null != carBean) {
                    genBottomData(carBean);
                }

                break;
            case CHANGE_CAR:
                carBean = (CarBean) action.getData();
                if (null != carBean) {
                    genBottomData(carBean);
                }
                break;
            case MAN_CHILD_LUUAGE:
                confirmJourney.setBackgroundColor(activity.getResources().getColor(R.color.all_bg_yellow));
                manLuggageBean = (ManLuggageBean) action.getData();
                if (null != carBean) {
                    genBottomData(carBean);
                }
                confirmJourney.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checkParams()) {
                            if (UserEntity.getUser().isLogin(activity)) {

                                if (null != collectGuideBean) {

                                    if ((carBean.carType == 1 && carBean.capOfPerson == 4
                                            && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 4)
                                            || (carBean.carType == 1 && carBean.capOfPerson == 6 && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 6)) {
                                        AlertDialogUtils.showAlertDialog(activity, getString(R.string.alert_car_full),
                                                "继续下单", "更换车型", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        checkGuide();
                                                        dialog.dismiss();
                                                    }
                                                }, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                    } else {
                                        checkGuide();
                                    }


                                } else {
                                    if ((carBean.carType == 1 && carBean.capOfPerson == 4
                                            && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 4)
                                            || (carBean.carType == 1 && carBean.capOfPerson == 6 && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 6)) {
                                        AlertDialogUtils.showAlertDialog(activity, getString(R.string.alert_car_full),
                                                "继续下单", "更换车型", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        goOrder();
                                                        dialog.dismiss();
                                                    }
                                                }, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                    } else {
                                        goOrder();
                                    }
                                }
                            } else {
                                Bundle bundle = new Bundle();//用于统计
                                bundle.putString("source", "单次接送下单");
                                startFragment(new FgLogin(), bundle);
                            }
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private void checkGuide() {
        String sTime = serverDate + " " + serverTime + ":00";
        OrderUtils.checkGuideCoflict(activity, 4, cityBean.cityId,
                null != collectGuideBean ? collectGuideBean.guideId : null, sTime,
                DateUtils.getToTime(sTime, Integer.valueOf(carListBean.estTime)),
                cityBean.cityId + "", 0, carBean.carType, carBean.carSeat,
                new HttpRequestListener() {
                    @Override
                    public void onDataRequestSucceed(BaseRequest request) {
                        RequestGuideConflict requestGuideConflict = (RequestGuideConflict) request;
                        List<String> list = requestGuideConflict.getData();
                        if (list.size() > 0) {
                            goOrder();
                        } else {
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

    private void goOrder() {
        FGOrderNew fgOrderNew = new FGOrderNew();
        Bundle bundle = new Bundle();
        bundle.putString("guideCollectId", collectGuideBean == null ? "" : collectGuideBean.guideId);
        bundle.putSerializable("collectGuideBean", collectGuideBean == null ? null : collectGuideBean);
        bundle.putString("source", source);

        bundle.putSerializable("KEY_CITY", cityBean);
        bundle.putSerializable("KEY_ARRIVAL", arrivalBean);
        bundle.putSerializable("KEY_START", startBean);

        bundle.putString("serverTime", serverTime);
        bundle.putString("price", carBean.price + "");
        bundle.putString("distance", carListBean.distance + "");

        carBean.expectedCompTime = carListBean.estTime;
        bundle.putParcelable("carBean", CarUtils.carBeanAdapter(carBean));

        bundle.putString("startCityId", cityBean.cityId + "");
        bundle.putString("endCityId", cityBean.cityId + "");//endCityId);
        bundle.putString("startDate", serverDate);
        bundle.putString("endDate", serverDate);

        bundle.putString("serverTime", serverTime);
        bundle.putString("serverDate", serverDate);

//                        bundle.putString("serverDayTime",serverDayTime+":00");
        bundle.putString("halfDay", "0");
        bundle.putString("adultNum", manLuggageBean.mans + "");
        bundle.putString("childrenNum", manLuggageBean.childs + "");
        bundle.putString("childseatNum", manLuggageBean.childSeats + "");
        bundle.putString("luggageNum", maxLuuages + "");//manLuggageBean.luggages + "");
        bundle.putString("passCities", "");
        bundle.putString("carTypeName", carBean.desc);
        bundle.putString("startCityName", cityBean.name);
        bundle.putSerializable("cityBean", cityBean);
        bundle.putSerializable("carListBean", carListBean);
        bundle.putInt("outnum", 0);
        bundle.putInt("innum", 0);
        bundle.putString("dayNums", "0");

//                        bundle.putParcelable("carBean",carBeanAdapter(carBean));
        bundle.putInt("type", 4);
        bundle.putString("orderType", "4");

        bundle.putParcelable("manLuggageBean", manLuggageBean);

        fgOrderNew.setArguments(bundle);
        startFragment(fgOrderNew);
    }

    FragmentManager fm;
    FgCarNew fgCarNew;

    private void initCarFragment(boolean isDataBack) {
        showCarsLayoutSingle.setVisibility(View.VISIBLE);
        fm = this.getSupportFragmentManager();//getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (null != fgCarNew) {
            transaction.remove(fgCarNew);
        }

        fgCarNew = new FgCarNew();
        Bundle bundle = new Bundle();
        bundle.putSerializable("collectGuideBean", collectGuideBean);
        bundle.putParcelable("carListBean", carListBean);
        bundle.putBoolean("isDataBack", isDataBack);
        bundle.putBoolean("isNetError", isNetError);
//        if(null != carListBean && carListBean.carList.size() == 0 && null != collectGuideBean){
//            CommonUtils.showToast(R.string.no_price_error);
//            return;
//        }

        if (isDataBack && null != carListBean) {
            String sTime = serverDate + " " + serverTime + ":00";
            bundle.putInt("cityId", cityId);
            bundle.putString("startTime", sTime);
            if (TextUtils.isEmpty(carListBean.estTime)) {
                bundle.putString("endTime", DateUtils.getToTime(sTime, 0));
            } else {
                bundle.putString("endTime", DateUtils.getToTime(sTime, Integer.valueOf(carListBean.estTime)));
            }
        }

        fgCarNew.setArguments(bundle);
        transaction.add(R.id.show_cars_layout_single, fgCarNew);
        transaction.commit();

    }


    public void showDaySelect() {
        Calendar cal = Calendar.getInstance();
        MyDatePickerListener myDatePickerDialog = new MyDatePickerListener(timeText);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                myDatePickerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal = Calendar.getInstance();
        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        dpd.setMaxDate(cal);
        dpd.show(this.activity.getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框

    }

    class MyDatePickerListener implements DatePickerDialog.OnDateSetListener {
        TextView mTextView;

        MyDatePickerListener(TextView textView) {
            this.mTextView = textView;
        }

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear + 1;
            String monthStr = String.format("%02d", month);
            String dayOfMonthStr = String.format("%02d", dayOfMonth);
            serverDate = year + "-" + monthStr + "-" + dayOfMonthStr;
            showTimeSelect();
        }
    }

    public void showTimeSelect() {
        Calendar cal = Calendar.getInstance();
        MyTimePickerDialogListener myTimePickerDialog = new MyTimePickerDialogListener();
        TimePickerDialog datePickerDialog = TimePickerDialog.newInstance(myTimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        datePickerDialog.show(this.activity.getFragmentManager(), "TimePickerDialog");                //显示日期设置对话框
    }


    class MyTimePickerDialogListener implements TimePickerDialog.OnTimeSetListener {


        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            String hour = String.format("%02d", hourOfDay);
            String minuteStr = String.format("%02d", minute);
            serverTime = hour + ":" + minuteStr;
            timeText.setText(serverDate + " " + serverTime);
            checkInput();
        }
    }


    @OnClick({R.id.city_layout, R.id.start_layout, R.id.end_layout, R.id.time_layout, R.id.confirm_journey, R.id.start_tips, R.id.start_title, R.id.start_detail, R.id.end_tips, R.id.end_title, R.id.end_detail})
    public void onClick(View view) {
        HashMap<String, String> map = new HashMap<String, String>();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.city_layout:
                bundle.putString("source", "下单过程中");
                bundle.putInt(KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_RENT);
                startFragment(new FgChooseCity(), bundle);
                map.put("source", "下单过程中");
                MobclickAgent.onEvent(activity, "search_trigger", map);
                break;
            case R.id.start_tips:
            case R.id.start_title:
            case R.id.start_detail:
            case R.id.start_layout:
                if (cityBean != null) {
                    FgPoiSearch fg = new FgPoiSearch();
                    bundle.putString("source", "下单过程中");
                    bundle.putString(KEY_FROM, "from");
                    bundle.putInt(FgPoiSearch.KEY_CITY_ID, cityBean.cityId);
                    bundle.putString(FgPoiSearch.KEY_LOCATION, cityBean.location);
                    startFragment(fg, bundle);
                    map.put("source", "下单过程中");
                    MobclickAgent.onEvent(activity, "search_trigger", map);
                } else {
                    CommonUtils.showToast("先选择城市");
                }
                break;
            case R.id.end_tips:
            case R.id.end_title:
            case R.id.end_detail:
            case R.id.end_layout:
                if (cityBean != null) {
                    FgPoiSearch fg = new FgPoiSearch();
                    bundle.putString("source", "下单过程中");
                    bundle.putString(KEY_FROM, "to");
                    bundle.putInt(FgPoiSearch.KEY_CITY_ID, cityBean.cityId);
                    bundle.putString(FgPoiSearch.KEY_LOCATION, cityBean.location);
                    startFragment(fg, bundle);
                    map.put("source", "下单过程中");
                    MobclickAgent.onEvent(activity, "search_trigger", map);
                } else {
                    CommonUtils.showToast("先选择城市");
                }
                break;
            case R.id.time_layout:
                if (startBean == null) {
                    CommonUtils.showToast("请先选择城市");
                    return;
                }
//                showDaySelect();
                showYearMonthDayTimePicker();
                break;
            case R.id.confirm_journey:
                break;
        }
    }


    public void showYearMonthDayTimePicker() {
        Calendar calendar = Calendar.getInstance();
        DateTimePicker picker = new DateTimePicker(activity, DateTimePicker.HOUR_OF_DAY);
        picker.setRange(2000, 2030);
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
//                showToast(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                serverDate = year + "-" + month + "-" + day;
                serverTime = hour + ":" + minute;
                timeText.setText(serverDate + " " + serverTime);
                checkInput();
            }
        });
        picker.show();
    }


    private void backPress() {
        if ((!TextUtils.isEmpty(useCityTips.getText()))) {
            AlertDialogUtils.showAlertDialog(activity, getString(R.string.back_alert_msg), "离开", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            finish();
        }
    }


    public void onBackPressed() {
        backPress();
    }
}