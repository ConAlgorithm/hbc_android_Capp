package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionSkuGuideBean;
import com.hugboga.custom.activity.FilterGuideListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.JsonUtils;

/**
 * Created by qingcha on 17/8/8.
 */
public class ActionPageSkuGuide extends ActionPageBase {
    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (actionBean.data == null) {
            return;
        }
        ActionSkuGuideBean bean = (ActionSkuGuideBean) JsonUtils.fromJson(actionBean.data, ActionSkuGuideBean.class);
        if (bean == null) {
            return;
        }
        FilterGuideListActivity.Params params = new FilterGuideListActivity.Params();
        params.goodsNo = bean.goodsNo;
        Intent intent = new Intent(context, FilterGuideListActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
        context.startActivity(intent);
    }
}
