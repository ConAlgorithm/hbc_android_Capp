package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhangqiang on 17/6/22.
 */

public class Photo implements Parcelable {

    public static final int IMAGE = 0; //表示图片
    public static final int ADD_IMAGE_ICON = 1;//表示是空图标

    public static final int PHOTO_SAVE = 0;//图片为暂存状态
    public static final int PHOTO_NOTPASS = 3;//图片审核未通过

    public String cardPhotoSrc;
    public int cardStatus;
    public int cardType;
    public String createTimeStr;
    public String guideCardId;
    public int orderIndex;
    public String signRemark;
    public int signStatus;
    public String updateTimeStr;

    /**
     * local 本地字段 判断是添加标志，还是图片
     *  见静态常量 IMAGE，ADD_IMAGE_ICON
     */
    public int photoType;

    public int uploadStatus = 0;
    public String uploadPercent;
    public String localFilePath;
    public int unquineId; //判断唯一


    // "cardPhotoSrc":"https://fr-dev.huangbaoche.com/guide/l_img123444.jpg?r=0.569977743775259", ###相册照片
//    "cardStatus":1,    ####照片状态1 正常 0 禁用 -1 删除  不用管 你们使用不到
//    "cardType":18,   ### 不用管 你们使用不到
//    "createTime":"2016-12-05 13:50:11",
//            "guideCardId":23563,   ###图片ID
//    "guideId":"200100013063",    ###司导ID
//    "signStatus":2, ## 0 暂存 1 待审核 2 审核通过 3 审核不通过
//    "updateTime":"2016-12-05 13:50:11"

    public void parse(JSONObject jsonObject){
        this.cardPhotoSrc = jsonObject.optString("cardPhotoSrcL"); //这里取了大图显示，要注意
        this.cardStatus = jsonObject.optInt("cardStatus");
        this.cardType = jsonObject.optInt("cardType");
        this.createTimeStr = jsonObject.optString("createTime");
        this.guideCardId = jsonObject.optString("guideCardId");
        this.signStatus = jsonObject.optInt("signStatus");
        this.signRemark = jsonObject.optString("signRemark");
        this.updateTimeStr = jsonObject.optString("updateTime");
    }



    public static String getUploadJsonContent(List<Photo> list){
        try{
            JSONObject albumJsonObject = new JSONObject();
            JSONArray photosJsonArray = new JSONArray();
            for (int i=0;i<list.size();i++){
                Photo photo = list.get(i);
                if(photo.photoType==ADD_IMAGE_ICON){
                    continue;
                }
                if(TextUtils.isEmpty(photo.cardPhotoSrc)){
                    continue;
                }
                JSONObject photoJson = new JSONObject();
                photoJson.put("cardPhotoSrc",photo.cardPhotoSrc);
                if(!TextUtils.isEmpty(photo.guideCardId)){
                    photoJson.put("guideCardId",photo.guideCardId);
                }
                photosJsonArray.put(photoJson);
            }
            albumJsonObject.put("photosList",photosJsonArray);
            return albumJsonObject.toString();
        }catch (Exception e){
        }
        return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cardPhotoSrc);
        dest.writeInt(this.cardStatus);
        dest.writeInt(this.cardType);
        dest.writeString(this.createTimeStr);
        dest.writeString(this.guideCardId);
        dest.writeInt(this.orderIndex);
        dest.writeString(this.signRemark);
        dest.writeInt(this.signStatus);
        dest.writeString(this.updateTimeStr);
        dest.writeInt(this.photoType);
        dest.writeInt(this.uploadStatus);
        dest.writeString(this.uploadPercent);
        dest.writeString(this.localFilePath);
        dest.writeInt(this.unquineId);
    }

    public Photo() {
    }

    protected Photo(Parcel in) {
        this.cardPhotoSrc = in.readString();
        this.cardStatus = in.readInt();
        this.cardType = in.readInt();
        this.createTimeStr = in.readString();
        this.guideCardId = in.readString();
        this.orderIndex = in.readInt();
        this.signRemark = in.readString();
        this.signStatus = in.readInt();
        this.updateTimeStr = in.readString();
        this.photoType = in.readInt();
        this.uploadStatus = in.readInt();
        this.uploadPercent = in.readString();
        this.localFilePath = in.readString();
        this.unquineId = in.readInt();
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
