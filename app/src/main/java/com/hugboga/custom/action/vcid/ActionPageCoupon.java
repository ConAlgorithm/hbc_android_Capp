package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.CouponActivity;
import com.hugboga.custom.constants.Constants;

/**
 * Created by qingcha on 16/8/13.
 */
public class ActionPageCoupon extends ActionPageBase {

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (ActionUtils.isLogin(context, actionBean)) {
            Intent intent = new Intent(context, CouponActivity.class);

            if (!TextUtils.isEmpty(actionBean.pushId)){
                intent.putExtra(Constants.PARAMS_SOURCE, "push" + actionBean.pushId);
            }else {
                intent.putExtra(Constants.PARAMS_SOURCE, actionBean.source);
            }
            intent.putExtra("isFromMyspace",true);
            context.startActivity(intent);
        }
    }

}

