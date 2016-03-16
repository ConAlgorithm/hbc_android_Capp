package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ChatBean;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 私聊
 */
public class ChatAdapter extends BaseAdapter<ChatBean> {

    private final ImageOptions options;

    public ChatAdapter(Context context) {
        super(context);
        options = new ImageOptions.Builder()
                .setLoadingDrawableId(R.mipmap.chat_head)
                .setCircular(true)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LetterVH viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chat,null);
            viewHolder = new LetterVH(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LetterVH) convertView.getTag();
        }
        ChatBean chatBean = getItem(position);
        if(chatBean!=null){
            viewHolder.mUsername.setText(chatBean.targetName);
            viewHolder.mMessage.setText(chatBean.message);
            viewHolder.mTime.setText(chatBean.timeStr);
            //x.image().bind(viewHolder.imgBg,bean.picture);
        }
        x.image().bind(viewHolder.mImage,chatBean.targetAvatar,options);
        return convertView;
    }

    /**
     * 刷新未读消息数
     *
     * @param vh
     * @param letter
     */
   /* private void flushPoint(LetterVH vh, Letter letter) {
        String imCount = letter.getImCount();
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
    }*/

    /**
     * 刷新IM订单显示
     *
     * @param vh
     * @param letter
     */
   /* private void flushOrder(LetterVH vh, Letter letter) {
        List<LetterOrder> orders = letter.getOrders();
        if (orders != null && orders.size() > 0) {
            vh.mOrdersLayout.setVisibility(View.VISIBLE);
            LetterOrderAdapter adapter = new LetterOrderAdapter(context);
            adapter.setList(orders);
            vh.mListView.setAdapter(adapter);
        } else {
            vh.mOrdersLayout.setVisibility(View.GONE);
        }
    }*/

    /**
     * 私信ViewHolder
     * Created by ZHZEPHI on 2016/3/4.
     */
    public class LetterVH  {
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
//        @ViewInject(R.id.letter_item_listview)
//        public ChildListView mListView;

    }

}
