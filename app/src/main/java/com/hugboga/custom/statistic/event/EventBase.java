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

}
