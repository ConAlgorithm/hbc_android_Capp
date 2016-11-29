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
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.GuideCarEventData;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForSingle;
import com.hugboga.custom.data.request.RequestGuideConflict;
import com.hugboga.custom.fragment.FgCarNew;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.MoneyTextView;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

import static android.view.View.GONE;

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
    MoneyTextView allMoneyText;
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
    MoneyTextView allMoneyTextSku;
    @Bind(R.id.money_pre)
    MoneyTextView moneyPre;

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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(Bundle arg0) {
        EventUtil eventUtil = EventUtil.getInstance();
        eventUtil.source = getIntentSource();
        source = getIntentSource();
        super.onCreate(arg0);
        setContentView(R.layout.fg_single_new);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initHeader();
        setSensorsEvent();
    }

    //神策统计_初始页浏览
    private void setSensorsEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("sku_type", "单次接送");
            properties.put("refer", source);
            SensorsDataAPI.sharedInstance(this).track("buy_view", properties);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //神策统计_确认行程
    private void setSensorsConfirmEvent() {
        try {
            int total = carBean.price;
            if (null != manLuggageBean) {
                int seat1Price = OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean);
                int seat2Price = OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean);
                total += seat1Price + seat2Price;
            }
            JSONObject properties = new JSONObject();
            properties.put("sku_type", "单次接送");
            properties.put("is_appoint_guide", null != collectGuideBean ? true : false);// 指定司导下单
            properties.put("adultNum", manLuggageBean.mans + "");// 出行成人数
            properties.put("childNum", manLuggageBean.childs + "");// 出行儿童数
            properties.put("childseatNum", manLuggageBean.childSeats + "");// 儿童座椅数
            properties.put("car_type", carBean.desc);//车型选择
            properties.put("price_total", total);//费用总计
            properties.put("distance", carListBean.distance);// 全程公里数
            properties.put("geton_time", serverDate + " " + serverTime);// 出发时间
            properties.put("geton_location", startBean.placeName);// 出发地
            properties.put("dest_location", arrivalBean.placeName);// 送达地
            properties.put("service_city", cityBean.name);// 用车城市
            SensorsDataAPI.sharedInstance(this).track("buy_confrim", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (cityBean == null) {
            String startCityId = getIntent().getStringExtra(Constants.PARAMS_CITY_ID);
            if (!TextUtils.isEmpty(startCityId)) {
                cityBean = DBHelper.findCityById(startCityId);
            }
        }
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
        if (!TextUtils.isEmpty(timeText.getText())
                && !TextUtils.isEmpty(useCityTips.getText())
                && !TextUtils.isEmpty(startTitle.getText())
                && !TextUtils.isEmpty(endTitle.getText())) {
            getData();
        }
    }



    String startLocation, termLocation;

    protected void getData() {
        cityId = cityBean.cityId;
        needChildrenSeat = cityBean.childSeatSwitch;
        startLocation = startBean.location;
        termLocation = arrivalBean.location;

        RequestCheckPriceForSingle requestCheckPriceForSingle = new RequestCheckPriceForSingle(activity, 4, airportCode, cityId,
                startLocation, termLocation, serverDate + " " + serverTime, carIds);
        requestData(requestCheckPriceForSingle);
    }

    String carIds = null;
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

        allMoneyText.setText(Tools.getRMB(activity)+ total);
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
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCheckPrice) {
            manLuggageBean = null;
            bottom.setVisibility(View.GONE);
            isNetError = false;
            RequestCheckPrice requestCheckPrice = (RequestCheckPrice) request;
            carListBean = (CarListBean) requestCheckPrice.getData();
            if (carListBean.carList.size() > 0) {
                if(null != collectGuideBean){
                    carListBak = (ArrayList<CarBean>)carListBean.carList.clone();
                    carListBean.carList = CarUtils.getSingleCarBeanList(carListBean.carList,eventData.guideCars);
                }

                if (null == collectGuideBean) {
                    carBean = CarUtils.initCarListData(carListBean.carList).get(0);//carListBean.carList.get(0);
                } else {
                    carBean = carListBean.carList.get(0);
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
    GuideCarEventData eventData;
    ArrayList<GuideCarBean> guideCars;

    //报价返回carlist 删除司导后显示使用
    public ArrayList<CarBean> carListBak;

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CARIDS:
                eventData = (GuideCarEventData)action.getData();
                carIds = eventData.carIds;
                guideCars = eventData.guideCars;
                break;
            case CHOOSE_POI_BACK:
                PoiBean poiBean  = (PoiBean)action.getData();
                if ("from".equals(poiBean.type)) {
                    startBean  = (PoiBean)action.getData();
                    startTips.setVisibility(GONE);
                    startTitle.setVisibility(View.VISIBLE);
                    startDetail.setVisibility(View.VISIBLE);
                    startTitle.setText(startBean.placeName);
                    startDetail.setText(startBean.placeDetail);
                    checkInput();
                } else if ("to".equals(poiBean.type)) {
                    arrivalBean =(PoiBean)action.getData();
                    endTips.setVisibility(GONE);
                    endTitle.setVisibility(View.VISIBLE);
                    endDetail.setVisibility(View.VISIBLE);
                    endTitle.setText(arrivalBean.placeName);
                    endDetail.setText(arrivalBean.placeDetail);
                    checkInput();
                }
                break;

            case CHOOSE_START_CITY_BACK://选择城市返回
                cityBean =  (CityBean)action.getData();
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

                break;
            case MAX_LUGGAGE_NUM://最大行李数
                maxLuuages = (int) action.getData();
                break;
            case CAR_CHANGE_SMALL://车的人数变少
                manLuggageBean = null;
                break;
            case ONBACKPRESS:
                break;
            case CHANGE_GUIDE://更换司导
                collectGuideBean = (CollectGuideBean) action.getData();
                break;
            case GUIDE_DEL://删除司导
                collectGuideBean = null;
                manLuggageBean = null;
                carListBean.carList = carListBak;
                if (null == carListBean) {
                    showCarsLayoutSingle.setVisibility(GONE);
                } else {
                    if (null != carListBean.carList && carListBean.carList.size() > 0) {
                        bottom.setVisibility(View.VISIBLE);
                        carBean = carListBean.carList.get(0);
                        genBottomData(carBean);
                    }
                    initCarFragment(true);
                }
                break;
            case CHANGE_CAR://换车
                carBean = (CarBean) action.getData();
                if (null != carBean) {
                    genBottomData(carBean);
                }
                break;
            case MAN_CHILD_LUUAGE://选择出行人
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
                                Intent intent = new Intent(activity, LoginActivity.class);
                                intent.putExtra("source",getEventSource());
                                startActivity(intent);
                            }
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_C;
    }

    @Override
    public String getEventSource() {
        return "单次订单";
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
                        ApiReportHelper.getInstance().addReport(request);
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
        bundle.putSerializable("carBean", carBean);

        bundle.putString("startCityId", cityBean.cityId + "");
        bundle.putString("endCityId", cityBean.cityId + "");//endCityId);
        bundle.putString("startDate", serverDate);
        bundle.putString("endDate", serverDate);

        bundle.putString("serverTime", serverTime);
        bundle.putString("serverDate", serverDate);

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

        bundle.putInt("type", 4);
        bundle.putString("orderType", "4");

        bundle.putSerializable("manLuggageBean", manLuggageBean);

        Intent intent = new Intent(activity,OrderNewActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE,getIntentSource());
        intent.putExtras(bundle);
        startActivity(intent);

        StatisticClickEvent.singleSkuClick(StatisticConstant.CONFIRM_C,source,carBean.desc+"",(manLuggageBean.mans + manLuggageBean.childs));
        setSensorsConfirmEvent();
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
        bundle.putSerializable("carListBean", carListBean);
        bundle.putBoolean("isDataBack", isDataBack);
        bundle.putBoolean("isNetError", isNetError);
        bundle.putInt("orderType",4);
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


    Intent intent;
    @OnClick({R.id.city_layout, R.id.start_layout, R.id.end_layout, R.id.time_layout, R.id.confirm_journey, R.id.start_tips, R.id.start_title, R.id.start_detail, R.id.end_tips, R.id.end_title, R.id.end_detail})
    public void onClick(View view) {
        HashMap<String, String> map = new HashMap<String, String>();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.city_layout:
                intent = new Intent(this, ChooseCityActivity.class);
                intent.putExtra("source", "下单过程中");
                intent.putExtra(KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_RENT);
                startActivity(intent);
                break;
            case R.id.start_tips:
            case R.id.start_title:
            case R.id.start_detail:
            case R.id.start_layout:
                if (cityBean != null) {
//                    FgPoiSearch fg = new FgPoiSearch();
                    bundle.putString("source", "下单过程中");
                    bundle.putString(KEY_FROM, "from");
                    bundle.putInt(PoiSearchActivity.KEY_CITY_ID, cityBean.cityId);
                    bundle.putString(PoiSearchActivity.KEY_LOCATION, cityBean.location);
                    intent = new Intent(activity,PoiSearchActivity.class);
                    intent.putExtras(bundle);
                    intent.putExtra("mBusinessType",4);
                    startActivity(intent);

                } else {
                    CommonUtils.showToast("先选择城市");
                }
                break;
            case R.id.end_tips:
            case R.id.end_title:
            case R.id.end_detail:
            case R.id.end_layout:
                if(null == cityBean){
                    CommonUtils.showToast("先选择城市");
                    return;
                }
                if (!TextUtils.isEmpty(startTitle.getText())) {
                    bundle.putString("source", "下单过程中");
                    bundle.putString(KEY_FROM, "to");
                    bundle.putInt(PoiSearchActivity.KEY_CITY_ID, cityBean.cityId);
                    bundle.putString(PoiSearchActivity.KEY_LOCATION, cityBean.location);
                    intent = new Intent(activity,PoiSearchActivity.class);
                    intent.putExtras(bundle);
                    intent.putExtra("mBusinessType",4);
                    startActivity(intent);

                } else {
                    CommonUtils.showToast("先选择出发地");
                }
                break;
            case R.id.time_layout:
                if(checkTextIsEmpty()) {
                    showYearMonthDayTimePicker();
                }
                break;
            case R.id.confirm_journey:
                break;
        }
    }

    //检查参数是否已填
    private boolean checkTextIsEmpty(){
        if(null == cityBean){
            CommonUtils.showToast("先选择城市");
            return false;
        }
        if(TextUtils.isEmpty(startTitle.getText())){
            CommonUtils.showToast("先选择出发地");
            return false;
        }
        if(TextUtils.isEmpty(endTitle.getText())){
            CommonUtils.showToast("先选择目的地");
            return false;
        }
        return true;
    }

    DateTimePicker picker;
    public void showYearMonthDayTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        picker = new DateTimePicker(activity, DateTimePicker.YEAR_MONTH_DAY);
        picker.setRange(calendar.get(Calendar.YEAR),calendar.get(Calendar.YEAR)+1);
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                String tmpDate = year + "-" + month + "-" + day;
                String startDate = calendar.get(Calendar.YEAR) +"-"+ (calendar.get(Calendar.MONTH) +1)+"-"+ calendar.get(Calendar.DAY_OF_MONTH);

                if(DateUtils.getDateByStr(tmpDate).before(DateUtils.getDateByStr(startDate))){
                    CommonUtils.showToast("不能选择今天之前的时间");
                    return;
                }
                if(DateUtils.getDistanceDays(startDate,tmpDate)>180){
                    CommonUtils.showToast(R.string.time_out_180);
                }else {
                    serverDate = year + "-" + month + "-" + day;
                    serverTime = hour + ":" + minute;
                    timeText.setText(serverDate + " " + serverTime);
                    checkInput();
                    picker.dismiss();
                }
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