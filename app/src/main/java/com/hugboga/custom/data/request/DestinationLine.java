package com.hugboga.custom.data.request;

import android.content.Context;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.DestinationTabItemBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by zhangqiang on 17/7/12.
 */
@HttpRequest(path = UrlLibs.API_DESTINATIONS_LINE, builder = NewParamsBuilder.class)
public class DestinationLine extends BaseRequest<DestinationTabItemBean> {

    public DestinationLine(Context context, int destinationId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("destinationId", destinationId);
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_DESTINATIONS_LINE, DestinationTabItemBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "430168";
    }
}
