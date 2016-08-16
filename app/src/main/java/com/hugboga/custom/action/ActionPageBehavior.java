package com.hugboga.custom.action;

import android.content.Context;

import com.hugboga.custom.action.data.ActionBean;

/**
 * Created by qingcha on 16/8/13.
 */
public interface ActionPageBehavior {

    public void intentPage(Context context, ActionBean actionBean);

    public String getType();

    public void setType(String _type);
}
