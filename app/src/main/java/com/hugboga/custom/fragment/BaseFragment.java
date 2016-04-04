package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.PhoneInfo;
import com.zhy.m.permission.MPermissions;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;


public abstract class BaseFragment extends com.huangbaoche.hbcframe.fragment.BaseFragment implements View.OnClickListener {
    public static String KEY_TITLE = "key_title";
    public static String KEY_FROM = "key_from";
    public static String KEY_BUSINESS_TYPE = "key_business_Type";
    public static String KEY_GOODS_TYPE = "key_goods_type";


    protected int mBusinessType = -1;//业务类型 1接机2送机3包车4次租
    protected int mGoodsType = -1;//1: 接机 2: 送机 3: 市内包车(由日租拆分出来) 4: 次租 5: 精品线路(由日租拆分出来) 6: 小长途 (由日租拆分出来)7: 大长途 (由日租拆分出来)

    protected TextView fgTitle; //标题
    protected TextView fgRightBtn; //右按钮
    protected View fgLeftBtn;//左按钮


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentId = R.id.drawer_layout;
        getBusinessType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView != null) {
            fgTitle = (TextView) contentView.findViewById(R.id.header_title);
            fgLeftBtn = contentView.findViewById(R.id.header_left_btn);
            fgRightBtn = (TextView) contentView.findViewById(R.id.header_right_txt);
            if (fgRightBtn != null) fgRightBtn.setOnClickListener(this);
            if (fgLeftBtn != null) fgLeftBtn.setOnClickListener(this);
        }
        return contentView;
    }

    /**
     * 当前的业务类型  Constants.BUSINESS_TYPE_OTHER
     */
    protected int getBusinessType() {
        if (mBusinessType != -1) return mBusinessType;
        if (getArguments() != null) {
            mBusinessType = getArguments().getInt(KEY_BUSINESS_TYPE, -1);
        }
        return mBusinessType;
    }

    public void setBusinessType(int businessType) {
        mBusinessType = businessType;
    }

    public void setGoodsType(int goodsType) {
        mGoodsType = goodsType;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn:
                MLog.e("header_left_btn");
                finish();
                break;
            case R.id.header_right_txt:
                showContactServiceDialog();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        collapseSoftInputMethod();
        EventBus.getDefault().post(new EventAction(EventType.CLICK_HEADER_LEFT_BTN_BACK));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().post(new EventAction(EventType.START_NEW_FRAGMENT));
    }

    @Override
    public boolean onBackPressed() {
        EventBus.getDefault().post(new EventAction(EventType.CLICK_HEADER_LEFT_BTN_BACK));
        return super.onBackPressed();
    }


    public void startFragment(BaseFragment fragment) {
        Bundle bundle = fragment.getArguments();
        bundle = bundle == null ? new Bundle() : bundle;
        startFragment(fragment, bundle);
    }

    public void startFragment(BaseFragment fragment, Bundle bundle) {
        collapseSoftInputMethod();
        editTextClearFocus();
        int tmpBusinessType = -1;
        int tmpGoodsType = -1;
        if (bundle != null && fragment != null) {
            fragment.setArguments(bundle);
            tmpBusinessType = bundle.getInt(KEY_BUSINESS_TYPE, -1);
            tmpGoodsType = bundle.getInt(KEY_GOODS_TYPE, -1);
        }
        if (fragment != null) {
            fragment.setSourceFragment(this);
            fragment.setBusinessType(tmpBusinessType == -1 ? mBusinessType : tmpBusinessType);
            fragment.setGoodsType(tmpGoodsType == -1 ? mGoodsType : tmpGoodsType);
        }
        super.startFragment(fragment);

    }


    /**
     * 切换流程状态 填写行程- 选车-填单-支付
     *
     * @param index 第几项 [0-3]
     */

    public void setProgressState(int index) {
        View bootView = getView();
        int[] textIds = {R.id.progress_text_1,
                R.id.progress_text_2,
                R.id.progress_text_3,
                R.id.progress_text_4,
        };
        int[] iconIds = {R.id.progress_icon_1,
                R.id.progress_icon_2,
                R.id.progress_icon_3,
                R.id.progress_icon_4,
        };
        for (int i = 0; i < textIds.length; i++) {
            View text = bootView.findViewById(textIds[i]);
            View icon = bootView.findViewById(iconIds[i]);
            if (text == null || icon == null) continue;
            if (index == i) {
                TextPaint tp = ((TextView) text).getPaint();
                tp.setFakeBoldText(true);
                bootView.findViewById(textIds[i]).setEnabled(true);
                bootView.findViewById(iconIds[i]).setSelected(true);
            } else {
                bootView.findViewById(textIds[i]).setEnabled(false);
                bootView.findViewById(iconIds[i]).setSelected(false);
                if (index < i) {
                    bootView.findViewById(iconIds[i]).setEnabled(false);
                } else {
                    bootView.findViewById(iconIds[i]).setEnabled(true);
                }
            }
        }
    }

    public void showTip(String tips) {
        Toast.makeText(getActivity(), tips, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showContactServiceDialog(){
        final AlertDialog.Builder callDialog = new AlertDialog.Builder(getActivity());
        callDialog.setTitle("呼叫客服");
        final String [] callItems = new String[]{Constants.CALL_NUMBER_IN,Constants.CALL_NUMBER_OUT};
        callDialog.setItems(callItems,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PhoneInfo.CallDial(getActivity(), callItems[which]);
            }
        });
        AlertDialog dialog = callDialog.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }
}
