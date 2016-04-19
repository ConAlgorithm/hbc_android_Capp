package com.huangbaoche.hbcframe.data.net;

import android.view.View;

/**
 * Created by admin on 2016/3/24.
 */
public class HttpRequestOption {

    /**
     * 是否需要展示loading
     */
    public boolean needShowLoading = true;
    /**
     * 点击按钮
     */
    public View btn ;
    /**
     * 设置点击按钮
     */
    public void setBtnEnabled(boolean dnabled){
        if(btn!=null)
        btn.setEnabled(dnabled);
    }

    @Override
    public HttpRequestOption clone() {
        HttpRequestOption option = new HttpRequestOption();
        option.btn = btn;
        option.needShowLoading = needShowLoading;
        return option;
    }
}
