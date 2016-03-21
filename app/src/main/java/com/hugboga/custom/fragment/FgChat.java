package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.ChatAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.parser.ParserChatList;
import com.hugboga.custom.data.request.RequestChatList;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 聊天页面
 * Created by admin on 2016/3/8.
 */

@ContentView(R.layout.fg_chat)
public class FgChat extends BaseFragment implements AdapterView.OnItemClickListener {

    @ViewInject(R.id.chat_logout)
    private View EmptyLayout;

    @ViewInject(R.id.chat_list)
    private View chatList;

    @ViewInject(R.id.chat_list)
    ListView listView;

    @ViewInject(R.id.chat_logout)
    View emptyView;

    private ChatAdapter adapter;

    @Override
    protected void initHeader() {
        fgTitle.setText("私聊");
    }

    @Override
    protected void initView() {
        adapter = new ChatAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected Callback.Cancelable requestData() {
        if(UserEntity.getUser().getUserToken(getActivity())==null){
            needHttpRequest = true;
            return null;
        }
        RequestChatList parserChatList = new RequestChatList(getActivity(),0,20);
        return  requestData(parserChatList);
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if(request instanceof RequestChatList){
            RequestChatList requestChatList = (RequestChatList)request;
            ArrayList dataList =  requestChatList.getData();
            adapter.setList(dataList);
            MLog.e("onDataRequestSucceed = "+dataList);
        }
    }
    @Event({R.id.login_btn})
    private void onClickView(View view){
        switch (R.id.login_btn){
            case R.id.login_btn:
                startFragment(new FgLogin());
                break;
        }
    }


    @Override
    public void onFragmentResult(Bundle bundle) {
        requestData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChatBean chatBean = adapter.getItem(position);
        MLog.e("chatBean"+chatBean);
        if (chatBean != null) {

            // 1：导游，2：用户，3：客服
            if ( "3".equals(chatBean.targetType)) {
                String titleJson =getChatInfo(chatBean.targetId,chatBean.targetAvatar);
                RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.APP_PUBLIC_SERVICE, chatBean.targetId, titleJson);
            } else if ("1".equals(chatBean.targetType)) {
                String titleJson = getChatInfo(chatBean.userId ,chatBean.targetAvatar);
                RongIM.getInstance().startPrivateChat(getActivity(), "Y" + chatBean.userId, titleJson);
            } else {
                MLog.e("目标用户不是客服，也不是司导");
            }
        }
    }

    private   String getChatInfo(String userId,String userAvatar){
        JSONObject obj = new JSONObject();
        try {
            obj.put("isChat",true);
            obj.put("userId",userId);
            obj.put("userAvatar",userAvatar);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
