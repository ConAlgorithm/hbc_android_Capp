package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeData implements Serializable {

    private ArrayList<CityContentItem> cityContentList;

    private ArrayList<String> bannerList;

    @SerializedName("oneDailyMessage")
    private DynamicsData.DynamicsItem dynamicsBean;

    private SalesPromotion salesPromotion;

    public ArrayList<CityContentItem> getCityContentList() {
        return cityContentList;
    }

    public ArrayList<String> getBannerList() {
        return bannerList;
    }

    public DynamicsData.DynamicsItem getDynamicsBean() {
        return dynamicsBean;
    }

    public SalesPromotion getSalesPromotion() {
        return salesPromotion;
    }

    public static class CityContentItem implements Serializable {
        private int cityId;
        private String mainTitle;
        private String subTitle;
        private String picture;
        private String tip;
        private ArrayList<TraveLineItem> traveLineList;

        public String getTip() {
            return tip;
        }

        public int getCityId() {
            return cityId;
        }

        public String getMainTitle() {
            return mainTitle;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public String getPicture() {
            return picture;
        }

        public ArrayList<TraveLineItem> getTraveLineList() {
            return traveLineList;
        }
    }

    public static class TraveLineItem implements Serializable {
        private int lineOrder;

        @SerializedName("lineSubject")
        private String describe;

        private double price;

        public int getLineOrder() {
            return lineOrder;
        }

        public String getDescribe() {
            return describe;
        }

        public double getPrice() {
            return price;
        }
    }

    public static class SalesPromotion implements Serializable {
        private int startSettingId;
        private String activityName;
        private String picture;
        private String urlAddress;

        public int getStartSettingId() {
            return startSettingId;
        }

        public String getActivityName() {
            return activityName;
        }

        public String getPicture() {
            return picture;
        }

        public String getUrlAddress() {
            return urlAddress;
        }
    }

}
