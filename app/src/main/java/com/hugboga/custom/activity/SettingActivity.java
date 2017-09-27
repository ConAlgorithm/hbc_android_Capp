package com.hugboga.custom.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestLogout;
import com.hugboga.custom.data.request.RequestUserInfo;
import com.hugboga.custom.developer.DeveloperOptionsActivity;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.widget.DialogUtil;
import com.qiyukf.unicorn.api.Unicorn;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created on 16/8/6.
 */

public class SettingActivity extends BaseActivity {
    @Bind(R.id.setting_menu_clear_cache_flag)
    TextView cacheSizeTextView;
    @Bind(R.id.setting_menu_version_content_flag)
    TextView newVersionTextView;
    SharedPre sharedPre;
    Long cacheSize;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.setting_menu_layout2)
    RelativeLayout settingMenuLayout2;
    @Bind(R.id.setting_clear_cache_arrow)
    ImageView settingClearCacheArrow;
    @Bind(R.id.setting_menu_layout7)
    RelativeLayout settingMenuLayout7;
    @Bind(R.id.setting_menu_layout3)
    RelativeLayout settingMenuLayout3;
    @Bind(R.id.setting_about_arrow)
    ImageView settingAboutArrow;
    @Bind(R.id.setting_menu_layout5)
    RelativeLayout settingMenuLayout5;
    @Bind(R.id.setting_exit)
    RelativeLayout settingExit;

    @Bind(R.id.setting_menu_developer_layout)
    RelativeLayout developerLayout;

    @Bind(R.id.red_point1)
    ImageView redPoint;
    @Bind(R.id.setOrChangPwd)
    TextView setOrChangPwd;

    boolean needInitPwd;
    boolean isMobileBinded = true;
    public static int REQUEST_CODE = 0x100;
    public  static int RESULT_OK = 0x001;
    @Override
    public int getContentViewId() {
        return R.layout.fg_setting;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initHeader();
        needInitPwd = getIntent().getBooleanExtra("needInitPwd",false);
        isMobileBinded = getIntent().getBooleanExtra("isMobileBinded",true);
        setState(needInitPwd);
        if(!UserEntity.getUser().isLogin(this)){
            settingMenuLayout2.setVisibility(View.GONE);
            settingExit.setVisibility(View.GONE);
        }
        if(!isMobileBinded){
            settingMenuLayout2.setVisibility(View.GONE);
        }
        if (HbcConfig.IS_DEBUG) {
            developerLayout.setVisibility(View.VISIBLE);
        } else {
            developerLayout.setVisibility(View.GONE);
        }
    }
    private void setState(boolean needInitPwd){
        if(needInitPwd){
            setOrChangPwd.setText(getString(R.string.setting_set_pwd));
            redPoint.setVisibility(View.VISIBLE);
        }else{
            setOrChangPwd.setText(getString(R.string.setting_chang_pwd));
            redPoint.setVisibility(View.GONE);
        }

        UserEntity.getUser().setNeedInitPwd(this,needInitPwd);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private DialogUtil mDialogUtil;
    Intent intent;
    @OnClick({R.id.setting_menu_layout2, R.id.setting_menu_layout3, R.id.setting_menu_layout5, R.id.setting_exit, R.id.setting_menu_layout7, R.id.setting_menu_developer_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_menu_layout2:
//                startFragment(new FgChangePsw());
                if(UserEntity.getUser().getNeedInitPwd(this)){
                    intent = new Intent(activity,SetPswActivity.class);
                    intent.putExtra("isFromSetting",true);
                    startActivityForResult(intent,REQUEST_CODE);
                }else{
                    intent = new Intent(activity,ChangePswActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.setting_menu_layout3:
                //意见反馈
//                startFragment(new FgCallBack());
                intent = new Intent(activity,CallBackActivity.class);
                startActivity(intent);

                break;
            case R.id.setting_menu_layout5:
                //关于我们
//                startFragment(new FgAbout());
                intent = new Intent(activity,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_exit:
                //退出登录
                AlertDialogUtils.showAlertDialog(activity, getString(R.string.setting_logout), getString(R.string.setting_logout_hint)
                        , getString(R.string.dialog_btn_sure), getString(R.string.dialog_btn_cancel)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mDialogUtil = DialogUtil.getInstance(activity);
                                mDialogUtil.showLoadingDialog();
                                RequestLogout requestLogout = new RequestLogout(activity);
                                HttpRequestUtils.request(activity, requestLogout, new HttpRequestListener() {
                                    @Override
                                    public void onDataRequestSucceed(BaseRequest request) {
                                        ApiReportHelper.getInstance().addReport(request);
                                        mDialogUtil.dismissLoadingDialog();
                                        UserEntity.getUser().clean(activity);
                                        IMUtil.getInstance().logoutNim();
                                        EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOOUT));
                                        finish();
                                    }

                                    @Override
                                    public void onDataRequestCancel(BaseRequest request) {

                                    }

                                    @Override
                                    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                                        mDialogUtil.dismissLoadingDialog();
                                        UserEntity.getUser().clean(activity);
                                        EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOOUT));
                                        Unicorn.setUserInfo(null);
                                        IMUtil.getInstance().logoutNim();
                                        finish();
                                    }
                                },false);
                            }
                        }, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                break;
            case R.id.setting_menu_layout7:
                AlertDialogUtils.showAlertDialog(activity, getString(R.string.setting_clear_cache), getString(R.string.setting_clear_cache_hint, getCacheSize())
                        , getString(R.string.dialog_btn_sure), getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Tools.deleteCache();
                        cacheSize = 0L;
                        cacheSizeTextView.setText(getCacheSize());
                        sharedPre.saveLongValue(SharedPre.CACHE_SIZE, 0);
                    }
                }, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.setting_menu_developer_layout:
                intent = new Intent(activity, DeveloperOptionsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 1:
                if(requestCode == REQUEST_CODE){
                    boolean needInitPwd = data.getBooleanExtra("needInitPwd",false);
                    setState(needInitPwd);
                }
                break;
            default:
                break;
        }
    }
    protected void initHeader() {
        newVersionTextView.setText("v" + BuildConfig.VERSION_NAME);
        headerTitle.setText(getString(R.string.setting_title));
        sharedPre = new SharedPre(activity);
        cacheSize = sharedPre.getLongValue(SharedPre.CACHE_SIZE);
        cacheSizeTextView.setText(getCacheSize());
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventAction(EventType.SETTING_BACK));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new EventAction(EventType.SETTING_BACK));
        finish();
    }

    private String getCacheSize() {
        String result = "";
        if (cacheSize == null) {
            return result;
        }
        long oneKB = 1024;
        long oneMB = 1024 * 1024;
        long oneGB = 1024 * 1024 * 1024;
        if (cacheSize == 0) {
            return "0K";
        } else if (cacheSize > 0 && cacheSize < oneKB) {
            return "1K";
        } else if (cacheSize > oneKB && cacheSize < oneMB) {
            long num = cacheSize / oneKB;
            return num + "K";
        } else if (cacheSize > oneMB && cacheSize < oneGB) {
            long num = cacheSize / oneMB;
            return num + "M";
        } else if (cacheSize > oneGB) {
            long num = cacheSize / oneGB;
            return num + "G";
        }
        return result;
    }

}

