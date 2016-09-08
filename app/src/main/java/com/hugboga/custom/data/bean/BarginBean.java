package com.hugboga.custom.data.bean;

/**
 * Created on 16/9/7.
 */

import java.util.List;

public class BarginBean implements IBaseBean{
    public String bargainAmount;    //砍价总金额
    public int bargainTotal;        //砍价总个数
    public int seconds;             //砍价剩余时间   总秒数
    public String serviceCityName;  //服务城市名称
    public String userAvatar;       //用户头像
    public String userName;         //用户真实 名称
    public String cnstr;            //服务文案
    public List<BarginWebchatList> bargainWechatRspList;


}
