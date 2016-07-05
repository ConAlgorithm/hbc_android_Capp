package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.BindMobileBean;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestChangeMobile;
import com.hugboga.custom.data.request.RequestBindMobile;
import com.hugboga.custom.data.request.RequestVerity;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

import org.greenrobot.eventbus.EventBus;

@ContentView(R.layout.fg_bind_mobile)
public class FgBindMobile extends BaseFragment {

    @ViewInject(R.id.bind_mobile_areacode)
    TextView areaCodeTextView;
    @ViewInject(R.id.bind_mobile_mobile)
    EditText mobileEditText;
    @ViewInject(R.id.bind_mobile_verity)
    EditText verityEditText;
    @ViewInject(R.id.bind_mobile_getcode)
    TextView getCodeBtn;
    @ViewInject(R.id.bind_mobile_time)
    TextView timeTextView;

    private String areaCode = "";
    private String mobile = "";
    private String unionid = "";
    private boolean isAfterProcess = false;

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestVerity) {
            RequestVerity requestVerity = (RequestVerity) request;
            showTip("验证码已发送");
            time = 59;
            handler.postDelayed(runnable, 0);
        } else if(request instanceof RequestBindMobile){
            RequestBindMobile checkMobile = (RequestBindMobile) request;
            UserBean userBean = checkMobile.getData();
            if(userBean.isNotRegister == 1){//未注册，跳转设置密码
                FgSetPassword fgSetPassword = new FgSetPassword();
                Bundle bundle = new Bundle();
                bundle.putString("areaCode",areaCode);
                bundle.putString("mobile",mobile);
                bundle.putString("unionid", unionid);

                if(userBean != null){
                    bundle.putSerializable("userBean",userBean);
                    UserEntity.getUser().setNickname(getActivity(), userBean.nickname);
                    UserEntity.getUser().setAvatar(getActivity(), userBean.avatar);
                    userBean.setUserEntity(getActivity());
                    UserSession.getUser().setUserToken(getActivity(), userBean.userToken);
                    connectIM();
                    EventBus.getDefault().post(
                            new EventAction(EventType.CLICK_USER_LOGIN));
                }
                startFragment(fgSetPassword, bundle);

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("source", source);
                MobclickAgent.onEvent(getActivity(), "bind_succeed", map);

            }else { //注册且登录成功
                userBean.setUserEntity(getActivity());
                UserSession.getUser().setUserToken(getActivity(), userBean.userToken);
                connectIM();
                EventBus.getDefault().post(
                        new EventAction(EventType.CLICK_USER_LOGIN));
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FRAGMENT_NAME, FgBindMobile.class.getSimpleName());
                finishForResult(bundle);

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("source", source);
                MobclickAgent.onEvent(getActivity(), "bind_succeed", map);
            }
        } else if(request instanceof RequestChangeMobile){
            //isAfterProcess 绑定手机号
            RequestChangeMobile requestChangeMobile = (RequestChangeMobile) request;
            FgSetPassword fgSetPassword = new FgSetPassword();
            Bundle bundle = new Bundle();
            bundle.putString("areaCode",areaCode);
            bundle.putString("mobile",mobile);
            bundle.putBoolean("isAfterProcess",isAfterProcess);
//            if(!TextUtils.isEmpty(unionid)){
//                bundle.putString("unionid",unionid);
//            }else{
//                bundle.putString("unionid",UserEntity.getUser().getUnionid(getActivity()));
//            }
            startFragment(fgSetPassword, bundle);

            HashMap<String,String> map = new HashMap<String,String>();
            map.put("source", source);
            MobclickAgent.onEvent(getActivity(), "bind_succeed", map);
        }
    }

    private void connectIM() {
        new IMUtil(getActivity()).conn(UserEntity.getUser().getImToken(getActivity()));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_right_txt:
                if(!TextUtils.isEmpty(unionid)){
                    UserEntity.getUser().setUnionid(getActivity(), unionid);
                    RequestBindMobile request = new RequestBindMobile(getActivity(),null,null,null,unionid,"1");
                    requestData(request);
                }
                break;
            case R.id.header_left_btn:
                super.onClick(v);
                break;
        }
    }

    @Event({R.id.bind_mobile_submit, R.id.bind_mobile_areacode, R.id.bind_mobile_getcode})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.bind_mobile_submit:
                //更换手机号
                collapseSoftInputMethod(); //隐藏键盘
                areaCode = areaCodeTextView.getText().toString();
                if (TextUtils.isEmpty(areaCode)) {
                    showTip("区号不能为空");
                    return;
                }
                areaCode = areaCode.substring(1);
                mobile = mobileEditText.getText().toString();
                if (TextUtils.isEmpty(mobile)) {
                    showTip("手机号不能为空");
                    return;
                }
                String verity = verityEditText.getText().toString();
                if (TextUtils.isEmpty(verity)) {
                    showTip("验证码不能为空");
                    return;
                }
//                if (TextUtils.equals(phone, UserEntity.getUser().getPhone(getActivity()))) {
//                    showTip("该手机号与当前手机号不能相同");
//                    return;
//                }
                if(isAfterProcess){
                    //绑定手机号
                    RequestChangeMobile requestChangeMobile = new RequestChangeMobile(getActivity(),areaCode,mobile,verity);
                    requestData(requestChangeMobile);
                }else{
                    RequestBindMobile request = new RequestBindMobile(getActivity(),areaCode,mobile,verity,unionid,"0");
                    requestData(request);
                }
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("source", source);
                MobclickAgent.onEvent(getActivity(), "bind", map);
                break;
            case R.id.bind_mobile_areacode:
                //选择区号
                collapseSoftInputMethod(); //隐藏键盘
                FgChooseCountry fg = new FgChooseCountry();
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM, "changeMobile");
                startFragment(fg, bundle);
                break;
            case R.id.bind_mobile_getcode:
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
//                if (TextUtils.equals(phone1, UserEntity.getUser().getPhone(getActivity()))) {
//                    showTip("该手机号与当前手机号不能相同");
//                    setBtnVisible(true);
//                    return;
//                }
                if(isAfterProcess){
                    RequestVerity requestVerity = new RequestVerity(getActivity(), areaCode1, phone1, 5);
                    requestData(requestVerity);
                }else {
                    RequestVerity requestVerity = new RequestVerity(getActivity(), areaCode1, phone1, 4);
                    requestData(requestVerity);
                }
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
        }else if(FgSetPassword.class.getSimpleName().equals(from)){
            Object object = bundle.getSerializable("userBean");
            UserBean userBean = null;
            if(object != null && object instanceof UserBean){
                userBean = (UserBean) object;
            }

            Bundle bundle1 = new Bundle();
            bundle1.putString(KEY_FRAGMENT_NAME, FgBindMobile.class.getSimpleName());
            if(userBean != null){
                bundle1.putSerializable("userBean",userBean);
            }
            finishForResult(bundle1);
        }
    }

    @Override
    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        fgTitle.setText("绑定手机号");
        if(getArguments()!=null){
            unionid = getArguments().getString("unionid");
            source = getArguments().getString("source");
            isAfterProcess = getArguments().getBoolean("isAfterProcess");
        }
    }

    @Override
    protected void initView() {
        fgRightBtn.setText("跳过");
        if(isAfterProcess){
            fgRightBtn.setVisibility(View.INVISIBLE);
        }else{
            fgRightBtn.setVisibility(View.VISIBLE);
            fgLeftBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onBackPressed() {
        if(!isAfterProcess){
            return true;
        }
        return super.onBackPressed();
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
//        phoneTextView.setText("当前手机号：" + sb.toString());
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("source", source);
        MobclickAgent.onEvent(getActivity(), "bind_launch", map);
    }
}
