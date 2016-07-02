package com.hugboga.custom.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.datepicker.CustomDayViewAdapter;
import com.hugboga.custom.activity.datepicker.Decorator;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.DateUtils;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static android.widget.Toast.LENGTH_SHORT;
public class DatePickerActivity extends BaseActivity {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    private CalendarPickerView calendar;
    private AlertDialog theDialog;
    private CalendarPickerView dialogView;
    private final Set<Button> modeButtons = new LinkedHashSet<Button>();

    int calender_type = 1;//1,日期单选,2 日期多选
    CalendarPickerView.SelectionMode model = CalendarPickerView.SelectionMode.SINGLE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_layout);
        ButterKnife.bind(this);
        calender_type = this.getIntent().getIntExtra("type",1);
        initViews();
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        if(calender_type == 1) {
            model = CalendarPickerView.SelectionMode.SINGLE;
        }else{
            model = CalendarPickerView.SelectionMode.RANGE;
        }

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        calendar.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(model) //
                .withSelectedDate(new Date());
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                ChooseDateBean chooseDateBean = new ChooseDateBean();
                if(calender_type == 1) {
                    chooseDateBean.halfDate = DateUtils.dateDateFormat.format(date);
                    chooseDateBean.showHalfDateStr = DateUtils.dateSimpleDateFormatMMdd.format(date);
                    chooseDateBean.type = calender_type;
                    chooseDateBean.isToday = DateUtils.isToday(date);
                    EventBus.getDefault().post(new EventAction(EventType.CHOOSE_DATE, chooseDateBean));
                }else{
                    List<Date> dates = calendar.getSelectedDates();
                    chooseDateBean.type = calender_type;
                    chooseDateBean.showStartDateStr = DateUtils.dateSimpleDateFormatMMdd.format(dates.get(0));
                    chooseDateBean.showEndDateStr = DateUtils.dateSimpleDateFormatMMdd.format(dates.get(dates.size()-1));
                    chooseDateBean.dayNums = (int)DateUtils.getDays(dates.get(0),dates.get(dates.size()-1));
                    chooseDateBean.isToday = DateUtils.isToday(dates.get(0));
                    EventBus.getDefault().post(new EventAction(EventType.CHOOSE_DATE, chooseDateBean));
                }
                finish();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        initButtonListeners(nextYear, lastYear);
    }


    private void initViews(){
        headerTitle.setText(getString(R.string.select_day));
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initButtonListeners(final Calendar nextYear, final Calendar lastYear) {
        final Button single = (Button) findViewById(R.id.button_single);
        final Button multi = (Button) findViewById(R.id.button_multi);
        final Button range = (Button) findViewById(R.id.button_range);
        final Button displayOnly = (Button) findViewById(R.id.button_display_only);
        final Button dialog = (Button) findViewById(R.id.button_dialog);
        final Button customized = (Button) findViewById(R.id.button_customized);
        final Button decorator = (Button) findViewById(R.id.button_decorator);
        final Button hebrew = (Button) findViewById(R.id.button_hebrew);
        final Button arabic = (Button) findViewById(R.id.button_arabic);
        final Button customView = (Button) findViewById(R.id.button_custom_view);

        modeButtons.addAll(Arrays.asList(single, multi, range, displayOnly, decorator, customView));

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsEnabled(single);

                calendar.setCustomDayView(new DefaultDayViewAdapter());
                calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
                calendar.init(lastYear.getTime(), nextYear.getTime()) //
                        .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                        .withSelectedDate(new Date());
            }
        });

        multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsEnabled(multi);

                calendar.setCustomDayView(new DefaultDayViewAdapter());
                Calendar today = Calendar.getInstance();
                ArrayList<Date> dates = new ArrayList<Date>();
                for (int i = 0; i < 5; i++) {
                    today.add(Calendar.DAY_OF_MONTH, 3);
                    dates.add(today.getTime());
                }
                calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
                calendar.init(new Date(), nextYear.getTime()) //
                        .inMode(CalendarPickerView.SelectionMode.MULTIPLE) //
                        .withSelectedDates(dates);
            }
        });

        range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsEnabled(range);

                calendar.setCustomDayView(new DefaultDayViewAdapter());
                Calendar today = Calendar.getInstance();
                ArrayList<Date> dates = new ArrayList<Date>();
                today.add(Calendar.DATE, 3);
                dates.add(today.getTime());
                today.add(Calendar.DATE, 5);
                dates.add(today.getTime());
                calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
                calendar.init(new Date(), nextYear.getTime()) //
                        .inMode(CalendarPickerView.SelectionMode.RANGE) //
                        .withSelectedDates(dates);
            }
        });

        displayOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsEnabled(displayOnly);

                calendar.setCustomDayView(new DefaultDayViewAdapter());
                calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
                calendar.init(new Date(), nextYear.getTime()) //
                        .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                        .withSelectedDate(new Date()) //
                        .displayOnly();
            }
        });

        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = "I'm a dialog!";
                showCalendarInDialog(title, R.layout.dialog);
                dialogView.init(lastYear.getTime(), nextYear.getTime()) //
                        .withSelectedDate(new Date());
            }
        });

        customized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarInDialog("Pimp my calendar!", R.layout.dialog_customized);
                dialogView.init(lastYear.getTime(), nextYear.getTime())
                        .withSelectedDate(new Date());
            }
        });

        decorator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsEnabled(decorator);

                calendar.setCustomDayView(new DefaultDayViewAdapter());
                calendar.setDecorators(Arrays.<CalendarCellDecorator>asList(new Decorator()));
                calendar.init(lastYear.getTime(), nextYear.getTime()) //
                        .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                        .withSelectedDate(new Date());
            }
        });

        hebrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarInDialog("I'm Hebrew!", R.layout.dialog);
                dialogView.init(lastYear.getTime(), nextYear.getTime(), new Locale("iw", "IL")) //
                        .withSelectedDate(new Date());
            }
        });

        arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarInDialog("I'm Arabic!", R.layout.dialog);
                dialogView.init(lastYear.getTime(), nextYear.getTime(), new Locale("ar", "EG")) //
                        .withSelectedDate(new Date());
            }
        });

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsEnabled(customView);

                calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
                calendar.setCustomDayView(new CustomDayViewAdapter());
                calendar.init(lastYear.getTime(), nextYear.getTime())
                        .inMode(CalendarPickerView.SelectionMode.SINGLE)
                        .withSelectedDate(new Date());
            }
        });

        findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toast = "Selected: " + calendar.getSelectedDate().getTime();
                Toast.makeText(DatePickerActivity.this, toast, LENGTH_SHORT).show();
            }
        });
    }

    private void showCalendarInDialog(String title, int layoutResId) {
        dialogView = (CalendarPickerView) getLayoutInflater().inflate(layoutResId, null, false);
        theDialog = new AlertDialog.Builder(this) //
                .setTitle(title)
                .setView(dialogView)
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        theDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialogView.fixDialogDimens();
            }
        });
        theDialog.show();
    }

    private void setButtonsEnabled(Button currentButton) {
        for (Button modeButton : modeButtons) {
            modeButton.setEnabled(modeButton != currentButton);
        }
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
