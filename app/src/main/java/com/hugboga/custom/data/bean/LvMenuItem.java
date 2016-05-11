package com.hugboga.custom.data.bean;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/3/9.
 */
public class LvMenuItem {

    public String name;
    public int icon;
    public String tips;

    public LvMenuItem(int icon, String name, String tips) {
        this.icon = icon;
        this.name = name;
        if (!TextUtils.isEmpty(tips)) {
            this.tips = tips;
        }
    }
}
