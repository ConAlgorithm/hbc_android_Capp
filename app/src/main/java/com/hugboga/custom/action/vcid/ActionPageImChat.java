package com.hugboga.custom.action.vcid;

import android.content.Context;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionImChatBean;
import com.hugboga.custom.activity.NIMChatActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.UnicornUtils;

/**
 * Created by qingcha on 17/2/9.
 */
public class ActionPageImChat extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (!ActionUtils.isLogin(context, actionBean) || actionBean.data == null) {
            return;
        }
        ActionImChatBean bean = (ActionImChatBean) JsonUtils.fromJson(actionBean.data, ActionImChatBean.class);
        if (bean == null) {
            return;
        }
        int type = CommonUtils.getCountInteger(bean.type);
        if (type == 3) {
            UnicornUtils.openServiceActivity(getContext(), UnicornServiceActivity.SourceType.TYPE_CHAT_LIST, actionBean.source);
        } else if (type == 1) {
            NIMChatActivity.start(getContext(), bean.guideId, bean.tid, actionBean.source);
        }
    }

}
