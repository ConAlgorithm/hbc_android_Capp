package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionGuideDetailBean;
import com.hugboga.custom.activity.GuideDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.JsonUtils;

/**
 * Created by qingcha on 16/9/27.
 */
public class ActionPageGuideDetail extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (!ActionUtils.isLogin(context) || TextUtils.isEmpty(actionBean.data)) {
            return;
        }
        ActionGuideDetailBean bean = (ActionGuideDetailBean) JsonUtils.fromJson(actionBean.data, ActionGuideDetailBean.class);
        if (bean == null) {
            return;
        }
        Intent intent = new Intent(context, GuideDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, bean.guideId);
        context.startActivity(intent);
    }
}