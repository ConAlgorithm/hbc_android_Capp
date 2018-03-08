package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangqiang on 17/6/26.
 */

public class TravelListAllBean implements Serializable {
    public int totalSize;
    public ArrayList<OrderBean> resultBean;
    public int unpayTotalSize;
    public int ingTotalSize;
    public int evaluationTotalSize;
    public String inviteContent;
}
