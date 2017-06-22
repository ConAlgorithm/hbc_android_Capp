package com.hugboga.custom.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.request.RequestChatOrderDetail;
import com.hugboga.custom.data.request.RequestIMOrder;
import com.hugboga.custom.data.request.RequestNIMBlackMan;
import com.hugboga.custom.data.request.RequestNIMUnBlackMan;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiFeedbackUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.widget.CountryLocalTimeView;
import com.hugboga.im.ImHelper;
import com.hugboga.im.ImObserverHelper;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.permission.MPermission;
import com.netease.nim.uikit.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.session.SessionEventListener;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.fragment.MessageFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by on 16/8/9.
 */
public class NIMChatActivity extends BaseActivity implements MessageFragment.OnFragmentInteractionListener, ImObserverHelper.OnUserStatusListener, ImObserverHelper.OnUserInfoListener{

    private final int BASIC_PERMISSION_REQUEST_CODE = 100;

    private MessageFragment messageFragment;

    private String sessionId;

    /**
     * @param context
     * @param contactId
     */
    public static void start(Context context, String contactId) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.setClass(context, NIMChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
        ImHelper.forceUpdateUserInfo(contactId);
    }



    @Bind(R.id.imchat_viewpage_layout)
    RelativeLayout viewPageLayout;
    @Bind(R.id.imchat_viewpage)
    ViewPager viewPage; //订单展示
    @Bind(R.id.imchat_point_layout)
    LinearLayout pointLayout; //小点容器

    @Bind(R.id.im_emptyview)
    TextView emptyView; //小点容器

    @Bind(R.id.header_right_btn)
    ImageView header_right_btn;

    @Bind(R.id.imchat_local_time_view)
    CountryLocalTimeView localTimeView;

    private String userId; //用户ID
    private String targetType; //目标类型
    private int inBlack;//标识对方是否被自己拉黑，1是 0否

    private String nationalFlag;
    private int timediff;
    private int timezone;
    private String cityName;
    private String countryName;

    ImObserverHelper imObserverHelper;

    @Override
    public int getContentViewId() {
        return R.layout.activity_nimchat;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initView();

        setImObservers();
        requestBasicPermission();
    }

    private void setImObservers() {
        imObserverHelper = new ImObserverHelper();
        imObserverHelper.setOnUserInfoListener(this);
        imObserverHelper.setOnUserStatusListener(this);

        imObserverHelper.registerUserInfo(sessionId);
        imObserverHelper.registerUserStatusObservers(true);
    }

    private void initView() {
        initDefaultTitleBar();
        fgRightTV.setVisibility(GONE);
        header_right_btn.setImageResource(R.mipmap.topbar_more);
        header_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        if (!TextUtils.isEmpty(targetType) && "3".equals(targetType)) {
            header_right_btn.setVisibility(GONE);
        }

         addConversationFragment();
    }

    private void addConversationFragment(){
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        sessionId = arguments.getString(Extras.EXTRA_ACCOUNT);
        messageFragment = new MessageFragment();
        messageFragment.setArguments(arguments);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction =  fm.beginTransaction();
        transaction.add(R.id.conversation_container,messageFragment);
        transaction.commitAllowingStateLoss();

        NimUIKit.setSessionListener(new SessionEventListener() {
            @Override
            public void onAvatarClicked(Context context, IMMessage message) {
                String fromAccount = message.getFromAccount();
                String sessionId = message.getSessionId();
                if(TextUtils.equals(fromAccount,sessionId)){
                    GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
                    params.guideId = userId;
                    Intent intent = new Intent(NIMChatActivity.this, GuideWebDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    startActivity(intent);
                }

            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {

            }
        });
        validateAllowMessage();

    }

    @Override
    public void onResume() {
        super.onResume();
        connectNim();
    }


    /**
     * 是否连接云信
     */
    private void connectNim() {
        StatusCode status = NIMClient.getStatus();
        if(status != StatusCode.LOGINED && status!=StatusCode.CONNECTING){
            IMUtil.getInstance().connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 解析用户ID信息
     */
    private void getUserInfoToOrder() {
        resetRightBtn();
        initRunningOrder(); //构建和该用户之间的订单
    }

    private void setOrderData(ChatBean chatBean){
        userId = chatBean.targetId;
        fgTitle.setText(chatBean.targetName); //设置标题
        targetType = String.valueOf(chatBean.getTargetType());
        inBlack = chatBean.inBlack;
        nationalFlag = chatBean.flag;
        timediff = chatBean.timediff;
        timezone = chatBean.timezone;
        cityName = chatBean.city_name;
        countryName = chatBean.country_name;
        localTimeView.setData(nationalFlag, timediff, timezone, cityName, countryName);
        getUserInfoToOrder();
    }

    private void resetRightBtn() {
        if (!TextUtils.isEmpty(targetType) && "3".equals(targetType)) {//3.客服 1.用户
            header_right_btn.setVisibility(GONE); //显示历史订单按钮
        } else {
            header_right_btn.setVisibility(View.VISIBLE); //显示历史订单按钮
        }
    }

    /**
     * 展示司导和用户之间的订单
     */
    private void initRunningOrder() {
        loadImOrder(); //显示聊天订单信息
    }

    /**
     * 基本权限管理
     */
    private void requestBasicPermission() {
        MPermission.with(NIMChatActivity.this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE
                )
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess(){
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed(){
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.grant_fail_title);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            dialog.setMessage(R.string.grant_fail_phone1);
        } else {
            dialog.setMessage(R.string.grant_fail_im);
            dialog.setPositiveButton(R.string.grant_fail_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestBasicPermission();
                }
            });
        }
        dialog.show();
    }

    /**
     * 根据数据刷新界面
     * @param orders
     */
    private void flushOrderView(ArrayList<OrderBean> orders) {
        if (orders != null && orders.size() > 0) {
            //有订单数据
            viewPageLayout.setVisibility(View.VISIBLE);
            viewPage.setAdapter(new IMOrderPagerAdapter(orders));
            viewPage.addOnPageChangeListener(onPageChangeListener);
            messageFragment.messageListScrollToBottom();
        } else {
            //无订单数据
            viewPageLayout.setVisibility(GONE);
        }
    }

    /**
     * 获取构建viewPage数据
     * @param datas
     * @return
     */
    private List<View> getOrderViews(ArrayList<OrderBean> datas) {
        List<View> views = new ArrayList<>();
        pointLayout.removeAllViews();
        for (OrderBean orderBean : datas) {
            View view = View.inflate(NIMChatActivity.this, R.layout.im_chat_orders_item, null);
            //设置状态
            TextView textView = (TextView) view.findViewById(R.id.im_chat_orders_item_state);
            textView.setText(getOrderStatus(textView, orderBean.orderStatus));
            //订单类型
            TextView textViewtype = (TextView) view.findViewById(R.id.im_chat_orders_item_ordertime);
            textViewtype.setText(getTypeStr(orderBean));
            //时间
            TextView textViewTime = (TextView)view.findViewById(R.id.im_chat_orders_item_address0);
            textViewTime.setText(getAddr(orderBean));
            //订单地址1
            TextView textViewAddr1 = (TextView) view.findViewById(R.id.im_chat_orders_item_address1);
            textViewAddr1.setText(getAddr1(orderBean));
            //订单地址2
            TextView textViewAddr2 = (TextView) view.findViewById(R.id.im_chat_orders_item_address2);
            textViewAddr2.setText(getAddr2(orderBean));
            views.add(view);
            //设置红点
            View viewp = View.inflate(NIMChatActivity.this, R.layout.im_chat_orders_point, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(3, 2, 3, 2);
            viewp.setLayoutParams(layoutParams);
            pointLayout.addView(viewp);
        }
        if (pointLayout.getChildCount() > 0) {
            pointLayout.getChildAt(0).setSelected(true);
        }
        return views;
    }

    private String getTypeStr(OrderBean orderBean) {
        return getOrderTypeStr(orderBean.orderType);
    }

    public String getOrderTypeStr(int type) {
        String result = "";
        switch (type) {
            case 1:
                result = "中文接机";
                break;
            case 2:
                result = "中文送机";
                break;
            case 3:
                result = "按天包车游";
                break;
            case 4:
                result = "单次接送";
                break;
            case 5:
            case 6:
                result = "线路包车游";
                break;
        }
        return result;
    }

    private String getAddr(OrderBean orderBean) {
        StringBuilder sb = new StringBuilder();
        if (orderBean.orderType == 1 || orderBean.orderType == 2 || orderBean.orderType == 4) {
            sb.append("时间：");
            try {
                sb.append(DateUtils.getStrWeekFormat3(orderBean.serviceTime));
            } catch (Exception e) {
               e.printStackTrace();
            }
        } else if (orderBean.orderType == 3) {
            sb.append("路线：");
            sb.append(orderBean.serviceCityName + " - " + orderBean.serviceEndCityName);
        } else {
            sb.append("路线：");
            sb.append(orderBean.lineSubject);
        }
        return sb.toString();
    }

    private String getAddr1(OrderBean orderBean) {
        StringBuilder sb = new StringBuilder();
        if (orderBean.orderType == 1 || orderBean.orderType == 2 || orderBean.orderType == 4) {
            sb.append("出发：");
            sb.append(orderBean.startAddress);
        } else {
            sb.append("日期：");
            sb.append(DateUtils.getPointStrFromDate2(orderBean.serviceTime));
            if (orderBean.isHalfDaily == 1) {
                sb.append(String.format(" （%1$s天）", "0.5"));
            } else if (orderBean.totalDays == 1) {
                sb.append(String.format(" （%1$s天）", orderBean.totalDays));
            } else {
                sb.append(" - ");
                sb.append(DateUtils.getPointStrFromDate2(orderBean.serviceEndTime));
                sb.append(String.format(" （%1$s天）", orderBean.totalDays));
            }
        }
        return sb.toString();
    }

    private String getAddr2(OrderBean orderBean) {
        StringBuilder sb = new StringBuilder();
        if (orderBean.orderType == 1 || orderBean.orderType == 2 || orderBean.orderType == 4) {
            sb.append("到达：");
            sb.append(orderBean.destAddress);
        }
        return sb.toString();
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            resetPoint(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 滑动小红点位置
     *
     * @param position
     */
    private void resetPoint(int position) {
        int views = pointLayout.getChildCount();
        for (int i = 0; i < views; i++) {
            pointLayout.getChildAt(i).setSelected(false);
        }
        pointLayout.getChildAt(position).setSelected(true);
    }



    class IMOrderPagerAdapter extends PagerAdapter {

        List<View> views;

        IMOrderPagerAdapter(ArrayList<OrderBean> datas) {
            this.views = getOrderViews(datas);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position), 0);
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }


    /**
     * 右上角的菜单，取消订单 联系客服
     */
    private PopupWindow popup;
    View menuLayout;
    public void showPopupWindow() {
        if (popup != null && popup.isShowing()) {
            return;
        }
        if (menuLayout == null) {
            menuLayout  = LayoutInflater.from(NIMChatActivity.this).inflate(R.layout.popup_top_right_menu, null);
        }
        TextView cancelOrderTV = (TextView)menuLayout.findViewById(R.id.cancel_order);
        TextView commonProblemTV = (TextView)menuLayout.findViewById(R.id.menu_phone);
        if(inBlack == 1){
            cancelOrderTV.setText("解除拉黑");
        }else{
            cancelOrderTV.setText("拉黑该用户");
        }
        if (!TextUtils.isEmpty(targetType) && "3".equals(targetType)) {//3.客服 1.用户
            cancelOrderTV.setVisibility(GONE); //显示历史订单按钮
        } else {
            cancelOrderTV.setVisibility(View.VISIBLE); //显示历史订单按钮
        }
        commonProblemTV.setText("历史订单");

        if (popup != null) {
            popup.showAsDropDown(header_right_btn,0, 0);
            return;
        }
        popup = new PopupWindow(menuLayout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAsDropDown(header_right_btn,0,0);

        menuLayout.findViewById(R.id.bg_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        cancelOrderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inBlack == 0) {
                    AlertDialogUtils.showAlertDialog(NIMChatActivity.this, getString(R.string.black_man), "确认拉黑", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        cancelOrderTV.setText("解除拉黑");
                            RequestNIMBlackMan requestBlackMan = new RequestNIMBlackMan(NIMChatActivity.this, userId);
                            HttpRequestUtils.request(NIMChatActivity.this, requestBlackMan, new HttpRequestListener() {
                                @Override
                                public void onDataRequestSucceed(BaseRequest request) {
                                    ApiReportHelper.getInstance().addReport(request);
                                    inBlack = 1;
                                }

                                @Override
                                public void onDataRequestCancel(BaseRequest request) {

                                }

                                @Override
                                public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

                                }
                            });
                            dialog.dismiss();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }else{
                    RequestNIMUnBlackMan requestUnBlackMan = new RequestNIMUnBlackMan(NIMChatActivity.this, userId);
                    HttpRequestUtils.request(NIMChatActivity.this, requestUnBlackMan, new HttpRequestListener() {
                        @Override
                        public void onDataRequestSucceed(BaseRequest request) {
                            ApiReportHelper.getInstance().addReport(request);
                            inBlack = 0;
                        }

                        @Override
                        public void onDataRequestCancel(BaseRequest request) {

                        }

                        @Override
                        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

                        }
                    });

                }
                popup.dismiss();

            }
        });
        commonProblemTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MLog.e("进入历史订单列表");
                Bundle bundle = new Bundle();
                bundle.putInt(NewOrderActivity.SEARCH_TYPE, NewOrderActivity.SearchType.SEARCH_TYPE_HISTORY.getType());
                bundle.putString(NewOrderActivity.SEARCH_USER, userId);
                Intent intent = new Intent(v.getContext(), NewOrderActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                popup.dismiss();
            }
        });
    }


    /**
     * 刷新IM聊天订单
     */
    private void loadImOrder() {
        RequestIMOrder requestIMOrder = new RequestIMOrder(NIMChatActivity.this, userId);
        HttpRequestUtils.request(NIMChatActivity.this, requestIMOrder, orderListener);
    }

    HttpRequestListener orderListener = new HttpRequestListener() {
        @Override
        public void onDataRequestSucceed(BaseRequest request) {
            Object[] objs = ((RequestIMOrder) request).getData();
            if (objs != null && objs[1] != null) {
                ArrayList<OrderBean> datas = (ArrayList) objs[1];
                flushOrderView(datas);

            }
            MLog.e("orderListener-onDataRequestSucceed");


        }

        @Override
        public void onDataRequestCancel(BaseRequest request) {
            MLog.e("orderListener-onDataRequestCancel");
        }

        @Override
        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            MLog.e("orderListener-onDataRequestError");
            ErrorHandler handler = new ErrorHandler(NIMChatActivity.this, this);
            handler.onDataRequestError(errorInfo, request);
        }
    };

//    HttpRequestListener imClearListener = new HttpRequestListener() {
//        @Override
//        public void onDataRequestSucceed(BaseRequest request) {
//            MLog.e("清除IM消息成功");
//        }
//
//        @Override
//        public void onDataRequestCancel(BaseRequest request) {
//
//        }
//
//        @Override
//        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
//            ErrorHandler handler = new ErrorHandler(NIMChatActivity.this, this);
//            handler.onDataRequestError(errorInfo, request);
//        }
//    };

    public static String getOrderStatus(TextView textView, OrderStatus orderStatus){
        textView.setTextColor(0xFFADADAD);
        switch (orderStatus) {
            case INITSTATE:     // 未支付
                return  "未支付";
            case PAYSUCCESS:
            case AGREE:    // 已支付--服务中
                return "未开始";
            case ARRIVED:
            case SERVICING:
            case COMPLAINT:    // 已支付--服务中
            case NOT_EVALUATED:     // 未评价
                textView.setTextColor(0xFFF9B800);
                return "正在服务";
            case COMPLETE:     // 已评价（已完成）
                return "已完成";
            case CANCELLED:     // 已取消（未支付）
                return "已取消";
            case REFUNDED: // 已退款（已支付）
                return "已退款";
            default:
                return "";
        }
    }


    @Override
    public void onBackPressed() {
        if (messageFragment == null || !messageFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (messageFragment != null) {
            messageFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        imObserverHelper.unRegisterUserInfo();
        imObserverHelper.registerUserStatusObservers(false);
        if (localTimeView != null) {
            localTimeView.setStop(true);
        }
        View view = this.getCurrentFocus();
        if(view!=null){
            view.clearFocus();
            hideInputMethod(view);
        }
        super.onDestroy();
    }


    @Override
    public void onSendMessageFailed(int code, String message) {
        if(code!=7101){
            Toast.makeText(MyApplication.getAppContext(),"发送消息失败请稍候重试",Toast.LENGTH_SHORT).show();
            ApiFeedbackUtils.requestIMFeedback(2, String.valueOf(code));
        }

    }

    @Override
    public boolean isAllowMessage() {
        return chatBean==null?true:chatBean.isCancel==0;
    }

    @Override
    public void onSendMessageSuccess() {
        MLog.i("nim send message success!");
    }


    @Override
    public String getEventSource() {
        return "IM聊天页";
    }

    ChatBean chatBean;
    private void validateAllowMessage(){
        RequestChatOrderDetail requestChatOrderDetail = new RequestChatOrderDetail(MyApplication.getAppContext(),sessionId);
        HttpRequestUtils.request(this, requestChatOrderDetail, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                chatBean = (ChatBean) request.getData();
                setOrderData(chatBean);
            }
            @Override
            public void onDataRequestCancel(BaseRequest request) {
            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            }
        });
    }

    @Override
    public void onPostUserNick(String nickName) {
        setTitle(nickName);
    }

    @Override
    public void onPostUserStatus(StatusCode code) {
        if(code!=StatusCode.LOGINED && code!=StatusCode.CONNECTING){
            ApiFeedbackUtils.requestIMFeedback(3,String .valueOf(code.getValue()));
        }
        if (code.wontAutoLogin()) {
            //IMUtil.getInstance().connect();
            if(emptyView!=null){
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText("聊天账号被踢出登录");
            }
        } else {
            if (code == StatusCode.NET_BROKEN) {
                if(emptyView!=null){
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(R.string.no_network);
                }
            } else if (code == StatusCode.UNLOGIN) {
                IMUtil.getInstance().connect();
                if(emptyView!=null){
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("正在登录聊天，请稍候...");
                }
            } else if (code == StatusCode.CONNECTING) {
                if(emptyView!=null){
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("正在重连聊天服务器，请稍候...");
                }
            } else if (code == StatusCode.LOGINING) {
                if(emptyView!=null){
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText("正在登录聊天，请稍候...");
                }
            } else {
                if(emptyView!=null){
                    emptyView.setVisibility(View.GONE);
                }
            }
        }
    }
}
