package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.InsureResultBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestAddInsure;
import com.hugboga.custom.data.request.RequestEditInsure;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.ToastUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created  on 16/4/22.
 */

@ContentView(R.layout.add_new_insure)
public class FgAddInsure extends BaseFragment implements HttpRequestListener {
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


    @Override
    protected void initHeader() {
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
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

    boolean isEdit = false;
    private void getArgument(){
        insureResultBean = this.getArguments().getParcelable("insureResultBean");
        if(null != insureResultBean){
            isEdit = true;
            name.setText(insureResultBean.name);
            cardid.setText(insureResultBean.passportNo);
            sexInt = insureResultBean.sex;
            sex.setText(sexInt == 0?"男":"女");
            birthday.setText(insureResultBean.birthday);
            fgTitle.setText("编辑投保人");
            check();
        }else{
            insureResultBean = new InsureResultBean();
            isEdit = false;
            fgTitle.setText("新增投保人");
        }
    }

    InsureResultBean insureResultBean;
    private void edit(){
        RequestEditInsure requestEditInsure = new RequestEditInsure(this.getActivity(),
                UserEntity.getUser().getUserId(this.getActivity()),insureResultBean.insuranceUserId,
                name.getText().toString(), cardid.getText().toString(),
                sexInt+"", birthday.getText().toString());
        HttpRequestUtils.request(this.getActivity(),requestEditInsure,this);
    }

    private void add(){
        RequestAddInsure requestAddInsure = new RequestAddInsure(this.getContext(), UserEntity.getUser().getUserId(this.getContext()),
                name.getText().toString(), cardid.getText().toString(),
                sexInt+"", birthday.getText().toString());
        HttpRequestUtils.request(this.getActivity(), requestAddInsure, this);
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        insureResultBean = (InsureResultBean)(request.getData()) ;
        if(request instanceof RequestEditInsure){
            EventBus.getDefault().post(new EventAction(EventType.EDIT_BACK_INSURE,insureResultBean));
        }else if(request instanceof RequestAddInsure){
            insureResultBean = (InsureResultBean)(request.getData()) ;
            EventBus.getDefault().post(new EventAction(EventType.ADD_INSURE,insureResultBean));
        }
        finish();
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {
        super.onDataRequestCancel(request);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    private void disableNextBtn() {
        nextBtnClick.setEnabled(false);
        nextBtnClick.setBackgroundColor(Color.parseColor("#d5dadb"));
    }

    private void enableNextBtn() {
        nextBtnClick.setEnabled(true);
        nextBtnClick.setBackgroundColor(ContextCompat.getColor(this.getActivity(), R.color.all_bg_yellow));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void showDaySelect(TextView sDateTime) {
        Calendar cal = Calendar.getInstance();
        MyDatePickerListener myDatePickerDialog = new MyDatePickerListener(sDateTime);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                myDatePickerDialog, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        cal = Calendar.getInstance();
//        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
//        dpd.setMaxDate(cal);
        dpd.show(this.getActivity().getFragmentManager(), "DatePickerDialog");   //显示日期设置对话框

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
            check();
        }
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
    @OnClick({R.id.sex, R.id.birthday,R.id.next_btn_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn_click:
                if(isEdit){
                    edit();
                }else{
                    add();
                }
                break;
            case R.id.sex:
                final CharSequence[] items3 = getResources().getStringArray(R.array.my_info_sex);
                final AlertDialog.Builder sexDialog = new AlertDialog.Builder(getActivity());
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
                showDaySelect(birthday);
                break;
        }
    }
}
