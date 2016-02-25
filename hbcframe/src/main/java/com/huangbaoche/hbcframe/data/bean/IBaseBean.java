package com.huangbaoche.hbcframe.data.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Bean 的 接口
 */

public interface IBaseBean extends Serializable {
	/**
	 * 
	  * @Title parser
	  * @Description bean 的解析，json格式
	  * @param jsonObj    参数
	  * @date 2013-12-25 下午05:29:00
	 */
	public void parser(JSONObject jsonObj) throws JSONException;
}
