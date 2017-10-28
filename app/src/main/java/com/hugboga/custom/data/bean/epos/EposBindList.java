package com.hugboga.custom.data.bean.epos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 绑定卡列表信息
 * Created by HONGBO on 2017/10/28 16:11.
 */

public class EposBindList implements Serializable {
    public String r0_Cmd;
    public String re_Identityty;
    public String r1_Code;
    public String hmac;
    public String re_Identityid;
    public String hmac_safe;
    public String errorMsg;
    public String re_BindList; //银行卡列表

    public List<EposBindCard> bindList = new ArrayList<>(); //接口解析是String类型，此自动需要手动解析赋值
}
