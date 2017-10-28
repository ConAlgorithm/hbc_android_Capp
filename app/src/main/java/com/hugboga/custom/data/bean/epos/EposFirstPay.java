package com.hugboga.custom.data.bean.epos;

import java.io.Serializable;

/**
 * 首次绑卡支付
 * Created by HONGBO on 2017/10/28 18:12.
 */

public class EposFirstPay implements Serializable {
    private String r0_Cmd;
    private String payNo;
    private String r6_Order;
    //SUCCESS(1, "提交成功"), FAILED(2, "提交失败"), VERIFY_FACTOR(3, "加验要素"),VERIFY_CODE(4, "短信验证"),VERIFY_FACTOR_CODE(5, "加验要素及短信验证");
    private String eposPaySubmitStatus;
    private String r1_Code;
    private String hmac;
    private String r2_TrxId;
    private String hmac_safe;
    private String errorMsg;
    private String ro_BankOrderId;

    /*
    加验要素
     CVV(1, "cvv2"), AVALIDDATE(2, "avalidDate"), NAME(3, "name"), PHONE(4, "phone"), CREDCODE(5, "credCode"), CARDNO(6, "cardNo");
    //5、身份证号  6、卡号
     */
}
