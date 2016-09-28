package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by qingcha on 16/9/27.
 */
public class ActionPageChatList extends ActionPageBase {
    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        context.startActivity(new Intent(context, MainActivity.class));
        EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 1));
    }
}
