package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.DynamicsData;
import com.hugboga.custom.data.request.RequestDynamics;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeDynamicView extends LinearLayout implements HttpRequestListener {

    //动态默认切换时间
    private static final int DEFAULT_SWITCH_TIME = 5000;

    private Paint mPaint;
    private LinearGradient gradient;
    private RectF gradientRect;
    private TextView dynamicTV;

    private DynamicsData dynamicsData;
    private boolean isRun = true;

    public HomeDynamicView(Context context) {
        this(context, null);
    }

    public HomeDynamicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(0xFF000000);

        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        TextView titleTV = new TextView(getContext());
        titleTV.setPadding(paddingLeft, 0, 0, 0);
        titleTV.setTextColor(0xFFFFEE31);
        titleTV.setTextSize(15);
        titleTV.setTypeface(Typeface.DEFAULT_BOLD);
        titleTV.setText(getContext().getString(R.string.home_dynamics));
        addView(titleTV);

        dynamicTV = new TextView(getContext());
        dynamicTV.setSingleLine(true);
        dynamicTV.setFocusable(true);
        dynamicTV.setFocusableInTouchMode(true);
        dynamicTV.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        dynamicTV.setMarqueeRepeatLimit(-1);
        dynamicTV.setPadding(paddingLeft, 0, paddingLeft, 0);
        dynamicTV.setTextColor(0xFFFFEE31);
        dynamicTV.setTextSize(15);
        addView(dynamicTV);

        gradientRect = new RectF(titleTV.getPaint().measureText(titleTV.getText().toString()) + paddingLeft * 2, 0, UIUtils.getScreenWidth() - paddingLeft, UIUtils.dip2px(55));
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int[] colors = {0xFF000000, 0x00000000, 0x00000000, 0x00000000, 0xFF000000};
        gradient = new LinearGradient(gradientRect.left, gradientRect.top, gradientRect.right, gradientRect.bottom, colors, null, Shader.TileMode.MIRROR);
        mPaint.setShader(gradient);

        setRequest(0);

//        dynamicTV.setText("我先测试一下测试测试一下不是的真的吗哈哈哈美女如云啊啊2332323 adasd 打死都能看到按时打卡了圣诞节dsdSBLASD");
    }

    public void dynamicText(String text) {
        dynamicTV.setText(text);
    }

    private void setRequest(long reqTime) {
        HttpRequestUtils.request(getContext(), new RequestDynamics(getContext(), reqTime), this);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawRect(gradientRect, mPaint);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestDynamics) {
            setVisibility(View.VISIBLE);
            RequestDynamics request = (RequestDynamics) _request;
            dynamicsData = request.getData();
            if (dynamicsData == null) {
                return;
            }
            if (dynamicsData.getDynamicsAllList() == null) {
//                Log.i("aa", "首次请求");
                dynamicsData.updateDynamicsAllList();
                doSwitch();
            } else {
//                Log.i("aa", "剩余10条 --  更新数据");
                dynamicsData.updateDynamicsAllList();
            }
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        if (_request instanceof RequestDynamics) {
            if(dynamicsData == null || dynamicsData.getDynamicsAllList() == null) {
                setVisibility(View.GONE);
            }
        }
    }

    private Handler switchingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(dynamicsData == null || dynamicsData.getDynamicsAllList() == null) {
                return;
            }
            ArrayList<DynamicsData.DynamicsItem> dynamicsList = dynamicsData.getDynamicsAllList();
            final int listSize = dynamicsList.size();
            final int position = dynamicsData.getPosition();
            if (position == listSize - 10) {//还剩10条的时候请求
//                Log.i("aa", "剩余10条发请求");
                setRequest(dynamicsData.getReqtime());
            }
            if (position < listSize && dynamicsList.get(position) != null) {
//                Log.d("aa", "正常更新  -- "+dynamicsList.get(position).getTrackInfo());
                dynamicTV.setText(dynamicsList.get(position).getTrackInfo());
            }
            if (position == listSize - 1) {
//                Log.i("aa", "数组末尾，  从头再来");
                dynamicsData.setPosition(0);
            } else {
                dynamicsData.setPosition(dynamicsData.getPosition() + 1);
            }
            if (isRun) {
                doSwitch();
            }
        }
    };

    private void doSwitch() {
        switchingHandler.sendEmptyMessageDelayed(0, DEFAULT_SWITCH_TIME);
    }

    public void onRestart() {
        if (isRun = false) {
            isRun = true;
            doSwitch();
        }
    }

    public void onSuspend() {
        isRun = false;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        dynamicsData = savedState.data;
        onRestart();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        onSuspend();
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.data = dynamicsData;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        DynamicsData data;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            data = (DynamicsData) in.readSerializable();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeSerializable(data);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
