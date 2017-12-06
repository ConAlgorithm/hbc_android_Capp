package com.hugboga.custom.data.bean.ai;

import com.google.gson.annotations.SerializedName;
import com.hugboga.custom.data.bean.ai.DuoDuoSaid;
import com.hugboga.custom.data.bean.ai.DurationReq;
import com.hugboga.custom.data.bean.ai.UserSaid;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class FakeAIQuestionsBean {
    public String chooseDestinationId;
    public String chooseDestinationType;
    public List<UserSaid> userSaidList;
    @SerializedName("DuoDuoSaid")
    public List<DuoDuoSaid> duoDuoSaid;
    public List<DurationReq> durationReqList;
    public List<AccompanyReq>  accompanyReqList;
}
