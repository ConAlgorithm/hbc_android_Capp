package com.hugboga.custom.data.bean;

/**
 * 私信订单信息
 * Created by ZHZEPHI on 2016/3/4.
 */
public class ChatOrderBean implements IBaseBean {

    public int orderStatus; //状态中文
    public String status;
    public String orderNo; //订单号
    public String serviceTime; //服务时间
    public String serviceEndTime; //服务结束时间
    public String type; //订单类型
    public String startAddress; //开始地址
    public String destAddress; //结束地址

}
