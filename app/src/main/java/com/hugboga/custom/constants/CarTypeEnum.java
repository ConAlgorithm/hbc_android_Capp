package com.hugboga.custom.constants;

import com.hugboga.custom.R;

/**
 *
 * c * Created by admin on 2016/3/20.
 */
public enum  CarTypeEnum  {
    T1S5(R.mipmap.choose_5seat_1),
    T1S7(R.mipmap.choose_7seat_1),
    T1S9(R.mipmap.choose_9seat_1),
    T1S12(R.mipmap.choose_12seat_1),

    T2S5(R.mipmap.choose_5seat_2),
    T2S7(R.mipmap.choose_7seat_2),
    T2S9(R.mipmap.choose_9seat_2),
    T2S12(R.mipmap.choose_12seat_2),

    T3S5(R.mipmap.choose_5seat_3),
    T3S7(R.mipmap.choose_7seat_3),
    T3S9(R.mipmap.choose_9seat_3),
    T3S12(R.mipmap.choose_12seat_3),

    T4S5(R.mipmap.choose_5seat_4),
    T4S7(R.mipmap.choose_7seat_4),
    T4S9(R.mipmap.choose_9seat_4),
    T4S12(R.mipmap.choose_12seat_4);

    public int imgRes;
    CarTypeEnum(int res){
        this.imgRes =res;
    }

    /**
     * 根据
     * @param type 车型 1，2，3，4
     * @param seat 座位数 5，7，9，12
     * @return
     */
    public static CarTypeEnum getCarType(int type,int seat){
        String carLabel = "T"+type+"S"+seat;
        try {
            return  CarTypeEnum.valueOf(carLabel);
        }catch (Exception e){

        }
       return  null;
    }
}