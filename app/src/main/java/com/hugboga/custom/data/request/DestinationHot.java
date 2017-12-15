package com.hugboga.custom.data.request;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.DestinationHotItemBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17/7/12.
 */
@HttpRequest(path = UrlLibs.API_DESTINATIONS_HOT, builder = NewParamsBuilder.class)
public class DestinationHot extends BaseRequest<ArrayList<DestinationHotItemBean>> {

    public DestinationHot(Context context) {
        super(context);
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_DESTINATIONS_HOT, new TypeToken<List<DestinationHotItemBean>>(){}.getType());
    }

    @Override
    public String getUrlErrorCode() {
        return "430169";
    }

}
