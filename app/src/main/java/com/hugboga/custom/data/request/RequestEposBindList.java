package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.epos.EposBindList;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseEposBindList;

import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * 绑定的卡信息请求
 * Created by HONGBO on 2017/10/28 16:08.
 */
@HttpRequest(path = UrlLibs.API_EPOS_BIND_LIST, builder = NewParamsBuilder.class)
public class RequestEposBindList extends BaseRequest<EposBindList> {

    public RequestEposBindList(Context context) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("peUserId", UserEntity.getUser().getUserId(context)); //用户ID
    }

    @Override
    public String getUrlErrorCode() {
        return "193";
    }

    @Override
    public ImplParser getParser() {
        return new ParseEposBindList();
    }
}
