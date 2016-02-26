package com.huangbaoche.hbcframe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.huangbaoche.hbcframe.fragment.BaseFragment;

import org.xutils.common.util.LogUtil;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;


/**
 * fragmentActivity 基类
 */
public class BaseFragmentActivity extends AppCompatActivity  {

    private ArrayList<BaseFragment> mFragmentList = new ArrayList<>();
    private int contentId;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
//		addErrorProcess();
    }
    @Override
    public void onBackPressed() {
        if(mFragmentList!=null&&mFragmentList.size()>0){
            for(int i=mFragmentList.size()-1;i>0;i--) {
                BaseFragment fragment = (BaseFragment) mFragmentList.get(i);
                if (fragment != null) {//取最后非空
                    LogUtil.i("fragment = " + fragment);
                    if(!fragment.onBackPressed()){//子类不处理
                        fragment.finish();
                    }
                    return;
                }
            }
        }
        super.onBackPressed();
    }
    /**
     * @return void    返回类型
     * @Title addErrorProcess
     * @Description 添加未捕获异常处理，将错误提交服务器，并退出程序，
     */
    private void addErrorProcess() {
        Thread.currentThread().setUncaughtExceptionHandler(mUncaughtExceptionHandlernew);
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandlernew);
    }

    UncaughtExceptionHandler mUncaughtExceptionHandlernew = new UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.printStackTrace();
            exitApp();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.i("onStart" + this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("onResume" + this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i("onPause" + this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i("onStop" + this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i("onDestroy" + this);
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
            LogUtil.e("startFragment fragment is null");
            return;
        }
        if(bundle!=null)fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(contentId, fragment).addToBackStack(null).commit();
    }
}
