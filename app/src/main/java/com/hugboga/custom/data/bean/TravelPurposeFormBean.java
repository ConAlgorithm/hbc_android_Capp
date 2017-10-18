package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */

public class TravelPurposeFormBean implements Serializable {

    public Integer listCount;           //列表数量

    @SerializedName("listData")
    public List<ListData> listDatas;

    public class ListData implements Serializable{
        public Integer adultNum;            //成人数
        public Integer childNum;            //儿童数
        public String createTime;	        //创建时间
        public Integer id;                  //工单ID
        public String opUserId;	            //操作人ID
        public String opUserName;	        //操作人名称
        public String orderNo;              //订单号
        public String partnerRemark;        //谁同行（多种角色用'|'分割，如：父母|儿童|妻子）
//        public Integer remark;              //内部备注(运营人员添加)
        public String toCity;	            //出行城市名称
//        public Integer toCityId;            //出行城市代码
        public Integer tripDayNum;	        //游玩天数
        public String tripTimeStr;	        //出行日期
        public String updateTime;	    	//更新时间
        public String userAreaCode;	        //用户区号
        public String userMobile;	        //用户电话
        public String userName;             //用户昵称
        public String userRemark;	        //用户备注
        public Integer userStatus;          //用户状态 (0-未沟通； 1-需求中； 2-意向； 3-下定； 4-付款)
        public String workChannel;	        //工单来源编码(1-皇包车客户端公众号；2-今日头条；3-M站；4-PC站；5-CAPP )
        public String workChannelName;      //工单来源名称
        public Integer workStatus;          //工单状态 （-1-删除; 0-未处理; 1-跟进中; 2-放弃）

    }
}
