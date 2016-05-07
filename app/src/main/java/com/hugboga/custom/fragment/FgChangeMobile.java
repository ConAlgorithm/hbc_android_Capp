package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestChangeMobile;
import com.hugboga.custom.data.request.RequestVerity;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fg_change_mobile)
public class FgChangeMobile extends BaseFragment {

    @ViewInject(R.id.change_mobile_phone_view)
    TextView phoneTextView;
    @ViewInject(R.id.change_mobile_areacode)
    TextView areaCodeTextView;
    @ViewInject(R.id.change_mobile_mobile)
    EditText mobileEditText;
    @ViewInject(R.id.change_mobile_verity)
    EditText verityEditText;
    @ViewInject(R.id.change_mobile_getcode)
    TextView getCodeBtn;
    @ViewInject(R.id.change_mobile_time)
    TextView timeTextView;

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestChangeMobile) {
            RequestChangeMobile requestChangeMobile = (RequestChangeMobile) request;
            UserEntity.getUser().setAreaCode(getActivity(), requestChangeMobile.areaCode);
            UserEntity.getUser().setPhone(getActivity(), requestChangeMobile.mobile);
            UserEntity.getUser().setLoginAreaCode(getActivity(), requestChangeMobile.areaCode);
            UserEntity.getUser().setLoginPhone(getActivity(), requestChangeMobile.mobile);
            showTip("更换手机号成功");
            finish();
//            notifyFragment(FgSetting.class, null);
//            notifyFragment(FgPersonCenter.class,null);
        } else if (request instanceof RequestVerity) {
            RequestVerity requestVerity = (RequestVerity) request;
            showTip("验证码已发送");
            time = 59;
            handler.postDelayed(runnable, 0);
        }
    }


    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (request instanceof RequestVerity) {
            setBtnVisible(true);
        }
        super.onDataRequestError(errorInfo, request);
    }

    Integer time = 59;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time > 0) {
                setBtnVisible(false);
                timeTextView.setText(String.valueOf(time--) + "秒");
                handler.postDelayed(this, 1000);
            } else {
                setBtnVisible(true);
                timeTextView.setText(String.valueOf(59) + "秒");
            }

        }
    };

    @Override
    protected void inflateContent() {
    }

    @Event({R.id.change_mobile_submit, R.id.change_mobile_areacode, R.id.change_mobile_getcode})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.change_mobile_submit:
                //更换手机号
                collapseSoftInputMethod(); //隐藏键盘
                String areaCode = areaCodeTextView.getText().toString();
                if (TextUtils.isEmpty(areaCode)) {
                    showTip("区号不能为空");
                    return;
                }
                areaCode = areaCode.substring(1);
                String phone = mobileEditText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showTip("手机号不能为空");
                    return;
                }
                String verity = verityEditText.getText().toString();
                if (TextUtils.isEmpty(verity)) {
                    showTip("验证码不能为空");
                    return;
                }
                if (TextUtils.equals(phone, UserEntity.getUser().getPhone(getActivity()))) {
                    showTip("该手机号与当前手机号不能相同");
                    return;
                }

                RequestChangeMobile changeMobile = new RequestChangeMobile(getActivity(), areaCode, phone, verity);
                requestData(changeMobile);
                break;
            case R.id.change_mobile_areacode:
                //选择区号
                collapseSoftInputMethod(); //隐藏键盘
                FgChooseCountry fg = new FgChooseCountry();
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM, "changeMobile");
                startFragment(fg, bundle);
                break;
            case R.id.change_mobile_getcode:
                //获取验证码
                collapseSoftInputMethod(); //隐藏键盘
                String areaCode1 = areaCodeTextView.getText().toString();
                if (TextUtils.isEmpty(areaCode1)) {
                    showTip("区号不能为空");
                    setBtnVisible(true);
                    return;
                }
                areaCode1 = areaCode1.substring(1);
                String phone1 = mobileEditText.getText().toString();
                if (TextUtils.isEmpty(phone1)) {
                    showTip("手机号不能为空");
                    setBtnVisible(true);
                    return;
                }
                if (TextUtils.equals(phone1, UserEntity.getUser().getPhone(getActivity()))) {
                    showTip("该手机号与当前手机号不能相同");
                    setBtnVisible(true);
                    return;
                }
                RequestVerity requestVerity = new RequestVerity(getActivity(), areaCode1, phone1, 5);
                requestData(requestVerity);
                break;
            default:
                break;
        }
    }

    /**
     * 设置按钮是否可以点击
     *
     * @param isClick
     */
    private void setBtnVisible(boolean isClick) {
        if (isClick) {
            getCodeBtn.setVisibility(View.VISIBLE);
            timeTextView.setVisibility(View.GONE);
        } else {
            getCodeBtn.setVisibility(View.GONE);
            timeTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCountry.class.getSimpleName().equals(from)) {
            String areaCode = bundle.getString(FgChooseCountry.KEY_COUNTRY_CODE);
            areaCodeTextView.setText("+" + areaCode);
        }
    }

    @Override
    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        fgTitle.setText("修改手机号");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        StringBuilder sb = new StringBuilder();
        String code = UserEntity.getUser().getAreaCode(getActivity());
        if (!TextUtils.isEmpty(code)) {
            sb.append("+" + code);
        }
        String phone = UserEntity.getUser().getPhone(getActivity());
        if (!TextUtils.isEmpty(phone)) {
            sb.append(phone);
        }
        phoneTextView.setText("当前手机号：" + sb.toString());
        return null;
    }

}
