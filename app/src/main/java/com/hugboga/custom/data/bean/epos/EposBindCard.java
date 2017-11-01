package com.hugboga.custom.data.bean.epos;

import android.text.TextUtils;

import com.hugboga.custom.R;

import java.io.Serializable;

/**
 * Epos绑定的卡信息
 * Created by HONGBO on 2017/10/28 16:14.
 */

public class EposBindCard implements Serializable {

    public String cardNo; //卡号 6225****6380
    public String bankName; //银行卡名称 招商银行信用卡
    public String cardType; //卡片类型 CREDIT_CARD
    public String bindId; //绑定ID f5/YD5FLFLnO6o/2NQJG0BEFPUljuKdb
    public String bankIcon; //银行图标 CMBCHINACREDIT

    /**
     * 获取银行图标资源ID
     * @return
     */
    public int getBankIconId() {
        if (TextUtils.isEmpty(bankIcon)) {
            return R.mipmap.ic_bank_ABCCREDIT;
        }
        switch (bankIcon) {
            case "ABCCREDIT":
                return R.mipmap.ic_bank_ABCCREDIT;
            case "BCCBCREDIT":
                return R.mipmap.ic_bank_BCCBCREDIT;
            case "BJRCBCREDIT":
                return R.mipmap.ic_bank_BJRCBCREDIT;
            case "BOCCREDIT":
                return R.mipmap.ic_bank_BOCCREDIT;
            case "BOCOCREDIT":
                return R.mipmap.ic_bank_BOCOCREDIT;
            case "BOSHCREDIT":
                return R.mipmap.ic_bank_BOSHCREDIT;
            case "BSBCREDIT":
                return R.mipmap.ic_bank_BSBCREDIT;
            case "CBHBCREDIT":
                return R.mipmap.ic_bank_CBHBCREDIT;
            case "CCBCREDIT":
                return R.mipmap.ic_bank_CCBCREDIT;
            case "CIBCREDIT":
                return R.mipmap.ic_bank_CIBCREDIT;
            case "CMBCCREDIT":
                return R.mipmap.ic_bank_CMBCCREDIT;
            case "CMBCHINACREDIT":
                return R.mipmap.ic_bank_CMBCHINACREDIT;
            case "CQRCBCREDIT":
                return R.mipmap.ic_bank_CQRCBCREDIT;
            case "ECITICCREDIT":
                return R.mipmap.ic_bank_ECITICCREDIT;
            case "EVERBRIGHTCREDIT":
                return R.mipmap.ic_bank_EVERBRIGHTCREDIT;
            case "GDBCREDIT":
                return R.mipmap.ic_bank_GDBCREDIT;
            case "GRCBCREDIT":
                return R.mipmap.ic_bank_GRCBCREDIT;
            case "GYCCBCREDIT":
                return R.mipmap.ic_bank_GYCCBCREDIT;
            case "GZCBCREDIT":
                return R.mipmap.ic_bank_GZCBCREDIT;
            case "HRBCBCREDIT":
                return R.mipmap.ic_bank_HRBCBCREDIT;
            case "HSBANKCREDIT":
                return R.mipmap.ic_bank_HSBANKCREDIT;
            case "HXBCREDIT":
                return R.mipmap.ic_bank_HXBCREDIT;
            case "HZBANKCREDIT":
                return R.mipmap.ic_bank_HZBANKCREDIT;
            case "ICBCCREDIT":
                return R.mipmap.ic_bank_ICBCCREDIT;
            case "JSBCHINACREDIT":
                return R.mipmap.ic_bank_JSBCHINACREDIT;
            case "LJBANKCREDIT":
                return R.mipmap.ic_bank_LJBANKCREDIT;
            case "NBCBCREDIT":
                return R.mipmap.ic_bank_NBCBCREDIT;
            case "NJCBCREDIT":
                return R.mipmap.ic_bank_NJCBCREDIT;
            case "PINGANCREDIT":
                return R.mipmap.ic_bank_PINGANCREDIT;
            case "PSBCCREDIT":
                return R.mipmap.ic_bank_PSBCCREDIT;
            case "SDBCREDIT":
                return R.mipmap.ic_bank_SDBCREDIT;
            case "SPDBCREDIT":
                return R.mipmap.ic_bank_SPDBCREDIT;
            case "SRCBCREDIT":
                return R.mipmap.ic_bank_SRCBCREDIT;
            case "TCCBCREDIT":
                return R.mipmap.ic_bank_TCCBCREDIT;
            case "TZBANKCREDIT":
                return R.mipmap.ic_bank_TZBANKCREDIT;
            default:
                return R.mipmap.ic_bank_ABCCREDIT;
        }
    }
}
