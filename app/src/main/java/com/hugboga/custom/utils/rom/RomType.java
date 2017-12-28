package com.hugboga.custom.utils.rom;

import com.hugboga.custom.utils.CommonUtils;

/**
 * Created by zhangqiang on 17/11/22.
 */

public enum RomType {

    TYPE_START1(1, "华为",""),
    TYPE_START2(2, "小米",""),
    TYPE_START3(3, "google",""),//用个推
    TYPE_START4(4,"极光",""),
    TYPE_START5(5, "其他","");

    private Integer num;
    private String pinPai;
    private String token;

    RomType(Integer num, String pinPai,String token) {
        this.num = num;
        this.pinPai = pinPai;
        this.token = token;
    }

    public static String getValueStr(Integer startNum) {
        switch (startNum) {
            case 1:
                return "华为";
            case 2:
                return "小米";
            case 3:
                return "google";
            case 4:
                return "其他";
            default:
                return "其他";
        }
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getPinPai() {
        return pinPai;
    }

    public void setPinPai(String pinPai) {
        this.pinPai = pinPai;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static int getNumByDeviceType(){
        if(Rom.isEmui()){
            return RomType.TYPE_START1.getNum();
        }else if(Rom.isMiui()){
            return RomType.TYPE_START2.getNum();
        }else if(CommonUtils.isSupportGoogleService() && !Rom.isEmui() && !Rom.isMiui()){
            return RomType.TYPE_START3.getNum();
        }else {
            return RomType.TYPE_START4.getNum();
        }
    }
}