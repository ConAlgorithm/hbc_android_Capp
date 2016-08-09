package com.hugboga.custom.activity;


import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.datepicker.CustomDayViewAdapter;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.AnimationUtils;
import com.hugboga.custom.utils.DateUtils;
import com.squareup.timessquare.CalendarPickerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class DatePickerActivity extends BaseActivity {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.week_layout)
    LinearLayout weekLayout;
    @Bind(R.id.show_tips)
    TextView showTips;
    private CalendarPickerView calendar;
    int calender_type = 1;//1,日期单选,2 日期多选 3,单选没底部文字
    CalendarPickerView.SelectionMode model = CalendarPickerView.SelectionMode.SINGLE;

    int clickTimes = 0;

    Date selectedDate;
    CustomDayViewAdapter customDayViewAdapter;

    Calendar nextYear, lastYear;

    ChooseDateBean mChooseDateBean;

    String startDate = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_layout);
        ButterKnife.bind(this);
        calender_type = this.getIntent().getIntExtra("type", 1);
        startDate = this.getIntent().getStringExtra("startDate");
        initViews();
        initWeek();
        mChooseDateBean = (ChooseDateBean)this.getIntent().getSerializableExtra("chooseDateBean");
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        lastYear = Calendar.getInstance();

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        customDayViewAdapter = new CustomDayViewAdapter();
        calendar.setCustomDayView(customDayViewAdapter);

        if (mChooseDateBean != null) {
            if (calender_type == 1 && null != mChooseDateBean.halfDate) {
                model = CalendarPickerView.SelectionMode.SINGLE;
                calendar.init(lastYear.getTime(), nextYear.getTime()).inMode(model).withSelectedDate(mChooseDateBean.halfDate);
                showTips.setText(R.string.show_tips_half);
            } else if (calender_type == 2 && null != mChooseDateBean.startDate) {
                showTips.setText(R.string.show_tips_start);
                model = CalendarPickerView.SelectionMode.RANGE;
                List<Date> dates = new ArrayList<>();
                dates.add(mChooseDateBean.startDate);
                dates.add(mChooseDateBean.endDate);
                calendar.init(lastYear.getTime(), nextYear.getTime()).inMode(model).withSelectedDates(dates);
            }else if(calender_type == 3 && null != mChooseDateBean.halfDate){
                model = CalendarPickerView.SelectionMode.SINGLE_NO_TEXT;
                calendar.init(lastYear.getTime(), nextYear.getTime()).inMode(model).withSelectedDate(mChooseDateBean.halfDate);
                showTips.setText(R.string.show_tips_half);
            } else {
                if (calender_type == 1) {
                    model = CalendarPickerView.SelectionMode.SINGLE;
                    showTips.setText(R.string.show_tips_half);
                } else {
                    model = CalendarPickerView.SelectionMode.RANGE;
                    showTips.setText(R.string.show_tips_start);
                }
                calendar.init(lastYear.getTime(), nextYear.getTime()).inMode(model);
            }
        } else {
            if (calender_type == 1) {
                model = CalendarPickerView.SelectionMode.SINGLE;
                showTips.setText(R.string.show_tips_half);
            } else if(calender_type == 2){
                model = CalendarPickerView.SelectionMode.RANGE;
                showTips.setText(R.string.show_tips_start);
            } else if(calender_type == 3){
                model = CalendarPickerView.SelectionMode.SINGLE_NO_TEXT;
                showTips.setText(R.string.show_tips_half);
            }
            calendar.init(lastYear.getTime(), nextYear.getTime()).inMode(model);
        }

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                ChooseDateBean chooseDateBean = new ChooseDateBean();
                if (calender_type == 1 || calender_type == 3) {
                    showTips.setVisibility(GONE);
                    chooseDateBean.halfDateStr = DateUtils.dateDateFormat.format(date);
                    chooseDateBean.halfDate = date;
                    chooseDateBean.showHalfDateStr = DateUtils.dateSimpleDateFormatMMdd.format(date);
                    chooseDateBean.type = calender_type;
                    chooseDateBean.isToday = DateUtils.isToday(date);
                    if (null != mChooseDateBean) {
                        chooseDateBean.startDate = mChooseDateBean.startDate;
                        chooseDateBean.endDate = mChooseDateBean.endDate;
                        chooseDateBean.start_date = mChooseDateBean.start_date;
                        chooseDateBean.end_date = mChooseDateBean.end_date;
                        chooseDateBean.showStartDateStr = mChooseDateBean.showStartDateStr;
                        chooseDateBean.showEndDateStr = mChooseDateBean.showEndDateStr;
                        chooseDateBean.dayNums = mChooseDateBean.dayNums;
                    }
                    finishDelay(chooseDateBean);
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
        title = getIntent().getStringExtra("title");
        if(null == title) {
            headerTitle.setText(getString(R.string.select_day));
        }else{
            headerTitle.setText(title);
        }

        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
