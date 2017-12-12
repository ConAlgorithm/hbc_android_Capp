package com.hugboga.custom.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.UnicornUtils;

/**
 * Created by zhangqiang on 17/8/31.
 */

public class CsDialog extends Dialog implements View.OnClickListener {
    private CsDialog.Params mParams;
    Context context;
    private CsDialog.OnCsListener listener;
    private boolean hasOnline = true;
    public CsDialog(Context context) {
        this(context, R.style.ShareDialog,true);
    }
    public CsDialog(Context context,boolean hasOnline) {
        this(context, R.style.ShareDialog,hasOnline);
    }
    public CsDialog(Context context, int themeResId,boolean hasOnline) {
        super(context, themeResId);
        this.context = context;
        setContentView(R.layout.view_cs_dialog);

        if (context instanceof Activity) {
            WindowManager windowManager = ((Activity) context).getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.width = display.getWidth();
            getWindow().setAttributes(lp);
        }

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        findViewById(R.id.online_cs_layout).setOnClickListener(this);
        findViewById(R.id.china_cs_layout).setOnClickListener(this);
        findViewById(R.id.adroad_cs_layout).setOnClickListener(this);
        findViewById(R.id.dialog_share_cancel_tv).setOnClickListener(this);
        findViewById(R.id.dialog_share_shadow_view).setOnClickListener(this);
        if(!hasOnline){
            findViewById(R.id.online_cs_layout).setVisibility(View.GONE);
            LinearLayout csLayout = (LinearLayout)findViewById(R.id.china_cs_layout);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) csLayout.getLayoutParams();
            lp.setMargins(0,0,0,0);
            findViewById(R.id.online_cs_layout).setLayoutParams(lp);
        }
    }

    public void setOnline(boolean hasOnline){
        this.hasOnline = hasOnline;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.online_cs_layout:
                UnicornUtils.openServiceActivity(context, mParams.sourceType, mParams.orderBean, mParams.skuItemBean);
                if (listener != null) {
                    listener.onCs();
                }
                break;
            case R.id.china_cs_layout:
                PhoneInfo.CallDial(context, Constants.CALL_NUMBER_IN);
                StatisticClickEvent.click(StatisticConstant.CLICK_CONCULT_TYPE, "电话");
                if (listener != null) {
                    listener.onCs();
                }
                break;
            case R.id.adroad_cs_layout:
                PhoneInfo.CallDial(context, Constants.CALL_NUMBER_OUT);
                StatisticClickEvent.click(StatisticConstant.CLICK_CONCULT_TYPE, "电话");
                if (listener != null) {
                    listener.onCs();
                }
                break;
            case R.id.dialog_share_shadow_view:
            case R.id.dialog_share_cancel_tv:
                dismiss();
                break;
        }
    }
    public void setParams(CsDialog.Params params) {
        this.mParams = params;
    }
    public static class Params {
        String source;
        OrderBean orderBean;
        SkuItemBean skuItemBean;
        int sourceType;
        String _title;

        public Params(String _title, int sourceType, OrderBean orderBean, SkuItemBean skuItemBean, String source) {
            this._title = _title;
            this.sourceType = sourceType;
            this.orderBean = orderBean;
            this.skuItemBean = skuItemBean;
            this.source = source;
        }

    }

    public interface OnCsListener {
        void onCs();
    }

    public void setOnCsListener(CsDialog.OnCsListener listener) {
        this.listener = listener;
    }
}
