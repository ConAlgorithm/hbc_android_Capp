package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/11/14
 *
 * API: crm/v1.0/c/advice/source
 * WIKI: http://wiki.hbc.tech/display/admin/CAPP+3.1.0
 */
public class ServiceQuestionBean implements Serializable{
    public static final int TYPE_QUESTION_ITEM = 0x03;
    public static final int TYPE_QUESTION_USER_ITEM = 0x04;
    public int viewType = TYPE_QUESTION_ITEM;        // 非接口返回 QuestionItemView : TYPE_QUESTION_ITEM 、QuestionItemView : TYPE_QUESTION_USER_ITEM

    public int adviceSourceId;          // 场景id
    public String adviceSourceName;     // 订单问题
    public String welcome;              // 欢迎语

    @SerializedName("advices")
    public ArrayList<QuestionItem> questionList;

    public static class QuestionItem implements Serializable, Cloneable{
        public int type;            // 问题类型：1目录（对应sons），2问题（对应answer），3客服跳转（对应customRole）
        public String answer;       // 问题答案，type=2才会有
        public int customRole;      // 客服id，type=3才会有
        @SerializedName("sons")
        public ArrayList<QuestionItem> questionItemList;  // 子问题，type=1时才会有

        public boolean isAnswer = false;                  // 非接口返回 是否显示答案
        public int lastCustomRole = 0;                    // 非接口返回 记录最后一条客服ID
        public boolean isRoot = false;                    // 非接口返回 是否是根目录

        public int adviceId;        // 问题id
        public String adviceName;   // 问题
        public int depth;           // 问题深度
        public int parentId;        // 上级ID
        public String parentName;   // 上级目录名称
        public int orderIndex;      // 顺序

        @Override
        public Object clone(){
            QuestionItem questionItem = null;
            try {
                questionItem = (QuestionItem) super.clone();
            } catch (CloneNotSupportedException e){
                e.printStackTrace();
            }
            return questionItem;
        }
    }
}
