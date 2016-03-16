package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.ResourcesConstants;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.ExceptionInfo;
import com.hugboga.custom.data.net.HttpRequestUtils;
import com.hugboga.custom.data.parser.InterfaceParser;
import com.hugboga.custom.data.parser.ParserCheckVersion;
import com.hugboga.custom.data.parser.ParserLogout;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.PushUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UpdateResources;
import com.hugboga.custom.widget.DialogUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class FgSetting extends BaseFragment {

    @ViewInject(R.id.setting_menu_version_content)
    TextView versionFlagTextView;
    @ViewInject(R.id.setting_menu_version_content_flag)
    TextView newVersionTextView;
    @ViewInject(R.id.setting_menu_mobile)
    TextView mobileTextView;

    AlertDialog versionDialog; //版本更新弹窗

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_setting, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    protected String fragmentTitle() {
        //设置标题颜色，返回按钮图片
        leftBtn.setImageResource(R.mipmap.top_back_black);
        titleText.setTextColor(getResources().getColor(R.color.my_content_title_color));
        versionFlagTextView.setText("v" + PhoneInfo.getSoftwareVersion(getActivity()));
        if(UserEntity.getUser().getIsNewVersion(getActivity())){
            newVersionTextView.setText("升级新版");
            newVersionTextView.setTextColor(Color.RED);
        }
        mobileTextView.setText(UserEntity.getUser().getPhone(getActivity()));
        return "设置";
    }

    @Override
    protected void requestDate() {
    }


    @Override
    public void onDataRequestSucceed(InterfaceParser parser) {
        if (parser instanceof ParserCheckVersion) {
            final ParserCheckVersion mParser = (ParserCheckVersion) parser;
            if(!TextUtils.isEmpty(mParser.url)){
                newVersionTextView.setText("升级新版");
                newVersionTextView.setTextColor(Color.RED);
            }
            if(TextUtils.isEmpty(mParser.url)){
                DialogUtil.getInstance(getActivity()).showCustomDialog("已是最新版本");
            }
            UserEntity.getUser().setIsNewVersion(getActivity(), !TextUtils.isEmpty(mParser.url));
            DialogUtil.getInstance(getActivity()).showUpdateDialog(mParser.force, mParser.content, mParser.url, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PushUtils.startDownloadApk(getActivity(), mParser.url);
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
            UpdateResources.checkRemoteResources(getActivity(), mParser, null);
            UpdateResources.checkRemoteDB(getActivity(), mParser.dbDownloadLink, mParser.dbVersion, null);
        }else if(parser instanceof  ParserLogout){
            getActivity().sendBroadcast(new Intent(FgHome.FILTER_FLUSH));
            UserEntity.getUser().clean(getActivity());
            finish();
            finish();
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, InterfaceParser parser) {
        if(parser instanceof  ParserLogout){
            getActivity().sendBroadcast(new Intent(FgHome.FILTER_FLUSH));
            UserEntity.getUser().clean(getActivity());
            finish();
            finish();
        }else {
            super.onDataRequestError(errorInfo, parser);
        }
    }

    @Override
    protected void inflateContent() {
    }


    @Override
    @OnClick({R.id.setting_menu_layout1, R.id.setting_menu_layout2, R.id.setting_menu_layout3, R.id.setting_menu_layout4, R.id.setting_menu_layout5, R.id.setting_exit, R.id.setting_menu_layout6})
    protected void onClickView(View view) {
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
                mHttpUtils = new HttpRequestUtils(getActivity(),new ParserCheckVersion(version,resourcesVersion),this);
                mHttpUtils.execute();
                break;
            case R.id.setting_menu_layout5:
                //关于我们
                Bundle bundle = new Bundle();
                bundle.putString(FgWebInfo.Web_URL, ResourcesConstants.H5_ABOUT);
                startFragment( new FgWebInfo(),bundle);
                break;
            case R.id.setting_exit:
                //退出登录
                new AlertDialog.Builder(getActivity()).setTitle("退出登录").setMessage("退出后不会删除任何历史数据，下次登录依然可以使用本账号").setNegativeButton("取消",null).setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParserLogout parser = new ParserLogout();
                        mHttpUtils = new HttpRequestUtils(getActivity(),parser,FgSetting.this);
                        mHttpUtils.execute();
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

}
