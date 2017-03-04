package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
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
public class CharterFirstStepActivity extends BaseActivity implements CharterFirstCountView.OnOutRangeListener {

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
        CharterSecondStepActivity.Params params = new CharterSecondStepActivity.Params();
        params.startBean = startBean;
        params.chooseDateBean = chooseDateBean;
        params.adultCount = countLayout.getAdultValue();
        params.childCount = countLayout.getChildValue();

        Intent intent = new Intent(this, CharterSecondStepActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_START_CITY_BACK:
                CityBean cityBean = (CityBean) action.getData();
                if (cityBean == null || startBean == cityBean || cityBean.fromTag == CharterFirstStepActivity.TAG) {
                    return;
                }
                startBean = cityBean;
                cityTV.setText(cityBean.name);
                requestData(new RequestCarMaxCapaCity(this, startBean.cityId));
                break;
            case CHOOSE_DATE:
                chooseDateBean = (ChooseDateBean) action.getData();
                if (chooseDateBean.type != DatePickerActivity.PARAM_TYPE_RANGE) {
                    break;
                }
                String dateStr = chooseDateBean.showStartDateStr;
                if (chooseDateBean.dayNums > 1) {
                    dateStr += " - " + chooseDateBean.showEndDateStr;
                }
                dateStr += String.format("（%1$s天）", chooseDateBean.dayNums);
                dateTV.setText(dateStr);

                setNextViewEnabled(true);
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
}
