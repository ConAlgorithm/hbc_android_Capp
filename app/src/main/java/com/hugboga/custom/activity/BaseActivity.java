package com.hugboga.custom.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.huangbaoche.hbcframe.activity.BaseFragmentActivity;
import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;


public class BaseActivity extends BaseFragmentActivity implements HttpRequestListener {

    public Activity activity;

    public Callback.Cancelable cancelable;
    private ErrorHandler errorHandler;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        activity = this;
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
    protected void onDestroy() {
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
}
