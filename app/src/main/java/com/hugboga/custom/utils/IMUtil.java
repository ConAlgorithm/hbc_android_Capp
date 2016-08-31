package com.hugboga.custom.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestNIMResetIMToken;
import com.hugboga.custom.data.request.RequestResetIMToken;
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

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by admin on 2016/3/10.
 */
public class IMUtil {

    private Context context;

    private static IMUtil instance;

    private static int requestIMTokenCount = 0;
    private String userId;
    private ReconnectHandler handler;
    private OnImSuccessListener listener;

    private IMUtil() {
        this.context = MyApplication.getAppContext();
    }

    public static IMUtil getInstance() {
        if (instance == null) {
            instance = new IMUtil();
        }
        requestIMTokenCount = 0;
        return instance;
    }

    public void connect() {
//        if(MyApplication.imType==MyApplication.IMTYPE_RONGIM){
//            connect(UserEntity.getUser().getImToken(context));
//        }else{
            connectNim(UserEntity.getUser().getNimUserId(context),UserEntity.getUser().getNimUserToken(context));
//        }

    }

    public void connect(String imToken) {
        if (!UserEntity.getUser().isLogin(context)) {
            RongIM.getInstance().disconnect();
            return;
        }
        if (TextUtils.isEmpty(imToken)) {
            MLog.i("hbc_im", "IMUtil connect 为空 重试");
            requestIMTokenUpdate();
            return;
        }
        RongIM.connect(imToken, new RongIMClient.ConnectCallback() {

            @Override
            public void onTokenIncorrect() {
                requestIMTokenUpdate();
                ApiFeedbackUtils.requestIMFeedback(1, null, "Token已过期，重新获取Token");
                MLog.i("hbc_im", "IMUtil_connect_onTokenIncorrect：Token已过期，重新获取Token");
            }

            @Override
            public void onSuccess(String _userId) {
                userId = _userId;
                initRongIm(context, userId);
                MLog.i("hbc_im", "IMUtil_connect_onSuccess：连接融云 userId:" + userId);
                if (listener != null) {
                    listener.onSuccess();
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                connectError();
                ApiFeedbackUtils.requestIMFeedback(2, errorCode != null ? "" + errorCode.getValue() : null);
                MLog.i("hbc_im", "IMUtil_connect_onError：连接融云 errorCode:" + errorCode);
            }

            @Override
            public void onFail(int errorCode) {
                super.onFail(errorCode);
                connectError();
                ApiFeedbackUtils.requestIMFeedback(3, "" + errorCode);
                MLog.i("hbc_im", "IMUtil_connect_onFail：连接融云 errorCode:" + errorCode);
            }
        });
        RongIM.getInstance().setConnectionStatusListener(new MyConnectionStatusListener());
    }

    private class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {

        @Override
        public void onChanged(ConnectionStatus connectionStatus) {

            switch (connectionStatus){

                case CONNECTED://连接成功。
                    MLog.i("hbc_im", "IMUtil_onChanged：连接成功");
                    break;
                case DISCONNECTED://断开连接。
                    ApiFeedbackUtils.requestIMFeedback(5, null);
                    MLog.i("hbc_im", "IMUtil_onChanged：断开连接");
                    break;
                case CONNECTING://连接中。
                    MLog.i("hbc_im", "IMUtil_onChanged：连接中");
                    break;
                case NETWORK_UNAVAILABLE://网络不可用。
                    MLog.i("hbc_im", "IMUtil_onChanged：网络不可用");
                    ApiFeedbackUtils.requestIMFeedback(6, null);
                    break;
                case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                    MLog.i("hbc_im", "IMUtil_onChanged：用户账户在其他设备登录，本机会被踢掉线");
                    ApiFeedbackUtils.requestIMFeedback(7, null);
                    break;
            }
        }
    }

    public interface OnImSuccessListener {
        public void onSuccess();
    }

    public void setOnImSuccessListener(OnImSuccessListener listener) {
        this.listener = listener;
    }

    private class ReconnectHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                RequestResetIMToken requestResetToken = new RequestResetIMToken(context);
                HttpRequestUtils.request(context, requestResetToken, httpRequestListener);
            } else if (msg.what == 2) {
                connect();
            }
        }
    }

    /**
     * update token
     */
    private void requestIMTokenUpdate() {
        if (handler == null) {
            handler = new ReconnectHandler();
        }
        if (requestIMTokenCount < 3) {
            requestIMTokenCount++;
            handler.sendEmptyMessageDelayed(1, 5000);
        }
    }

    private void connectError() {
        if (handler == null) {
            handler = new ReconnectHandler();
        }
        if (requestIMTokenCount < 3) {
            requestIMTokenCount++;
            handler.sendEmptyMessageDelayed(2, 5000);
        }
    }

    public void reconnect() {
        if (RongIM.getInstance() != null && RongIMClient.getInstance() != null) {
            if (RongIM.getInstance().getCurrentConnectionStatus() != RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED
                    && RongIM.getInstance().getCurrentConnectionStatus() != RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING) {
                RongIMClient.getInstance().reconnect(new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {
                        ApiFeedbackUtils.requestIMFeedback(1, null, "Token已过期，重新获取Token");
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(userId)) {
                            initRongIm(context, userId);
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        ApiFeedbackUtils.requestIMFeedback(2, errorCode != null ? "" + errorCode.getValue() : null);
                    }
                });
            }
        }
    }

    public void logoutNim(){
        StatusCode status = NIMClient.getStatus();
        if(status == StatusCode.LOGINED){
            NIMClient.getService(AuthService.class).logout();
        }
    }

    HttpRequestListener httpRequestListener = new HttpRequestListener() {
        @Override
        public void onDataRequestSucceed(BaseRequest request) {
            Object[] object = (Object[]) request.getData();
//            if(object.length==4){
//                String rimUserId = object[2].toString();
//                String rimToken = object[3].toString();
//                UserEntity.getUser().setRimUserId(context,rimUserId);
//                UserEntity.getUser().setImToken(context,rimToken);
//                connect(rimToken);
//            }
            //connect(request.getData().toString());
           // UserEntity.getUser().setImToken(context, request.getData().toString());
        }

        @Override
        public void onDataRequestCancel(BaseRequest request) {

        }

        @Override
        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            ErrorHandler handler = new ErrorHandler((Activity) context, this);
            handler.onDataRequestError(errorInfo, request);
            ApiFeedbackUtils.requestIMFeedback(4, null);
        }
    };

    /**
     * IM扩展功能自定义
     */
    public static void initRongIm(Context context, String userId) {
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
//                new VoIPInputProvider(RongContext.getInstance()),// 语音通话
        };
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
        RongIM.getInstance().setCurrentUserInfo(getUserInfo(context));
    }

    /**
     * 获取头像UserInfo
     *
     * @return
     */
    public static UserInfo getUserInfo(Context context) {
        UserInfo userInfo = null;
        String userid = UserEntity.getUser().getUserId(context);
        String username = UserEntity.getUser().getNickname(context);
        String guideAvatarUrl = UserEntity.getUser().getAvatar(context);
      /*  if (!TextUtils.isEmpty(guideAvatarUrl) && guideAvatarUrl.startsWith(UrlLibs.SERVER_HTTP_SCHEME_HTTPS)) {
            guideAvatarUrl = guideAvatarUrl.replace(UrlLibs.SERVER_HTTP_SCHEME_HTTPS, UrlLibs.SERVER_HTTP_SCHEME_HTTP);
        }*/
        Uri uri = null;
        if(!TextUtils.isEmpty(guideAvatarUrl)){
            uri = Uri.parse(guideAvatarUrl);
        }
        userInfo = new UserInfo("Y" + userid, username, uri);
        MLog.e("guideAvatarUrl =  "+guideAvatarUrl);
        return userInfo;
    }


    private void connectNim(String account,String token){
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
        AbortableFuture<LoginInfo> loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                NimUIKit.setAccount(account);
                com.netease.nim.uikit.UserPreferences.setEarPhoneModeEnable(false);
                MessageAudioControl.getInstance(context).setEarPhoneModeEnable(false);
                // 构建缓存
                DataCacheManager.buildDataCacheAsync(MyApplication.getAppContext(), new Observer<Void>() {
                    @Override
                    public void onEvent(Void aVoid) {
                        if(listener!=null){
                            listener.onSuccess();
                        }
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
}
