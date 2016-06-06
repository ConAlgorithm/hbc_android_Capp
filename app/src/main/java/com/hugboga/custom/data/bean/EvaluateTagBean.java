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
}
