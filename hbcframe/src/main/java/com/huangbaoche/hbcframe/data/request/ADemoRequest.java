package com.huangbaoche.hbcframe.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.xutils.ex.DbException;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.x;

import java.util.Map;

/**
 * Created by admin on 2016/2/25.
 */


@HttpRequest(
        path = "a")
public class ADemoRequest extends BaseRequest {


        public ADemoRequest(Context context) {
                super(context);
        }

        @Override
        public Map getDataMap() {
                return null;
        }

        @Override
        public ImplParser getParser() {
                return null;
        }
}
