package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionTravelListBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.JsonUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by qingcha on 16/9/27.
 */
public class ActionPageTravelList extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (!ActionUtils.isLogin(context, actionBean)) {
            return;
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.PARAMS_PAGE_INDEX, 3);
        context.startActivity(intent);
        if (actionBean.data != null) {
            ActionTravelListBean bean = (ActionTravelListBean) JsonUtils.fromJson(actionBean.data, ActionTravelListBean.class);
            if (bean != null) {
                EventBus.getDefault().post(new EventAction(EventType.TRAVEL_LIST_TYPE, CommonUtils.getCountInteger(bean.type) - 1));
            }
        }
    }
}
