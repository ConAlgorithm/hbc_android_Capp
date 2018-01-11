package com.hugboga.custom.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.GuidanceOrderActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.SeckillsBean;
import com.hugboga.custom.statistic.sensors.SensorsUtils;

/**
 * Created by qingcha on 17/12/8.
 */

public class IntentUtils {

    public static void intentCharterActivity(Context context, String source) {
        intentCharterActivity(context,null,null,null, source);
    }

    public static void intentCharterActivity(Context context, SeckillsBean seckillsBean, GuidesDetailData guidesDetailData, CityBean startBean, String source) {
        String guideId = "";
        if (guidesDetailData != null || startBean != null) {//不需要进入引导页
            Intent intent = new Intent(context, CharterFirstStepActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, source);
            if (guidesDetailData != null) {
                intent.putExtra(Constants.PARAMS_GUIDE, guidesDetailData);
                guideId = guidesDetailData.guideId;
            }
            if (startBean != null) {
                intent.putExtra(Constants.PARAMS_START_CITY_BEAN, startBean);
            }
            context.startActivity(intent);
        } else {
            GuidanceOrderActivity.Params params = new GuidanceOrderActivity.Params();
            params.orderType = 3;
            params.source = source;
            if (seckillsBean != null) {
                params.seckillsBean = seckillsBean;
            }
            Intent intent = new Intent(context, GuidanceOrderActivity.class);
            intent.putExtra(Constants.PARAMS_DATA, params);
            intent.putExtra(Constants.PARAMS_SOURCE, source);
            context.startActivity(intent);
        }
        SensorsUtils.setSensorsBuyViewEvent("按天包车游", source, guideId);
    }

    public static void intentSingleActivity(Context context, String source) {
        intentSingleActivity(context,null, source);
    }

    public static void intentSingleActivity(Context context, SingleActivity.Params _params, String source) {
        String guideId = "";
        if (_params != null && (_params.guidesDetailData != null || !TextUtils.isEmpty(_params.cityId))) {//不需要进入引导页
            if (_params.guidesDetailData != null) {
                guideId = _params.guidesDetailData.guideId;
            }
            Intent intent = new Intent(context, SingleActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, source);
            intent.putExtra(Constants.PARAMS_DATA, _params);
            context.startActivity(intent);
        } else {
            GuidanceOrderActivity.Params params = new GuidanceOrderActivity.Params();
            params.orderType = 4;
            params.source = source;
            Intent intent = new Intent(context, GuidanceOrderActivity.class);
            intent.putExtra(Constants.PARAMS_DATA, params);
            intent.putExtra(Constants.PARAMS_SOURCE, source);
            context.startActivity(intent);
        }
        SensorsUtils.setSensorsBuyViewEvent("单次", source, guideId);
    }

    public static void intentPickupActivity(Context context, String source) {
        intentPickupActivity(context, null, source);
    }

    public static void intentPickupActivity(Context context, PickSendActivity.Params _params, String source) {
        String guideId = "";
        boolean condition1 = _params != null && (_params.guidesDetailData != null || _params.flightBean != null);
        boolean condition2 =  _params != null && !TextUtils.isEmpty(_params.cityName) && !TextUtils.isEmpty(_params.cityId);
        if (condition1 || condition2) {//不需要进入引导页
            if (_params.guidesDetailData != null) {
                guideId = _params.guidesDetailData.guideId;
            }
            Intent intent = new Intent(context, PickSendActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, source);
            intent.putExtra(Constants.PARAMS_DATA, _params);
            context.startActivity(intent);
        } else {
            GuidanceOrderActivity.Params params = new GuidanceOrderActivity.Params();
            params.orderType = 1;
            params.source = source;
            Intent intent = new Intent(context, GuidanceOrderActivity.class);
            intent.putExtra(Constants.PARAMS_DATA, params);
            intent.putExtra(Constants.PARAMS_SOURCE, source);
            context.startActivity(intent);
        }
        SensorsUtils.setSensorsBuyViewEvent("接机", source, guideId);
    }

    public static void intentSendActivity(Context context, String source) {
        intentSendActivity(context, null, source);
    }

    public static void intentSendActivity(Context context, PickSendActivity.Params _params, String source) {
        String guideId = "";
        boolean condition1 = _params != null && (_params.guidesDetailData != null || _params.airPortBean != null);
        boolean condition2 =  _params != null && !TextUtils.isEmpty(_params.cityName) && !TextUtils.isEmpty(_params.cityId);
        if (condition1 || condition2) {//不需要进入引导页
            if (_params.guidesDetailData != null) {
                guideId = _params.guidesDetailData.guideId;
            }
            Intent intent = new Intent(context, PickSendActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, source);
            intent.putExtra(Constants.PARAMS_DATA, _params);
            context.startActivity(intent);
        } else {
            GuidanceOrderActivity.Params params = new GuidanceOrderActivity.Params();
            params.orderType = 2;
            params.source = source;
            Intent intent = new Intent(context, GuidanceOrderActivity.class);
            intent.putExtra(Constants.PARAMS_DATA, params);
            intent.putExtra(Constants.PARAMS_SOURCE, source);
            context.startActivity(intent);
        }
        SensorsUtils.setSensorsBuyViewEvent("送机", source,guideId);
    }

}
