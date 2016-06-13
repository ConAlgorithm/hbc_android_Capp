package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
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
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.OrderDetailFloatView;
import com.hugboga.custom.widget.OrderDetailGuideInfo;
import com.hugboga.custom.widget.OrderDetailInfoView;
import com.hugboga.custom.widget.OrderDetailTitleBar;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * Created by qingcha on 16/6/1.
 *
 * 取消订单的逻辑和代码来自旧的FgOrder
 *
 */
@ContentView(R.layout.fg_order_detail)
public class FgOrderDetail extends BaseFragment implements View.OnClickListener{

    @ViewInject(R.id.order_detail_title_layout)
    private OrderDetailTitleBar titleBar;

    @ViewInject(R.id.order_detail_guideinfo_view)
    private OrderDetailGuideInfo guideInfoView;

    @ViewInject(R.id.order_detail_float_view)
    private OrderDetailFloatView floatView;

    @ViewInject(R.id.order_detail_group_layout)
    private LinearLayout groupLayout;

    @ViewInject(R.id.order_detail_empty_tv)
    private TextView emptyTV;

    private Params params;
    private OrderBean orderBean;
    private DialogUtil mDialogUtil;

    public static class Params implements Serializable {
        public String orderId;
        public String source;
        public boolean isUpdate;
        public int orderType;
    }

    public static FgOrderDetail newInstance(Params params) {
        FgOrderDetail fragment = new FgOrderDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS_DATA, params);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mDialogUtil = DialogUtil.getInstance(getActivity());
    }

    @Override
    protected void initHeader(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            params = (Params)savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                params = (Params)bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        titleBar.setTitle(params.orderType);
        emptyTV.setVisibility(View.VISIBLE);
        if (params.isUpdate) {
            requestData();
        }
    }

    @Override
    protected void initHeader() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    @Override
    protected Callback.Cancelable requestData() {
        RequestOrderDetail request = new RequestOrderDetail(getActivity(), params.orderId);
        return requestData(request);
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestOrderDetail) {
            emptyTV.setVisibility(View.GONE);
            RequestOrderDetail mParser = (RequestOrderDetail) _request;
            orderBean = mParser.getData();

            titleBar.update(orderBean);
            floatView.update(orderBean);
            final int count = groupLayout.getChildCount();
            for (int i = 0; i < count; i++) {
                View item = groupLayout.getChildAt(i);
                if (item instanceof HbcViewBehavior) {
                    ((HbcViewBehavior) item).update(orderBean);
                }
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
            emptyTV.setVisibility(View.GONE);
            emptyTV.setText(R.string.data_load_error_retry);
            emptyTV.setOnClickListener(this);
        }
    }

    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case ORDER_DETAIL_PAY://立即支付
                FgChoosePayment.RequestParams requestParams = new FgChoosePayment.RequestParams();
                requestParams.orderId = orderBean.orderNo;
                requestParams.shouldPay = orderBean.orderPriceInfo.actualPay;
                requestParams.source = source;
                startFragment(FgChoosePayment.newInstance(requestParams));
                break;
            case ORDER_DETAIL_INSURANCE_H5://皇包车免费赠送保险
                Bundle bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_INSURANCE);
                startFragment(new FgWebInfo(), bundle);
                break;
            case ORDER_DETAIL_BACK://返回
                finish();
                break;
            case ORDER_DETAIL_CALL://联系客服
                mDialogUtil.showCallDialog();
                break;
            case ORDER_DETAIL_MORE://更多
                showPopupWindow();
                break;
            case ORDER_DETAIL_ADD_INSURER://添加投保人 copy FgOrder
                FgInsure fgAddInsure = new FgInsure();
                Bundle insureBundle = new Bundle();
                insureBundle.putParcelable("orderBean", orderBean);
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
                PhoneInfo.CallDial(getActivity(), orderBean.orderGuideInfo.guideTel);
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
                RongIM.getInstance().startPrivateChat(getActivity(), "G" + guideInfo.guideID, new ParserChatInfo().toJsonString(chatInfo));
                break;
            case ORDER_DETAIL_GUIDE_INFO://司导个人信息
                if (orderBean == null || orderBean.orderGuideInfo == null) {
                    return;
                }
                startFragment(FgGuideDetail.newInstance(orderBean.orderGuideInfo.guideID));
                break;
            case ORDER_DETAIL_UPDATE_COLLECT://更新收藏UI
                orderBean.orderGuideInfo.storeStatus = (int) action.getData();
                updateCollectViewText();
                break;
            case ORDER_DETAIL_GUIDE_COLLECT://收藏
                if (orderBean.orderGuideInfo == null) {
                    return;
                }
                mDialogUtil.showLoadingDialog();
                BaseRequest baseRequest = null;
                if (orderBean.orderGuideInfo.isCollected()) {
                    baseRequest = new RequestUncollectGuidesId(getActivity(), orderBean.orderGuideInfo.guideID);
                } else {
                    baseRequest = new RequestCollectGuidesId(getActivity(), orderBean.orderGuideInfo.guideID);
                }
                requestData(baseRequest);
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

    private void updateCollectViewText() {
        TextView collectTV = (TextView)guideInfoView.findViewById(R.id.ogi_collect_tv);
        collectTV.setText(getContext().getString(orderBean.orderGuideInfo.isCollected() ? R.string.uncollect : R.string.collect));
    }

    /**
     * 取消订单
     */
    private void cancelOrder(String orderID, double cancelPrice) {
        if (cancelPrice < 0) cancelPrice = 0;
        mDialogUtil.showLoadingDialog();
        RequestOrderCancel request = new RequestOrderCancel(getActivity(), orderID, cancelPrice, "");
        requestData(request);
    }

    private void notifyOrderList(int jumpType, boolean refreshRunning, boolean refreshFinish, boolean refreshCancel) {
        Intent intent = new Intent();
        intent.setAction(FgTravel.FILTER_FLUSH);
        intent.putExtra(FgTravel.JUMP_TYPE, jumpType);
        intent.putExtra(FgTravel.REFRESH_RUNNING, refreshRunning);
        intent.putExtra(FgTravel.REFRESH_FINISH, refreshFinish);
        intent.putExtra(FgTravel.REFRESH_CANCEL, refreshCancel);
        getActivity().sendBroadcast(intent);
    }

    /**
     * 右上角的菜单，取消订单 联系客服
     */
    public void showPopupWindow() {
        View menuLayout = LayoutInflater.from(getActivity()).inflate(R.layout.popup_top_right_menu, null);
        TextView cancelOrderTV = (TextView)menuLayout.findViewById(R.id.cancel_order);
        TextView commonProblemTV = (TextView)menuLayout.findViewById(R.id.menu_phone);
        commonProblemTV.setText("常见问题");
        if (orderBean.orderStatus.code <= 5) {
            cancelOrderTV.setVisibility(View.VISIBLE);
        } else {
            cancelOrderTV.setVisibility(View.GONE);
        }

        final PopupWindow popup = new PopupWindow(menuLayout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAsDropDown(titleBar.findViewById(R.id.header_detail_right_1_btn));

        cancelOrderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果此订单不能取消，直接进行提示
                popup.dismiss();
                if (!orderBean.cancelable && !TextUtils.isEmpty(orderBean.cancelText)) {
                    mDialogUtil.showCustomDialog(orderBean.cancelText);
                    return;
                }

                String tip = "";
                if (orderBean.orderStatus == OrderStatus.INITSTATE) {
                    tip = getString(R.string.order_cancel_tip);
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
            }
        });
        commonProblemTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_PROBLEM);
                bundle.putBoolean(FgWebInfo.CONTACT_SERVICE, true);
                startFragment(new FgWebInfo(), bundle);
                popup.dismiss();
            }
        });
    }
}
