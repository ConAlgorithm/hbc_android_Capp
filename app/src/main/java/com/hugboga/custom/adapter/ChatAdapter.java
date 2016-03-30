package com.hugboga.custom.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.ChatVH;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.ChatOrderBean;
import com.hugboga.custom.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.ParseException;
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
            vh.mMessage.setText(chatBean.message);
            try {
                vh.mTime.setText(DateUtils.resetLetterTime(chatBean.timeStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            x.image().bind(vh.mImage, chatBean.targetAvatar, options);
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

}
