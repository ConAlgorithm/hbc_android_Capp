package com.huangbaoche.hbcframe.data.parser;

import com.huangbaoche.hbcframe.HbcConfig;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

/**
 * 解析器实现类
 * Created by admin on 2016/2/24.
 */
public abstract class ImplParser implements InterfaceParser{

    // 服务端返回的标准格式解析器
    private ImplParser serverParser ;

    /**
     * 检查请求相应头等处理
     *
     * @param request
     * @throws Throwable
     */
    public void checkResponse(UriRequest request) throws Throwable{};

    /**
     * 转换result为resultType类型的对象
     *
     * @param resultType  返回值类型(可能带有泛型信息)
     * @param resultClass 返回值类型
     * @param result      字符串数据
     * @return
     * @throws Throwable
     */
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable{
        JSONObject jsonObject = new JSONObject(result);
        Object data = getServerParser().parseObject(jsonObject);
        if (data instanceof JSONObject) {
            JSONObject dataObj = (JSONObject) data;
            return parseObject(dataObj);
        } else if (data instanceof JSONArray) {
            JSONArray dataArray = (JSONArray) data;
            return parseArray(dataArray);
        } else if (data instanceof String) {
            return parseString((String) data);
        }
        return result;
    }

    /**
     * 实现方法 解析数据
     * @param obj jsonObj json对象，data的内容
     * @return 解析后的数据源可以是Bean 或者 String，list 但是要跟 request的 类型一致
     * @throws Throwable
     */
    public abstract Object parseObject(JSONObject obj) throws Throwable;

    /**
     * 解析方法，如果返回的data是array格式的重载此方法
     * @param array jsonArray 数据源
     * @return 解析后的数据源可以是Bean 或者 String，list 但是要跟 request的 类型一致
     * @throws Throwable
     */

    public Object parseArray(JSONArray array) throws Throwable{
        return  array.toString();
    }

    /**
     * 解析方法，如果返回的data是String格式的重载此方法
     * @param string 数据源
     * @return 解析后的数据源可以是Bean 或者 String，list 但是要跟 request的 类型一致
     * @throws Throwable
     */
    public Object parseString(String string) throws Throwable{
        return  string;
    }

    /**
     * 获取服务器数据解析的标准格式解析器
     * @return
     */
    public ImplParser getServerParser() {
        if(serverParser==null){
            try {
                serverParser = (ImplParser) HbcConfig.parser.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return serverParser;
    }


    //自定义 服务端返回的标准格式解析器
    public void setServerParser(ImplParser serverParser) {
        this.serverParser = serverParser;
    }

}
