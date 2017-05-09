package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class InsureSearchBean implements Serializable{

    public int totalSize;
    @SerializedName("resultBean")
    public ArrayList<InsureListBean> insuranceList;
}
