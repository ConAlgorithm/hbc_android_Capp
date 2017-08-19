package com.hugboga.custom.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.NetWork;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestNIMResetIMToken;
import com.hugboga.im.ImHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by admin on 2016/3/10.
 */
public class IMUtil {

    private Context context;

    private static IMUtil instance;


    private IMUtil() {
        this.context = MyApplication.getAppContext();
    }

    public static IMUtil getInstance() {
        if (instance == null) {
            instance = new IMUtil();
        }
        return instance;
    }

    public void connect() {
        connectNim(UserEntity.getUser().getNimUserId(context),UserEntity.getUser().getNimUserToken(context));
    }


    public void logoutNim(){
        ImHelper.setUserId("");
        ImHelper.logoutNim();
    }

    private void connectNim(String account,String token){

        ImHelper.setUserId(UserEntity.getUser().getUserId(MyApplication.getAppContext()));//与云信业务无关，统计用的id
        if (!UserEntity.getUser().isLogin(context)) {
            return;
        }

        if(TextUtils.isEmpty(account) || TextUtils.isEmpty(token)){
            requestNIMTokenUpdate();
            return;
        }
        loginNim(account,token);
    }

    private void loginNim(final String account,final String  token){
        // 登录
        ImHelper.loginNim(MyApplication.getAppContext(), account, token, new ImHelper.IMLoginCallback() {
            @Override
            public void onSuccess() {
                reconnectTimes = 0;
                if(nimReconnectHandler!=null){
                    nimReconnectHandler.removeCallbacksAndMessages(null);
                }
                EventBus.getDefault().post(new EventAction(EventType.NIM_LOGIN_SUCCESS));
            }

            @Override
            public void onFailed(int code) {
                if(code==302 || code==404 || code == 405){
                    requestNIMTokenUpdate();
                    return;
                }
                nimConnectError();
                //ApiFeedbackUtils.requestIMFeedback(1, String.valueOf(code));
            }

            @Override
            public void onException(Throwable exception) {
                nimConnectError();
                if(exception!=null && !TextUtils.isEmpty(exception.getMessage())){
                    //ApiFeedbackUtils.requestIMFeedback(1, "0","云信登录异常");
                }
            }
        });
    }

    /**
     * update token
     */
    private void requestNIMTokenUpdate() {
        if (nimReconnectHandler == null) {
            nimReconnectHandler = new NIMReconnectHandler();
        }
        if (reconnectTimes < 3) {
            reconnectTimes++;
            nimReconnectHandler.sendEmptyMessageDelayed(1, 5000);
        }
    }


    private void nimConnectError(){
        if(nimReconnectHandler==null){
            nimReconnectHandler = new NIMReconnectHandler();
        }
        if (reconnectTimes < 3) {
            reconnectTimes++;
            nimReconnectHandler.sendEmptyMessageDelayed(2, 5000);
        }
    }

    private static int reconnectTimes = 0;
    private NIMReconnectHandler nimReconnectHandler;

    private class NIMReconnectHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                RequestNIMResetIMToken requestResetToken = new RequestNIMResetIMToken(context);
                HttpRequestUtils.request(context, requestResetToken, new HttpRequestListener() {
                    @Override
                    public void onDataRequestSucceed(BaseRequest request) {
                        Object[] object = (Object[]) request.getData();
                        if(object.length==2){
                            String nimUserId = object[0].toString();
                            String nimToken = object[1].toString();
                            UserEntity.getUser().setNimUserId(context,nimUserId);
                            UserEntity.getUser().setNimUserToken(context,nimToken);
                            connectNim(nimUserId,nimToken);
                        }
                        reconnectTimes = 0;
                    }

                    @Override
                    public void onDataRequestCancel(BaseRequest request) {
                    }

                    @Override
                    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                    }
                });
            } else if (msg.what == 2) {
                connect();
            }
        }
    }

    /**
     * 判断云信是否登录
     * @return
     */
    public boolean isLogined(){
        if(!NetWork.isNetworkAvailable(MyApplication.getAppContext())){
            CommonUtils.showToast(R.string.net_broken);
            return false;
        }
        StatusCode statusCode = NIMClient.getStatus();
        if(statusCode==StatusCode.LOGINING){
            CommonUtils.showToast("正在登录聊天服务器");
            return false;
        }
        if(statusCode!=StatusCode.LOGINED){
            CommonUtils.showToast("正在登录聊天服务器");
            connect();
            return false;
        }
        return true;
    }
}
