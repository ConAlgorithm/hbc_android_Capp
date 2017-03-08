package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarMaxCapaCityBean;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCarMaxCapaCity;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.widget.CharterFirstCountView;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.title.TitleBar;

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

    private CityBean startBean;
    private ChooseDateBean chooseDateBean;
    private int maxPassengers;
    private CharterDataUtils charterDataUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @OnClick({R.id.charter_first_city_layout})
    public void selectStartCity() {
        Bundle bundle = new Bundle();
        bundle.putString(ChooseCityActivity.KEY_FROM, ChooseCityActivity.PARAM_TYPE_START);
        bundle.putInt(KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_DAILY);
        bundle.putString(ChooseCityActivity.KEY_FROM_TAG, CharterFirstStepActivity.TAG);
        bundle.putString(Constants.PARAMS_SOURCE, getEventSource());
        Intent intent = new Intent(this, ChooseCityActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
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

        //开始城市变了清除数据、开始日期变了清除、结束日期变了不清除
        boolean isClearTravelData = (charterDataUtils.chooseDateBean != null && !chooseDateBean.start_date.equals(charterDataUtils.chooseDateBean.start_date))
                                    || startBean != charterDataUtils.getStartCityBean(1);
        if (isClearTravelData) {
            charterDataUtils.onDestroy();
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
            countLayout.setMaxPassengers(10);//FIXME v4.0
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
