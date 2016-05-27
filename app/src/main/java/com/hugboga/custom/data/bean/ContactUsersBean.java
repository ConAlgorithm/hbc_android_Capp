package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 16/5/27.
 */

public class ContactUsersBean implements Parcelable {

    public String userName;
    public String userPhone;
    public String phoneCode;

    public String user1Name;
    public String user1Phone;
    public String phone1Code;

    public String user2Name;
    public String user2Phone;
    public String phone2Code;


    public boolean isForOther;
    public String otherName;
    public String otherPhone;
    public String otherphoneCode;
    public boolean isSendMessage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.userPhone);
        dest.writeString(this.phoneCode);
        dest.writeString(this.user1Name);
        dest.writeString(this.user1Phone);
        dest.writeString(this.phone1Code);
        dest.writeString(this.user2Name);
        dest.writeString(this.user2Phone);
        dest.writeString(this.phone2Code);
        dest.writeByte(this.isForOther ? (byte) 1 : (byte) 0);
        dest.writeString(this.otherName);
        dest.writeString(this.otherPhone);
        dest.writeString(this.otherphoneCode);
        dest.writeByte(this.isSendMessage ? (byte) 1 : (byte) 0);
    }

    public ContactUsersBean() {
    }

    protected ContactUsersBean(Parcel in) {
        this.userName = in.readString();
        this.userPhone = in.readString();
        this.phoneCode = in.readString();
        this.user1Name = in.readString();
        this.user1Phone = in.readString();
        this.phone1Code = in.readString();
        this.user2Name = in.readString();
        this.user2Phone = in.readString();
        this.phone2Code = in.readString();
        this.isForOther = in.readByte() != 0;
        this.otherName = in.readString();
        this.otherPhone = in.readString();
        this.otherphoneCode = in.readString();
        this.isSendMessage = in.readByte() != 0;
    }

    public static final Creator<ContactUsersBean> CREATOR = new Creator<ContactUsersBean>() {
        @Override
        public ContactUsersBean createFromParcel(Parcel source) {
            return new ContactUsersBean(source);
        }

        @Override
        public ContactUsersBean[] newArray(int size) {
            return new ContactUsersBean[size];
        }
    };
}
