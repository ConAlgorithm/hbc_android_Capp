package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by qingcha on 16/7/16.
 */
public class EvaluateData implements Serializable{
    //30元 + 5% × 好友首单总金额
    public String commentTipParam1;//提示参数1 600元
    public String commentTipParam2;//提示参数2 30元
    public String commentTipParam3;//提示参数3 5%
    public String wechatShareUrl;//分享信息：url
    public String wechatShareTitle;//分享信息：标题
    public String wechatShareHeadSrc;//分享信息：头图src
    public String wechatShareContent;//分享内容
}
