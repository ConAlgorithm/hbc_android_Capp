package com.hugboga.custom.action.vcid;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.action.ActionPageBase;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.data.ActionBean;

/**
 * Created by qingcha on 16/9/27.
 */
public class ActionPageChatList extends ActionPageBase {
    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        super.intentPage(context, actionBean);
        if (!ActionUtils.isLogin(context, actionBean)) {
            return;
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.PARAMS_PAGE_INDEX, 1);
        context.startActivity(intent);
    }
}
