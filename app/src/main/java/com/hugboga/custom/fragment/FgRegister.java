package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.huangbaoche.hbcframe.data.net.ExceptionErrorCode;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.ServerException;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.constants.ResourcesConstants;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestLogin;
import com.hugboga.custom.data.request.RequestRegister;
import com.hugboga.custom.data.request.RequestVerity;
import com.hugboga.custom.widget.DialogUtil;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import java.util.regex.Pattern;

@ContentView(R.layout.fg_register)
public class FgRegister extends BaseFragment {

    @ViewInject(R.id.register_areacode)
    private TextView areaCodeTextView;
    @ViewInject(R.id.register_phone)
    EditText phoneEditText;
    @ViewInject(R.id.register_verity)
    EditText verityEditText;
    @ViewInject(R.id.register_password)
    EditText passwordEditText;
    @ViewInject(R.id.register_getcode)
    TextView getCodeBtn; //发送验证码按钮
    @ViewInject(R.id.register_time)
    TextView timeTextView; //验证码倒计时

    String areaCode;
    String phone;
    String password;

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestRegister) {
            RequestRegister register = (RequestRegister) request;
            UserBean userBean = register.getData();
            if (userBean != null) {
                //注册成功，进行登录操作
                //登录成功
                UserEntity.getUser().setUserId(getActivity(), userBean.userID);
                UserEntity.getUser().setUserToken(getActivity(), userBean.userToken);
                UserEntity.getUser().setPhone(getActivity(), phone); //手机号已经不再返回
                UserEntity.getUser().setCode(getActivity(), areaCode);
                UserEntity.getUser().setNickname(getActivity(), userBean.nickname);
                UserEntity.getUser().setAvatar(getActivity(), userBean.avatar);
                UserEntity.getUser().setOrderPoint(getActivity(), 0); //清空IM未读的小红点
                showTip("注册成功");
                Bundle bundle = new Bundle();
                bundle.putBoolean("isLogin", true);
//                notifyFragment(FgPersonCenter.class,bundle);
                finish();
            }
        } else if (request instanceof RequestVerity) {
            RequestVerity parserVerity = (RequestVerity) request;
            showTip("验证码已发送");
            setBtnVisible(false);
            time = 59;
            handler.postDelayed(runnable, 0);
        }else if (request instanceof RequestLogin) {
            RequestLogin requestLogin = (RequestLogin) request;
            UserBean userBean = requestLogin.getData();
            if(userBean!=null){
                //登录成功
                UserEntity.getUser().setUserId(getActivity(), userBean.userID);
                UserEntity.getUser().setUserToken(getActivity(), userBean.userToken);
                UserEntity.getUser().setPhone(getActivity(), phone); //手机号已经不再返回
                UserEntity.getUser().setCode(getActivity(), areaCode);
                UserEntity.getUser().setNickname(getActivity(), userBean.nickname);
                UserEntity.getUser().setAvatar(getActivity(), userBean.avatar);
                UserEntity.getUser().setOrderPoint(getActivity(), 0); //清空IM未读的小红点
                showTip("登录成功");
                Bundle bundle = new Bundle();
                bundle.putBoolean("isLogin", true);
//                notifyFragment(FgPersonCenter.class,bundle);
                finish();
            }
        }
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
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            setBtnVisible(true);
            if (errorInfo.state == ExceptionErrorCode.ERROR_CODE_SERVER) {
                if (errorInfo.exception instanceof ServerException) {
                    ServerException se = (ServerException) errorInfo.exception;
                    if (se.getCode() == 40070010 || se.getCode() == 10014) {
                        //区号手机号，已经被注册
                        DialogUtil.getInstance(getActivity()).showCustomDialog("提醒", "此手机号已经注册，是否直接登录？", "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }, "登录", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Bundle bundle = new Bundle();
                                bundle.putString("areaCode", areaCode);
                                bundle.putString("phone", phone);
                                startFragment(new FgLogin(), bundle);
                            }
                        }).show();
                        return;
                        }
                    }
                }
            super.onDataRequestError(errorInfo, request);
    }

    @Override
    protected void inflateContent() {
    }

    @Event({R.id.register_submit, R.id.register_login, R.id.register_areacode, R.id.register_getcode, R.id.register_protocol})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.register_submit:
                //注册提交
                collapseSoftInputMethod(); //隐藏键盘
                areaCode = areaCodeTextView.getText().toString();
                if (TextUtils.isEmpty(areaCode)) {
                    showTip("区号不能为空");
                    return;
                }
                areaCode = areaCode.substring(1);
                phone = phoneEditText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showTip("手机号不能为空");
                    return;
                }
                String verity = verityEditText.getText().toString();
                if (TextUtils.isEmpty(verity)) {
                    showTip("验证码不能为空");
                    return;
                }
                password = passwordEditText.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    showTip("密码不能为空");
                    return;
                }
                if (!Pattern.matches("[\\w]{4,16}", password)) {
                    showTip("密码必须是4-16位数字或字母");
                    return;
                }
//                String channelStr = AppInfo.getVersionChannel(getActivity());
                Integer channelInt= 1000;
                try {
//                    channelInt = Integer.valueOf(channelStr);
                }catch (Exception e){
                    MLog.e("getVersionChannel ", e);
                }
                RequestRegister requestRegister = new RequestRegister(getActivity(), areaCode, phone, password, verity,null,channelInt);
                requestData(requestRegister);
                break;
            case R.id.register_login:
                //跳转到登录
                finish();
                startFragment(new FgLogin());
                break;
            case R.id.register_areacode:
                //选择地区
                collapseSoftInputMethod(); //隐藏键盘
                FgChooseCountry fg = new FgChooseCountry();
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM, "register");
                startFragment(fg, bundle);
                break;
            case R.id.register_getcode:
                //获取验证码
                getCodeBtn.setClickable(false);
                collapseSoftInputMethod(); //隐藏键盘
                areaCode = areaCodeTextView.getText().toString();
                if (TextUtils.isEmpty(areaCode)) {
                    showTip("区号不能为空");
                    setBtnVisible(true);
                    return;
                }
                areaCode = areaCode.substring(1);
                phone = phoneEditText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showTip("手机号不能为空");
                    setBtnVisible(true);
                    return;
                }
                RequestVerity requestVerity = new RequestVerity(getActivity(), areaCode, phone, 1);
                requestData(requestVerity);
                break;
            case R.id.register_protocol:
                FgWebInfo fgWebInfo = new FgWebInfo();
                Bundle bundle1 = new Bundle();
                bundle1.putString(FgWebInfo.Web_URL, ResourcesConstants.H5_PROTOCOL);
                fgWebInfo.setArguments(bundle1);
                startFragment(fgWebInfo);
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
            getCodeBtn.setClickable(true);
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
//        leftBtn.setImageResource(R.mipmap.top_close);
        fgTitle.setTextColor(getResources().getColor(R.color.my_content_title_color));
        fgTitle.setText("注册");
        //初始化数据
        if(mTargetFragment instanceof FgLogin){
            String code = getArguments().getString("areaCode");
            if(code!=null && !code.isEmpty()){
                areaCodeTextView.setText("+" + code);
            }
            String phone = getArguments().getString("phone");
            if(phone!=null && !phone.isEmpty()){
                phoneEditText.setText(phone);
            }
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

}
