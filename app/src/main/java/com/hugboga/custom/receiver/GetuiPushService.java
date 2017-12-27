package com.hugboga.custom.receiver;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.hugboga.custom.data.bean.PushMessage;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.PushUtils;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
/**
 * Created by zhangqiang on 17/11/21.
 */
public class GetuiPushService extends GTIntentService {

    int status;

    public GetuiPushService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.e(TAG, "onReceiveServicePid -> " + "pid = " + pid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage _msg) {
        Log.e(TAG,"onReceiveMessageData");
        byte[] payload = _msg.getPayload();
        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            Log.d(TAG, "receiver payload = " + data);
            Handler handler = new Handler(context.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what != 1 || msg.obj == null) {
                        return;
                    }
                    String message = (String) msg.obj;
                    PushMessage pushMessage = (PushMessage) JsonUtils.fromJson(message, PushMessage.class);
                    PushUtils.onPushReceive(pushMessage);
                }
            };
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = data;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        PushUtils.pushRegister(3, clientid);

    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.e(TAG, "onReceiveOnlineState -> " + "online = " + online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.e(TAG, "onReceiveCommandResult -> " + "cmdMessage = " + cmdMessage.getPkgName());
    }

}