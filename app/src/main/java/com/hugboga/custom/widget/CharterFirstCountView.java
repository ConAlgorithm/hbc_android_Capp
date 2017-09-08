package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.OrderUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/2/22.
 */
public class CharterFirstCountView extends LinearLayout implements ChooseCountView.OnCountChangeListener, ChooseCountView.OnInvalidClickListener {

    @Bind(R.id.charter_first_count_adult_choose_count_view)
    ChooseCountView adultCountView;
    @Bind(R.id.charter_first_count_child_choose_count_view)
    ChooseCountView childCountView;

    @Bind(R.id.charter_first_count_child_seat_layout)
    RelativeLayout childSeatLayout;
    @Bind(R.id.charter_first_count_child_seat_choose_count_view)
    ChooseCountView childSeatCountView;

    @Bind(R.id.charter_first_count_child_seat_hint_layout)
    LinearLayout childSeatHintLayout;
    @Bind(R.id.charter_first_count_child_seat_hint_tv)
    TextView childSeathintTV;

    @Bind(R.id.charter_first_count_hint_tv)
    TextView hintTV;

    private Context context;

    private int adultCount = 0;      // 成人数
    private int childCount = 0;      // 儿童数
    private int childSeatCount = 0;  // 儿童座椅数
    private int maxPassengers = 10;  // 最大乘车人数

    private OnOutRangeListener listener;
    private boolean isSupportChildSeat;
    private int firstClickCount = 1;
    CsDialog csDialog;
    public CharterFirstCountView(Context context) {
        this(context, null);
    }

    public CharterFirstCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        View view = inflate(context, R.layout.view_charter_first_count, this);
        ButterKnife.bind(view);

        adultCountView.setOnCountChangeListener(this);
        childCountView.setOnCountChangeListener(this);
        childSeatCountView.setOnCountChangeListener(this);
        childSeatCountView.setOnInvalidClickListener(this);
        setCountViewEnabled(false);
        childSeatCountView.setCount(0, false);
    }

    @Override
    public void onCountChange(View view, int count) {
        if (context instanceof CharterFirstStepActivity) {
            if (firstClickCount == 1) {
                SensorsUtils.onAppClick(((CharterFirstStepActivity) context).getEventSource(), "出行人数", ((CharterFirstStepActivity) context).getIntentSource());
                firstClickCount += 1;
            }
        }
        switch (view.getId()) {
            case R.id.charter_first_count_adult_choose_count_view://成人数
                this.adultCount = count;
                checkCountView();
                break;
            case R.id.charter_first_count_child_choose_count_view://儿童数
                this.childCount = count;
                checkCountView();

                if (childSeatCount > childCount) {
                    childSeatCount--;
                    childSeatCountView.setCount(childSeatCount);
                }
                resetChildSeatLayout();
                break;
            case R.id.charter_first_count_child_seat_choose_count_view://儿童座椅
                this.childSeatCount = count;
                checkCountView();
                break;
        }
        setHintViewVisibility();
    }

    @Override
    public void onInvalidClick(View view, int count, boolean isPlus) {
        switch (view.getId()) {
            case R.id.charter_first_count_child_seat_choose_count_view://儿童座椅，应该不会出现这种情况（儿童座椅能添加，人数不能添加）
                if (childSeatCount == childCount) {
                    return;
                }
                if (isPlus) {
                    setHintViewVisibility(true);
                }
                break;
        }
    }

    public void resetChildSeatLayout() {
        if (childCount > 0) {
            if (isSupportChildSeat) {
                childSeatLayout.setVisibility(View.VISIBLE);
                childSeatHintLayout.setVisibility(View.GONE);
            } else {
                childSeatLayout.setVisibility(View.GONE);
                childSeatHintLayout.setVisibility(View.VISIBLE);
                childSeathintTV.setText("很抱歉，该城市暂不提供儿童座椅");
            }
        } else {
            childSeatLayout.setVisibility(View.GONE);
            childSeatHintLayout.setVisibility(View.GONE);
        }
    }

    public void setCountViewEnabled(boolean isEnabled) {
        adultCountView.setEnabled(isEnabled);
        childCountView.setEnabled(isEnabled);
        if (!isEnabled) {
            childSeatLayout.setVisibility(View.GONE);
            childSeatHintLayout.setVisibility(View.GONE);
            hintTV.setVisibility(View.GONE);
        }
    }

    /**
     * type 1:成人、2:儿童、3:儿童座椅(1.5)
     * 乘车人数 =（成人数 + 儿童座椅数 * 1.5 + 不用座椅儿童数）
     * */
    private boolean checkCount(int type) {
        switch (type) {
            case 1:
            case 2:
                return (adultCount + Math.round(childSeatCount * 1.5) + (childCount - childSeatCount) < maxPassengers);
            case 3:
                double count = adultCount + childSeatCount * 1.5 + (childCount - childSeatCount);
                return maxPassengers - count >= 0.5 && childSeatCount < childCount;
            default:
                return false;
        }
    }

    private void checkCountView() {
        adultCountView.setIsCanAdd(checkCount(1));
        childCountView.setIsCanAdd(checkCount(2));
        childSeatCountView.setIsCanAdd(checkCount(3));
    }

    private boolean isResetCountView() {
        if (!isSupportChildSeat) {
            childSeatCount = 0;
        }
        return  adultCount + childSeatCount * 1.5 + (childCount - childSeatCount) > maxPassengers;
    }

    public void setMaxPassengers(boolean isInit, int maxPassengers, boolean isSupportChildSeat, boolean isGuide, boolean isSeckills) {
        if (maxPassengers <= 0) {
            setCountViewEnabled(false);
            return;
        }
        this.maxPassengers = maxPassengers;
        this.isSupportChildSeat = isSupportChildSeat;

        if (isInit || isResetCountView()) {
            adultCount = maxPassengers >= 2 ? 2 : 1;
            childCount = 0;
            childSeatCount = 0;

            adultCountView.setMinCount(1).setCount(adultCount, false);
            childCountView.setCount(0, false);
            childSeatCountView.setCount(0, false);
            childSeatLayout.setVisibility(View.GONE);
            childSeatHintLayout.setVisibility(View.GONE);
        } else {
            resetChildSeatLayout();
        }
        if (isSeckills) {
            hintTV.setText(context.getResources().getString(R.string.charter_first_max_passengers_hint3, "" + maxPassengers));
        } else {
            int hintResId = isGuide ? R.string.charter_first_max_passengers_hint2 : R.string.charter_first_max_passengers_hint;
            String contentStr = context.getResources().getString(hintResId, "" + maxPassengers);
            if (context instanceof Activity) {
                OrderUtils.genCLickSpan((Activity) context, hintTV, contentStr,
                        context.getResources().getString(R.string.charter_first_max_passengers_service),
                        null,
                        0xFFFFFFFF,
                        new OrderUtils.MyCLickSpan.OnSpanClickListener() {
                            @Override
                            public void onSpanClick(View view) {
                                //DialogUtil.showServiceDialog(context, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, ((BaseActivity)context).getEventSource());
                                csDialog = CommonUtils.csDialog(getContext(), null, null, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, ((BaseActivity) context).getEventSource(), new CsDialog.OnCsListener() {
                                    @Override
                                    public void onCs() {
                                        if (csDialog != null && csDialog.isShowing()) {
                                            csDialog.dismiss();
                                        }
                                    }
                                });

                            }
                        });
            } else {
                hintTV.setText(contentStr);
            }
        }
        checkCountView();
        setHintViewVisibility();
        setCountViewEnabled(true);
    }

    private void setHintViewVisibility() {
        setHintViewVisibility(false);
    }

    private void setHintViewVisibility(boolean isToughShow) {
        if (maxPassengers <= 0) {
            hintTV.setVisibility(View.INVISIBLE);
            return;
        }
        final boolean isOutRange = (adultCount + Math.round(childSeatCount * 1.5) + (childCount - childSeatCount)) >= maxPassengers;
        boolean isShow = isOutRange || isToughShow;
        if (isShow) {
            hintTV.setVisibility(View.VISIBLE);
        } else {
            hintTV.setVisibility(View.INVISIBLE);
        }

        if (listener != null) {
            listener.onOutRangeChange(isShow);
        }
    }

    public void setHintViewVisibility(int visibility) {
        hintTV.setVisibility(visibility);
    }

    public int getAdultValue() {
        return adultCountView.getCount();
    }

    public int getChildValue() {
        return childCountView.getCount();
    }

    public int getChildSeatValue() {
        return childSeatCountView.getCount();
    }

    public void setAdultValue(int value) {
        this.adultCount = value;
    }

    public void setChildValue(int value) {
        this.childCount = value;
    }

    public int getPassengers() {
        return getAdultValue() + getChildValue();
    }

    public interface OnOutRangeListener {
        public void onOutRangeChange(boolean isOut);
    }

    public void setOnOutRangeListener(OnOutRangeListener listener) {
        this.listener = listener;
    }

}
