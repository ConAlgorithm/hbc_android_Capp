package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangqiang on 17/6/22.
 */

public class OssTokenParamBean {
    @SerializedName("OSSAccessKeyId")
    private String ossAccessKeyId;
    @SerializedName("Signature")
    private String signature;
    private String policy;

    public String getOssAccessKeyId() {
        return ossAccessKeyId;
    }

    public void setOssAccessKeyId(String ossAccessKeyId) {
        this.ossAccessKeyId = ossAccessKeyId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
