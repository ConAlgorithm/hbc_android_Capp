package com.hugboga.custom.data.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZHZEPHI on 2015/7/29.
 */
public class PushMessage implements IBaseBean {

    public String type; //App根据此参数的内容判断此条message的意图
    public String messageID; //如果有对应的PUSH，此参数必有，且与PUSH的pushID相同
    public String notification; //Android独有。标识此条message是否在通知栏显示
    public String title; //标题。如没有该参数，在通知栏中标题显示为App名称，在App中的弹出框中标题显示为“提示”
    public String content; //描述，App将原样显示其中的内容。如没有则无该参数
    public String accountID; //App将比对此id与当前账号id是否相同，不同将忽略此条message。无该参数表示针对所有账号
    public String vaild; //有效期，日期格式的字符串(yyyy-MM-dd HH:mm:ss)。App将比对本机时间是否在此时间之后，是则忽略此条message。无该参数表示永久有效
    public String[] version; //适用版本。数组中列出所有需要响应此条message的版本号，如果App的当前版本与此参数不匹配则忽略此条message。无该参数表示适用所有版本
    public String force;
    public String url;
    public String orderID;
    public int orderType;
    public int goodsType;

    public void parser(String str){
        try {
            JSONObject obj = new JSONObject(str);
            parser(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    @Override
    public void parser(JSONObject jsonObj) throws JSONException {
        type = jsonObj.optString("type");
        messageID = jsonObj.optString("messageID");
        notification = jsonObj.optString("notification");
        title = jsonObj.optString("title");
        content = jsonObj.optString("content");
        accountID = jsonObj.optString("accountID");
        vaild = jsonObj.optString("vaild");
        JSONArray vaildArray = jsonObj.optJSONArray("version");
        if(vaildArray!=null){
            version = new String[vaildArray.length()];
            for (int i = 0; i < vaildArray.length(); i++) {
                version[i] = vaildArray.get(i).toString();
            }
        }
        force = jsonObj.optString("force");
        url = jsonObj.optString("url");
        orderID = jsonObj.optString("orderNo");
        orderType = jsonObj.optInt("orderType");
        goodsType = jsonObj.optInt("goodsType");
    }

}
