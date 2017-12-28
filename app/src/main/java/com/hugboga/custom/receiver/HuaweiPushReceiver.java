package com.hugboga.custom.receiver;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.huawei.hms.support.api.push.PushReceiver;
import com.hugboga.custom.data.bean.PushMessage;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.PushUtils;

/**
 * Created by zhangqiang on 17/11/20.
 */
public class HuaweiPushReceiver extends PushReceiver{

    public static final String TAG = "HuaweiPushRevicer";

    public HuaweiPushReceiver() {
        super();

    }
    @Override
    public void onEvent(Context context, Event event, Bundle bundle) {
        super.onEvent(context, event, bundle);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] bytes, Bundle bundle) {
        super.onPushMsg(context, bytes, bundle);
        try {
            if (context != null) {
                Handler handler = new Handler(context.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what != 1 || msg.obj == null) {
                            return;
                        }
                        String message = (String) msg.obj;
                        Log.i(TAG, "收到PUSH透传消息,消息内容为:"  + message);
                        PushMessage pushMessage = (PushMessage) JsonUtils.fromJson(message, PushMessage.class);
                        PushUtils.onPushReceive(pushMessage);
                    }
                };
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = new String(bytes, "UTF-8");
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onPushMsg(Context context, byte[] bytes, String s) {
        super.onPushMsg(context, bytes, s);
    }

    @Override
    public void onPushState(Context context, boolean b) {
        super.onPushState(context, b);
        Log.i(TAG, "Push连接状态为:" + b);
    }

    @Override
    public void onToken(Context context, String token, Bundle bundle) {
        super.onToken(context, token, bundle);
        String belongId = bundle.getString("belongId");
        Log.i(TAG, "获取token和belongId成功，token = " + token + ",belongId = " + belongId);
        Log.i(PushUtils.TAG, "HuaweiPushReceiver onToken() token = " + token + ",belongId = " + belongId);
        PushUtils.pushRegister(1, token);
    }

    @Override
    public void onToken(Context context, String s) {
        super.onToken(context, s);
    }
}