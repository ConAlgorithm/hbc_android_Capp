package com.huangbaoche.hbcframe.data.net;

public class ExceptionErrorCode {
	/** 正常 */
	public static final int REQUEST_OK = 0;

	/** 解析错误 */
	public static final int ERROR_CODE_PARSE = 2;
	/** 网络不可用 */
	public static final int ERROR_CODE_NET_UNAVAILABLE = 3;
	/** 联网超时 */
	public static final int ERROR_CODE_NET_TIMEOUT = 4;
	/** 服务器 404 */
	public static final int ERROR_CODE_NET_NOTFOUND = 5;
	/** URL 错误 */
	public static final int ERROR_CODE_URL = 6;
	/** 服务器端返回的错误 */
	public static final int ERROR_CODE_SERVER = 7;
	/** 联网失败 */
	public static final int ERROR_CODE_NET = 8;
	/** 证书*/
	public static final int ERROR_CODE_SSL = 9;
	/** 其他 */
	public static final int ERROR_CODE_OTHER = 10;

	/** 未捕获异常 */
	public static final int ERROR_CODE_UNCATCH = 11;
}
