package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/5.
 */
public class ImListBean implements Serializable {

    public int totalSize;

    public int offset;

    public ArrayList<ChatBean> resultBean;

    public ArrayList<ServiceBean> serviceBean;

    public void filterService() {
        if (serviceBean != null && serviceBean.size() > 0) {
            ServiceBean serviceItemBean = serviceBean.get(0);
            ChatBean chatBean = new ChatBean();
            chatBean.targetAvatar = serviceItemBean.targetAvatar;
            chatBean.targetName = serviceItemBean.targetName;
            chatBean.setTargetType(serviceItemBean.targetType);
            if (resultBean == null) {
                resultBean = new ArrayList<ChatBean>();
            }
            resultBean.add(0, chatBean);
            ++totalSize;
        }
    }

    public static class ServiceBean implements Serializable {
        public String targetAvatar;
        public String targetName;
        public int targetType;
    }

}
