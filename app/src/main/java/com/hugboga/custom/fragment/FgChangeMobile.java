package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.ExceptionInfo;
import com.hugboga.custom.data.net.HttpRequestUtils;
import com.hugboga.custom.data.parser.InterfaceParser;
import com.hugboga.custom.data.parser.ParserChangeMobile;
import com.hugboga.custom.data.parser.ParserVerity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_change_mobile, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    protected String fragmentTitle() {
        //设置标题颜色，返回按钮图片
        leftBtn.setImageResource(R.mipmap.top_back_black);
        titleText.setTextColor(getResources().getColor(R.color.my_content_title_color));
        return "更换手机号";
    }

    @Override
    protected void requestDate() {
        StringBuilder sb = new StringBuilder();
        String code = UserEntity.getUser().getCode(getActivity());
        if(!code.isEmpty()){
            sb.append("+"+code);
        }
        String phone = UserEntity.getUser().getPhone(getActivity());
        if(!phone.isEmpty()){
            sb.append(phone);
        }
        phoneTextView.setText("当前手机号：" + sb.toString());
    }

    @Override
    public void onDataRequestSucceed(InterfaceParser parser) {
        if (parser instanceof ParserChangeMobile) {
            ParserChangeMobile mParser = (ParserChangeMobile) parser;
            UserEntity.getUser().setCode(getActivity(),mParser.areaCode);
            UserEntity.getUser().setPhone(getActivity(), mParser.mobile);
            showTip("更换手机号成功");
            finish();
            notifyFragment(FgSetting.class, null);
            notifyFragment(FgPersonCenter.class,null);
        }else if(parser instanceof  ParserVerity){
            ParserVerity parserVerity = (ParserVerity) parser;
            showTip("验证码已发送");
            time = 59;
            handler.postDelayed(runnable, 0);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, InterfaceParser parser) {
        if(parser instanceof ParserVerity){
            setBtnVisible(true);
        }
        super.onDataRequestError(errorInfo, parser);
    }

    Integer time = 59;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(time>0){
                setBtnVisible(false);
                timeTextView.setText(String.valueOf(time--) + "秒");
                handler.postDelayed(this,1000);
            }else{
                setBtnVisible(true);
                timeTextView.setText(String.valueOf(59) + "秒");
            }

        }
    };

    @Override
    protected void inflateContent() {
    }

    @Override
    protected void setClick(View view) {
//	view.findViewById(R.id.fg_home_btn).setOnClickListener(this);
    }

    @Override
    @OnClick({R.id.change_mobile_submit, R.id.change_mobile_areacode, R.id.change_mobile_getcode})
    protected void onClickView(View view) {
        switch (view.getId()) {
            case R.id.change_mobile_submit:
                //更换手机号
                collapseSoftInputMethod(); //隐藏键盘
                String areaCode = areaCodeTextView.getText().toString();
                if(TextUtils.isEmpty(areaCode)){
                    showTip("区号不能为空");
                    return;
                }
                areaCode = areaCode.substring(1);
                String phone = mobileEditText.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    showTip("手机号不能为空");
                    return;
                }
                String verity = verityEditText.getText().toString();
                if(TextUtils.isEmpty(verity)){
                    showTip("验证码不能为空");
                    return;
                }
                if(TextUtils.equals(phone, UserEntity.getUser().getPhone(getActivity()))){
                    showTip("该手机号与当前手机号不能相同");
                    return;
                }
                ParserChangeMobile parser = new ParserChangeMobile(areaCode, phone, verity);
                mHttpUtils = new HttpRequestUtils(getActivity(),parser,this);
                mHttpUtils.execute();
                break;
            case R.id.change_mobile_areacode:
                //选择区号
                collapseSoftInputMethod(); //隐藏键盘
                FgChooseCountry fg = new FgChooseCountry();
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM,"changeMobile");
                startFragment(fg,bundle);
                break;
            case R.id.change_mobile_getcode:
                //获取验证码
                collapseSoftInputMethod(); //隐藏键盘
                String areaCode1 = areaCodeTextView.getText().toString();
                if(TextUtils.isEmpty(areaCode1)){
                    showTip("区号不能为空");
                    setBtnVisible(true);
                    return;
                }
                areaCode1 = areaCode1.substring(1);
                String phone1 = mobileEditText.getText().toString();
                if(TextUtils.isEmpty(phone1)){
                    showTip("手机号不能为空");
                    setBtnVisible(true);
                    return;
                }
                if(TextUtils.equals(phone1, UserEntity.getUser().getPhone(getActivity()))){
                    showTip("该手机号与当前手机号不能相同");
                    setBtnVisible(true);
                    return;
                }
                ParserVerity parserVerity = new ParserVerity(areaCode1,phone1,3);
                mHttpUtils = new HttpRequestUtils(getActivity(),parserVerity,this);
                mHttpUtils.execute();
                break;
            default:
                break;
        }
    }

    /**
     * 设置按钮是否可以点击
     * @param isClick
     */
    private void setBtnVisible(boolean isClick){
        if(isClick){
            getCodeBtn.setVisibility(View.VISIBLE);
            timeTextView.setVisibility(View.GONE);
        }else{
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
        if(FgChooseCountry.class.getSimpleName().equals(from)){
            String areaCode = bundle.getString(FgChooseCountry.KEY_COUNTRY_CODE);
            areaCodeTextView.setText("+"+areaCode);
        }
    }

}
