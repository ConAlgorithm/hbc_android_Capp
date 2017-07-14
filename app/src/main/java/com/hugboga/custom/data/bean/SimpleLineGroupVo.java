package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class SimpleLineGroupVo implements Serializable {
    int groupId;
    String groupName;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
