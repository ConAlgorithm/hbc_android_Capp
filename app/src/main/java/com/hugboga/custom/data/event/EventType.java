package com.hugboga.custom.data.event;

/**
 * event 类型定义
 */
public enum EventType {
    CLICK_USER_LOGIN,//登录
    CLICK_USER_LOOUT,//退出
    REFRESH_ORDER_DETAIL,//刷新订单详情
    SET_MAIN_PAGE_INDEX,//设置main 页面滚动到第几个tab
    PAY_CANCEL//取消支付
}
