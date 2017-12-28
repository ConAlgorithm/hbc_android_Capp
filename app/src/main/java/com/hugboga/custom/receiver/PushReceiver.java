/*
 * OrderInfoReceiver.java [V1.0.0]
 * classes:com.hugboga.custom.receiver.OrderInfoReceiver
 * ZHZEPHI Create at 2015年1月15日 下午8:10:07
 */
package com.hugboga.custom.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.bean.PushMessage;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.PushUtils;
import cn.jpush.android.api.JPushInterface;

/**
 * Push
 */
public class PushReceiver extends BroadcastReceiver {

    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null || intent == null) {
            return;
        }
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String pushId = intent.getStringExtra(JPushInterface.EXTRA_REGISTRATION_ID);
            PushUtils.pushRegister(4, pushId);
            Log.i(PushUtils.TAG,"JGPushReceiver onReceive() pushId = " + pushId);
        }
        String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String msg2 = bundle.getString(JPushInterface.EXTRA_EXTRA);
        MLog.e(", msgId="+msgId+", msg1="+message+" msg2="+msg2);

        if(msg2==null || msg2.isEmpty()){
            return;
        }

        PushMessage pushMessage = (PushMessage) JsonUtils.fromJson(msg2, PushMessage.class);
        if (TextUtils.isEmpty(pushMessage.title)) {
            pushMessage.title = title;
        }
        if (TextUtils.isEmpty(pushMessage.message)) {
            pushMessage.message = message;
        }

        PushUtils.onPushReceive(pushMessage);
    }
}