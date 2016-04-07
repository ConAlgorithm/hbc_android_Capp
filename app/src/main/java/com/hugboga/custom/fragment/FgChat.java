package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.recycler.ZListPageView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.ChatAdapter;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.data.request.RequestChatList;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

/**
 * 聊天页面
 * Created by admin on 2016/3/8.
 */

@ContentView(R.layout.fg_chat)
public class FgChat extends BaseFragment implements View.OnClickListener, ZBaseAdapter.OnItemClickListener, ZListPageView.NoticeViewTask {

    @ViewInject(R.id.header_left_btn)
    private ImageView leftBtn;

    @ViewInject(R.id.chat_content)
    RelativeLayout chatLayout; //主题内容显示
    @ViewInject(R.id.listview)
    ZListPageView recyclerView;
    @ViewInject(R.id.swipe)
    ZSwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.chat_logout)
    RelativeLayout emptyLayout;

    private ChatAdapter adapter;

    @Override
    protected void initHeader() {
        fgTitle.setText("私聊");
        leftBtn.setImageResource(R.mipmap.header_menu);
        leftBtn.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        MLog.e(this + " initView");
        initListView();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    private void initListView() {
        MLog.e(this + " initListView");
        adapter = new ChatAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setzSwipeRefreshLayout(swipeRefreshLayout);
        recyclerView.setEmptyLayout(emptyLayout);
        RequestChatList parserChatList = new RequestChatList(getActivity());
        recyclerView.setRequestData(parserChatList);
        recyclerView.setOnItemClickListener(this);
        recyclerView.setNoticeViewTask(this);
    }

    /**
     * 加载数据
     */
    public void loadData() {
        if (recyclerView != null) {
            recyclerView.showPageFirst();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected Callback.Cancelable requestData() {
        MLog.e("isLogin=" + UserEntity.getUser().isLogin(getActivity()));
        if (!UserEntity.getUser().isLogin(getActivity())) {
            needHttpRequest = true;
            emptyLayout.setVisibility(View.VISIBLE);
            chatLayout.setVisibility(View.GONE);
            return null;
        } else {
            emptyLayout.setVisibility(View.GONE);
            chatLayout.setVisibility(View.VISIBLE);
            loadData();
            return null;
        }
    }

    @Override
    protected void inflateContent() {
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
    }

    @Event({R.id.login_btn, R.id.header_left_btn})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                startFragment(new FgLogin());
                break;
            case R.id.header_left_btn:
                MLog.e("left  " + view);
                ((MainActivity) getActivity()).openDrawer();
                break;
        }
    }


    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.e("onFragmentResult " + bundle);
        requestData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                ((MainActivity) getActivity()).openDrawer();
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(EventAction action) {
        MLog.e(this + " onEventMainThread " + action.getType());
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
            case REFRESH_CHAT_LIST:
                requestData();
                break;
            case CLICK_USER_LOOUT:
                chatLayout.setVisibility(View.GONE);
                //清理列表数据
                adapter.getDatas().clear();
                adapter.notifyDataSetChanged();
                emptyLayout.setVisibility(View.VISIBLE);
                ((MainActivity) getActivity()).setIMCount(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        ChatBean chatBean = adapter.getDatas().get(position);
        if ("3".equals(chatBean.targetType)) {
            String titleJson = getChatInfo(chatBean.targetId, chatBean.targetAvatar, chatBean.targetName, chatBean.targetType);
            RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.APP_PUBLIC_SERVICE, chatBean.targetId, titleJson);
        } else if ("1".equals(chatBean.targetType)) {
            String titleJson = getChatInfo(chatBean.userId, chatBean.targetAvatar, chatBean.targetName, chatBean.targetType);
            RongIM.getInstance().startPrivateChat(getActivity(), chatBean.targetId, titleJson);
        } else {
            MLog.e("目标用户不是客服，也不是司导");
        }
    }

    private String getChatInfo(String userId, String userAvatar, String title, String targetType) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.isChat = true;
        chatInfo.userId = userId;
        chatInfo.userAvatar = userAvatar;
        chatInfo.title = title;
        chatInfo.targetType = targetType;
        return new ParserChatInfo().toJsonString(chatInfo);
    }

    @Override
    public void notice(Object object) {
        List<ChatBean> chatBeans = adapter.getDatas();
        if (chatBeans != null && chatBeans.size() > 0) {
            int totalCount = 0;
            for (ChatBean bean : chatBeans) {
                totalCount += bean.imCount;
            }
            ((MainActivity) getActivity()).setIMCount(totalCount);
            MLog.e("totalCount = " + totalCount);
        }
    }

    @Override
    public void error(ExceptionInfo errorInfo, BaseRequest request) {

    }
}
