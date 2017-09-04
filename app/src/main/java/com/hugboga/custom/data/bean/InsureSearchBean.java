package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.List;

public class InsureSearchBean implements Serializable{

    public int totalSize;
    public List<List<InsureListBean>> insuranceMap;
}
