package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqiang on 17/8/26.
 */

public class SearchGuideBean implements Serializable {
    public int totalSize;
    public List<GuideSearchItemBean> resultBean;

    public static class GuideSearchItemBean{
        public String avatar;//
        public int cityId;
        public String cityName;
        public int continentId;
        public String continentName;
        public int countryId;
        public String countryName;
        public int gender;
        public String guideId;
        public List<String> guideLabelList;
        public String guideName;
        public String guideNo;
        public String keyword;//本地封装,非接口返回
    }
}
