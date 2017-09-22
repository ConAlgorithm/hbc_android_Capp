package com.hugboga.custom.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by qingcha on 17/9/22.
 */

public class TravelerInfoDataCheck {

    public String contactsName;
    public String contactsPhone;
    public String contactsAreaCode;

    public boolean isStandby;
    public String standbyContactsName;
    public String standbyContactsPhone;
    public String standbyContactsAreaCode;

    public String otherContactsName;
    public String otherContactsPhone;
    public String otherContactsAreaCode;
    public boolean isSendMessage;
    public boolean isForOther;

    public String time;
    public String address;
    public String addressDescription;
    public String flightBrandSign;
    public String flight;
    public String wechat;
    public String mark;


    public boolean isContacts;
    public boolean isPhone;
    public boolean isAreaCode;

    public boolean isStandbyState;
    public boolean isStandbyContacts;
    public boolean isStandbyPhone;
    public boolean isStandbyAreaCode;

    public boolean isOtherContacts;
    public boolean isOtherPhone;
    public boolean isOtherAreaCode;
    public boolean isSendMessageState;
    public boolean isForOtherState;

    public boolean isTime;
    public boolean isAddress;
    public boolean isAddressDescription;
    public boolean isCheckin;
    public boolean isFlight;
    public boolean isWechat;
    public boolean isMark;


    public EditText contactsET;
    public EditText phoneET;
    public EditText standbyContactsET;
    public EditText standbyPhoneET;
    public EditText otherContactsET;
    public EditText otherPhoneET;
    public EditText checkinET;
    public EditText flightET;
    public EditText wechatET;
    public EditText markET;

    public static final int TYPE_CONTACTS = 1;
    public static final int TYPE_PHONE = 2;
    public static final int TYPE_STANDBYCONTACTS = 3;
    public static final int TYPE_STANDBYPHONE = 4;
    public static final int TYPE_OTHERCONTACTS = 5;
    public static final int TYPE_OTHERPHONE = 6;
    public static final int TYPE_CHECKIN = 7;
    public static final int TYPE_FLIGHT = 8;
    public static final int TYPE_WECHAT = 9;
    public static final int TYPE_MARK = 10;

    private OnDataChangeListener listener;

    public void setListener(OnDataChangeListener _listener) {
        setEditTextListener(TYPE_CONTACTS, contactsET, contactsName);
        setEditTextListener(TYPE_PHONE, phoneET, contactsPhone);
        setEditTextListener(TYPE_STANDBYCONTACTS, standbyContactsET, standbyContactsName);
        setEditTextListener(TYPE_STANDBYPHONE, standbyPhoneET, standbyContactsPhone);
        setEditTextListener(TYPE_OTHERCONTACTS, otherContactsET, otherContactsName);
        setEditTextListener(TYPE_OTHERPHONE, otherPhoneET, otherContactsPhone);
        setEditTextListener(TYPE_CHECKIN, checkinET, flightBrandSign);
        setEditTextListener(TYPE_FLIGHT, flightET, flight);
        setEditTextListener(TYPE_WECHAT, wechatET, wechat);
        setEditTextListener(TYPE_MARK, markET, mark);
        this.listener = _listener;
    }

    public void checkContactsPhone(String _Phone) {
        isPhone = !TextUtils.equals(contactsPhone, _Phone);
        onDataChange();
    }

    public void checkContactsAreaCode(String _areaCode) {
        isAreaCode = !TextUtils.equals(contactsAreaCode, _areaCode);
        onDataChange();
    }

    public void checkStandbyContactsPhone(String _Phone) {
        isStandbyPhone  = !TextUtils.equals(standbyContactsPhone, _Phone);
        onDataChange();
    }
    public void checkStandbyContactsAreaCode(String _areaCode) {
        isStandbyAreaCode = !TextUtils.equals(standbyContactsAreaCode, _areaCode);
        onDataChange();
    }

    public void checkOtherContactsPhone(String _Phone) {
        isOtherPhone = !TextUtils.equals(otherContactsPhone, _Phone);
        onDataChange();
    }

    public void checkOtherContactsAreaCode(String _areaCode) {
        isOtherAreaCode = !TextUtils.equals(otherContactsAreaCode, _areaCode);
        onDataChange();
    }

    public void checkIsSendMessage(boolean _isSendMessage) {
        isSendMessageState = isSendMessage != _isSendMessage;
        onDataChange();
    }

    public void checkIsForOther(boolean _isForOther) {
        isForOtherState = isForOther != _isForOther;
        onDataChange();
    }

    public void checkIsStandby(boolean _isStandby) {
        isStandbyState = isStandby != _isStandby;
        onDataChange();
    }

    public void checkTime(String _time) {
        isTime = !TextUtils.equals(time, _time);
        onDataChange();
    }

    public void checkAddress(String _address) {
        isAddress = !TextUtils.equals(address, _address);
        onDataChange();
    }

    public void checkAddressDetail(String _addressDetail) {
        isAddressDescription = !TextUtils.equals(addressDescription, _addressDetail);
        onDataChange();
    }

    public void setEditTextListener(final int type, EditText editText, final String defaultStr) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isChange = !TextUtils.equals(defaultStr, s.toString());
                switch (type) {
                    case TYPE_CONTACTS:
                        isContacts = isChange;
                        break;
                    case TYPE_PHONE:
                        isPhone = isChange;
                        break;
                    case TYPE_STANDBYCONTACTS:
                        isStandbyContacts = isChange;
                        break;
                    case TYPE_STANDBYPHONE:
                        isStandbyPhone = isChange;
                        break;
                    case TYPE_OTHERCONTACTS:
                        isOtherContacts = isChange;
                        break;
                    case TYPE_OTHERPHONE:
                        isOtherPhone = isChange;
                        break;
                    case TYPE_CHECKIN:
                        isCheckin = isChange;
                        break;
                    case TYPE_FLIGHT:
                        isFlight = isChange;
                        break;
                    case TYPE_WECHAT:
                        isWechat = isChange;
                        break;
                    case TYPE_MARK:
                        isMark = isChange;
                        break;
                }
                onDataChange();
            }
        });
    }

    public void onDataChange() {
        boolean isStandbyChange;
        if (isStandbyState) {
            if (!isStandby) {
                isStandbyChange = isStandbyState || isStandbyContacts || isStandbyPhone || isStandbyAreaCode;
            } else {
                isStandbyChange = isStandbyState;
            }
        } else if (isStandby) {
            isStandbyChange = isStandbyContacts || isStandbyPhone || isStandbyAreaCode;
        } else {
            isStandbyChange = false;
        }

        boolean isForOtherChange;
        if (isForOtherState) {
            if (!isForOther) {
                isForOtherChange = isOtherContacts || isOtherPhone || isOtherAreaCode || isSendMessageState || isForOtherState;
            } else {
                isForOtherChange = isForOtherState;
            }
        } else if (isForOther) {
            isForOtherChange = isOtherContacts || isOtherPhone || isOtherAreaCode || isSendMessageState;
        } else {
            isForOtherChange = false;
        }

        boolean isChange = isContacts || isPhone || isAreaCode
                || isStandbyChange || isForOtherChange
                || isTime || isAddress || isAddressDescription || isCheckin || isFlight || isWechat || isMark;
        if (listener != null) {
            listener.onDataChange(isChange);
        }
    }

    public interface OnDataChangeListener {
        public void onDataChange(boolean isChange);
    }

    public void setOnDataChangeListener(OnDataChangeListener listener) {
        this.listener = listener;
    }

}
