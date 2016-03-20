package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.constants.ResourcesConstants;
import com.hugboga.custom.data.bean.CheckVersionBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.parser.ParserLogout;
import com.hugboga.custom.data.request.RequestCheckVersion;
import com.hugboga.custom.data.request.RequestLogout;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UpdateResources;
import com.hugboga.custom.widget.DialogUtil;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import de.greenrobot.event.EventBus;

@ContentView(R.layout.fg_setting)
public class FgSetting extends BaseFragment {

    @ViewInject(R.id.setting_menu_version_content)
    TextView versionFlagTextView;
    @ViewInject(R.id.setting_menu_version_content_flag)
    TextView newVersionTextView;
    @ViewInject(R.id.setting_menu_mobile)
    TextView mobileTextView;
    AlertDialog versionDialog; //版本更新弹窗
    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCheckVersion) {
            RequestCheckVersion requestCV = (RequestCheckVersion) request;
            final CheckVersionBean checkVersionBean = requestCV.getData();
            if(!TextUtils.isEmpty(checkVersionBean.url)){
                newVersionTextView.setText("升级新版");
                newVersionTextView.setTextColor(Color.RED);
            }
            if(TextUtils.isEmpty(checkVersionBean.url)){
                DialogUtil.getInstance(getActivity()).showCustomDialog("已是最新版本");
            }
            UserEntity.getUser().setIsNewVersion(getActivity(), !TextUtils.isEmpty(checkVersionBean.url));
            DialogUtil.getInstance(getActivity()).showUpdateDialog(checkVersionBean.force, checkVersionBean.content, checkVersionBean.url, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    PushUtils.startDownloadApk(getActivity(), checkVersionBean.url);
                    if(dialog!=null)
                    dialog.dismiss();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(dialog!=null)
                    dialog.dismiss();
                }
            });
            UpdateResources.checkRemoteResources(getActivity(), checkVersionBean, null);
            UpdateResources.checkRemoteDB(getActivity(), checkVersionBean.dbDownloadLink, checkVersionBean.dbVersion, null);
        }else if(request instanceof RequestLogout){
            getActivity().sendBroadcast(new Intent(FgHome.FILTER_FLUSH));
            UserEntity.getUser().clean(getActivity());
            EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOOUT));
            finish();
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if(request instanceof  RequestLogout){
            getActivity().sendBroadcast(new Intent(FgHome.FILTER_FLUSH));
            UserEntity.getUser().clean(getActivity());
            EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOOUT));
            finish();
        }else {
            super.onDataRequestError(errorInfo, request);
        }
    }

    @Override
    protected void inflateContent() {
    }


    @Event({R.id.setting_menu_layout1, R.id.setting_menu_layout2, R.id.setting_menu_layout3, R.id.setting_menu_layout4, R.id.setting_menu_layout5, R.id.setting_exit, R.id.setting_menu_layout6})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.setting_menu_layout1:
                //更换手机号
                startFragment(new FgChangeMobile());
                break;
            case R.id.setting_menu_layout2:
                //修改密码
                startFragment(new FgChangePsw());
                break;
            case R.id.setting_menu_layout3:
                //意见反馈
                startFragment(new FgCallBack());
                break;
            case R.id.setting_menu_layout4:
                //软件更新
                String version = PhoneInfo.getSoftwareVersion(getActivity());
                int resourcesVersion = new SharedPre(getActivity()).getIntValue(SharedPre.RESOURCES_H5_VERSION);
                final RequestCheckVersion requestCheckVersion = new RequestCheckVersion(getActivity(),version,resourcesVersion);
                requestData(requestCheckVersion);
                break;
            case R.id.setting_menu_layout5:
                //关于我们
                Bundle bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, ResourcesConstants.H5_ABOUT);
                startFragment( new FgWebInfo(),bundle);
                break;
            case R.id.setting_exit:
                //退出登录
                new AlertDialog.Builder(getActivity()).setTitle("退出登录").setMessage("退出后不会删除任何历史数据，下次登录依然可以使用本账号").setNegativeButton("取消",null).setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParserLogout parser = new ParserLogout();
                        RequestLogout requestLogout = new RequestLogout(getActivity());
                        requestData(requestLogout);
                    }
                }).show();
                break;
            case R.id.setting_menu_layout6:
                //给个评价
                try{
                    Uri uri = Uri.parse("market://details?id="+getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch (Exception e){
                    showTip("设备上没有安装应用市场");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        mobileTextView.setText(UserEntity.getUser().getPhone(getActivity()));
    }

    @Override
    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        fgTitle.setTextColor(getResources().getColor(R.color.my_content_title_color));
        fgTitle.setText("设置");
        versionFlagTextView.setText("v" + PhoneInfo.getSoftwareVersion(getActivity()));
        if(UserEntity.getUser().getIsNewVersion(getActivity())){
            newVersionTextView.setText("升级新版");
            newVersionTextView.setTextColor(Color.RED);
        }
        mobileTextView.setText(UserEntity.getUser().getPhone(getActivity()));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

}
