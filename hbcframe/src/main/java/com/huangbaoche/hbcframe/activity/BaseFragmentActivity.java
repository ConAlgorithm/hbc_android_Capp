package com.huangbaoche.hbcframe.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.huangbaoche.hbcframe.fragment.BaseFragment;
import com.huangbaoche.hbcframe.util.MLog;
import com.umeng.analytics.MobclickAgent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * fragmentActivity 基类
 */
public abstract class BaseFragmentActivity extends AppCompatActivity  {

    private ArrayList<BaseFragment> mFragmentList = new ArrayList<>();
    protected int contentId = -1;
    public abstract int getContentViewId();
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
//		addErrorProcess();
    }

    public boolean isSoftInputShow(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            if (!this.getFragmentList().get(this.getFragmentsSize() - 1).getClass().getSimpleName().equalsIgnoreCase("FgIMChat")) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isSoftInputShow()) {
                        try {
                            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(BaseFragmentActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }catch (Exception e){
        }
        try {
            return super.dispatchTouchEvent(event);
        }catch (Exception e){
            return false;
        }
    }

    //获取fragment个数
    protected  int getFragmentsSize(){
        return mFragmentList.size();
    }

    //是否需要关闭fragment
    protected void doFragmentBack(){
        if(mFragmentList!=null&&mFragmentList.size()>0){
            for(int i=mFragmentList.size()-1;i>0;i--) {
                BaseFragment fragment = (BaseFragment) mFragmentList.get(i);
                if (fragment != null) {//取最后非空
                    MLog.i("fragment = " + fragment);
                    if(!fragment.onBackPressed()){//子类不处理
                        fragment.finish();
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        doFragmentBack();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * @return void    返回类型
     * @Title addErrorProcess
     * @Description 添加未捕获异常处理，将错误提交服务器，并退出程序，
     */
    protected void addErrorProcess() {
        Thread.currentThread().setUncaughtExceptionHandler(mUncaughtExceptionHandlernew);
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandlernew);
    }

    UncaughtExceptionHandler mUncaughtExceptionHandlernew = new UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
           MLog.e("崩溃退出",ex);
            exitApp();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        MLog.i("onStart" + this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.i("onResume" + this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.i("onPause" + this);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MLog.i("onStop" + this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        MLog.i("onDestroy" + this);
    }

    /**
     * @return void    返回类型
     * @Title exitApp
     * @Description 退出程序
     */
    public void exitApp() {
        finish();
        System.exit(0);
    }


    public synchronized void addFragment(BaseFragment fragment){
        mFragmentList.add(fragment);
    }

    public synchronized void removeFragment(BaseFragment fragment){
        mFragmentList.remove(fragment);
    }

    public ArrayList<BaseFragment> getFragmentList(){
        return mFragmentList;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public void startFragment(BaseFragment fragment){
        startFragment(fragment,null);
    }

    public void startFragment(BaseFragment fragment,Bundle bundle){
        if(fragment ==null){
            MLog.e("startFragment fragment is null");
            return;
        }
        BaseFragment tFragment = null;
        if(mFragmentList !=null&&mFragmentList.size()>0){
            mFragmentList.get(mFragmentList.size() - 1);
            for(int i=mFragmentList.size()-1;i>=0;i--){
                tFragment = mFragmentList.get(i);
                if(tFragment !=null&&tFragment.getContentId()!=-1)break;
                tFragment =null;
            }
        }
        if(tFragment!=null) {
            tFragment.startFragment(fragment, bundle);
        }else{
            addFragment(fragment);
            if(bundle!=null){
                fragment.setArguments(bundle);
            }
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.add(contentId, fragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }

    }

}
