package com.huangbaoche.hbcframe.util;

import org.xutils.common.util.KeyValue;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by HONGBO on 2017/12/28 17:37.
 */

public class PairUtils {
    /**
     * 格式化请求参数
     *
     * @return
     */
    public static String getRequesetStr(List requestParams) {
        StringBuilder sb = new StringBuilder();
        TreeMap tm = new TreeMap();
        if (requestParams != null) {
            for (Object kv : requestParams) {
                if (kv instanceof KeyValue) {
                    KeyValue keyValue = (KeyValue) kv;
                    if (keyValue.value != null) {
                        tm.put(keyValue.key, keyValue.value);
                    }
                }
            }
            sb.append(tm.toString());
        }
        return sb.toString();
    }
}
