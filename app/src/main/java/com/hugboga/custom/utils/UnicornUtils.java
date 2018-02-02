package com.hugboga.custom.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.tools.HLog;
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
import com.qiyukf.unicorn.ui.fragment.ServiceMessageFragment;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by on 16/8/26.
 */

public class UnicornUtils {

    private static final String UNICORN_APPKEY = "d1838897aaf0debe1da1f0443c6942ff";
    private static final String CUSTOMER_AVATAR = "https://hbcdn.huangbaoche.com/im/avatar/default/k_head.jpg";
    public static final int UNICORN_ERP_GROUPID = 196067;//售前ID

    public static void initUnicorn() {
        try {
            if (!Unicorn.init(MyApplication.getAppContext(), UNICORN_APPKEY, getDefaultOptions(), new UnicornImageLoaderRealize())) {
                HLog.e("七鱼初始化失败");
                uploadQiyuInitError("");
            }
        } catch (Exception e) {
            String errorMsg = "七鱼初始化失败";
            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                errorMsg = e.getMessage();
            }
            uploadQiyuInitError(errorMsg);
        }

    }

    public static void openServiceActivity(final Context context, final int sourceType,
                                           final OrderBean orderBean, final SkuItemBean skuItemBean,
                                           String source) {
        if (!Unicorn.isServiceAvailable()) {
            initUnicorn();
            CommonUtils.showToast("连接客服失败，请稍候重试");
            return;
        }
        if ((sourceType == UnicornServiceActivity.SourceType.TYPE_LINE && skuItemBean == null)
                || (sourceType == UnicornServiceActivity.SourceType.TYPE_ORDER && orderBean == null)) {
            return;
        }
        if (!CommonUtils.isLogin(context, source)) {
            return;
        }
        UnicornServiceActivity.Params params = new UnicornServiceActivity.Params();
        params.sourceType = sourceType;
        params.orderBean = orderBean;
        params.skuItemBean = skuItemBean;
        Intent intent = new Intent(context, ServiceQuestionActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, !TextUtils.isEmpty(source) ? source : getSource(sourceType));
        context.startActivity(intent);
        StatisticClickEvent.click(StatisticConstant.CLICK_CONCULT_TYPE, "IM");
    }

    private static String getSource(int sourceType) {
        switch (sourceType) {
            case UnicornServiceActivity.SourceType.TYPE_ORDER:
                return "订单详情";
            default:
                return "客服IM";
        }
    }

    public static void openServiceActivity(Context context, int sourceType, String source) {
        if (!CommonUtils.isLogin(context, source)) {
            return;
        }
        UnicornServiceActivity.Params params = new UnicornServiceActivity.Params();
        params.sourceType = sourceType;
        Intent intent = new Intent(context, ServiceQuestionActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, source);
        context.startActivity(intent);
        StatisticClickEvent.click(StatisticConstant.CLICK_CONCULT_TYPE, "IM");
    }

    public static void addServiceFragment(BaseActivity activity, int containerId
            , ProductDetail productDetail, int staffId, ArrayList<ServiceUserInfo> extraList, String source) {
        SharedPre.setInteger(UserEntity.getUser().getUserId(activity), SharedPre.QY_SERVICE_UNREADCOUNT, 0);
        SharedPre.setInteger(UserEntity.getUser().getUserId(activity), SharedPre.QY_GROUP_ID, staffId);

        YSFUserInfo userInfo = new YSFUserInfo();
        userInfo.userId = UserEntity.getUser().getUserId(activity);
        userInfo.data = getServiceUserInfo(extraList);
        Unicorn.setUserInfo(userInfo);

        UICustomization uiCustomization = new UICustomization();
        uiCustomization.leftAvatar = CUSTOMER_AVATAR;
        uiCustomization.rightAvatar = UserEntity.getUser().getAvatar(activity);
        uiCustomization.titleBackgroundColor = 0xFF2D2B24;
        uiCustomization.titleBarStyle = 1;
        YSFOptions options = getDefaultOptions();
        options.uiCustomization = uiCustomization;
        Unicorn.updateOptions(options);

        Unicorn.toggleNotification(false);

        // 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入三个参数分别为来源页面的url，来源页面标题，来源页面额外信息（可自由定义）
        // 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
        ConsultSource consultSource = new ConsultSource("", source, "");
        if (staffId > 0) {
            consultSource.groupId = staffId;
        }
        if (productDetail != null) {
            consultSource.productDetail = productDetail;
        }
        ServiceMessageFragment fragment = Unicorn.newServiceFragment("旅行小管家", consultSource, new FrameLayout(activity));
        if (fragment == null) {
            return;
        }
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(containerId, fragment);
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {

        }
    }

    private static String getServiceUserInfo(ArrayList<ServiceUserInfo> extraList) {
        Context context = MyApplication.getAppContext();
        UserEntity userEntity = UserEntity.getUser();
        ArrayList<ServiceUserInfo> list = new ArrayList<ServiceUserInfo>();
        list.add(new ServiceUserInfo("real_name", userEntity.getNickname(context)));
        list.add(new ServiceUserInfo("mobile_phone", userEntity.getPhone(context)));
        list.add(new ServiceUserInfo("user_name", "真实姓名", userEntity.getUserName(context)));
        list.add(new ServiceUserInfo("areaCode", "区域号码", userEntity.getAreaCode(context)));
        list.add(new ServiceUserInfo("userId", "用户ID", userEntity.getUserId(context)));
        list.add(new ServiceUserInfo("version", "应用版本", userEntity.getVersion(context)));
        if (extraList != null && extraList.size() > 0) {
            list.addAll(extraList);
        }
        return JsonUtils.toJson(list);
    }

    public static class ServiceUserInfo implements Serializable {

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
                if (listener != null && e != null) {
                    listener.onLoadFailed(e.getCause());
                }
            }
        }
    }

    public static void uploadQiyuInitError(String errorMsg) {
        if (isGranted(Manifest.permission.READ_PHONE_STATE, MyApplication.getAppContext())) {
            ApiFeedbackUtils.requestIMFeedback(23001, "23001", TextUtils.isEmpty(errorMsg) ? "七鱼客服初始化失败" : errorMsg);

        }
    }

    public static boolean isGranted(String permission, Context context) {
        return !isMarshmallow() || isGranted_(permission, context);
    }

    private static boolean isGranted_(String permission, Context context) {
        int checkSelfPermission = ActivityCompat.checkSelfPermission(context, permission);
        return checkSelfPermission == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}
