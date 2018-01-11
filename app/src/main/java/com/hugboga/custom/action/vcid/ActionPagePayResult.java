package com.hugboga.custom.action.vcid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionPayResultBean;
import com.hugboga.custom.activity.ChoosePaymentActivity;
import com.hugboga.custom.activity.PayResultActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.SharedPre;

/**
 * Created by zhangqiang on 17/9/4.
 */

public class ActionPagePayResult extends ActionPageBase {
    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (!ActionUtils.isLogin(context, actionBean) || actionBean.data == null) {
            return;
        }
        ActionPayResultBean bean = (ActionPayResultBean) JsonUtils.fromJson(actionBean.data, ActionPayResultBean.class);
        if (bean == null) {
            return;
        }
        PayResultActivity.Params params = new PayResultActivity.Params();
        if (bean.resultType.equals("1")) {
            params.payResult = true;
        } else {
            params.payResult = false;
        }
        params.orderId = bean.orderNo;
        params.orderType = SharedPre.getInteger(ChoosePaymentActivity.ORDERTYPE, 0);
        params.apiType = SharedPre.getInteger(ChoosePaymentActivity.APITYPE, 0);
        Intent intent = new Intent(context, PayResultActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, "收银台");
        intent.putExtra(Constants.PARAMS_DATA, params);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
