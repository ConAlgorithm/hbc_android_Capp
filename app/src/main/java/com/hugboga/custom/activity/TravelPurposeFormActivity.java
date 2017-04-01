package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestTravelPurposeForm;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.SharedPre;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;


/**
 * Created by Administrator on 2017/2/27.
 */

public class TravelPurposeFormActivity extends BaseActivity implements View.OnClickListener{

    public static final String TAG = "only_year_month_day";   //在加载时间控件的时候用

    @Bind(R.id.header_left_btn)
    ImageView headerLeft;
    @Bind(R.id.header_right_btn)
    ImageView headerRight;
    @Bind(R.id.header_title)
    TextView title;

    @Bind(R.id.city_name)
    TextView cityName;//目的地
    @Bind(R.id.start_date)
    TextView startDate;//出发日期
    @Bind(R.id.uncertain_check)
    CheckBox unCertainCheck;//是否确定日期
    @Bind(R.id.remark)
    EditText remark;//备注信息
    @Bind(R.id.user_name)
    EditText userName;//姓名
    @Bind(R.id.areacode)
    TextView areaCode;//区号
    @Bind(R.id.phone)
    EditText phone;//电话
    @Bind(R.id.submit_btn)
    Button submitBtn;//提交按钮

    CityBean cityBean;
    AreaCodeBean areaCodeBean;
    DateTimePicker picker;
    String tripTimeStr ;
    String areaCodeStr = "86";

    //EditText变化监听
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setButtonStatus(submitBtn,checkContent());//设置提交按钮状态
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_purpose_form);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    public void init(){
        title.setText(getString(R.string.travel_purpose_title));
        headerLeft.setVisibility(View.VISIBLE);
        headerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerRight.setVisibility(View.GONE);
        //添加监听
        userName.addTextChangedListener(watcher);
        phone.addTextChangedListener(watcher);
        remark.addTextChangedListener(watcher);
        //手机号初始化
        if (!TextUtils.isEmpty(SharedPre.getString(SharedPre.CODE,null)) && !TextUtils.isEmpty(SharedPre.getString(SharedPre.PHONE,null))){
            areaCode.setText("+"+SharedPre.getString(SharedPre.CODE,null).trim());
            areaCodeStr = SharedPre.getString(SharedPre.CODE,null).trim();
            phone.setText(SharedPre.getString(SharedPre.PHONE,null).trim());
        }
        //时间不确定
        unCertainCheck.setChecked(false);
        unCertainCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setButtonStatus(submitBtn,checkContent());
                if (isChecked){
                    startDate.setTextColor(0xFFCCCCCC);
                } else {
                    startDate.setTextColor(getResources().getColor(R.color.basic_black));
                }
            }
        });
        //初始button
        setButtonStatus(submitBtn,checkContent());
    }


    @OnClick({R.id.purpose_place_layout,R.id.start_date,R.id.remark,R.id.areacode,R.id.submit_btn})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.purpose_place_layout:
                Intent intent = new Intent(this,ChooseCityActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM, "purpose");
                bundle.putInt(KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_DAILY);
                bundle.putString(ChooseCityActivity.KEY_FROM_TAG, CharterFirstStepActivity.TAG);
                intent.putExtras(bundle);
                intent.putExtra("fromInterCity",true);
                startActivity(intent);
                break;
            case R.id.start_date:
                gotoSelectDate();
                break;
            case R.id.areacode:
                collapseSoftInputMethod(remark);//收起可能还在展示的软键盘
                collapseSoftInputMethod(userName);
                collapseSoftInputMethod(phone);
                Intent intent1 = new Intent(this,ChooseCountryActivity.class);
                startActivity(intent1);
                break;
            case R.id.submit_btn:
                gotoNext();//提交
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action){
        switch (action.getType()){
            case PURPOSER_CITY:           //选择城市
                if (action.getData() instanceof CityBean) {
                    cityBean = (CityBean) action.getData();
                    cityName.setText(cityBean.name);
                }
                setButtonStatus(submitBtn,checkContent());
                break;
            case CHOOSE_COUNTRY_BACK:           //选择areaCode
                if (action.getData() instanceof AreaCodeBean){
                    areaCodeBean = (AreaCodeBean)action.getData();
                    areaCode.setText("+"+areaCodeBean.getCode());
                    areaCodeStr = areaCodeBean.getCode();
                }
                setButtonStatus(submitBtn,checkContent());
                break;
        }
    }

    /**
     * 判断每个字段是否都已填
     * @return
     */
    public Boolean checkContent(){
        if (TextUtils.isEmpty(cityName.getText().toString().trim())){
            return false;
        }else if (TextUtils.isEmpty(userName.getText().toString().trim())){
            return false;
        }else if (TextUtils.isEmpty(areaCode.getText().toString().trim())){
            return false;
        }else if (TextUtils.isEmpty(phone.getText().toString().trim())) {
            return false;
        }else if (TextUtils.isEmpty(startDate.getText().toString().trim())){
            if (unCertainCheck.isChecked()){
                return true;
            }else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 设置提交按钮的状态
     * @param button
     */
    public void setButtonStatus(Button button,Boolean b){
        if (b){
            button.setEnabled(true);
            button.setFocusable(true);
            button.setBackground(getResources().getDrawable(R.drawable.shape_rounded_yellow_btn));
        }else {
            button.setEnabled(false);
            button.setFocusable(false);
            button.setBackground(getResources().getDrawable(R.drawable.shape_rounded_gray_btn));
        }
    }

    /**
     * 选择时间
     */
    public void gotoSelectDate(){
        final Calendar calendar = Calendar.getInstance();
        picker = new DateTimePicker(this,DateTimePicker.ONLY_YEAR_MONTH_DAY);
        //设置控件头部属性
        picker.setTitleText("请选择旅行日期");
        picker.setTitleTextSize(18);
        picker.setTitleTextColor(getResources().getColor(R.color.basic_black));
        picker.setCancelTextColor(getResources().getColor(R.color.default_yellow));
        picker.setSubmitTextColor(getResources().getColor(R.color.default_yellow));
        picker.setTopBackgroundColor(getResources().getColor(R.color.text_color_grey));
        picker.setLineColor(getResources().getColor(R.color.text_hint_color));
        picker.useMaxRatioLine();

        picker.setRange(calendar.get(Calendar.YEAR),calendar.get(Calendar.YEAR)+1);
        picker.setSelectedItem(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                String tempDate = year + "年" + month + "月";
                String currentDate = calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月";
                if(DateUtils.getDateByStr2(tempDate).before(DateUtils.getDateByStr2(currentDate))){
                    CommonUtils.showToast("不能选择今天之前的时间");
                    return;
                }
                startDate.setText(tempDate);
                picker.dismiss();
                setButtonStatus(submitBtn, checkContent());
            }
        });
        picker.show();
    }

    /**
     * 点击提交
     */
    public void gotoNext(){
        if ("+86".equals(areaCode.getText().toString().trim())){
            if (!phone.getText().toString().startsWith("1") || !(11 == phone.getText().toString().length())){
                Toast.makeText(this,R.string.phone_format_incorrect,Toast.LENGTH_SHORT).show();
                phone.setFocusable(true);
                return;
            }
        }

        //判断不确定按钮是否已经选中
        if (unCertainCheck.isChecked()){
            tripTimeStr = "待定";
        } else {
            tripTimeStr = startDate.getText().toString();
        }
        RequestTravelPurposeForm requestTravelPurposeForm = new RequestTravelPurposeForm(this, UserEntity.getUser().getUserId(this),
                UserEntity.getUser().getUserName(this),UserEntity.getUser().getAreaCode(this),UserEntity.getUser().getPhone(this),
                String.valueOf(cityBean.cityId),cityBean.name,tripTimeStr,
                remark.getText().toString(),areaCodeStr,phone.getText().toString(),
                userName.getText().toString().toString());
        requestData(requestTravelPurposeForm);
        MobClickUtils.onEvent(StatisticConstant.YI_XIANG_SUCCEED);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        AlertDialogUtils.showAlertDialog(this, getResources().getString(R.string.submit_success), getResources().getString(R.string.alert_submit_success), "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(TravelPurposeFormActivity.this, TravelPurposeFormListActivity.class);
                TravelPurposeFormActivity.this.startActivity(intent);
                TravelPurposeFormActivity.this.finish();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
