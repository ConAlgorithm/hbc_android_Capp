package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.activity.GiftDialogActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CouponActivityBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestCouponActivity;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.SharedPre;

/**
 * Created by qingcha on 16/12/10.
 *
 * 1. 未登录状态，第一次提示规则：
 *  （1）在首页、城市页、商品详情页，各自页面浏览时长超过8秒弹出提示（8秒可配置）
 *  （2）本次启动内，以上三种页面一共只显示一次
 *  （3）如果没有触发弹出就杀掉app，不能记为第一次
 *  （4）这个提示的总开关可配置
 * 2. 未登录状态，第二次提示规则：
 *  （1）第一次已提示，再次启动app仍未登录，且未填写过手机号领取成功，且距离第一次弹出提示时间超过7天，打开首页立即再次提示
 */
public class GiftController implements HttpRequestListener {

    public static final String PARAMS_FIRST_SHOW_TIME = "gift_first_show_time"; //首次弹出时间
    public static final String PARAMS_GAINED= "gift_gained"; //是否领取成功
    public static final String PARAMS_SHOW_COUNT= "show_count"; //展示次数

    private static GiftController instance = null;

    private Activity mActivity;

    private CouponActivityBean data;
    private boolean isAbort = false;
    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mActivity != null && !mActivity.isFinishing() && data != null) {
                SharedPre.setLong(PARAMS_FIRST_SHOW_TIME, System.currentTimeMillis());

                int count = SharedPre.getInteger(PARAMS_SHOW_COUNT, 0);
                SharedPre.setInteger(PARAMS_SHOW_COUNT, ++count);

                Intent intent = new Intent(mActivity, GiftDialogActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, data);
                mActivity.startActivity(intent);
            }
        }
    };

    public static GiftController getInstance(Activity _activity) {
        if (instance == null) {
            instance = new GiftController(_activity);
        } else {
            instance.setActivity(_activity);
        }
        return instance;
    }

    private GiftController(Activity _context) {
        this.mActivity = _context;
        HttpRequestUtils.request(mActivity, new RequestCouponActivity(mActivity), this, false);
    }

    private void setActivity(Activity _activity) {
        this.mActivity = _activity;
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        ApiReportHelper.getInstance().addReport(_request);
        if (_request instanceof RequestCouponActivity) {
            RequestCouponActivity request = (RequestCouponActivity) _request;
            data = request.getData();
            if (!isAbort) {
                showGiftDialog();
            }
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

    }

    public void showGiftDialog() {
        int count = SharedPre.getInteger(PARAMS_SHOW_COUNT, 0);

        if (UserEntity.getUser().isLogin(mActivity) || mHandler == null || mRunnable == null
                || data == null || data.couponActiviyVo == null || !data.couponActiviyVo.activityStatus || count >= 2) {
            return;
        }
        abortion();
        long firstShowTime = SharedPre.getLong(PARAMS_FIRST_SHOW_TIME, 0);

        if (firstShowTime == 0) {//未展示过
            isAbort = false;
            mHandler.postDelayed(mRunnable, data.couponActiviyVo.scanTime * 1000);
        } else {//未领取过且距离首次展示X天
            boolean isGained = SharedPre.getBoolean(PARAMS_GAINED, false);
            boolean cycleTime =  System.currentTimeMillis() >= (firstShowTime + data.couponActiviyVo.cycleTime * 1000);
            if (!isGained && cycleTime) {
                isAbort = false;
                mHandler.post(mRunnable);
            }
        }
    }

    public void abortion() {
        if (mHandler == null || mRunnable == null) {
            return;
        }
        isAbort = true;
        mHandler.removeCallbacks(mRunnable);
    }

}
