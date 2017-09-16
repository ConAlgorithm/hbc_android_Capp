package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqiang on 17/8/3.
 */

public class HomeAlbumRelItemVo implements Serializable {

    //goods
    public String goodsNo	= "";//	商品NO
    public String goodsName="";//	商品名称
    public String goodsPic	="";//	商品图片
    public int perPrice	= 0;//	商品人均价
    public String goodsDetailUrl = "";//商品跳转

    //guides
    public String guideId;//		司导ID（专辑为2，司导时使用）
    public String guideName;//		司导姓名（专辑为2，司导时使用）
    public String guideCover;//		司导主页banner大图（专辑为2，司导时使用）
    public String guideAvatar;//		司导头像（专辑为2，司导时使用）
    public int guideCityId;//		司导所在城市ID（专辑为2，司导时使用）
    public String guideCityName;//		司导所在城市名称（专辑为2，司导时使用）
    public int guideCommentNum;//	评论数（专辑为2，司导时使用）
    public double guideServiceStar;//		星级分数（专辑为2，司导时使用）
    public List<String> guideSkillLabelNames;//		特殊技能标签集合（专辑为2，司导时使用）
    public String guideHomeDesc;//		司导个人简介（专辑为2，司导时使用）
    public String guideLangString;//		语言信息（专辑为2，司导时使用）
    public String guideOrderUrl;//  找他预定URL（专辑为2，司导时使用）
    public int isCollected; //1:收藏 0:未收藏

}
