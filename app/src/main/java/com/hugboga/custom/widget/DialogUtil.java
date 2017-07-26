package com.hugboga.custom.widget;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.DialogUtilInterface;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.Common;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.UnicornUtils;

/**
 * <p> DialogUtil mDialogUtil = new DialogUtil(activity);
 * <p> mDialogUtil.showLoadingDialog();
 * <p>  mDialogUtil.dismissLoadingDialog();
 *
 * @date 2012-6-6 上午10:41:22
 */
public class DialogUtil implements DialogUtilInterface {
    public Activity mContext;
    private Dialog mLoadingDialog;
    private AlertDialog settingDialog;
    private AlertDialog overtimeDialog;
    private AlertDialog noRoomDialog;
    private Dialog customDialog;
    private float density;
    private AlertDialog mUpdateVersionDialog;

    private static DialogUtil dialogUtil;

    private AlertDialog versionDialog;

    private DialogUtil(Activity context) {
        mContext = context;
        density = PhoneInfo.getDensity(mContext);
    }

    public static DialogUtil getInstance(Activity context) {
        if (dialogUtil == null) {
            dialogUtil = new DialogUtil(context);
        }
        dialogUtil.mContext = context;
        return dialogUtil;
    }

    /**
     * @return Dialog    返回类型
     * @Title showLoadingDialog
     * @Description 显示 默认 数据加载 loading 对话框
     * @author aceway-liwei
     * @date 2012-6-13 上午09:34:50
     */
    public Dialog showLoadingDialog() {
//		return showLoadingDialog("数据加载中，等待过后更精彩!");
        return showLoadingDialog(mContext.getString(R.string.data_load));
    }

    /**
     * @param message 显示的 内容
     * @return Dialog    返回类型
     * @Title showLoadingDialog
     * @Description 显示 数据加载 loading 对话框
     * @author aceway-liwei
     * @date 2012-6-13 上午09:35:20
     */
    public Dialog showLoadingDialog(String message) {
        return showLoadingDialog(message, false);
    }

    /**
     * @param cancelAble 是否能取消 Dialog
     * @return Dialog    返回类型
     * @Title showLoadingDialog
     * @Description 显示 数据加载 loading 对话框
     * @author aceway-liwei
     * @date 2012-6-13 上午09:35:20
     */
    public Dialog showLoadingDialog(boolean cancelAble) {
        return showLoadingDialog(mContext.getString(R.string.data_load), cancelAble);
    }

    /**
     * @param message    显示的 内容
     * @param cancelAble 是否能取消
     * @return Dialog    返回类型
     * @Title showLoadingDialog
     * @Description 显示 数据加载 loading 对话框
     * @author aceway-liwei
     * @date 2012-6-13 上午09:35:20
     */
    public Dialog showLoadingDialog(String message, boolean cancelAble) {
        try {
            if (mContext.isFinishing()) {
                return mLoadingDialog;
            }
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.setCancelable(cancelAble);
//			mLoadingDialog.cancel();
            } else {
                View loadingView = mContext.getLayoutInflater().inflate(R.layout.dailog_loading, null);

                mLoadingDialog = new Dialog(mContext, R.style.loading_dialog_style);
                mLoadingDialog.setCanceledOnTouchOutside(false);
                mLoadingDialog.setCancelable(cancelAble);
                mLoadingDialog.getWindow().setContentView(loadingView);
                mLoadingDialog.show();

            }
        }catch (Exception e){}
        return mLoadingDialog;
    }

    /**
     * @Title dismissLoadingDialog
     * @Description 隐藏 loadingDialog
     * @author aceway-liwei
     * @date 2012-6-13 上午09:37:05
     */
    public void dismissLoadingDialog() {
        if (!mContext.isFinishing() && mLoadingDialog != null&&mLoadingDialog.isShowing())
            try {
                mLoadingDialog.dismiss();
            } catch (Exception e){}
    }

    public void dismissDialog() {
        try {
            if (!mContext.isFinishing()) {
                if (settingDialog != null && settingDialog.isShowing()) {
                    settingDialog.dismiss();
                }
                if (overtimeDialog != null && overtimeDialog.isShowing()) {
                    overtimeDialog.dismiss();
                }
                if (noRoomDialog != null && noRoomDialog.isShowing()) {
                    noRoomDialog.dismiss();
                }
                if (customDialog != null && customDialog.isShowing()) {
                    customDialog.dismiss();
                }
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * @return Dialog    返回类型
     * @throws
     * @Title showSettingDialog
     * @Description 显示 设置 对话框，点击 跳转至 设置网络
     * @author aceway-liwei
     * @date 2012-6-13 上午09:37:33
     */
    public Dialog showSettingDialog() {
        if (settingDialog != null && settingDialog.isShowing()) {
            return settingDialog;
        }

        int iconId = Common.getIdFormDraw(mContext, "alert_dialog_icon");
        if (iconId == 0) {
//			throw new RuntimeException("在 drawable 没有名为alert_dialog_icon的 Dialog图标 ");
        }
        settingDialog = new Builder(getRootActivity(mContext))
                .setIcon(iconId)
                .setTitle(R.string.app_name)
                .setMessage(R.string.no_network)
                .setPositiveButton(R.string.dialog_btn_setting,
                        new OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // 联网失败，setting net
                                openSetting();
                            }
                        })
                .setNegativeButton(R.string.dialog_btn_cancel, null)
                .create();
        settingDialog.show();
        return settingDialog;
    }


    /**
     * @return void    返回类型
     * @throws
     * @Title openSetting
     * @Description 跳转设置页
     * @author aceway-liwei
     * @date 2012-6-13 上午09:31:10
     */
    private void openSetting() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
/*		ComponentName componentName = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
		intent.setComponent(componentName);*/
        intent.setClassName("com.android.settings", "com.android.phone.Settings");

        mContext.startActivity(new Intent(Settings.ACTION_SETTINGS));

//		mContext.startActivity(intent);
    }

    /**
     * \
     *
     * @param baseRequest
     * @return Dialog    返回类型
     * @throws
     * @Title showOvertimeDialog
     * @Description 联网超时, 确定重新联网
     * @author aceway-liwei
     * @date 2012-6-13 上午09:45:31
     */
    public Dialog showOvertimeDialog(final BaseRequest baseRequest, final HttpRequestListener listener) {
        if (overtimeDialog != null && overtimeDialog.isShowing()) {
            return overtimeDialog;
        }
        int iconId = Common.getIdFormDraw(mContext, "alert_dialog_icon");
        if (iconId == 0) {
//			throw new RuntimeException("在 drawable 没有名为alert_dialog_icon的 Dialog图标 ");
        }
        overtimeDialog = new Builder(getRootActivity(mContext))
                .setIcon(iconId)
                .setTitle(R.string.app_name)
                .setMessage(R.string.net_overtime)
                .setPositiveButton(R.string.dialog_btn_again,
                        new OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // 联网超时，重新联网
                                HttpRequestUtils.request(mContext, baseRequest, listener);
                            }
                        })
                .setNegativeButton(R.string.dialog_btn_cancel, null)
                .create();
        overtimeDialog.show();
        return overtimeDialog;
    }

    public Dialog showNoRoomDialog() {
        if (noRoomDialog == null) {
            int iconId = Common.getIdFormDraw(mContext, "alert_dialog_icon");
            if (iconId == 0) {
//			throw new RuntimeException("在 drawable 没有名为alert_dialog_icon的 Dialog图标 ");
            }
            noRoomDialog = new Builder(getRootActivity(mContext))
                    .setIcon(iconId)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.no_room)
                    .setPositiveButton(R.string.dialog_btn_ok, null)
                    .setNegativeButton(R.string.dialog_btn_cancel, null)
                    .create();

        }
        noRoomDialog.show();
        return noRoomDialog;
    }

    /**
     * 关闭当前页 只有 "确定" 按钮 ，点击后 关闭当前页
     *
     * @param content
     * @return Dialog    返回类型
     * @Title showCustomDialog
     * @author aceway-liwei
     * @date 2013-2-26 上午10:25:53
     */
    public Dialog showCustomFinishDialog(String content) {
        OnClickListener positiveClick = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mContext.finish();
            }
        };
        customDialog = showCustomDialog(mContext.getString(R.string.app_name), content, mContext.getString(R.string.dialog_btn_ok), positiveClick, null, null);
        return customDialog;
    }

    /**
     * 关闭fragment 只有 "确定" 按钮 ，点击后 关闭当前页
     *
     * @param content
     * @Title showCustomDialog
     * @author aceway-liwei
     */
    public Dialog showCustomFinishDialog(String content, final BaseFragment fragment) {
        OnClickListener positiveClick = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (fragment != null && fragment.isVisible())
                    fragment.finish();
            }
        };
        if (fragment.isVisible())
            customDialog = showCustomDialog(mContext.getString(R.string.app_name), content, mContext.getString(R.string.dialog_btn_ok), positiveClick, null, null);
        return customDialog;
    }

    /**
     * 用户自定义提示内容   只有 "确定" 按钮
     *
     * @param content
     * @return Dialog    返回类型
     * @Title showCustomDialog
     * @author aceway-liwei
     * @date 2013-2-26 上午10:25:53
     */
    public Dialog showCustomDialog(String content) {
        customDialog = showCustomDialog(mContext.getString(R.string.app_name), content, mContext.getString(R.string.dialog_btn_ok), null, null, null);
        return customDialog;
    }

    /**
     * 用户自定义确定事件 只有 "确定" 按钮 </br>
     * 如果需要两个按钮  可以 {@link  #showCustomDialog(String, OnClickListener, OnClickListener)}
     *
     * @param content
     * @param positiveClick 确定按钮的 点击事件
     * @return Dialog    返回类型
     * @Title showCustomDialog
     * @author aceway-liwei
     * @date 2013-2-26 上午10:25:53
     */
    public Dialog showCustomDialog(String content, OnClickListener positiveClick) {
        customDialog = showCustomDialog(mContext.getString(R.string.app_name), content, mContext.getString(R.string.dialog_btn_ok), positiveClick, null, null);
        return customDialog;
    }

    /**
     * 用户自定义  有 "确定" 和 "取消" 两个按钮,无操作事件 可置 null
     *
     * @param content
     * @param positiveClick 确定按钮的 点击事件
     * @param negativeClick 取消按钮的点击事件
     * @return Dialog    返回类型
     * @Title showCustomDialog
     * @author aceway-liwei
     * @date 2013-2-26 上午10:25:53
     */
    public Dialog showCustomDialog(String content, OnClickListener positiveClick, OnClickListener negativeClick) {
        customDialog = showCustomDialog(mContext.getString(R.string.app_name), content, mContext.getString(R.string.dialog_btn_ok), positiveClick
                , mContext.getString(R.string.dialog_btn_cancel), negativeClick);
        return customDialog;
    }

    /**
     * 用户自定义 提示框
     *
     * @param content
     * @param positiveText  确定按钮的 文字
     * @param positiveClick 确定按钮的 点击事件
     * @param negativeText  取消按钮的文字
     * @param negativeClick 取消按钮的点击事件
     * @return Dialog    返回Dialog
     * @Title showCustomDialog
     * @author aceway-liwei
     * @date 2013-2-26 上午10:22:20
     */
    public Dialog showCustomDialog(String title, String content, String positiveText, OnClickListener positiveClick
            , String negativeText, OnClickListener negativeClick) {
        if (getRootActivity(mContext).isFinishing()) {
            return null;
        }
        if (customDialog != null && customDialog.isShowing()) {
            try {
                customDialog.dismiss();
            } catch (Exception e) {

            }
        }
        int iconId = Common.getIdFormDraw(mContext, "alert_dialog_icon");
        Builder builder = new AlertDialog.Builder(mContext);
        builder.setIcon(iconId)
                .setTitle(title)
                .setMessage(content);
        if (positiveText != null) {
            builder.setPositiveButton(positiveText, positiveClick);
        }
        if (negativeText != null) {
            builder.setNegativeButton(negativeText, negativeClick);
        }
        builder.setCancelable(true);
        customDialog = builder.create();
        customDialog.show();
        return customDialog;
    }

    public void showUpdateDialog(boolean hasUpdate,boolean force, String content, final String url, OnClickListener positiveClick, OnClickListener negativeClick) {
        MLog.e("版本检测 hasUpdate="+hasUpdate+" force="+force);
        if (TextUtils.isEmpty(url)||!hasUpdate) {
            if (negativeClick != null)
                negativeClick.onClick(null, 0);
            return;
        }
        //新版本推送
        if (content == null) {
            versionDialog = new Builder(mContext).create();
        } else {
            versionDialog = new AlertDialog.Builder(mContext).setItems(content.split("#"), null).create();
        }
        versionDialog.setTitle("发现新版本");
        versionDialog.setCancelable(false);
        if (!force) {
            versionDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "稍后更新", negativeClick);
        }
        versionDialog.setButton(DialogInterface.BUTTON_POSITIVE, "前去更新", positiveClick);
        if (!versionDialog.isShowing()) {
            versionDialog.show();
        }
    }

    public void showUpdateDialog(boolean hasUpdate, boolean force, String content, final String url) {
        if (TextUtils.isEmpty(url)||!hasUpdate) {
            return;
        }
        //新版本推送
        if (content == null) {
            versionDialog = new Builder(mContext).create();
        } else {
            versionDialog = new AlertDialog.Builder(mContext).setItems(content.split("#"), null).create();
        }
        versionDialog.setTitle("发现新版本");
        versionDialog.setCancelable(false);
        if (!force) {
            versionDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "稍后更新", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    versionDialog.dismiss();
                }
            });
        }
        versionDialog.setButton(DialogInterface.BUTTON_POSITIVE, "去google play更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonUtils.launchAppDetail(BuildConfig.APPLICATION_ID, "com.android.vending");
            }
        });
        if (!versionDialog.isShowing()) {
            versionDialog.show();
        }
    }

    public AlertDialog getVersionDialog() {
        return versionDialog;
    }

    //打开浏览器
    protected void openBrowser(String url) {

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }


    /**
     * @return Activity    返回类型
     * @throws
     * @Title getRootActivity
     * @Description 得到 最上层的Activity,确保dialog的稳定
     * @author aceway-liwei
     * @date 2012-6-6 上午10:41:07
     */
    public Activity getRootActivity(Activity activtiy) {
//        Activity context = activtiy.getParent();
//        if (context == null) {
//            return activtiy;
//        } else {
//            return getRootActivity(context);
//        }
        return activtiy;
    }

    public void showCallDialog(final String... source) {
        showDefaultServiceDialog(mContext, "联系客服", source != null && source.length > 0 ? source[0] : "");
    }

    public static AlertDialog showCallDialogTitle(final Context _context, final String source, final int sourceType) {
        String[] str = {"境内客服:" + Constants.CALL_NUMBER_IN, "境外客服:" + Constants.CALL_NUMBER_OUT, "取消"};
        AlertDialog dialog = new AlertDialog.Builder(_context)
                .setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            PhoneInfo.CallDial(_context, Constants.CALL_NUMBER_IN);
                            StatisticClickEvent.click(StatisticConstant.CLICK_CONCULT_TYPE, "电话");
                        } else if (which == 1) {
                            PhoneInfo.CallDial(_context, Constants.CALL_NUMBER_OUT);
                            StatisticClickEvent.click(StatisticConstant.CLICK_CONCULT_TYPE, "电话");
                        } else {
                            dialog.dismiss();
                        }
                        SensorsUtils.setSensorsServiceEvent(sourceType, source, which);
                    }
                }).create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }


    public static AlertDialog showDefaultServiceDialog(final Context _context, String source) {
        return showDefaultServiceDialog(_context, null, source);
    }

    public static AlertDialog showDefaultServiceDialog(final Context _context, final String _title, String source) {
        return showServiceDialog(_context, _title, UnicornServiceActivity.SourceType.TYPE_DEFAULT, null, null, source);
    }

    public static AlertDialog showServiceDialog(final Context _context, final String _title, final int sourceType, final OrderBean orderBean, final SkuItemBean skuItemBean, final String source) {
        if ((sourceType == UnicornServiceActivity.SourceType.TYPE_LINE && skuItemBean == null)
                || (sourceType == UnicornServiceActivity.SourceType.TYPE_ORDER && orderBean == null) ) {
            return showDefaultServiceDialog(_context, source);
        }
        /*String title = _title;
        if (TextUtils.isEmpty(title)) {
            title = "咨询小助手";
        }*/
        String [] str = {"在线咨询", "境内用户客服热线", "境外用户客服专线","取消"};
        AlertDialog dialog = new AlertDialog.Builder(_context)
                //.setTitle(title)
                .setItems(str,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            UnicornUtils.openServiceActivity(_context, sourceType, orderBean, skuItemBean);
                        } else if (which == 1) {
                            PhoneInfo.CallDial(_context, Constants.CALL_NUMBER_IN);
                            StatisticClickEvent.click(StatisticConstant.CLICK_CONCULT_TYPE, "电话");
                        } else if(which ==2){
                            PhoneInfo.CallDial(_context, Constants.CALL_NUMBER_OUT);
                            StatisticClickEvent.click(StatisticConstant.CLICK_CONCULT_TYPE, "电话");
                        }else if(which == 3){
                            dialog.dismiss();
                        }
                        SensorsUtils.setSensorsServiceEvent(sourceType, source, which);
                    }
                }).create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }

}
