package com.hugboga.custom.data.net;


import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.constants.Constants;

import java.util.HashMap;

public class UrlLibs {

    /**
     * 接口地址
     */

    public static String SERVER_HTTP_SCHEME_HTTP = "http://";
    public static String SERVER_HTTP_SCHEME_HTTPS = "https://";
    public static String SERVER_HTTP_SCHEME = SERVER_HTTP_SCHEME_HTTP;


    public static String SERVER_IP_HOST_PUBLIC_DEV = "api.dev.hbc.tech/";//开发
    public static String SERVER_IP_HOST_PUBLIC_EXAMINATION = "api.test.hbc.tech/";//test
    public static String SERVER_IP_HOST_PUBLIC_STAGE = "api2.huangbaoche.com/";//stage
    public static String SERVER_IP_HOST_PUBLIC_FORMAL = "api2.huangbaoche.com/";//生产

    public static String SERVER_IP_HOST_PUBLIC_DEFAULT = SERVER_IP_HOST_PUBLIC_DEV;//默认

    public static String SERVER_IP_HOST_PUBLIC = SERVER_HTTP_SCHEME + SERVER_IP_HOST_PUBLIC_DEFAULT;//主域名



//    开发环境 host  res.dev.hbc.tech
//    测试环境 host  res.test.hbc.tech
//    生产环境 host  res2.huangbaoche.com

    public static String DEV_H5_HOST = "http://info.dev.hbc.tech";
    public static String TEST_H5_HOST = "http://info.test.hbc.tech";
    public static String FORMAL_H5_HOST = "https://info.huangbaoche.com";

    public static String H5_HOST = BuildConfig.H5_HOST;

    public static  String DEV_SHARE_BASE_URL_1 = "http://test.op.huangbaoche.com/dev/app/auth.html";
    public static  String DEV_SHARE_BASE_URL_2 = "http://act.dev.huangbaoche.com";
    public static  String DEV_SHARE_BASE_URL_3 = "http://act.dev.huangbaoche.com";
    public static  String DEV_SHARE_BASE_URL_4 = "http://m.dev.huangbaoche.com";
    public static  String DEV_SHARE_APPID = "wx1354271c597184ee";

    public static  String TEST_SHARE_BASE_URL_1 = "http://test.op.huangbaoche.com/test/app/auth.html";
    public static  String TEST_SHARE_BASE_URL_2 = "http://act.test.huangbaoche.com";
    public static  String TEST_SHARE_BASE_URL_3 = "http://act.test.huangbaoche.com";
    public static  String TEST_SHARE_BASE_URL_4 = "http://m.test.huangbaoche.com";
    public static  String TEST_SHARE_APPID = "wx1354271c597184ee";

    public static  String FORMAL_SHARE_BASE_URL_1 = "http://op.huangbaoche.com/app/auth.html";
    public static  String FORMAL_SHARE_BASE_URL_2 = "http://act.huangbaoche.com";
    public static  String FORMAL_SHARE_BASE_URL_3 = "http://act.huangbaoche.com";
    public static  String FORMAL_SHARE_BASE_URL_4 = "http://m.huangbaoche.com";
    public static  String FORMAL_SHARE_APPID = "wx62ad814ba9bf0b68";

    public static  String SHARE_BASE_URL_1 = FORMAL_SHARE_BASE_URL_1;
    public static  String SHARE_BASE_URL_2 = FORMAL_SHARE_BASE_URL_2;
    public static  String SHARE_BASE_URL_3 = FORMAL_SHARE_BASE_URL_3;
    public static  String SHARE_BASE_URL_4 = FORMAL_SHARE_BASE_URL_4;
    public static  String SHARE_APPID = FORMAL_SHARE_APPID;//测试wx1354271c597184ee 线上wx62ad814ba9bf0b68

//    http://m.test.huangbaoche.com/app/dailyDetail.html?userId=100000001023&cityId=217
//    http://m.huangbaoche.com/app/dailyDetail.html?userId=100000001023&cityId=217
    public static String H5_DAIRY = BuildConfig.SHARE_BASE_URL_4 + "/app/dailyDetail.html";


    public static String H5_ACTIVITY= BuildConfig.SHARE_BASE_URL_3 + "/h5/cactivity/index.html?userId=";//国行
    public static String H5_ABOUT = H5_HOST+"/cinfos/about.html";//  关于我们
    public static String H5_RULES = H5_HOST+"/cinfos/rules.html ";//  砍价活动规则
    public static String H5_ADDFEE_C = H5_HOST+"/cinfos/addfee_c.html";//  单次接送—后付费用说明
    public static String H5_ADDFEE_J = H5_HOST+"/cinfos/addfee_j.html";//  接机—后付费用说明
    public static String H5_ADDFEE_R = H5_HOST+"/cinfos/addfee_r.html";//  日租—后付费用说明
    public static String H5_ADDFEE_S = H5_HOST+"/cinfos/addfee_s.html";//  送机—后付费用说明
    public static String H5_ADDFEE_X = H5_HOST+"/cinfos/addfee_x.html"; // 后付费用说明
    public static String H5_CANCEL = H5_HOST+"/cinfos/cancel.html"; // 取消规则
    public static String H5_INSURANCE = H5_HOST+"/cinfos/insurance.html"; // 皇包车免费赠送保险说明
    public static String H5_NOTICE = H5_HOST+"/cinfos/notice.html";  //预订须知
    public static String H5_NOTICE_V2_2 = H5_HOST+"/cinfos/notice_v2_2.html"; // 预订须知
    public static String H5_PRICE = H5_HOST+"/cinfos/price.html";  //费用说明
    public static String H5_PRICE_V2_2 = H5_HOST+"/cinfos/price_v2_2.html"; // 费用说明
    public static String H5_PROBLEM = H5_HOST+"/cinfos/problem.html"; //常见问题
    public static String H5_PROTOCOL = H5_HOST+"/cinfos/protocol.html"; // 用户协议
    public static String H5_SERVICE = H5_HOST+"/cinfos/service.html"; // 服务承诺
    public static String H5_RAVEL_FUND_RULE = H5_HOST+ "/cinfos/actdes.html";//旅游基金规则说明

    public static String H5_TAI_MANGU = H5_HOST + "/cinfos/tai/BBK.html";//曼谷
    public static String H5_TAI_PUJIDAO = H5_HOST + "/cinfos/tai/bki.html";//普吉
    public static String H5_TAI_QINGMAI = H5_HOST + "/cinfos/tai/cnx.html";//清迈
    public static String H5_TAI_SUMEIDAO = H5_HOST + "/cinfos/tai/VSM.html";//苏梅岛
    public static String H5_TAI_AGREEMENT = H5_HOST + "/cinfos/agreement.html";//用户协议和条款

    public static String H5_SHAREGUI = H5_HOST + "/cactivity/shareGui/index.html?";//分享砍价



    public static HashMap<Integer, String> OverPriceMap = new HashMap<Integer, String>();

    static {
        OverPriceMap.put(Constants.BUSINESS_TYPE_PICK, H5_ADDFEE_J);
        OverPriceMap.put(Constants.BUSINESS_TYPE_SEND, H5_ADDFEE_S);
        OverPriceMap.put(Constants.BUSINESS_TYPE_DAILY, H5_ADDFEE_R);
        OverPriceMap.put(Constants.BUSINESS_TYPE_RENT, H5_ADDFEE_C);
        OverPriceMap.put(Constants.BUSINESS_TYPE_OTHER, H5_ADDFEE_X);
    }


    /**
     * url host 的 enum
     */
    public enum UrlHost {
        DEVELOPER(SERVER_IP_HOST_PUBLIC_DEV),
        EXAMINATION(SERVER_IP_HOST_PUBLIC_EXAMINATION),
        STAGE(SERVER_IP_HOST_PUBLIC_STAGE),
        FORMAL(SERVER_IP_HOST_PUBLIC_FORMAL);


        public String url;

        UrlHost(String url) {
            this.url = url;
        }
    }

    //current urlErrorCode 106

    //-------个人信息--------
    public static final String SERVER_IP_PUBLIC_UER_CENTER = "ucenter/v1.0/c/user/";

    public static final String SERVER_IP_CAPTCHA            = SERVER_IP_PUBLIC_UER_CENTER + "captcha?";                      // 发送验证码
    public static final String SERVER_IP_REGISTER           = SERVER_IP_PUBLIC_UER_CENTER + "register?";                     // 注册
    public static final String SERVER_IP_LOGIN              = SERVER_IP_PUBLIC_UER_CENTER + "login?";                        // 登录
    public static final String SERVER_IP_LOGOUT             = SERVER_IP_PUBLIC_UER_CENTER + "logout?";                       // 退出登录
    public static final String SERVER_IP_INFORMATION        = SERVER_IP_PUBLIC_UER_CENTER + "information?";                  // 获取用户信息
    public static final String SERVER_IP_INFORMATION_UPDATE = SERVER_IP_PUBLIC_UER_CENTER + "information/update?";           // 修改个人信息
    public static final String SERVER_IP_PASSWORD_RESET     = SERVER_IP_PUBLIC_UER_CENTER + "password/reset?";               // 重置密码
    public static final String SERVER_IP_PASSWORD_UPDATE    = SERVER_IP_PUBLIC_UER_CENTER + "password/update?";              // 修改密码
    public static final String SERVER_IP_MOBILE_UPDATE      = SERVER_IP_PUBLIC_UER_CENTER + "mobile/update?";                // 修改手机号 ???? 未使用
    public static final String SERVER_IP_FEEDBACK_SAVE      = SERVER_IP_PUBLIC_UER_CENTER + "feedback/save?";                // 意见反馈
    public static final String GET_ACCESS_TOKEN             = SERVER_IP_PUBLIC_UER_CENTER + "wechat/login/check/unionid?";   // 微信登录校验openid是否已注册
    public static final String WECHAT_CHECK_MOBILE          = SERVER_IP_PUBLIC_UER_CENTER + "wechat/login/check/mobile?";    // 微信登录校验手机号是否填写
    public static final String WECHAT_SET_PASSWORD          = SERVER_IP_PUBLIC_UER_CENTER + "wechat/login/register/mobile?"; // 微信绑定设置密码
    public static final String WECHAT_BIND_MOBILE           = SERVER_IP_PUBLIC_UER_CENTER + "wechat/bind/mobile?";           // 微信绑定手机号
    public static final String WECHAT_AFTER_SET_PASSWORD    = SERVER_IP_PUBLIC_UER_CENTER + "wechat/update/password?";       // 微信跳过后再绑定手机设置密码

    public static final String COLLECT_GUIDES_LIST          = "ucenter/v1.1/c/favorite/guides";                         // 收藏的司导列表
    public static final String COLLECT_GUIDES_FILTER        = "ucenter/v1.1/c/favorite/guides/filter";                  // 过滤用户收藏的司导
    public static final String COLLECT_GUIDES_ID            = "ucenter/v1.0/c/userid/favorite/guide";                   // 收藏司导（司导ID）
    public static final String UNCOLLECT_GUIDES_ID          = "ucenter/v1.0/c/userid/unfavor/guide";                    // 取消收藏司导（司导ID）
    public static final String GET_INVITATION_CODE          = "ucenter/v1.0/c/invitation/code";                         // 获取邀请码
    public static final String DEDUCTION                    = "ucenter/v1.0/c/travelFund/deduction";                    // 获取可用优惠券
    public static final String TRAVELFUND_LOGS              = "ucenter/v1.0/c/travelFund/logs";                         // 旅游基金流水
    public static final String TRAVELFUND_INVITATION_LOGS   = "ucenter/v1.1/c/travelFund/logs/invitation";              // 查询通过邀请用户获得的基金流水

    public static final String SERVER_IP_SUBMIT_PICKUP      = "trade/v1.3/c/order/pickup?";                             // 提交订单 接机
    public static final String SERVER_IP_SUBMIT_TRANSFER    = "trade/v1.3/c/order/transfer?";                           // 提交订单 送机下单
    public static final String SERVER_IP_SUBMIT_SINGLE      = "trade/v1.3/c/order/single?";                             // 提交订单 单次用车
    public static final String SERVER_IP_SUBMIT_DAILY12     = "trade/v1.4/c/order/daily?";                              // 提交订单 日租包车 ???? 2个
    public static final String RECOMMENDLIN                 = "trade/v1.0/c/order/recommendline?";                      // 提交订单 推荐线路
    public static final String API_ORDER_EDIT               = "trade/v1.2/c/order/edit";                                // 修改订单
    public static final String SERVER_IP_ORDER_DETAIL       = "trade/v1.2/c/order/detail?";                             // 订单详情
    public static final String SERVER_IP_ORDER_LIST         = "trade/v1.2/c/order/list?";                               // 订单列表
    public static final String SERVER_IP_ORDER_CANCEL       = "trade/v1.0/c/order/cancel?";                             // 取消订单
    public static final String GET_USER_COUPON              = "trade/v1.0/c/order/home?";                               // 获取用户优惠卷
    public static final String SERVER_IP_GUIDES_COMMENTS    = "trade/v1.3/c/order/evaluate?";                           // 对车导评价 *** 1.0
    public static final String SERVER_IP_ORDER_PAY_ID       = "trade/v1.0/c/pay/getmobilepayurl?";                      // 订单支付 支付宝
    public static final String SERVER_IP_IM_ORDER_LIST      = "trade/v1.0/c/order/list/im";                             // IM聊天界面的订单数据
    public static final String SERVER_IP_ORDER_HISTORY      = "trade/v1.0/c/order/list/history?";                       // IM中查询历史订单
    public static final String API_HOME_DYNAMICS            = "trade/v1.0/c/order/track";                               // 首页今日动态
    public static final String CANCLE_TIPS                  = "trade/v1.0/c/order/cancelTips";                          // 退改规则
    public static final String GUIDE_CONFLIC                = "trade/v1.0/c/order/guides/conflict";                     // 导游是否可服务
    public static final String PAY_SUCCESS                  = "trade/v1.0/c/order/paysuccess";                          // 支付成功页
    public static final String QUERYBARGAIN                 = "trade/v1.0/c/queryBargain?";                             // 砍价详情查询
    public static final String DELIVER_INFO                 = "trade/v1.0/c/order/deliverInfo";                         // 订单详情页发单情况
    public static final String ACCEPT_GUIDE_LIST            = "trade/v1.0/c/order/acceptGuide";                         // 表态愿意接单的导游列表
    public static final String CREATEBARGAIN                = "trade/v1.0/c/createBargain?";                            // 砍价分享

    public static final String SERVER_IP_PRICE_PICKUP       = "price/v1.1/c/airportPickupPrice?";                       // 查询价格 接机
    public static final String SERVER_IP_PRICE_TRANSFER     = "price/v1.1/c/airportTransferPrice?";                     // 查询价格 送机
    public static final String GET_CAR_INFOS                = "price/v1.3/c/dailyPrice?";                               // 查询价格 日租包车
    public static final String SERVER_IP_PRICE_SINGLE       = "price/v1.1/c/singlePrice?";                              // 查询价格 单次用车
    public static final String SERVER_IP_PRICE_SKU          = "price/v1.2/c/goodsPrice?";                               // 查询价格 SKU
    public static final String SERVER_IP_AIRPORT            = "price/v1.0/c/airports?";                                 // 机场 暂时不用，使用DB

    public static final String API_SKU_CITY_LIST            = "goods/v1.2/c/home/cityGoods?";                           // 城市商品列表
    public static final String API_SKU_COUNTRY_LIST         = "goods/v1.2/c/home/countryGoods?";                        // 国家列表
    public static final String API_SKU_ROUTE_LIST           = "goods/v1.2/c/home/lineGroupGoods?";                      // 线路圈
    public static final String API_GOODS_BY_ID              = "goods/v1.2/c/home/goodsByNo";                            // 按商品编号查商品详情

    public static final String SERVER_IP_COUPONS            = "marketing/v2.0/c/coupons?";                              // 优惠券
    public static final String SERVER_IP_COUPONS_BIND       = "marketing/v1.0/c/coupons/bind?";                         // 优惠券 绑定
    public static final String GET_AD_PICTURE               = "marketing/v1.0/c/activity/effectivestart";               // 获取启动图
    public static final String MOSTFIT                      = "marketing/v1.1/c/coupons/mostFit";                       // 下单获取最适合的优惠券
    public static final String API_COUPONS_AVAILABLE        = "marketing/v1.1/c/coupons/available";                     // 获取可用优惠券

    public static final String ADD_INSURE                   = "insurance/v1.0/c/user/add";                              // 新增投保人
    public static final String EDIT_INSURE_LIST             = "insurance/v1.0/c/user/edit";                             // 编辑投保人
    public static final String DEL_INSURE_LIST              = "insurance/v1.0/c/user/delete";                           // 删除投保人
    public static final String GET_INSURE_LIST              = "insurance/v1.0/c/user/list";                             // 查询投保人
    public static final String SUBMIT_INSURE_LIST           = "insurance/v1.0/c/insurance/submit";                      // 提交投保人

    public static final String SERVER_IP_FLIGHTS_BY_NO      = "flight/v1.0/c/flights?";                                 // 航班查询 通过航班号
    public static final String SERVER_IP_FLIGHTS_BY_CITY    = "flight/v1.0/c/city/flights?";                            // 航班查询 通过城市

    public static final String SERVER_IP_POI                = "poi/v1.1/c/places?";                                     // poi 地理搜索
    public static final String UPLOAD_LOCATION              = "poi/v1.0/c/city?";                                       // 上传经纬度
    public static final String UPLOAD_LOCATION_V11          = "poi/v1.1/c/city?";                                       // 获取定位城市机场列表

    public static final String API_EVALUATE_NEW             = "supplier/v1.1/c/guide/comments/create";                  // 评价司导
    public static final String API_EVALUATE_TAG             = "supplier/v1.0/c/guides/comments/labels/OrderType";       // 获取各个星级的标签列表
    public static final String API_COMMENTS_LIST            = "supplier/v1.0/c/guide/commentsForGuideDetail";           // 司导个人页的导游评价列表
    public static final String API_GUIDES_DETAIL            = "supplier/v1.0/c/guidecenter/detail";                     // 导游详情
    public static final String CARS                         = "supplier/v1.0/c/guidecenter/detail/cars";                // 司导个人车辆信息

    public static final String SERVER_IP_ACCESSKEY          = "passport/v1.0/getAccessKey?";                            // 获取accessKey
    public static final String SERVER_IP_CHECK_APP_VERSION  = "passport/v1.0/checkAppVersion?";                         // 版本检测
    public static final String API_REPORT                   = "passport/v1.0/report";                                   // App信息采集上报接口

    public static final String API_HOME                     = "basicdata/v1.2/c/home/citys/contents?";                  // 首页城市卡片列表
    public static final String SERVER_IP_UPLOAD_LOGS        = "pt/v1.0/log/stream?";                                    // 发送log日志
    public static final String SERVER_IP_PIC_UPLOAD         = "file/v1.0/upload?";                                      // 更新头像\图片上传

    public static final String API_ERROR_FEEDBACK           = "communication/v2.0/c/biz/error";                         // 反馈app业务异常信息
    public static final String SERVER_IP_IM_TOKEN_UPDATE    = "communication/v2.0/c/im/token";                          // IM 通知更新 ??? 3.0
    public static final String SERVER_IP_NIM_TOKEN_UPDATE   = "communication/v3.0/c/im/token";                          // 获取云信token
    public static final String GET_CHAT_ID                  = "communication/v3.0/c/im/chat/info";                      // 获取chat/info
    public static final String SERVER_IP_IM_UPDATE          = "communication/v2.0/c/im/clear";                          // IM 通知更新为已读 ??? 3.0
    public static final String SERVER_IP_NIM_UPDATE         = "communication/v3.0/c/im/clear";                          // 云信通知更新为已读
    public static final String SERVER_IP_PUSH_TOKEN         = "communication/v2.0/c/push/token";                        // 获取用户注册信息 - 注册极光别名时调用该接口进行反馈（Android + IOS）
    public static final String SERVER_IP_PUSH_CLICK         = "communication/v2.0/c/push/click";                        // APP收到的push被点击时，调用该接口进行反馈（Android + IOS）
    public static final String SERVER_IP_PUSH_RECEIVE       = "communication/v2.0/c/push/receive";                      // Android APP 接到push未点击时，调用该接口进行反馈（Android）

    public static final String SERVER_IP_NIMCHAT_LIST       = "communication/v3.0/c/im/list";                           // 云信最新聊天列表
    public static final String REMOVE_NIM_CHAT              = "communication/v3.0/c/im/chat/remove";                    // 云信私聊列表删除对话接口
    public static final String ADD_NIM_BLACK                = "communication/v3.0/c/im/black/add";                      // 云信私聊对象加入黑名单
    public static final String REMOVE_NIM_BLACK             = "communication/v3.0/c/im/black/remove";                   // 云信私聊对象移出黑名单

    public static final String SERVER_IP_CHAT_LIST          = "communication/v2.0/c/im/list";                           // 聊天
    public static final String REMOVE_CHAT                  = "communication/v2.0/c/im/chat/remove";                    // 私聊列表删除对话接口
    public static final String ADD_BLACK                    = "communication/v2.0/c/im/black/add";                      // 私聊对象加入黑名单
    public static final String REMOVE_BLACK                 = "communication/v2.0/c/im/black/remove";                   // 私聊对象移出黑名单
    public static final String API_IM_SERVER_INFO           = "communication/v2.0/c/im/info/kf";                        // 获取当前环境下的客服信息

    /**========v1.4城市首页接口============**/
    public static final String API_CITY_HOME_LIST            = "goods/v1.4/c/home/cityGoods?";                           // 城市商品列表


}
