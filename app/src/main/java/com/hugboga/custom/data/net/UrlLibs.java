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


    public static String SERVER_IP_HOST_PUBLIC_DEV = "api5-dev.huangbaoche.com/";//开发
    public static String SERVER_IP_HOST_PUBLIC_EXAMINATION = "api5-test.huangbaoche.com/";//test
    public static String SERVER_IP_HOST_PUBLIC_STAGE = "api5.huangbaoche.com/";//stage
    public static String SERVER_IP_HOST_PUBLIC_FORMAL = "api5.huangbaoche.com/";//生产

    public static String SERVER_IP_HOST_PUBLIC_DEFAULT = SERVER_IP_HOST_PUBLIC_DEV;//默认

    public static String SERVER_IP_HOST_PUBLIC = SERVER_HTTP_SCHEME + SERVER_IP_HOST_PUBLIC_DEFAULT;//主域名

    public static final String SERVER_IP_HOST_FORMAL = "https://api5.huangbaoche.com/";//线上域名


//    开发环境 host  res.dev.hbc.tech
//    测试环境 host  res.test.hbc.tech
//    生产环境 host  res2.huangbaoche.com

    public static String DEV_H5_HOST = "https://info-dev.huangbaoche.com";
    public static String TEST_H5_HOST = "https://info-test.huangbaoche.com";
    public static String FORMAL_H5_HOST = "https://info.huangbaoche.com";

    public static String H5_HOST = BuildConfig.H5_HOST;

    public static  String DEV_SHARE_BASE_URL_1 = "https://op-dev.huangbaoche.com/app/auth.html";
    public static  String DEV_SHARE_BASE_URL_2 = "https://act-dev.huangbaoche.com";
    public static  String DEV_SHARE_BASE_URL_3 = "https://act-dev.huangbaoche.com";
    public static  String DEV_SHARE_BASE_URL_4 = "https://m-dev.huangbaoche.com";
    public static  String DEV_SHARE_APPID = "wx1354271c597184ee";

    public static  String TEST_SHARE_BASE_URL_1 = "https://op-test.huangbaoche.com/app/auth.html";
    public static  String TEST_SHARE_BASE_URL_2 = "https://act-test.huangbaoche.com";
    public static  String TEST_SHARE_BASE_URL_3 = "https://act-test.huangbaoche.com";
    public static  String TEST_SHARE_BASE_URL_4 = "https://m-test.huangbaoche.com";
    public static  String TEST_SHARE_APPID = "wx1354271c597184ee";

    public static  String FORMAL_SHARE_BASE_URL_1 = "https://op.huangbaoche.com/app/auth.html";
    public static  String FORMAL_SHARE_BASE_URL_2 = "https://act.huangbaoche.com";
    public static  String FORMAL_SHARE_BASE_URL_3 = "https://act.huangbaoche.com";
    public static  String FORMAL_SHARE_BASE_URL_4 = "https://m.huangbaoche.com";
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
    public static String H5_ADDFEE_C = H5_HOST+"/cinfos/addfee_c.html";//  单次接送—后付费用说明
    public static String H5_ADDFEE_J = H5_HOST+"/cinfos/addfee_j.html";//  接机—后付费用说明
    public static String H5_ADDFEE_R = H5_HOST+"/cinfos/addfee_r.html";//  日租—后付费用说明
    public static String H5_ADDFEE_S = H5_HOST+"/cinfos/addfee_s.html";//  送机—后付费用说明
    public static String H5_ADDFEE_X = H5_HOST+"/cinfos/addfee_x.html"; // 后付费用说明
    public static String H5_CANCEL = H5_HOST+"/cinfos/cancel.html"; // 取消规则
    public static String H5_INSURANCE = H5_HOST+"/cinfos/insurance.html"; // 皇包车免费赠送保险说明
    public static String H5_NOTICE_V2_2 = H5_HOST+"/cinfos/notice_v2_2.html"; // 预订须知
    public static String H5_PRICE_V2_2 = H5_HOST+"/cinfos/price_v2_2.html"; // 费用说明
    public static String H5_PROBLEM = H5_HOST+"/cinfos/problem.html"; //常见问题
    public static String H5_PROTOCOL = H5_HOST+"/cinfos/protocol.html"; // 用户协议
    public static String H5_SERVICE = H5_HOST+"/cinfos/service.html"; // 服务承诺
    public static String H5_RAVEL_FUND_RULE = H5_HOST+ "/cinfos/travelfund.html";//旅游基金规则说明
    public static String H5_RAVEL_FUND_RULE_AGENTS = H5_HOST+ "/cinfos/travelfundagents.html";//旅游基金代理规则说明
    public static String H5_INVITE_FRIEND = BuildConfig.SHARE_BASE_URL_3 + "/h5/cactivity/inviteFriend/index.html";//旅游基金页面
    public static String H5_TAI_MANGU = H5_HOST + "/cinfos/tai/BBK.html";//曼谷
    public static String H5_TAI_PUJIDAO = H5_HOST + "/cinfos/tai/bki.html";//普吉
    public static String H5_TAI_QINGMAI = H5_HOST + "/cinfos/tai/cnx.html";//清迈
    public static String H5_TAI_SUMEIDAO = H5_HOST + "/cinfos/tai/VSM.html";//苏梅岛
    public static String H5_TAI_AGREEMENT = H5_HOST + "/cinfos/agreement.html";//用户协议和条款

    public static String H5_SHAREGUI = H5_HOST + "/cactivity/shareGui/index.html?";//分享砍价
    public static String H5_CREDIT_CARD_ARGEEMENT =H5_HOST + "/cinfos/pay_agreement.html";//常用信用卡协议

    public static String H5_GUIDE_DETAIL = BuildConfig.SHARE_BASE_URL_3 + "/h5/gactivity/guideIndex2/index.html?";
    public static String H5_RULES = BuildConfig.SHARE_BASE_URL_3+"/h5/cactivity/bargainNew/bargainInfo.html";//  砍价活动规则


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

    //current urlErrorCode 209

    //-------个人信息--------
    public static final String SERVER_IP_PUBLIC_UER_CENTER = "ucenter/v1.0/c/user/";

    public static final String SERVER_IP_CAPTCHA            = SERVER_IP_PUBLIC_UER_CENTER + "captcha?";                      // 发送验证码
    public static final String SERVER_IP_REGISTER           = SERVER_IP_PUBLIC_UER_CENTER + "register?";                     // 注册
    public static final String SERVER_IP_LOGIN              = SERVER_IP_PUBLIC_UER_CENTER + "login?";                        // 登录
    public static final String SERVER_IP_LOGIN_BYCAPTCHA    = SERVER_IP_PUBLIC_UER_CENTER + "login/bycaptcha?";              // 免密登录145
    public static final String SERVER_IP_LOGOUT             = SERVER_IP_PUBLIC_UER_CENTER + "logout?";                       // 退出登录
    public static final String SERVER_IP_INFORMATION        = SERVER_IP_PUBLIC_UER_CENTER + "information?";                  // 获取用户信息
    public static final String SERVER_IP_INFORMATION_UPDATE = SERVER_IP_PUBLIC_UER_CENTER + "information/update?";           // 修改个人信息
    public static final String SERVER_IP_PASSWORD_RESET     = SERVER_IP_PUBLIC_UER_CENTER + "password/reset?";               // 重置密码
    public static final String SERVER_IP_PASSWORD_UPDATE    = SERVER_IP_PUBLIC_UER_CENTER + "password/update?";              // 修改密码
    public static final String SERVER_IP_PASSWORD_SET       = SERVER_IP_PUBLIC_UER_CENTER + "password/init?";                // 设置初始密码146
    public static final String SERVER_IP_MOBILE_UPDATE      = SERVER_IP_PUBLIC_UER_CENTER + "mobile/update?";                // 修改手机号 ???? 未使用
    public static final String SERVER_IP_FEEDBACK_SAVE      = SERVER_IP_PUBLIC_UER_CENTER + "feedback/save?";                // 意见反馈
    public static final String GET_ACCESS_TOKEN             = SERVER_IP_PUBLIC_UER_CENTER + "wechat/login/check/unionid?";   // 微信登录校验openid是否已注册
    public static final String WECHAT_CHECK_MOBILE          = SERVER_IP_PUBLIC_UER_CENTER + "wechat/login/check/mobile?";    // 微信登录校验手机号是否填写
    public static final String WECHAT_SET_PASSWORD          = SERVER_IP_PUBLIC_UER_CENTER + "wechat/login/register/mobile?"; // 微信绑定设置密码
    public static final String WECHAT_BIND_MOBILE           = SERVER_IP_PUBLIC_UER_CENTER + "wechat/bind/mobile?";           // 微信绑定手机号
    public static final String WECHAT_AFTER_SET_PASSWORD    = SERVER_IP_PUBLIC_UER_CENTER + "wechat/update/password?";       // 微信跳过后再绑定手机设置密码

    public static final String COLLECT_GUIDES_LIST          = "ucenter/v1.2/c/favorite/guides";                         // 收藏的司导列表
    public static final String COLLECT_GUIDES_ID            = "ucenter/v1.0/c/userid/favorite/guide";                   // 收藏司导（司导ID）
    public static final String UNCOLLECT_GUIDES_ID          = "ucenter/v1.0/c/userid/unfavor/guide";                    // 取消收藏司导（司导ID）
    public static final String GUIDES_SAVED                 = "ucenter/v1.3/c/favorite/guides";                         // 已收藏司导列表 163
    public static final String GET_INVITATION_CODE          = "ucenter/v1.0/c/invitation/code";                         // 获取邀请码
    public static final String DEDUCTION                    = "ucenter/v1.0/c/travelFund/deduction";                    // 获取可用优惠券
    public static final String LINES_SAVED                  = "ucenter/v1.0/c/favorite/goodsNoList";                    // 已收藏线路列表 181
    public static final String COLLECT_LINES_LIST           = "ucenter/v1.1/c/favorite/goodsList";                      // 收藏线路列表 182
    public static final String COLLECT_LINES_NO             = "ucenter/v1.0/c/favorite/goods";                          // 收藏线路  183
    public static final String UNCOLLECT_LINES_NO           = "ucenter/v1.0/c/unfavorite/goods";                        // 取消收藏线路  184
    public static final String TRAVELFUND_HOME              = "ucenter/v1.0/c/travelFundHome";                          // 用户旅游基金首页  207
    public static final String QUERY_TRAVELFUND_INCOMELOG   = "ucenter/v1.0/c/queryTravelFundIncomeLog";                // 分页查询用户旅游基金收入流水  208
    public static final String QUERY_TRAVELFUND_EXPENSELOG  = "ucenter/v1.0/c/queryTravelFundExpenseLog";               // 分页查询用户旅游基金支出流水  209

    public static final String SERVER_IP_SUBMIT_PICKUP      = "trade/v1.5/c/order/pickup?";                             // 提交订单 接机
    public static final String SERVER_IP_SUBMIT_PICKUP2     = "trade/v1.4/c/order/pickup?";                             // 提交订单 秒杀接机150
    public static final String SERVER_IP_SUBMIT_TRANSFER    = "trade/v1.5/c/order/transfer?";                           // 提交订单 送机下单
    public static final String SERVER_IP_SUBMIT_SINGLE      = "trade/v1.5/c/order/single?";                             // 提交订单 单次用车
    public static final String SERVER_IP_SUBMIT_DAILY12     = "trade/v1.5/c/order/daily?";                              // 提交订单 日租包车 ???? 2个
    public static final String RECOMMENDLIN                 = "trade/v1.5/c/order/recommendline?";                      // 提交订单 推荐线路
    public static final String API_ORDER_EDIT               = "trade/v1.2/c/order/edit";                                // 修改订单
    public static final String SERVER_IP_ORDER_DETAIL       = "trade/v1.6/c/order/detail?";                             // 订单详情
    public static final String SERVER_IP_ORDER_CANCEL       = "trade/v1.0/c/order/cancel?";                             // 取消订单
    public static final String GET_USER_COUPON              = "trade/v1.0/c/order/home?";                               // 获取用户优惠卷
    public static final String SERVER_IP_GUIDES_COMMENTS    = "trade/v1.3/c/order/evaluate?";                           // 对车导评价 *** 1.0
    public static final String SERVER_IP_ORDER_PAY_ID       = "trade/v1.0/c/pay/getmobilepayurl?";                      // 订单支付 支付宝
    public static final String API_COUPON_PAY               = "trade/v1.0/c/pay/couponPay";                             // 买劵支付接口151
    public static final String SERVER_IP_IM_ORDER_LIST      = "trade/v1.0/c/order/list/im";                             // IM聊天界面的订单数据
    public static final String SERVER_IP_ORDER_HISTORY      = "trade/v1.0/c/order/list/history?";                       // IM中查询历史订单
    public static final String CANCLE_TIPS                  = "trade/v1.0/c/order/cancelTips";                          // 退改规则
    public static final String GUIDE_CONFLIC                = "trade/v1.1/c/order/guides/conflict";                     // 导游是否可服务 46
    public static final String PAY_SUCCESS                  = "trade/v1.1/c/order/paysuccess";                          // 支付成功页
    public static final String QUERYBARGAIN                 = "trade/v1.0/c/queryBargain?";                             // 砍价详情查询
    public static final String DELIVER_INFO                 = "trade/v1.5/c/order/deliverInfo";                         // 订单详情页发单情况
    public static final String ACCEPT_GUIDE_LIST            = "trade/v1.0/c/order/acceptGuide";                         // 表态愿意接单的导游列表
    public static final String CREATEBARGAIN                = "trade/v1.1/c/createBargain?";                            // 砍价分享
    public static final String CANCEL_REASON                = "trade/v1.0/c/order/cancelReason";                        // 取消订单原因接口
    public static final String ORDER_LIST_ALL               = "trade/v1.5/c/order/list/all";                            // 订单列表 全部 114
    public static final String ORDER_LIST_UNPAY             = "trade/v1.5/c/order/list/unpay";                          // 订单列表 待支付 115
    public static final String ORDER_LIST_DOING             = "trade/v1.5/c/order/list/doing";                          // 订单列表 进行中 116
    public static final String ORDER_LIST_UNEVALUDATE       = "trade/v1.5/c/order/list/unevaludate";                    // 订单列表 待评价 117
    public static final String GUIDE_CHOOSE                 = "trade/v1.0/c/order/guide/choose";                        // 指派司导接口 111
    public static final String API_ORDER_GROUP              = "trade/v1.5/c/order/group";                               // 组合单下单c  122
    public static final String API_BIND_CREDIT_CARD         = "trade/v1.0/c/yilian/bindcard";                           // 易联支付绑定卡127
    public static final String API_QUERY_CREDIT_CARD        = "trade/v1.0/c/yilian/querycard";                          // 易联支付查询已绑定的卡128
    public static final String API_QUERY_BANK_BELONG        = "trade/v1.0/c/yilian/querybank";                          // 易联查询银行卡所属银行129
    public static final String API_CREDIT_PAY               = "trade/v1.0/c/yilian/pay";                                // 易联支付接口130
    public static final String API_COUPON_CREDIT_PAY        = "trade/v1.0/c/yilian/couponpay";                          // 买劵易联支付接口152
    public static final String API_CALENDAR_LIST            = "trade/v1.0/c/calendar/list";                             // Capp司导日历查询 158
    public static final String API_DAILY_SECKILL            = "trade/v1.1/c/order/secKillDaily";                        // 秒杀包车下单 165
    public static final String API_PAYMENT_ABROD_CREDIT     = "trade/v1.0/c/ocean/pay";                                 // 境外信用卡  186
    public static final String API_EPOS_FIRST_PAY           = "trade/v1.0/c/epos/firstpay";                             // 国内信用卡首次支付  188
    public static final String API_EPOS_CHECK_FACTOR        = "trade/v1.0/c/epos/factorandverifycode";                  // 国内信用卡支付加验要素 189
    public static final String API_EPOS_BIND_LIST_PAY       = "trade/v1.0/c/epos/bindpay";                              // 已绑定的信用卡支付 190
    public static final String API_EPOS_SMS_SEND            = "trade/v1.0/c/epos/verifycodereceive";                    // 易宝获取验证码 191
    public static final String API_EPOS_SMS_VERIFY          = "trade/v1.0/c/epos/verifysale";                           // 易宝短信验证 192
    public static final String API_EPOS_BIND_LIST           = "trade/v1.0/c/epos/bindlist";                             // 国内信用卡绑定列表  193
    public static final String API_IP_FAKE_AI_GET           = "trade/v1.1/c/ai/askready?";                              // AI数据GET  197
    public static final String API_IP_FAKE_AI_POST          = "trade/v1.2/c/ai/askDuoDuo?";                             // AI数据POST 196

    public static final String SERVER_IP_PRICE_PICKUP       = "price/v1.5/c/airportPickupPrice?";                       // 查询价格 接机
    public static final String SERVER_IP_PRICE_TRANSFER     = "price/v1.5/c/airportTransferPrice?";                     // 查询价格 送机
    public static final String GET_CAR_INFOS                = "price/v1.3/c/dailyPrice?";                               // 废弃 查询价格 日租包车
    public static final String SERVER_IP_PRICE_SINGLE       = "price/v1.5/c/singlePrice?";                              // 查询价格 单次用车
    public static final String SERVER_IP_PRICE_SKU          = "price/v1.5/c/goodsPrice?";                               // 查询价格 SKU
    public static final String SERVER_IP_AIRPORT            = "price/v1.0/c/airports?";                                 // 机场 暂时不用，使用DB
    public static final String API_CAR_MAX_CAPACITY         = "price/v1.0/c/car/max/capacity";                          // 可服务车型最大可乘坐人数 118
    public static final String API_CAR_MAX_CAPACITY_SECKILLS= "price/v1.1/c/car/max/capacity";                          // 秒杀可服务车型最大可乘坐人数 166
    public static final String API_CITY_ROUTE               = "price/v1.0/c/queryCityRoute";                            // 查询城市行程  119
    public static final String API_BATCH_PRICE              = "price/v1.7/c/batchPrice";                                // 组合单报价  121
    public static final String API_BATCH_PRICE_CONTAINSFEE  = "price/v1.0/c/batchPriceContainsFee";                     // 组合单费用包含详细 180


    public static final String API_CITY_HOME_LIST           = "goods/v1.5/c/home/cityGoods?";                           // 城市商品列表
    public static final String API_CITY_HOME_COUNTRY_LIST   = "goods/v1.4/c/home/countryGoods?";                        // 国家列表
    public static final String API_CITY_HOME_ROUTE_LIST     = "goods/v1.4/c/home/lineGroupGoods?";                      // 线路圈
    public static final String API_GOODS_BY_ID              = "goods/v1.4/c/home/goodsByNo";                            // 按商品编号查商品详情
    public static final String GOODS_BOOK_DATE              = "goods/v1.0/c/home/goodsbookdate";                        // 根据商品编号查询可预订日期
    public static final String API_COUNTRY_GROUP            = "goods/v1.0/c/home/countryGroup";                         // 国家线路圈页 137
    public static final String API_GOODS_FILTER             = "goods/v1.0/c/home/goods/search";                         // 商品列表页筛选 139
    public static final String API_GOODS_GUIDE_INFO_LIST    = "goods/v1.1/c/goodsguideinfolist";                        // C端关联商品的可服务司导列表170
    public static final String API_QUERY_GOODS_STOCK        = "goods/v1.1/c/queryGoodsStock";                           // 商品库存171
    public static final String API_QUERY_GUIDE_STOCK        = "goods/v1.1/c/queryGuideGoodsStock";                      // 商品司导库存172
    public static final String API_GOODS_GUIDE_FILTER_OPTIONS= "goods/v1.0/c/goodsguidecondition";                      // 关联商品的可服务司导过滤条件 175
    public static final String API_GOODS_LINE_SEARCH        = "goods/v1.2/c/goodsSearch";                               // 线路搜索 177
    public static final String API_RECOMMENDED_GOODS        = "goods/v1.1/c/crossRecommendedGoods";                     // 根据订单城市交叉推荐商品列表v1.0 185
    public static final String API_CITY_DESTINATION_HOME    = "goods/v1.1/c/queryDestinationHome";                      // 查询目的地首页 194
    public static final String API_CITY_QUERY_SKU_LIST      = "goods/v1.1/c/pageQueryDestinationGoodsList?";            // 查询目的地玩法列表 195
    public static final String API_QUERY_CITY_GUIDE         = "goods/v1.0/c/queryCityGuide";                            // 查询目的地玩法列表 199
    public static final String API_QUERY_SHARE_INFO         = "goods/v1.0/c/queryShareInfo?";                           // 查询目的地分享信息 200
    public static final String API_QUERY_TAG_GOODS_LIST     = "goods/v1.0/c/pageQueryTagGoodsList";                     // 分页查询标签玩法列表 201

    public static final String SERVER_IP_COUPONS            = "marketing/v2.0/c/coupons?";                              // 优惠券(弃用)
    public static final String SERVER_IP_COUPONS_UNUSED     = "marketing/v2.0/c/coupons/unused?";                       // 优惠券未使用(我的,未使用)154
    public static final String SERVER_IP_COUPONS_USED       = "marketing/v2.0/c/coupons/used?";                         // 优惠券已使用(我的,失效)155
    public static final String SERVER_IP_COUPONS_UNAVAILABLE= "marketing/v1.1/c/coupons/unavailable?";                  // 优惠券已使用(下单,不可用券)156
    public static final String SERVER_IP_COUPONS_BIND       = "marketing/v1.0/c/coupons/bind?";                         // 优惠券 绑定
    public static final String GET_AD_PICTURE               = "marketing/v1.0/c/activity/effectivestart";               // 获取启动图
    public static final String MOSTFIT                      = "marketing/v1.1/c/coupons/mostFit";                       // 下单获取最适合的优惠券
    public static final String API_COUPONS_AVAILABLE        = "marketing/v1.1/c/coupons/available";                     // 获取可用优惠券(下单,可用优惠券)
    public static final String COUPON_ACTIVITY              = "marketing/v1.0/c/activity/couponactivity";               // CApp3.2领券礼物活动
    public static final String ACQUIRE_PACKET               = "marketing/v1.0/p/coupon/acquirePacket";                  // 未登陆领取礼包
    public static final String API_PICKUP_COUPON_OPEN       = "marketing/v1.0/c/coupons/pickupCouponOpen";              // 接机支付成功 绑定送机包车券 开关 149
    public static final String API_COUPONS_ORDERTIP         = "marketing/v1.1/c/coupons/orderTip";                      // 下单第一步获取优惠信息 163

    public static final String ADD_INSURE                   = "insurance/v1.0/c/user/add";                              // 新增投保人
    public static final String EDIT_INSURE_LIST             = "insurance/v1.0/c/user/edit";                             // 编辑投保人
    public static final String DEL_INSURE_LIST              = "insurance/v1.0/c/user/delete";                           // 删除投保人
    public static final String GET_INSURE_LIST              = "insurance/v1.0/c/user/list";                             // 查询投保人
    public static final String SUBMIT_INSURE_LIST           = "insurance/v2.0/c/insurance/submit";                      // 提交投保人
    public static final String API_INSURANCE_RESUBMIT       = "insurance/v2.0/c/insurance/resubmit";                    // 重新提交保单 135
    public static final String API_INSURANCE_SEARCH         = "insurance/v2.0/c/insurance/search";                      // 查询订单保单接口 136

    public static final String SERVER_IP_FLIGHTS_BY_NO      = "flight/v1.0/c/flights?";                                 // 航班查询 通过航班号
    public static final String SERVER_IP_FLIGHTS_BY_CITY    = "flight/v1.0/c/city/flights?";                            // 航班查询 通过城市

    public static final String UPLOAD_LOCATION              = "poi/v1.0/c/city?";                                       // 上传经纬度
    public static final String UPLOAD_LOCATION_V11          = "poi/v1.1/c/city?";                                       // 获取定位城市机场列表

    public static final String API_EVALUATE_NEW             = "supplier/v1.1/c/guide/comments/create";                  // 评价司导
    public static final String API_EVALUATE_RETURN_MONEY    = "supplier/v1.0/c/guide/returnMoney";                      // 评价返现开关159
    public static final String API_EVALUATE_COMMENTS        = "supplier/v1.0/c/order/comments";                         // 评价返回信息160
    public static final String API_EVALUATE_TAG             = "supplier/v1.0/c/guides/comments/labels/OrderType";       // 获取各个星级的标签列表
    public static final String API_COMMENTS_LIST            = "supplier/v1.0/c/guide/commentsForGuideDetail";           // 司导个人页的导游评价列表
    public static final String API_GUIDES_DETAIL            = "supplier/v1.0/c/guidecenter/detail";                     // 导游详情
    /*废弃*/public static final String CARS                 = "supplier/v1.0/c/guidecenter/detail/cars";                // 司导个人车辆信息 废弃
    public static final String API_CARS                     = "supplier/v2.0/c/guidecenter/detail/cars";                // 司导个人车辆信息2.0 142
    public static final String GUIDE_CROP_VALID             = "supplier/v1.0/c/guides/crop/valid";                      // 用户派单，验证接口
    public static final String API_GUIDECROP                = "supplier/v2.0/c/guides/guideCrop";                       // 查询司导可服务城市
    public static final String API_FILTER_GUIDES            = "supplier/v2.0/c/guides/qualityGuidesList";               // 查询精选司导列表 138
    public static final String API_GUIDE_EXTINFO            = "supplier/v2.0/c/guide/guidePersonalPageExtInfo";         // 司导个人页附加信息141
    public static final String API_GUIDE_AVAILABLE_CHECK    = "supplier/v2.0/c/guides/availableCheck";                  // 下单前校验司导是否可用 143
    public static final String API_GUIDE_FILTER_OPTIONS     = "supplier/v1.0/c/guide/screening/conditions";             // 精选司导筛选条件 153
    public static final String API_CHOICE_COMMENTS          = "supplier/v1.0/c/index/comments";                         // 精选司导筛选条件 174
    public static final String API_CHECK_SKU                = "supplier/v2.0/c/guides/check/sku";                       // 商品下单指定司导校验 176
    public static final String API_GOODS_GUIDE_SEARCH       = "supplier/v1.0/c/search/guide?";                          // 搜索司导 178
    public static final String API_CHAT_JUDGE               = "supplier/v1.0/c/chat/judge";                             // 根据司导和用户信息，判断当前该司导针对该用户是否可聊 205

    public static final String SERVER_IP_ACCESSKEY          = "passport/v1.0/getAccessKey?";                            // 获取accessKey
    public static final String UPDATE_DEVICE_INFO           = "passport/v1.0/updateDeviceInfo";                         // 更新设备信息接口144
    public static final String SERVER_IP_CHECK_APP_VERSION  = "passport/v1.0/checkAppVersion?";                         // 版本检测
    public static final String API_REPORT                   = "passport/v1.0/report";                                   // App信息采集上报接口
    public static final String API_OSS_TOKEN_URL            = "passport/v1.0/ossToken";                                 // 获取图片上传环境参数 157
    public static final String API_UPDATE_ANTICHEAT_INFO    = "passport/v1.0/updateAntiCheatInfo";                      // 更新设备反作弊信息 186

    public static final String API_HOME                     = "basicdata/v1.6/c/home/aggregation";                      // 首页城市卡片列表
    public static final String API_DESTINATIONS_TAB         = "basicdata/v1.0/c/home/destlist";                         // 目的地列表  167
    public static final String API_DESTINATIONS_HOT         = "basicdata/v1.0/c/queryHotDestinationHome";               // 目的地热门城市 168
    public static final String API_DESTINATIONS_LINE        = "basicdata/v1.0/c/queryTopDestinationHome";               // 目的地线路   169
    public static final String API_HOT_SEARCH               = "basicdata/v1.0/c/search/hotkeywords";                    // 热词搜索 179
    public static final String API_TOP_DRAWER               = "basicdata/v1.0/c/home/hide/header";                      // 首页上二楼 198

    public static final String API_ERROR_FEEDBACK           = "communication/v2.0/c/biz/error";                         // 反馈app业务异常信息
    public static final String SERVER_IP_NIM_TOKEN_UPDATE   = "communication/v3.0/c/im/token";                          // 获取云信token
    public static final String GET_CHAT_ID                  = "communication/v3.0/c/im/chat/info";                      // 获取chat/info
    public static final String SERVER_IP_NIM_UPDATE         = "communication/v3.0/c/im/clear";                          // 云信通知更新为已读
    public static final String SERVER_IP_PUSH_TOKEN         = "communication/v2.0/c/push/token";                        // 获取用户注册信息 - 注册极光别名时调用该接口进行反馈（Android + IOS）
    public static final String SERVER_IP_PUSH_CLICK         = "communication/v2.0/c/push/click";                        // APP收到的push被点击时，调用该接口进行反馈（Android + IOS）
    public static final String SERVER_IP_PUSH_RECEIVE       = "communication/v2.0/c/push/receive";                      // Android APP 接到push未点击时，调用该接口进行反馈（Android）

    public static final String SERVER_IP_NIMCHAT_LIST       = "communication/v4.0/c/im/list";                           // 云信最新聊天列表
    public static final String REMOVE_NIM_CHAT              = "communication/v3.0/c/im/chat/remove";                    // 云信私聊列表删除对话接口
    public static final String ADD_NIM_BLACK                = "communication/v3.0/c/im/black/add";                      // 云信私聊对象加入黑名单
    public static final String REMOVE_NIM_BLACK             = "communication/v3.0/c/im/black/remove";                   // 云信私聊对象移出黑名单
    public static final String API_SINGLE_CHAT_ORDER_DETAIL = "communication/v4.0/c/im/target/info";                    // 聊天列表订单信息
    public static final String API_IM_SERVER_INFO           = "communication/v2.0/c/im/info/kf";                        // 获取当前环境下的客服信息
    public static final String API_UPLOAD_IMANALYSIS_INFO   = "communication/v4.0/c/im/chat/upload";                    // 上传Im统计信息  161
    public static final String API_IMANALYSIS_SWITCH        = "communication/v4.0/c/im/chat/upload/switch";             // IM统计开关 162
    public static final String API_CAPTCHA                  = "communication/v1.0/c/voice/captcha";                     // 获取语音验证码 187
    public static final String API_PUSH_DEVICE              = "communication/v1.0/c/push/device";                       // push用户设备注册 202
    public static final String API_PUSH_DEVICE_LOGIN        = "communication/v1.0/c/push/device/login";                 // 用户设备登录 203
    public static final String API_PUSH_DEVICE_LOGOUT       = "communication/v1.0/c/push/device/logout";                // 用户设备注销 204F20
    public static final String AVAILABLE_CUSTOMER_SERVICE   = "communication/v1.0/c/im/availableCustomerService";       // 获取客服组ID 206

    public static final String API_SERVICE_QUESTION_LIST    = "crm/v1.0/c/advice/source";                               // 客服常见问题
    public static final String API_CREATE_TRAVEL_FORM       = "crm/v1.0/c/workorderthird/create";                       // 创建旅行意向单125
    public static final String API_QUERY_TRAVEL_FORM_LIST   = "crm/v1.0/c/workorderthird/list";                         // 创建旅行意向单126
    public static final String API_QUERY_TRAVEL_FORM_DETAIL = "crm/v1.0/c/workorderthird/detail";                       // 意向单详情123
    public static final String API_QUERY_HAS_CREATE_FORM    = "crm/v1.0/c/workorderthird/hasWorkorder";                 // 是否创建过意向单124

    public static final String POI_SEARCH_PLACES            = "search/v1.0/c/places";                                   // POI 搜索接口
    public static final String API_DIRECTION                = "proxy/v1.0/c/direction";                                 // 行程查询 120
    public static final String SERVER_IP_PIC_UPLOAD         = "file/v1.0/upload?";                                      // 更新头像\图片上传

    public static final String API_ACTIVITY_BUYNOW          = "activity/v1.0/c/buyNow";                                 // 参与接机秒杀活动详情 147
    public static final String API_AIRPORT_PICKUP_PRICE     = "activity/v1.0/c/airportPickupPrice";                     // 接机秒杀报价 148
    public static final String API_SECKILLS_BATCH_PRICE     = "activity/v1.1/c/batchPrice";                             // 秒杀报价 164

}