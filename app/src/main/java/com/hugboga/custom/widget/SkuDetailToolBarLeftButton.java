package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.UnreadCountChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqi on 2017/12/25.
 */

public class SkuDetailToolBarLeftButton extends RelativeLayout implements View.OnClickListener {


    @BindView(R.id.header_right_btn_share_red_dot)
    ImageView headerRightBtnShareRedDot;
    @BindView(R.id.backgroundimageview)
    ImageView backGroundImageView;

    private boolean isWeiXin, isChatMessage = false;//是否安装微信,否有新消息
    private ToolBarLeftClick clickListener;
    private PathChatDialog pathChatDialog;

    public interface ToolBarLeftClick {
        void shareClickListener();

        void serviceChatListener();
    }

    View view;

    public SkuDetailToolBarLeftButton(Context context) {
        this(context, null);
    }

    public SkuDetailToolBarLeftButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fg_sku_detail_header_image_share, this);
        ButterKnife.bind(view);

        isLnstallWeiXin();
        if (UserEntity.getUser().isLogin(context)) {
            try {
                if (Unicorn.isServiceAvailable()) {
                    Unicorn.addUnreadCountChangeListener(listener, true);
                }
            } catch (Exception e) {
                MLog.e("SkuDetailActivity:添加客服监听失败");
            }
        }
        setOnClickListener(this);
    }

    public UnreadCountChangeListener listener = new UnreadCountChangeListener() { // 声明一个成员变量
        @Override
        public void onUnreadCountChange(int count) {
            SharedPre.setInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT, count);
            if (count > 0) {
                isChatMessage = true;
            } else {
                isChatMessage = false;
            }
            distinguishChatConfug();

        }
    };

    public void setClickListener(ToolBarLeftClick clickListener) {
        this.clickListener = clickListener;
    }

    public void distinguishChatConfug() {
        if (!isWeiXin) {
            backGroundImageView.setImageResource(R.mipmap.play_down_chat);
        } else {
            backGroundImageView.setImageResource(R.mipmap.play_dot_black);
        }
        if (SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT, 0) > 0) {
            isChatMessage = true;
        } else {
            isChatMessage = false;
        }
        isChatRedDot(isChatMessage);
        if (pathChatDialog != null) {
            pathChatDialog.setMessageState(isChatMessage);
        }

    }

    public void isLnstallWeiXin() {
        WXShareUtils wxShareUtils = WXShareUtils.getInstance(getContext());
        isWeiXin = wxShareUtils.isInstall(false);
        distinguishChatConfug();
    }

    private void isChatRedDot(boolean b) {
        if (b) {
            if (!isWeiXin) {
                backGroundImageView.setImageResource(R.mipmap.play_down_chat_red_dot);
                return;
            }
            headerRightBtnShareRedDot.setVisibility(View.VISIBLE);
        } else {
            if (!isWeiXin) {
                backGroundImageView.setImageResource(R.mipmap.play_down_chat);
                return;
            }
            headerRightBtnShareRedDot.setVisibility(View.GONE);
        }
    }


    public void onClick(View view) {
        if (clickListener == null) {
            return;
        }
        if (!isWeiXin) {
            clickListener.serviceChatListener();
            return;
        }
        pathChatDialog = new PathChatDialog(getContext(), new PathChatDialog.DialogClickListener() {
            @Override
            public void shareClick() {
                clickListener.shareClickListener();
            }

            @Override
            public void chatClict() {
                clickListener.serviceChatListener();
            }
        });
        pathChatDialog.setMessageState(isChatMessage);
        pathChatDialog.show();
    }

}
