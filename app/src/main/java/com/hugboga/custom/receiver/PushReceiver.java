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

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.data.bean.PushMessage;
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
        MLog.e("bundle="+bundle);
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String msg1 = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String msg2 = bundle.getString(JPushInterface.EXTRA_EXTRA);
        MLog.e("msg1="+msg1+" msg2="+msg2);
        if(msg2==null || msg2.isEmpty()){
            return;
        }
        //格式化返回的json对象
        PushMessage pushMessage = new PushMessage();
        pushMessage.parser(msg2);
        pushMessage.title = title;
        pushMessage.content = msg1;
        /*
        部分工作在此处进行过滤
		1. 是否使用当前版本
		2. 是否超过有效期
		3. 是否需要验证当前账号
		 */
        PushUtils pushUtils = new PushUtils();
//        gotoMain(context, pushMessage);
        PushUtils.showNotification(context,pushMessage);
       /* if (!TextUtils.isEmpty(pushMessage.type) && pushUtils.isVersion(context, pushMessage.version) && pushUtils.isVaild(pushMessage.vaild) && pushUtils.isAccountId(pushMessage.accountID)) {
            gotoMain(context, pushMessage);
            *//*
            是否需要通知栏提醒，在此进行通知栏显示
             *//*
            if (!pushMessage.notification.isEmpty() && pushMessage.notification.equals("true")) {
                PushUtils.showNotification(context,pushMessage);
            }
        }*/

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

}
