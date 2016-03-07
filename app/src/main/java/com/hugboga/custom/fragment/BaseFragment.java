package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;

import org.xutils.view.annotation.ViewInject;


public abstract class BaseFragment extends com.huangbaoche.hbcframe.fragment.BaseFragment{
    public static String KEY_TITLE = "key_title";
    public static String KEY_FROM = "key_from";
    public static String KEY_FRAGMENT_NAME = "key_fragment_name";
    public static String KEY_BUSINESS_TYPE = "key_business_Type";
    public static String KEY_GOODS_TYPE = "key_goods_type";


    protected int mBusinessType = -1;//业务类型 1接机2送机3包车4次租


    @ViewInject(R.id.header_title)
    protected TextView fgTitle; //标题

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentId = R.id.drawer_layout;
    }

    /**
     *  当前的业务类型  Constants.BUSINESS_TYPE_OTHER
     */
    protected int getBusinessType(){
        if(mBusinessType!=-1)return mBusinessType;
        if(getArguments()!=null){
            mBusinessType = getArguments().getInt(KEY_BUSINESS_TYPE,-1);
        }
        return mBusinessType;
    }

    public void setBusinessType(int businessType){
        mBusinessType = businessType;
    }

    /**
     * 回传数据使用，启动fragment 在 onFragmentResult中接收数据
     *
     * @param bundle 参数
     */
    public void finishForResult(Bundle bundle) {

        collapseSoftInputMethod();
        finish();
        Bundle mBundle = new Bundle();
        if(getArguments()!=null) mBundle.putAll(getArguments());
        if (bundle != null )mBundle.putAll(bundle);
        mBundle.putString(KEY_FRAGMENT_NAME, this.getClass().getSimpleName());
        Fragment fragment = this.getTarget();
        if (fragment != null && fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onFragmentResult(mBundle);
        }
        MLog.i(this + " finishForResult " + fragment);

    }

    /**
     * 切换流程状态 填写行程- 选车-填单-支付
     *
     * @param index 第几项 [0-3]
     */

    public void setProgressState(int index) {
//        View bootView = getView();
//        int[] textIds = {R.id.progress_text_1,
//                R.id.progress_text_2,
//                R.id.progress_text_3,
//                R.id.progress_text_4,
//        };
//        int[] iconIds = {R.id.progress_icon_1,
//                R.id.progress_icon_2,
//                R.id.progress_icon_3,
//                R.id.progress_icon_4,
//        };
//        for (int i = 0; i < textIds.length; i++) {
//            View text = bootView.findViewById(textIds[i]);
//            View icon = bootView.findViewById(iconIds[i]);
//            if (text == null || icon == null) continue;
//            if (index == i) {
//                bootView.findViewById(textIds[i]).setEnabled(true);
//                bootView.findViewById(iconIds[i]).setSelected(true);
//            } else {
//                bootView.findViewById(textIds[i]).setEnabled(false);
//                bootView.findViewById(iconIds[i]).setSelected(false);
//                if (index < i) {
//                    bootView.findViewById(iconIds[i]).setEnabled(false);
//                } else {
//                    bootView.findViewById(iconIds[i]).setEnabled(true);
//                }
//            }
//        }
    }

}
