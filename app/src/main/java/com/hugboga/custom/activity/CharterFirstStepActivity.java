package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarMaxCapaCityBean;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCarMaxCapaCity;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.CharterFirstCountView;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.title.TitleBar;

import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            guidesDetailData = (GuidesDetailData) savedInstanceState.getSerializable(GuideDetailActivity.PARAM_GUIDE_BEAN);
            startBean = (CityBean) savedInstanceState.getSerializable(Constants.PARAMS_START_CITY_BEAN);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                guidesDetailData = (GuidesDetailData) bundle.getSerializable(GuideDetailActivity.PARAM_GUIDE_BEAN);
                startBean = (CityBean) bundle.getSerializable(Constants.PARAMS_START_CITY_BEAN);
            }
        }
        setContentView(R.layout.activity_charter_first);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (charterDataUtils != null) {
            charterDataUtils.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (guidesDetailData != null) {
            outState.putSerializable(GuideDetailActivity.PARAM_GUIDE_BEAN, guidesDetailData);
        }
        if (startBean != null) {
            outState.putSerializable(Constants.PARAMS_START_CITY_BEAN, startBean);
        }
    }

    private void initView() {
        charterDataUtils = CharterDataUtils.getInstance();
        if (guidesDetailData != null) {
            charterDataUtils.guidesDetailData = guidesDetailData;
        }

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
            guideLayout.setVisibility(View.VISIBLE);
            Tools.showImage(avatarIV, guidesDetailData.avatar, R.mipmap.icon_avatar_guide);
            guideTV.setText(String.format("Hi，我是您的司导%1$s，欢迎来到%2$s，期待与您共度美好的旅行时光~", guidesDetailData.guideName, guidesDetailData.countryName));
        }

        if (startBean != null) {
            cityTV.setText(startBean.name);
            requestData(new RequestCarMaxCapaCity(this, startBean.cityId));
        }
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
        intent.putExtra(DatePickerActivity.PARAM_TITLE, "请选择包车开始日期");
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
                requestData(new RequestCarMaxCapaCity(this, startBean.cityId, guidesDetailData.getCarIds()));
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
                startBean = charterDataUtils.getStartCityBean(1);
                chooseDateBean = charterDataUtils.chooseDateBean;
                maxPassengers = charterDataUtils.maxPassengers;
                cityTV.setText(startBean.name);
                setDateViewText();
                countLayout.setAdultValue(charterDataUtils.adultCount);
                countLayout.setChildValue(charterDataUtils.childCount);
                countLayout.setMaxPassengers(maxPassengers);
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCarMaxCapaCity) {
            CarMaxCapaCityBean carMaxCapaCityBean = ((RequestCarMaxCapaCity) _request).getData();
            maxPassengers = carMaxCapaCityBean.numOfPerson;
            countLayout.setMaxPassengers(maxPassengers);
            countLayout.setSliderEnabled(true);
        }
    }

    private void setDateViewText() {
        String dateStr = chooseDateBean.showStartDateStr;
        if (chooseDateBean.dayNums > 1) {
            dateStr += " - " + chooseDateBean.showEndDateStr;
        }
        dateStr += String.format("（%1$s天）", chooseDateBean.dayNums);
        dateTV.setText(dateStr);

        setNextViewEnabled(true);
    }

    public void setNextViewEnabled(boolean isEnabled) {
        if (maxPassengers <= 0 || chooseDateBean == null) {
            return;
        }
        nextTV.setEnabled(isEnabled);
        nextTV.setBackgroundResource(isEnabled ? R.drawable.shape_rounded_yellow_btn : R.drawable.shape_rounded_gray_btn);
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
}
