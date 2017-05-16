package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.fragment.FgChooseAirAddress;
import com.hugboga.custom.fragment.FgChooseAirNumber;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created on 16/8/3.
 */

public class ChooseAirActivity extends BaseActivity {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.choose_content)
    FrameLayout choose_content;


    TextView fgTitle;
    ImageView header_left_btn;
    @Bind(R.id.daily_tap_1)
    TextView dailyTap1;
    @Bind(R.id.daily_tap_line1)
    View dailyTapLine1;
    @Bind(R.id.daily_layout_1)
    RelativeLayout dailyLayout1;
    @Bind(R.id.daily_tap_2)
    TextView dailyTap2;
    @Bind(R.id.daily_tap_line2)
    View dailyTapLine2;
    @Bind(R.id.daily_layout_2)
    RelativeLayout dailyLayout2;
    @Bind(R.id.header_center)
    LinearLayout headerCenter;

    public void initHeader() {
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                finish();
            }
        });
        headerTitle.setText("选择航班");
        headerRightBtn.setVisibility(View.VISIBLE);
        headerRightBtn.setImageResource(R.mipmap.topbar_cs);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_choose_air;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
        initView();
        initHeader();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    ChooseDateBean chooseDateBean;
    @Subscribe
    public void onEventMainThread(final EventAction action) {

        switch (action.getType()) {
            case PICK_FLIGHT_BACK:
                FlightBean flightBean  = (FlightBean) action.getData();
                EventBus.getDefault().post(new EventAction(EventType.AIR_NO,flightBean));
                finish();
                break;
        }
    }

    FgChooseAirAddress fgChooseAirAddress;
    FgChooseAirNumber fgChooseAirNumber;
    private FragmentManager fm;

    RadioButton address;
    RadioButton number;

    View rootView;
    FragmentTransaction transaction;

    public void initView() {

        fgChooseAirAddress = new FgChooseAirAddress();
        fgChooseAirNumber = new FgChooseAirNumber();

        final Bundle bundle = new Bundle();

        fm = getSupportFragmentManager();//getFragmentManager();

        transaction = fm.beginTransaction();
        transaction.add(R.id.choose_content, fgChooseAirNumber);
        transaction.commit();
    }
    private void selectTap(int index) {
        if (index == 1) {
            dailyTapLine1.setVisibility(View.GONE);
            dailyTapLine2.setVisibility(View.VISIBLE);
            dailyTap1.setTextColor(getResources().getColor(R.color.common_font_air));
            dailyTap2.setTextColor(getResources().getColor(R.color.common_font_color_black));
        } else {
            dailyTapLine1.setVisibility(View.VISIBLE);
            dailyTapLine2.setVisibility(View.GONE);
            dailyTap1.setTextColor(getResources().getColor(R.color.common_font_color_black));
            dailyTap2.setTextColor(getResources().getColor(R.color.common_font_air));
        }
    }

    private int pickOrSend = 1; //1接机 2送机

    @OnClick({R.id.header_left_btn, R.id.header_title, R.id.daily_layout_1, R.id.daily_layout_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                break;
            case R.id.header_title:
                break;
            case R.id.daily_layout_1:
                hideSoftInput();
                selectTap(0);
                pickOrSend = 1;
                if (!fgChooseAirAddress.isAdded()) {
                    transaction = fm.beginTransaction();
                    transaction.add(R.id.choose_content, fgChooseAirAddress);
                    transaction.commit();
                } else {
                    transaction = fm.beginTransaction();
                    transaction.hide(fgChooseAirNumber);
                    transaction.show(fgChooseAirAddress);
                    transaction.commit();
                }
                break;
            case R.id.daily_layout_2:
                hideSoftInput();
                selectTap(1);
                pickOrSend = 2;
                if (!fgChooseAirNumber.isAdded()) {
                    transaction = fm.beginTransaction();
                    transaction.add(R.id.choose_content, fgChooseAirNumber);
                    transaction.commit();
                } else {
                    transaction = fm.beginTransaction();
                    transaction.hide(fgChooseAirAddress);
                    transaction.show(fgChooseAirNumber);
                    transaction.commit();
                }
                break;
        }
    }
}