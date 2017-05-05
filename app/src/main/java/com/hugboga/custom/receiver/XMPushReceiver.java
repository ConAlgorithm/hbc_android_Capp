package com.hugboga.custom.receiver;

import android.content.Context;
import android.text.TextUtils;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.PushMessage;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestPushReceive;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.PushUtils;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.greenrobot.eventbus.EventBus;

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
public class XMPushReceiver extends PushMessageReceiver implements HttpRequestListener {
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {

        if (context == null || TextUtils.isEmpty(miPushMessage.getContent())) {
            return;
        }

        PushMessage pushMessage = (PushMessage) JsonUtils.fromJson(miPushMessage.getContent(), PushMessage.class);
        pushMessage.messageID = miPushMessage.getMessageId();
        pushMessage.title = miPushMessage.getTitle();
        if (!TextUtils.isEmpty(miPushMessage.getDescription())) {
            pushMessage.message = miPushMessage.getDescription();
        }
        if (pushMessage == null) {
            return;
        }
        PushUtils.showNotification(pushMessage);
        RequestPushReceive request = new RequestPushReceive(context, miPushMessage.getMessageId());
        HttpRequestUtils.request(context, request, this);
        if ("IM".equals(pushMessage.type)) {
            EventBus.getDefault().post(new EventAction(EventType.REFRESH_CHAT_LIST));
        }
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
            PushUtils.uploadMiPushRegister(regId);
        } else if ("set-alias".equals(miPushCommandMessage.getCommand())) {
            String alias = "";
            if (miPushCommandMessage.getCommandArguments() != null
                    && miPushCommandMessage.getCommandArguments().size() > 0) {
                alias = miPushCommandMessage.getCommandArguments().get(0);
            }
            PushUtils.uploadMiPushAlias((int) miPushCommandMessage.getResultCode(), alias);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        ApiReportHelper.getInstance().addReport(request);
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

    }
}
