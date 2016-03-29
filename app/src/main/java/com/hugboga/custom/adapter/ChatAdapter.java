package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.ChatOrderBean;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.widget.ChildListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 私聊
 */
public class ChatAdapter extends BaseAdapter<ChatBean> {

    private final ImageOptions options;

    public ChatAdapter(Context context) {
        super(context);
        options = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.chat_head).setFailureDrawableId(R.mipmap.chat_head).setCircular(true).build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LetterVH viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat, null);
            viewHolder = new LetterVH(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LetterVH) convertView.getTag();
        }
        final ChatBean chatBean = getItem(position);
        if (chatBean != null) {
            viewHolder.mUsername.setText(chatBean.targetName);
            viewHolder.mMessage.setText(chatBean.message);
            try {
                viewHolder.mTime.setText(DateUtils.resetLetterTime(chatBean.timeStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            x.image().bind(viewHolder.mImage, chatBean.targetAvatar, options);
            flushOrder(viewHolder, chatBean);
            flushPoint(viewHolder, chatBean);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("3".equals(chatBean.targetType)) {
                        String titleJson = getChatInfo(chatBean.targetId, chatBean.targetAvatar, chatBean.targetName, chatBean.targetType);
                        RongIM.getInstance().startCustomerServiceChat(mContext, chatBean.targetId, titleJson);
                    } else if ("1".equals(chatBean.targetType)) {
                        String titleJson = getChatInfo(chatBean.userId, chatBean.targetAvatar, chatBean.targetName, chatBean.targetType);
                        RongIM.getInstance().startPrivateChat(mContext, chatBean.targetId, titleJson);
                    } else {
                        MLog.e("目标用户不是客服，也不是司导");
                    }
                }
            });
        }
        return convertView;
    }

    private String getChatInfo(String userId, String userAvatar, String title, String targetType) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("isChat", true);
            obj.put("userId", userId);
            obj.put("userAvatar", userAvatar);
            obj.put("title", title);
            obj.put("targetType", targetType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    /**
     * 刷新未读消息数
     *
     * @param vh
     */
    private void flushPoint(LetterVH vh, ChatBean chatBean) {
        String imCount = chatBean.imCount;
        if (!TextUtils.isEmpty(imCount) || !"".equals(imCount.trim())) {
            Integer ints = Integer.parseInt(imCount);
            if (ints > 0) {
                vh.mUnReadCount.setVisibility(View.VISIBLE);
                if (ints > 99) {
                    ints = 99;
                }
                vh.mUnReadCount.setText(String.valueOf(ints));
            } else {
                vh.mUnReadCount.setVisibility(View.GONE);
            }
        } else {
            vh.mUnReadCount.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新IM订单显示
     *
     * @param vh
     */
    private void flushOrder(LetterVH vh, ChatBean chatBean) {
        List<ChatOrderBean> orders = chatBean.orders;
        if (orders != null && orders.size() > 0) {
            vh.mOrdersLayout.setVisibility(View.VISIBLE);
            LetterOrderAdapter adapter = new LetterOrderAdapter(mContext);
            adapter.setList(orders);
            vh.mListView.setAdapter(adapter);
        } else {
            vh.mOrdersLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 私信ViewHolder
     * Created by ZHZEPHI on 2016/3/4.
     */
    public class LetterVH {
        public LetterVH(View itemView) {
            x.view().inject(this, itemView);
        }

        @ViewInject(R.id.letter_item_img)
        public ImageView mImage; //头像
        @ViewInject(R.id.footer_order_btn_point)
        public TextView mUnReadCount;
        @ViewInject(R.id.letter_item_username)
        public TextView mUsername;
        @ViewInject(R.id.letter_item_time)
        public TextView mTime;
        @ViewInject(R.id.letter_item_message)
        public TextView mMessage;
        @ViewInject(R.id.letter_item_orders)
        public LinearLayout mOrdersLayout;
        @ViewInject(R.id.letter_item_listview)
        public ChildListView mListView;
    }

}
