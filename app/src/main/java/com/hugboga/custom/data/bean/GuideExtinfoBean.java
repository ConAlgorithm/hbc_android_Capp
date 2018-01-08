package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by qingcha on 17/4/24.
 *
 * http://wiki.hbc.tech/display/hbcAdmin/C4.0.0#C4.0.0-8.分享司导个人页附加信息
 */
public class GuideExtinfoBean implements Serializable{
    public String guideId;          // 司导标识
    public String guideName;        // 司导名字
    public String avatar;           // 司导头像
    public String avatarUrl;        // 司导头像小图地址
    public String avatarUrlL;       // 司导头像大图地址
    public Integer cityId;          // 司导所在城市标识
    public String cityName;         // 司导所在城市名称
    public String homeDesc;         // 司导简介
    public Integer isCollected;     // 司导是否被收藏 1-是,0-否
    public String localTime;        // 司导当地时间,格式：yyyy-MM-dd HH:mm:ss
    public int accessible;          // 是否可联系司导 ，0-不可联系，1-可联系无状态，2-在线，3-休息，4-忙碌
    public Integer localTimezone;   // 司导所在城市时区
    public String neUserId;         // 司导聊天ID

}
