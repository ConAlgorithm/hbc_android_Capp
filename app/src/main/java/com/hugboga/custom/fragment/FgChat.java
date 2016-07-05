package com.hugboga.custom.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
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
import com.hugboga.custom.data.request.RequestRemoveChat;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;


import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

import static u.aly.au.T;
import static u.aly.au.ad;

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

    @ViewInject(R.id.chat_list_empty_tv)
    TextView emptyTV;

    private ChatAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        if (UserEntity.getUser().isLogin(getActivity()) && recyclerView != null && !recyclerView.isLoading() && adapter != null && adapter.getItemCount() <= 0) {
            loadData();
        }
    }

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
        recyclerView.setOnItemLongClickListener(new ZBaseAdapter.OnItemLongClickListener(){
            @Override
            public void onItemLongClick(View view, final int position) {
                if(position != 0){
                   final ChatBean chatBean = adapter.getDatas().get(position);
                    AlertDialogUtils.showAlertDialog(getActivity(), getString(R.string.del_chat), "确定", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestRemoveChat requestRemoveChat = new RequestRemoveChat(getActivity(),chatBean.userId);
                            HttpRequestUtils.request(getContext(), requestRemoveChat, new HttpRequestListener() {
                                @Override
                                public void onDataRequestSucceed(BaseRequest request) {
                                    adapter.removeDatas(position);
                                }

                                @Override
                                public void onDataRequestCancel(BaseRequest request) {

                                }

                                @Override
                                public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

                                }
                            });
                            dialog.dismiss();
                        }
                    },new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    /**
     * 加载数据
     */
    public void loadData() {
        if (recyclerView != null) {
            emptyTV.setVisibility(View.GONE);
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

    @Event({R.id.login_btn, R.id.header_left_btn, R.id.chat_list_empty_tv})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                Bundle bundle = new Bundle();
                bundle.putString("source","私聊页");
                startFragment(new FgLogin(), bundle);

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("source", "私聊页");
                MobclickAgent.onEvent(getActivity(), "login_trigger", map);
                break;
            case R.id.header_left_btn:
                MLog.e("left  " + view);
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.chat_list_empty_tv:
                loadData();
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

    @Subscribe
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
                if(adapter.getDatas()!=null)
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
            String titleJson = getChatInfo(chatBean.targetId, chatBean.targetAvatar, chatBean.targetName, chatBean.targetType,chatBean.inBlack);
            RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.APP_PUBLIC_SERVICE, chatBean.targetId, titleJson);
        } else if ("1".equals(chatBean.targetType)) {
            String titleJson = getChatInfo(chatBean.userId, chatBean.targetAvatar, chatBean.targetName, chatBean.targetType,chatBean.inBlack);
            RongIM.getInstance().startPrivateChat(getActivity(), chatBean.targetId, titleJson);
        } else {
            MLog.e("目标用户不是客服，也不是司导");
        }
    }

    private String getChatInfo(String userId, String userAvatar, String title, String targetType,int inBlack) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.isChat = true;
        chatInfo.userId = userId;
        chatInfo.userAvatar = userAvatar;
        chatInfo.title = title;
        chatInfo.targetType = targetType;
        chatInfo.inBlack = inBlack;
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
        emptyTV.setVisibility(View.GONE);
    }

    @Override
    public void error(ExceptionInfo errorInfo, BaseRequest request) {
        emptyTV.setVisibility(View.VISIBLE);
    }
}
