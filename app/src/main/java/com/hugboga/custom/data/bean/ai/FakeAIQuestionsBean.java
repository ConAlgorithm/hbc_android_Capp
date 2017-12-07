package com.hugboga.custom.data.bean.ai;

import com.google.gson.annotations.SerializedName;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class FakeAIQuestionsBean implements Serializable {
    public String customServiceStatus;
    public String chooseDestinationId;
    public String chooseDestinationType;
    public List<UserSaid> userSaidList;
    @SerializedName("DuoDuoSaid")
    public List<DuoDuoSaid> duoDuoSaid;
    public List<DurationReq> durationReqList;
    public List<AccompanyReq> accompanyReqList;
    public DestinationHomeVo recommendationDestinationHome; //推荐结果
    public List<FakeAIArrayBean> hotDestinationReqList;
}
