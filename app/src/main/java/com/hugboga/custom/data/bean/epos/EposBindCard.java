package com.hugboga.custom.data.bean.epos;

import java.io.Serializable;

/**
 * Epos绑定的卡信息
 * Created by HONGBO on 2017/10/28 16:14.
 */

public class EposBindCard implements Serializable {
    public String cardNo; //卡号 6225****6380
    public String bankName; //银行卡名称 招商银行信用卡
    public String cardType; //卡片类型 CREDIT_CARD
    public String bindId; //绑定ID f5/YD5FLFLnO6o/2NQJG0BEFPUljuKdb
}
