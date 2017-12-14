package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionGuideDetailBean;
import com.hugboga.custom.activity.GuideWebDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.JsonUtils;

/**
 * Created by qingcha on 16/9/27.
 */
public class ActionPageGuideDetail extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (actionBean.data == null) {
            return;
        }
        ActionGuideDetailBean bean = (ActionGuideDetailBean) JsonUtils.fromJson(actionBean.data, ActionGuideDetailBean.class);
        if (bean == null) {
            return;
        }
        GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
        params.guideId = bean.guideId;
        Intent intent = new Intent(context, GuideWebDetailActivity.class);
        if (!TextUtils.isEmpty(actionBean.pushId)){
            intent.putExtra(Constants.PARAMS_SOURCE,actionBean.pushId);
        }else {
            intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
        }
        intent.putExtra(Constants.PARAMS_DATA, params);
        context.startActivity(intent);
    }
}