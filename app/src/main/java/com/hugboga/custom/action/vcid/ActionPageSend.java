package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.IntentUtils;

/**
 * Created by qingcha on 16/9/27.
 *
 * 中文送机
 */
public class ActionPageSend extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        IntentUtils.intentSendActivity(context, actionBean.source);
    }
}