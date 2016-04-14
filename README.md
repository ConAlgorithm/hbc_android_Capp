## 皇包车 C端App 

## 目录介绍
* app 主程序项目 目录
* hbcframe 框架层 与业务无关的模块，可复用的都会在这里
* IMKit 荣云IM聊天


## app目录

 * activity ：Activity类  继承 hbcframe 的BaseFragmentActivity
 * adapter ：适配器 继承 hbcframe 的 BaseAdapter(ListView 适用)， ZBaseAdapter(ZSwipeRefreshLayout 适用)
 * alipay : 支付宝
 * constants :常量，项目中尽量不要写魔法数字（不明确意义），用常量代替数字，如果公用写到此处
 * data ： 数据类
  * bean :实体，在网络请求和页面交互中用到
  * event :EventBus 用到，文件可以移到常量constants目录下
  * net ：网络请求，Urllib 所有接口地址， WebAgent ：webview代理
  * parser: bean的解析器，以Parser开头，之后可以更新Gson 或者是 fastJson 
  * request：请求器，以Request开头，每个请求实现一个请求，重写上行参数、请求方法和请求器
 * fragment:页面类，以Fg开头，业务多了，可以按业务再分目录
 * receiver:接受广播，push广播
 * service：服务类，LogServer 统计日志服务，独立运行，在程序崩溃后不会立即停，再提交完最后一次后停止
 * utils:工具类
 * widget：自定义控件
 * wxapi:微信分享，微信支付
    
## hbcframe目录
 * activity ：Activity基类类 
 * adapter ：适配器 继承 hbcframe 的 BaseAdapter(ListView 适用)， ZBaseAdapter(ZSwipeRefreshLayout 适用)
 * data ： 数据类
  * bean :实体，在网络请求和页面交互中用到
  * net ：网络请求
    * HttpRequestUtils ：网络请求主要入口
    * DefaultSSLSocketFactory：Https用到的SSL工程类，从keystore取出ssl所用的key
    * ErrorHandler：错误处理
    * ExceptionErrorCode：异常错误码
    * ExceptionInfo：异常信息类，对异常的封装
    * HbcParamsBuilder：请求器的编译器，对header添加公共参数，拼接HOST，使用SSL
    * HttpRequestListener：请求回调类
    * HttpRequestOption：请求传的配置文件，非必传
    * ServerException：服务器返回的错误信息
  * parser: bean的解析器，以Parser开头，之后可以更新Gson 或者是 fastJson 
  * request：请求器，以Request开头，每个请求实现一个请求，重写上行参数、请求方法和请求器
 * fragment:页面类，以Fg开头，业务多了，可以按业务再分目录
 * page:分页工具类
 * utils:工具类
 * widget：自定义控件

 ## 注意事项   
* BaseRequest 的 Map<String,Object> getDataMap() 在有翻页的时候不要重写，否则每次取出来的Map都是新创建的，导致翻页有问题 
     map的初始化可以放在构造方法里
* 继承BaseRequest 的子类一定要写@HttpRequest 注解，builder = HbcParamsBuilder. class, 公用的处理方法包括拼接host ，添加header公共头信息 ，ssh加密都这个类
* onEvent 注解只能用于private方法
* 在重新完子类的方法后，要调用super.方法，除非有特殊处理

    