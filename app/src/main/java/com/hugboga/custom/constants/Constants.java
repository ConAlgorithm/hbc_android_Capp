package com.hugboga.custom.constants;

import android.os.Environment;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.PromiseBean;
import com.hugboga.custom.data.net.UrlLibs;

import java.io.File;
import java.util.HashMap;

public final class Constants {

    //SDCard path
    public static final String IMAGE_DIR = Environment.getExternalStorageDirectory() + File.separator + "huangbaocheC";
    public static final String HEAD_IMAGE = "head.jpg";
    public static final String HEAD_IMAGE_NEW = "head_new.jpg";
    public static final String VERSION_FILE = "huangbaoche_C.apk";

    public static final int BUSINESS_TYPE_PICK = 1;//接机
    public static final int BUSINESS_TYPE_SEND = 2;//送机
    public static final int BUSINESS_TYPE_DAILY = 3;//日租
    public static final int BUSINESS_TYPE_RENT = 4;//次租
    public static final int BUSINESS_TYPE_COMMEND = 5;//固定路线
    public static final int BUSINESS_TYPE_RECOMMEND = 6;//推荐线路
    public static final int BUSINESS_TYPE_COMBINATION = 888;//组合单订单类型

    public static final int BUSINESS_TYPE_HOME = 8;//首页SKU

    public static final int BUSINESS_TYPE_DAILY_SHORT = 6;// 市内包车
    public static final int BUSINESS_TYPE_DAILY_LONG = 7;// 跨城市包车
    public static final int BUSINESS_TYPE_OTHER = -1;//其他

    public static final int PAY_STATE_ALIPAY = 1;//支付宝
    public static final int PAY_STATE_WECHAT = 2;//微信
    public static final int PAY_STATE_BANK = 3;//银行

    public static final String SERVER_TIME_END = "23:59:59";

    public static final String PARAMS_DATA = "data";
    public static final String PARAMS_ID = "id";
    public static final String PARAMS_ACTION = "action";
    public static final String PARAMS_TYPE = "type";
    public static final String PARAMS_TAG = "tag";
    public static final String PARAMS_SOURCE = "source";
    public static final String SOURCE_CLASS = "source_class";
    public static final String PARAMS_SOURCE_DETAIL = "source_detail";
    public static final String PARAMS_ORDER_NO = "orderNo";
    public static final String PARAMS_ORDER_TYPE = "orderType";
    public static final String PARAMS_START_CITY_BEAN = "startCityBean";
    public static final String PARAMS_SECKILLS = "seckills";
    public static final String PARAMS_GUIDE = "guide";
    public static final String PARAMS_SEARCH_KEYWORD = "keyword";

    public static final String PARAMS_CITY_ID = "cityId";

    public static final int DEFAULT_PAGESIZE = 20;
    public static final String REQUEST_SOURCE = "c";
    public static final int REQUEST_CHANNEL_ID = 18;

    public static final String CHANNEL_GOOGLE_PLAY = "10007";
    public static final String CHANNEL_OFFICIAL = "10000";
    public static final int EVALUATE_OK = 1000;
    public static final String CLICKED_SINGLE_PICK_SEND = "clicked_single_pick_send";
    public static final String CLICKED_PICK_SEND = "clicked_picked_send";

    public static final String[][] TaiCityNames = {{"曼谷", UrlLibs.H5_TAI_MANGU},
            {"普吉", UrlLibs.H5_TAI_PUJIDAO},
            {"清迈", UrlLibs.H5_TAI_QINGMAI},
            {"苏梅岛", UrlLibs.H5_TAI_SUMEIDAO}};
    /*
        客服电话
         */
    public static final String CALL_NUMBER_IN = "400-610-0009";//R.string.domestic_phone
    public static final String CALL_NUMBER_OUT = "+861059715436";//R.string.overseas_phone
    /**
     * title
     */
    public static HashMap<Integer, Integer> TitleMap = new HashMap<Integer, Integer>();
    /**
     * title
     */
    public static HashMap<Integer, String> TitleMap2 = new HashMap<Integer, String>();
    /**
     * 颜色
     */
//    public static HashMap<Integer, Integer> BgColors = new HashMap<Integer, Integer>();
    /**
     * 送达目的地
     */
    public static HashMap<String, Integer> arrivalMap = new HashMap<String, Integer>();


    /**
     * 车辆类型
     */
    public static final int CAR_TYPE_COMMON = 1;//经济型
    public static final int CAR_TYPE_COMFORT = 2;//舒适型
    public static final int CAR_TYPE_GORGEOUSNESS = 3;//豪华型
    public static final int CAR_TYPE_EXTRAVAGANT = 4;//奢华型


    /**
     * 座位
     */
    public static final int CAR_SEAT_5 = 1;
    public static final int CAR_SEAT_7 = 2;
    public static final int CAR_SEAT_9 = 3;
    public static final int CAR_SEAT_12 = 4;

    /**
     * 车辆类型
     */
    public static HashMap<Integer, String> CarTypeMap = new HashMap<Integer, String>();
    /***
     * 座位
     */
    public static HashMap<Integer, Integer> CarSeatMap = new HashMap<Integer, Integer>();

    /**
     * 座位信息，乘坐人数，行李数
     **/
    public static HashMap<Integer, Integer[]> CarSeatInfoMap = new HashMap<Integer, Integer[]>();
    /**
     * 代表车型 根据 类型	座位
     **/
    public static HashMap<Integer, HashMap<Integer, String>> CarDescInfoMap = new HashMap<Integer, HashMap<Integer, String>>();

    /**
     * 车辆类型图片
     */
    public static HashMap<String, Integer> CarPicMap = new HashMap<String, Integer>();

    /**
     * 签证状态。1-国内签证；2-落地签证；0-未确定
     */
    public static HashMap<Integer, String> VisaInfoMap = new HashMap<Integer, String>();

    /**
     * 车辆类型图片
     */
//    public static HashMap<Integer, Integer> BtnBg = new HashMap<Integer, Integer>();

    /**
     * 承诺
     */
    public static HashMap<Integer, PromiseBean> PromiseMap = new HashMap<Integer, PromiseBean>();

    /**
     * 儿童座椅
     */
    public static HashMap<Integer, String> ChildrenSeatMap = new HashMap<Integer, String>();

    /**
     * Query目的地搜索部分
     */
    public static final int QUERY_TXT_LENGTH_LIMIT = 2; //目的地搜索部分输入字符自动搜索阈值
    public static final long QUERY_DELAY_MILLIS = 1000; //目的地搜索部分输入字符后延迟搜索时长


    static {


        arrivalMap.put("交通", R.mipmap.list_traffic);
        arrivalMap.put("酒店", R.mipmap.list_hotels);
        arrivalMap.put("景点", R.mipmap.list_scenic);
//
        TitleMap.put(Constants.BUSINESS_TYPE_PICK, R.string.title_pick);
        TitleMap.put(Constants.BUSINESS_TYPE_SEND, R.string.title_send);
        TitleMap.put(Constants.BUSINESS_TYPE_DAILY, R.string.title_daily_in_town);
        TitleMap.put(Constants.BUSINESS_TYPE_DAILY_SHORT, R.string.title_daily_out_town);
        TitleMap.put(Constants.BUSINESS_TYPE_RENT, R.string.title_rent);
        TitleMap.put(Constants.BUSINESS_TYPE_DAILY_LONG, R.string.title_daily_out_town);
        TitleMap.put(Constants.BUSINESS_TYPE_COMMEND, R.string.title_commend);
        TitleMap.put(Constants.BUSINESS_TYPE_OTHER, R.string.title_other);

        TitleMap2.put(Constants.BUSINESS_TYPE_PICK, "接机费用");
        TitleMap2.put(Constants.BUSINESS_TYPE_SEND, "送机费用");
        TitleMap2.put(Constants.BUSINESS_TYPE_DAILY, "包车费用");
        TitleMap2.put(Constants.BUSINESS_TYPE_RENT, "单次用车费用");
        TitleMap2.put(Constants.BUSINESS_TYPE_COMMEND, "精品线路费用");

//        BgColors.put(Constants.BUSINESS_TYPE_PICK, R.color.basic_pick_toolbar_color);
//        BgColors.put(Constants.BUSINESS_TYPE_SEND, R.color.basic_send_toolbar_color);
//        BgColors.put(Constants.BUSINESS_TYPE_DAILY, R.color.basic_daily_toolbar_color);
//        BgColors.put(Constants.BUSINESS_TYPE_RENT, R.color.basic_rent_toolbar_color);
//        BgColors.put(Constants.BUSINESS_TYPE_COMMEND, R.color.basic_commend_toolbar_color);
//        BgColors.put(Constants.BUSINESS_TYPE_OTHER, R.color.basic_other_toolbar_color);

        CarTypeMap.put(CAR_TYPE_COMMON, "经济");
        CarTypeMap.put(CAR_TYPE_COMFORT, "舒适");
        CarTypeMap.put(CAR_TYPE_GORGEOUSNESS, "豪华");
        CarTypeMap.put(CAR_TYPE_EXTRAVAGANT, "奢华");

        ChildrenSeatMap.put(1, "婴儿（0-1岁）");
        ChildrenSeatMap.put(2, "幼儿（1-4岁）");
        ChildrenSeatMap.put(3, "学童（4-7岁）");
        ChildrenSeatMap.put(4, "儿童（7-12岁）");

        CarSeatMap.put(CAR_SEAT_5, 5);
        CarSeatMap.put(CAR_SEAT_7, 7);
        CarSeatMap.put(CAR_SEAT_9, 9);
        CarSeatMap.put(CAR_SEAT_12, 12);

        HashMap<Integer, String> tmpCarinfo = new HashMap<Integer, String>();
        tmpCarinfo.put(CAR_SEAT_5, "大众Polo、尼桑Livina、丰田卡罗拉、现代伊兰特、雪佛兰科鲁兹");
        tmpCarinfo.put(CAR_SEAT_7, "马自达5、雪佛兰科帕奇、丰田Fortuner");
        tmpCarinfo.put(CAR_SEAT_9, "现代H1、尼桑Urvan");
        tmpCarinfo.put(CAR_SEAT_12, "现代Starex");
        CarDescInfoMap.put(CAR_TYPE_COMMON, tmpCarinfo);

        tmpCarinfo = new HashMap<Integer, String>();
        tmpCarinfo.put(CAR_SEAT_5, "丰田锐志、丰田Venza、三菱欧蓝德、斯巴鲁森林人、Mini Countryman");
        tmpCarinfo.put(CAR_SEAT_7, "丰田汉兰达、本田奥德赛、现代I800、福特Freestar");
        tmpCarinfo.put(CAR_SEAT_9, "大众凯路威、标致Expert、起亚Carnival、福特Transit");
        tmpCarinfo.put(CAR_SEAT_12, "丰田海狮、雪佛兰Express 3500、大众Crafter");
        CarDescInfoMap.put(CAR_TYPE_COMFORT, tmpCarinfo);

        tmpCarinfo = new HashMap<Integer, String>();
        tmpCarinfo.put(CAR_SEAT_5, "奔驰SLK、沃尔沃XC60、雷克萨斯ES350、宝马X5");
        tmpCarinfo.put(CAR_SEAT_7, "奔驰Viano、丰田普瑞维亚、尼桑Elgrand、丰田Alphard、路虎发现");
        tmpCarinfo.put(CAR_SEAT_9, "奔驰Vito、Vauxhall Vivaro");
        tmpCarinfo.put(CAR_SEAT_12, "福特Transit等同级车");
        CarDescInfoMap.put(CAR_TYPE_GORGEOUSNESS, tmpCarinfo);

        tmpCarinfo = new HashMap<Integer, String>();
        tmpCarinfo.put(CAR_SEAT_5, "保时捷卡宴、林肯MKX、宝马7系");
        tmpCarinfo.put(CAR_SEAT_7, "凯迪拉克凯雷德、雷克萨斯GX470、福特Flex");
        tmpCarinfo.put(CAR_SEAT_9, "福特征服者9座/GMC Yukon XL9座等同级车");
        tmpCarinfo.put(CAR_SEAT_12, "奔驰Sprinter等同级车");
        CarDescInfoMap.put(CAR_TYPE_EXTRAVAGANT, tmpCarinfo);

        CarSeatInfoMap.put(CarSeatMap.get(CAR_SEAT_5), new Integer[]{4, 3});
        CarSeatInfoMap.put(CarSeatMap.get(CAR_SEAT_7), new Integer[]{6, 4});
        CarSeatInfoMap.put(CarSeatMap.get(CAR_SEAT_9), new Integer[]{8, 5});
        CarSeatInfoMap.put(CarSeatMap.get(CAR_SEAT_12), new Integer[]{11, 6});

//		CarPicMap.put("5_1",R.mipmap.choose_5seat_1);
//		CarPicMap.put("7_1",R.mipmap.choose_7seat_1);
//		CarPicMap.put("9_1",R.mipmap.choose_9seat_1);
//		CarPicMap.put("12_1",R.mipmap.choose_12seat_1);
//		CarPicMap.put("5_2",R.mipmap.choose_5seat_2);
//		CarPicMap.put("7_2",R.mipmap.choose_7seat_2);
//		CarPicMap.put("9_2",R.mipmap.choose_9seat_2);
//		CarPicMap.put("12_2",R.mipmap.choose_12seat_2);
//		CarPicMap.put("5_3",R.mipmap.choose_5seat_3);
//		CarPicMap.put("7_3",R.mipmap.choose_7seat_3);
//		CarPicMap.put("9_3",R.mipmap.choose_9seat_3);
//		CarPicMap.put("12_3",R.mipmap.choose_12seat_3);
//		CarPicMap.put("5_4",R.mipmap.choose_5seat_4);
//		CarPicMap.put("7_4",R.mipmap.choose_7seat_4);
//		CarPicMap.put("9_4",R.mipmap.choose_9seat_4);
//		CarPicMap.put("12_4",R.mipmap.choose_12seat_4);

        VisaInfoMap.put(0, "签证未确定");
        VisaInfoMap.put(1, "国内签证");
        VisaInfoMap.put(2, "落地签证");

//        BtnBg.put(BUSINESS_TYPE_PICK, R.drawable.btn_blue);
//        BtnBg.put(BUSINESS_TYPE_SEND, R.drawable.btn_green);
//        BtnBg.put(BUSINESS_TYPE_DAILY, R.drawable.btn_orange);
//        BtnBg.put(BUSINESS_TYPE_RENT, R.drawable.btn_pink);
//        BtnBg.put(BUSINESS_TYPE_COMMEND, R.drawable.btn_purple);

        PromiseMap.put(0, new PromiseBean(R.mipmap.pro_all, R.string.promise_title_all, R.string.promise_content_all));
        PromiseMap.put(1, new PromiseBean(R.mipmap.pro_wait, R.string.promise_title_wait, R.string.promise_content_wait));
        PromiseMap.put(2, new PromiseBean(R.mipmap.pro_app, R.string.promise_title_app, R.string.promise_content_app));
        PromiseMap.put(3, new PromiseBean(R.mipmap.pro_pay, R.string.promise_title_pay, R.string.promise_content_pay));
        PromiseMap.put(4, new PromiseBean(R.mipmap.pro_safe, R.string.promise_title_safe, R.string.promise_content_safe));
    }
}
