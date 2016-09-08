package com.hugboga.custom.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestNIMResetIMToken;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.DataCacheManager;
import com.netease.nim.uikit.session.audio.MessageAudioControl;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

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
        StatusCode status = NIMClient.getStatus();
        if(status == StatusCode.LOGINED){
            NIMClient.getService(AuthService.class).logout();
        }
    }

    private void connectNim(String account,String token){
        if (!UserEntity.getUser().isLogin(context)) {
            return;
        }

        if(TextUtils.isEmpty(account) || TextUtils.isEmpty(token)){
            requestNIMTokenUpdate();
            return;
        }
        NimUIKit.setAccount(null);
        loginNim(account,token);
    }

    private void loginNim(final String account,final String  token){
        // 登录

        AbortableFuture<LoginInfo> loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                // 构建缓存
                DataCacheManager.buildDataCacheAsync(MyApplication.getAppContext(), new Observer<Void>() {
                    @Override
                    public void onEvent(Void aVoid) {
                        NimUIKit.setAccount(account);
                        com.netease.nim.uikit.UserPreferences.setEarPhoneModeEnable(false);
                        MessageAudioControl.getInstance(context).setEarPhoneModeEnable(false);
                        EventBus.getDefault().post(new EventAction(EventType.NIM_LOGIN_SUCCESS));
                    }
                });


                reconnectTimes = 0;
                if(nimReconnectHandler!=null){
                    nimReconnectHandler.removeCallbacksAndMessages(null);
                }
            }
            @Override
            public void onFailed(int code) {
                if(code==302 || code==404 || code == 405){
                    requestNIMTokenUpdate();
                    return;
                }
                nimConnectError();
                ApiFeedbackUtils.requestIMFeedback(10, "云信登录失败：code:" + code);
            }
            @Override
            public void onException(Throwable exception) {
                nimConnectError();
                if(exception!=null && !TextUtils.isEmpty(exception.getMessage())){
                    ApiFeedbackUtils.requestIMFeedback(11, "云信登录异常");
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
        StatusCode statusCode = NIMClient.getStatus();
        if(statusCode==StatusCode.LOGINING){
            Toast.makeText(MyApplication.getAppContext(),"正在登录聊天服务器",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(statusCode!=StatusCode.LOGINED){
            Toast.makeText(MyApplication.getAppContext(),"正在登录聊天服务器",Toast.LENGTH_SHORT).show();
            connect();
            return false;
        }
        return true;
    }
}
