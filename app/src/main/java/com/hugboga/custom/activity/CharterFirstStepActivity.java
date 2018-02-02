package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarMaxCapaCityBean;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.SeckillsBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCarMaxCapaCity;
import com.hugboga.custom.data.request.RequestGuideCrop;
import com.hugboga.custom.data.request.RequestNewCars;
import com.hugboga.custom.data.request.RequestTravelPurposeForm;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.GuideCalendarUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.UnicornUtils;
import com.hugboga.custom.widget.CharterFirstCountView;
import com.hugboga.custom.widget.ConponsTipView;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.OrderGuidanceView;
import com.hugboga.custom.widget.OrderGuideLayout;
import com.hugboga.custom.widget.OrderInfoItemView;
import com.hugboga.custom.widget.title.TitleBar;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/2/21.
 */
public class CharterFirstStepActivity extends BaseActivity implements CharterFirstCountView.OnOutRangeListener, TitleBar.OnTitleBarBackListener {

    public static final String TAG = CharterFirstStepActivity.class.getSimpleName();

    @BindView(R.id.charter_first_titlebar)
    TitleBar titlebar;
    @BindView(R.id.charter_first_city_layout)
    OrderInfoItemView cityLayout;
    @BindView(R.id.charter_first_date_layout)
    OrderInfoItemView dateLayout;
    @BindView(R.id.charter_first_count_view)
    CharterFirstCountView countLayout;
    @BindView(R.id.charter_first_bottom_next_tv)
    TextView nextTV;
    @BindView(R.id.charter_first_seckills_layout)
    RelativeLayout seckillsLayout;
    @BindView(R.id.charter_first_guidance_layout)
    OrderGuidanceView guidanceLayout;

    @BindView(R.id.charter_first_conpons_tipview)
    ConponsTipView conponsTipView;
    @BindView(R.id.charter_first_scrollview)
    ScrollView scrollView;

    @BindView(R.id.charter_first_guide_layout)
    OrderGuideLayout guideLayout;

    private CityBean startBean;
    private ChooseDateBean chooseDateBean;
    private int maxPassengers;
    private boolean isSupportChildSeat;
    private CharterDataUtils charterDataUtils;
    private GuidesDetailData guidesDetailData;
    private boolean isEnabled = false;
    public SeckillsBean seckillsBean;//秒杀活动参数
    private boolean isExtraStartBean;
    private String sourceTag;

    private boolean isOperated = true;//在页面有任意点击操作就记录下来，只记录第一次，统计需要
    CsDialog csDialog;

    @Override
    public int getContentViewId() {
        return R.layout.activity_charter_first;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            seckillsBean = (SeckillsBean) savedInstanceState.getSerializable(Constants.PARAMS_SECKILLS);
            guidesDetailData = (GuidesDetailData) savedInstanceState.getSerializable(Constants.PARAMS_GUIDE);
            startBean = (CityBean) savedInstanceState.getSerializable(Constants.PARAMS_START_CITY_BEAN);
            sourceTag = savedInstanceState.getString(Constants.PARAMS_TAG);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                seckillsBean = (SeckillsBean) bundle.getSerializable(Constants.PARAMS_SECKILLS);
                guidesDetailData = (GuidesDetailData) bundle.getSerializable(Constants.PARAMS_GUIDE);
                startBean = (CityBean) bundle.getSerializable(Constants.PARAMS_START_CITY_BEAN);
                sourceTag = bundle.getString(Constants.PARAMS_TAG);
            }
        }
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (charterDataUtils.isSeckills()) {
            seckillsLayout.setVisibility(View.VISIBLE);
        } else {
            seckillsLayout.setVisibility(View.GONE);
        }
        updateConponsTipView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (charterDataUtils != null) {
            charterDataUtils.onDestroy();
            charterDataUtils.cleanGuidesDate();
        }
        GuideCalendarUtils.getInstance().onDestory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (guidesDetailData != null) {
            outState.putSerializable(Constants.PARAMS_GUIDE, guidesDetailData);
        }
        if (startBean != null) {
            outState.putSerializable(Constants.PARAMS_START_CITY_BEAN, startBean);
        }
        if (seckillsBean != null) {
            outState.putSerializable(Constants.PARAMS_SECKILLS, seckillsBean);
        }
    }

    private void initView() {
        charterDataUtils = CharterDataUtils.getInstance();

        titlebar.setTitleBarBackListener(this);
        countLayout.setOnOutRangeListener(this);

        OrderUtils.setNextStepText(nextTV, R.string.daily_first_next, R.string.daily_first_choose_scope);

        if (guidesDetailData == null) {
            guideLayout.setVisibility(View.GONE);
            guidanceLayout.setVisibility(View.VISIBLE);
        } else {
            charterDataUtils.guidesDetailData = guidesDetailData;
            startBean = DatabaseManager.getCityBean("" + guidesDetailData.cityId);
            guideLayout.setVisibility(View.VISIBLE);
            guideLayout.setData(guidesDetailData);
            requestData(new RequestGuideCrop(this, guidesDetailData.guideId), false);
            GuideCalendarUtils.getInstance().sendRequest(this, guidesDetailData.guideId, 3);
            guidanceLayout.setVisibility(View.GONE);
        }

        charterDataUtils.seckillsBean = seckillsBean;

        if (startBean != null) {
            cityLayout.setDesc(startBean.name);
            if (guidesDetailData == null) {
                requestCarMaxCapaCity();
                guidanceLayout.setData("" + startBean.cityId, startBean.name);
            } else {
                getGuideCars();
            }
            if (TextUtils.equals(sourceTag, GuidanceOrderActivity.TAG)) {
                isExtraStartBean = false;
            } else {
                isExtraStartBean = true;
            }
        } else {
            isExtraStartBean = false;
        }

        setSensorsBuyRouteEvent();
    }

    @OnClick({R.id.charter_first_city_layout})
    public void selectStartCity() {
        setSensorsOnOperated();
        if (guidesDetailData != null) {
            Intent intent = new Intent(this, ChooseGuideCityActivity.class);
            intent.putExtra(Constants.PARAMS_ID, guidesDetailData.guideId);
            intent.putExtra(Constants.PARAMS_SOURCE, getReferH5EventSource(getEventSource()));
            startActivity(intent);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(ChooseCityActivity.KEY_FROM, ChooseCityActivity.PARAM_TYPE_START);
            bundle.putInt(KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_DAILY);
            bundle.putString(ChooseCityActivity.KEY_FROM_TAG, CharterFirstStepActivity.TAG);
            bundle.putString(Constants.PARAMS_SOURCE, getReferH5EventSource(getEventSource()));
            Intent intent = new Intent(this, ChooseCityActivity.class);
            intent.putExtra("fromInterCity", true);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        SensorsUtils.onAppClick(getEventSource(), "下一步", getIntentSource());
        overridePendingTransition(R.anim.push_bottom_in, 0);
    }

    @OnClick({R.id.charter_first_bottom_service_layout, R.id.charter_first_bottom_online_layout})
    public void onService(View view) {
        setSensorsOnOperated();
        switch (view.getId()) {
            case R.id.charter_first_bottom_service_layout:
                //DialogUtil.showCallDialogTitle(this,getEventSource(),UnicornServiceActivity.SourceType.TYPE_CHARTERED);
                csDialog = CommonUtils.csDialog(activity, null, null, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, "按天包车", false, new CsDialog.OnCsListener() {
                    @Override
                    public void onCs() {
                        csDialog.dismiss();
                    }
                });
                SensorsUtils.onAppClick(getEventSource(), "联系客服", getIntentSource());
                break;
            case R.id.charter_first_bottom_online_layout:
                UnicornUtils.openServiceActivity(this, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
                SensorsUtils.onAppClick(getEventSource(), "在线咨询", getIntentSource());
                break;
        }
    }

    @OnClick({R.id.charter_first_date_layout})
    public void selectDate() {
        setSensorsOnOperated();
        Intent intent = new Intent(activity, DatePickerActivity.class);
        if (guidesDetailData != null) {
            intent.putExtra(DatePickerActivity.PARAM_ASSIGN_GUIDE, true);
        }
        intent.putExtra(Constants.PARAMS_ORDER_TYPE, Constants.BUSINESS_TYPE_DAILY);
        intent.putExtra(DatePickerActivity.PARAM_TYPE, charterDataUtils.isSeckills() ? DatePickerActivity.PARAM_TYPE_SINGLE : DatePickerActivity.PARAM_TYPE_RANGE);
        intent.putExtra(DatePickerActivity.PARAM_BEAN, chooseDateBean);
        intent.putExtra(DatePickerActivity.PARAM_TITLE, getString(R.string.daily_first_date_picker_title));
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        startActivity(intent);
        SensorsUtils.onAppClick(getEventSource(), "包车日期", getIntentSource());
    }

    @OnClick({R.id.charter_first_bottom_next_tv})
    public void nextStep() {

        //开始城市、开始日期改变了清除全部数据
        boolean isChangeDate = (charterDataUtils.chooseDateBean != null && !chooseDateBean.start_date.equals(charterDataUtils.chooseDateBean.start_date));
        boolean isChangeCity = charterDataUtils.getStartCityBean(1) != null && startBean.cityId != charterDataUtils.getStartCityBean(1).cityId;
        if (isChangeDate || isChangeCity) {
            charterDataUtils.onDestroy();
        }
        if (charterDataUtils.chooseDateBean != null) {
            if (chooseDateBean.dayNums > charterDataUtils.chooseDateBean.dayNums) {//增加了天数
                if (charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null) {//最有一天选了送机，清空当天数据
                    charterDataUtils.resetSendInfo();
                    charterDataUtils.cleanDayDate(chooseDateBean.dayNums);
                }
            } else if (chooseDateBean.dayNums < charterDataUtils.chooseDateBean.dayNums) {//减少了天数
                for (int i = charterDataUtils.travelList.size(); i >= chooseDateBean.dayNums; i--) {
                    if (i == chooseDateBean.dayNums) {
                        if (charterDataUtils.travelList.get(i - 1).routeType == CityRouteBean.RouteType.AT_WILL) {
                            charterDataUtils.cleanDayDate(i);
                            charterDataUtils.addStartCityBean(i, charterDataUtils.setDefaultCityBean(i));
                        }
                        continue;
                    }
                    charterDataUtils.cleanDayDate(i);
                    if (charterDataUtils.currentDay > chooseDateBean.dayNums) {
                        charterDataUtils.currentDay = chooseDateBean.dayNums;
                    }
                }
            }
        }
        charterDataUtils.chooseDateBean = chooseDateBean;
        charterDataUtils.adultCount = countLayout.getAdultValue();
        charterDataUtils.childCount = countLayout.getChildValue();
        charterDataUtils.childSeatCount = countLayout.getChildSeatValue();
        charterDataUtils.isSupportChildSeat = isSupportChildSeat;
        charterDataUtils.maxPassengers = maxPassengers;
        charterDataUtils.addStartCityBean(1, startBean);

        Intent intent = new Intent(this, CharterSecondStepActivity.class);
        startActivity(intent);

        StatisticClickEvent.dailyClick(StatisticConstant.CONFIRM_R, getIntentSource(), chooseDateBean.dayNums,
                guidesDetailData != null, (countLayout.getAdultValue() + countLayout.getChildValue()) + "");
        setSensorsConfirmEvent();
        SensorsUtils.onAppClick(getEventSource(), "下一步", getIntentSource());
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_START_CITY_BACK:
                CityBean cityBean = (CityBean) action.getData();
                if (cityBean == null || startBean == cityBean || !CharterFirstStepActivity.TAG.equals(cityBean.fromTag)) {
                    return;
                }
                startBean = cityBean;
                cityLayout.setDesc(cityBean.name);
                isExtraStartBean = false;
                requestCarMaxCapaCity();
                if (guidesDetailData == null) {
                    guidanceLayout.setVisibility(View.VISIBLE);
                    guidanceLayout.setData("" + startBean.cityId, startBean.name);
                }
                break;
            case CHOOSE_GUIDE_CITY_BACK:
                ChooseGuideCityActivity.GuideServiceCitys guideServiceCitys = (ChooseGuideCityActivity.GuideServiceCitys) action.getData();
                charterDataUtils.guideCropList = guideServiceCitys.guideCropList;
                startBean = guideServiceCitys.getSelectedCityBean();
                cityLayout.setDesc(startBean.name);
                isExtraStartBean = false;
                getGuideCars();
                break;
            case CHOOSE_DATE:
                ChooseDateBean _chooseDateBean = (ChooseDateBean) action.getData();
                this.chooseDateBean = _chooseDateBean;
                setDateViewText();
                break;
            case CHARTER_FIRST_REFRESH:
                if (charterDataUtils.guidesDetailData == null) {
                    guideLayout.setVisibility(View.GONE);
                    guidesDetailData = null;
                }
                startBean = charterDataUtils.getStartCityBean(1);
                chooseDateBean = charterDataUtils.chooseDateBean;
                maxPassengers = charterDataUtils.maxPassengers;
                isSupportChildSeat = charterDataUtils.isSupportChildSeat;
                cityLayout.setDesc(startBean.name);
                isExtraStartBean = false;
                setDateViewText();
                countLayout.setMaxPassengers(false, maxPassengers, isSupportChildSeat, guidesDetailData != null, charterDataUtils.isSeckills());
                if (guidesDetailData == null) {
                    guidanceLayout.setVisibility(View.VISIBLE);
                    guidanceLayout.setData("" + startBean.cityId, startBean.name);
                }
                break;
            case FROM_PURPOSER:
                finish();
            case CLICK_USER_LOGIN:
            case CLICK_USER_LOOUT:
                updateConponsTipView();
                break;

        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCarMaxCapaCity) {
            CarMaxCapaCityBean carMaxCapaCityBean = ((RequestCarMaxCapaCity) _request).getData();
            maxPassengers = carMaxCapaCityBean.numOfPerson;
            isSupportChildSeat = carMaxCapaCityBean.isSupportChildSeat();
            countLayout.setCountViewEnabled(true);
            countLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing() && countLayout != null) {
                        countLayout.setMaxPassengers(true, maxPassengers, isSupportChildSeat, guidesDetailData != null, charterDataUtils.isSeckills());
                    }
                }
            });
            setNextViewEnabled(true);
            isEnabled = true;
        } else if (_request instanceof RequestGuideCrop) {
            charterDataUtils.guideCropList = ((RequestGuideCrop) _request).getData();
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        super.onDataRequestError(errorInfo, _request);
        if (!isFinishing() && _request instanceof RequestCarMaxCapaCity) {
            if (countLayout != null) {
                setNextViewEnabled(false);
                countLayout.setCountViewEnabled(false);
                isEnabled = false;
            }
        }
    }

    private void setDateViewText() {
        String dateStr = chooseDateBean.showStartDateStr;
        if (chooseDateBean.dayNums > 1) {
            dateStr += " - " + chooseDateBean.showEndDateStr;
        }
        dateStr += String.format("（%1$s天）", chooseDateBean.dayNums);
        dateLayout.setDesc(dateStr);

        if (isEnabled) {
            setNextViewEnabled(true);
        }
    }

    public void setNextViewEnabled(boolean isEnabled) {
        if (maxPassengers <= 0 || chooseDateBean == null) {
            return;
        }
        nextTV.setEnabled(isEnabled);
        nextTV.setBackgroundResource(isEnabled ? R.drawable.shape_rounded_yellow_btn : R.drawable.shape_rounded_gray_btn);
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_R;
    }

    @Override
    public String getEventSource() {
        return getString(R.string.custom_chartered);
    }

    @Override
    public void onOutRangeChange(boolean isOut) {
        if (isOut) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    private boolean isShowSaveDialog() {
        if ((startBean != null && !isExtraStartBean) || chooseDateBean != null) {
            AlertDialogUtils.showAlertDialog(CharterFirstStepActivity.this, getString(R.string.hint), getString(R.string.daily_first_purpose_desc), getString(R.string.daily_first_purpose_confirm)
                    , getString(R.string.dialog_btn_back), getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SensorsUtils.onAppClick(getEventSource(), "帮我规划", getIntentSource());
                            if (UserEntity.getUser().isLogin(CharterFirstStepActivity.this)
                                    && !TextUtils.isEmpty(UserEntity.getUser().getPhone(CharterFirstStepActivity.this))) {
                                showIntentionDialog(String.format(getString(R.string.daily_first_purpose_service), UserEntity.getUser().getPhone(CharterFirstStepActivity.this)));
                            } else {
                                Intent intent = new Intent(CharterFirstStepActivity.this, TravelPurposeFormActivity.class);
                                intent.putExtra("cityName", startBean != null ? startBean.name : "");
                                intent.putExtra("cityId", startBean != null ? startBean.cityId : 0);
                                intent.putExtra("startDate", chooseDateBean != null ? chooseDateBean.start_date : "");
                                intent.putExtra("days", chooseDateBean != null ? chooseDateBean.dayNums : 0);
                                intent.putExtra("adultNum", countLayout.getAdultValue());
                                intent.putExtra("childNum", countLayout.getChildValue());
                                intent.putExtra("isFromOrder", true);
                                startActivity(intent);
                            }
                            dialog.dismiss();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SensorsUtils.onAppClick(getEventSource(), "确认离开", getIntentSource());
                            dialog.dismiss();
                            CharterFirstStepActivity.this.finish();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SensorsUtils.onAppClick(getEventSource(), "取消", getIntentSource());
                            dialog.dismiss();
                        }
                    });
            return true;
        } else {
            return false;
        }
    }

    public void showIntentionDialog(String content) {
        AlertDialogUtils.showAlertDialog(this, false, getString(R.string.hint), content, getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestTravelPurposeForm();
                dialog.dismiss();
                CharterFirstStepActivity.this.finish();
            }
        });
    }

    public void requestCarMaxCapaCity() {
        String carIds = "";
        if (guidesDetailData != null) {
            carIds = guidesDetailData.getCarIds();
        }
        String activityNo = "";
        if (charterDataUtils != null && charterDataUtils.seckillsBean != null) {
            activityNo = charterDataUtils.seckillsBean.timeLimitedSaleNo;
        }
        requestData(new RequestCarMaxCapaCity(CharterFirstStepActivity.this, startBean.cityId, carIds, activityNo));
    }

    public void requestTravelPurposeForm() {
        UserEntity userEntity = UserEntity.getUser();
        RequestTravelPurposeForm requestTravelPurposeForm = new RequestTravelPurposeForm(this
                , userEntity.getUserId(this)
                , userEntity.getUserName(this)
                , ""
                , ""
                , startBean != null ? startBean.cityId : 0
                , startBean != null ? startBean.name : ""
                , chooseDateBean != null ? chooseDateBean.start_date : ""
                , ""
                , userEntity.getAreaCode(this)
                , userEntity.getPhone(this)
                , userEntity.getUserName(this)
                , chooseDateBean != null ? chooseDateBean.dayNums : 0
                , countLayout.getAdultValue()
                , countLayout.getChildValue());
        HttpRequestUtils.request(this, requestTravelPurposeForm, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            }
        }, false);
    }

    @Override
    public boolean onTitleBarBack() {
        return isShowSaveDialog();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (isShowSaveDialog()) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void getGuideCars() {
        if (guidesDetailData == null) {
            return;
        }
        RequestNewCars requestCars = new RequestNewCars(this, 1, guidesDetailData.guideId, null);
        HttpRequestUtils.request(this, requestCars, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                ArrayList<GuideCarBean> guideCarBeanList = ((RequestNewCars) request).getData();
                if (guideCarBeanList == null) {
                    CommonUtils.showToast(R.string.daily_guide_cars_null);
                    return;
                }
                guidesDetailData.guideCars = guideCarBeanList;
                guidesDetailData.guideCarCount = guideCarBeanList.size();
                requestCarMaxCapaCity();
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                CommonUtils.apiErrorShowService(CharterFirstStepActivity.this, errorInfo, request, CharterFirstStepActivity.this.getEventSource());
            }
        }, true);
    }

    public void updateConponsTipView() {
        if (charterDataUtils.isSeckills()) {
            conponsTipView.setVisibility(View.GONE);
            return;
        }
        conponsTipView.update(3);
    }

    //神策统计_确认行程
    private void setSensorsConfirmEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", "按天包车游");
            properties.put("hbc_guide_id", guidesDetailData != null ? guidesDetailData.guideId : "");// 指定司导下单
            properties.put("hbc_adultNum", countLayout.getAdultValue());// 出行成人数
            properties.put("hbc_childNum", countLayout.getChildValue());// 出行儿童数
            properties.put("hbc_start_time", chooseDateBean.start_date);// 出发日期
            properties.put("hbc_end_time", chooseDateBean.end_date);// 结束日期
            properties.put("hbc_service_city", startBean != null ? startBean.name : ""); // 用车城市
            properties.put("hbc_total_days", chooseDateBean.dayNums);// 游玩天数
            SensorsDataAPI.sharedInstance(this).track("buy_confirm", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //神策统计_下单-有操作
    private void setSensorsOnOperated() {
        if (isOperated) {
            isOperated = false;
            SensorsUtils.onOperated(getIntentSource(), getEventSource());
        }
    }

    //神策统计_来到填行程页
    private void setSensorsBuyRouteEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_refer", getIntentSource());
            properties.put("hbc_guide_id", guidesDetailData != null ? guidesDetailData.guideId : "");
            properties.put("hbc_sku_type", "按天包车游");
            SensorsDataAPI.sharedInstance(this).track("buy_route", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
