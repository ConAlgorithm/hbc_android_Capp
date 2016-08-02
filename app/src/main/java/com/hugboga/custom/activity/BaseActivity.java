package com.hugboga.custom.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.activity.BaseFragmentActivity;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;


public class BaseActivity extends BaseFragmentActivity implements View.OnClickListener {

    public static String KEY_FROM = "key_from";
    public static String KEY_BUSINESS_TYPE = "key_business_Type";
    public static String KEY_GOODS_TYPE = "key_goods_type";

    public Activity activity;
    protected TextView fgTitle; //标题
    protected TextView fgRightBtn; //右按钮
    protected View fgLeftBtn;//左按钮

    protected String source = ""; //友盟统计用 获取从哪个界面进入
    protected String umeng_key = "";//友盟事件ID
    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        activity = this;
        ButterKnife.bind(this);
        initView();
    }

    public void initView(){
        fgTitle = (TextView) findViewById(R.id.header_title);
        fgLeftBtn = findViewById(R.id.header_left_btn);
        fgRightBtn = (TextView) findViewById(R.id.header_right_txt);
        if (fgRightBtn != null) fgRightBtn.setOnClickListener(this);
        if (fgLeftBtn != null) fgLeftBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn:
                MLog.e("header_left_btn");
                finish();
                break;
            case R.id.header_right_txt:
                Object o = v.getTag();
                String source = "";
                if(o != null && o instanceof String){
                    source = (String) o;
                }
                if(!TextUtils.isEmpty(source)){
                    String[] strArr = source.split(",");
                    if (strArr != null && strArr.length == 3) {
                        DialogUtil.getInstance(activity).showCallDialog(strArr[0],strArr[1],strArr[2]);
                    }
                }else{
                    DialogUtil.getInstance(activity).showCallDialog();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 展示软键盘
     */
    public void showSoftInputMethod(EditText inputText) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 收起软键盘
     */
    public void collapseSoftInputMethod(EditText inputText) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && inputText != null)
                imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
        }
    }
}
