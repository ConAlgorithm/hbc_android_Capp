package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.PromiseAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.constants.ResourcesConstants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.DailyBean;
import com.hugboga.custom.data.bean.PromiseBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.DateUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 夸城市包车
 * Created by admin on 2016/1/14.
 */
@ContentView(R.layout.include_daily_outtown)
public class FgDailyOutTown extends BaseFragment {

    @ViewInject(R.id.daily_from)
    private TextView wContent;
    @ViewInject(R.id.daily_to)
    private TextView tContent;
    @ViewInject(R.id.daily_date_start)
    private TextView sDateTime;
    @ViewInject(R.id.daily_date_end)
    private TextView eDateTime;
    @ViewInject(R.id.daily_day_start)
    private TextView inTownDaysTv;
    @ViewInject(R.id.daily_day_end)
    private TextView outTownDaysTv;

    @ViewInject(R.id.bottom_promise_wait)
    private TextView promiseWait;
    @ViewInject(R.id.bottom_promise_app)
    private TextView promiseApp;

    @ViewInject(R.id.daily_city_tips)
    private TextView cityTips;

    private CityBean startBean;//起始地
    private CityBean arrivalBean;//达到目的地
    private ArrayList<CityBean> passCityList;//途径城市

    private String startDate;
    private String endDate;
    private int mTotalDays = 2;

    private DailyBean bean = new DailyBean();

    @Override
    protected int getBusinessType() {
        mBusinessType = Constants.BUSINESS_TYPE_DAILY;
        setGoodsType(Constants.BUSINESS_TYPE_DAILY_SHORT);
        return mBusinessType;
    }

    @Override
    protected void initView() {
        CityBean cityBean = (CityBean) getArguments().getSerializable(FgDaily.KEY_CITY_BEAN);
        startBean = cityBean;
        promiseWait.setVisibility(View.GONE);
        promiseApp.setVisibility(View.VISIBLE);
        setCityInfo();
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {
    }

    @Event({R.id.daily_btn,
            R.id.daily_from_layout,
            R.id.daily_to_layout,
            R.id.daily_start_layout,
            R.id.daily_end_layout,
            R.id.bottom_promise_layout,
            R.id.submit_order_tip,
            R.id.daily_day_start_layout,
            R.id.daily_day_end_layout,
            R.id.daily_layout_1,
            R.id.daily_layout_2,
    })
    private void onClickView(View view) {
        Bundle bundle;
        HashMap<String,String> map = new HashMap<String,String>();
        switch (view.getId()) {
            case R.id.daily_from_layout:
                bundle = new Bundle();
                bundle.putString(KEY_FROM, "startAddress");
                bundle.putString("source","下单过程中");
                startFragment(new FgChooseCity(), bundle);
                map.put("source", "下单过程中");
                MobclickAgent.onEvent(getActivity(), "search_trigger", map);
                break;
            case R.id.daily_to_layout:
                if (startBean != null) {
                    bundle = new Bundle();
                    bundle.putString(KEY_FROM, "endAddress");
                    bundle.putString("source","下单过程中");
                    bundle.putInt(FgChooseCity.KEY_CITY_ID, startBean.cityId);
                    startFragment(new FgChooseCity(), bundle);
                    map.put("source", "下单过程中");
                    MobclickAgent.onEvent(getActivity(), "search_trigger", map);
                } else {
                    Toast.makeText(getActivity(), "先选择起始城市", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.daily_start_layout:
                showDatePicker(sDateTime, null);
                break;
            case R.id.daily_end_layout:
                showDatePicker(eDateTime, sDateTime.getText().toString());
                break;
            case R.id.daily_day_start_layout:
                showSelectDay(1, 0, mTotalDays - 1);
                break;
            case R.id.daily_day_end_layout:
                showSelectDay(0, 1, mTotalDays);
                break;
            case R.id.daily_btn:
                startFgCar();
                break;
            case R.id.bottom_promise_layout:
                showPromiseDialog();
                break;
            case R.id.submit_order_tip:
                bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_NOTICE_V2_2);
                startFragment(new FgWebInfo(), bundle);
                break;
        }
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCity.class.getSimpleName().equals(from)) {
            String fromKey = bundle.getString(KEY_FROM);
            if ("startAddress".equals(fromKey)) {
                startBean = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
                setCityInfo();
            } else if ("endAddress".equals(fromKey)) {
                arrivalBean = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
                tContent.setText(arrivalBean.name);
            }
        }
    }

    private void setCityInfo() {
        if (startBean == null) return;
        wContent.setText(startBean.name);
        tContent.setText("");
        cityTips.setText(startBean.dailyTip);
        cityTips.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initHeader() {
    }

    /**
     * 承诺
     */
    private void showPromiseDialog() {
        int[] types = {0, 2, 3, 4};
        ArrayList<PromiseBean> list = new ArrayList<PromiseBean>();
        for (int type : types) {
            list.add(Constants.PromiseMap.get(type));
        }
        PromiseAdapter adapter = new PromiseAdapter(getActivity());
        adapter.setList(list);
        new AlertDialog.Builder(getActivity())
                .setAdapter(adapter, null)
                .setCancelable(true)
                .show()
                .setCanceledOnTouchOutside(true);
    }

    private boolean checkDataValid() {
        processDays();
        if (startBean == null) {
            Toast.makeText(getActivity(), "选择起始城市", Toast.LENGTH_LONG).show();
            return false;
        } else if (arrivalBean == null) {
            Toast.makeText(getActivity(), "选择达到城市", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(bean.startDate) || TextUtils.isEmpty(bean.endDate)) {
            Toast.makeText(getActivity(), "选择服务日期", Toast.LENGTH_LONG).show();
            return false;
        }
        if (mTotalDays <= 0) {
            Toast.makeText(getActivity(), "请重新选择服务日期", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void processDays() {
        try {
            if (mTotalDays < 0) return;
            String startDate = sDateTime.getText().toString();
            String endDate = eDateTime.getText().toString();
            bean.startDate = startDate;
            bean.endDate = endDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startFgCar() {
        if (!checkDataValid()) return;
        bean.startCityID = startBean.cityId;
        bean.startCityName = startBean.name;
        bean.startLocation = startBean.location;
        bean.terminalCityID = arrivalBean.cityId;
        bean.terminalCityName = arrivalBean.name;
        bean.terminalLocation = arrivalBean.location;
        bean.childSeatSwitch = startBean.childSeatSwitch;
        bean.areaCode = startBean.areaCode;
        bean.oneCityTravel = 2;
        bean.totalDay = mTotalDays;
        FgCar fg = new FgCar();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FgCar.KEY_DAILY, bean);
        fg.setArguments(bundle);
        startFragment(fg);
    }

    private void showDatePicker(final TextView mTextView, String startDate) {
        Calendar cal = Calendar.getInstance();
        try {
            if (!TextUtils.isEmpty(mTextView.getText().toString())) {
                cal.setTime(DateUtils.getDateByStr(mTextView.getText().toString()));
            } else if (!TextUtils.isEmpty(startDate)) {
                cal.setTime(DateUtils.getDateByStr(startDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String date = "You picked the following date: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        String monthStr = String.format("%02d", (monthOfYear + 1));
                        String dayOfMonthStr = String.format("%02d", dayOfMonth);
                        String serverDate = year + "-" + monthStr + "-" + dayOfMonthStr;
                        mTextView.setText(serverDate);
                        countDay();
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        cal = Calendar.getInstance();
        try {
            if (!TextUtils.isEmpty(startDate)) {

                cal.setTime(DateUtils.getDateByStr(startDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        dpd.setMaxDate(cal);
        dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");
    }

    /**
     * 计算总天数
     */
    private void countDay() {
        String startDateStr = sDateTime.getText().toString();
        String endDateStr = eDateTime.getText().toString();
        if (TextUtils.isEmpty(startDateStr)) return;
        try {
            if (TextUtils.isEmpty(endDateStr)) {
                //起始日期加一天
                Date startDate = DateUtils.dateDateFormat.parse(startDateStr);
                Date endDate = DateUtils.addDay(startDate, 1);
                endDateStr = DateUtils.dateDateFormat.format(endDate);
                eDateTime.setText(endDateStr);
            }
            mTotalDays = DateUtils.getDiffByDate(DateUtils.getDateByStr(startDateStr), DateUtils.getDateByStr(endDateStr)) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (mTotalDays <= 0) {
            Toast.makeText(getActivity(), "请重新选择服务日期", Toast.LENGTH_LONG).show();
            return;
        }
        String startDate = sDateTime.getText().toString();
        String endDate = eDateTime.getText().toString();
        bean.startDate = startDate;
        bean.endDate = endDate;
        if (bean.outTownDays == -1 || bean.outTownDays > mTotalDays) {
            bean.outTownDays = 1;
        }
        bean.inTownDays = mTotalDays - bean.outTownDays;
        inTownDaysTv.setText(bean.inTownDays + "天");
        outTownDaysTv.setText(bean.outTownDays + "天");
    }


    private void showSelectDay(final int type, final int min, int max) {
        if (max < min) return;
        String[] dayInfo;
        dayInfo = new String[max + 1 - min];
        for (int i = 0; i + min <= max; i++) {
            dayInfo[i] = (i + min) + "天";
        }
        MLog.e("dayInfo= " + Arrays.toString(dayInfo));
        new AlertDialog.Builder(getActivity())
                .setTitle("天数选择")
                .setItems(dayInfo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == 1) {
                            bean.inTownDays = which + min;
                            bean.outTownDays = mTotalDays - bean.inTownDays;
                        } else {
                            bean.outTownDays = which + min;
                            bean.inTownDays = mTotalDays - bean.outTownDays;
                        }
                        inTownDaysTv.setText(bean.inTownDays + "天");
                        outTownDaysTv.setText(bean.outTownDays + "天");
                    }
                })
                .show();
    }

}
