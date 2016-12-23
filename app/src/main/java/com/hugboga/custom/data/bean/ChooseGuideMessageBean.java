package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by qingcha on 16/12/23.
 */
public class ChooseGuideMessageBean implements Serializable{

    public int result;//指派结果 1-成功；-1，-2，-3，-4 都失败，错误原因对应message
    public String message;

    public boolean isSucceed() {
        return result == 1;
    }

}
