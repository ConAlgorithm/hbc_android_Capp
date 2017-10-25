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

public class VoiceVerifyCodeGetView extends LinearLayout implements TextWatcher {

    public static final int REQUEST_CODE = 0xFF001;

    @Bind(R.id.vvc_get_code_tv)
    TextView codeTV;
    @Bind(R.id.vvc_get_phone_et)
    EditText phoneET;
    @Bind(R.id.vvc_get_phone_confirm_tv)
    TextView confirmTV;

    private String code = "86";
    private OnConfirmListener listener;

    public VoiceVerifyCodeGetView(Context context) {
        this(context, null);
    }

    public VoiceVerifyCodeGetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_vvc_get, this);
        ButterKnife.bind(view);
        phoneET.addTextChangedListener(this);
    }

    @OnClick({R.id.vvc_get_code_tv, R.id.vvc_get_code_arrow_iv, R.id.vvc_get_phone_confirm_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vvc_get_code_tv:
            case R.id.vvc_get_code_arrow_iv:
                Intent intent = new Intent(getContext(), ChooseCountryActivity.class);
                intent.putExtra(ChooseCountryActivity.PARAM_VIEW_ID, REQUEST_CODE);
                getContext().startActivity(intent);
                break;
            case R.id.vvc_get_phone_confirm_tv:
                if (!checkData()) {
                    return;
                }
                if (listener != null) {
                    listener.onConfirm(code, phoneET.getText().toString());
                }
                break;
        }
    }

    public void setAreaCode(String _code) {
        code = CommonUtils.addPhoneCodeSign(_code);
        codeTV.setText(code);
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
        confirmTV.setEnabled(isEnabled);
        confirmTV.setBackgroundResource(isEnabled ? R.drawable.shape_rounded_yellow_btn : R.drawable.shape_rounded_gray_btn);
    }

    public interface OnConfirmListener {
        public void onConfirm(String code, String phone);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }
}
