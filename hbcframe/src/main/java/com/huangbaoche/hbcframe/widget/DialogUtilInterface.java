package com.huangbaoche.hbcframe.widget;

import android.app.Activity;
import android.app.Dialog;

import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.request.BaseRequest;

/**
 * Created by admin on 2016/3/24.
 */
public interface DialogUtilInterface {

    public Dialog showLoadingDialog();
    public void   dismissLoadingDialog();
    public Dialog showSettingDialog();
    public Dialog showOvertimeDialog(BaseRequest baseRequest,HttpRequestListener listener);

}
