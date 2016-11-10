package com.hugboga.custom.adapter;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.ChatVH;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.ChatOrderBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.NimRecentListSyncUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.Tools;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 私聊
 */
public class ChatAdapter extends ZBaseAdapter<ChatBean, ChatVH> {

    private final ImageOptions options;

    public ChatAdapter(Context context) {
        super(context);
        options = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.chat_head).setFailureDrawableId(R.mipmap.chat_head).setCircular(true).build();
    }

    @Override
    protected int initResource() {
        return R.layout.item_chat;
    }

    @Override
    protected ZBaseViewHolder getVH(View view) {
        return new ChatVH(view);
    }

    @Override
    protected void getView(int position, ChatVH vh) {
        final ChatBean chatBean = datas.get(position);

        if (chatBean != null) {
            vh.mUsername.setText(chatBean.targetName);
            if (chatBean.targetType == 3) {
                vh.mMessage.setText("您有任何问题,欢迎咨询客服小包子");
                vh.serviceIconTV.setVisibility(View.VISIBLE);
            } else if (!TextUtils.isEmpty(chatBean.message)) {
                vh.mMessage.setText(chatBean.message.trim());
                vh.serviceIconTV.setVisibility(View.GONE);
            } else {
                vh.mMessage.setText("");
                vh.serviceIconTV.setVisibility(View.GONE);
            }
            try {
                vh.mTime.setText(DateUtils.resetLetterTime(chatBean.timeStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(chatBean.targetAvatar)) {
                Tools.showImage(vh.mImage, chatBean.targetAvatar, R.mipmap.journey_head_portrait);
            } else {
                vh.mImage.setImageResource(R.mipmap.journey_head_portrait);
            }

            flushOrder(vh, chatBean);
            flushPoint(vh, chatBean);
        }
    }

    /**
     * 刷新未读消息数
     *
     * @param vh
     */
    private void flushPoint(ChatVH vh, ChatBean chatBean) {
            if(chatBean.targetType==3){
                vh.mUnReadCount.setVisibility(View.GONE);
                int unreadCount = SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_SERVICE_UNREADCOUNT,0);
                if(unreadCount>0){
                    vh.serviceUnread.setVisibility(View.VISIBLE);
                }else{
                    vh.serviceUnread.setVisibility(View.GONE);
                }
            }else{
                vh.serviceUnread.setVisibility(View.GONE);
                Integer ints = chatBean.imCount;
                if (ints > 0) {
                    vh.mUnReadCount.setVisibility(View.VISIBLE);
                    if (ints > 99) {
                        vh.mUnReadCount.setText("99+");
                    }else{
                        vh.mUnReadCount.setText(""+ints);
                    }
                } else {
                    vh.mUnReadCount.setVisibility(View.GONE);
                }
            }

        //vh.mUnReadCount.setText("99+");
    }

    /**
     * 刷新IM订单显示
     *
     * @param vh
     */
    private void flushOrder(ChatVH vh, ChatBean chatBean) {
        List<ChatOrderBean> orders = chatBean.orders;
        if (orders != null && orders.size() > 0) {
            vh.mOrdersLayout.setVisibility(View.VISIBLE);
            LetterOrderAdapter adapter = new LetterOrderAdapter(context);
            adapter.setList(orders);
            vh.mListView.setAdapter(adapter);
        } else {
            vh.mOrdersLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 同步云信数据并更新页面显示
     * @param recentContacts
     */
     public void syncUpdate(List<RecentContact> recentContacts){
         NimRecentListSyncUtils.recentListSync(datas,recentContacts);
         this.notifyDataSetChanged();
     }

    /**
     * 删除会话同步云信
     * @param recentContact
     */
    public void syncDeleteItemUpdate(RecentContact recentContact){
        if(datas!=null){
            int index = NimRecentListSyncUtils.deleteSync(datas,recentContact);
            if(index!=-1){
                notifyItemRemoved(index);
            }
        }
    }

    /**
     * 新的会话消息同步更新
     * @param list
     */
    public boolean syncNewMsgUpdate(List<RecentContact> list){
        boolean refresh = false;
        if(datas!=null){
            refresh = NimRecentListSyncUtils.updateRecentSync(datas,list);
            notifyDataSetChanged();
        }
        return  refresh;
    }

}
