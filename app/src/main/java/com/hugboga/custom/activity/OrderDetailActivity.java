package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderGuideInfo;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.data.request.RequestCollectGuidesId;
import com.hugboga.custom.data.request.RequestOrderCancel;
import com.hugboga.custom.data.request.RequestOrderDetail;
import com.hugboga.custom.data.request.RequestUncollectGuidesId;
import com.hugboga.custom.fragment.FgChoosePayment;
import com.hugboga.custom.fragment.FgEvaluate;
import com.hugboga.custom.fragment.FgGuideDetail;
import com.hugboga.custom.fragment.FgInsure;
import com.hugboga.custom.fragment.FgInsureInfo;
import com.hugboga.custom.fragment.FgOrderCancel;
import com.hugboga.custom.fragment.FgOrderEdit;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.OrderDetailFloatView;
import com.hugboga.custom.widget.OrderDetailGuideInfo;
import com.hugboga.custom.widget.OrderDetailItineraryView;
import com.hugboga.custom.widget.OrderDetailTitleBar;
import com.hugboga.custom.widget.TopTipsLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;

import static com.hugboga.custom.data.net.UrlLibs.H5_INSURANCE;
import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by qingcha on 16/8/2.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.top_tips_layout)
    TopTipsLayout topTipsLayout;

    @Bind(R.id.order_detail_title_layout)
    OrderDetailTitleBar titleBar;

    @Bind(R.id.order_detail_guideinfo_view)
    OrderDetailGuideInfo guideInfoView;

    @Bind(R.id.order_detail_itinerary_view)
    OrderDetailItineraryView itineraryView;

    @Bind(R.id.order_detail_float_view)
    OrderDetailFloatView floatView;

    @Bind(R.id.order_detail_group_layout)
    LinearLayout groupLayout;

    @Bind(R.id.order_detail_empty_tv)
    TextView emptyTV;

    @Bind(R.id.order_detail_explain_tv)
    TextView explainTV;

    private PopupWindow popup;
    private View menuLayout;
    private Params params;
    private OrderBean orderBean;
    private DialogUtil mDialogUtil;

    public static class Params implements Serializable {
        public String orderId;
        public String source;
        public int orderType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.fg_order_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mDialogUtil = DialogUtil.getInstance(this);
        titleBar.setTitle(params.orderType);
        emptyTV.setVisibility(View.VISIBLE);
        titleBar.findViewById(R.id.header_detail_right_1_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        titleBar.findViewById(R.id.header_detail_right_2_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtil.showCallDialog();
            }
        });
//        TODO itineraryView.setFragment(this);
        requestData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    private void genTopTIps() {
        if (null != orderBean) {
            if (orderBean.orderStatus.code == 2) {//未付款
                topTipsLayout.setText(R.string.order_detail_top1_tips);
            } else if (orderBean.orderStatus.code == 1) {//已付款
                topTipsLayout.setText(R.string.order_detail_top2_tips);
            } else {
                topTipsLayout.hide();
            }
        } else {
            topTipsLayout.hide();
        }
    }

    private void requestData() {
        RequestOrderDetail request = new RequestOrderDetail(this, params.orderId);
        requestData(request);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.order_detail_empty_tv:
                emptyTV.setOnClickListener(null);
                emptyTV.setText("");
                requestData();
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestOrderDetail) {
            emptyTV.setVisibility(View.GONE);
            RequestOrderDetail mParser = (RequestOrderDetail) _request;
            orderBean = mParser.getData();
            genTopTIps();
            titleBar.update(orderBean);
            floatView.update(orderBean);
            final int count = groupLayout.getChildCount();
            for (int i = 0; i < count; i++) {
                View item = groupLayout.getChildAt(i);
                if (item instanceof HbcViewBehavior) {
                    ((HbcViewBehavior) item).update(orderBean);
                }
            }
            if (orderBean.cancelRules != null && orderBean.cancelRules.size() > 0) {
                String explainStr = "";
                for (int i = 0; i < orderBean.cancelRules.size(); i++) {
                    explainStr += orderBean.cancelRules.get(i);
                }
                explainTV.setText(explainStr);
            }
            if (floatView.findViewById(R.id.order_detail_pay_tv) != null) {
                floatView.findViewById(R.id.order_detail_pay_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        FgChoosePayment.RequestParams requestParams = new FgChoosePayment.RequestParams();
//                        requestParams.orderId = orderBean.orderNo;
//                        requestParams.shouldPay = orderBean.orderPriceInfo.actualPay;
//                        requestParams.source = source;
//                        startFragment(FgChoosePayment.newInstance(requestParams));
                        ChoosePaymentActivity.RequestParams requestParams = new ChoosePaymentActivity.RequestParams();
                        requestParams.orderId = orderBean.orderNo;
                        requestParams.shouldPay = orderBean.orderPriceInfo.actualPay;
                        requestParams.source = source;
                        Intent intent = new Intent(OrderDetailActivity.this, ChoosePaymentActivity.class);
                        intent.putExtra(Constants.PARAMS_DATA, requestParams);
                        OrderDetailActivity.this.startActivity(intent);
                    }
                });
            }
        } else if (_request instanceof RequestOrderCancel) {//取消订单
//            DialogUtil dialogUtil = DialogUtil.getInstance(getActivity());
//            dialogUtil.showCustomDialog(getContext().getString(R.string.order_cancel_succeed), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    bringToFront(FgTravel.class,new Bundle());
//                    finish();
//                }
//            });
//            notifyOrderList(FgTravel.TYPE_ORDER_CANCEL, true, false, true);
//            CommonUtils.showToast();
            CommonUtils.showToast(R.string.order_detail_cancel_oeder);
            requestData();
            EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOGIN));
        } else if (_request instanceof RequestUncollectGuidesId) {//取消收藏
            orderBean.orderGuideInfo.storeStatus = 0;
            updateCollectViewText();
        } else if (_request instanceof RequestCollectGuidesId) {//收藏
            orderBean.orderGuideInfo.storeStatus = 1;
            updateCollectViewText();
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        super.onDataRequestError(errorInfo, _request);
        if (_request instanceof RequestOrderDetail) {
            emptyTV.setVisibility(View.VISIBLE);
            emptyTV.setText(R.string.data_load_error_retry);
            emptyTV.setOnClickListener(this);
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
//            case ORDER_DETAIL_PAY://立即支付
//                FgChoosePayment.RequestParams requestParams = new FgChoosePayment.RequestParams();
//                requestParams.orderId = orderBean.orderNo;
//                requestParams.shouldPay = orderBean.orderPriceInfo.actualPay;
//                requestParams.source = source;
//                Log.i("aa", "FgOrderDetail 2222 "+  orderBean.orderNo);
//                startFragment(FgChoosePayment.newInstance(requestParams));
//                break;
            case ORDER_DETAIL_INSURANCE_H5://皇包车免费赠送保险

//                Bundle bundle = new Bundle();
//                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_INSURANCE);
//                startFragment(new FgWebInfo(), bundle);

                Intent intent = new Intent(context, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, H5_INSURANCE);
                context.startActivity(intent);

                break;
            case ORDER_DETAIL_BACK://返回
                finish();
                break;
//            case ORDER_DETAIL_CALL://联系客服
//                mDialogUtil.showCallDialog();
//                break;
//            case ORDER_DETAIL_MORE://更多
//                showPopupWindow();
//                break;
            case ORDER_DETAIL_ADD_INSURER://添加投保人 copy FgOrder
                FgInsure fgAddInsure = new FgInsure();
                Bundle insureBundle = new Bundle();
                insureBundle.putSerializable("orderBean", orderBean);
                fgAddInsure.setArguments(insureBundle);
                startFragment(fgAddInsure);
                break;
            case ORDER_DETAIL_LIST_INSURER://投保人列表
                startFragment(FgInsureInfo.newInstance(orderBean));
                break;
            case ORDER_DETAIL_GUIDE_CALL://联系司导
                if (orderBean.orderGuideInfo == null) {
                    return;
                }
                PhoneInfo.CallDial(OrderDetailActivity.this, orderBean.orderGuideInfo.guideTel);
                break;
            case ORDER_DETAIL_GUIDE_CHAT://和司导聊天
                final OrderGuideInfo guideInfo = orderBean.orderGuideInfo;
                if (guideInfo == null) {
                    return;
                }
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.isChat = true;
                chatInfo.userId = guideInfo.guideID;
                chatInfo.userAvatar = guideInfo.guideAvatar;
                chatInfo.title = guideInfo.guideName;
                chatInfo.targetType = "1";
                RongIM.getInstance().startPrivateChat(OrderDetailActivity.this, "G" + guideInfo.guideID, new ParserChatInfo().toJsonString(chatInfo));
                break;
            case ORDER_DETAIL_GUIDE_INFO://司导个人信息
                if (orderBean == null || orderBean.orderGuideInfo == null) {
                    return;
                }
//                startFragment(FgGuideDetail.newInstance(orderBean.orderGuideInfo.guideID));
                Intent intent1 = new Intent(this, GuideDetailActivity.class);
                intent1.putExtra(Constants.PARAMS_DATA, orderBean.orderGuideInfo.guideID);
                startActivity(intent1);
                break;
            case ORDER_DETAIL_UPDATE_COLLECT://更新收藏UI
                orderBean.orderGuideInfo.storeStatus = (int) action.getData();
                updateCollectViewText();
                break;
            case ORDER_DETAIL_GUIDE_COLLECT://收藏 只可收藏不可取消
                if (orderBean.orderGuideInfo == null || orderBean.orderGuideInfo.isCollected()) {
                    return;
                }
                mDialogUtil.showLoadingDialog();
                requestData(new RequestCollectGuidesId(OrderDetailActivity.this, orderBean.orderGuideInfo.guideID));
                break;
            case ORDER_DETAIL_UPDATE_EVALUATION://更新评价UI
                requestData();
                break;
            case ORDER_DETAIL_GUIDE_EVALUATION://评价司导
                startFragment(FgEvaluate.newInstance(orderBean));
                break;
            case ORDER_DETAIL_UPDATE_INFO://更新个人信息UI
                requestData();
                break;
            case ADD_INSURE_SUCCESS://更新添加投保人
                if (orderBean.orderNo.equals(action.getData())) {
                    requestData();
                }
                break;
            case ORDER_DETAIL_UPDATE://更新数据
                requestData();
                break;
            case ORDER_DETAIL_TOURIST_INFO://出行人信息
                if (orderBean == null) {
                    return;
                }
                startFragment(FgOrderEdit.newInstance(orderBean));
                break;
            default:
                break;
        }
    }

    private void updateCollectViewText() {
        TextView collectTV = (TextView) guideInfoView.findViewById(R.id.ogi_collect_tv);
        collectTV.setText(getString(orderBean.orderGuideInfo.isCollected() ? R.string.uncollect : R.string.collect));
    }

    /**
     * 取消订单
     */
    private void cancelOrder(String orderID, double cancelPrice) {
        if (cancelPrice < 0) cancelPrice = 0;
        mDialogUtil.showLoadingDialog();
        RequestOrderCancel request = new RequestOrderCancel(this, orderID, cancelPrice, "");
        requestData(request);
    }

    /**
     * 右上角的菜单，取消订单 联系客服
     */
    public void showPopupWindow() {
        if (popup != null && popup.isShowing()) {
            return;
        }
        if (menuLayout == null) {
            menuLayout  = LayoutInflater.from(this).inflate(R.layout.popup_top_right_menu, null);
        }
        TextView cancelOrderTV = (TextView)menuLayout.findViewById(R.id.cancel_order);
        TextView commonProblemTV = (TextView)menuLayout.findViewById(R.id.menu_phone);
        commonProblemTV.setText("常见问题");
        if (orderBean.orderStatus.code <= 5) {
            cancelOrderTV.setVisibility(View.VISIBLE);
        } else {
            cancelOrderTV.setVisibility(View.GONE);
        }

        if (popup != null) {
            popup.showAsDropDown(titleBar.findViewById(R.id.header_detail_right_1_btn));
            return;
        }
        popup = new PopupWindow(menuLayout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAsDropDown(titleBar.findViewById(R.id.header_detail_right_1_btn));

        cancelOrderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogUtil == null) {
                    mDialogUtil = DialogUtil.getInstance(OrderDetailActivity.this);
                }
                //如果此订单不能取消，直接进行提示
                popup.dismiss();

                if (orderBean.cancelable) {//cancelable是否能取消
                    String tip = "";
                    if (orderBean.orderStatus == OrderStatus.INITSTATE) {
                        tip = getString(R.string.order_cancel_tip);
                    } else if (orderBean.isChangeManual) {//需要人工取消订单
                        mDialogUtil.showCallDialogTitle("如需要取消订单，请联系客服处理");
                        return;
                    } else {
                        tip = orderBean.cancelTip;
                    }
                    mDialogUtil.showCustomDialog(getString(R.string.app_name), tip, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (orderBean.orderStatus == OrderStatus.INITSTATE) {
                                cancelOrder(orderBean.orderNo, 0);
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(FgOrderCancel.KEY_ORDER, orderBean);
                                bundle.putString("source", source);
                                startFragment(new FgOrderCancel(), bundle);
                            }
                        }
                    }, "返回", null);
                } else {
                    mDialogUtil.showCustomDialog(orderBean.cancelText);
                }
            }
        });
        commonProblemTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_PROBLEM);
//                bundle.putBoolean(FgWebInfo.CONTACT_SERVICE, true);
//                startFragment(new FgWebInfo(), bundle);

                Intent intent = new Intent(context, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_PROBLEM);
                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                context.startActivity(intent);

                popup.dismiss();
            }
        });
    }

}
