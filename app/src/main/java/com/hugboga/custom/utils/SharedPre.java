package com.hugboga.custom.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;


/**
 * Save Data To SharePreference Or Get Data from SharePreference
 * 
 *通过SharedPreferences来存储数据，自定义类型
 */
public class SharedPre {
	private Context ctx;
	private String FileName = "hugboga";
	public static String ACCESSKEY ="accesskey";
	public static String USERID ="userId";
	public static String USERTOKEN ="userToken";
	public static String IM_TOKEN ="im_Token";
	public static String USERAVATAR ="avatar";
	public static String NICKNAME ="nickname";
	public static String PHONE ="login_phone";
	public static String CODE ="login_code";
	public static String VERSION ="app_version";
	public static String IS_NEW_VERSION ="is_new_version";
	public static String IS_WEAK_PSW ="is_weak_psw";
	public static String WEAK_PSW_MESSAGE ="weak_psw_message";
	public static String ORDER_POINT_NUM ="order_point_num";
	public static String RESOURCES_H5_VERSION ="resources_h5_version";
	public static String RESOURCES_DB_VERSION ="resources_db_version";
	public static String RESOURCES_CITY_HISTORY ="resources_city_history";
	public static String RESOURCES_AIRPORT_HISTORY ="resources_airport_history";
	public static String RESOURCES_PLACE_HISTORY ="resources_place_history";

	public SharedPre(Context ctx) {
		this.ctx = ctx;
	}

	/**
	 * Set int value into SharePreference
	 * 
	 * @param key
	 * @param value
	 */
	//通过SharedPreferences来存储键值对
	public void saveIntValue(String key, int value) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePre.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public void writeDownStartApplicationTime() {
		SharedPreferences sp = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE);
		long now = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		//Date now = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:hh-mm-ss");
		SharedPreferences.Editor editor = sp.edit();
		//editor.putString("启动时间", now.toString());
		editor.putLong("nowtimekey", now);
		editor.commit();
		
	}
	
	/**
	 * Set String value into SharePreference
	 * 
	 * @param key
	 * @param value
	 */
	public void saveStringValue(String key, String value) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePre.edit();
		editor.putString(key, value);
		editor.commit();
	}
	/**
	 * Set String value into SharePreference
	 * 

	 * @param key
	 * @param value
	 */
	public void saveLongValue(String key, long value) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePre.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * Set Boolean value into SharePreference
	 * 

	 * @param key
	 * @param value
	 */
	public void saveBooleanValue(String key, boolean value) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePre.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	
	/**
	 * Set  value into SharePreference
	 * 
	 * @param key
	 * @param value
	 */
	public void saveStringSetValue(String key, Set<String> value) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePre.edit();
		editor.putStringSet(key, value);
		editor.commit();
	}

	/**
	 * Remove key from SharePreference
	 * 
	 * @param key
	 */
	public void removeKey(String key) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePre.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 *查询是否包含key的值，存在为true，否则为false
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(String key) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		return sharePre.contains(key);
	}

	/**
	 * Get all value
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAllMap() {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		return (Map<String, Object>) sharePre.getAll();
	}

	/**
	 * Get Integer Value
	 * @param key
	 * @return
	 */
	public Integer getIntValue(String key,int defValue) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		return sharePre.getInt(key, defValue);
	}
	/**
	 * Get Integer Value
	 * 
	 * @param key
	 * @return
	 */
	public Integer getIntValue(String key) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		return sharePre.getInt(key, -1);
	}

	/**
	 * Get String Value
	 * 通过输入的key来获得value
	 * @param key
	 * @return
	 */
	public String getStringValue(String key) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		return sharePre.getString(key, null);
	}
	/**
	 * Get String Value
	 * 通过输入的key来获得value, 若value不存在，返回自定义的默认值defaultstr
	 * @param key
	 * @param defaultstr
	 * @return
	 */
	public String getStringValue(String key,String defaultstr) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		return sharePre.getString(key, defaultstr);
	}

	public Boolean getBooleanValue(String key) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		return sharePre.getBoolean(key, false);
	}
	public long getLongValue(String key) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		return sharePre.getLong(key, 0);
	}

	public Set<String> getStringSetValue(String key){
		SharedPreferences sharePre = ctx.getSharedPreferences(FileName,
				Context.MODE_PRIVATE);
		return sharePre.getStringSet(key, null);
	}
	/**
	 * Get Value, Remove key
	 * 
	 * @param key
	 * @return
	 */
	public Integer getIntValueAndRemove(String key) {
		Integer value = getIntValue(key);
		removeKey(key);
		return value;
	}
	public void clean() {
		removeKey(USERID);
		removeKey(USERTOKEN);
		removeKey(ACCESSKEY);
		removeKey(PHONE);
		removeKey(CODE);
		removeKey(USERAVATAR);
		removeKey(NICKNAME);
		removeKey(ORDER_POINT_NUM);
		removeKey(IS_WEAK_PSW);
		removeKey(WEAK_PSW_MESSAGE);
	}
}