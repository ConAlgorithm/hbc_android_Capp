package com.hugboga.custom.data.bean.ai;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * AI初始化解析内容
 * Created by Administrator on 2017/12/1.
 */

public class FakeAIBean {
    public String askDuoDuoSessionID; //5e32b802-8c62-4673-b69f-03c975017d74
    @SerializedName("DuoDuoSaid")
    public List<DuoDuoSaid> duoDuoSaid;
    public List<ServiceType> serviceTypeReqList;
    public List<UserSaid> userSaidList;
    public List<FakeAIArrayBean> hotDestinationReqList;
    public List<String> hiList;
}
