package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqiang on 17/8/3.
 */

public class HomeCityItemVo implements Serializable {
    public String goodsNo	= "";//	商品NO
    public String goodsName	= "";//	商品名称
    public String recommendedReason ="";//		推荐语
    public int guidesNum	= 0;//	可服务司导数
    public int purchases	= 0;//	已购买数
    public int perPrice	= 0;//	人均单价
    public String goodsPic	 = "";//	商品图片
    public String guidePic = "";//司导头像
    public String goodsDetailUrl = ""; //跳转

    public int isCollected;     // 非接口返回,本地判断用,是否收藏过线路,1收藏 0 未收藏

    public String guideId;//		司导ID（城市推荐为1, 推荐司导时使用）
    public String guideName;//		司导姓名（城市推荐为1, 推荐司导时使用）
    public String guideCover;//		司导主页banner大图（城市推荐为1, 推荐司导时使用）
    public String guideAvatar;//		司导头像（城市推荐为1, 推荐司导时使用）
    public int guideCityId;//		司导所在城市ID（城市推荐为1, 推荐司导时使用）
    public String guideCityName;//		司导所在城市名称（城市推荐为1, 推荐司导时使用）
    public int guideCommentNum;//		评论数（城市推荐为1, 推荐司导时使用）
    public double guideServiceStar;//		星级分数（城市推荐为1, 推荐司导时使用）
    public List<String> guideSkillLabelNames;//		特殊技能标签集合（城市推荐为1, 推荐司导时使用）
    public String guideHomeDesc;//		司导个人简介（城市推荐为1, 推荐司导时使用）
    public String guideLangString;//		语言信息（城市推荐为1, 推荐司导时使用）
    public String guideOrderUrl;//		找他预定URL（城市推荐为1, 推荐司导时使用）
}
