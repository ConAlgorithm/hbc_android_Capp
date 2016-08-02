package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.CheckVersionBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCheckVersion;
import com.hugboga.custom.utils.PushUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UpdateResources;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by qingcha on 16/5/18.
 */
@ContentView(R.layout.fg_about)
public class FgAbout extends BaseFragment {

    @ViewInject(R.id.about_version_tv)
    TextView versionTV;

    @Override
    protected void initHeader() {
        fgTitle.setText(getActivity().getString(R.string.about_title));
        versionTV.setText(getActivity().getString(R.string.about_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCheckVersion) {
            RequestCheckVersion requestCV = (RequestCheckVersion) request;
            final CheckVersionBean checkVersionBean = requestCV.getData();
            if (TextUtils.isEmpty(checkVersionBean.url)) {
                DialogUtil.getInstance(getActivity()).showCustomDialog(getActivity().getString(R.string.about_newest));
            }
            UserEntity.getUser().setIsNewVersion(getActivity(), !TextUtils.isEmpty(checkVersionBean.url));
            DialogUtil.getInstance(getActivity()).showUpdateDialog(checkVersionBean.hasAppUpdate,checkVersionBean.force, checkVersionBean.content, checkVersionBean.url, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PushUtils.startDownloadApk(getActivity(), checkVersionBean.url);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
            UpdateResources.checkRemoteResources(getActivity(), checkVersionBean, null);
            UpdateResources.checkRemoteDB(getActivity(), checkVersionBean.dbDownloadLink, checkVersionBean.dbVersion, null);
        }
    }

    @Event({R.id.about_update_layout, R.id.about_grade_layout, R.id.about_story_layout})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.about_update_layout://软件更新
                int resourcesVersion = new SharedPre(getActivity()).getIntValue(SharedPre.RESOURCES_H5_VERSION);
                final RequestCheckVersion requestCheckVersion = new RequestCheckVersion(getActivity(), resourcesVersion);
                requestData(requestCheckVersion);
                break;
            case R.id.about_grade_layout://给我评价
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e){
                    showTip(getActivity().getString(R.string.about_no_appstore));
                }
                break;
            case R.id.about_story_layout://关于我们

//                Bundle bundle = new Bundle();
//                bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_ABOUT);
//                startFragment(new FgWebInfo(), bundle);

                Intent intent = new Intent(context, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_ABOUT);
                context.startActivity(intent);

                break;
            default:
                break;
        }
    }

}
