package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionChoosePayBean;
import com.hugboga.custom.activity.ChoosePaymentActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.JsonUtils;

/**
 * Created by qingcha on 17/6/14.
 */

public class ActionPageChoosePay extends ActionPageBase {
    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (!ActionUtils.isLogin(context, actionBean) || actionBean.data == null) {
            return;
        }
        ActionChoosePayBean bean = (ActionChoosePayBean) JsonUtils.fromJson(actionBean.data, ActionChoosePayBean.class);
        if (bean == null) {
            return;
        }

        ChoosePaymentActivity.RequestParams requestParams = new ChoosePaymentActivity.RequestParams();
        requestParams.orderId = bean.orderId;
        requestParams.shouldPay = CommonUtils.getCountDouble(bean.payPrice);
        requestParams.source = actionBean.source;
        requestParams.needShowAlert = true;
        requestParams.orderType = 0;
        requestParams.isOrder = false;
        requestParams.apiType = CommonUtils.getCountInteger(bean.apiType);
        requestParams.isWechat = CommonUtils.getCountInteger(bean.isWechat) == 1;
        requestParams.isAliPay = CommonUtils.getCountInteger(bean.isAliPay) == 1;
        requestParams.isUnionpay = CommonUtils.getCountInteger(bean.isUnionpay) == 1;

        Intent intent = new Intent(context, ChoosePaymentActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, requestParams);
        intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
        context.startActivity(intent);
    }
}
