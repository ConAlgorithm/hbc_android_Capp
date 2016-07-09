package com.hugboga.custom.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestApiFeedback;
import com.hugboga.custom.data.request.RequestResetIMToken;

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

    Context context;
    private int requestIMTokenCount = 0;
    private String userId;

    public IMUtil(final Context context) {
        this.context = context;
    }

    public void conn(String imToken) {
        connect(imToken);
    }

    public void connect(String imToken) {
        if (TextUtils.isEmpty(imToken)) {
            MLog.e("IMToken 不能为空");
            return;
        }
        RongIM.connect(imToken, new RongIMClient.ConnectCallback() {

            @Override
            public void onTokenIncorrect() {
                MLog.e("-Token已过期，重新获取Token");
                if (requestIMTokenCount < 3) {
                    requestIMTokenCount++;
                    requestIMTokenUpdate();
                }
                requestApiFeedback(1, null);
            }

            @Override
            public void onSuccess(String _userId) {
                MLog.e("-连接融云 ——onSuccess— -" + userId);
                userId = _userId;
                initRongIm(context, userId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MLog.e("-连接融云 ——onError— -" + errorCode);
                if (requestIMTokenCount < 3) {
                    //您需要更换 Token
                    requestIMTokenCount++;
                    requestIMTokenUpdate();
                }
                requestApiFeedback(2, errorCode != null ? "" + errorCode.getValue() : null);
            }

            @Override
            public void onFail(int errorCode) {
                super.onFail(errorCode);
                requestApiFeedback(3, "" + errorCode);
            }
        });
        RongIM.getInstance().setConnectionStatusListener(new MyConnectionStatusListener());
    }

    private void requestApiFeedback(int errorMessage, String errorCode) {
        RequestApiFeedback requestApiFeedback = new RequestApiFeedback(context,
                UserEntity.getUser().getUserId(context),
                ApiFeedbackUtils.getImErrorFeedback(errorMessage, errorCode));
        HttpRequestUtils.request(context, requestApiFeedback, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {

            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

            }
        }, false);
    }

    /**
     * update token
     */
    private void requestIMTokenUpdate() {
        RequestResetIMToken requestResetToken = new RequestResetIMToken(context);
        HttpRequestUtils.request(context, requestResetToken, httpRequestListener);
    }

    HttpRequestListener httpRequestListener = new HttpRequestListener() {
        @Override
        public void onDataRequestSucceed(BaseRequest request) {
            connect(request.getData().toString());
        }

        @Override
        public void onDataRequestCancel(BaseRequest request) {

        }

        @Override
        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            ErrorHandler handler = new ErrorHandler((Activity) context, this);
            handler.onDataRequestError(errorInfo, request);
            requestApiFeedback(4, null);
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

    private class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {

        @Override
        public void onChanged(ConnectionStatus connectionStatus) {

            switch (connectionStatus){

                case CONNECTED://连接成功。

                    break;
                case DISCONNECTED://断开连接。
                    requestApiFeedback(5, null);
                    break;
                case CONNECTING://连接中。

                    break;
                case NETWORK_UNAVAILABLE://网络不可用。
                    requestApiFeedback(6, null);
                    break;
                case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                    requestApiFeedback(7, null);
                    break;
            }
        }
    }
}
