package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.m.permission.MPermissions;

import org.greenrobot.eventbus.EventBus;


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

    protected String source = ""; //友盟统计用 获取从哪个界面进入
    protected String umeng_key = "";//友盟事件ID
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentId = R.id.drawer_layout;
        getFrom();
        getBusinessType();
    }


    public void getFrom(){
        if(null != this.getArguments()) {
            source = this.getArguments().getString("source");
        }
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
                Object o = v.getTag();
                String source = "";
                if(o != null && o instanceof String){
                    source = (String) o;
                }
                if(!TextUtils.isEmpty(source)){
                    String[] strArr = source.split(",");
                    if (strArr != null && strArr.length == 3) {
                        DialogUtil.getInstance(getActivity()).showCallDialog(strArr[0],strArr[1],strArr[2]);
                    }
                }else{
                    DialogUtil.getInstance(getActivity()).showCallDialog();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this.getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this.getActivity());
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
        if(this instanceof FgPickNew || this instanceof FgSendNew
                | this instanceof FgChooseAirAddress){
            EventBus.getDefault().post(new EventAction(EventType.PICK_SEND_ONBACKPRESS));
        }else {
            EventBus.getDefault().post(new EventAction(EventType.ONBACKPRESS));
        }
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
        CommonUtils.showToast(tips);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
