package com.hugboga.custom.widget.charter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.CharterSecondStepActivity;
import com.hugboga.custom.activity.ChooseAirActivity;
import com.hugboga.custom.activity.ChooseAirPortActivity;
import com.hugboga.custom.activity.ChooseCityActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.utils.CharterDataUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/2/25.
 */
public class CharterSubtitleView extends LinearLayout{

    @Bind(R.id.charter_subtitle_day_tv)
    TextView dayTV;
    @Bind(R.id.charter_subtitle_arrow_iv)
    ImageView arrowIV;

    @Bind(R.id.charter_subtitle_pickup_layout)
    LinearLayout pickupLayout;
    @Bind(R.id.charter_subtitle_pickup_icon_iv)
    ImageView iconIV;
    @Bind(R.id.charter_subtitle_pickup_left_tv)
    TextView leftTV;
    @Bind(R.id.charter_subtitle_pickup_right_tv)
    TextView rightTV;
    @Bind(R.id.charter_subtitle_pickup_arrow_iv)
    ImageView pickupArrowIV;

    private Context context;
    private CharterDataUtils charterDataUtils;
    private OnPickUpOrSendSelectedListener listener;

    public CharterSubtitleView(Context context) {
        this(context, null);
    }

    public CharterSubtitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = inflate(context, R.layout.view_charter_subtitle, this);
        ButterKnife.bind(view);

        charterDataUtils = CharterDataUtils.getInstance();
    }

    public void update() {
        CityBean currentDayCityBean = charterDataUtils.getCurrentDayStartCityBean();
        if (charterDataUtils.chooseDateBean.dayNums == 1) {
            dayTV.setText(currentDayCityBean.name);
        } else {
            dayTV.setText(String.format("Day%1$s: %2$s", charterDataUtils.currentDay, currentDayCityBean.name));
        }

        if (charterDataUtils.isShowEmpty) {
            pickupLayout.setVisibility(View.GONE);
            arrowIV.setVisibility(View.GONE);
            return;
        }

        if (charterDataUtils.isFirstDay()) {
            pickupLayout.setVisibility(View.VISIBLE);
            arrowIV.setVisibility(View.GONE);
            if (charterDataUtils.flightBean != null) {
                iconIV.setBackgroundResource(R.drawable.selector_charter_pickup);
                iconIV.setSelected(charterDataUtils.isSelectedPickUp);
                leftTV.setText("从机场出发 ");
                rightTV.setText(charterDataUtils.flightBean.flightNo);
                rightTV.setTextColor(getContext().getResources().getColor(R.color.default_black));
                pickupArrowIV.setVisibility(View.VISIBLE);
            } else {
                iconIV.setBackgroundResource(R.mipmap.trip_icon_come);
                leftTV.setText("从机场出发");
                rightTV.setText("请添加航班号");
                rightTV.setTextColor(0xFF19B9DA);
                pickupArrowIV.setVisibility(View.GONE);
            }
        } else if( charterDataUtils.isLastDay()) {
            arrowIV.setVisibility(View.VISIBLE);
            pickupLayout.setVisibility(View.VISIBLE);
            if (charterDataUtils.airPortBean != null) {
                iconIV.setBackgroundResource(R.drawable.selector_charter_pickup);
                iconIV.setSelected(charterDataUtils.isSelectedSend);
                leftTV.setText("送机 ");
                rightTV.setText(charterDataUtils.airPortBean.airportName);
                rightTV.setTextColor(getContext().getResources().getColor(R.color.default_black));
                pickupArrowIV.setVisibility(View.VISIBLE);
            } else {
                iconIV.setBackgroundResource(R.mipmap.trip_icon_go);
                leftTV.setText("如需送机");
                rightTV.setText("请选择送机机场");
                rightTV.setTextColor(0xFF19B9DA);
                pickupArrowIV.setVisibility(View.GONE);
            }
        } else {
            arrowIV.setVisibility(View.VISIBLE);
            pickupLayout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.charter_subtitle_pickup_layout})
    public void onClick(View view) {
        if (charterDataUtils.isFirstDay() && (charterDataUtils.isSelectedPickUp || charterDataUtils.flightBean == null)) {//包车第一天，添写接机航班号
            intentActivity(ChooseAirActivity.class);
        } else if (charterDataUtils.isLastDay() && (charterDataUtils.isSelectedSend || charterDataUtils.airPortBean == null)) {//包车最后一天，添写送达机场
            Intent intent = new Intent(context, ChooseAirPortActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
            if (charterDataUtils.getCurrentDayStartCityBean() != null) {
                intent.putExtra(ChooseAirPortActivity.KEY_GROUPID, charterDataUtils.getCurrentDayStartCityBean().groupId);
            }
            context.startActivity(intent);
        }
    }

    @OnClick({R.id.charter_subtitle_pickup_icon_iv})
    public void onSelectedPickUpOrSend() {
        boolean isSelected = true;
        boolean isPickUp = true;
        if (charterDataUtils.isFirstDay()) {
            charterDataUtils.isSelectedPickUp = !charterDataUtils.isSelectedPickUp;
            isSelected = charterDataUtils.isSelectedPickUp;
            isPickUp = true;
        } else if (charterDataUtils.isLastDay()) {
            charterDataUtils.isSelectedSend = !charterDataUtils.isSelectedSend;
            isSelected = charterDataUtils.isSelectedSend;
            isPickUp = false;
        }
        iconIV.setSelected(isSelected);
        if (listener != null) {
            listener.onPickUpOrSendSelected(isPickUp, isSelected);
        }
    }

    private void intentActivity(Class<?> cls) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        context.startActivity(intent);
    }

    private String getEventSource() {
        if (context instanceof BaseActivity) {
            return ((BaseActivity) context).getEventSource();
        } else {
            return "";
        }
    }

    @OnClick({R.id.charter_subtitle_day_tv})
    public void selectStartCity() {
        if (charterDataUtils.isFirstDay()) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(ChooseCityActivity.KEY_FROM, ChooseCityActivity.PARAM_TYPE_START);
        bundle.putInt(ChooseCityActivity.KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_DAILY);
        bundle.putString(ChooseCityActivity.KEY_FROM_TAG, CharterSecondStepActivity.TAG);
        bundle.putInt(ChooseCityActivity.KEY_CITY_ID, charterDataUtils.getStartCityBean(1).cityId);
        bundle.putString(Constants.PARAMS_SOURCE, getEventSource());
        Intent intent = new Intent(context, ChooseCityActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public interface OnPickUpOrSendSelectedListener {
        /**
         * @param isPickUp 接机true、送机false
         */
        public void onPickUpOrSendSelected(boolean isPickUp, boolean isSelected);
    }

    public void setOnPickUpOrSendSelectedListener(OnPickUpOrSendSelectedListener listener) {
        this.listener = listener;
    }
}
