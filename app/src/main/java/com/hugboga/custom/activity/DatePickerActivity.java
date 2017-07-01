package com.hugboga.custom.activity;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.datepicker.CustomDayViewAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.AnimationUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.GuideCalendarUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.squareup.timessquare.CalendarListBean;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.MonthCellDescriptor;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePickerView;

import static android.view.View.GONE;

public class DatePickerActivity extends Activity {

    public static final int PARAM_TYPE_SINGLE = 1;
    public static final int PARAM_TYPE_RANGE = 2;
    public static final int PARAM_TYPE_SINGLE_NOTEXT = 3;

    public static final String PARAM_TYPE = "type";
    public static final String PARAM_BEAN = "chooseDateBean";
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_ASSIGN_GUIDE = "isAssignGuide";

    @Bind(R.id.date_picker_title_tv)
    TextView headerTitle;
    @Bind(R.id.week_layout)
    LinearLayout weekLayout;
    @Bind(R.id.date_picker_root_layout)
    LinearLayout rootLayout;
    @Bind(R.id.show_tips)
    TextView showTips;

    @Bind(R.id.date_picker_guide_title_layout)
    RelativeLayout guideTitleLayout;
    @Bind(R.id.date_picker_guide_arrow_iv)
    ImageView guideArrowIV;
    @Bind(R.id.date_picker_guide_title_tv)
    TextView guideTitleTV;
    @Bind(R.id.date_picker_guide_confirm_tv)
    TextView guideConfirmTV;
    @Bind(R.id.timepicker_view)
    TimePickerView timepickerView;
    @Bind(R.id.date_picker_guide_hint_tv)
    TextView guideHintTV;
    @Bind(R.id.date_picker_guide_hint_iv)
    ImageView guideHintIV;

    @Bind(R.id.calendar_view)
    CalendarPickerView calendar;

    int calender_type = 1;//1,日期单选,2 日期多选 3,单选没底部文字
    CalendarPickerView.SelectionMode model = CalendarPickerView.SelectionMode.SINGLE;

    int clickTimes = 0;

    Date selectedDate;
    CustomDayViewAdapter customDayViewAdapter;

    Calendar nextYear, lastYear;

    ChooseDateBean mChooseDateBean;

    String startDate = "";

    private int orderType;
    private boolean isAssignGuide = false;
    private HashMap<String, CalendarListBean> guideCalendarMap;
    private DialogUtil mDialogUtil;
    private ChooseDateBean setlectedChooseDateBean;
    private boolean firstShow = true;

    public void setTitleStep(int step, ChooseDateBean chooseDateBean) {
        if (step == 1) {
            guideArrowIV.setVisibility(View.GONE);
            guideConfirmTV.setVisibility(View.GONE);
            if (orderType == 2) {
                guideTitleTV.setText("起飞日期（当地时间）");
            } else if (orderType == 4) {
                guideTitleTV.setText("出发日期（当地时间）");
            }
            guideArrowIV.setEnabled(false);
            guideTitleTV.setEnabled(false);
            guideTitleTV.setPadding(UIUtils.dip2px(15), 0, 0, 0);
            calendar.setVisibility(View.VISIBLE);
            weekLayout.setVisibility(View.VISIBLE);
            timepickerView.setVisibility(View.GONE);
            if (isAssignGuide) {
                guideHintTV.setVisibility(View.VISIBLE);
                guideHintIV.setVisibility(View.VISIBLE);
            }
        } else {
            guideArrowIV.setVisibility(View.VISIBLE);
            guideConfirmTV.setVisibility(View.VISIBLE);
            guideConfirmTV.setVisibility(View.VISIBLE);
            guideArrowIV.setEnabled(true);
            guideTitleTV.setEnabled(true);
            guideTitleTV.setPadding(UIUtils.dip2px(30), 0, 0, 0);
            calendar.setVisibility(View.GONE);
            weekLayout.setVisibility(View.GONE);
            timepickerView.setVisibility(View.VISIBLE);
            guideTitleTV.setText(DateUtils.getPointStrFromDate2(chooseDateBean.halfDateStr));
            guideHintTV.setVisibility(View.GONE);
            guideHintIV.setVisibility(View.GONE);

            if (firstShow) {
                Calendar _calendar = Calendar.getInstance();
                if (mChooseDateBean != null && !TextUtils.isEmpty(mChooseDateBean.serverTime)) {
                    try {
                        _calendar.setTime(DateUtils.timeFormat2.parse(mChooseDateBean.serverTime));
                    } catch (ParseException e) {
                        _calendar.set(Calendar.HOUR_OF_DAY, 9);
                        _calendar.set(Calendar.MINUTE, 0);
                    }
                }
                timepickerView.setSelectedItem(_calendar.get(Calendar.HOUR_OF_DAY), _calendar.get(Calendar.MINUTE));
                firstShow = false;
            }
        }
    }

    @OnClick({R.id.date_picker_guide_arrow_iv, R.id.date_picker_guide_title_tv})
    public void backDate() {
        setTitleStep(1, null);
    }

    @OnClick({R.id.date_picker_guide_confirm_tv})
    public void onConfirm() {
        setlectedChooseDateBean.serverTime = timepickerView.getSelectedHour() + ":" + timepickerView.getSelectedMinute();
        EventBus.getDefault().post(new EventAction(EventType.CHOOSE_DATE, setlectedChooseDateBean));
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_layout);
        ButterKnife.bind(this);

        orderType = this.getIntent().getIntExtra(Constants.PARAMS_ORDER_TYPE, 0);
        isAssignGuide = this.getIntent().getBooleanExtra(DatePickerActivity.PARAM_ASSIGN_GUIDE, false);

        int viewHeight = (int) (UIUtils.getScreenHeight() * 0.7f);
        if (orderType != 3 && (orderType == 4 || orderType == 2)) {
            viewHeight = (int) (UIUtils.getScreenHeight() * 0.6f);
            headerTitle.setVisibility(View.GONE);
            showTips.setVisibility(View.GONE);
            guideTitleLayout.setVisibility(View.VISIBLE);
            setTitleStep(1, null);
        }
        if (isAssignGuide) {
            guideCalendarMap = GuideCalendarUtils.getInstance().getCalendarMap();
            if (guideCalendarMap == null) {
                mDialogUtil = DialogUtil.getInstance(this);
                mDialogUtil.showLoadingDialog(false);
                GuideCalendarUtils.getInstance().setOnAllRequestSucceedListener(new GuideCalendarUtils.OnAllRequestSucceedListener() {
                    @Override
                    public void onAllRequestSucceed(HashMap<String, CalendarListBean> guideCalendarMap) {
                        if (DatePickerActivity.this.isFinishing() || !isAssignGuide) {
                            return;
                        }
                        DatePickerActivity.this.guideCalendarMap = guideCalendarMap;
                        if (mDialogUtil != null) {
                            mDialogUtil.dismissDialog();
                        }
                        initCalendar();
                    }
                });
                mHandler.sendEmptyMessageDelayed(1, 30 * 1000);
            }
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, viewHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rootLayout.setLayoutParams(params);

        calender_type = this.getIntent().getIntExtra("type", 1);
        startDate = this.getIntent().getStringExtra("startDate");
        initViews();
        initWeek();
        mChooseDateBean = (ChooseDateBean)this.getIntent().getSerializableExtra("chooseDateBean");
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.MONTH, 6);

        lastYear = Calendar.getInstance();

        customDayViewAdapter = new CustomDayViewAdapter();
        calendar.setCustomDayView(customDayViewAdapter);

        initCalendar();

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(MonthCellDescriptor cell, Date date) {
                ChooseDateBean chooseDateBean = new ChooseDateBean();
                if (calender_type == 1) {
                    showTips.setVisibility(GONE);
                    chooseDateBean.type = calender_type;
                    chooseDateBean.showStartDateStr = DateUtils.dateSimpleDateFormatMMdd.format(date);
                    chooseDateBean.showEndDateStr = DateUtils.dateSimpleDateFormatMMdd.format(date);
                    chooseDateBean.start_date = DateUtils.dateDateFormat.format(date);
                    chooseDateBean.end_date = DateUtils.dateDateFormat.format(date);
                    chooseDateBean.dayNums = 1;
                    chooseDateBean.isToday = DateUtils.isToday(date);
                    chooseDateBean.startDate = date;
                    chooseDateBean.endDate = date;
                    finishDelay(chooseDateBean);
                }else if (calender_type == 3) {
                    showTips.setVisibility(GONE);
                    chooseDateBean.halfDateStr = DateUtils.dateDateFormat.format(date);
                    chooseDateBean.halfDate = date;
                    chooseDateBean.showHalfDateStr = DateUtils.dateSimpleDateFormatMMdd.format(date);
                    chooseDateBean.type = calender_type;
                    chooseDateBean.isToday = DateUtils.isToday(date);
                    if (null != mChooseDateBean) {
                        chooseDateBean.maxDateStr = mChooseDateBean.maxDate == null ? "" : DateUtils.dateDateFormat.format(mChooseDateBean.maxDate);
                        chooseDateBean.startDate = mChooseDateBean.startDate;
                        chooseDateBean.endDate = mChooseDateBean.endDate;
                        chooseDateBean.start_date = mChooseDateBean.start_date;
                        chooseDateBean.end_date = mChooseDateBean.end_date;
                        chooseDateBean.showStartDateStr = mChooseDateBean.showStartDateStr;
                        chooseDateBean.showEndDateStr = mChooseDateBean.showEndDateStr;
                        chooseDateBean.dayNums = mChooseDateBean.dayNums;
                    }
                    if (orderType == 4 || orderType == 2) {
                        setlectedChooseDateBean = chooseDateBean;
                        setTitleStep(2, chooseDateBean);
                    } else {
                        finishDelay(chooseDateBean);
                    }
                } else {
                    if (clickTimes == 1) {
                        if (calendar.getSelectedDate().before(selectedDate)) {
                            selectedDate = calendar.getSelectedDate();
                            clickTimes = 1;
                            showTips.setText(R.string.show_tips_end);
                            AnimationUtils.showAnimation(showTips,200,null);
                        } else {
                            showTips.setVisibility(GONE);
                            List<Date> dates = calendar.getSelectedDates();
                            chooseDateBean.type = calender_type;
                            chooseDateBean.showStartDateStr = DateUtils.dateSimpleDateFormatMMdd.format(dates.get(0));
                            chooseDateBean.showEndDateStr = DateUtils.dateSimpleDateFormatMMdd.format(dates.get(dates.size() - 1));
                            chooseDateBean.start_date = DateUtils.dateDateFormat.format(dates.get(0));
                            chooseDateBean.end_date = DateUtils.dateDateFormat.format(dates.get(dates.size() - 1));
                            chooseDateBean.dayNums = (int) DateUtils.getDays(dates.get(0), dates.get(dates.size() - 1));
                            chooseDateBean.isToday = DateUtils.isToday(dates.get(0));
                            chooseDateBean.startDate = dates.get(0);
                            chooseDateBean.endDate = dates.get(dates.size() - 1);
                            if (null != mChooseDateBean) {
                                chooseDateBean.showHalfDateStr = mChooseDateBean.showHalfDateStr;
                                chooseDateBean.halfDate = mChooseDateBean.halfDate;
                                chooseDateBean.halfDateStr = mChooseDateBean.halfDateStr;
                            }
                            finishDelay(chooseDateBean);
                        }
                    } else {
                        clickTimes += 1;
                        selectedDate = calendar.getSelectedDate();
                        calendar.setSelectedDate(selectedDate);
                        showTips.setText(R.string.show_tips_end);
                        AnimationUtils.showAnimation(showTips,200,null);
                    }
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }

    public void initCalendar() {
        calendar.setGuideCalendarMap(guideCalendarMap);
        if (mChooseDateBean != null) {
            if (calender_type == 1 && null != mChooseDateBean.startDate) {
                model = CalendarPickerView.SelectionMode.SINGLE;
                calendar.init(lastYear.getTime(), nextYear.getTime()).inMode(model).withSelectedDate(mChooseDateBean.startDate);
                showTips.setText(R.string.show_tips_half);
            } else if (calender_type == 2 && null != mChooseDateBean.startDate) {
                showTips.setText(R.string.show_tips_start);
                model = CalendarPickerView.SelectionMode.RANGE;
                List<Date> dates = new ArrayList<>();
                dates.add(mChooseDateBean.startDate);
                dates.add(mChooseDateBean.endDate);
                calendar.init(lastYear.getTime(), nextYear.getTime()).inMode(model).withSelectedDates(dates);
            }else if(calender_type == 3 && null != mChooseDateBean.halfDate){
                showTips.setText(R.string.show_tips_half);
                model = CalendarPickerView.SelectionMode.SINGLE_NO_TEXT;
                Date minDate = mChooseDateBean.minDate != null ? mChooseDateBean.minDate : lastYear.getTime();
                mChooseDateBean.maxDate = nextYear.getTime() != null ? new Date(nextYear.getTime().getTime() - 24 * 3600000) : null;
                calendar.init(minDate, nextYear.getTime()).inMode(model).withSelectedDate(mChooseDateBean.halfDate);
            } else {
                if (calender_type == 1) {
                    model = CalendarPickerView.SelectionMode.SINGLE;
                    showTips.setText(R.string.show_tips_half);
                } else {
                    model = CalendarPickerView.SelectionMode.RANGE;
                    showTips.setText(R.string.show_tips_half);
                }
                calendar.init(lastYear.getTime(), nextYear.getTime()).inMode(model);
            }
        } else {
            if (calender_type == 1) {
                model = CalendarPickerView.SelectionMode.SINGLE;
                calendar.init(lastYear.getTime(), nextYear.getTime()).inMode(model);
                showTips.setText(R.string.show_tips_half);
            } else if(calender_type == 2){
                model = CalendarPickerView.SelectionMode.RANGE;
                calendar.init(lastYear.getTime(), nextYear.getTime()).inMode(model);
                showTips.setText(R.string.show_tips_start);
            } else if(calender_type == 3){
                Date minDate = lastYear.getTime();
                model = CalendarPickerView.SelectionMode.SINGLE_NO_TEXT;
                calendar.init(minDate, nextYear.getTime()).inMode(model);
                showTips.setText(R.string.show_tips_half);
            }
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mDialogUtil != null) {
                mDialogUtil.dismissDialog();
            }
            isAssignGuide = false;
        }
    };

    public void finishDelay(final ChooseDateBean chooseDateBean) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                EventBus.getDefault().post(new EventAction(EventType.CHOOSE_DATE, chooseDateBean));
            }
        }, 100);
    }

    private void initWeek() {
        String[] weekStr = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        for (int offset = 0; offset < 7; offset++) {
            final TextView textView = (TextView) weekLayout.getChildAt(offset);
            textView.setText(weekStr[offset]);
        }
    }

    String title = null;
    private void initViews() {
        title = getIntent().getStringExtra(PARAM_TITLE);
        if(null == title) {
            headerTitle.setText(getString(R.string.select_day));
        }else{
            headerTitle.setText(title);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @OnClick(R.id.date_picker_out_side_view)
    public void onOutSideClickListener(View view) {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialogUtil != null) {
            mDialogUtil.dismissDialog();
        }
    }

}
