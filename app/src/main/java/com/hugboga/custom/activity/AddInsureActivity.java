package com.hugboga.custom.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.InsureResultBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestAddInsure;
import com.hugboga.custom.data.request.RequestEditInsure;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created on 16/8/6.
 */

public class AddInsureActivity extends BaseActivity implements HttpRequestListener {
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.cardid)
    EditText cardid;
    @Bind(R.id.sex)
    TextView sex;
    @Bind(R.id.birthday)
    TextView birthday;
    @Bind(R.id.next_btn_click)
    Button nextBtnClick;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;


    protected void initHeader() {
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                finish();
            }
        });
    }

    protected void initView() {
        getArgument();
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    disableNextBtn();
                } else {
                    check();
                }
            }
        });
        cardid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    disableNextBtn();
                } else {
                    check();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
//        hideInputMethod(name);
//        hideInputMethod(cardid);
        hideSoftInput();
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.add_new_insure);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getArgument();
        initView();
        initHeader();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    boolean isEdit = false;

    private void getArgument() {
        insureResultBean = (InsureResultBean) this.getIntent().getSerializableExtra("insureResultBean");
        if (null != insureResultBean) {
            isEdit = true;
            name.setText(insureResultBean.name);
            cardid.setText(insureResultBean.passportNo);
            sexInt = insureResultBean.sex;
            sex.setText(sexInt == 0 ? "男" : "女");
            birthday.setText(insureResultBean.birthday);
            headerTitle.setText("编辑投保人");
            check();
        } else {
            insureResultBean = new InsureResultBean();
            isEdit = false;
            headerTitle.setText("新增投保人");
        }
    }

    InsureResultBean insureResultBean;

    private void edit() {
        RequestEditInsure requestEditInsure = new RequestEditInsure(activity,
                UserEntity.getUser().getUserId(activity), insureResultBean.insuranceUserId,
                name.getText().toString(), cardid.getText().toString(),
                sexInt + "", birthday.getText().toString());
        HttpRequestUtils.request(activity, requestEditInsure, this);
    }

    private void add() {
        RequestAddInsure requestAddInsure = new RequestAddInsure(activity, UserEntity.getUser().getUserId(activity),
                name.getText().toString(), cardid.getText().toString(),
                sexInt + "", birthday.getText().toString());
        HttpRequestUtils.request(activity, requestAddInsure, this);
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        insureResultBean = (InsureResultBean) (request.getData());
        if (request instanceof RequestEditInsure) {
            EventBus.getDefault().post(new EventAction(EventType.EDIT_BACK_INSURE, insureResultBean));
        } else if (request instanceof RequestAddInsure) {
            insureResultBean = (InsureResultBean) (request.getData());
            EventBus.getDefault().post(new EventAction(EventType.ADD_INSURE, insureResultBean));
        }
        finish();
    }

    private void disableNextBtn() {
        nextBtnClick.setEnabled(false);
        nextBtnClick.setBackgroundColor(Color.parseColor("#d5dadb"));
    }

    private void enableNextBtn() {
        nextBtnClick.setEnabled(true);
        nextBtnClick.setBackgroundColor(ContextCompat.getColor(activity, R.color.all_bg_yellow));
    }

    ChooseDateBean chooseDateBean;
    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_DATE:
                chooseDateBean = (ChooseDateBean) action.getData();
                birthday.setText(chooseDateBean.halfDateStr);
                check();
                break;
        }
    }

    private void check() {
        if (TextUtils.isEmpty(name.getText())) {
//            ToastUtils.showLong("姓名不能为空");
            disableNextBtn();
            return;
        }
        if (TextUtils.isEmpty(cardid.getText())) {
//            ToastUtils.showLong("护照不能为空");
            disableNextBtn();
            return;
        }
        if (TextUtils.isEmpty(sex.getText())) {
//            ToastUtils.showLong("性别不能为空");
            disableNextBtn();
            return;
        }
        if (TextUtils.isEmpty(birthday.getText())) {
//            ToastUtils.showLong("出生日期不能为空");
            disableNextBtn();
            return;
        }
        enableNextBtn();
    }

    DatePicker picker;
    SimpleDateFormat dateDateFormat;
    public void showDaySelect() {
//        Intent intent = new Intent(activity,DatePickerActivity.class);
//        intent.putExtra("startDate","1990-01-01");
//        intent.putExtra("title","请选择出生日期");
//        intent.putExtra("type",3);
//        startActivity(intent);
        Calendar calendar = Calendar.getInstance();
        picker = new DatePicker(activity, DatePicker.YEAR_MONTH_DAY);
        picker.setRangeStart(1900, 01, 01);
        Calendar currentCalendar = Calendar.getInstance();
        picker.setRangeEnd(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1, currentCalendar.get(Calendar.DATE));
        picker.setTitleText("请选择出生日期");
        try {
            if (!TextUtils.isEmpty(birthday.getText())) {
                if (dateDateFormat == null) {
                    dateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                }
                calendar.setTime(dateDateFormat.parse(birthday.getText().toString()));
                picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
            } else {
                picker.setSelectedItem(1990, 1, 1);
            }
        } catch (ParseException e) {
            picker.setSelectedItem(1990, 1, 1);
        }
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String serverDate = year + "-" + month + "-" + day;
                birthday.setText(serverDate);
                check();
                picker.dismiss();
            }
        });
        picker.show();

    }



    private int getSexInt(CharSequence[] items3) {
        String str = sex.getText().toString();
        if (str == null || str.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < items3.length; i++) {
            if (str.equals(items3[i])) {
                return i;
            }
        }
        return -1;
    }

    int sexInt = 0;

    @OnClick({R.id.sex, R.id.birthday, R.id.next_btn_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn_click:
                if (isEdit) {
                    edit();
                } else {
                    add();
                }
                break;
            case R.id.sex:
                final CharSequence[] items3 = getResources().getStringArray(R.array.my_info_sex);
                final AlertDialog.Builder sexDialog = new AlertDialog.Builder(activity);
                sexDialog.setTitle("选择性别");
                sexDialog.setSingleChoiceItems(items3, getSexInt(items3), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sexInt = which;
                        String sexStr = items3[which].toString();
                        sex.setText(sexStr);
                        dialog.dismiss();
                        check();
                    }
                });

                Dialog dialog = sexDialog.create();
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.birthday:
                showDaySelect();
                break;
        }
    }
}

