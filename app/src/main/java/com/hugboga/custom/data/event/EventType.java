package com.hugboga.custom.data.event;

/**
 * event 类型定义
 */
public enum EventType {
    CLICK_USER_LOGIN,//登录
    CLICK_USER_LOOUT,//退出
    SETTING_BACK,//seting 界面返回需重新请求个人信息,刷新小红点
    REFRESH_ORDER_DETAIL,//刷新订单详情
    REFRESH_CHAT_LIST,//刷新聊天列表
    SET_MAIN_PAGE_INDEX,//设置main 页面滚动到第几个tab
    PAY_CANCEL,//取消支付
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
    WECHAT_SHARE_SUCCEED,//微信分享成功
    PAY_RESULT,//支付回调
    TRAVEL_LIST_TYPE,//行程列表分类
    QUESTION_ITEM,//客服问题

    CHANGE_MOBILE,//修改手机号
    BIND_MOBILE,//绑定手机号

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
    FGTRAVEL_UPDATE,//登录

    ORDER_DETAIL_PAY,//去支付
    ORDER_DETAIL_BACK,//返回
    ORDER_DETAIL_CALL,//电话
    ORDER_DETAIL_MORE,//更多
    ORDER_DETAIL_UPDATE_COLLECT,//更新收藏UI
    ORDER_DETAIL_UPDATE_EVALUATION,//更新评价UI
    ORDER_DETAIL_UPDATE_INFO,//更新个人信息UI
    ORDER_DETAIL_UPDATE,//刷新数据
    ORDER_DETAIL_ROUTE,//路线详情

    SHOW_ORDER_DETAIL,//显示订单详情
    ORDER_GO_HOME,//支付成功回首页
    GUIDE_ERROR_TIME,//司导无法在所选时间段内进行服务
    GUIDE_DEL,//删除司导
    CHANGE_GUIDE,//换司导
    ONBACKPRESS,//触发后退键
    PICK_SEND_ONBACKPRESS,//接送backpress

    SKU_HOTEL_NUM_CHANGE,//sku 酒店房间数量变化
    CHOOSE_DATE,//选日期

    CAR_CHANGE_SMALL,//车型小于人数
    MAX_LUGGAGE_NUM,//计算出的最大行李数
    CHOOSE_START_CITY_BACK,//选开始城市返回
    CHOOSE_END_CITY_BACK,//选结束城市返回
    CHOOSE_POI_BACK,//选POI返回
    CHOOSE_COUNTRY_BACK,//选country返回
    COUPON_BACK,//选country返回
    PICK_FLIGHT_BACK,//选航班号返回
    AIR_PORT_BACK,//选airport返回
    CHOOSE_GUIDE_CITY_BACK,//选司导所能服务的城市

    NIM_LOGIN_SUCCESS,//云信登录成功回调
    CARIDS,//司导车辆id

    CHOOSE_POI,//选POI

    CITY_FILTER_TYPE,//城市列表按类型过滤
    CITY_FILTER_DAY,//城市列表按天数过滤
    CITY_FILTER_THEME,//城市列表按主题过滤
    CITY_FILTER_CLOSE,//关闭城市过滤

    SHOW_GIFT_DIALOG,//显示未登录领卷

    SKU_ORDER_REFRESH,//线路下单 数据刷新

    CHARTER_LIST_REFRESH,//包车选行程刷新
    CHARTER_FIRST_REFRESH,//刷新包车第一步页面

    PURPOSER_CITY,//填写意向单
    YILIAN_PAY,//易联支付

    GUIDE_FILTER_CITY,
    GUIDE_FILTER_SCOPE,
    GUIDE_FILTER_SORT,
    FILTER_CLOSE,

    SKU_FILTER_SCOPE,

    SHOW_GUIDE_DETAIL_BAR,
}
