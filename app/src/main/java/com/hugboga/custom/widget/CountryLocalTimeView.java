package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2016/11/14.
 */
public class CountryLocalTimeView extends FrameLayout implements View.OnClickListener{
    @Bind(R.id.local_country_iv)
    ImageView countryImageIV;
    @Bind(R.id.local_time_tv)
    TextView localTimeTV;
    @Bind(R.id.country_flag_layout)
    LinearLayout localLayout;

    @Bind(R.id.local_time_detial_tv)
    TextView localTimeDetialTV;
    @Bind(R.id.local_time_detial_difference_tv)
    TextView timeDiffTV;
    @Bind(R.id.local_time_detial_layout)
    LinearLayout localTimeDetialLayout;

    private volatile long delayedMillis;
    private volatile boolean isStop = false;

    private SimpleDateFormat dateFormat, dateFormat2;
    private String regionStr;
    private boolean isShowDescription = false;

    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public CountryLocalTimeView(Context context) {
        this(context, null);
    }

    public CountryLocalTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_local_time, this);
        ButterKnife.bind(view);

        dateFormat = new SimpleDateFormat("MM月dd日");
        dateFormat2 = new SimpleDateFormat("HH:mm");
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    public void setData(String flag, int timediff, int _timezone, String cityName, String countryName) {
        this.isStop = false;

        regionStr = TextUtils.isEmpty(cityName) ? countryName : cityName;
        if (TextUtils.isEmpty(regionStr)) {
            this.setVisibility(View.GONE);
            return;
        } else {
            this.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(flag)) {
            countryImageIV.setImageResource(R.mipmap.country_flag_default);
        } else {
            Tools.showImage(countryImageIV, flag, R.mipmap.country_flag_default);
        }

        String timeDiffStr = "与北京没有时差，看到消息后我会立即回复，谢谢";
        if (Math.abs(timediff) != 0) {
            timeDiffStr = String.format("与北京有%1$s小时时差，看到消息后我会立即回复，谢谢", "" + Math.abs(timediff));
        }
        timeDiffTV.setText(timeDiffStr);

        showLocalAnimation();

        String timeZoneString = "GMT";
        if (_timezone >= 0) {
            timeZoneString += "+";
        } else {
            timeZoneString += "-";
        }
        timeZoneString += Math.abs(_timezone);
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        dateFormat.setTimeZone(timeZone);
        dateFormat2.setTimeZone(timeZone);
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
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = dateFormat.format(sysTime);
                    CharSequence sysTimeStr2 = dateFormat2.format(sysTime);
                    localTimeDetialTV.setText(String.format("Hi，%1$s现在是 %2$s %3$s", regionStr, sysTimeStr, sysTimeStr2));
                    localTimeTV.setText(String.format("%1$s\n%2$s", sysTimeStr2, regionStr));
                break;
                default:
                break;

            }
        }
    };

    public void showLocalAnimation() {
        localLayout.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(UIUtils.getScreenWidth() - 5, localLayout.getWidth(), localLayout.getHeight(), localLayout.getHeight());
        animation.setDuration(1000);
        localLayout.startAnimation(animation);
    }

    @OnClick({R.id.country_flag_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.country_flag_layout:
                if (isShowDescription) {
                    return;
                }
                localLayout.setVisibility(View.GONE);
                localTimeDetialLayout.setVisibility(View.VISIBLE);

                AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
                alphaAnimation.setDuration(300);
                localTimeDetialLayout.startAnimation(alphaAnimation);
                isShowDescription = true;
                break;
        }
    }

    public void closeDescription() {
        if (isShowDescription) {
            localLayout.setVisibility(View.VISIBLE);
            localTimeDetialLayout.setVisibility(View.GONE);
            isShowDescription = false;
        }
    }
}
