package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionSkuListBean;
import com.hugboga.custom.activity.CityListActivity;
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
        if (!ActionUtils.isLogin(context) || actionBean.data == null) {
            return;
        }
        ActionSkuListBean bean = (ActionSkuListBean) JsonUtils.fromJson(actionBean.data, ActionSkuListBean.class);
        if (bean == null) {
            return;
        }
        CityListActivity.Params params = new CityListActivity.Params();
        params.id = CommonUtils.getCountInteger(bean.areaId);
        switch (CommonUtils.getCountInteger(bean.type)) {
            case 1:
                params.cityHomeType = CityListActivity.CityHomeType.ROUTE;
                break;
            case 2:
                params.cityHomeType = CityListActivity.CityHomeType.COUNTRY;
                break;
            case 3:
                params.cityHomeType = CityListActivity.CityHomeType.CITY;
                break;
        }
        Intent intent = new Intent(context, CityListActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
        context.startActivity(intent);
    }
}