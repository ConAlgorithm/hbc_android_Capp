package com.hugboga.custom.data.bean;

import java.util.List;

/**
 * Created by ZHZEPHI on 2015/7/24.
 */
public class CouponBean implements IBaseBean{

    public String couponID;
    public Integer couponType; //优惠券类型；1-代金券；2-折扣券，3-包车折扣劵
    public String price; //显示价格
    public Double actualPrice; //实际支付金额
    public String startDate;
    public String endDate;
    public Integer couponStatus; //优惠券状态。1-可用；2-使用；-1-废弃；98-锁定
    public String content; //说明
    public String applyArea; //有效区域
    public String applyType; //服务类型
    public String applyCar; //适用车型
    public String applyRule; //满1000元可用/满1000公里可用/满10小时可用
    public Integer pageIndex; //优惠劵分页，每次获取历史优惠劵拿最大数请求
    public String batchName; //渠道名称
    public String applyCarClass;//座椅限制

    public List<CouponTitleContent> dataList;

}
