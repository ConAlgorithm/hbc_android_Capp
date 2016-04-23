package com.hugboga.custom.data.bean;

import java.util.List;

/**
 * Created by dyt on 16/4/23.
 */
public class InsureBean {
//    {"data":{"totalSize":2,"resultBean":[
//        {"birthday":"2013-10-10","insuranceUserId":"IU91440316911","name":"李达","passportNo":"G123","sex":1,"userId":"123","userStatus":1},
//        {"birthday":"2013-10-10","createtime":"2016-04-12 17:36:38","insuranceUserId":"IU91440316919","name":"李达","passportNo":"G12311","sex":1,"source":1,"updatetime":"2016-04-12 17:36:38","userId":"123","userStatus":1}]},
//        "status":200}

    public int totalSize;
    public List<InsureResultBean> resultBean;

}
