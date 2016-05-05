package com.hugboga.custom.data.bean;

import java.util.List;

/**
 * Created  on 16/5/4.
 */
public class ADPictureBean {
        public String createTime;
        public String offlineTime;
        public String onlineTime;
        public String projectName;
        public int startSettingId;
        public int status;
        public String updateTime;
        /**
         * activityStartId : 89
         * osType : 1
         * picture : http://fr.dev.hbc.tech/guide/20160421/201604211832357223.jpg
         * property : 720x1280
         */

        public List<PicListBean> picList;

        public static class PicListBean {
            public int activityStartId;
            public int osType;
            public String picture;
            public String property;
        }
    }
