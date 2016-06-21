package com.hugboga.custom.data.bean;


import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "line_group_item")
public class LineGroupItem implements IBaseBean,Parcelable{

    @Column(name = "group_id", isId = true)
    public int group_id;

    @Column(name = "group_name")
    public String group_name;

    @Column(name = "type")
    public int type;

    @Column(name = "sub_city_id")
    public int sub_city_id;

    @Column(name = "sub_city_name")
    public String sub_city_name;

    @Column(name = "sub_place_id")
    public int sub_place_id;

    @Column(name = "sub_place_name")
    public String sub_place_name;

    @Column(name = "sub_group_id")
    public int sub_group_id;

    @Column(name = "sub_group_name")
    public String sub_group_name;

    @Column(name = "hot_weight")
    public int hot_weight;

    public boolean isSelected;

    public int group_type;//1, 国家 2,城市

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.group_id);
        dest.writeString(this.group_name);
        dest.writeInt(this.type);
        dest.writeInt(this.sub_city_id);
        dest.writeString(this.sub_city_name);
        dest.writeInt(this.sub_place_id);
        dest.writeString(this.sub_place_name);
        dest.writeInt(this.sub_group_id);
        dest.writeString(this.sub_group_name);
        dest.writeInt(this.hot_weight);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.group_type);
    }

    public LineGroupItem() {
    }

    protected LineGroupItem(Parcel in) {
        this.group_id = in.readInt();
        this.group_name = in.readString();
        this.type = in.readInt();
        this.sub_city_id = in.readInt();
        this.sub_city_name = in.readString();
        this.sub_place_id = in.readInt();
        this.sub_place_name = in.readString();
        this.sub_group_id = in.readInt();
        this.sub_group_name = in.readString();
        this.hot_weight = in.readInt();
        this.isSelected = in.readByte() != 0;
        this.group_type = in.readInt();
    }

    public static final Creator<LineGroupItem> CREATOR = new Creator<LineGroupItem>() {
        @Override
        public LineGroupItem createFromParcel(Parcel source) {
            return new LineGroupItem(source);
        }

        @Override
        public LineGroupItem[] newArray(int size) {
            return new LineGroupItem[size];
        }
    };
}
