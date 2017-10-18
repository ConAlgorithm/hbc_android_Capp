# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

-dontshrink
-dontpreverify
-dontoptimize
-dontusemixedcaseclassnames

-flattenpackagehierarchy
-allowaccessmodification
-printmapping map.txt

-optimizationpasses 7
-dontnote
-verbose
-keepattributes Exceptions,InnerClasses
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-ignorewarnings

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.IntentService
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends java.lang.Throwable {*;}
-keep public class * extends java.lang.Exception {*;}
-keep public class com.hugboga.guide.data.entity.** {*;}
-keep public class com.hugboga.guide.fragment.** {*;}
-keep public class android.net.http.SslError

-keep public class com.hugboga.custom.R$*{
    public static final int *;
}

-keepattributes Exceptions,InnerClasses
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

# 以下是自定义ZWebView中Java中js对象
-keep class com.hugboga.custom.data.net.WebAgent { *; }

# 以下是xUtils的混淆规则
-keep class * extends java.lang.annotation.Annotation { *; }


# 以下是友盟混淆的规则
-keepclassmembers class * {
    public <init>(org.json.JSONObject);
}

# Permission
-dontwarn com.zhy.m.**
-keep class com.zhy.m.** {*;}
-keep interface com.zhy.m.** { *; }
-keep class **$$PermissionProxy { *; }

# android-support-v4
-dontwarn android.support.**
-keep class android.support.** { *;}

# android-support-v7-appcompat
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *;}

# google-play-service
-dontwarn com.google.**
-keep class com.google.** { *;}

# libammsdk
-dontnote com.tencent.**
-keep class com.tencent.** {*;}

# jpush-sdk-release
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *;}
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

# com.umeng.fb
-dontwarn com.umeng.fb.**
-keep class com.umeng.fb.** { *;}

# com.yalantis.ucrop,剪切图片
-dontwarn com.yalantis.ucrop.**
-keep class com.yalantis.ucrop.** { *;}

# permission-lib,权限混淆
-dontwarn com.zhy.m.permission.**
-keep class com.zhy.m.permission.** { *;}
-keep interface com.zhy.m.permission.** { *; }
-keepclassmembers class **.$$PermissionProxy {
   public *;
   private *;
}

# Rong IM 2.4.6版本
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
 public *;
}
-keepattributes Exceptions,InnerClasses
-keep class io.rong.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent{*;}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keepclassmembers class * extends com.sea_monster.dao.AbstractDao {
 public static java.lang.String TABLENAME;
}
-keep class **$Properties
-dontwarn org.eclipse.jdt.annotation.**
-keep class com.ultrapower.** {*;}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# adding this in to preserve line numbers so that the stack traces
# can be remapped
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-keep class com.hugboga.custom.widget.DialogUtil {
    public static ** getInstance(**);
}

-keepclassmembers class ** {
    public void onEvent*(**);
}
################### region for xUtils
-keepattributes Signature,*Annotation*
-keep public class org.xutils.** {
    public protected *;
}
-keep public interface org.xutils.** {
    public protected *;
}
-keepclassmembers class * extends org.xutils.** {
    public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
    @org.xutils.view.annotation.Event <methods>;
}
#################### end region

##EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @de.greenrobot.event.Subscribe <methods>;
}
-keep enum de.greenrobot.event.ThreadMode { *; }
-keep enum de.greenrobot.event.EventBusException { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keep class com.hugboga.custom.receiver.XMPushReceiver {*;}

-keep class com.hugboga.custom.data.event.EventAction { *; }
-keep class com.hugboga.custom.activity.PersonInfoActivity{ *; }
-keep class com.hugboga.custom.activity.IMChatActivity{ *; }
-keep class com.hugboga.custom.activity.BaseActivity{ *; }
-keep class com.hugboga.custom.MyApplication{ *; }

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keep class com.anupcowkur.reservoir.**{*;}

-keep class com.hugboga.custom.data.bean.**{*;}
-keep class com.hugboga.custom.widget.**{*;}
-keep class com.hugboga.custom.activity.ChooseCityActivity



-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-printmapping build/outputs/mapping/release/mapping.txt
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#eventbus ProGuard
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

-keep class com.hugboga.custom.fragment.** {*;}
-keep class com.hugboga.custom.widget.** {*;}
-keep class org.xmlpull.v1.** {*;}
-keep class * implements java.io.Serializable {*;}
-keepclassmembers class * implements java.io.Serializable {*;}

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# 七鱼
-dontwarn com.qiyukf.**
-keep class com.qiyukf.** {*;}
# 云信相关
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify


# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

-dontwarn org.apache.http.**
-dontwarn com.amap.**
-dontwarn com.alibaba.**
-dontwarn com.netease.**
-dontwarn io.netty.**
-dontwarn com.autonavi.amap.**


#如果你使用全文检索插件，需要加入
-dontwarn java.nio.channels.SeekableByteChannel
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}
-keep class org.lukhnos.** {*;}
-keep class org.tartarus.** {*;}

### keep options
#system default, from android example
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keepattributes *Annotation*,InnerClasses
#-keepattributes SourceFile,LineNumberTable

### 3rd party jars
-keep class android.support.** {*;}
-keep class com.amap.** {*;}
-keep class android.webkit.** {*;}
-keep class com.autonavi.amap.** {*;}

### nimlib
-keep class com.netease.** {*;}
-keep class com.alibaba.fastjson.** {*;}
-keep class net.sqlcipher.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class **.R$* {
 *;
}

#TONGDUN
-dontwarn android.os.**
-dontwarn com.android.internal.**
-keep class cn.tongdun.android.**{*;}

-keep class com.hugboga.im.** {*;}

-ignorewarnings
#-libraryjars   libs/payecoplugin.jar
-keep class  com.payeco.android.plugin.**{*;}
-dontwarn   com.payeco.android.plugin.**
#不混淆org.apache.http.legacy.jar
 -dontwarn android.net.compatibility.**
 -dontwarn android.net.http.**
 -dontwarn com.android.internal.http.multipart.**
 -dontwarn org.apache.commons.**
 -keep class android.net.compatibility.**{*;}
 -keep class android.net.http.**{*;}
 -keep class com.android.internal.http.multipart.**{*;}
 -keep class org.apache.commons.**{*;}
 -keep class org.apache.http.**{*;}
-keep class com.pili.pldroid.player.** { *; }
-keep class tv.danmaku.ijk.media.player.** {*;}

#神策
-dontwarn com.sensorsdata.analytics.android.sdk.**
-keep class com.sensorsdata.analytics.android.sdk.** {
*;
}

# ProGuard configurationsfor NetworkBench Lens
-keep class com.networkbench.** { *; }
-dontwarn com.networkbench.**
# End NetworkBench Lens