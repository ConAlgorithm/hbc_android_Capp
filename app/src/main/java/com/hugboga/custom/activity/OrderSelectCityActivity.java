package com.hugboga.custom.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.huangbaoche.hbcframe.activity.BaseFragmentActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.OrderSelectCityAdapter;
import com.hugboga.custom.fragment.FgChooseCity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.w3c.dom.Text;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyt on 16/4/13.
 */


public class OrderSelectCityActivity extends BaseFragmentActivity {


    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.start_city_click)
    TextView startCityClick;
    @Bind(R.id.full_day)
    RadioButton fullDay;
    @Bind(R.id.half_day)
    RadioButton halfDay;
    @Bind(R.id.people_text_click)
    TextView peopleTextClick;
    @Bind(R.id.child_text)
    TextView childText;
    @Bind(R.id.show_child_seat_layout)
    LinearLayout showChildSeatLayout;
    @Bind(R.id.child_no_confirm_click)
    TextView childNoConfirmClick;
    @Bind(R.id.baggage_text_click)
    TextView baggageTextClick;
    @Bind(R.id.baggage_no_confirm_click)
    TextView baggageNoConfirmClick;
    @Bind(R.id.start_date)
    TextView startDate;
    @Bind(R.id.start_layout_click)
    LinearLayout startLayoutClick;
    @Bind(R.id.end_date)
    TextView endDate;
    @Bind(R.id.end_layout_click)
    LinearLayout endLayoutClick;
    @Bind(R.id.go_city_text_click)
    TextView goCityTextClick;
    @Bind(R.id.next_btn_click)
    Button nextBtnClick;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_order_select_city);
        ButterKnife.bind(this);
        initHeader();
        initSelectPeoplePop();
    }

    protected void initHeader() {
        TextView header_title = (TextView) findViewById(R.id.header_title);
        header_title.setText(R.string.select_city_title);

    }

    private PopupWindow peoplePop;
    private View view;
    OrderSelectCityAdapter adapter;
    TextView cancle;
    TextView ok;
    TextView title;
    ListView manList;
    ListView childList;
    private void initSelectPeoplePop() {
        view = LayoutInflater.from(this).inflate(R.layout.pop_select_people, null);
        childList = (ListView) view.findViewById(R.id.child_list);
        manList = (ListView)view.findViewById(R.id.man_list);
        adapter = new OrderSelectCityAdapter(this);
        manList.setAdapter(adapter);
        childList.setAdapter(adapter);
        cancle = (TextView)view.findViewById(R.id.cancle);
        ok = (TextView)view.findViewById(R.id.ok);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSelectPeoplePop();
            }
        });
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showSelectPeoplePop();
            }
        });
        peoplePop = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        peoplePop.setBackgroundDrawable(new BitmapDrawable());
        peoplePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        peoplePop.setOutsideTouchable(true);
        peoplePop.setFocusable(true);
    }

    public void showSelectPeoplePop() {
        if (null != peoplePop) {
            peoplePop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }
    public void hideSelectPeoplePop() {
        if (null != peoplePop) {
            peoplePop.dismiss();
        }
    }

    @OnClick({R.id.header_left_btn, R.id.start_city_click, R.id.people_text_click, R.id.show_child_seat_layout, R.id.child_no_confirm_click, R.id.baggage_text_click, R.id.baggage_no_confirm_click, R.id.start_layout_click, R.id.end_layout_click, R.id.go_city_text_click, R.id.next_btn_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.start_city_click:
                startFragment(new FgChooseCity());
                break;
            case R.id.people_text_click:
                showSelectPeoplePop();
                break;
            case R.id.show_child_seat_layout:
                break;
            case R.id.child_no_confirm_click:
                break;
            case R.id.baggage_text_click:
                break;
            case R.id.baggage_no_confirm_click:
                break;
            case R.id.start_layout_click:
                showDaySelect(startDate);
                break;
            case R.id.end_layout_click:
                showDaySelect(endDate);
                break;
            case R.id.go_city_text_click:
                break;
            case R.id.next_btn_click:
                break;
        }
    }

    public void showDaySelect(TextView sDateTime) {
        Calendar cal = Calendar.getInstance();
        MyDatePickerListener myDatePickerDialog = new MyDatePickerListener(sDateTime);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                myDatePickerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal = Calendar.getInstance();
        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        dpd.setMaxDate(cal);
        dpd.show(this.getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框

    }


    class MyDatePickerListener implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
        TextView mTextView;

        MyDatePickerListener(TextView textView) {
            this.mTextView = textView;
        }

        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear + 1;
            String monthStr = String.format("%02d", month);
            String dayOfMonthStr = String.format("%02d", dayOfMonth);
            String serverDate = year + "-" + monthStr + "-" + dayOfMonthStr;
            mTextView.setText(serverDate);
        }
    }


}
