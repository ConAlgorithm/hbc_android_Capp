package cn.qqtheme.framework.picker;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;

import cn.qqtheme.framework.util.DateUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by qingcha on 17/6/27.
 */
public class TimePickerView extends LinearLayout {

    protected int textSize = WheelView.TEXT_SIZE;
    protected int textColorNormal = 0xFFCCCCCC;
    protected int textColorFocus = 0xFF7F7F7F;
    protected int offset = 1;
    protected boolean cycleDisable = false;
    protected WheelView.LineConfig lineConfig;

    public static final int HOUR_24 = 0;//24小时制
    public static final int HOUR_12 = 1;//12小时制
    private int mode;
    private String hourLabel = "时", minuteLabel = "分";
    private String selectedHour = "", selectedMinute = "";
    private int startHour, startMinute = 0;
    private int endHour, endMinute = 50;

    private WheelView hourView;
    private WheelView minuteView;

    /**
     * 安卓开发应避免使用枚举类（enum），因为相比于静态常量enum会花费两倍以上的内存。
     * http://developer.android.com/training/articles/memory.html#Overhead
     */
    @IntDef(value = {HOUR_24, HOUR_12})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public TimePickerView(Context context) {
        this(context, null);
    }

    public TimePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView() {
        this.mode = HOUR_24;
        if (mode == HOUR_12) {
            startHour = 1;
            endHour = 12;
            selectedHour = DateUtils.fillZero(Calendar.getInstance().get(Calendar.HOUR));
        } else {
            startHour = 0;
            endHour = 23;
            selectedHour = DateUtils.fillZero(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        }
        selectedMinute = DateUtils.fillZero(getChanceMinute(Calendar.getInstance().get(Calendar.MINUTE)));

        lineConfig = new WheelView.LineConfig();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);

        hourView = new WheelView(getContext());
        LinearLayout.LayoutParams hourParams= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        hourParams.rightMargin = dip2px(10);
        hourView.setLayoutParams(hourParams);
        hourView.setTextSize(textSize);
        hourView.setOffset(offset);
        hourView.setTextColor(textColorNormal, textColorFocus);
        hourView.setLineConfig(lineConfig);
        hourView.setCycleDisable(cycleDisable);
        addView(hourView);

        TextView hourTextView = new TextView(getContext());
        LinearLayout.LayoutParams hourTextParams= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        hourTextParams.rightMargin = dip2px(10);
        hourTextView.setLayoutParams(hourTextParams);
        hourTextView.setTextSize(textSize);
        hourTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(hourLabel)) {
            hourTextView.setText(hourLabel);
        }
        addView(hourTextView);

        minuteView = new WheelView(getContext());
        LinearLayout.LayoutParams minuteParams= new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        minuteParams.rightMargin = dip2px(10);
        minuteView.setLayoutParams(minuteParams);
        minuteView.setTextSize(textSize);
        minuteView.setTextColor(textColorNormal, textColorFocus);
        minuteView.setLineConfig(lineConfig);
        minuteView.setOffset(offset);
        minuteView.setCycleDisable(cycleDisable);
        addView(minuteView);

        TextView minuteTextView = new TextView(getContext());
        minuteTextView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        minuteTextView.setTextSize(textSize);
        minuteTextView.setTextColor(textColorFocus);
        if (!TextUtils.isEmpty(minuteLabel)) {
            minuteTextView.setText(minuteLabel);
        }
        addView(minuteTextView);

        ArrayList<String> hours = new ArrayList<String>();
        for (int i = startHour; i <= endHour; i++) {
            hours.add(DateUtils.fillZero(i));
        }
        if (hours.indexOf(selectedHour) == -1) {
            //当前设置的小时不在指定范围，则默认选中范围开始的小时
            selectedHour = hours.get(0);
        }
        hourView.setItems(hours, selectedHour);
        minuteView.setItems(changeMinuteData(selectedHour), selectedMinute);

        hourView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedHour = item;
                minuteView.setItems(changeMinuteData(item), selectedMinute);
            }
        });
        minuteView.setOnWheelListener(new WheelView.OnWheelListener() {
            @Override
            public void onSelected(boolean isUserScroll, int index, String item) {
                selectedMinute = item;
            }
        });
    }

    public int dip2px(float dpValue) {
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                getContext().getResources().getDisplayMetrics()) + 0.5f);
    }

    /**
     * 设置时间显示的单位
     */
    public void setLabel(String hourLabel, String minuteLabel) {
        this.hourLabel = hourLabel;
        this.minuteLabel = minuteLabel;
    }

    /**
     * 设置范围：开始的时分
     */
    public void setRangeStart(int startHour, int startMinute) {
        boolean illegal = false;
        if (startHour < 0 || startMinute < 0 || startMinute > 59) {
            illegal = true;
        }
        if (mode == HOUR_12 && (startHour == 0 || startHour > 12)) {
            illegal = true;
        }
        if (mode == HOUR_24 && startHour >= 24) {
            illegal = true;
        }
        if (illegal) {
            throw new IllegalArgumentException("out of range");
        }
        this.startHour = startHour;
        this.startMinute = getChanceMinute(startMinute);
    }

    /**
     * 设置范围：结束的时分
     */
    public void setRangeEnd(int endHour, int endMinute) {
        boolean illegal = false;
        if (endHour < 0 || endMinute < 0 || endMinute > 59) {
            illegal = true;
        }
        if (mode == HOUR_12 && (endHour == 0 || endHour > 12)) {
            illegal = true;
        }
        if (mode == HOUR_24 && endHour >= 24) {
            illegal = true;
        }
        if (illegal) {
            throw new IllegalArgumentException("out of range");
        }
        this.endHour = endHour;
        this.endMinute = getChanceMinute(endMinute);
    }

    /**
     * 设置默认选中的时间
     */
    public void setSelectedItem(int hour, int minute) {
        selectedHour = DateUtils.fillZero(hour);
        selectedMinute = DateUtils.fillZero(getChanceMinute(minute));
        hourView.setSelectedItem(selectedHour);
        minuteView.setSelectedItem(selectedMinute);
    }

    private ArrayList<String> changeMinuteData(String hour) {
        ArrayList<String> minutes = new ArrayList<String>();
        int hourInt = DateUtils.trimZero(hour);
        if (startHour == endHour) {
            if (startMinute > endMinute) {
                int temp = startMinute;
                startMinute = endMinute;
                endMinute = temp;
            }
            for (int i = startMinute/10; i <= endMinute/10; i++) {
                minutes.add(DateUtils.fillZero(i * 10));
            }
        } else if (hourInt == startHour) {
            for (int i = startMinute/10; i < 6; i++) {
                minutes.add(DateUtils.fillZero(i*10));
            }
        } else if (hourInt == endHour) {
            for (int i = 0; i <= endMinute/10; i++) {
                minutes.add(DateUtils.fillZero(i*10));
            }
        } else {
            for (int i = 0; i < 6; i++) {
                minutes.add(DateUtils.fillZero(i*10));
            }
        }
        if (minutes.indexOf(selectedMinute) == -1) {
            //当前设置的分钟不在指定范围，则默认选中范围开始的分钟
            selectedMinute = minutes.get(0);
        }
        return minutes;
    }

    public int getChanceMinute(int minute) {
        int _minute = (minute / 10) * 10;
        if (_minute < 50 && minute % 10 > 0) {
            _minute += 10;
        }
        return _minute;
    }

    public String getSelectedHour() {
        return selectedHour;
    }

    public String getSelectedMinute() {
        return selectedMinute;
    }

}
