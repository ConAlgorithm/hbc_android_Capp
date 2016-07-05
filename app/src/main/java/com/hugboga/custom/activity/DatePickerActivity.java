package com.hugboga.custom.activity;


import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.datepicker.CustomDayViewAdapter;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.DateUtils;
import com.squareup.timessquare.CalendarPickerView;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DatePickerActivity extends BaseActivity {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.week_layout)
    LinearLayout weekLayout;
    private CalendarPickerView calendar;
    private AlertDialog theDialog;
    private CalendarPickerView dialogView;
    private final Set<Button> modeButtons = new LinkedHashSet<Button>();

    int calender_type = 1;//1,日期单选,2 日期多选
    CalendarPickerView.SelectionMode model = CalendarPickerView.SelectionMode.SINGLE;

    int clickTimes = 0;

    Date selectedDate;
    CustomDayViewAdapter customDayViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_layout);
        ButterKnife.bind(this);
        calender_type = this.getIntent().getIntExtra("type", 1);
        initViews();
        initWeek();
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final Calendar lastYear = Calendar.getInstance();

        if (calender_type == 1) {
            model = CalendarPickerView.SelectionMode.SINGLE;
        } else {
            model = CalendarPickerView.SelectionMode.RANGE;
        }

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        customDayViewAdapter = new CustomDayViewAdapter();
        calendar.setCustomDayView(customDayViewAdapter);
        calendar.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(model); //
//                .withSelectedDate(new Date());
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                ChooseDateBean chooseDateBean = new ChooseDateBean();
                if (calender_type == 1) {
                    finishDelay();
                    chooseDateBean.halfDate = DateUtils.dateDateFormat.format(date);
                    chooseDateBean.showHalfDateStr = DateUtils.dateSimpleDateFormatMMdd.format(date);
                    chooseDateBean.type = calender_type;
                    chooseDateBean.isToday = DateUtils.isToday(date);
                    EventBus.getDefault().post(new EventAction(EventType.CHOOSE_DATE, chooseDateBean));
                } else {
                    if (clickTimes == 1) {
                        if (calendar.getSelectedDate().before(selectedDate)) {
                            selectedDate = calendar.getSelectedDate();
                            clickTimes = 1;
                        } else {
                            finishDelay();
                            List<Date> dates = calendar.getSelectedDates();
                            chooseDateBean.type = calender_type;
                            chooseDateBean.showStartDateStr = DateUtils.dateSimpleDateFormatMMdd.format(dates.get(0));
                            chooseDateBean.showEndDateStr = DateUtils.dateSimpleDateFormatMMdd.format(dates.get(dates.size() - 1));
                            chooseDateBean.start_date = DateUtils.dateDateFormat.format(dates.get(0));
                            chooseDateBean.end_date = DateUtils.dateDateFormat.format(dates.get(dates.size() - 1));
                            chooseDateBean.dayNums = (int) DateUtils.getDays(dates.get(0), dates.get(dates.size() - 1));
                            chooseDateBean.isToday = DateUtils.isToday(dates.get(0));
                            EventBus.getDefault().post(new EventAction(EventType.CHOOSE_DATE, chooseDateBean));
                        }
                    } else {
                        clickTimes += 1;
                        selectedDate = calendar.getSelectedDate();
                    }
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

    }

    private void finishDelay(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },500);
    };

    private void initWeek(){
        String[] weekStr = new String[]{"日","一","二","三","四","五","六"};
        for (int offset = 0; offset < 7; offset++) {
            final TextView textView = (TextView) weekLayout.getChildAt(offset);
            textView.setText(weekStr[offset]);
        }
    }


    private void initViews() {
        headerTitle.setText(getString(R.string.select_day));
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        boolean applyFixes = theDialog != null && theDialog.isShowing();
        if (applyFixes) {
            dialogView.unfixDialogDimens();
        }
        super.onConfigurationChanged(newConfig);
        if (applyFixes) {
            dialogView.post(new Runnable() {
                @Override
                public void run() {
                    dialogView.fixDialogDimens();
                }
            });
        }
    }

    @OnClick(R.id.header_left_btn)
    public void onClick() {
    }
}
