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

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.data.bean.PushMessage;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestPushReceive;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.PushUtils;

import cn.jpush.android.api.JPushInterface;
import org.greenrobot.eventbus.EventBus;

/**
 * Push
 */
public class PushReceiver extends BroadcastReceiver implements HttpRequestListener {

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
            PushUtils.uploadPushRegister(pushId);
        }
        MLog.e("bundle="+bundle);
        String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String msg2 = bundle.getString(JPushInterface.EXTRA_EXTRA);
        MLog.e(", msgId="+msgId+", msg1="+message+" msg2="+msg2);

        if(msg2==null || msg2.isEmpty()){
            return;
        }

        PushMessage pushMessage = (PushMessage) JsonUtils.fromJson(msg2, PushMessage.class);
        pushMessage.messageID = msgId;
        pushMessage.title = title;
        pushMessage.message = message;
        /*
        部分工作在此处进行过滤
		1. 是否使用当前版本
		2. 是否超过有效期
		3. 是否需要验证当前账号
		 */
        PushUtils.showNotification(pushMessage);
        uploadPushReceive(context,msgId);
        if("IM".equals(pushMessage.type)){
            notifyChatList();
        }
    }

    private void notifyChatList() {
        EventBus.getDefault().post(new EventAction(EventType.REFRESH_CHAT_LIST));
    }

    private void uploadPushReceive(Context context,String pushId){
        RequestPushReceive request = new RequestPushReceive(context,pushId);
        HttpRequestUtils.request(context,request,this);

    }
    /**
     * 交给Main进行事件处理
     * 只有在Main处于打开状态，才进行弹出提示，才能接收到广播信息
     * @param context
     * @param pushMessage
     */
    private void gotoMain(Context context, PushMessage pushMessage) {
        try {
            Intent intent1 = new Intent(MainActivity.FILTER_PUSH_DO);
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable(MainActivity.PUSH_BUNDLE_MSG, pushMessage);
            intent1.putExtras(bundle1);
            context.sendBroadcast(intent1);
        }catch (Exception e){}
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        ApiReportHelper.getInstance().addReport(request);
        MLog.e("pushReceiver = "+request.getData());
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        MLog.e("pushReceiver ="+errorInfo);
    }
}
