package com.hugboga.custom.data.bean.combination;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qingcha on 17/3/6.
 */

public class GroupParam implements Serializable{
    public GroupParentParam parent;            // 父单
    public List<GroupPickupParam> pickupList;  // 接机子单
    public List<GroupDailyParam> dailyList;    // 日租子单
    public List<GroupTransParam> transList;    // 送机子单
}
