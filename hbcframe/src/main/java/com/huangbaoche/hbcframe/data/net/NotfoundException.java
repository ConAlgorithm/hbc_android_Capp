package com.huangbaoche.hbcframe.data.net;

/**
 *  
  * @ClassName NotfoundException
  * @Description 页面找不到 404 错误
  * @author aceway-liwei
  * @date 2012-6-25 上午11:26:15
  *
 */
public class NotfoundException extends Exception {

	/**
	  * @Fields serialVersionUID : （用一句话描述这个变量表示什么）
	  */
	
	private static final long serialVersionUID = 1L;
	
	public NotfoundException() {
		super();
	}
	
	public NotfoundException(String msg) {
		super(msg);
	}
	
}
