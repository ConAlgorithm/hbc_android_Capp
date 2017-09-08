package com.huangbaoche.hbcframe.data.net;
/**
 * 
  * @ClassName ExceptionInfo
  * @Description 错误信息
  * @author aceway-liwei
  * @date 2012-6-25 上午11:26:50
  *
 */
public class ExceptionInfo {

	public int state;
	
	public Exception exception;

	public String errorCode;
	
	public String uuid;//服务器返回的错误ID

	public Throwable throwable;
	
	public ExceptionInfo(int state,Exception e) {
		this.state=state;
		this.exception=e;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	@Override
	public String toString() {
		return "ExceptionInfo ErrorState="+state+" Exception = "+exception;
	}
}
