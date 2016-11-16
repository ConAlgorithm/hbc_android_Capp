package com.hugboga.custom.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.ServiceQuestionActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.qiyukf.unicorn.activity.ServiceMessageFragment;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.OnMessageItemClickListener;
import com.qiyukf.unicorn.api.ProductDetail;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnicornImageLoader;
import com.qiyukf.unicorn.api.YSFOptions;
import com.qiyukf.unicorn.api.YSFUserInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by on 16/8/26.
 */

public class UnicornUtils {

    private static final String APPKEY = "d1838897aaf0debe1da1f0443c6942ff";
    private static final long GROUP_ID = 128411;
    private static final String CUSTOMER_AVATAR = "http://fr.huangbaoche.com/im/avatar/default/k_head.jpg";

    public static void initUnicorn() {
        Unicorn.init(MyApplication.getAppContext(), APPKEY, getDefaultOptions(), new UnicornImageLoaderRealize());
    }

    public static void openDefaultServiceActivity(Context context) {
        if (!CommonUtils.isLogin(context)) {
            return;
        }
        UnicornServiceActivity.Params params = new UnicornServiceActivity.Params();
        params.sourceType = UnicornServiceActivity.SourceType.TYPE_DEFAULT;
        Intent intent = new Intent(context, ServiceQuestionActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        context.startActivity(intent);
    }

    public static void addServiceFragment(BaseActivity activity, int containerId, ProductDetail productDetail, int staffId) {
        Unicorn.setUserInfo(null);
        SharedPre.setInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT, 0);

        Context context = MyApplication.getAppContext();
        YSFUserInfo userInfo = new YSFUserInfo();
        userInfo.userId = UserEntity.getUser().getUserId(context);
        userInfo.data = getServiceUserInfo();
        Unicorn.setUserInfo(userInfo);

        UICustomization uiCustomization = new UICustomization();
        uiCustomization.leftAvatar = CUSTOMER_AVATAR;
        uiCustomization.rightAvatar = UserEntity.getUser().getAvatar(context);
        uiCustomization.titleBackgroundColor = 0xFF2D2B24;
        uiCustomization.titleBarStyle = 1;
        YSFOptions options = getDefaultOptions();
        options.uiCustomization = uiCustomization;
        Unicorn.updateOptions(options);

        Unicorn.toggleNotification(false);

        // 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入三个参数分别为来源页面的url，来源页面标题，来源页面额外信息（可自由定义）
        // 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
        ConsultSource source = new ConsultSource("", "CAPP_Android", "");
        if (staffId > 0) {
            source.staffId = 51071;//TODO staffId
        }
        if (productDetail != null) {
            source.productDetail = productDetail;
        }
        ServiceMessageFragment fragment = new ServiceMessageFragment();
        fragment.setArguments("皇包车客服", source, new FrameLayout(activity));
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(containerId, fragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {

        }
    }

    private static String getServiceUserInfo() {
        Context context = MyApplication.getAppContext();
        UserEntity userEntity = UserEntity.getUser();
        ArrayList<ServiceUserInfo> list = new ArrayList<ServiceUserInfo>();
        list.add(new ServiceUserInfo("real_name", userEntity.getNickname(context)));
        list.add(new ServiceUserInfo("mobile_phone", userEntity.getPhone(context)));
        list.add(new ServiceUserInfo("user_name", "真实姓名", userEntity.getUserName(context)));
        list.add(new ServiceUserInfo("areaCode", "区域号码", userEntity.getAreaCode(context)));
        list.add(new ServiceUserInfo("userId", "用户ID", userEntity.getUserId(context)));
        list.add(new ServiceUserInfo("version", "应用版本", userEntity.getVersion(context)));
        return JsonUtils.toJson(list);
    }

    private static class ServiceUserInfo implements Serializable {

        public String key;
        public String label;
        public String value;

        public ServiceUserInfo(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public ServiceUserInfo(String key, String label, String value) {
            this.key = key;
            this.label = label;
            this.value = value;
        }
    }

    private static YSFOptions getDefaultOptions() {
        YSFOptions options = new YSFOptions();
        options.savePowerConfig = new SavePowerConfig();
        OnMessageItemClickListener messageItemClickListener = new OnMessageItemClickListener() {
            // 响应 url 点击事件
            public void onURLClicked(Context context, String url) {
                Intent intent = new Intent(context, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, url);
                context.startActivity(intent);
            }
        };
        options.onMessageItemClickListener = messageItemClickListener;
        return options;
    }

    private static class UnicornImageLoaderRealize implements UnicornImageLoader {

        @Nullable
        @Override
        public Bitmap loadImageSync(String uri, int width, int height) {
            return null;
        }

        @Override
        public void loadImage(String uri, int width, int height, ImageLoaderListener listener) {
            if (width > 0 && height > 0) {
                Glide.with(MyApplication.getAppContext())
                        .load(uri)
                        .asBitmap()
                        .centerCrop()
                        .override(width, height)
                        .into(new LoggingListener(listener));
            } else {
                Glide.with(MyApplication.getAppContext())
                        .load(uri)
                        .asBitmap()
                        .centerCrop()
                        .into(new LoggingListener(listener));
            }
        }

        public class LoggingListener extends SimpleTarget<Bitmap> {

            private ImageLoaderListener listener;

            public LoggingListener(ImageLoaderListener listener) {
                this.listener = listener;
            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (listener != null) {
                    listener.onLoadComplete(resource);
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                if (listener != null) {
                    listener.onLoadFailed(e.getCause());
                }
            }
        }
    }

}
