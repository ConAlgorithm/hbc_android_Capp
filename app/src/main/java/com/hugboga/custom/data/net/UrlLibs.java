package com.hugboga.custom.data.net;


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


    /**
     * url host 的 enum
     */
    public static enum UrlHost {
        DEVELOPER(SERVER_IP_HOST_PUBLIC_DEV),
        EXAMINATION(SERVER_IP_HOST_PUBLIC_EXAMINATION),
        STAGE(SERVER_IP_HOST_PUBLIC_STAGE),
        FORMAL(SERVER_IP_HOST_PUBLIC_FORMAL);

        public String url;

        private UrlHost(String url) {
            this.url = url;
        }
    }


    //-------业务-------

    //ACCESS KEY
    public static final String SERVER_IP_ACCESSKEY = "passport/v1.0/getAccessKey?";
    //版本检测
    public static final String SERVER_IP_CHECK_APP_VERSION = "reflash/v1.0/checkAppVersion?";
    //upload logs
    public static final String SERVER_IP_UPLOAD_LOGS = "pt/v1.0/log/stream?";

    //首页
    public static final String SERVER_IP_HOME = "basicdata/v1.0/p/home/citys/contents?";
    //SKU
    public static final String SERVER_IP_CITY_SKU = "goods/v1.0/p/home/cityGoods?";
    //聊天
    public static final String SERVER_IP_CHAT_LIST = "communication/v2.0/c/im/list?";

    //-------个人信息--------
    public static final String SERVER_IP_PUBLIC_UER_CENTER = "ucenter/v1.0/c/user/";
    /**
     * 验证码
     **/
    public static final String SERVER_IP_CAPTCHA = SERVER_IP_PUBLIC_UER_CENTER + "captcha?";
    /**
     * 注册
     **/
    public static final String SERVER_IP_REGISTER = SERVER_IP_PUBLIC_UER_CENTER + "register?";
    /**
     * 登录
     **/
    public static final String SERVER_IP_LOGIN = SERVER_IP_PUBLIC_UER_CENTER + "login?";
    /**
     * 个人信息
     **/
    public static final String SERVER_IP_INFORMATION = SERVER_IP_PUBLIC_UER_CENTER + "information?";
    /**
     * 修改个人信息
     **/
    public static final String SERVER_IP_INFORMATION_UPDATE = SERVER_IP_PUBLIC_UER_CENTER + "information/update?";
    /**
     * 修改密码
     **/
    public static final String SERVER_IP_PASSWORD_UPDATE = SERVER_IP_PUBLIC_UER_CENTER + "password/update?";
    /**
     * 忘记密码
     **/
    public static final String SERVER_IP_PASSWORD_RESET = SERVER_IP_PUBLIC_UER_CENTER + "password/reset?";
    /**
     * 意见反馈
     **/
    public static final String SERVER_IP_FEEDBACK_SAVE = SERVER_IP_PUBLIC_UER_CENTER + "feedback/save?";
    /**
     * 修改手机
     **/
    public static final String SERVER_IP_MOBILE_UPDATE = SERVER_IP_PUBLIC_UER_CENTER + "mobile/update?";
    /**
     * 注销
     **/
    public static final String SERVER_IP_LOGOUT = SERVER_IP_PUBLIC_UER_CENTER + "logout?";

    /**
     * poi 地理搜索
     **/
    public static final String SERVER_IP_POI = "poi/v1.0/c/places?";
    /**
     * 航班查询 通过航班号
     **/
    public static final String SERVER_IP_FLIGHTS_BY_NO = "flight/v1.0/c/flights?";
    /**
     * 航班查询 通过城市
     **/
    public static final String SERVER_IP_FLIGHTS_BY_CITY = "flight/v1.0/c/city/flights?";
    /**
     * 机场 暂时不用，使用DB
     **/
    public static final String SERVER_IP_AIRPORT = "price/v1.0/c/airports?";
    /**
     * 城市 暂时不用，使用DB
     **/
    public static final String SERVER_IP_CITY = "price/v1.0/c/cities?";


    //--------查询价格-------

    /**
     * 查询价格 接机
     **/
    public static final String SERVER_IP_PRICE_PICKUP = "price/v1.0/c/airportPickupPrice?";
    /**
     * 查询价格 送机
     **/
    public static final String SERVER_IP_PRICE_TRANSFER = "price/v1.0/c/airportTransferPrice?";
    /**
     * 查询价格 日租包车
     **/
    public static final String SERVER_IP_PRICE_DAILY = "price/v1.1/c/dailyPrice?";
    /**
     * 查询价格 单次用车
     **/
    public static final String SERVER_IP_PRICE_SINGLE = "price/v1.0/c/singlePrice?";
    /**
     * 查询价格 SKU
     **/
    public static final String SERVER_IP_PRICE_SKU = "price/v1.0/c/goodsPrice?";

    //-------订单类--------
    public static final String SERVER_IP_TRADE = "trade/v1.0/c/order/";

    public static final String SERVER_IP_TRADE_1_1 = "trade/v1.1/c/order/";
    /**
     * 提交订单 接机
     **/
    public static final String SERVER_IP_SUBMIT_PICKUP = UrlLibs.SERVER_IP_TRADE + "pickup?";
    /**
     * 提交订单 送机
     **/
    public static final String SERVER_IP_SUBMIT_TRANSFER = UrlLibs.SERVER_IP_TRADE + "transfer?";
    /**
     * 提交订单 日租包车
     **/
    public static final String SERVER_IP_SUBMIT_DAILY = UrlLibs.SERVER_IP_TRADE_1_1 + "daily?";
    /**
     * 提交订单 单次用车
     **/
    public static final String SERVER_IP_SUBMIT_SINGLE = UrlLibs.SERVER_IP_TRADE + "single?";
    /**
     * 取消订单
     **/
    public static final String SERVER_IP_ORDER_CANCEL = UrlLibs.SERVER_IP_TRADE + "cancel?";
    /**
     * 修改订单
     **/
    public static final String SERVER_IP_ORDER_EDIT = UrlLibs.SERVER_IP_TRADE_1_1 + "edit?";
    /**
     * 订单列表
     **/
    public static final String SERVER_IP_ORDER_LIST = UrlLibs.SERVER_IP_TRADE_1_1 + "list?";
    /**
     * IM中查询历史订单
     */
    public static final String SERVER_IP_ORDER_HISTORY = "trade/v1.0/c/order/list/history?";
    /**
     * 订单详情
     **/
    public static final String SERVER_IP_ORDER_DETAIL = UrlLibs.SERVER_IP_TRADE_1_1 + "detail?";
    /**
     * 订单增项费用
     **/
    public static final String SERVER_IP_ORDER_OVER_PRICE = UrlLibs.SERVER_IP_TRADE + "cost/additional?";

    /**
     * 对车导评价
     **/
    public static final String SERVER_IP_GUIDES_COMMENTS = SERVER_IP_TRADE + "evaluate?";

    /**
     * 订单支付 支付宝
     **/
    public static final String SERVER_IP_ORDER_PAY_ID = "trade/v1.0/c/pay/getmobilepayurl?";

    /**
     * 优惠券
     **/
    public static final String SERVER_IP_COUPONS = "marketing/v1.0/c/coupons?";
    /**
     * 优惠券 绑定
     **/
    public static final String SERVER_IP_COUPONS_BIND = "marketing/v1.0/c/coupons/bind?";

    /**
     * 图片上传
     **/
    public static final String SERVER_IP_PIC_UPLOAD = "file/v1.0/upload?";

    /**
     * IM token
     **/
    public static final String SERVER_IP_IM_TOKEN = "communication/v1.0/e/im/token?";
    /**
     * IM 通知更新为已读
     **/
    public static final String SERVER_IP_IM_UPDATE = "communication/v2.0/c/im/clear?";
    /**
     * IM 通知更新为已读
     **/
    public static final String SERVER_IP_IM_TOKEN_UPDATE = "trade/v1.0/c/order/im/token/update?";

    /**
     * IM聊天界面的订单数据
     */
    public static final String SERVER_IP_IM_ORDER_LIST = "trade/v1.0/c/order/list/im";
}
