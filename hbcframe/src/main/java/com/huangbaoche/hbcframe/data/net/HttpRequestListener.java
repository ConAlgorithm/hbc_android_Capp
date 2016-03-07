package com.huangbaoche.hbcframe.data.net;


import com.huangbaoche.hbcframe.data.request.BaseRequest;

/**
 * 
 * @ClassName SyncServicesRequestListener
 * @Description 异步加载的监听器，分别 在加载成功和 出错调用两个方法 ，Baseactivity 无需实现此接口
 * @author aceway-liwei
 * @date 2012-6-5 下午03:44:15
 * 
 */
public interface HttpRequestListener {

	void onDataRequestSucceed(BaseRequest request);

	/**
	 * 请求 被取消
	 * */
	void onDataRequestCancel(BaseRequest request);
	/**
	 * 请求失败
	 * 如果 实现自己的错误处理 重写 相应的错误，其他的 调用
	 * */
	void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request);

}
