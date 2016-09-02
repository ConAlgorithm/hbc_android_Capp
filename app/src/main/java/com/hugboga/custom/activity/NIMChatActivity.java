package com.hugboga.custom.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.data.request.RequestApiFeedback;
import com.hugboga.custom.data.request.RequestBlackMan;
import com.hugboga.custom.data.request.RequestIMClear;
import com.hugboga.custom.data.request.RequestIMOrder;
import com.hugboga.custom.data.request.RequestNIMBlackMan;
import com.hugboga.custom.data.request.RequestNIMClear;
import com.hugboga.custom.data.request.RequestNIMUnBlackMan;
import com.hugboga.custom.data.request.RequestUnBlackMan;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiFeedbackUtils;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.PermissionRes;
import com.hugboga.custom.utils.UIUtils;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nim.uikit.session.SessionEventListener;
import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.fragment.MessageFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import static android.view.View.GONE;

/**
 * Created by on 16/8/9.
 */
public class NIMChatActivity extends BaseActivity implements IMUtil.OnImSuccessListener{

    public static final String ORDER_INFO_KEY = "order_info_key";

    private MessageFragment messageFragment;


    public static void start(Context context, String contactId, SessionCustomization customization, String orderJson) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        intent.putExtra(ORDER_INFO_KEY,orderJson);
        intent.setClass(context, NIMChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
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

    //public final String USER_IM_ADD = "G";
    private boolean isChat = false; //是否开启聊天
    private String userId; //用户ID
    private String imUserId; //用户ID
    private String userAvatar; //用户头像
    private String targetType; //目标类型
    private int inBlack;//标识对方是否被自己拉黑，1是 0否
    private int isHideMoreBtn;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_nimchat);
        ButterKnife.bind(this);
        initView();
        grantAudio();
    }

    private void initView() {
        initDefaultTitleBar();
        fgRightBtn.setVisibility(GONE);
        if (isHideMoreBtn == 1) {
            header_right_btn.setVisibility(GONE);
        } else {
            header_right_btn.setImageResource(R.mipmap.top_more);
            header_right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupWindow();
                }
            });
            if (!TextUtils.isEmpty(targetType) && "3".equals(targetType)) {
                header_right_btn.setVisibility(GONE);
            }
        }


        Bundle bundle = getIntent().getExtras();
        String orderJson = bundle.getString(ORDER_INFO_KEY);
        getUserInfoToOrder(orderJson);

         addConversationFragment();
        //刷新订单信息

         IMUtil.getInstance().setOnImSuccessListener(this);
    }

    private void addConversationFragment(){
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
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
                    Intent intent = new Intent(NIMChatActivity.this, GuideDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, userId);
                    startActivity(intent);
                }

            }

            @Override
            public void onAvatarLongClicked(Context context, IMMessage message) {

            }
        });

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
        notifyChatList();
        clearImChat(); //清空未读消息记录
        super.onPause();
    }

    @Override
    public void onSuccess() {
        emptyView.setVisibility(View.GONE);
    }

    /**
     * 解析用户ID信息
     */
    private void getUserInfoToOrder(String jsonStr) {
        if(TextUtils.isEmpty(jsonStr)){
            return;
        }
        try {
            ChatInfo imInfo = new ParserChatInfo().parseObject(new JSONObject(jsonStr));
            if (imInfo == null) return;
            isChat = imInfo.isChat;
            userId = imInfo.userId;
            imUserId = imInfo.imUserId;
            userAvatar = imInfo.userAvatar;
            fgTitle.setText(imInfo.title); //设置标题
            targetType = imInfo.targetType;
            inBlack = imInfo.inBlack;
            isHideMoreBtn = imInfo.isHideMoreBtn;
            resetRightBtn();
            initRunningOrder(); //构建和该用户之间的订单
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void resetRightBtn() {
        if (isHideMoreBtn == 1) {
            header_right_btn.setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(targetType) && "3".equals(targetType)) {//3.客服 1.用户
                header_right_btn.setVisibility(GONE); //显示历史订单按钮
            } else {
                header_right_btn.setVisibility(View.VISIBLE); //显示历史订单按钮
            }
        }
    }

    /**
     * 展示司导和用户之间的订单
     */
    private void initRunningOrder() {
        clearImChat(); //进入后清空消息提示
        //setUserInfo(); //设置聊天对象头像
        //resetChatting(); //设置是否可以聊天
        loadImOrder(); //显示聊天订单信息
        initEmpty(); //构建空提示
    }

    /**
     * 授权获取手机音频权限
     */
    private void grantAudio() {
        MPermissions.requestPermissions(this, PermissionRes.IM_PERMISSION, android.Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    @PermissionGrant(PermissionRes.IM_PERMISSION)
    public void requestAudioSuccess() {
    }

    @PermissionDenied(PermissionRes.IM_PERMISSION)
    public void requestAudioFailed() {
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
                    grantAudio();
                }
            });
        }
        dialog.show();
    }

    /**
     * 设置是否可以聊天
     */
    private void resetChatting() {
//        if (!isChat) {
//            View view1 = view.getChildAt(0);
//            view1.setVisibility(GONE);
//            fgTitle.setText(getString(R.string.chat_log));
//        }
    }

    /**
     * 设置对方头像
     */
    private void setUserInfo() {
        if (userAvatar != null && !userAvatar.equals("")) {
            UserInfo peerUser = new UserInfo(imUserId, getString(R.string.title_activity_imchat), Uri.parse(userAvatar));
//            RongContext.getInstance().getUserInfoCache().put(imUserId, peerUser);
            RongIM.getInstance().refreshUserInfoCache(peerUser);
        }
    }

    /**
     * 根据数据刷新界面
     *
     * @param orders
     */
    private void flushOrderView(ArrayList<OrderBean> orders) {
        if (orders != null && orders.size() > 0) {
            //有订单数据
            viewPageLayout.setVisibility(View.VISIBLE);
            viewPage.setAdapter(new IMOrderPagerAdapter(orders));
            viewPage.addOnPageChangeListener(onPageChangeListener);
        } else {
            //无订单数据
            viewPageLayout.setVisibility(GONE);
        }
    }

    /**
     * 获取构建viewPage数据
     *
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
            textView.setText(getOrderStatus(orderBean.orderStatus));
//            resetStatusColor(textView, letterOrder.status);
            //订单类型和时间
            TextView textViewtype = (TextView) view.findViewById(R.id.im_chat_orders_item_ordertime);
            textViewtype.setText(getTypeStr(orderBean));
            //订单地址1
            TextView textViewAddr1 = (TextView) view.findViewById(R.id.im_chat_orders_item_address1);
            textViewAddr1.setText(getAddr1(orderBean));
            //订单地址1
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
        StringBuilder sb = new StringBuilder();
        MLog.e("orderGoodsType =" + orderBean.orderGoodsType);
        MLog.e("getOrderTypeStr = " + orderBean.getOrderTypeStr(NIMChatActivity.this));
        if (orderBean.orderGoodsType == 5) {
            sb.append("[" + orderBean.getOrderTypeStr(NIMChatActivity.this) + "]");
            sb.append(orderBean.lineSubject);
        } else {
            sb.append("[" + orderBean.getOrderTypeStr(NIMChatActivity.this) + "]");
        }
        return sb.toString();
    }

    private String getAddr1(OrderBean orderBean) {
        StringBuilder sb = new StringBuilder();
        if (orderBean.orderGoodsType == 1 || orderBean.orderGoodsType == 2 || orderBean.orderGoodsType == 4) {
            sb.append("出发：");
            sb.append(orderBean.startAddress);
        } else if (orderBean.orderGoodsType == 5) {
            //线路
            sb.append(orderBean.serviceTime + "至" + orderBean.serviceEndTime);
        } else {
            sb.append(orderBean.serviceCityName + "-" + orderBean.serviceEndCityName);
        }
        return sb.toString();
    }

    private String getAddr2(OrderBean orderBean) {
        StringBuilder sb = new StringBuilder();
        if (orderBean.orderGoodsType == 1 || orderBean.orderGoodsType == 2 || orderBean.orderGoodsType == 4) {
            sb.append("到达：");
            sb.append(orderBean.destAddress);
        } else if (orderBean.orderGoodsType == 5) {
            //线路
            sb.append("出发：");
            sb.append(orderBean.serviceCityName);
        } else {
            sb.append(orderBean.serviceTime + "至" + orderBean.serviceEndTime);
        }
        return sb.toString();
    }

    /**
     * 根据私信订单状态改变颜色
     *
     * @param tv
     * @param status
     */
    private void resetStatusColor(TextView tv, String status) {
        if (!TextUtils.isEmpty(status)) {
            if (getString(R.string.letter_order_state1).equals(status)) {
                tv.setTextColor(ContextCompat.getColor(NIMChatActivity.this, R.color.letter_item_order1));
            } else if (getString(R.string.letter_order_state2).equals(status)) {
                tv.setTextColor(ContextCompat.getColor(NIMChatActivity.this, R.color.letter_item_order2));
            } else if (getString(R.string.letter_order_state3).equals(status)) {
                tv.setTextColor(ContextCompat.getColor(NIMChatActivity.this, R.color.letter_item_order3));
            }
        }
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
            popup.showAsDropDown(header_right_btn,0, UIUtils.dip2px(5f));
            return;
        }
        popup = new PopupWindow(menuLayout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAsDropDown(header_right_btn,0,UIUtils.dip2px(5f));

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

    private void notifyChatList() {
        EventBus.getDefault().post(new EventAction(EventType.REFRESH_CHAT_LIST));
    }

    /**
     * 构建聊天为空界面
     */
    private void initEmpty() {
        //消息为空提示
//        if (view == null) {
//            return;
//        }
    }

//    private void requestApiFeedback(int errorMessage, String errorCode) {
//        RequestApiFeedback requestApiFeedback = new RequestApiFeedback(NIMChatActivity.this,
//                UserEntity.getUser().getUserId(NIMChatActivity.this),
//                ApiFeedbackUtils.getImErrorFeedback(errorMessage, errorCode));
//        HttpRequestUtils.request(NIMChatActivity.this, requestApiFeedback, new HttpRequestListener() {
//            @Override
//            public void onDataRequestSucceed(BaseRequest request) {
//
//            }
//
//            @Override
//            public void onDataRequestCancel(BaseRequest request) {
//
//            }
//
//            @Override
//            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
//
//            }
//        }, false);
//    }

    /**
     * 清空融云消息数
     */
    private void clearImChat() {
        // 调用接口清空聊天未读信息
        RequestNIMClear requestIMClear = new RequestNIMClear(NIMChatActivity.this, userId, targetType);
        HttpRequestUtils.request(NIMChatActivity.this, requestIMClear, imClearListener);
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

    HttpRequestListener imClearListener = new HttpRequestListener() {
        @Override
        public void onDataRequestSucceed(BaseRequest request) {
            MLog.e("清除IM消息成功");
        }

        @Override
        public void onDataRequestCancel(BaseRequest request) {

        }

        @Override
        public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            ErrorHandler handler = new ErrorHandler(NIMChatActivity.this, this);
            handler.onDataRequestError(errorInfo, request);
        }
    };

    public static String getOrderStatus(OrderStatus orderStatus){
        switch (orderStatus) {
            case INITSTATE:     // 未支付
                return  "未支付";
            case PAYSUCCESS:
            case AGREE:    // 已支付--服务中
                return "未开始";
            case ARRIVED:
            case SERVICING:
            case COMPLAINT:    // 已支付--服务中
                return "进行中";
            case NOT_EVALUATED:     // 未评价
                return "进行中";
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



}
