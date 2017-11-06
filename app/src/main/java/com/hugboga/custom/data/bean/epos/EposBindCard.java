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
            return R.mipmap.ic_icon_bank_default;
        }
        switch (bankIcon) {
            case "ABCCREDIT":
                return R.mipmap.ic_icon_bank_abccredit;
            case "BANKOFDLCREDIT":
                return R.mipmap.ic_icon_bank_bankofdlcredit;
            case "BCCBCREDIT":
                return R.mipmap.ic_icon_bank_bccbcredit;
            case "BJRCBCREDIT":
                return R.mipmap.ic_icon_bank_bjrcbcredit;
            case "BOCCREDIT":
                return R.mipmap.ic_icon_bank_boccredit;
            case "BOCOCREDIT":
                return R.mipmap.ic_icon_bank_bococredit;
            case "BOSHCREDIT":
                return R.mipmap.ic_icon_bank_boshcredit;
            case "BSBCREDIT":
                return R.mipmap.ic_icon_bank_bsbcredit;
            case "CBHBCREDIT":
                return R.mipmap.ic_icon_bank_cbhbcredit;
            case "CCBCREDIT":
                return R.mipmap.ic_icon_bank_ccbcredit;
            case "CIBCREDIT":
                return R.mipmap.ic_icon_bank_cibcredit;
            case "CMBCCREDIT":
                return R.mipmap.ic_icon_bank_cmbccredit;
            case "CMBCHINACREDIT":
                return R.mipmap.ic_icon_bank_cmbchinacredit;
            case "CQRCBCREDIT":
                return R.mipmap.ic_icon_bank_cqrcbcredit;
            case "ECITICCREDIT":
                return R.mipmap.ic_icon_bank_eciticcredit;
            case "EVERBRIGHTCREDIT":
                return R.mipmap.ic_icon_bank_everbrightcredit;
            case "GDBCREDIT":
                return R.mipmap.ic_icon_bank_gdbcredit;
            case "GRCBCREDIT":
                return R.mipmap.ic_icon_bank_grcbcredit;
            case "GYCCBCREDIT":
                return R.mipmap.ic_icon_bank_gyccbcredit;
            case "GZCBCREDIT":
                return R.mipmap.ic_icon_bank_gzcbcredit;
            case "HRBCBCREDIT":
                return R.mipmap.ic_icon_bank_hrbcbcredit;
            case "HSBANKCREDIT":
                return R.mipmap.ic_icon_bank_hsbankcredit;
            case "HXBCREDIT":
                return R.mipmap.ic_icon_bank_hxbcredit;
            case "HZBANKCREDIT":
                return R.mipmap.ic_icon_bank_hzbankcredit;
            case "ICBCCREDIT":
                return R.mipmap.ic_icon_bank_icbccredit;
            case "JSBCHINACREDIT":
                return R.mipmap.ic_icon_bank_jsbchinacredit;
            case "LJBANKCREDIT":
                return R.mipmap.ic_icon_bank_ljbankcredit;
            case "NBCBCREDIT":
                return R.mipmap.ic_icon_bank_nbcbcredit;
            case "NJCBCREDIT":
                return R.mipmap.ic_icon_bank_njcbcredit;
            case "PINGANCREDIT":
                return R.mipmap.ic_icon_bank_pingancredit;
            case "PSBCCREDIT":
                return R.mipmap.ic_icon_bank_psbccredit;
            case "SDBCREDIT":
                return R.mipmap.ic_icon_bank_sdbcredit;
            case "SPDBCREDIT":
                return R.mipmap.ic_icon_bank_spdbcredit;
            case "SRCBCREDIT":
                return R.mipmap.ic_icon_bank_srcbcredit;
            case "TCCBCREDIT":
                return R.mipmap.ic_icon_bank_tccbcredit;
            case "TZBANKCREDIT":
                return R.mipmap.ic_icon_bank_tzbankcredit;
            default:
                return R.mipmap.ic_icon_bank_default;
        }
    }
}
