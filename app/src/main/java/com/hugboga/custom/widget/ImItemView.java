package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.LetterOrderAdapter;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.ChatOrderBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.Tools;

import org.xutils.view.annotation.ViewInject;

import java.text.ParseException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/5.
 */
public class ImItemView extends FrameLayout implements HbcViewBehavior  {

    @Bind(R.id.letter_item_img)
    public CircleImageView mImage; //头像
    @Bind(R.id.footer_order_btn_point)
    public TextView mUnReadCount;
    @Bind(R.id.letter_item_username)
    public TextView mUsername;
    @Bind(R.id.letter_item_time)
    public TextView mTime;
    @Bind(R.id.letter_item_message)
    public TextView mMessage;
    @Bind(R.id.letter_item_orders)
    public LinearLayout mOrdersLayout;
    @Bind(R.id.letter_item_listview)
    public ChildListView mListView;
    @Bind(R.id.footer_service_unread)
    public View serviceUnread;
    @Bind(R.id.letter_item_service_icon_tv)
    public TextView serviceIconTV;

    public ImItemView(Context context) {
        this(context,null);
    }

    public ImItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.item_chat,this);
        ButterKnife.bind(this, view);
    }

    @Override
    public void update(Object _data) {
        final ChatBean chatBean = (ChatBean)_data;
        if (chatBean != null) {
            mUsername.setText(chatBean.targetName);
            if (chatBean.targetType == 3) {
                mMessage.setText("您有任何问题，欢迎咨询客服小包子");
                serviceIconTV.setVisibility(View.VISIBLE);
            } else if (!TextUtils.isEmpty(chatBean.lastMsg)) {
                mMessage.setText(chatBean.lastMsg.trim());
                serviceIconTV.setVisibility(View.GONE);
            } else {
                mMessage.setText("");
                serviceIconTV.setVisibility(View.GONE);
            }
            try {
                mTime.setText(DateUtils.resetLetterTime(chatBean.lastTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(chatBean.targetAvatar)) {
                Tools.showImage(mImage, chatBean.targetAvatar, R.mipmap.icon_avatar_guide);
            } else {
                mImage.setImageResource(R.mipmap.icon_avatar_guide);
            }
            flushOrder( chatBean);
            flushPoint( chatBean);
        }
    }

    /**
     * 刷新未读消息数
     */
    private void flushPoint(ChatBean chatBean) {
        if(chatBean.targetType==3){
            mUnReadCount.setVisibility(View.GONE);
            int unreadCount = SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT,0);
            if(unreadCount>0){
                serviceUnread.setVisibility(View.VISIBLE);
            }else{
                serviceUnread.setVisibility(View.GONE);
            }
        }else{
            serviceUnread.setVisibility(View.GONE);
            Integer ints = chatBean.imCount;
            if (ints > 0) {
                mUnReadCount.setVisibility(View.VISIBLE);
                if (ints > 99) {
                   mUnReadCount.setText("99+");
                }else{
                    mUnReadCount.setText(""+ints);
                }
            } else {
                mUnReadCount.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 刷新IM订单显示
     */
    private void flushOrder(ChatBean chatBean) {
        List<ChatOrderBean> orders = chatBean.orderInfo;
        if (orders != null && orders.size() > 0) {
            mOrdersLayout.setVisibility(View.VISIBLE);
            LetterOrderAdapter adapter = new LetterOrderAdapter(ImItemView.this.getContext());
            adapter.setList(orders);
            mListView.setAdapter(adapter);
        } else {
            mOrdersLayout.setVisibility(View.GONE);
        }
    }
}
