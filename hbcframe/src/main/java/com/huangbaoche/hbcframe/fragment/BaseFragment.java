package com.huangbaoche.hbcframe.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.huangbaoche.hbcframe.activity.BaseFragmentActivity;
import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.FastClickUtils;
import com.huangbaoche.hbcframe.util.MLog;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;


public abstract class BaseFragment extends Fragment implements HttpRequestListener, View.OnTouchListener {
    public static String KEY_FRAGMENT_NAME = "key_fragment_name";
    public static String KEY_FROM = "key_from";

    public boolean needHttpRequest = true;
    public Callback.Cancelable cancelable;
    protected int contentId = -1;

    private boolean injected = false;
    private boolean initView = false;
    private ErrorHandler errorHandler;
    protected BaseFragment mSourceFragment;//fragment来源，从哪个跳转来的
    private ArrayList<EditText> editTextArray = new ArrayList<EditText>();

    private View contentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        if (contentView == null)
            contentView = x.view().inject(this, inflater, container);
        MLog.i(this + "onCreateView " + contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MLog.i(this + "onViewCreated");
        if (!injected) {
            x.view().inject(this, this.getView());
        }
        if (!initView) {
            initView = true;
            getView().setOnTouchListener(this);
            initHeader(savedInstanceState);
            initHeader();
            initView();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        MLog.i(this + " onStart");
        if (needHttpRequest) {
            needHttpRequest = false;
            cancelable = requestData();
        } else {
            inflateContent();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MLog.i(this + " onResume");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        MLog.i(this + " onViewStateRestored");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MLog.i(this + " onSaveInstanceState");
    }

    @Override
    public void onPause() {
        super.onPause();
        MLog.i(this + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        MLog.i(this + " onStop");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (contentView != null && contentView.getParent() != null) {
            MLog.i(this + " onDestroyView");
            ((ViewGroup) contentView.getParent()).removeView(contentView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MLog.i(this + " onDestroy");
        if (cancelable != null) {
            cancelable.cancel();
        }
    }


    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        needHttpRequest = true;
        if (errorHandler == null) {
            errorHandler = new ErrorHandler(getActivity(), this);
        }

//        errorInfo.exception.
        errorHandler.onDataRequestError(errorInfo, request);
        errorHandler = null;
//        result={"message":"已为用户 111234 购买订单号 R190349171522 的保险","status":80007}
//        code: 80007, msg: 已为用户 111234 购买订单号 R190349171522 的保险, result: null
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {

    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {
        needHttpRequest = true;
    }

    /**
     * fragment回调使用；<br/>
     * Fragment A 启用 Fragment B<br/>
     * B 向 A 回传 数据可以通过 B的finishForResult(bundle);<br/>
     * A 在 onFragmentResult(Bundle bundle) 可以收到B 回传的数据<br/>
     *
     * @param bundle 参数
     */
    public void onFragmentResult(Bundle bundle) {
        MLog.e(this + " onFragmentResult");
    }


    /**
     * 初始化Header
     */
    protected abstract void initHeader();

    /**
     * 初始化Header
     */
    protected void initHeader(Bundle savedInstanceState) {}

    /**
     * 初始化界面
     */
    protected abstract void initView();

    /**
     * 请求方法 会在加载时调用
     */
    protected abstract Callback.Cancelable requestData();

    /**
     * 填充内容，在执行onRestart的时候调用
     */
    protected abstract void inflateContent();

    /**
     * 请求方法 会在加载完成是调用
     */
    protected Callback.Cancelable requestData(BaseRequest request) {
        cancelable = HttpRequestUtils.request(getActivity(), request, this);
        return cancelable;
    }

    /**
     * 设置 错误处理
     *
     * @param errorHandler
     */
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /**
     * 启动新的fragment
     *
     * @param fragment
     */
    public void startFragment(BaseFragment fragment) {
        Bundle bundle = fragment.getArguments();
        bundle = bundle == null ? new Bundle() : bundle;
        startFragment(fragment, bundle);
    }

    public void startFragment(BaseFragment fragment, Bundle bundle) {
        if (FastClickUtils.isFastClick()) {
            return;
        }
        if (fragment == null) return;
        if (getContentId() == -1)
            throw new RuntimeException("BaseFragment ContentId not null, BaseFragment.setContentId(int)");
        if(getActivity()!=null)
        ((BaseFragmentActivity) getActivity()).addFragment(fragment);
        collapseSoftInputMethod();
        editTextClearFocus();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        try {
            fragment.setSourceFragment(this);
        }catch (Exception e) {
            //java.lang.NullPointerException: Attempt to invoke virtual method 
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(contentId, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();

    }

    /**
     * 将fragment从后台堆栈中弹出,  (模拟用户按下BACK 命令).
     */
    public void finish() {
        collapseSoftInputMethod();
        //将fragment从后台堆栈中弹出,  (模拟用户按下BACK 命令).
        if (getFragmentManager() == null) return;
        getFragmentManager().popBackStack();
        Fragment fragment = this.geSourceFragment();
        if (fragment != null && fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onResume();
        }
        ((BaseFragmentActivity) getActivity()).removeFragment(this);
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
        if (getArguments() != null) mBundle.putAll(getArguments());
        if (bundle != null) mBundle.putAll(bundle);
        mBundle.putString(KEY_FRAGMENT_NAME, this.getClass().getSimpleName());
        BaseFragment fragment = this.geSourceFragment();
        if (fragment != null ) {
            fragment.onFragmentResult(mBundle);
        }
        MLog.i(this + " finishForResult " + fragment);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 启动页面，如果有，从栈提到最上面，上面的都关闭；如果没有则新建
     */
    public void bringToFront(Class fragment, Bundle bundle) {
        collapseSoftInputMethod();
        if (getActivity() instanceof BaseFragmentActivity) {

            ArrayList<BaseFragment> fragmentList = ((BaseFragmentActivity) getActivity()).getFragmentList();
            BaseFragment targetFg = null;
            for (Fragment fg : fragmentList) {
                if (fragment.isInstance(fg)) {
                    targetFg = (BaseFragment) fg;
                    break;
                }
            }
            if (targetFg != null) {
                for (int i = fragmentList.size() - 1; i >= 0; i--) {
                    BaseFragment fg = (BaseFragment) fragmentList.get(i);
                    if (fg != targetFg) {
                        if (fg != null)
                            fg.finish();
                    } else
                        break;
                }
                targetFg.onFragmentResult(bundle);
            } else {
                try {
                    BaseFragment fg = (BaseFragment) fragment.newInstance();
                    startFragment(fg, bundle);
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void clearFragmentList() {
        if (getActivity() instanceof BaseFragmentActivity) {
            ArrayList<BaseFragment> fragmentList = ((BaseFragmentActivity) getActivity()).getFragmentList();
            for (int i = fragmentList.size() - 1; i >= 1; i--) {
                BaseFragment fg = (BaseFragment) fragmentList.get(i);
                if (fg != null) {
                    String simpleName = fg.getClass().getSimpleName();
                    if ("FgHome".equals(simpleName) || "FgChat".equals(simpleName) || "FgTravel".equals(simpleName)) {
                        return;
                    }
                    fg.finish();
                }
            }
        }
        DefaultSSLSocketFactory.resetSSLSocketFactory(getActivity());
    }

    public void clearFragment() {
        if (getActivity() instanceof BaseFragmentActivity) {
            ArrayList<BaseFragment> fragmentList = ((BaseFragmentActivity) getActivity()).getFragmentList();
            for (int i = fragmentList.size() - 1; i >= 1; i--) {
                BaseFragment fg = (BaseFragment) fragmentList.get(i);
                if (fg != null) {
                    String simpleName = fg.getClass().getSimpleName();
                    if ("FgHome".equals(simpleName) || "FgChat".equals(simpleName) || "FgTravel".equals(simpleName)) {
                        return;
                    }
                    ((BaseFragmentActivity)getActivity()).removeFragment(fg);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.remove(fg);
                    transaction.commitAllowingStateLoss();
                }
            }
        }
        DefaultSSLSocketFactory.resetSSLSocketFactory(getActivity());
    }

    /**
     * 点击 back键的时候触发该方法，如果返回false，返回事件交给上层处理。如果返回true，则表示自己处理事件
     *
     * @return
     */
    public boolean onBackPressed() {
        MLog.e(this + "onBackPressed");
        if (cancelable != null)
            cancelable.cancel();
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    /**
     * 展示软键盘
     */
    public void showSoftInputMethod(EditText inputText) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 收起软键盘
     */
    public void collapseSoftInputMethod() {
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && getView() != null)
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    /**
     * 取消光标
     */
    public void editTextClearFocus() {
        if (editTextArray != null && editTextArray.size() > 0) {
            for (EditText editText : editTextArray) {
                editText.clearFocus();
            }
        }
    }

    public BaseFragment geSourceFragment() {
        return mSourceFragment;
    }

    public void setSourceFragment(BaseFragment mTargetFragment) {
        this.mSourceFragment = mTargetFragment;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    /**
     * 通知 栈里的 fragment 更新数据 会调用 onFragmentResult
     *
     * @param fragment
     * @param bundle
     */
    public void notifyFragment(Class fragment, Bundle bundle) {
        ArrayList<BaseFragment> fragmentList = ((BaseFragmentActivity) getActivity()).getFragmentList();
        for (int i = fragmentList.size() - 1; i >= 0; i--) {
            BaseFragment fg = (BaseFragment) fragmentList.get(i);
            if (fragment.isInstance(fg)) {
                if (bundle != null) {
                    bundle.putString(KEY_FROM, this.getClass().getSimpleName());
                }
                fg.onFragmentResult(bundle);
                break;
            }
        }
    }

    /**
     * 获取点击事件ID
     */
    public String getEventId(){
        return "";
    }

    /**
     * 获取来源
     */
    public String getEventSource(){
        return "";
    }

    /**
     * 获取来源map
     */
    public Map getEventMap(){
        ArrayMap map = new ArrayMap();
        map.put("source", getEventSource());
        return map;
    }
}
