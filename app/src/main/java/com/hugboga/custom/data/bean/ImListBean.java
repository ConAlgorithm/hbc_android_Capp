package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class ImListBean implements Serializable {

    public int totalSize;

    public int offset;

    public ArrayList<ChatBean> resultBean;

    public void filterService() {
        if (resultBean == null || resultBean.size() <= 0) {
            return;
        }
        ChatBean chatBean = resultBean.get(0);
        if (chatBean.getTargetType() == 3) {
            resultBean.remove(0);
            --totalSize;
        }
    }


}
