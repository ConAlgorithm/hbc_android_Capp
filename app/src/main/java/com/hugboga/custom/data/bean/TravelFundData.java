package com.hugboga.custom.data.bean;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.utils.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/5/26.
 */
public class TravelFundData implements Serializable {

    private int fundAmount;//可用旅游基金
    private int listCount;//个数
    private String effectiveDate;//有效期
    private ArrayList<TravelFundBean> listData;
    private RewardFields rewardFields;

    private int invitedUserCount;//邀请用户个数
    private int invitationAmount;//通过邀请获取的金额
    private int logsCount;//个数
    private ArrayList<TravelFundBean> logs;



    public ArrayList<TravelFundBean> getListData() {
        return listData;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public int getListCount() {
        return listCount;
    }

    public String getFundAmount() {
        return String.valueOf(fundAmount);
    }

    public int getFundAmountInt() {
        return fundAmount;
    }

    public ArrayList<TravelFundBean> getLogs() {
        return logs;
    }

    public String getLogsCount() {
        return String.valueOf(logsCount);
    }

    public String getInvitedUserCount() {
        return String.valueOf(invitedUserCount);
    }

    public String getInvitationAmount() {
        return String.valueOf(invitationAmount);
    }

    public RewardFields getRewardFields() {
        return rewardFields;
    }

    public static class TravelFundBean implements Serializable {
        private double amount;//金额 未注册/注册两种情况都是-1， 首次使用返现是大于0值
        private String avatar;
        private String createDate;//时间
        private String desc;//描述
        private String orderNo;//订单编号，source为3时会有
        private String username;
        private int gender;
        private int source;

        private String type;//记录类型 未注册/注册/首次使用返现
        private String userName;
        private String updateTime;

        public double getAmount() {
            return amount;
        }

        public String getType() {
            return type;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getCreateDate() {
            return createDate;
        }

        public String getDesc() {
            return desc;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public String getUserName() {
            return userName;
        }

        public String getUsername() {
            return username;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        /**
         * 性别：0.保密、1.男、2.女；
         * */
        public String getGender() {
            String result = null;
            switch (gender) {
                case 0:
                    result = MyApplication.getAppContext().getString(R.string.gender_male);
                    break;
                case 1:
                    result = MyApplication.getAppContext().getString(R.string.gender_female);
                    break;
                case 2:
                    result = MyApplication.getAppContext().getString(R.string.gender_secrecy);
                    break;
                default:
                    result = "";
                    break;
            }
            return result;
        }
    }

    public static class RewardFields implements Serializable {
        private String couponAmount;         // 600元
        private String rewardAmountPerOrder; // 30元
        private String rewardRatePerOrder;   // 5%
        public String headline;              // 标题
        public String subhead;               // 副标题
        public String name;                  // 名字
        public String nickName;              // 昵称

        public String getRewardRatePerOrder() {
            return rewardRatePerOrder;
        }

        public String getRewardAmountPerOrder() {
            return rewardAmountPerOrder;
        }

        public String getCouponAmount() {
            return couponAmount;
        }
    }
}
