package com.hugboga.custom.widget;

import android.content.Context;
import android.view.View;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.request.RequestBargainShare;
import com.hugboga.custom.utils.ApiReportHelper;

public class BargainShareDialog extends ShareDialog {

    private String shareTitle;
    private String orderNo;
    private String source;
    private OnStartBargainListener listener;

    public BargainShareDialog(Context context) {
        super(context);
    }

    public BargainShareDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setData(String shareTitle, String orderNo, String source) {
        this.shareTitle = shareTitle;
        this.orderNo = orderNo;
        this.source = source;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_share_wechat_layout://微信好友
                getShareUrl(1);
                break;
            case R.id.dialog_share_moments_layout://朋友圈
                getShareUrl(2);
                break;
            case R.id.dialog_share_shadow_view:
            case R.id.dialog_share_cancel_tv:
                dismiss();
                break;
        }
    }

    private void getShareUrl(final int shareType) {
        RequestBargainShare requestBargainShare = new RequestBargainShare(getContext(), orderNo);
        HttpRequestUtils.request(getContext(), requestBargainShare, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                String h5Url = ((RequestBargainShare)request).getData();
                ShareDialog.Params params = new ShareDialog.Params(R.mipmap.bargain_share, shareTitle, getContext().getResources().getString(R.string.share_bargin_100), h5Url, source);
                setParams(params);
                setShare(shareType);
                if (listener != null) {
                    listener.onStartBargain();
                }
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

            }
        }, true);
    }

    public interface OnStartBargainListener {
        public void onStartBargain();
    }

    public void setOnStartBargainListener(OnStartBargainListener listener) {
        this.listener = listener;
    }
}
