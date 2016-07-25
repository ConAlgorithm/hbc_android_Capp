package com.hugboga.custom.data.bean;


import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "line_group")
public class LineGroupBean  implements  IBaseBean,Parcelable{

    @Column(name = "group_id", isId = true)
    public int group_id;

    @Column(name = "group_name")
    public String group_name;

    @Column(name = "level")
    public int level;
    @Column(name = "parent_id")
    public int parent_id;
    @Column(name = "parent_type")
    public int parent_type;
    @Column(name = "sub_places")
    public String sub_places;
    @Column(name = "sub_cities")
    public String sub_cities;
    @Column(name = "hot_weight")
    public int hot_weight;
    @Column(name = "parent_name")
    public String parent_name;

    public boolean isSelected;

    public int type;

    public int has_sub;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.group_id);
        dest.writeString(this.group_name);
        dest.writeInt(this.level);
        dest.writeInt(this.parent_id);
        dest.writeInt(this.parent_type);
        dest.writeString(this.sub_places);
        dest.writeString(this.sub_cities);
        dest.writeInt(this.hot_weight);
        dest.writeString(this.parent_name);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
        dest.writeInt(this.has_sub);
    }

    public LineGroupBean() {
    }

    protected LineGroupBean(Parcel in) {
        this.group_id = in.readInt();
        this.group_name = in.readString();
        this.level = in.readInt();
        this.parent_id = in.readInt();
        this.parent_type = in.readInt();
        this.sub_places = in.readString();
        this.sub_cities = in.readString();
        this.hot_weight = in.readInt();
        this.parent_name = in.readString();
        this.isSelected = in.readByte() != 0;
        this.type = in.readInt();
        this.has_sub = in.readInt();
    }

    public static final Creator<LineGroupBean> CREATOR = new Creator<LineGroupBean>() {
        @Override
        public LineGroupBean createFromParcel(Parcel source) {
            return new LineGroupBean(source);
        }

        @Override
        public LineGroupBean[] newArray(int size) {
            return new LineGroupBean[size];
        }
    };
}
