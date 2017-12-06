package com.hugboga.custom.data.bean.ai;

import com.google.gson.annotations.SerializedName;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;

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
    @SerializedName("recommendationDestinationGoodsList")
    public List<DestinationGoodsVo> goodsList; //推荐结果
    public List<AccompanyReq>  accompanyReqList;
    public DestinationHomeVo recommendationDestinationHome; //推荐结果
}
