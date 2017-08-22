package com.hugboga.custom.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.huangbaoche.hbcframe.util.DialogUtils;

/**
 * Created  on 16/4/16.
 */
public class AlertDialogUtils {

    public static void showAlertDialog(Context context,String content){
        DialogUtils.showAlertDialog(context, content);
    }

    public static void showAlertDialogOneBtn(Context context,String content,String okText){
        DialogUtils.showAlertDialogOneBtn(context, content,okText);
    }

    public static void showAlertDialog(Context context,String content,String okText,DialogInterface.OnClickListener onClick){
        DialogUtils.showAlertDialog(context, content,okText,onClick);
    }

    public static AlertDialog showAlertDialog(Context context, String content, String okText, String cancleText,
                                       DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancleClick){
        return DialogUtils.showAlertDialog(context, content,okText,cancleText,okClick,cancleClick);
    }

    public static AlertDialog showAlertDialog(Context context, boolean cancelable, String title, String content, String okText, String cancleText
                                              ,DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancleClick){
        return DialogUtils.showAlertDialog(context,cancelable, title, content,okText,cancleText,okClick,cancleClick);
    }


    public static AlertDialog showAlertDialog(Context context, String title, String content, String okText, String cancleText,
                                              DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancleClick){
        return DialogUtils.showAlertDialog(context, title, content,okText,cancleText,okClick,cancleClick);
    }

    public static AlertDialog showAlertDialogCancelable(Context context, String content, String okText, String cancleText,
                                              DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancleClick){
        return DialogUtils.showAlertDialog(context, content,okText,cancleText,okClick,cancleClick);
    }

    public static AlertDialog showAlertDialog(Context context, String title, String content, String okText, String cancleText, String neutralText,
                                              DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancleClick
                                                ,DialogInterface.OnClickListener neutralClick) {
        return DialogUtils.showAlertDialog(context,title, content,okText,cancleText,neutralText,okClick,cancleClick,neutralClick);
    }

    public static AlertDialog showAlertDialog(Context context,boolean cancelable, String title, String content,String okText,DialogInterface.OnClickListener onClick){
        return DialogUtils.showAlertDialog(context,cancelable, title, content,okText,onClick);
    }

    public static AlertDialog showAlertDialog(Context context, boolean cancelable, boolean canceledOnTouchOutside, String title, String content) {
        return DialogUtils.showAlertDialog(context,cancelable, canceledOnTouchOutside, title, content);
    }

}
