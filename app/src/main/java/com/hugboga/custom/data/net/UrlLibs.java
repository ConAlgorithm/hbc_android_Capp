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
    public static final String SERVER_IP_CHECK_APP_VERSION = "passport/v1.0/checkAppVersion?";
    //upload logs
    public static final String SERVER_IP_UPLOAD_LOGS = "pt/v1.0/log/stream?";

    //首页
    public static final String SERVER_IP_HOME = "basicdata/v1.0/p/home/citys/contents?";
    //SKU
    public static final String SERVER_IP_CITY_SKU = "goods/v1.0/p/home/cityGoods?";

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
     * 微信登录校验openid是否已注册
     **/
    public static final String GET_ACCESS_TOKEN = SERVER_IP_PUBLIC_UER_CENTER + "wechat/login/check/openid?";
    /**
     * 微信登录校验手机号是否填写
     **/
    public static final String WECHAT_CHECK_MOBILE = SERVER_IP_PUBLIC_UER_CENTER + "wechat/login/check/mobile?";
    /**
     * 微信绑定手机号
     **/
    public static final String WECHAT_BIND_MOBILE = SERVER_IP_PUBLIC_UER_CENTER + "wechat/bind/mobile?";
    /**
     * 微信登录通过code获取access_token
     **/
//    public static final String GET_ACCESS_TOKEN = "sns/oauth2/access_token?";
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
     * 微信绑定设置密码
     **/
    public static final String WECHAT_SET_PASSWORD = SERVER_IP_PUBLIC_UER_CENTER + "wechat/login/register/mobile?";
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
    public static final String SERVER_IP_TRADE_1_2 = "trade/v1.2/c/order/";

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
    public static final String SERVER_IP_SUBMIT_DAILY12 = UrlLibs.SERVER_IP_TRADE_1_2 + "daily?";

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

    //聊天
    public static final String SERVER_IP_CHAT_LIST = "communication/v2.0/c/im/list?";
    /**
     * IM 通知更新为已读
     **/
    public static final String SERVER_IP_IM_UPDATE = "communication/v2.0/c/im/clear?";
    /**
     * IM 通知更新
     **/
    public static final String SERVER_IP_IM_TOKEN_UPDATE = "communication/v2.0/c/im/token?";

    /**
     * IM聊天界面的订单数据
     */
    public static final String SERVER_IP_IM_ORDER_LIST = "trade/v1.0/c/order/list/im";
    /**
     * 获取用户注册信息 - 注册极光别名时调用该接口进行反馈（Android + IOS）
     */
    public static final String SERVER_IP_PUSH_TOKEN = "communication/v2.0/c/push/token";
    /**
     * Android APP 接到push未点击时，调用该接口进行反馈（Android）
     */
    public static final String SERVER_IP_PUSH_RECEIVE = "communication/v2.0/c/push/receive";
    /**
     * APP收到的push被点击时，调用该接口进行反馈（Android + IOS）
     */
    public static final String SERVER_IP_PUSH_CLICK = "communication/v2.0/c/push/click";

    /**
     * 获取用户优惠卷
     */
    public static final String GET_USER_COUPON = "trade/v1.0/c/order/home?";

    /**
     * 上传经纬度  http://api.dev.hbc.tech/poi/v1.0/e/city?longitude=1&latitude=2
     */
    public static final String UPLOAD_LOCATION = "poi/v1.0/e/city?";

    /**
     * 获取车辆信息
     */
    public static final String GET_CAR_INFOS = "price/v1.2/e/dailyPrice?";

    /**
     * 新增投保人
     */
    public static final String ADD_INSURE = "insurance/v1.0/c/user/add";

    /**
     * 查询投保人
     */
    public static final String GET_INSURE_LIST = "insurance/v1.0/c/user/list";

    /**
     * 删除投保人
     */
    public static final String DEL_INSURE_LIST = "insurance/v1.0/c/user/delete";

    /**
     * 编辑投保人
     */
    public static final String EDIT_INSURE_LIST = "insurance/v1.0/c/user/edit";

    /**
     * 提交投保人
     */
    public static final String SUBMIT_INSURE_LIST = "insurance/v1.0/c/insurance/submit";

}
