package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionOrderDetailBean;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.JsonUtils;

/**
 * Created by qingcha on 16/9/27.
 */
public class ActionPageOrderDetail extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (!ActionUtils.isLogin(context, actionBean) || actionBean.data == null) {
            return;
        }
        ActionOrderDetailBean bean = (ActionOrderDetailBean) JsonUtils.fromJson(actionBean.data, ActionOrderDetailBean.class);
        if (bean == null) {
            return;
        }
        OrderDetailActivity.Params params = new OrderDetailActivity.Params();
        params.orderType = CommonUtils.getCountInteger(bean.orderType);
        params.orderId = bean.orderNo;
        params.source = actionBean.source;
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        context.startActivity(intent);
    }
}
