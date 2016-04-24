package com.hugboga.custom.data.event;

/**
 * event 类型定义
 */
public enum EventType {
    CLICK_USER_LOGIN,//登录
    CLICK_USER_LOOUT,//退出
    REFRESH_ORDER_DETAIL,//刷新订单详情
    REFRESH_CHAT_LIST,//刷新聊天列表
    SET_MAIN_PAGE_INDEX,//设置main 页面滚动到第几个tab
    PAY_CANCEL,//取消支付
    CLICK_HEADER_LEFT_BTN_BACK, //点击后退按钮 或者back健
    START_NEW_FRAGMENT, //startfragment
    WECHAT_LOGIN_CODE,
    EDIT_INSURE, //编辑投保人
    ADD_INSURE, //添加投保人
    EDIT_BACK_INSURE, //编辑返回
    CHECK_INSURE, //选择投保人
    ADD_INSURE_SUCCESS //添加投保人成功
}
