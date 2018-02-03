package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UnicornUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FgchatHeaderView extends LinearLayout {

    @BindView(R.id.chat_header_online_service_unread)
    TextView serviceUnread;

    private CsDialog csDialog;

    public FgchatHeaderView(Context context) {
        this(context, null);
    }

    public FgchatHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_chat_header, this);
        ButterKnife.bind(view);
    }

    @OnClick({R.id.chat_header_cell_layout, R.id.chat_header_online_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_header_cell_layout:
                csDialog = CommonUtils.csDialog(getContext(), null, null, null, UnicornServiceActivity.SourceType.TYPE_DEFAULT, "私聊", false, new CsDialog.OnCsListener() {
                    @Override
                    public void onCs() {
                        if (csDialog != null && csDialog.isShowing()) {
                            csDialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.chat_header_online_layout:
                UnicornUtils.openServiceActivity(getContext(), UnicornServiceActivity.SourceType.TYPE_CHAT_LIST,"私聊");
                break;
        }
    }

    public void flushPoint() {
        int unreadCount = SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT,0);
        if (unreadCount > 0) {
            serviceUnread.setVisibility(View.VISIBLE);
        } else {
            serviceUnread.setVisibility(View.GONE);
        }
    }
}
