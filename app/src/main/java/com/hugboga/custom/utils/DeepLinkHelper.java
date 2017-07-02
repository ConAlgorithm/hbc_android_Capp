package com.hugboga.custom.utils;

import android.app.Notification;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hugboga.custom.action.ActionController;
import com.hugboga.custom.action.data.ActionBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangqiang on 17/6/30.
 */

public class DeepLinkHelper {

    private final static String fileName = "app.json";
    ActionBean actionBean = null;
    String url="";
    ParseListner parseListner;
    Context mContext;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String pathJson = msg.getData().getString("pathJson");
            try {
                actionBean = parseUrl(url,pathJson);
                if(parseListner!= null){
                    parseListner.getParseBack(actionBean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    //static DeepLinkUtils deepLinkUtils;

    private DeepLinkHelper(Builder builder) {
        this.url = builder.url;
        this.parseListner = builder.parseListner;
        this.mContext = builder.mContext;
    }
    public void startParse(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String pathJson = getJson(fileName);
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("pathJson",pathJson);
                message.setData(bundle);

                handler.sendMessage(message);
            }
        }).start();
    }
    public void setUrl(String _url){
        this.url = _url;
    }

    /*url  https://m-test.huangbaoche.com/app/home
    *      https://m-test.huangbaoche.com/aboutus
    *https://m-test.huangbaoche.com/app/home.html?capp={xx}
    * _url = m-test.huangbaoche.com/app/home.html?capp={xx}
    * */
    public  ActionBean parseUrl(String _url,String pathJson) throws JSONException {


        String path = "";
        String webUrl = _url;

        String jsonData = Uri.parse(_url).getQueryParameter("capp");

        List<String> pathSegments =  Uri.parse(_url).getPathSegments();
        for(int i=0;i<pathSegments.size();i++){
            path +=pathSegments.get(i);
            path +="/";
        }
        if(path.charAt(path.length()-1) == '/'){
            path = (String) path.subSequence(0,path.length()-1);
        }

        JSONObject jsonObject = new JSONObject(pathJson);
        JSONObject jsonObject1 = null;
        String tValue ="";
        String vValue ="";
        Iterator<String> iterator = jsonObject.keys();
        String key = null;
        while(iterator.hasNext()){
            key = iterator.next();
            if(key.equals(path)){
                jsonObject1 = jsonObject.getJSONObject(key);
                break;
            }
        }

        if(jsonObject1 != null){
            tValue = jsonObject1.optString("t");
            vValue = jsonObject1.optString("v");
        }


        if(jsonData != null){
            //final ActionBean actionBean = (ActionBean) JsonUtils.fromJson(jsonData, ActionBean.class);
            ActionBean actionBean = new ActionBean();
            actionBean.data = jsonData;
            actionBean.type = tValue;
            actionBean.vcid = vValue;
            actionBean.url =  webUrl;
            actionBean.source = "外部调起";
            ActionController actionFactory = ActionController.getInstance();
            actionFactory.doAction(mContext,actionBean);
        }

        return actionBean;
    }

    //解析assert中json文件
    public String getJson(String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = mContext.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public void setParseListner(ParseListner parseListner){
        this.parseListner = parseListner;
    }
    public interface ParseListner{
        public void getParseBack(ActionBean actionBean);
    }
    public static class Builder{
        String url="";
        ParseListner parseListner = null;
        Context mContext;
        public Builder url(String url){
            this.url=url;
            return this;
        }
        public Builder parseListner(ParseListner parseListner){
            this.parseListner=parseListner;
            return this;
        }
        public Builder context(Context context){
            this.mContext=context;
            return this;
        }
        public DeepLinkHelper build(){
            return new DeepLinkHelper(this);
        }
    }
}

