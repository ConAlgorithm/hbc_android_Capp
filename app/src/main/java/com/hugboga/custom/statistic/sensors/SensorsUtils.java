package com.hugboga.custom.statistic.sensors;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.activity.BargainActivity;
import com.hugboga.custom.activity.DailyWebInfoActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.statistic.bean.EventPayBean;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONObject;

/**
 * Created by on 16/11/25.
 * 神策统计
 */
public class SensorsUtils {

    //支付结果
    public static void setSensorsPayResultEvent(EventPayBean eventPayBean, String payMethod, boolean payResult) {
        try {
            String skuType = "";
            switch (eventPayBean.orderType) {
                case 1:
                    skuType = "接机";
                    break;
                case 2:
                    skuType = "送机";
                    break;
                case 3:
                    skuType = "定制包车游";
                    break;
                case 4:
                    skuType = "单次接送";
                    break;
                case 5:
                    skuType = "固定线路";
                    break;
                case 6:
                    skuType = "推荐线路";
                    break;

            }
            JSONObject properties = new JSONObject();
            properties.put("sku_type", skuType);
            properties.put("order_id", eventPayBean.orderId);
            properties.put("is_appoint_guide", eventPayBean.isSelectedGuide);//指定司导下单
            properties.put("price_total", eventPayBean.shouldPay);//费用总计
            properties.put("price_coupon", "" + eventPayBean.couponPrice);//使用优惠券
            properties.put("price_tra_fund", eventPayBean.travelFundPrice);//使用旅游基金
            properties.put("price_actually", eventPayBean.actualPay);//实际支付金额
            properties.put("pay_method", payMethod);//支付方式
            properties.put("pay_result", payResult);
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("pay_result", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //点击支付
    public static void setSensorsPayOnClickEvent(EventPayBean eventPayBean, String payMethod) {
        try {
            String skuType = "";
            switch (eventPayBean.orderType) {
                case 1:
                    skuType = "接机";
                    break;
                case 2:
                    skuType = "送机";
                    break;
                case 3:
                    skuType = "定制包车游";
                    break;
                case 4:
                    skuType = "单次接送";
                    break;
                case 5:
                    skuType = "固定线路";
                    break;
                case 6:
                    skuType = "推荐线路";
                    break;

            }
            JSONObject properties = new JSONObject();
            properties.put("sku_type", skuType);
            properties.put("order_id", eventPayBean.orderId);
            properties.put("is_appoint_guide", eventPayBean.isSelectedGuide);//指定司导下单
            properties.put("price_total", eventPayBean.shouldPay);//费用总计
            properties.put("price_coupon", "" + eventPayBean.couponPrice);//使用优惠券
            properties.put("price_tra_fund", eventPayBean.travelFundPrice);//使用旅游基金
            properties.put("price_actually", eventPayBean.actualPay);//实际支付金额
            properties.put("pay_method", payMethod);//支付方式
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("buy_pay", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //分享
    public static void setSensorsShareEvent(String type, String _source) {
        try {
            String source = "";
            if (_source.equals(TravelFundActivity.class.getSimpleName())) {
                source = "旅游基金";
            } else if (_source.equals(DailyWebInfoActivity.class.getSimpleName())) {
                source = "包车游详情";
            } else if (_source.equals(BargainActivity.class.getSimpleName())) {
                source = "砍价";
            } else if (_source.equals(SkuDetailActivity.class.getSimpleName())) {
                source = "商品详情";
            }

            JSONObject properties = new JSONObject();
            properties.put("share_channelId", type);
            properties.put("share_content", source);
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("share", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //联系客服
    public static void setSensorsServiceEvent(int sourceType, int _type) {
        try {
            String webTitle = "其它";
            switch (sourceType) {
                case UnicornServiceActivity.SourceType.TYPE_CHARTERED:
                    webTitle = "包车游详情";
                    break;
                case UnicornServiceActivity.SourceType.TYPE_LINE:
                    webTitle = "商品详情";
                    break;
                case UnicornServiceActivity.SourceType.TYPE_ORDER:
                    webTitle = "订单";
                    break;
            }

            String typeStr = "";
            switch (_type) {
                case 0:
                    typeStr = "在线";
                    break;
                case 1:
                    typeStr = "境内";
                    break;
                case 2:
                    typeStr = "境外";
                    break;
            }

            JSONObject properties = new JSONObject();
            properties.put("web_title", webTitle);
            properties.put("cs_type", typeStr);
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("contact_servicedesk", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
