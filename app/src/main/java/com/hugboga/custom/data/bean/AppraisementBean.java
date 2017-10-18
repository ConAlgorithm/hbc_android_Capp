package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/6.
 */
public class AppraisementBean implements Serializable {
    public String orderNo;
    public String content;//内容
    public float totalScore;//总分
    public ArrayList<GuideLabels> guideLabels;

    public String commentTipParam1;//提示参数1 600元
    public String commentTipParam2;//提示参数2 30元
    public String commentTipParam3;//提示参数3 5%
    public String wechatShareUrl;//分享信息：url
    public String wechatShareTitle;//分享信息：标题
    public String wechatShareHeadSrc;//分享信息：头图src
    public String wechatShareContent;//分享内容

    public ArrayList<String> commentPic;//评价小图
    public ArrayList<String> commentPicL;//评价大图
    public String guideReply; //司导回复信息
    public int auditStatus;//1 = 下线
    public static class GuideLabels implements Serializable {
        public String name;
        public boolean checked;
    }
}
