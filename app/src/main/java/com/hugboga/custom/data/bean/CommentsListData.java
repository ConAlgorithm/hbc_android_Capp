package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/18.
 */
public class CommentsListData implements Serializable{

    private ArrayList<EvaluateItemData> listData;
    private int listCount;

    public ArrayList<EvaluateItemData> getListData() {
        return listData;
    }

    public int getListCount() {
        return listCount;
    }
}
