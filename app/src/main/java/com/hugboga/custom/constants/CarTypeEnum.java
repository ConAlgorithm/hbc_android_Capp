package com.hugboga.custom.constants;

import com.hugboga.custom.R;

/**
 * c * Created by admin on 2016/3/20.
 */
public enum CarTypeEnum {
    T1S5(R.mipmap.jj5),
    T1S7(R.mipmap.jj7),
    T1S9(R.mipmap.jj9),
    T1S12(R.mipmap.jj12),

    T2S5(R.mipmap.ss5),
    T2S7(R.mipmap.ss7),
    T2S9(R.mipmap.ss9),
    T2S12(R.mipmap.ss12),

    T3S5(R.mipmap.hh5),
    T3S7(R.mipmap.hh7),
    T3S9(R.mipmap.hh9),
    T3S12(R.mipmap.hh12),

    T4S5(R.mipmap.sh5),
    T4S7(R.mipmap.sh7),
    T4S9(R.mipmap.sh9),
    T4S12(R.mipmap.sh12);

    public int imgRes;

    CarTypeEnum(int res) {
        this.imgRes = res;
    }

    /**
     * 根据
     *
     * @param type 车型 1，2，3，4
     * @param seat 座位数 5，7，9，12
     * @return
     */
    public static CarTypeEnum getCarType(int type, int seat) {
        String carLabel = "T" + type + "S" + seat;
        try {
            return CarTypeEnum.valueOf(carLabel);
        } catch (Exception e) {

        }
        return null;
    }
}