package com.huangbaoche.hbcframe.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.R;

public class DialogUtils {

    private static final int COLOR_POSITIVE = 0xFFFFB209;
    private static final int COLOR_NEGATIVE = 0xFF929292;
    private static final int COLOR_NEUTRAL = 0xFF929292;

    private static void setButtonColor(AlertDialog dialog, int whichButton) {
        if (dialog == null || dialog.getButton(whichButton) == null) {
            return;
        }
        int color = 0;
        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                color = COLOR_POSITIVE;
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                color = COLOR_NEGATIVE;
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                color = COLOR_NEUTRAL;
                break;
        }
        if (color == 0) {
            return;
        }
        dialog.getButton(whichButton).setTextColor(color);
    }

    public static void showAlertDialog(Context context, String content) {
        showAlertDialog(context, content, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public static void showAlertDialogOneBtn(Context context,String content,String okText){
        showAlertDialog(context, content, okText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public static void showAlertDialog(Context context,String content,String okText,DialogInterface.OnClickListener onClick){
        showAlertDialog(context, false, null, content, okText, onClick);
    }

    public static AlertDialog showAlertDialog(Context context,boolean cancelable, String title, String content,String okText,DialogInterface.OnClickListener onClick){
        LinearLayout rl = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_dialog, null);
        TextView titleTV = (TextView) rl.findViewById(R.id.view_dialog_title_tv);
        if (!TextUtils.isEmpty(title)) {
            titleTV.setText(title);
            titleTV.setVisibility(View.VISIBLE);
        } else {
            titleTV.setVisibility(View.GONE);
        }
        TextView contentTV = (TextView) rl.findViewById(R.id.view_dialog_content_tv);
        contentTV.setText(content);

        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(rl);
        AlertDialog dialog = builder.create();
        builder.setCancelable(cancelable);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, okText, onClick);
        dialog.show();
        setButtonColor(dialog, DialogInterface.BUTTON_POSITIVE);
        return dialog;
    }

    public static AlertDialog showAlertDialog(Context context, boolean cancelable, boolean canceledOnTouchOutside, String title, String content) {
        LinearLayout rl = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_dialog, null);
        TextView titleTV = (TextView) rl.findViewById(R.id.view_dialog_title_tv);
        if (!TextUtils.isEmpty(title)) {
            titleTV.setText(title);
            titleTV.setVisibility(View.VISIBLE);
        } else {
            titleTV.setVisibility(View.GONE);
        }

        TextView contentTV = (TextView) rl.findViewById(R.id.view_dialog_content_tv);
        contentTV.setText(content);

        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(rl);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        dialog.show();
        return dialog;
    }

    public static AlertDialog showAlertDialog(Context context, String content, String okText, String cancleText,
                                              DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancleClick){
        return showAlertDialog(context, false, null, content, okText, cancleText, okClick, cancleClick);
    }

    public static AlertDialog showAlertDialog(Context context, String title, String content, String okText, String cancleText,
                                              DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancleClick){
        return showAlertDialog(context, false, title, content, okText, cancleText, okClick, cancleClick);
    }

    public static AlertDialog showAlertDialog(Context context, boolean cancelable, String title, String content, String okText, String cancleText
            ,DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancleClick){
        LinearLayout rl = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_dialog, null);
        TextView titleTV = (TextView) rl.findViewById(R.id.view_dialog_title_tv);
        if (!TextUtils.isEmpty(title)) {
            titleTV.setText(title);
            titleTV.setVisibility(View.VISIBLE);
        } else {
            titleTV.setVisibility(View.GONE);
        }

        TextView contentTV = (TextView) rl.findViewById(R.id.view_dialog_content_tv);
        contentTV.setText(content);

        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(rl);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(cancelable);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, okText, okClick);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancleText, cancleClick);
        dialog.show();

        setButtonColor(dialog, DialogInterface.BUTTON_POSITIVE);
        setButtonColor(dialog, DialogInterface.BUTTON_NEGATIVE);
        return dialog;
    }

    public static AlertDialog showAlertDialog(Context context, String title, String content, String okText, String cancleText, String neutralText,
                                              DialogInterface.OnClickListener okClick, DialogInterface.OnClickListener cancleClick
            ,DialogInterface.OnClickListener neutralClick) {
        LinearLayout rl = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_dialog, null);
        TextView titleTV = (TextView) rl.findViewById(R.id.view_dialog_title_tv);
        if (!TextUtils.isEmpty(title)) {
            titleTV.setText(title);
            titleTV.setVisibility(View.VISIBLE);
        } else {
            titleTV.setVisibility(View.GONE);
        }

        TextView contentTV = (TextView) rl.findViewById(R.id.view_dialog_content_tv);
        contentTV.setText(content);

        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(rl);
        AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, okText, okClick);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancleText, cancleClick);
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, neutralText, neutralClick);
        dialog.show();

        setButtonColor(dialog, DialogInterface.BUTTON_POSITIVE);
        setButtonColor(dialog, DialogInterface.BUTTON_NEGATIVE);
        setButtonColor(dialog, DialogInterface.BUTTON_NEUTRAL);
        return dialog;
    }
}
