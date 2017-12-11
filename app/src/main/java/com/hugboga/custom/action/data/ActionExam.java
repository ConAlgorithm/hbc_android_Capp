package com.hugboga.custom.action.data;

import java.io.Serializable;

/**
 * Action附属属性
 * Created by HONGBO on 2017/12/11 16:50.
 */

public class ActionExam implements Serializable {
    public boolean isShareBtn; //是否动态获取分享配置
    public String shareNo; //动态分享请求码
}
