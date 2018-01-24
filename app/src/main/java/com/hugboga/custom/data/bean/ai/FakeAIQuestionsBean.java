package com.hugboga.custom.data.bean.ai;

import com.google.gson.annotations.SerializedName;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class FakeAIQuestionsBean {
    public String customServiceStatus;
    public String chooseDurationId;
    public String chooseAccompanyId;
    public String chooseDestinationId;
    public String chooseDestinationType;
    public String chooseServiceTypeOption;
    public String chooseServiceTypeId;
    public String chooseDestinationCountryId;
    public String chooseDestinationCountryName;
    public Object userSaidList;
    public Object regardsList;
    public String customServiceId;
    @SerializedName("DuoDuoSaid")
    public List<DuoDuoSaid> duoDuoSaid;
    public List<ServiceType> chooseServiceTypeList;
    public List<DurationReq> durationReqList;
    public List<AccompanyReq> accompanyReqList;
    public DestinationHomeVo recommendationDestinationHome; //推荐结果
    public List<FakeAIArrayBean> hotDestinationReqList;
}
