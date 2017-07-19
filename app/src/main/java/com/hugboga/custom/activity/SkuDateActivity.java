package com.hugboga.custom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.huangbaoche.hbcframe.widget.monthpicker.model.CalendarDay;
import com.huangbaoche.hbcframe.widget.monthpicker.monthswitchpager.view.MonthSwitchView;
import com.huangbaoche.hbcframe.widget.monthpicker.monthswitchpager.view.MonthView;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.GoodsBookDateBean;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.EffectiveShapeView;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SkuDateActivity extends Activity implements MonthView.OnDayClickListener{

    @Bind(R.id.sku_date_display_iv)
    EffectiveShapeView displayIV;
    @Bind(R.id.sku_date_description_tv)
    TextView descriptionTV;
    @Bind(R.id.sku_date_month_view)
    MonthSwitchView monthView;

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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    private void initMonthView() {
        GoodsBookDateBean bookDateInfo = params.skuItemBean.bookDateInfo;
        Calendar beginDate = Calendar.getInstance();
        try {
            beginDate.setTime(DateUtils.dateDateFormat.parse(bookDateInfo.bookDates[0]));
        } catch (Exception e){
            e.printStackTrace();
        }
        int year = beginDate.get(Calendar.YEAR);
        int month = beginDate.get(Calendar.MONTH) + 1;
        int day = beginDate.get(Calendar.DAY_OF_MONTH);
        CalendarDay startCalendarDay = new CalendarDay(year, month, day);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 6);
        int endYear = endDate.get(Calendar.YEAR);
        int endMonth = endDate.get(Calendar.MONTH) + 1;
        int endDay = endDate.get(Calendar.DAY_OF_MONTH);

        monthView.setData(startCalendarDay, new CalendarDay(endYear, endMonth, endDay));
        monthView.setOnDayClickListener(this);
        monthView.setSelectDay(startCalendarDay);
        serverDate = startCalendarDay.getDayString();
    }

    @OnClick(R.id.sku_date_confirm_tv)
    public void onConfirm() {
        if (!CommonUtils.isLogin(this)) {
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
        finish();
    }

    @Override
    public void onDayClick(CalendarDay calendarDay) {
        serverDate = calendarDay.getDayString();
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

    //神策统计_商品进入下单
    private void setSensorsOnClickEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_id", params.skuItemBean.goodsNo);
            properties.put("hbc_sku_name", params.skuItemBean.goodsName);
            properties.put("hbc_sku_type", params.skuItemBean.goodsClass == 1 ? "固定线路" : "推荐线路");
            SensorsDataAPI.sharedInstance(this).track("sku_buy", properties);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
