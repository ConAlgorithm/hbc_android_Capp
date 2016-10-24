package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionSkuDetailBean;
import com.hugboga.custom.action.data.ActionSkuListBean;
import com.hugboga.custom.activity.CityHomeListActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.JsonUtils;
import com.netease.nim.uikit.common.util.C;

/**
 * Created by qingcha on 16/9/27.
 */
public class ActionPageSkuList extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (!ActionUtils.isLogin(context) || TextUtils.isEmpty(actionBean.data)) {
            return;
        }
        ActionSkuListBean bean = (ActionSkuListBean) JsonUtils.fromJson(actionBean.data, ActionSkuListBean.class);
        if (bean == null) {
            return;
        }
        CityHomeListActivity.Params params = new CityHomeListActivity.Params();
        params.id = CommonUtils.getCountInteger(bean.areaId);
        switch (CommonUtils.getCountInteger(bean.areaId)) {
            case 1:
                params.cityHomeType = CityHomeListActivity.CityHomeType.ROUTE;
                break;
            case 2:
                params.cityHomeType = CityHomeListActivity.CityHomeType.COUNTRY;
                break;
            case 3:
                params.cityHomeType = CityHomeListActivity.CityHomeType.CITY;
                break;
        }
        Intent intent = new Intent(context, CityHomeListActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
        context.startActivity(intent);
    }
}