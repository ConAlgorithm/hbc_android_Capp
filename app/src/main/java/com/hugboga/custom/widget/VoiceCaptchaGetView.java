package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCountryActivity;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Created by qingcha on 17/10/20.
 */

public class VoiceCaptchaGetView extends LinearLayout implements TextWatcher {

    @Bind(R.id.vvc_get_code_tv)
    TextView codeTV;
    @Bind(R.id.vvc_get_phone_et)
    EditText phoneET;
    @Bind(R.id.vvc_get_phone_confirm_tv)
    TextView confirmTV;

    private String code = "86";
    private OnConfirmListener listener;

    public VoiceCaptchaGetView(Context context) {
        this(context, null);
    }

    public VoiceCaptchaGetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_vc_get, this);
        ButterKnife.bind(view);
        phoneET.addTextChangedListener(this);

        phoneET.setFocusable(true);
        phoneET.setFocusableInTouchMode(true);
        phoneET.requestFocus();
    }

    @OnClick({R.id.vvc_get_code_tv, R.id.vvc_get_code_arrow_iv, R.id.vvc_get_phone_confirm_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vvc_get_code_tv:
            case R.id.vvc_get_code_arrow_iv:
                Intent intent = new Intent(getContext(), ChooseCountryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.vvc_get_phone_confirm_tv:
                if (phoneET.getText() == null || TextUtils.isEmpty(phoneET.getText().toString())) {
                    CommonUtils.showToast(R.string.voice_captcha_input_error);
                } else if (!checkData()) {
                    return;
                }
                if (listener != null) {
                    listener.onConfirm(code, phoneET.getText().toString());
                }
                break;
        }
    }

    public void setAreaCode(String _code) {
        if (TextUtils.isEmpty(_code)) {
            return;
        }
        code = CommonUtils.addPhoneCodeSign(_code);
        codeTV.setText(code);
    }

    public void setPhone(String _phone) {
        phoneET.setText(_phone);
        setConfirmViewEnabled(!TextUtils.isEmpty(_phone));
        phoneET.setSelection(_phone != null ? _phone.length() : 0);
    }

    public EditText getPhoneEditText() {
        return phoneET;
    }

    public boolean checkData() {
        return CommonUtils.checkInlandPhoneNumber(code, phoneET.getText() != null ? phoneET.getText().toString() : "");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean isEnabled = s != null && !TextUtils.isEmpty(s.toString());
        setConfirmViewEnabled(isEnabled);
    }

    public interface OnConfirmListener {
        public void onConfirm(String code, String phone);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    private void setConfirmViewEnabled(boolean isEnabled) {
        confirmTV.setEnabled(isEnabled);
        confirmTV.setBackgroundResource(isEnabled ? R.drawable.shape_rounded_yellow_btn : R.drawable.shape_rounded_gray_btn);
    }
}
