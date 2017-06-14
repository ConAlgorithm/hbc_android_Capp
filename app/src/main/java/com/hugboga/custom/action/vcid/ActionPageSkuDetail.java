package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionSkuDetailBean;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.JsonUtils;

/**
 * Created by qingcha on 16/9/27.
 */
public class ActionPageSkuDetail extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (!ActionUtils.isLogin(context, actionBean) || actionBean.data == null) {
            return;
        }
        ActionSkuDetailBean bean = (ActionSkuDetailBean) JsonUtils.fromJson(actionBean.data, ActionSkuDetailBean.class);
        if (bean == null) {
            return;
        }
        Intent intent = new Intent(context, SkuDetailActivity.class);
        intent.putExtra(Constants.PARAMS_ID, bean.goodsNo);
        intent.putExtra(WebInfoActivity.WEB_URL, bean.url);
        intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
        context.startActivity(intent);
    }
}