package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;


public abstract class BaseFragment extends com.huangbaoche.hbcframe.fragment.BaseFragment {
    public static String KEY_TITLE = "key_title";
    public static String KEY_FROM = "key_from";
    public static String KEY_BUSINESS_TYPE = "key_business_Type";
    public static String KEY_GOODS_TYPE = "key_goods_type";


    protected int mBusinessType = -1;//业务类型 1接机2送机3包车4次租
    protected int mGoodsType = -1;//1: 接机 2: 送机 3: 市内包车(由日租拆分出来) 4: 次租 5: 精品线路(由日租拆分出来) 6: 小长途 (由日租拆分出来)7: 大长途 (由日租拆分出来)

    @ViewInject(R.id.header_title)
    protected TextView fgTitle; //标题


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentId = R.id.drawer_layout;
        getBusinessType();
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

    @Event({R.id.header_left_btn})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                MLog.e("header_left_btn");
                finish();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().post(new EventAction(EventType.CLICK_HEADER_LEFT_BTN_BACK));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().post(new EventAction(EventType.START_NEW_FRAGMENT));
    }

    @Override
    public boolean onBackPressed() {
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
            fragment.setTarget(this);
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
                TextPaint tp = ((TextView)text).getPaint();
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
}
