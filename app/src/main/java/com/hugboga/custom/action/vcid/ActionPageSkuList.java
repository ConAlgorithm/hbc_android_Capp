package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionSkuListBean;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.JsonUtils;

/**
 * Created by qingcha on 16/9/27.
 *
 * test = "{\"t\": \"2\", \"v\": \"4\", \"d\": {\"t\":\"3\", \"ai\":\"204\"}}";
 */
public class ActionPageSkuList extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (actionBean.data == null) {
            return;
        }
        ActionSkuListBean bean = (ActionSkuListBean) JsonUtils.fromJson(actionBean.data, ActionSkuListBean.class);
        if (bean == null) {
            return;
        }
        CityActivity.Params params = new CityActivity.Params();
        params.id = CommonUtils.getCountInteger(bean.areaId);
        params.titleName = bean.areaName;
        switch (CommonUtils.getCountInteger(bean.type)) {
            case 1:
                params.cityHomeType = CityActivity.CityHomeType.ROUTE;
                break;
            case 2:
                params.cityHomeType = CityActivity.CityHomeType.COUNTRY;
                break;
            case 3:
                params.cityHomeType = CityActivity.CityHomeType.CITY;
                break;
        }
        Intent intent = new Intent(context, CityActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        if (actionBean.pushId != null){
            intent.putExtra(Constants.PARAMS_SOURCE, actionBean.pushId);
        } else{
            intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
        }
        context.startActivity(intent);
    }
}