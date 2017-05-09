package com.hugboga.custom.widget.title;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/2/21.
 */
public class TitleBar extends TitleBarBase {

    @Bind(R.id.titlebar_left_layout)
    RelativeLayout leftLayout;
    @Bind(R.id.titlebar_left_tv)
    TextView leftTV;

    @Bind(R.id.titlebar_right_layout)
    RelativeLayout rightLayout;
    @Bind(R.id.titlebar_right_tv)
    TextView rightTV;

    @Bind(R.id.titlebar_middle_tv)
    TextView middleTV;

    private OnTitleBarBackListener listener;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_titlebar_default, this);
        ButterKnife.bind(view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        int defaultTitle = typedArray.getResourceId(R.styleable.TitleBar_title_res, View.NO_ID);
        if (defaultTitle != View.NO_ID) {
            middleTV.setText(context.getString(defaultTitle));
        }
        int rightIcon = typedArray.getResourceId(R.styleable.TitleBar_right_icon, View.NO_ID);
        if (rightIcon != View.NO_ID) {
            rightLayout.setVisibility(View.VISIBLE);
            rightTV.setBackgroundResource(rightIcon);
        }
        typedArray.recycle();
    }

    @OnClick({R.id.titlebar_left_layout})
    public void onBack(View view) {
        if (listener != null) {
            if (listener.onTitleBarBack()) {
                return;
            }
        }
        Context context = getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            CommonUtils.hideSoftInput(activity);
            activity.finish();
        }
    }

    public void setRightListener(OnClickListener listener) {
        if (rightLayout == null) {
            return;
        }
        rightLayout.setVisibility(View.VISIBLE);
        rightLayout.setOnClickListener(listener);
    }

    public void setRightListener(int resid, OnClickListener listener) {
        if (rightLayout == null) {
            return;
        }
        rightTV.setBackgroundResource(resid);
        rightLayout.setVisibility(View.VISIBLE);
        rightLayout.setOnClickListener(listener);
    }

    public void setTitle(String title) {
        if (middleTV == null) {
            return;
        }
        middleTV.setText(title);
    }

    public void setTitle(int resid) {
        if (middleTV == null) {
            return;
        }
        middleTV.setText(resid);
    }

    public TextView getLeftTV() {
        return leftTV;
    }

    public TextView getRightTV() {
        return rightTV;
    }

    public void setLeftViewHide() {
        if (leftLayout == null) {
            return;
        }
        leftLayout.setVisibility(View.INVISIBLE);
    }

    public interface OnTitleBarBackListener{
        public boolean onTitleBarBack();
    }

    public void setTitleBarBackListener(OnTitleBarBackListener listener){
        this.listener = listener;
    }
}
