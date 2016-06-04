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
    ADD_INSURE_SUCCESS, //添加投保人成功
    AIR_NO,//航班号返回
    MAN_CHILD_LUUAGE, //乘客行李
    CHANGE_CAR, //选择汽车
    CHOOSE_GUIDE, //收藏选择司导
    CONTACT, //通讯录选取联系人
    CONTACT_BACK, //通讯录选取联系人返回显示

    SINGLE_TYPE, //单次接送
    DAIRY_TYPE, //包车
    PICK_SEND_TYPE, //接送机
    SINGLE_BACK, //单次接送返回
    pick_BACK, //接机返回
    SEND_BACK, //送机返回
    SELECT_COUPON_BACK, //选择优惠券返回
    CHLID_SEAT_PRICE_BACK, //返回儿童座椅价钱
    CHECK_SWITCH,//办理登机
    WAIT_SWITCH,//接机等待

    BACK_HOME,//回首页

    ORDER_DETAIL_PAY,//去支付
    ORDER_DETAIL_INSURANCE_H5,//皇包车免费赠送保险说
    ORDER_DETAIL_ADD_INSURER,//添加投保人
    ORDER_DETAIL_BACK,//返回
    ORDER_DETAIL_CALL,//电话
    ORDER_DETAIL_MORE,//更多
    ORDER_DETAIL_GUIDE_COLLECT,//收藏
    ORDER_DETAIL_GUIDE_EVALUATION,//评价
    ORDER_DETAIL_GUIDE_CALL,//给司导电话
    ORDER_DETAIL_GUIDE_CHAT,//和司导聊天
    ORDER_DETAIL_GUIDE_INFO,//司导详情
    ORDER_DETAIL_UPDATE_COLLECT,//更新收藏UI
    ORDER_DETAIL_UPDATE_EVALUATION,//更新评价UI
    ORDER_DETAIL_UPDATE_INFO,//更新个人信息UI

    SHOW_ORDER_DETAIL,//显示订单详情
    ORDER_GO_HOME,//支付成功回首页
}
