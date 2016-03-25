package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.ChatAdapter;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestChatList;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 聊天页面
 * Created by admin on 2016/3/8.
 */

@ContentView(R.layout.fg_chat)
public class FgChat extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.chat_logout)
    private View emptyLayout;

    @ViewInject(R.id.chat_list)
    private View chatList;
    @ViewInject(R.id.header_left_btn)
    private ImageView leftBtn;

    @ViewInject(R.id.chat_list)
    ListView listView;

    @ViewInject(R.id.chat_logout)
    View emptyView;

    private ChatAdapter adapter;

    @Override
    protected void initHeader() {
        fgTitle.setText("私聊");
        leftBtn.setImageResource(R.mipmap.header_menu);
        leftBtn.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        adapter = new ChatAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
    }

    @Override
    protected Callback.Cancelable requestData() {
        if (UserEntity.getUser().getUserToken(getActivity()) == null) {
            needHttpRequest = true;
            return null;
        }
        RequestChatList parserChatList = new RequestChatList(getActivity(), 0, 20);
        return requestData(parserChatList);
    }

    @Override
    protected void inflateContent() {
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestChatList) {
            RequestChatList requestChatList = (RequestChatList) request;
            ArrayList dataList = requestChatList.getData();
            adapter.setList(dataList);
            MLog.e("onDataRequestSucceed = " + dataList);
        }
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
        requestData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                MLog.e("left  " + view);
                ((MainActivity) getActivity()).openDrawer();
                break;
        }
    }
}
