package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestAddInsure;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.ToastUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyt on 16/4/22.
 */

@ContentView(R.layout.add_new_insure)
public class FgAddInsure extends BaseFragment implements HttpRequestListener {
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.cardid)
    EditText cardid;
    @Bind(R.id.sex)
    EditText sex;
    @Bind(R.id.birthday)
    EditText birthday;
    @Bind(R.id.next_btn_click)
    Button nextBtnClick;


    @Override
    protected void initHeader() {
        fgTitle.setText("新增投保人");
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
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

    private void check() {
        if (TextUtils.isEmpty(name.getText())) {
            ToastUtils.showLong("姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(cardid.getText())) {
            ToastUtils.showLong("护照不能为空");
            return;
        }
        if (TextUtils.isEmpty(sex.getText())) {
            ToastUtils.showLong("性别不能为空");
            return;
        }
        if (TextUtils.isEmpty(birthday.getText())) {
            ToastUtils.showLong("出生日期不能为空");
            return;
        }

        RequestAddInsure requestAddInsure = new RequestAddInsure(this.getContext(),UserEntity.getUser().getUserId(this.getContext()),
                name.getText().toString(),cardid.getText().toString(),
                sex.getText().toString(),birthday.getText().toString());
        HttpRequestUtils.request(this.getActivity(), requestAddInsure, this);
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
        dpd.setMinDate(cal);
        cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        dpd.setMaxDate(cal);
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

    @OnClick({R.id.sex, R.id.birthday})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_btn_click:
                check();
                break;
            case R.id.sex:
                final CharSequence[] items3 = getResources().getStringArray(R.array.my_info_sex);
                final AlertDialog.Builder sexDialog = new AlertDialog.Builder(getActivity());
                sexDialog.setTitle("选择性别");
                sexDialog.setSingleChoiceItems(items3, getSexInt(items3), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sexStr = items3[which].toString();
                        sex.setText(sexStr);
                        dialog.dismiss();
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
