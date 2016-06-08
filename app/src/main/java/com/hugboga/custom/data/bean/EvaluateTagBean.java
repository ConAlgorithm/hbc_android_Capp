package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/6.
 */
public class EvaluateTagBean {

    @SerializedName("1")
    public ArrayList<EvaluateTag> oneStarTags;
    @SerializedName("2")
    public ArrayList<EvaluateTag> twoStarTags;
    @SerializedName("3")
    public ArrayList<EvaluateTag> threeStarTags;
    @SerializedName("4")
    public ArrayList<EvaluateTag> fourStarTags;
    @SerializedName("5")
    public ArrayList<EvaluateTag> fiveStarTags;

    public static class EvaluateTag {
        public int labelId;
        public String labelName;
        public int orderIndex;
        public int orderType;
        public int starType;
    }

    public ArrayList<EvaluateTagBean.EvaluateTag> getCurrentList(int level) {
        ArrayList<EvaluateTagBean.EvaluateTag> resultList = null;
        switch (level) {
            case 1:
                resultList = oneStarTags;
                break;
            case 2:
                resultList = twoStarTags;
                break;
            case 3:
                resultList = threeStarTags;
                break;
            case 4:
                resultList = fourStarTags;
                break;
            case 5:
                resultList = fiveStarTags;
                break;
        }
        return resultList;
    }
}
