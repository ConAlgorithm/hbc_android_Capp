package com.hugboga.custom.widget.charter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterSecondStepActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.widget.CsDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/3/9.
 */
public class CharterEmptyView extends LinearLayout {

    public static final int EMPTY_TYPE = 1;
    public static final int ERROR_TYPE = 2;

    @BindView(R.id.charter_list_empty_iv)
    ImageView emptyIV;
    @BindView(R.id.charter_list_empty_hint_tv)
    TextView hintTV;
    @BindView(R.id.charter_list_empty_refresh_tv)
    TextView refreshTV;

    private CharterEmptyView.OnRefreshDataListener listener;
    private int type;
    CsDialog csDialog;
    public CharterEmptyView(Context context) {
        this(context, null);
    }

    public CharterEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view  = inflate(context, R.layout.view_charter_empty, this);
        ButterKnife.bind(view);

        refreshTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        refreshTV.getPaint().setAntiAlias(true);
    }

    public void setEmptyType(int _type) {
        this.type = _type;
        if (_type == ERROR_TYPE) {
            setVisibility(View.VISIBLE);
            emptyIV.setBackgroundResource(R.drawable.empty_wifi);
            hintTV.setText("似乎与网络断开，请检查网络环境");
            refreshTV.setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.VISIBLE);
            emptyIV.setBackgroundResource(R.drawable.empty_trip);
            refreshTV.setVisibility(View.GONE);
            if (getContext() instanceof CharterSecondStepActivity) {
                final CharterSecondStepActivity charterSecondStepActivity = (CharterSecondStepActivity) getContext();
                OrderUtils.genCLickSpan(charterSecondStepActivity, hintTV, "很抱歉，还不能线上预订这个城市的包车服务\n请联系客服，帮您定制行程",
                        "联系客服",
                        null,
                        0xFFA8A8A8,
                        new OrderUtils.MyCLickSpan.OnSpanClickListener() {
                            @Override
                            public void onSpanClick(View view) {
                                //DialogUtil.showServiceDialog(getContext(), null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, charterSecondStepActivity.getEventSource());
                                csDialog = CommonUtils.csDialog(getContext(), null, null, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, charterSecondStepActivity.getEventSource(), new CsDialog.OnCsListener() {
                                    @Override
                                    public void onCs() {
                                        if (csDialog != null && csDialog.isShowing()) {
                                            csDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        });
            }
        }
    }

    @OnClick({R.id.charter_list_empty_refresh_tv})
    public void refreshData() {
        if (listener != null) {
            this.listener.onRefresh(type);
        }
    }

    public interface OnRefreshDataListener {
        public void onRefresh(int type);
    }

    public void setOnRefreshDataListener(CharterEmptyView.OnRefreshDataListener listener) {
        this.listener = listener;
    }
}
