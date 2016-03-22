package com.hugboga.custom.widget;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.utils.Common;
import com.hugboga.custom.utils.PhoneInfo;

/**
 * <p> DialogUtil mDialogUtil = new DialogUtil(activity);
 * <p> mDialogUtil.showLoadingDialog();
 * <p>  mDialogUtil.dismissLoadingDialog();
 *
 * @date 2012-6-6 上午10:41:22
 */
public class DialogUtil {
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
        if (mContext.isFinishing()) {
            return mLoadingDialog;
        }
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            TextView text = (TextView) mLoadingDialog.getWindow().findViewById(R.id.loading_message);
            text.setText(message);
            mLoadingDialog.setCancelable(cancelAble);
//			mLoadingDialog.cancel();
        } else {
            View loadingView = (View) mContext.getLayoutInflater().inflate(R.layout.dailog_loading, null);
            TextView text = (TextView) loadingView.findViewById(R.id.loading_message);
            if (!TextUtils.isEmpty(message))
                text.setText(message);
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            mLoadingDialog = new Dialog(getRootActivity(mContext), R.style.loading_dialog_style);
//		mLoadingDialog=	new Dialog(mContext);
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.setCancelable(cancelAble);
            mLoadingDialog.show();
            mLoadingDialog.getWindow().setContentView(loadingView);
        }
        return mLoadingDialog;
    }

    /**
     * @Title dismissLoadingDialog
     * @Description 隐藏 loadingDialog
     * @author aceway-liwei
     * @date 2012-6-13 上午09:37:05
     */
    public void dismissLoadingDialog() {
        if (!mContext.isFinishing() && mLoadingDialog != null)
            mLoadingDialog.dismiss();
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

//    /**
//     * \
//     *
//     * @param syncServices
//     * @return Dialog    返回类型
//     * @throws
//     * @Title showOvertimeDialog
//     * @Description 联网超时, 确定重新联网
//     * @author aceway-liwei
//     * @date 2012-6-13 上午09:45:31
//     */
    /*public Dialog showOvertimeDialog(final HttpRequestUtils syncServices) {
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
                                syncServices.execute();
                            }
                        })
                .setNegativeButton(R.string.dialog_btn_cancel, null)
                .create();
        overtimeDialog.show();
        return overtimeDialog;
    }*/

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
        OnClickListener postitiveClick = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mContext.finish();
            }
        };
        customDialog = showCustomDialog(mContext.getString(R.string.app_name), content, mContext.getString(R.string.dialog_btn_ok), postitiveClick, null, null);
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
        OnClickListener postitiveClick = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (fragment != null && fragment.isVisible())
                    fragment.finish();
            }
        };
        if (fragment.isVisible())
            customDialog = showCustomDialog(mContext.getString(R.string.app_name), content, mContext.getString(R.string.dialog_btn_ok), postitiveClick, null, null);
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
     * @param postitiveClick 确定按钮的 点击事件
     * @return Dialog    返回类型
     * @Title showCustomDialog
     * @author aceway-liwei
     * @date 2013-2-26 上午10:25:53
     */
    public Dialog showCustomDialog(String content, OnClickListener postitiveClick) {
        customDialog = showCustomDialog(mContext.getString(R.string.app_name), content, mContext.getString(R.string.dialog_btn_ok), postitiveClick, null, null);
        return customDialog;
    }

    /**
     * 用户自定义  有 "确定" 和 "取消" 两个按钮,无操作事件 可置 null
     *
     * @param content
     * @param postitiveClick 确定按钮的 点击事件
     * @param negativeClick  取消按钮的点击事件
     * @return Dialog    返回类型
     * @Title showCustomDialog
     * @author aceway-liwei
     * @date 2013-2-26 上午10:25:53
     */
    public Dialog showCustomDialog(String content, OnClickListener postitiveClick, OnClickListener negativeClick) {
        customDialog = showCustomDialog(mContext.getString(R.string.app_name), content, mContext.getString(R.string.dialog_btn_ok), postitiveClick
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
            customDialog.cancel();
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
        builder.setCancelable(false);
        customDialog = builder.create();
        customDialog.show();
        return customDialog;
    }

//    /**
//     * 根据state 做系统事件
//     *
//     * @param content 内容提示
//     * @param state   状态值
//     * @param opr     辅助参数
//     * @return Dialog    返回类型
//     * @Title showCustomDialog
//     * @author aceway-liwei
//     * @date 2013-3-7 下午02:16:02
//     */
//    public Dialog showCustomDialog(String content, final int state, final String opr) {
//        VersionBean versionBean = null;
//        if(state==-999){
//            //强制更新，解析内容
//            versionBean = new VersionBean();
//            try {
//                versionBean.parser(content);
//                content = versionBean.content;
//            } catch (JSONException e) {
//                e.printStackTrace();
//                content = "系统错误";
//            }
//        }
//        final VersionBean finalVersionBean = versionBean;
//        OnClickListener listener = new OnClickListener() {
//            private Intent intent;
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (state) {
//                    case 10012://userToken不合法或已失效，登录信息失效，请重新登录
//                        UserEntity.getUser().setUserId(mContext, null);
//                        UserEntity.getUser().setUserToken(mContext, null);
//                        if (mContext instanceof BaseFragmentActivity) {
//                            BaseFragmentActivity activity = (BaseFragmentActivity) mContext;
//                            BaseFragment fragment = null;
//                            for (Fragment fg : activity.getSupportFragmentManager().getFragments()) {
//                                if (fg != null && fg instanceof BaseFragment) {
//                                    fragment = (BaseFragment) fg;
//                                }
//                            }
//                            if (fragment != null) {
//                                fragment.bringToFront(FgHome.class, new Bundle());
//                                fragment.startFragment(new FgLogin());
//                            }
//                        }
//                        break;
//                    case 10013:
//                        //  设备禁止访问，则直接退出重新登录
//                        UserEntity.getUser().clean(mContext);
//                        if (mContext instanceof BaseFragmentActivity) {
//                            BaseFragmentActivity activity = (BaseFragmentActivity) mContext;
//                            BaseFragment fragment = null;
//                            for (Fragment fg : activity.getSupportFragmentManager().getFragments()) {
//                                if (fg != null && fg instanceof BaseFragment) {
//                                    fragment = (BaseFragment) fg;
//                                }
//                            }
//                            if (fragment != null) {
//                                fragment.bringToFront(FgHome.class, new Bundle());
//                                fragment.startFragment(new FgLogin());
//                            }
//                        }
//                        break;
//                    case -888:
//                        //强制退出
//                        UserEntity.getUser().clean(mContext);
//                        if (mContext instanceof BaseFragmentActivity) {
//                            BaseFragmentActivity activity = (BaseFragmentActivity) mContext;
//                            BaseFragment fragment = null;
//                            for (Fragment fg : activity.getSupportFragmentManager().getFragments()) {
//                                if (fg != null && fg instanceof BaseFragment) {
//                                    fragment = (BaseFragment) fg;
//                                }
//                            }
//                            if (fragment != null) {
//                                fragment.finish();
//                            }
//                        }
//                        System.exit(0); //强制退出
//                        break;
//                    case -999:
//                        //强制更新
//                        if (mContext instanceof BaseFragmentActivity) {
//                            BaseFragmentActivity activity = (BaseFragmentActivity) mContext;
//                            BaseFragment fragment = null;
//                            for (Fragment fg : activity.getSupportFragmentManager().getFragments()) {
//                                if (fg != null && fg instanceof BaseFragment) {
//                                    fragment = (BaseFragment) fg;
//                                }
//                            }
//                            if (fragment != null) {
//                                // 强制更新，并且点击开始下载更新
//                                PushUtils.startDownloadApk(mContext, finalVersionBean.url);
//                            }
//                        }
//                        break;
//                }
//            }
//        };
//        customDialog = showCustomDialog(content, listener);
//        return customDialog;
//    }

    private Intent getStartAcitityIntent(int opr) {
        Intent intent = null;
        /*switch (opr) {
        case 0://登录
			intent=new Intent(mContext,LoginActivity.class);
			break;
		case 1://首页
			intent =new Intent(mContext,LoginActivity.class);
			break;
		case 2://关于
			intent =new Intent(mContext,LoginActivity.class);
			break;
		case 3://个人信息
			intent =new Intent(mContext,PersonalInfoAcitity.class);
			break;
		default:
			break;
		}*/
        return intent;
    }
    public void showUpdateDialog(String force, String content, final String url,OnClickListener positiveClick,OnClickListener negativeClick){
        if(TextUtils.isEmpty(url)) {
            if(negativeClick!=null)
                negativeClick.onClick(null,0);
            return;
        }
        //新版本推送
        if(content==null){
            versionDialog = new Builder(mContext).create();
        }else{
            versionDialog = new AlertDialog.Builder(mContext).setItems(content.split("#"), null).create();
        }
        versionDialog.setTitle("发现新版本");
        versionDialog.setCancelable(false);
        if(!force.equals("true")){
            versionDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "稍后更新",  negativeClick);
        }
        versionDialog.setButton(DialogInterface.BUTTON_POSITIVE, "前去更新", positiveClick);
        if(!versionDialog.isShowing()){
            versionDialog.show();
        }
    }

    /* public Dialog showVersionCheck(String title, String content,final String downloadUrl,final boolean forceUpdate,View.OnClickListener closeClickListener){
            Builder builder = new Builder(mContext,R.style.ContentOverlay);
            View  view = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
            tv_title.setText(title);
            tv_msg.setText(content);
            View.OnClickListener onclick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBrower(downloadUrl);
                }
            };
            setVersionButton(view, forceUpdate,downloadUrl, onclick,closeClickListener);
//			builder.setView(view);
            mUpdateVersionDialog = builder.create();
            mUpdateVersionDialog.setCancelable(!forceUpdate);
            mUpdateVersionDialog.show();
            mUpdateVersionDialog.getWindow().setContentView(view);

         return mUpdateVersionDialog;
     }*/
    /* private void setVersionButton(View view ,boolean  forceUpdate,String url,View.OnClickListener updateClickListener,final View.OnClickListener closeClickListener) {
            TextView tv_negative = (TextView) view.findViewById(R.id.tv_negative);
			TextView tv_positive = (TextView) view.findViewById(R.id.tv_positive);
			tv_negative.setTag(url);
			tv_positive.setTag(url);
			if(forceUpdate){
				tv_positive.setText("现在升级");
				tv_negative.setText("退出程序");
				tv_negative.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mContext.finish();
					}
				});
			}else{
				tv_positive.setText("现在升级");
				tv_negative.setText("稍后升级");
				tv_negative.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mUpdateVersionDialog.dismiss();
						if(closeClickListener!=null)
						closeClickListener.onClick(v);
					}
				});
			}
			tv_positive.setOnClickListener(updateClickListener);
		}*/
    //打开浏览器
    protected void openBrower(String url) {

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
        Activity context = activtiy.getParent();
        if (context == null) {
            return activtiy;
        } else {
            return getRootActivity(context);
        }
    }

    public void showCallDialog() {
        String[] str = {"境内客服:" + Constants.CALL_NUMBER_IN, "境外客服:" + Constants.CALL_NUMBER_OUT};
        new AlertDialog.Builder(getRootActivity(mContext))
                .setTitle("联系客服")
                .setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            PhoneInfo.CallDial(mContext, Constants.CALL_NUMBER_IN);
                        } else {
                            PhoneInfo.CallDial(mContext, Constants.CALL_NUMBER_OUT);
                        }
                    }
                }).show();
    }

}
