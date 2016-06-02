package com.hugboga.custom.utils;

import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.SelectCarBean;

/**
 * Created by dyt on 16/6/1.
 */

public class CarUtils {

    public static  SelectCarBean carBeanAdapter(CarBean carBean){
        SelectCarBean selectCarBean = new SelectCarBean();
        selectCarBean.seatCategory = carBean.carSeat;
        selectCarBean.carType = carBean.carType;
        selectCarBean.price = carBean.price;
        selectCarBean.carDesc = carBean.desc;
        selectCarBean.capOfLuggage = carBean.capOfLuggage;
        selectCarBean.capOfPerson = carBean.capOfPerson;
        selectCarBean.localPrice = carBean.localPrice;
        selectCarBean.models = carBean.models;
        selectCarBean.pricemark = carBean.pricemark;
        selectCarBean.expectedCompTime = carBean.expectedCompTime;
        return selectCarBean;
    }
}
