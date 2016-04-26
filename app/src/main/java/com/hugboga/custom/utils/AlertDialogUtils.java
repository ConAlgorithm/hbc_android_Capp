package com.hugboga.custom.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by dyt on 16/4/16.
 */
public class AlertDialogUtils {

    public static void showAlertDialog(Context context,String content){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setMessage(content);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static AlertDialog showAlertDialog(Context context, String content, String okText, String cancleText,
                                       DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancleClick){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setMessage(content);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,okText, okClick);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancleText, cancleClick);
        dialog.show();
        return dialog;
    }
}
