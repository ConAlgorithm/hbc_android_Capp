package com.hugboga.custom.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.OrderSelectCityAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.ToastUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.xutils.common.Callback;

import java.util.Calendar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
/**
 * Created by dyt on 16/4/14.
 */
@ContentView(R.layout.activity_order_select_city)
public class FgOrderSelectCity extends BaseFragment implements NumberPicker.OnValueChangeListener,NumberPicker.Formatter {


    @ViewInject(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @ViewInject(R.id.header_title)
    TextView headerTitle;
    @ViewInject(R.id.header_right_btn)
    ImageView headerRightBtn;
    @ViewInject(R.id.header_right_txt)
    TextView headerRightTxt;
    @ViewInject(R.id.start_city_click)
    TextView startCityClick;
    @ViewInject(R.id.full_day)
    RadioButton fullDay;
    @ViewInject(R.id.half_day)
    RadioButton halfDay;
    @ViewInject(R.id.people_text_click)
    TextView peopleTextClick;
    @ViewInject(R.id.child_text)
    TextView childText;
    @ViewInject(R.id.show_child_seat_layout)
    LinearLayout showChildSeatLayout;
    @ViewInject(R.id.child_no_confirm_click)
    ImageView childNoConfirmClick;
    @ViewInject(R.id.baggage_text_click)
    TextView baggageTextClick;
    @ViewInject(R.id.baggage_no_confirm_click)
    ImageView baggageNoConfirmClick;
    @ViewInject(R.id.start_date)
    TextView startDate;
    @ViewInject(R.id.start_layout_click)
    LinearLayout startLayoutClick;
    @ViewInject(R.id.end_date)
    TextView endDate;
    @ViewInject(R.id.end_layout_click)
    LinearLayout endLayoutClick;
    @ViewInject(R.id.go_city_text_click)
    TextView goCityTextClick;
    @ViewInject(R.id.next_btn_click)
    Button nextBtnClick;
    @ViewInject(R.id.half_day_show)
    LinearLayout half_day_show;

    @ViewInject(R.id.minus)
    TextView minus;
    @ViewInject(R.id.add)
    TextView add;

    @Override
    protected void inflateContent() {
    }

    @Override
    protected void initView() {
        initHeader();
        initSelectPeoplePop();
        fullDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    half_day_show.setVisibility(View.GONE);
                    isHalfTravel = false;
                    endDate.setText(R.string.select_date);
                }
            }
        });
        halfDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    half_day_show.setVisibility(View.VISIBLE);
                    isHalfTravel = true;
                    endDate.setText("---");
                }
            }
        });
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    protected void initHeader() {
        fgTitle.setText(R.string.select_city_title);
//        TextView header_title = (TextView) mainView.findViewById(R.id.header_title);
//        header_title.setText(R.string.select_city_title);

    }

    public String format(int value) {
//        String tmpStr = String.valueOf(value);
//        if (value < 10) {
//            tmpStr = "0" + tmpStr;
//        }
        return value+"";
    }

    private void init() {
        manList.setFormatter(this);
        manList.setOnValueChangedListener(this);
        manList.setMaxValue(11);
        manList.setMinValue(1);
        manList.setValue(1);

        childList.setFormatter(this);
        childList.setOnValueChangedListener(this);
        childList.setMaxValue(11);
        childList.setMinValue(0);
        childList.setValue(0);

        baggageList.setFormatter(this);
        baggageList.setOnValueChangedListener(this);
        baggageList.setMaxValue(11);
        baggageList.setMinValue(0);
        baggageList.setValue(0);

    }

    int manNum = 1;
    int childNum = 0;
    int baggageNum = 0;
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()){
            case R.id.man_list:
                manNum = newVal;
                break;
            case R.id.child_list:
                childNum = newVal;
                break;
            case R.id.baggage_list:
                baggageNum = newVal;
                break;
        }

    }

    private PopupWindow peoplePop;
    private View view;
    OrderSelectCityAdapter adapter;
    TextView cancle;
    TextView ok;
    TextView title;
    NumberPicker manList;
    NumberPicker childList;
    NumberPicker baggageList;

    LinearLayout mans_layout;

    LinearLayout scope_layout,scope_layout_in,scope_layout_out,scope_layout_other;
    TextView out_title,in_title,other_title;
    TextView out_tips,in_tips;

    private void initSelectPeoplePop() {
        view = LayoutInflater.from(this.getActivity()).inflate(R.layout.pop_select_people, null);
        scope_layout  = (LinearLayout) view.findViewById(R.id.scope_layout);
        scope_layout_in  = (LinearLayout) view.findViewById(R.id.scope_layout_in);
        scope_layout_out  = (LinearLayout) view.findViewById(R.id.scope_layout_out);
        scope_layout_other  = (LinearLayout) view.findViewById(R.id.scope_layout_other);
        out_title  = (TextView) view.findViewById(R.id.title);
        in_title  = (TextView) view.findViewById(R.id.title);
        other_title  = (TextView) view.findViewById(R.id.title);
        out_tips  = (TextView) view.findViewById(R.id.title);
        in_tips  = (TextView) view.findViewById(R.id.title);
        scope_layout_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSelectPeoplePop();
            }
        });
        scope_layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSelectPeoplePop();
            }
        });
        scope_layout_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSelectPeoplePop();
            }
        });

        title  = (TextView) view.findViewById(R.id.title);
        childList = (NumberPicker) view.findViewById(R.id.child_list);
        manList = (NumberPicker)view.findViewById(R.id.man_list);
        baggageList = (NumberPicker)view.findViewById(R.id.baggage_list);
        mans_layout = (LinearLayout) view.findViewById(R.id.mans_layout);
        init();
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
                if(baggageList.isShown()){
                    baggageTextClick.setText(String.format(getString(R.string.select_city_baggage_num), baggageNum));
                }else {
                    if(childNum > 0){
                        showChildSeatLayout.setVisibility(View.VISIBLE);
                    }else{
                        showChildSeatLayout.setVisibility(View.GONE);
                    }
                    peopleTextClick.setText(String.format(getString(R.string.select_city_man_child_num), manNum, childNum));
                }
                hideSelectPeoplePop();
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

    //type 1,选人 2,行李 3,选城市范围
    public void showSelectPeoplePop(int type) {
        if (null != peoplePop) {
            if(type == 2){
                baggageList.setVisibility(View.VISIBLE);
                mans_layout.setVisibility(View.GONE);
                scope_layout.setVisibility(View.GONE);
                title.setText(R.string.select_baggage_tips);
                ok.setVisibility(View.VISIBLE);
                cancle.setVisibility(View.VISIBLE);
            }else if(type ==1){
                baggageList.setVisibility(View.GONE);
                mans_layout.setVisibility(View.VISIBLE);
                scope_layout.setVisibility(View.GONE);
                title.setText(R.string.select_people_tips);
                ok.setVisibility(View.VISIBLE);
                cancle.setVisibility(View.VISIBLE);
            }else if(type == 3){
                baggageList.setVisibility(View.GONE);
                mans_layout.setVisibility(View.GONE);
                scope_layout.setVisibility(View.VISIBLE);
                ok.setVisibility(View.GONE);
                cancle.setVisibility(View.GONE);
                title.setText(R.string.select_scope);
            }
            peoplePop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }
    public void hideSelectPeoplePop() {
        if (null != peoplePop) {
            peoplePop.dismiss();
        }
    }

    CityBean startBean;
    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCity.class.getSimpleName().equals(from)) {
            String fromKey = bundle.getString(KEY_FROM);
            if ("startAddress".equals(fromKey)) {
                startBean = (CityBean) bundle.getSerializable(FgChooseCity.KEY_CITY);
                startCityClick.setText(startBean.name);

            }
        }
    }

    int childSeatNums = 0;
    boolean isHalfTravel = false;
    @Event({R.id.minus,R.id.add,R.id.header_left_btn, R.id.start_city_click, R.id.people_text_click, R.id.show_child_seat_layout, R.id.child_no_confirm_click, R.id.baggage_text_click, R.id.baggage_no_confirm_click, R.id.start_layout_click, R.id.end_layout_click, R.id.go_city_text_click, R.id.next_btn_click})
    private void onClickView(View view){
        switch (view.getId()) {
            case R.id.minus:
                if(childSeatNums >=1) {
                    childSeatNums--;
                    childText.setText(getString(R.string.select_city_child) + childSeatNums);
                }
                break;
            case R.id.add:
                if(childSeatNums <= 10) {
                    childSeatNums++;
                    childText.setText(getString(R.string.select_city_child) + childSeatNums);
                }
                break;
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.start_city_click:
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM, "startAddress");
                startFragment(new FgChooseCity(), bundle);
                break;
            case R.id.people_text_click:
                showSelectPeoplePop(1);
                break;
            case R.id.show_child_seat_layout:
                break;
            case R.id.child_no_confirm_click:
                break;
            case R.id.baggage_text_click:
                showSelectPeoplePop(2);
                break;
            case R.id.baggage_no_confirm_click:
                break;
            case R.id.start_layout_click:
                showDaySelect(startDate);
                break;
            case R.id.end_layout_click:
                if(!isHalfTravel) {
                    showDaySelect(endDate);
                }
                break;
            case R.id.go_city_text_click:
                showSelectPeoplePop(3);
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
        dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框

    }

    String strat_date_str = "";
    String end_date_str = "";

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
            if(mTextView.getId() == R.id.start_date){

                if(TextUtils.isEmpty(strat_date_str)){
                    if(TextUtils.isEmpty(end_date_str)){
                        strat_date_str = serverDate;
                        mTextView.setText(serverDate);
                    }else{
                        if(DateUtils.compareDate(serverDate,end_date_str)<= 0){
                            strat_date_str = serverDate;
                            mTextView.setText(serverDate);
                        }else{
                            ToastUtils.showLong(R.string.start_end_error);
                        }
                    }
                }else{

                    if(DateUtils.compareDate(serverDate,end_date_str)<= 0){
                        strat_date_str = serverDate;
                        mTextView.setText(serverDate);
                    }else{
                        ToastUtils.showLong(R.string.start_end_error);
                    }
                }

            }else{
                if(TextUtils.isEmpty(strat_date_str)){
                    end_date_str = serverDate;
                    mTextView.setText(serverDate);
                }else{
                    if(DateUtils.compareDate(strat_date_str,serverDate) <= 0){
                        end_date_str = serverDate;
                        mTextView.setText(serverDate);
                    }else{
                        ToastUtils.showLong(R.string.start_end_error);
                    }

                }
            }
        }
    }


}
