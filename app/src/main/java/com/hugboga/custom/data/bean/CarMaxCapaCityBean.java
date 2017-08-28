package com.hugboga.custom.data.bean;

import java.io.Serializable;

public class CarMaxCapaCityBean implements Serializable{

    public int numOfPerson;//最大出行人数
    private Integer childSeatSwitch;//是否提供儿童座椅，1为是，0为否

    public boolean isSupportChildSeat() {
        if (childSeatSwitch != null && childSeatSwitch == 1) {
            return true;
        } else {
            return false;
        }
    }
}
