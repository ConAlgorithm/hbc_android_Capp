package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 17/3/3.
 */

public class CharterlItemBean implements Serializable {

    public CityBean startCityBean;
    public CityBean endCityBean;
    public ArrayList<CityRouteBean.Fence> startFence;
    public ArrayList<CityRouteBean.Fence> endFence;
}
