package com.hugboga.custom.data.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/18.
 *
 *  cAvatar: "http://fr.dev.hbc.tech/user/20160608/s_201606081718048312.jpg",
 *  cName: "哦哦",
 *  content: "不错啊啊啊啊",
 *  createTime: "2016-06-07 22:11:19",
 *  createTimeYMD: "2016-06-07",
 *  labelNamesArr: [
 *      "接机5星标签1",
 *      "不错",
 *      "好棒啊",
 *      "司导人很好"
 *  ],
 *  orderNo: "LN191109848751",
 *  orderType: 5,
 *  orderTypeNameForCGuideDetail: "超省心包车游",
 *  totalScore: 3
 */

public class EvaluateItemData implements Serializable {
    @SerializedName("cAvatar")
    private String avatar;
    @SerializedName("cName")
    private String name;
    private String content;
    private String createTime;
    private String createTimeYMD;
    private ArrayList<String> labelNamesArr;
    private String orderNo;
    private int orderType;
    @SerializedName("orderTypeNameForCGuideDetail")
    private String orderTypeStr;
    private float totalScore;

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        String result = content;
        if (TextUtils.isEmpty(content)) {
            int scoreStrId = -1;
            switch ((int)totalScore) {
                case 1:
                    scoreStrId = R.string.evaluate_star_very_unsatisfactory;
                    break;
                case 2:
                    scoreStrId = R.string.evaluate_star_unsatisfactory;
                    break;
                case 3:
                    scoreStrId = R.string.evaluate_star_ordinary;
                    break;
                case 4:
                    scoreStrId = R.string.evaluate_star_satisfied;
                    break;
                case 5:
                    scoreStrId = R.string.evaluate_star_very_satisfied;
                    break;

            }
            if (scoreStrId != -1) {
                result = MyApplication.getAppContext().getString(scoreStrId);
            }
        }
        return result;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getCreateTimeYMD() {
        return createTimeYMD;
    }

    public ArrayList<String> getLabelNamesArr() {
        return labelNamesArr;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public int getOrderType() {
        return orderType;
    }

    public String getOrderTypeStr() {
        return orderTypeStr;
    }

    public int getTotalScore() {
        return (int)totalScore;
    }
}
