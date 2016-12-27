package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestPriceSku;
import com.hugboga.custom.fragment.FgCarNew;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.MoneyTextView;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;
import com.umeng.analytics.MobclickAgent;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hugboga.custom.R.id.all_journey_text;
import static com.hugboga.custom.R.id.all_money_left;
import static com.hugboga.custom.R.id.all_money_text_sku;

import static com.hugboga.custom.R.id.time_layout;


/**
 * Created on 16/8/3.
 */

public class SkuNewActivity extends BaseActivity {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.sku_title)
    TextView skuTitle;
    @Bind(R.id.sku_day)
    TextView skuDay;
    @Bind(R.id.sku_city_line)
    TextView skuCityLine;
    @Bind(R.id.time_left)
    TextView timeLeft;
    @Bind(R.id.time_text)
    TextView timeText;
    @Bind(time_layout)
    LinearLayout timeLayout;
    @Bind(R.id.rl_starttime)
    RelativeLayout rlStarttime;
    @Bind(R.id.confirm_journey)
    TextView confirmJourney;
    @Bind(all_money_left)
    TextView allMoneyLeft;
    @Bind(R.id.all_money_text)
    MoneyTextView allMoneyText;
    @Bind(R.id.all_money_left_sku)
    TextView allMoneyLeftSku;
    @Bind(all_money_text_sku)
    MoneyTextView allMoneyTextSku;
    @Bind(all_journey_text)
    TextView allJourneyText;
    @Bind(R.id.bottom)
    RelativeLayout bottom;
    @Bind(R.id.sku_city_hotel)
    TextView skuCityHotel;
    @Bind(R.id.time_text_start_end)
    TextView timeTextStartEnd;
    @Bind(R.id.money_pre)
    MoneyTextView moneyPre;
    @Bind(R.id.header_right_btn)
    ImageView headerRightImage;

    public void initHeader() {
        headerTitle.setText(R.string.sku_new_title);
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerRightTxt.setVisibility(View.GONE);

        RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(38), UIUtils.dip2px(38));
        headerRightImageParams.rightMargin = UIUtils.dip2px(18);
        headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        headerRightImage.setLayoutParams(headerRightImageParams);
        headerRightImage.setPadding(0,0,0,0);
        headerRightImage.setImageResource(R.mipmap.icon_service);
        headerRightImage.setVisibility(View.VISIBLE);
        headerRightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.getInstance(activity).showCallDialog();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("source", "填写行程页面");
                MobclickAgent.onEvent(activity, "callcenter_oneway", map);
                v.setTag("填写行程页面,calldomestic_oneway,calloverseas_oneway");
            }
        });
    }

    private SkuItemBean skuBean;
    private CityBean cityBean;
    private String serverDate;//包车日期，yyyy-MM-dd
    private String serverTime = "09:00";//时间 HH-mm
    private int adult;//成人数
    private int child;//儿童数
    private boolean needChildrenSeat = false;//是否需要儿童座椅
    private PopupWindow popupWindow;//弹窗
    private CarListBean carListBean;//车型
    private CarBean carTypeBean;//车型
    private String areaCode;//区号
    private PoiBean startPoiBean;//上车地点

    private boolean showHotal;//是否显示酒店

    @Override
    public void onCreate(Bundle arg0) {
        skuBean = (SkuItemBean) getIntent().getSerializableExtra(SkuDetailActivity.WEB_SKU);
        cityBean = (CityBean) getIntent().getSerializableExtra(SkuDetailActivity.WEB_CITY);
        super.onCreate(arg0);
        setContentView(R.layout.fg_sku_new);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initHeader();
        setSensorsEvent();
    }

    //神策统计_初始页浏览
    private void setSensorsEvent() {
        try {
            if (skuBean == null) {
                return;
            }
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", skuBean.goodsClass == 1 ? "固定线路" : "推荐线路");
            properties.put("hbc_refer", getIntentSource());
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
                total += carListBean.hotelPrice  * hourseNum;
            }

            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", skuBean.goodsClass == 1 ? "固定线路" : "推荐线路");
            properties.put("hbc_is_appoint_guide", false);// 指定司导下单
            properties.put("hbc_adultNum", manLuggageBean.mans);// 出行成人数
            properties.put("hbc_childNum", manLuggageBean.childs);// 出行儿童数
            properties.put("hbc_childseatNum", manLuggageBean.childSeats);// 儿童座椅数
            properties.put("hbc_car_type", carBean.desc);//车型选择
            properties.put("hbc_price_total", total);//费用总计
            properties.put("hbc_start_time", serverDate);//出发日期
            properties.put("hbc_sku_id", skuBean.goodsNo);//商品ID
            properties.put("hbc_sku_name", skuBean.goodsName);//商品名称
            SensorsDataAPI.sharedInstance(this).track("buy_confirm", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    protected void initView() {
//        skuBean = (SkuItemBean) getIntent().getSerializableExtra(SkuDetailActivity.WEB_SKU);
//        cityBean = (CityBean) getIntent().getSerializableExtra(SkuDetailActivity.WEB_CITY);
        source = getIntent().getStringExtra("source");
        MLog.e("skuBean= " + skuBean);
        if (skuBean == null) return;

        if (skuBean.goodsClass == 1) {//固定
            skuTitle.setText(CityUtils.addImg(activity, skuBean.goodsName, R.mipmap.chaoshengxin));
        } else {//推荐
            skuTitle.setText(CityUtils.addImg(activity, skuBean.goodsName, R.mipmap.chaoziyou));
        }

        skuCityLine.setText("起止:"+skuBean.places);
        skuDay.setText(getString(R.string.sku_days, skuBean.daysCount));
        needChildrenSeat = cityBean != null && cityBean.childSeatSwitch;

//        public int hotelCostAmount;//天数
//        public int hotelStatus;//是否有酒店
        if (skuBean.hotelStatus == 1) {
            skuCityHotel.setVisibility(View.VISIBLE);
            skuCityHotel.setText("含酒店:" + skuBean.hotelCostAmount + "晚");
        }

        confirmJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkParams();
            }
        });

    }

    private boolean checkParams(){
        if(null == manLuggageBean) {
            CommonUtils.showToast(R.string.add_man_toast);
            return false;
        }
        return true;
    }


    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        bottom.setVisibility(View.GONE);
        carListBean = null;
        isNetError = true;
//        confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
//        confirmJourney.setOnClickListener(null);
        initCarFragment();
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {
        super.onDataRequestCancel(request);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestPriceSku) {
            bottom.setVisibility(View.GONE);
            isNetError = false;
//            confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
//            confirmJourney.setOnClickListener(null);
            manLuggageBean = null;
            carListBean = ((RequestPriceSku) request).getData();
            if (carListBean.carList.size() > 0) {
                carBean = carListBean.carList.get(0);
                bottom.setVisibility(View.VISIBLE);
                if (skuBean.hotelStatus == 1) {
                    carListBean.showHotel = true;
                    hotelNum = skuBean.hotelCostAmount;
                    carListBean.hotelNum = skuBean.hotelCostAmount;
                    carListBean.hourseNum = hourseNum;
                }
                genBottomData(carBean, hourseNum);

            } else {
                bottom.setVisibility(View.GONE);
            }
            initCarFragment();
        }

    }

    String serverDayTime = "";

    private void getData() {
        serverDayTime = serverDate + " " + serverTime + ":00";
        timeTextStartEnd.setVisibility(View.VISIBLE);
        timeTextStartEnd.setText("起止日期:" + serverDate + " ~ " + DateUtils.getEndDateByStr(serverDate, skuBean.daysCount));

        MLog.e("serverDayTime= " + serverDayTime);
        RequestPriceSku request = new RequestPriceSku(activity, skuBean.goodsNo, serverDayTime, cityBean.cityId + "");
        requestData(request);
    }

    int perPrice = 0;

    private void genBottomData(CarBean carBean, int hourseNum) {

        allMoneyLeft.setVisibility(View.GONE);
        allMoneyText.setVisibility(View.GONE);
        allMoneyTextSku.setVisibility(View.GONE);
        allJourneyText.setVisibility(View.GONE);

        allMoneyLeftSku.setVisibility(View.VISIBLE);
        allMoneyTextSku.setVisibility(View.VISIBLE);

        int total = carBean.price;
        if (null != manLuggageBean) {
            int seat1Price = OrderUtils.getSeat1PriceTotal(carListBean, manLuggageBean);
            int seat2Price = OrderUtils.getSeat2PriceTotal(carListBean, manLuggageBean);
            total += seat1Price + seat2Price;
            total += carListBean.hotelPrice  * hourseNum;
            perPrice = total / (manLuggageBean.childs + manLuggageBean.mans);
        }

        allMoneyTextSku.setText(Tools.getRMB(activity) + total);
//        if(carListBean.showHotel) {
            if (manLuggageBean !=null && manLuggageBean.childs + manLuggageBean.mans > 1) {
                moneyPre.setVisibility(View.VISIBLE);
                moneyPre.setText("人均: " + Tools.getRMB(activity)  + perPrice);
                carListBean.hourseNum = hourseNum;
            } else {
                moneyPre.setVisibility(View.GONE);
            }
//        }
    }


    CarBean carBean;
    ManLuggageBean manLuggageBean;
    int hotelNum = 1;//几晚
    int hourseNum = 1;//几间房
    int maxLuuages = 0;
    ChooseDateBean chooseDateBean;

    @Subscribe
    public void onEventMainThread(final EventAction action) {

        switch (action.getType()) {
            case CHOOSE_DATE:
                chooseDateBean = (ChooseDateBean)action.getData();
                if(chooseDateBean.type == 3){
                    serverDate = chooseDateBean.halfDateStr;
                    timeText.setText(serverDate);
                    getData();

                }
                break;
            case MAX_LUGGAGE_NUM:
                maxLuuages = (int)action.getData();
                break;
            case CAR_CHANGE_SMALL:
//                confirmJourney.setBackgroundColor(Color.parseColor("#d5dadb"));
//                confirmJourney.setOnClickListener(null);
                manLuggageBean = null;
                break;
            case SKU_HOTEL_NUM_CHANGE:
                hourseNum = (int) action.getData();
                genBottomData(carBean, hourseNum);
                break;
            case ONBACKPRESS:
//                backPress();
                break;
            case CHANGE_CAR:
                carBean = (CarBean) action.getData();
                if (null != carBean) {
                    genBottomData(carBean, hourseNum);
                }
                break;
            case MAN_CHILD_LUUAGE:
                confirmJourney.setBackgroundColor(activity.getResources().getColor(R.color.all_bg_yellow));
                manLuggageBean = (ManLuggageBean) action.getData();
                if (null != carBean) {
                    genBottomData(carBean, hourseNum);
                }
                confirmJourney.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkParams()) {
                            if (UserEntity.getUser().isLogin(activity)) {
                                if ((carBean.carType == 1 && carBean.capOfPerson == 4
                                        && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 4)
                                        || (carBean.carType == 1 && carBean.capOfPerson == 6 && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 6)) {
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
                                goLogin();
                            }
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    public void goLogin(){
        Intent intent = new Intent(activity,LoginActivity.class);
        intent.putExtra("source",getIntentSource());
        startActivity(intent);
    }

    @Override
    public String getEventId() {
        if(skuBean.goodsClass == 1) {//固定
            return StatisticConstant.LAUNCH_RG;
        }else {
            return StatisticConstant.LAUNCH_RT;
        }
    }

    @Override
    public String getEventSource() {
        if(skuBean.goodsClass == 1) {//固定
            return "固定线路包车";
        }else {
            return "推荐线路包车";
        }
    }

    private void goNext() {
//        FGOrderNew fgOrderNew = new FGOrderNew();
        Bundle bundle = new Bundle();
        bundle.putString("guideCollectId", "");
        bundle.putSerializable("collectGuideBean", null);
        bundle.putString("source", getIntentSource());
        bundle.putSerializable("carListBean", carListBean);

        bundle.putString("startCityId", cityBean.cityId + "");
        bundle.putString("endCityId", cityBean.cityId + "");//endCityId);
        bundle.putString("startDate", serverDate);
        bundle.putString("endDate", serverDate);
        bundle.putString("serverDayTime", serverDayTime + ":00");
        bundle.putString("halfDay", "0");
        bundle.putString("adultNum", manLuggageBean.mans + "");
        bundle.putString("childrenNum", manLuggageBean.childs + "");
        bundle.putString("childseatNum", manLuggageBean.childSeats + "");
        bundle.putString("luggageNum", maxLuuages+"");//manLuggageBean.luggages + "");
        bundle.putString("passCities", "");
        bundle.putString("carTypeName", carBean.desc);
        bundle.putString("startCityName", cityBean.name);
        bundle.putString("dayNums", skuBean.daysCount + "");
        bundle.putSerializable("startBean", cityBean);
        bundle.putSerializable("endBean", cityBean);
        bundle.putInt("outnum", skuBean.daysCount);
        manLuggageBean.luggages = maxLuuages;
        bundle.putSerializable("manLuggageBean",manLuggageBean);
        bundle.putInt("innum", 0);
        bundle.putString("source", source);
        bundle.putBoolean("isHalfTravel", false);
        bundle.putSerializable("passCityList", null);
        bundle.putSerializable("carBean", carBean);

        if(skuBean.goodsClass == 1){
            bundle.putInt("type", 5);
            bundle.putString("orderType", "5");
            StatisticClickEvent.singleSkuClick(StatisticConstant.CONFIRM_RG,getIntentSource(),carBean.desc+"",(manLuggageBean.mans + manLuggageBean.childs));
         }else{
            bundle.putInt("type", 6);
            bundle.putString("orderType", "6");
            StatisticClickEvent.singleSkuClick(StatisticConstant.CONFIRM_RT,getIntentSource(),carBean.desc+"",(manLuggageBean.mans + manLuggageBean.childs));
        }

        bundle.putSerializable("web_sku", skuBean);
        bundle.putSerializable("web_city", cityBean);
//        fgOrderNew.setArguments(bundle);
//        startFragment(fgOrderNew);

        Intent intent = new Intent(activity,OrderNewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

        setSensorsConfirmEvent();
    }

    FragmentManager fm;
    FgCarNew fgCarNew;
    boolean isNetError  = false;
    private void initCarFragment() {
        fm = getSupportFragmentManager();//getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (null != fgCarNew) {
            transaction.remove(fgCarNew);
        }

        fgCarNew = new FgCarNew();
        Bundle bundle = new Bundle();
        bundle.putSerializable("carListBean", carListBean);
        bundle.putBoolean("isNetError", isNetError);
        if(skuBean.goodsClass == 1){
            bundle.putInt("orderType", 5);
        }else{
            bundle.putInt("orderType", 6);
        }
        bundle.putString(Constants.PARAMS_SOURCE, getEventSource());
        fgCarNew.setArguments(bundle);
        transaction.add(R.id.show_cars_layout_sku, fgCarNew);
        transaction.commit();
    }


    public void showDaySelect() {
        Intent intent = new Intent(activity,DatePickerActivity.class);
        intent.putExtra("type",3);
        intent.putExtra("title","请选择出发日期");
        intent.putExtra("chooseDateBean",chooseDateBean);
        startActivity(intent);
    }



    @OnClick({R.id.time_text, time_layout, R.id.confirm_journey})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.time_text:
            case time_layout:
                showDaySelect();
                break;
            case R.id.confirm_journey:
                break;
        }
    }

    private void backPress() {
        if ((!TextUtils.isEmpty(timeText.getText()))) {
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
