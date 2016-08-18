package com.hugboga.custom.statistic.event;

import java.util.HashMap;

/**
 * Created by qingcha on 16/8/18.
 */
public abstract class EventBase {

    public abstract String getEventId();

    public HashMap getParamsMap() {
        return null;
    }

    public String booleanTransform(int is) {
        return booleanTransform(is == 1);
    }

    public String booleanTransform(boolean is) {
        return is ? "是" : "否";
    }

}
