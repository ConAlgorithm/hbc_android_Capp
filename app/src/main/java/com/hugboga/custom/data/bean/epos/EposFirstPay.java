package com.hugboga.custom.data.bean.epos;

import java.io.Serializable;

/**
 * 首次绑卡支付
 * Created by HONGBO on 2017/10/28 18:12.
 */

public class EposFirstPay implements Serializable {
    public String r0_Cmd;
    public String payNo;
    public String r6_Order;
    //SUCCESS(1, "提交成功"), FAILED(2, "提交失败"), VERIFY_FACTOR(3, "加验要素"),VERIFY_CODE(4, "短信验证"),VERIFY_FACTOR_CODE(5, "加验要素及短信验证");
    public String eposPaySubmitStatus;
    public String r1_Code;
    public String hmac;
    public String rp_NeedFactor; //加验要素内容，"cvv2,avalidDate "
    public String r2_TrxId;
    public String hmac_safe;
    public String needVaildFactors; //加验要素编号，"1,2,"
    public String errorMsg;
    public String ro_BankOrderId;

    /*
    加验要素
     CVV(1, "cvv2"), AVALIDDATE(2, "avalidDate"), NAME(3, "name"), PHONE(4, "phone"), CREDCODE(5, "credCode"), CARDNO(6, "cardNo");
    //5、身份证号  6、卡号
     */
}
