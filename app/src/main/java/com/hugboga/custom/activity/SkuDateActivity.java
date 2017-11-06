package com.hugboga.custom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.monthpicker.model.CalendarDay;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.listener.MonthChangeListener;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.view.MonthSwitchView;
import com.hugboga.custom.widget.monthpicker.monthswitchpager.view.MonthView;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CalendarGoodsBean;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SkuCalendarUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.EffectiveShapeView;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SkuDateActivity extends Activity implements MonthView.OnDayClickListener, MonthChangeListener, SkuCalendarUtils.SkuCalendarListenr {

    @Bind(R.id.sku_date_display_iv)
    EffectiveShapeView displayIV;
    @Bind(R.id.sku_date_description_tv)
    TextView descriptionTV;
    @Bind(R.id.sku_date_month_view)
    MonthSwitchView monthView;
    @Bind(R.id.sku_date_info_layout)
    RelativeLayout infoLayout;
    @Bind(R.id.sku_date_calendar_loading_layout)
    FrameLayout calendarLoadingLayout;
    @Bind(R.id.sku_date_guide_name_tv)
    TextView guideNameTV;

    private SkuOrderActivity.Params params;
    private String serverDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (SkuOrderActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (SkuOrderActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.activity_sku_date);
        ButterKnife.bind(this);

        initMonthView();

        Tools.showImage(displayIV, params.skuItemBean.goodsPicture, R.mipmap.line_goods_dafault);
        descriptionTV.setText(params.skuItemBean.goodsName);

        if (params.guidesDetailData != null) {
            RelativeLayout.LayoutParams infoLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, UIUtils.dip2px(120));
            infoLayoutParams.addRule(RelativeLayout.ABOVE, R.id.sku_date_content_layout);
            infoLayout.setLayoutParams(infoLayoutParams);
            guideNameTV.setVisibility(View.VISIBLE);
            guideNameTV.setText(getString(R.string.sku_date_selected_guide) + params.guidesDetailData.guideName);
        }

        setSensorsShowEvent();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkuCalendarUtils.getInstance().onDestory();
    }

    private void initMonthView() {
        SkuCalendarUtils.getInstance().setSkuCalendarListenr(this);

        Calendar beginDate = Calendar.getInstance();
        int year = beginDate.get(Calendar.YEAR);
        int month = beginDate.get(Calendar.MONTH) + 1;
        int day = beginDate.get(Calendar.DAY_OF_MONTH);
        CalendarDay startCalendarDay = new CalendarDay(year, month, day);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 6);
        int endYear = endDate.get(Calendar.YEAR);
        int endMonth = endDate.get(Calendar.MONTH) + 1;
        int endDay = endDate.get(Calendar.DAY_OF_MONTH);
        CalendarDay endCalendarDay = new CalendarDay(endYear, endMonth, endDay);

        monthView.setData(startCalendarDay, endCalendarDay);

        monthView.setOnDayClickListener(this);
        monthView.setMonthChangeListener(this);
    }

    @OnClick(R.id.sku_date_confirm_tv)
    public void onConfirm() {
        if (!CommonUtils.isLogin(this,getEventSource())) {
            return;
        }
        if (TextUtils.isEmpty(serverDate)) {
            CommonUtils.showToast(R.string.sku_date_check_select);
            return;
        }
        params.serverDate = serverDate;
        Intent intent = new Intent(this, SkuOrderActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
        startActivity(intent);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("routename", params.skuItemBean.goodsName);
        MobclickAgent.onEventValue(this, "chose_route", map, CommonUtils.getCountInteger(params.skuItemBean.goodsMinPrice));
        setSensorsOnClickEvent();
    }

    @OnClick({R.id.sku_date_out_side_view, R.id.sku_date_display_iv})
    public void onClose() {
        SkuCalendarUtils.getInstance().onDestory();
        finish();
    }

    @Override
    public void onDayClick(CalendarDay calendarDay) {
        serverDate = calendarDay.getDayString();
    }

    public CalendarDay getEndCalendarDay() {
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 6);
        int endYear = endDate.get(Calendar.YEAR);
        int endMonth = endDate.get(Calendar.MONTH) + 1;
        int endDay = endDate.get(Calendar.DAY_OF_MONTH);
        return new CalendarDay(endYear, endMonth, endDay);
    }

    //获取上个界面的来源
    public String getIntentSource(){
        String intentSource = null;
        Intent intent = getIntent();
        if (null != intent) {
            intentSource = intent.getStringExtra(Constants.PARAMS_SOURCE);
        }
        return intentSource;
    }

    private void setSensorsShowEvent() {
        try {
            JSONObject properties2 = new JSONObject();
            properties2.put("hbc_sku_type", "线路详情");
            properties2.put("hbc_refer", getIntentSource());
            SensorsDataAPI.sharedInstance(this).track("buy_view", properties2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //神策统计_商品进入下单
    private void setSensorsOnClickEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_id", params.skuItemBean.goodsNo);
            properties.put("hbc_sku_name", params.skuItemBean.goodsName);
            properties.put("hbc_sku_type", params.skuItemBean.goodsClass == 1 ? "固定线路" : "推荐线路");
            SensorsDataAPI.sharedInstance(this).track("sku_buy", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMonthChange(Date date) {
        String guideId = "";
        if (params.guidesDetailData != null) {
            guideId = params.guidesDetailData.guideId;
        }
        SkuCalendarUtils.getInstance().sendRequest(this, params.skuItemBean.goodsNo, guideId, date);
    }

    @Override
    public void onCalendarInit(Calendar _selectedCalendar) {
        if (_selectedCalendar != null) {
            CalendarDay selectedCalendarDay = new CalendarDay(_selectedCalendar.get(Calendar.YEAR), _selectedCalendar.get(Calendar.MONTH) + 1, _selectedCalendar.get(Calendar.DAY_OF_MONTH));
            monthView.setSelectDay(selectedCalendarDay);
            serverDate = selectedCalendarDay.getDayString();
        }
    }

    @Override
    public void onCalendarRequestSucceed(boolean isCurrentMonth, ArrayMap<String, CalendarGoodsBean> guideCalendarMap) {
        monthView.setGoodsCalendarMap(guideCalendarMap);
    }

    @Override
    public void onLoadViewShow(boolean isShow) {
        Log.i("aa", "onLoadViewShow " + isShow);
        calendarLoadingLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public String getEventSource() {
        return "商品出行日期";
    }
}
