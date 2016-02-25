package com.huangbaoche.hbcframe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.huangbaoche.hbcframe.fragment.BaseFragment;

import org.xutils.common.util.LogUtil;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;


/**
 * fragmentActivity 基类
 */
public class BaseFragmentActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<BaseFragment> mFragmentList = new ArrayList<>();

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
                    /*if(!fragment.onBackPressed()){//子类不处理
                        fragment.finish();
                    }*/
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
     * @date 2012-6-20 上午10:32:00
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * @return void    返回类型
     * @Title exitApp
     * @Description 退出程序
     * @author aceway-liwei
     * @date 2012-6-20 上午10:31:00
     */
    public void exitApp() {
        finish();
        System.exit(0);
    }

    @Override
    public void onClick(View v) {

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
}
