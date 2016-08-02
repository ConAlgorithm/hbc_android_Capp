package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import com.hugboga.custom.activity.DatePickerActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
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
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.umeng.analytics.MobclickAgent;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hugboga.custom.R.id.all_journey_text;
import static com.hugboga.custom.R.id.all_money_left;
import static com.hugboga.custom.R.id.all_money_text_sku;
import static com.hugboga.custom.R.id.go_city_text_click_right;
import static com.hugboga.custom.R.id.time_layout;
import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created  on 16/5/20.
 */
@ContentView(R.layout.fg_sku_new)
public class FgSkuNew extends BaseFragment {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
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
    TextView allMoneyText;
    @Bind(R.id.all_money_left_sku)
    TextView allMoneyLeftSku;
    @Bind(all_money_text_sku)
    TextView allMoneyTextSku;
    @Bind(all_journey_text)
    TextView allJourneyText;
    @Bind(R.id.bottom)
    RelativeLayout bottom;
    @Bind(R.id.sku_city_hotel)
    TextView skuCityHotel;
    @Bind(R.id.time_text_start_end)
    TextView timeTextStartEnd;
    @Bind(R.id.money_pre)
    TextView moneyPre;

    @Override
    protected void initHeader() {
        fgTitle.setText(R.string.sku_new_title);
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setText("常见问题");
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_PROBLEM);
//                bundle.putBoolean(FgWebInfo.CONTACT_SERVICE, true);
//                startFragment(new FgWebInfo(), bundle);

                Intent intent = new Intent(context, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_PROBLEM);
                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                startActivity(intent);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("source", "填写行程页面");
                MobclickAgent.onEvent(getActivity(), "callcenter_oneway", map);
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
    private FgCarSuk fgCarSuk;
    private CarBean carTypeBean;//车型
    private String areaCode;//区号
    private PoiBean startPoiBean;//上车地点

    private boolean showHotal;//是否显示酒店

    @Override
    protected void initView() {
        skuBean = (SkuItemBean) getArguments().getSerializable(SkuDetailActivity.WEB_SKU);
        cityBean = (CityBean) getArguments().getSerializable(SkuDetailActivity.WEB_CITY);
        source = getArguments().getString("source");
        MLog.e("skuBean= " + skuBean);
        if (skuBean == null) return;

        if (skuBean.goodsClass == 1) {//固定
            skuTitle.setText(CityUtils.addImg(getActivity(), skuBean.goodsName, R.mipmap.chaoshengxin));
        } else {//推荐
            skuTitle.setText(CityUtils.addImg(getActivity(), skuBean.goodsName, R.mipmap.chaoziyou));
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
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

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
        RequestPriceSku request = new RequestPriceSku(getActivity(), skuBean.goodsNo, serverDayTime, cityBean.cityId + "");
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

        allMoneyTextSku.setText("￥ " + total);
        if(carListBean.showHotel) {
            moneyPre.setVisibility(View.VISIBLE);
            moneyPre.setText("人均:￥ " + perPrice);
            carListBean.hourseNum = hourseNum;
        }
    }


    CarBean carBean;
    ManLuggageBean manLuggageBean;
    int hotelNum = 1;//几晚
    int hourseNum = 1;//几间房
    int maxLuuages = 0;
    ChooseDateBean chooseDateBean;
    @Subscribe
    public void onEventMainThread(EventAction action) {

        switch (action.getType()) {
            case CHOOSE_DATE:
                chooseDateBean = (ChooseDateBean)action.getData();
                if(chooseDateBean.type == 1){
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
                confirmJourney.setBackgroundColor(getContext().getResources().getColor(R.color.all_bg_yellow));
                manLuggageBean = (ManLuggageBean) action.getData();
                if (null != carBean) {
                    genBottomData(carBean, hourseNum);
                }
                confirmJourney.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkParams()) {
                            if (UserEntity.getUser().isLogin(getActivity())) {
                                if ((carBean.carType == 1 && carBean.capOfPerson == 4
                                        && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 4)
                                        || (carBean.carType == 1 && carBean.capOfPerson == 6 && (Integer.valueOf(manLuggageBean.mans) + Integer.valueOf(manLuggageBean.childs)) == 6)) {
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

                            } else {
                                Bundle bundle = new Bundle();//用于统计
                                bundle.putString("source", "sku下单");
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


    private void goNext() {

        FGOrderNew fgOrderNew = new FGOrderNew();
        Bundle bundle = new Bundle();
        bundle.putString("guideCollectId", "");
        bundle.putSerializable("collectGuideBean", null);
        bundle.putString("source", source);
        bundle.putParcelable("carListBean", carListBean);

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
        bundle.putInt("innum", 0);
        bundle.putString("source", source);
        bundle.putBoolean("isHalfTravel", false);
        bundle.putSerializable("passCityList", null);
        bundle.putParcelable("carBean", CarUtils.carBeanAdapter(carBean));

        if(skuBean.goodsClass == 1){
            bundle.putInt("type", 5);
            bundle.putString("orderType", "5");
        }else{
            bundle.putInt("type", 6);
            bundle.putString("orderType", "6");
        }

        bundle.putSerializable("web_sku", skuBean);
        bundle.putSerializable("web_city", cityBean);
        fgOrderNew.setArguments(bundle);
        startFragment(fgOrderNew);
    }

    FragmentManager fm;
    FgCarNew fgCarNew;
    boolean isNetError  = false;
    private void initCarFragment() {
        fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (null != fgCarNew) {
            transaction.remove(fgCarNew);
        }

        fgCarNew = new FgCarNew();
        Bundle bundle = new Bundle();
        if (getArguments() != null) {
            bundle.putAll(getArguments());
        }
        bundle.putParcelable("carListBean", carListBean);
        bundle.putBoolean("isNetError", isNetError);
        fgCarNew.setArguments(bundle);
        transaction.add(R.id.show_cars_layout_sku, fgCarNew);
        transaction.commit();
    }


    public void showDaySelect() {
//        Intent intent = new Intent(getActivity(),DatePickerActivity.class);
//        intent.putExtra("type",1);
//        intent.putExtra("title","请选择出发日期");
//        startActivity(intent);

        Calendar cal = Calendar.getInstance();
        MyDatePickerListener myDatePickerDialog = new MyDatePickerListener(timeText);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                myDatePickerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal = Calendar.getInstance();
        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        dpd.setMaxDate(cal);
        dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框

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
            timeText.setText(serverDate);
            getData();
//            showTimeSelect();
        }
    }

    public void showTimeSelect() {
        Calendar cal = Calendar.getInstance();
        MyTimePickerDialogListener myTimePickerDialog = new MyTimePickerDialogListener();
        TimePickerDialog datePickerDialog = TimePickerDialog.newInstance(myTimePickerDialog, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        datePickerDialog.show(this.getActivity().getFragmentManager(), "TimePickerDialog");                //显示日期设置对话框
    }


    class MyTimePickerDialogListener implements TimePickerDialog.OnTimeSetListener {


        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            String hour = String.format("%02d", hourOfDay);
            String minuteStr = String.format("%02d", minute);
            serverTime = hour + ":" + minuteStr;
            timeText.setText(serverDate + " " + serverTime);
            getData();
        }
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
            AlertDialogUtils.showAlertDialog(getContext(), getString(R.string.back_alert_msg), "离开", "取消", new DialogInterface.OnClickListener() {
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


    @Override
    public boolean onBackPressed() {
        backPress();
        return true;
    }
}
