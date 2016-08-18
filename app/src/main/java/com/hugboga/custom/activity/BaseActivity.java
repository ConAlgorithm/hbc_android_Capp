package com.hugboga.custom.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.activity.BaseFragmentActivity;
import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;


public class BaseActivity extends BaseFragmentActivity implements HttpRequestListener {

    public static String KEY_FROM = "key_from";
    public static String KEY_BUSINESS_TYPE = "key_business_Type";
    public static String KEY_GOODS_TYPE = "key_goods_type";

    public Activity activity;

    protected String source = ""; //友盟统计用 获取从哪个界面进入
    protected String umeng_key = "";//友盟事件ID

    public Callback.Cancelable cancelable;
    private ErrorHandler errorHandler;

    protected TextView fgTitle; //标题
    protected TextView fgRightBtn; //右按钮
    protected View fgLeftBtn;//左按钮

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        activity = this;
        MobClickUtils.OnEvent(this, getEventId(), getEventMap());
    }

    protected void initDefaultTitleBar() {
        fgTitle = (TextView) findViewById(R.id.header_title);
        fgLeftBtn = findViewById(R.id.header_left_btn);
        fgRightBtn = (TextView) findViewById(R.id.header_right_txt);
        if (fgRightBtn != null) {
            fgRightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                }
            });
        }
        if (fgLeftBtn != null) {
            fgLeftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
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
        if (cancelable != null) {
            cancelable.cancel();
        }
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

    protected Callback.Cancelable requestData(BaseRequest request) {
        cancelable = HttpRequestUtils.request(this, request, this);
        return cancelable;
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {

    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (errorHandler == null) {
            errorHandler = new ErrorHandler(this, this);
        }
        errorHandler.onDataRequestError(errorInfo, request);
        errorHandler = null;//TODO 旧代码粘贴，没必要赋空，耗内存，需优化。

    }

    /**
     * 获取点击事件ID
     */
    public String getEventId(){
        return "";
    }

    /**
     * 获取来源
     */
    public String getEventSource(){
        return "";
    }

    /**
     * 获取来源map
     */
    public Map getEventMap(){
        ArrayMap map = new ArrayMap();
        map.put("source", getEventSource());
        return map;
    }
}
