package com.hugboga.custom.utils;

import android.util.Log;

import com.hugboga.custom.constants.CarTypeEnum;
import com.hugboga.custom.constants.ChooseCarTypeEnum;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.GuideCars;

import java.util.ArrayList;
import java.util.List;


public class CarUtils {


    public static int getCarImgs(int carType,int carSeat){
        ChooseCarTypeEnum carTypeEnum = ChooseCarTypeEnum.getCarType(carType, carSeat);
        if (carTypeEnum != null) {
            return carTypeEnum.imgRes;
        }
        return -1;
    }

    public static void sortListDataImage(List<CarBean> carList) {
        for (CarBean bean : carList) {
            CarTypeEnum carTypeEnum = CarTypeEnum.getCarType(bean.carType, bean.carSeat);
            if (carTypeEnum != null) {
                bean.imgRes = carTypeEnum.imgRes;
            }
        }
    }

    public static ArrayList<CarBean> initCarListData(ArrayList<CarBean> checkCarList) {

        for(CarBean carBean:checkCarList){
            if(null == carBean){
                return null;
            }
            ChooseCarTypeEnum carTypeEnum = ChooseCarTypeEnum.getCarType(carBean.carType, carBean.carSeat);
            if (carTypeEnum != null) {
                carBean.imgRes = carTypeEnum.imgRes;
                Log.e("==============",carBean.imgRes+"");
            }
        }
//        int id = 1;
//        CarBean bean;
//        ArrayList<CarBean> carList = new ArrayList<CarBean>(16);
//        for (int i = 1; i <= 4; i++) {
//            for (int j = 1; j <= 4; j++) {
//                bean = new CarBean();
//                bean.id = id;
//                bean.carType = i;
//                bean.carSeat = Constants.CarSeatMap.get(j);
//                bean.originalPrice = 0;
////                bean.models = Constants.CarDescInfoMap.get(i).get(j);
//                ChooseCarTypeEnum carTypeEnum = ChooseCarTypeEnum.getCarType(bean.carType, bean.carSeat);
//                if (carTypeEnum != null) {
//                    bean.imgRes = carTypeEnum.imgRes;
//                    Log.e("==============",bean.imgRes+"");
//                }
//
//                CarBean newCarBean = isMatchLocal(bean,checkCarList);
//                if (null != newCarBean) {
//                    bean.models = newCarBean.models;
//                    bean.capOfLuggage = newCarBean.capOfLuggage;
//                    bean.desc = newCarBean.desc;
//                    bean.capOfPerson = newCarBean.capOfPerson;
//                    bean.price = newCarBean.price;
//                    bean.pricemark = newCarBean.pricemark;
//                    bean.priceChannel = newCarBean.priceChannel;
//                    bean.orderChannel = new CarBean().orderChannel;
//                    carList.add(bean);
//                }
//                id++;
//            }
//        }
        return checkCarList;
    }


    //报价是否有司导车型
    public static  CarBean isMatchLocal(CarBean bean,ArrayList<CarBean> carList) {
        if(null == carList || null == bean){
            return  null;
        }
        for (int i = 0; i < carList.size(); i++) {
            if (carList.get(i).carType == bean.carType && carList.get(i).carSeat == bean.carSeat) {
                return carList.get(i);
            }
        }
        return null;
    }

    public static  CarBean getNewCarBean(CollectGuideBean collectGuideBean) {
        CarBean carBean = new CarBean();
        carBean.capOfLuggage = collectGuideBean.numOfLuggage;
        carBean.capOfPerson = collectGuideBean.numOfPerson;
        carBean.carType = collectGuideBean.carType;
        carBean.desc = collectGuideBean.carDesc;
        carBean.models = collectGuideBean.carModel;
        carBean.carSeat = collectGuideBean.carClass;
        carBean.imgRes = CarUtils.getCarImgs(collectGuideBean.carType,collectGuideBean.carClass);
        return carBean;
    }

//    //司导车辆信息ids
//    public static String getCarIds(List<GuideCars> guideCars){
//        String ids = "";
//        if(null != guideCars) {
//            int size = guideCars.size();
//            for (int i = 0; i < size; i++) {
//                if (i == size - 1) {
//                    ids += guideCars.get(i).carModelId;
//                }else{
//                    ids += guideCars.get(i).carModelId+",";
//                }
//            }
//        }
//        return ids;
//    }

    //司导车辆信息ids
    public static String getCarIds(ArrayList<GuideCarBean> guideCars){
        String ids = "";
        if(null != guideCars) {
            int size = guideCars.size();
            for (int i = 0; i < size; i++) {
                if (i == size - 1) {
                    ids += guideCars.get(i).carModelId;
                }else{
                    ids += guideCars.get(i).carModelId+",";
                }
            }
        }
        return ids;
    }

    //司导bean
    public static CollectGuideBean collectGuideBean = null;

    //根据司导车辆和 报价返回车辆 生成新的车辆信息
    public static ArrayList<CarBean> getCarBeanList(List<CarBean> carBeans,List<GuideCars> guideCars){
        ArrayList<CarBean> list = new ArrayList<>();
        for(int n = 0;n < carBeans.size();n++) {
            for (int i = 0; i < guideCars.size(); i++) {
                if (carBeans.get(n).carType == guideCars.get(i).carType &&
                        carBeans.get(n).seatType == guideCars.get(i).carClass) {
                    CarBean carBean = (CarBean)(carBeans.get(n).clone());
                    carBean.carLicenceNo = guideCars.get(i).carLicenceNo;
                    carBean.carLicenceNoCovered = guideCars.get(i).carLicenceNoCovered;
                    carBean.carBrandName = guideCars.get(i).carBrandName;
                    carBean.carName = guideCars.get(i).carName;
                    list.add(carBean);
                }
            }
        }
        return list;
    }


}
