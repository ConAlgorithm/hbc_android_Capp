package com.hugboga.custom.action.constants;

/**
 * Created by qingcha on 16/8/13.
 *
 * 外部调起页面对应type
 * WIKI: http://wiki.hbc.tech/pages/viewpage.action?pageId=5505204
 */
public class ActionPageType {

    public static final int WEBVIEW = 1;                      // 1：通用webView
    public static final int HOME = 2;                         // 2：首页
    public static final int SEARCH = 3;                       // 3：搜索页
    public static final int SKU_LIST = 4;                     // 4：商品列表页（type：组合、国家、城市，areaId, areaName）
    public static final int SKU_DETAIL = 5;                   // 5：商品详情页（cityId, goodsNo）
    public static final int CHARTERED_CAR = 6;                // 6：自定义包车游
    public static final int PICK_UP = 7;                      // 7：中文接机
    public static final int SEND = 8;                         // 8：中文送机
    public static final int SINGLE = 9;                       // 9：单次接送
    public static final int IM_CHAT = 10;                     // 10：聊天页面（登录、id、name、avatar、type）
    public static final int ORDER_LIST = 11;                  // 11：订单列表（type:进行中、已完成、已取消）
    public static final int ORDER_DETAIL = 12;                // 12：订单详情（登录、orderNo，orderType）
    public static final int USER_INFO = 14;                   // 14：个人资料（登录）
    public static final int COUPON = 15;                      // 15：优惠券列表（登录）
    public static final int TRAVEL_FUND = 16;                 // 16：旅游基金（登录）
    public static final int INSURE = 18;                      // 18：常用投保人（登录）
    public static final int COLLECT_GUIDE = 19;               // 19：收藏的司导（登录）
    public static final int GUIDE_DETAIL = 20;                // 20：司导详情页（登录、司导ID）
    public static final int ACTIVITY_LIST = 21;               // 21：活动列表
    public static final int SERVICER_CENTER = 22;             // 22：服务规则
    public static final int CHAT_LIST = 30;                   // 30：私聊列表
    public static final int TRAVEL_FUND_USED_BILL = 31;       // 31：旅游基金使用明细
    public static final int INVITE_BILL = 32;                 // 32：邀请记录（登录）
    public static final int PURPOSE_FORM = 33;                // 33：意向单页
    public static final int CHOOSE_PAY = 34;                  // 34：支付收银台（登录、orderId、payPrice、apiType、isWechat、isAlipay、isUnionpay）
    public static final int EVALUATE = 35;                    // 35: 已评价界面 (orderId)
    public static final int SKU_GUIDE = 38;                   // 38: 商品可服务司导 (goodsNo)
    public static final int PAY_RESULT = 39;                  // 39: 支付结果页(orderId,orderType,payResult,apiType)
}
