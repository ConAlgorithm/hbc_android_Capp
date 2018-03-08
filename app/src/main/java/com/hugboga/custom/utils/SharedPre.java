package com.hugboga.custom.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hugboga.custom.MyApplication;

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
	private static final int MODE = Context.MODE_PRIVATE;
	public static final String FILENAME = "hugboga";
	public static String ACCESSKEY ="accesskey";
	public static String USERID ="userId";
	public static String USERTOKEN ="userToken";
	public static String IM_TOKEN ="im_Token";
	public static String UNIONID ="unionid";
	public static String USERAVATAR ="avatar";
	public static String NICKNAME ="nickname";
	public static String USERNAME = "username";
	public static String PHONE ="login_phone";
	public static String LOGIN_PHONE ="for_login_phone";
	public static String CODE ="login_code";
	public static String LOGIN_CODE ="for_login_code";
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
	public static String CACHE_SIZE ="cache_size";
	public static String TRAVELFUND ="travelFund";
	public static String COUPONS = "coupons";
	public static String NEEDINITPWD = "needInitPwd";
	public static String TRAVEL_FUND_HINT = "travelFundHint";
	public static String PAY_WECHAT_ORDER_ID = "pay_wechat_order_id";
	public static String PAY_WECHAT_ORDER_TYPE = "pay_wechat_order_type";
	public static String PAY_WECHAT_APITYPE = "pay_wechat_apitype";
	public static String PAY_EXTAR_PARAMS = "pay_extar_params";
	public static String PAY_ORDER = "pay_order";
	public static String GENDER ="gender";
	public static String AGETYPE ="ageType";
	public static String APP_LAUNCH_COUNT ="appLaunchCount";
	public static String USER_TYPE ="userType";

	public static String RIM_USERID ="rim_userId";//融云userid

	public static String NIM_TOKEN ="nim_Token"; //云信token
	public static String NIM_USERID ="nim_userId";//云信用户名

	public static String QY_SERVICE_UNREADCOUNT = "qy_unreadmsg_count";
	public static String QY_GROUP_ID = "qy_group_id";
	public static String IM_CHAT_COUNT = "im_chat_count";//im司导总聊天数

	public static String IM_THE_FIRST_TIME_CHAT = "the_first_time_chat";//判断当前账号是否是第一次聊天
	public SharedPre(Context ctx) {
		this.ctx = ctx;
	}

	/*public void setTravelFundHintIsShow(boolean isShow) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePre.edit();
		editor.putBoolean(TRAVEL_FUND_HINT, isShow);
		editor.commit();
	}

	public boolean isShowTravelFundHint() {
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		return sharePre.getBoolean(TRAVEL_FUND_HINT, false);
	}*/

	/**
	 * Set int value into SharePreference
	 *
	 * @param key
	 * @param value
	 */
	//通过SharedPreferences来存储键值对
	public void saveIntValue(String key, int value) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePre.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void writeDownStartApplicationTime() {
		SharedPreferences sp = ctx.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
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
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
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
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
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
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
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
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
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
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
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
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
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
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
				Context.MODE_PRIVATE);
		return (Map<String, Object>) sharePre.getAll();
	}

	/**
	 * Get Integer Value
	 * @param key
	 * @return
	 */
	public Integer getIntValue(String key,int defValue) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
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
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
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
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
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
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
				Context.MODE_PRIVATE);
		return sharePre.getString(key, defaultstr);
	}

	public Boolean getBooleanValue(String key) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
				Context.MODE_PRIVATE);
		return sharePre.getBoolean(key, false);
	}
	public long getLongValue(String key) {
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
				Context.MODE_PRIVATE);
		return sharePre.getLong(key, 0);
	}

	public Set<String> getStringSetValue(String key){
		SharedPreferences sharePre = ctx.getSharedPreferences(FILENAME,
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
		removeKey(USERNAME);
		removeKey(RIM_USERID);
		removeKey(NIM_TOKEN);
		removeKey(NIM_USERID);
	}

	public static boolean setString(String key, String value) {
		return setString(FILENAME, key, value);
	}

	public static String getString(String key, String defaultValue) {
		return getString(FILENAME, key, defaultValue);
	}

	public static String getString(String name, String key, String defaultValue) {
		Context context = MyApplication.getAppContext();
		SharedPreferences mSharedPreferences = context.getSharedPreferences(name, MODE);
		return mSharedPreferences.getString(key, defaultValue);
	}

	public static boolean setString(String name, String key, String value) {
		Context context = MyApplication.getAppContext();
		SharedPreferences mSharedPreferences = context.getSharedPreferences(name, MODE);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putString(key, value);
		return mEditor.commit();
	}

	public static boolean setInteger(String key, int value) {
		return setInteger(FILENAME, key, value);
	}

	public static int getInteger(String key, int defaultValue) {
		return getInteger(FILENAME, key, defaultValue);
	}

	public static boolean setInteger(String name, String key, int value) {
		Context context = MyApplication.getAppContext();
		SharedPreferences mSharedPreferences = context.getSharedPreferences(name, MODE);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putInt(key, value);
		return mEditor.commit();
	}

	public static int getInteger(String name, String key, int defaultValue) {
		Context context = MyApplication.getAppContext();
		SharedPreferences mSharedPreferences = context.getSharedPreferences(name, MODE);
		return mSharedPreferences.getInt(key, defaultValue);
	}

	public static boolean setLong(String key, long value) {
		return setLong(FILENAME, key, value);
	}

	public static long getLong(String key, long defaultValue) {
		return getLong(FILENAME, key, defaultValue);
	}

	public static boolean setLong(String name, String key, long value) {
		Context context = MyApplication.getAppContext();
		SharedPreferences mSharedPreferences = context.getSharedPreferences(name, MODE);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putLong(key, value);
		return mEditor.commit();
	}

	public static long getLong(String name, String key, long defaultValue) {
		Context context = MyApplication.getAppContext();
		SharedPreferences mSharedPreferences = context.getSharedPreferences(name, MODE);
		return mSharedPreferences.getLong(key, defaultValue);
	}

	public static boolean setBoolean(String key, boolean value) {
		return setBoolean(FILENAME, key, value);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return getBoolean(FILENAME, key, defaultValue);
	}

	public static boolean getBoolean(String name, String key, boolean defaultValue) {
		Context context = MyApplication.getAppContext();
		SharedPreferences mSharedPreferences = context.getSharedPreferences(name, MODE);
		return mSharedPreferences.getBoolean(key, defaultValue);
	}

	public static boolean setBoolean(String name, String key, boolean value) {
		Context context = MyApplication.getAppContext();
		SharedPreferences mSharedPreferences = context.getSharedPreferences(name, MODE);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putBoolean(key, value);
		return mEditor.commit();
	}

	public static boolean delete(String name, String key) {
		Context context = MyApplication.getAppContext();
		SharedPreferences mSharedPreferences = context.getSharedPreferences(name, MODE);
		if (mSharedPreferences.contains(key)) {
			SharedPreferences.Editor mEditor = mSharedPreferences.edit();
			mEditor.remove(key);
			return mEditor.commit();
		} else {
			return false;
		}
	}

	public static boolean clear(String name) {
		Context context = MyApplication.getAppContext();
		SharedPreferences mSharedPreferences = context.getSharedPreferences(name, MODE);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.clear();
		return mEditor.commit();
	}
}