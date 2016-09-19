package com.hugboga.custom.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.ChildListView;
import com.hugboga.custom.widget.CircleImageView;

import net.grobas.view.PolygonImageView;

import org.xutils.view.annotation.ViewInject;

/**
 * 聊天VH
 * Created by ZHZEPHI on 2016/3/30.
 */
public class ChatVH extends ZBaseViewHolder {

    @ViewInject(R.id.letter_item_img)
    public CircleImageView mImage; //头像
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
    @ViewInject(R.id.footer_service_unread)
    public View serviceUnread;

    public ChatVH(View itemView) {
        super(itemView);
    }
}
