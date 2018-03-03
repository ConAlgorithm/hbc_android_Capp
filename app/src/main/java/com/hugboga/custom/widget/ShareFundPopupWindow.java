package com.hugboga.custom.widget;

import android.Manifest;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.TwoDimensionActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ShareFundBean;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.ShareUrls;
import com.hugboga.custom.data.request.RequestGetInvitationCode;
import com.hugboga.custom.data.request.RequestTravelFundLogs;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.AnimationUtils;
import com.hugboga.custom.utils.PermissionRes;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by zhangqi on 2018/2/28.
 */

public class ShareFundPopupWindow implements View.OnClickListener {

    private View menuLayout;
    private Context context;
    private CompatPopupWindow popup;
    private LinearLayout linearLayout;
    private ShareFundBean shareFundBean;

    public ShareFundPopupWindow(Context context, ShareFundBean shareFundBean) {
        this.shareFundBean = shareFundBean;
        menuLayout = LayoutInflater.from(context).inflate(R.layout.popup_share_travel_fund, null);
        popup = new CompatPopupWindow(menuLayout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        this.context = context;
        linearLayout = menuLayout.findViewById(R.id.linear_layout);
        menuLayout.findViewById(R.id.wx_share).setOnClickListener(this);
        menuLayout.findViewById(R.id.wx_share_two).setOnClickListener(this);
        menuLayout.findViewById(R.id.link).setOnClickListener(this);
        menuLayout.findViewById(R.id.note).setOnClickListener(this);
        menuLayout.findViewById(R.id.pr_code).setOnClickListener(this);
        menuLayout.findViewById(R.id.bg_view).setOnClickListener(this);
    }

    public void showAsDropDown(View header_left_btn) {
        popup.showAsDropDown(header_left_btn);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        AnimationUtils.showAnimation(linearLayout, 1000, null);
    }


    private void grantSendMessages() {

        if (checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            MPermissions.requestPermissions((Activity) context, PermissionRes.SEND_SMS, android.Manifest.permission.SEND_SMS);
        } else {
            sendMms();
        }

    }

    @PermissionGrant(PermissionRes.SEND_SMS)
    public void requestPhoneSuccess() {
        sendMms();
    }

    @PermissionDenied(PermissionRes.SEND_SMS)
    public void requestPhoneFailed() {

        AlertDialogUtils.showAlertDialog((Activity) context, false, context.getString(R.string.grant_fail_title), context.getString(R.string.grant_fail_phone)
                , context.getString(R.string.grant_fail_btn), context.getString(R.string.grant_fail_btn_exit)
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        grantSendMessages();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });

    }

    private void sendMms() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("sms_body", shareFundBean.message);
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);
        closePopupWindow();
    }


    private void WXShare(int type) {

        WXShareUtils wxShareUtils = WXShareUtils.getInstance(context);
        wxShareUtils.source = getClass().getSimpleName();
        wxShareUtils.share(type, shareFundBean.shareData.picUrl, shareFundBean.shareData.title, shareFundBean.shareData.content, shareFundBean.shareData.goUrl);

        closePopupWindow();

    }

    private void closePopupWindow() {
        if (popup != null) {
            popup.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wx_share:
                WXShare(1);
                break;
            case R.id.wx_share_two:
                WXShare(2);
                break;
            case R.id.link:
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(shareFundBean.shareData.goUrl);
                closePopupWindow();
                break;
            case R.id.pr_code:
                Intent intent = new Intent(context, TwoDimensionActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, ((BaseActivity) context).getEventSource());
                intent.putExtra(TwoDimensionActivity.TWODIMENSION, "http://www.baidu.com");
                context.startActivity(intent);
                closePopupWindow();
                break;
            case R.id.note:
                grantSendMessages();
                break;
            case R.id.bg_view:
                closePopupWindow();
                break;
        }
    }
}
