package com.hugboga.custom.action;

/**
 * Created by qingcha on 16/7/27.
 */
public class ActionType {

    /**
     * 1:web页面
     * */
    public static final int WEB_ACTIVITY = 1;

    /**
     * 2:界面
     * */
    public static final int NATIVE_PAGE = 2;

    /**
     * 3:功能
     * */
    public static final int FUNCTION = 3;

    /**
     * 外部调起页面对应type
     * WIKI: http://wiki.hbc.tech/pages/viewpage.action?pageId=5505204
     * */
    public static class PageType {
        public static final int WEBVIEW = 1;       // 1：通用webView
        public static final int HOME = 2;          // 2：首页
        public static final int SEARCH = 3;        // 3：搜索页
//        public static final int SEARCH = 15;       // 15：优惠券列表
//        public static final int SEARCH = 16;       // 16：旅游基金
//        public static final int SEARCH = 21;       // 21：活动列表
    }

    public static class FunctionType {
        public static final int LOGOUT = 1;         // 1：注销
        public static final int UPDATE = 2;         // 2：更新
        public static final int EXTENSION = 3;      // 3：推广（应用外跳转）
    }


}
