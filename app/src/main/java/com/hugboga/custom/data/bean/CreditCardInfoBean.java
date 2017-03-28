package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/8.
 * 银行实体类
 */

public class CreditCardInfoBean implements Serializable{


    public String createTime;//创建时间
    public String creditCardNo;//银行卡号
    public Integer id;
    public String idCardNo;//用户身份证号
    public String telNo;//用户手机号
    public String updateTime;
    public String userId;
    public String userName;//用户名称
    public String accType;//卡的类型  01：借记卡，02：信用卡
    public String bandId;//银行ID  添加新卡信用卡的时候用
    public String bankId;//银行ID  支付平台，查询已绑定卡的时候用
    public String bankName;//银行名称

}
