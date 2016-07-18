package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.DynamicsData;
import com.hugboga.custom.data.request.RequestDynamics;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/22.
 */
public class HomeDynamicsTextView extends View implements HttpRequestListener {
    /**
     * 每条动态的间隙
     * firstText + DEFAULT_SPACE
     * secondText + DEFAULT_SPACE
     * */
    private static final String DEFAULT_SPACE = "              ";

    /**
     * 默认移动像素数，也就是速度
     * */
    private static final int DEFAULT_SPEED = 2;

    private String firstText, secondText;
    private int firstTextLength, secondTextLength;
    private int step;
    private int baseline;

    /**
     * 是否开始滚动
     * @see #setSwitch(boolean)
     * */
    public boolean isStarting = false;

    /**
     * 用来判断firstText和secondText的顺序
     * */
    private boolean isRrdinal = true;

    /**
     * 当前要显示的text，变量
     * */
    public String showText = "";

    private Paint mPaint;

    private DynamicsData dynamicsData;

    public HomeDynamicsTextView(Context context) {
        this(context, null);
    }

    public HomeDynamicsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xFFFFEE31);
        mPaint.setTextSize(UIUtils.sp2px(15));
        setRequest(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //firstText滚动出屏幕后，取下一条数据放到secondText后面
        if (isRrdinal && (step - firstTextLength) >= firstTextLength) {
            firstText = getNextText();
            firstTextLength = (int)mPaint.measureText(firstText);
            showText = secondText + firstText;
            isRrdinal = false;
            step = secondTextLength;
        } else if (!isRrdinal && (step - secondTextLength) >= secondTextLength) {
            secondText = getNextText();
            secondTextLength = (int)mPaint.measureText(secondText);
            showText = firstText + secondText;
            isRrdinal = true;
            step = firstTextLength;
        }

        if (baseline <= 0) {
            Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
            baseline = (getHeight() - 0 - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        }

        int showLength = isRrdinal ? firstTextLength : secondTextLength;
        canvas.drawText(showText, showLength - step, baseline, mPaint);

        if (isStarting) {
            step += DEFAULT_SPEED;//速度
            invalidate();
        }
    }

    public String getNextText() {
        String resultText = "";
        if (dynamicsData == null || dynamicsData.getDynamicsAllList() == null) {
            return resultText;
        }
        ArrayList<DynamicsData.DynamicsItem> dynamicsList = dynamicsData.getDynamicsAllList();
        final int listSize = dynamicsList.size();
        final int position = dynamicsData.getPosition();
        if (position == listSize - 10) {//还剩10条的时候请求
            setRequest(dynamicsData.getReqtime());
        }
        if (dynamicsList.get(position) != null) {
//            String test = ""+position +position + position + position;//FIXME qingcha test data
            resultText = dynamicsList.get(position).getTrackInfo() + DEFAULT_SPACE;
        }
        if (position >= listSize - 1) {//数组末尾,从头开始
            dynamicsData.setPosition(0);
        } else {
            dynamicsData.setPosition(dynamicsData.getPosition() + 1);
        }
        return resultText;
    }

    /**
     * 初始化数据，并开始滚动
     * 首次状态：firstText + secondText
     * */
    private void initData() {
        if (dynamicsData == null || dynamicsData.getDynamicsList() == null) {
            return;
        }
        dynamicsData.setPosition(0);
        firstText = getNextText();
        firstTextLength = (int) mPaint.measureText(firstText);
        secondText = getNextText();
        secondTextLength = (int) mPaint.measureText(secondText);
        step = firstTextLength;
        showText = firstText + secondText;
        isRrdinal = true;
        isStarting = true;
        invalidate();
    }

    private void setRequest(long reqTime) {
        HttpRequestUtils.request(getContext(), new RequestDynamics(getContext(), reqTime), this, false);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestDynamics) {
            RequestDynamics request = (RequestDynamics) _request;
            dynamicsData = request.getData();
            if (dynamicsData == null || dynamicsData.getDynamicsList() == null) {
                return;
            }
            if (dynamicsData.getDynamicsAllList() == null) {//首次请求
                dynamicsData.updateDynamicsAllList();
                initData();
            } else {//更新数据
                dynamicsData.updateDynamicsAllList();
            }
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

    }

    public boolean isStarting() {
        return isStarting;
    }

    public void setSwitch(boolean _isStarting) {
        if (this.isStarting == _isStarting) {
            return;
        }
        this.isStarting = _isStarting;
        if (isStarting) {
            invalidate();
        }
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
        initData();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        setSwitch(false);
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
