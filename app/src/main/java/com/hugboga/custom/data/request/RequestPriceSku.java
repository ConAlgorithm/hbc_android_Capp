package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserCarList;
import com.hugboga.custom.utils.Config;

import org.xutils.http.annotation.HttpRequest;

import java.util.TreeMap;

/**
 * 请求车型
 * Created by admin on 2016/3/20.
 */

@HttpRequest(path = UrlLibs.SERVER_IP_PRICE_SKU, builder = NewParamsBuilder.class)
public class RequestPriceSku extends BaseRequest<CarListBean> {

    /**
     * @param context
     * @param goodsNo     商品ID
     * @param serviceDate 服务时间 yyyy-MM-dd HH:mm:ss
     */
    public RequestPriceSku(Context context, String goodsNo, String serviceDate,String cityId,int numOfRooms) {
        super(context);
        map = new TreeMap();
        map.put("goodsNo", goodsNo);
        map.put("serviceDate", serviceDate);
        map.put("channelId", Config.channelId);
        map.put("cityId", cityId);
        if(numOfRooms != 0) {
            map.put("numOfRooms", numOfRooms);
        }
    }

    @Override
    public ImplParser getParser() {
        return new ParserCarList();
    }


    @Override
    public String getUrlErrorCode() {
        return "40057";
    }
}
