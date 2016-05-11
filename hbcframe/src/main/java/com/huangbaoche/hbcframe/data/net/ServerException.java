package com.huangbaoche.hbcframe.data.net;

import org.xutils.ex.HttpException;

/**
 *  
  * @ClassName ServerException
  * @Description 服务器端返回的统一错误提示
  * @author aceway-liwei
  * @date 2012-6-25 上午11:26:15
  *
 */
public class ServerException extends HttpException {

	/**
	  * @Fields serialVersionUID : （用一句话描述这个变量表示什么）
	  */
	
	private static final long serialVersionUID = 1L;
//	private String detailMessage;//这是异常的信息在父类里
	private int code;//错误状态
	private String opr;//错误信息 辅助参数

	public ServerException(int code,String msg ) {
		super(code,msg);
		this.code  = code;
	}
	public ServerException(int code,String msg ,String opr) {
		super(code,msg);
		this.code  = code;
		this.opr = opr;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setOpr(String opr) {
		this.opr = opr;
	}

	public String getOpr() {
		return opr;
	}
	
}
