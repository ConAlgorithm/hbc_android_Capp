package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
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
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCarMaxCapaCity;
import com.hugboga.custom.data.request.RequestGuideCrop;
import com.hugboga.custom.data.request.RequestNewCars;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.CharterFirstCountView;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.title.TitleBar;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;

import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/2/21.
 */
public class CharterFirstStepActivity extends BaseActivity implements CharterFirstCountView.OnOutRangeListener, TitleBar.OnTitleBarBackListener{

    public static final String TAG = CharterFirstStepActivity.class.getSimpleName();

    @Bind(R.id.charter_first_titlebar)
    TitleBar titlebar;
    @Bind(R.id.charter_first_city_tv)
    TextView cityTV;
    @Bind(R.id.charter_first_date_tv)
    TextView dateTV;
    @Bind(R.id.charter_first_count_view)
    CharterFirstCountView countLayout;
    @Bind(R.id.charter_first_bottom_next_tv)
    TextView nextTV;

    @Bind(R.id.charter_first_guide_layout)
    LinearLayout guideLayout;
    @Bind(R.id.charter_first_guide_avatar_iv)
    PolygonImageView avatarIV;
    @Bind(R.id.charter_first_guide_tv)
    TextView guideTV;

    private CityBean startBean;
    private ChooseDateBean chooseDateBean;
    private int maxPassengers;
    private CharterDataUtils charterDataUtils;
    private GuidesDetailData guidesDetailData;
    private boolean isEnabled = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_charter_first;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            guidesDetailData = (GuidesDetailData) savedInstanceState.getSerializable(GuideWebDetailActivity.PARAM_GUIDE_BEAN);
            startBean = (CityBean) savedInstanceState.getSerializable(Constants.PARAMS_START_CITY_BEAN);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                guidesDetailData = (GuidesDetailData) bundle.getSerializable(GuideWebDetailActivity.PARAM_GUIDE_BEAN);
                startBean = (CityBean) bundle.getSerializable(Constants.PARAMS_START_CITY_BEAN);
            }
        }
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (charterDataUtils != null) {
            charterDataUtils.onDestroy();
            charterDataUtils.cleanGuidesDate();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (guidesDetailData != null) {
            outState.putSerializable(GuideWebDetailActivity.PARAM_GUIDE_BEAN, guidesDetailData);
        }
        if (startBean != null) {
            outState.putSerializable(Constants.PARAMS_START_CITY_BEAN, startBean);
        }
    }

    private void initView() {
        charterDataUtils = CharterDataUtils.getInstance();

        titlebar.setTitleBarBackListener(this);
        titlebar.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showServiceDialog(CharterFirstStepActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
            }
        });

        countLayout.setOnOutRangeListener(this);

        if (guidesDetailData == null) {
            guideLayout.setVisibility(View.GONE);
        } else {
            charterDataUtils.guidesDetailData = guidesDetailData;
            startBean = DatabaseManager.getCityBean("" + guidesDetailData.cityId);
            guideLayout.setVisibility(View.VISIBLE);
            Tools.showImage(avatarIV, guidesDetailData.avatar, R.mipmap.icon_avatar_guide);
            guideTV.setText(String.format("Hi，我是您的司导%1$s，欢迎来到%2$s，期待与您共度美好的旅行时光~", guidesDetailData.guideName, guidesDetailData.countryName));
            requestData(new RequestGuideCrop(this, guidesDetailData.guideId));
        }

        if (startBean != null) {
            cityTV.setText(startBean.name);
            if (guidesDetailData == null) {
                requestData(new RequestCarMaxCapaCity(this, startBean.cityId));
            } else {
                getGuideCars();
            }
        }

        setSensorsEvent();
    }

    @OnClick({R.id.charter_first_city_layout})
    public void selectStartCity() {
        if (guidesDetailData != null) {
            Intent intent = new Intent(this, ChooseGuideCityActivity.class);
            intent.putExtra(Constants.PARAMS_ID, guidesDetailData.guideId);
            startActivity(intent);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(ChooseCityActivity.KEY_FROM, ChooseCityActivity.PARAM_TYPE_START);
            bundle.putInt(KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_DAILY);
            bundle.putString(ChooseCityActivity.KEY_FROM_TAG, CharterFirstStepActivity.TAG);
            bundle.putString(Constants.PARAMS_SOURCE, getEventSource());
            Intent intent = new Intent(this, ChooseCityActivity.class);
            intent.putExtra("fromInterCity", true);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        overridePendingTransition(R.anim.push_bottom_in, 0);
    }

    @OnClick({R.id.charter_first_date_layout})
    public void selectDate() {
        Intent intent = new Intent(activity, DatePickerActivity.class);
        intent.putExtra(DatePickerActivity.PARAM_TYPE, DatePickerActivity.PARAM_TYPE_RANGE);
        intent.putExtra(DatePickerActivity.PARAM_BEAN, chooseDateBean);
        intent.putExtra(DatePickerActivity.PARAM_TITLE, "请选择包车日期");
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        startActivity(intent);
    }

    @OnClick({R.id.charter_first_bottom_next_tv})
    public void nextStep() {

        //开始城市、开始日期改变了清除全部数据
        boolean isChangeDate = (charterDataUtils.chooseDateBean != null && !chooseDateBean.start_date.equals(charterDataUtils.chooseDateBean.start_date));
        boolean isChangeCity  = charterDataUtils.getStartCityBean(1) != null && startBean.cityId != charterDataUtils.getStartCityBean(1).cityId;
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

        CharterSecondStepActivity.Params params = new CharterSecondStepActivity.Params();
        params.startBean = startBean;
        params.chooseDateBean = chooseDateBean;
        params.adultCount = countLayout.getAdultValue();
        params.childCount = countLayout.getChildValue();
        params.maxPassengers = maxPassengers;

        Intent intent = new Intent(this, CharterSecondStepActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);

        StatisticClickEvent.dailyClick(StatisticConstant.CONFIRM_R, getIntentSource(), chooseDateBean.dayNums,
                guidesDetailData != null, (countLayout.getAdultValue() + countLayout.getChildValue()) + "");
        setSensorsConfirmEvent();
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_START_CITY_BACK:
                CityBean cityBean = (CityBean) action.getData();
                if (cityBean == null || startBean == cityBean || !CharterFirstStepActivity.TAG.endsWith(cityBean.fromTag)) {
                    return;
                }
                startBean = cityBean;
                cityTV.setText(cityBean.name);
                requestData(new RequestCarMaxCapaCity(this, startBean.cityId));
                break;
            case CHOOSE_GUIDE_CITY_BACK:
                ChooseGuideCityActivity.GuideServiceCitys guideServiceCitys = (ChooseGuideCityActivity.GuideServiceCitys) action.getData();
                charterDataUtils.guideCropList = guideServiceCitys.guideCropList;
                startBean = guideServiceCitys.getSelectedCityBean();
                cityTV.setText(startBean.name);
                getGuideCars();
                break;
            case CHOOSE_DATE:
                ChooseDateBean _chooseDateBean = (ChooseDateBean) action.getData();
                if (_chooseDateBean.type != DatePickerActivity.PARAM_TYPE_RANGE) {
                    break;
                }
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
                cityTV.setText(startBean.name);
                setDateViewText();
                countLayout.setAdultValue(charterDataUtils.adultCount);
                countLayout.setChildValue(charterDataUtils.childCount);
                countLayout.setMaxPassengers(maxPassengers, guidesDetailData != null);
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCarMaxCapaCity) {
            CarMaxCapaCityBean carMaxCapaCityBean = ((RequestCarMaxCapaCity) _request).getData();
            maxPassengers = carMaxCapaCityBean.numOfPerson;
            mHandler.sendEmptyMessageDelayed(1, 200);//FIXME: 17/5/6 临时办法，待优化
            countLayout.setSliderEnabled(true);
            setNextViewEnabled(true);
            isEnabled = true;
        } else if (_request instanceof RequestGuideCrop) {
            charterDataUtils.guideCropList = ((RequestGuideCrop) _request).getData();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    countLayout.setMaxPassengers(maxPassengers, guidesDetailData != null);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        super.onDataRequestError(errorInfo, _request);
        if (_request instanceof RequestCarMaxCapaCity) {
            setNextViewEnabled(false);
            countLayout.setAdultValue(0);
            countLayout.setChildValue(0);
            countLayout.setSliderEnabled(false);
            countLayout.setHintViewVisibility(View.GONE);
            isEnabled = false;
        }
    }

    private void setDateViewText() {
        String dateStr = chooseDateBean.showStartDateStr;
        if (chooseDateBean.dayNums > 1) {
            dateStr += " - " + chooseDateBean.showEndDateStr;
        }
        dateStr += String.format("（%1$s天）", chooseDateBean.dayNums);
        dateTV.setText(dateStr);

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
        setNextViewEnabled(!isOut);
    }

    private boolean isShowSaveDialog() {
        if (startBean != null || chooseDateBean != null) {
            AlertDialogUtils.showAlertDialogCancelable(this, "订单未填写完，要离开吗？", "取消", "离开", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CharterFirstStepActivity.this.finish();
                    dialog.dismiss();
                }
            });
            return true;
        } else {
            return false;
        }
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
        RequestNewCars requestCars = new RequestNewCars(this, 1, guidesDetailData.guideId, null, 20, 0);
        HttpRequestUtils.request(this, requestCars, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                ArrayList<GuideCarBean> guideCarBeanList = ((RequestNewCars)request).getData();
                if (guideCarBeanList == null) {
                    CommonUtils.showToast("很抱歉，该司导暂无符合的车型");
                    return;
                }
                guidesDetailData.guideCars = guideCarBeanList;
                guidesDetailData.guideCarCount = guideCarBeanList.size();
                requestData(new RequestCarMaxCapaCity(CharterFirstStepActivity.this, startBean.cityId, guidesDetailData.getCarIds()));
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

    //神策统计_确认行程
    private void setSensorsConfirmEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", "按天包车游");
            properties.put("hbc_is_appoint_guide", guidesDetailData != null ? true : false);// 指定司导下单
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

    //神策统计_初始页浏览
    private void setSensorsEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_refer", getIntentSource());
            properties.put("hbc_sku_type", "按天包车游");
            SensorsDataAPI.sharedInstance(this).track("buy_view", properties);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
