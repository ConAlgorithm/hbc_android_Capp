package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.BargainActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestBargainShare;
import com.hugboga.custom.data.request.RequestChangeUserInfo;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayResultBargainLayout extends RelativeLayout {

    @Bind(R.id.pay_result_bargain_parent_layout)
    RelativeLayout parentLayout;
    @Bind(R.id.pay_result_bargain_title_layout)
    RelativeLayout titleLayout;

    private String orderNo;
    private int orderType;

    public PayResultBargainLayout(Context context) {
        this(context, null);
    }

    public PayResultBargainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_pay_result_bargain_layout, this);
        ButterKnife.bind(view);

        int itemHeight = (int)((260 / 720.0) * UIUtils.getScreenWidth());
        RelativeLayout.LayoutParams parentParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, itemHeight);
        parentLayout.setLayoutParams(parentParams);

        int titleViewWidth = (int)(UIUtils.getScreenWidth() / 5.0 * 4);
        int titleViewHeight = (int)((76 / 576.0) * titleViewWidth);
        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(titleViewWidth, titleViewHeight);
        titleLayout.setLayoutParams(titleViewParams);
    }

    public void setParams(String orderNo, int orderType) {
        this.orderNo = orderNo;
        this.orderType = orderType;
    }

    @OnClick(R.id.pay_result_bargain_detail_view)
    public void intentBargainDetail() {
        if (orderNo == null) {
            return;
        }
        Intent intentBargain = new Intent(getContext(), BargainActivity.class);
        intentBargain.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_title));
        intentBargain.putExtra("orderNo", orderNo);
        getContext().startActivity(intentBargain);
    }

    @OnClick(R.id.pay_result_bargain_share_wechat)
    public void shareWechat() {
        onClickShare(1);
    }

    @OnClick(R.id.pay_result_bargain_share_moments)
    public void shareMoments() {
        onClickShare(2);
    }

    //------以下代码来自BargainActivity------

    //是否显示过填写姓名popup
    boolean isShowAddNamePopup = false;
    public void onClickShare(final int shareType) {
        if(TextUtils.isEmpty(UserEntity.getUser().getUserName(getContext()))) {
            if(!isShowAddNamePopup){
                isShowAddNamePopup = true;
                showAddName();
            } else {
                showShareDialog(shareType);
            }
        } else {
            showShareDialog(shareType);
        }
    }

    public String getShareTitle() {
        String serviceOrderType = "";
        switch (orderType) {
            case 1:
                serviceOrderType = "中文接机服务";
                break;
            case 2:
                serviceOrderType = "中文送机服务";
                break;
            case 3:
            case 888:
                serviceOrderType = "按天包车游服务";
                break;
            case 4:
                serviceOrderType = "单次接送服务";
                break;
            case 5:
            case 6:
                serviceOrderType = "线路包车游服务";
                break;
        }
        return String.format(getContext().getResources().getString(R.string.share_bargin_title), serviceOrderType);
    }

    private View popupView;
    PopupWindow popupWindow;
    EditText nameEdit;
    private void showAddName(){
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.bargain_add_name_layout,null);
        popupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        nameEdit = (EditText)popupView.findViewById(R.id.real_name);
        popupView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.hideSoftInputMethod(nameEdit);
                popupWindow.dismiss();
            }
        });
        popupView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nameEdit.getText()) || TextUtils.isEmpty(nameEdit.getText().toString().trim())){
                    CommonUtils.showToast(R.string.real_name);
                    return;
                }
                String name = nameEdit.getText().toString();
                for(int i = 0;i< name.length();i++) {
                    if (!Tools.isEmojiCharacter(name.charAt(i))) {
                        CommonUtils.showToast("真实姓名不能包含表情符号");
                        return;
                    }
                }
                name = name.replaceAll(" ", "");
                final String requestName = name;
                //真实姓名
                RequestChangeUserInfo request = new RequestChangeUserInfo(getContext(), null, null, null, null, null, name);
                HttpRequestUtils.request(getContext(), request, new HttpRequestListener() {
                    @Override
                    public void onDataRequestSucceed(BaseRequest request) {
                        ApiReportHelper.getInstance().addReport(request);
                        UserEntity.getUser().setUserName(getContext(), requestName);
                        CommonUtils.hideSoftInputMethod(nameEdit);
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onDataRequestCancel(BaseRequest request) {

                    }

                    @Override
                    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                        if (popupWindow != null) {
                            CommonUtils.hideSoftInputMethod(nameEdit);
                            popupWindow.dismiss();
                        }
                    }
                });
            }
        });
        try {
            popupWindow.showAtLocation(((Activity)getContext()).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            popupWindow.showAsDropDown(this);
        }
    }

    private void showShareDialog(final int shareType) {
        final String source = getContext().getString(R.string.par_result_title);
        SensorsUtils.setSensorsShareEvent(shareType == 1 ? "微信好友" : "朋友圈", source, null, null);
        RequestBargainShare requestBargainShare = new RequestBargainShare(getContext(), orderNo);
        HttpRequestUtils.request(getContext(), requestBargainShare, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                if (getContext() instanceof Activity) {
                    Activity activity = (Activity) getContext();
                    if (activity.isFinishing()) {
                        return;
                    }
                }
                ApiReportHelper.getInstance().addReport(request);
                String h5Url = ((RequestBargainShare)request).getData();
                ShareDialog.Params mParams = new ShareDialog.Params(R.mipmap.bargain_share, getShareTitle(), getContext().getResources().getString(R.string.share_bargin_100), h5Url, source);
                WXShareUtils wxShareUtils = WXShareUtils.getInstance(getContext());
                wxShareUtils.source = source;
                wxShareUtils.share(shareType, mParams.resID, mParams.title, mParams.content, mParams.shareUrl);
                EventBus.getDefault().post(new EventAction(EventType.WECHAT_SHARE, shareType));
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

            }
        }, true);
    }
}
