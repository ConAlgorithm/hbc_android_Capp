package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/11/14.
 */
public class CountryLocalTimeView extends FrameLayout{

    @Bind(R.id.local_country_iv)
    ImageView countryImageIV;
    @Bind(R.id.local_time_tv)
    TextView localTimeTV;

    private volatile long delayedMillis;
    private volatile boolean isStop = false;

    private SimpleDateFormat dateFormat;
    private String timediffStr;


    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public CountryLocalTimeView(Context context) {
        this(context, null);
    }

    public CountryLocalTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_local_time, this);
        ButterKnife.bind(view);

        dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    public void setData(String flag, int _timediff, int _timezone, String cityName, String countryName) {
        this.isStop = false;

        TimeZone tz = TimeZone.getDefault();
        String strTz = tz.getDisplayName(false, TimeZone.SHORT);
        int local = 0;
        if (strTz == null || !strTz.contains("GMT") || strTz.length() != 9) {
            this.setVisibility(View.GONE);
        } else {
            try {
                String minutes = strTz.substring(4, 6);
                if (minutes.charAt(0) == '0') {
                    local = CommonUtils.getCountInteger("" + minutes.charAt(1));
                } else {
                    local = CommonUtils.getCountInteger(minutes);
                }
                if (strTz.charAt(3) == '-') {
                    local = -local;
                }
                this.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                this.setVisibility(View.GONE);
                return;
            }
        }

        Tools.showImage(countryImageIV, flag, R.mipmap.country_flag_default);

        int timediff = Math.abs(local - _timezone);//司导与用户的时差
        timediffStr = timediff == 0 ? "与您无时差" : String.format("与您相差%1$s小时", timediff);

        String timeZoneString = "GMT";
        if (_timezone >= 0) {
            timeZoneString += "+";
        } else {
            timeZoneString += "-";
        }
        timeZoneString += Math.abs(_timezone);
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        dateFormat.setTimeZone(timeZone);
        Calendar mCalendar = Calendar.getInstance(timeZone);
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        delayedMillis = 60 * 1000 - mCalendar.get(Calendar.SECOND) * 1000 - mCalendar.get(Calendar.MILLISECOND);
        singleThreadExecutor.execute(timeRunnable);
    }

    private Runnable timeRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                if (delayedMillis > 0 && mHandler != null) {
                    mHandler.sendEmptyMessage(1);
                    Thread.sleep(delayedMillis);
                    delayedMillis = 0;
                }
                do {
                    mHandler.sendEmptyMessage(1);
                    Thread.sleep(60000);
                } while (!isStop && mHandler != null);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (isStop) {
                        return;
                    }
                    CharSequence sysTimeStr = dateFormat.format(System.currentTimeMillis());
                    localTimeTV.setText(String.format("司导时间：%1$s，%2$s", sysTimeStr, timediffStr));
                break;
                default:
                break;

            }
        }
    };
}
