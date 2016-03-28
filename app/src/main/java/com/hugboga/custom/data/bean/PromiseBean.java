package com.hugboga.custom.data.bean;

/**
 * 承诺
 * Created by admin on 2015/8/3.
 */
public class PromiseBean implements IBaseBean {

    public int icon;
    public int title;
    public int content;

    public PromiseBean() {
    }

    public PromiseBean(int icon, int title, int content) {
        this.icon = icon;
        this.title = title;
        this.content = content;
    }

//    @Override
//    public void parser(JSONObject jsonObj) throws JSONException {
//
//    }
}
