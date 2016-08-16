package com.hugboga.custom.action;

import android.content.Context;
import com.hugboga.custom.action.data.ActionBean;

/**
 * Created by qingcha on 16/8/13.
 */
public abstract class ActionPageBase implements ActionPageBehavior {

    protected String vcidType;
    private Context context;

    @Override
    public void intentPage(Context context, ActionBean actionBean) {
        this.context = context;
    }

    @Override
    public void setType(String _type) {
        this.vcidType = _type;
    }

    @Override
    public String getType() {
        return vcidType;
    }

    protected Context getContext() {
        return context;
    }

}
