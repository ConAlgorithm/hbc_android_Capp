package com.hugboga.custom.data.bean;

/**
 * Created  on 16/4/23.
 */
public class InsureResultBean implements IBaseBean{
//    {"birthday":"2013-10-10","insuranceUserId":"IU91440316911","name":"李达","passportNo":"G123","sex":1,"userId":"123","userStatus":1},

    public String birthday;
    public String insuranceUserId;
    public String name;
    public String passportNo;
    public int sex;
    public String userId;
    public int userStatus;//1,2 : "购买中"3: "购买成功"4: "购买失败"5: "注销中"6: "注销成功"7: "注销失败" 8: "有问题"
    public int intsource;

    public int isDel; //是否是删除模式  1 是  0 不是

    public int isCheck; //1 是 0 不是

    public String getUserStatusString() {
        String result = null;
        switch (userStatus) {
            case 1:
            case 2:
                result = "购买中";
                break;
            case 3:
                result = "购买成功";
                break;
            case 4:
                result = "购买失败";
                break;
            case 5:
                result = "注销中";
                break;
            case 6:
                result = "注销成功";
                break;
            case 7:
                result = "注销失败";
                break;
            case 8:
                result = "有问题";
                break;
        }
        return result;
    }


}
