package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionEvaluateBean;
import com.hugboga.custom.activity.EvaluateNewActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.JsonUtils;

/**
 * Created by zhangqiang on 17/6/23.
 */

public class ActionPageEvaluate extends ActionPageBase {
    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (!ActionUtils.isLogin(context, null) || actionBean.data == null) {
            return;
        }
        ActionEvaluateBean bean = (ActionEvaluateBean) JsonUtils.fromJson(actionBean.data, ActionEvaluateBean.class);
        if (bean == null) {
            return;
        }

        Intent intent = new Intent(context, EvaluateNewActivity.class);
        intent.putExtra("actionParams", bean.orderId);
        intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
        context.startActivity(intent);
    }
}
