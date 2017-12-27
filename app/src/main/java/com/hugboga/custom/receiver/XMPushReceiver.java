package com.hugboga.custom.receiver;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hugboga.custom.data.bean.PushMessage;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.PushUtils;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

/**
 * 1、PushMessageReceiver 是个抽象类，该类继承了 BroadcastReceiver。<br/>
 * 2、需要将自定义的 DemoMessageReceiver 注册在 AndroidManifest.xml 文件中：
 * <pre>
 * {@code
 *  <receiver
 *      android:name="com.xiaomi.mipushdemo.DemoMessageReceiver"
 *      android:exported="true">
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.ERROR" />
 *      </intent-filter>
 *  </receiver>
 *  }</pre>
 * 3、DemoMessageReceiver 的 onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。<br/>
 * 4、DemoMessageReceiver 的 onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，
 *    这个回调方法会在用户手动点击通知后触发。<br/>
 * 5、DemoMessageReceiver 的 onNotificationMessageArrived 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。<br/>
 * 6、DemoMessageReceiver 的 onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。<br/>
 * 7、DemoMessageReceiver 的 onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。<br/>
 * 8、以上这些方法运行在非 UI 线程中。
 *
 */
public class XMPushReceiver extends PushMessageReceiver {
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {

        if (context == null || TextUtils.isEmpty(miPushMessage.getContent())) {
            return;
        }

        PushMessage pushMessage = (PushMessage) JsonUtils.fromJson(miPushMessage.getContent(), PushMessage.class);
        pushMessage.messageID = miPushMessage.getMessageId();
        if (TextUtils.isEmpty(pushMessage.title)) {
            pushMessage.title = miPushMessage.getTitle();
        }
        if (TextUtils.isEmpty(pushMessage.message)) {
            pushMessage.message = miPushMessage.getDescription();
        }
        if (pushMessage == null) {
            return;
        }
        PushUtils.onPushReceive(pushMessage);
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
        if ("register".equals(miPushCommandMessage.getCommand())) {
            String regId = "";
            if (miPushCommandMessage.getCommandArguments() != null
                    && miPushCommandMessage.getCommandArguments().size() > 0) {
                regId = miPushCommandMessage.getCommandArguments().get(0);
            }
            PushUtils.pushRegister(2, regId);
            Log.i(PushUtils.TAG,"XMPushReceiver onCommandResult() regId = " + regId);
        } else if ("set-alias".equals(miPushCommandMessage.getCommand())) {
            String alias = "";
            if (miPushCommandMessage.getCommandArguments() != null
                    && miPushCommandMessage.getCommandArguments().size() > 0) {
                alias = miPushCommandMessage.getCommandArguments().get(0);
            }
        }
    }
}