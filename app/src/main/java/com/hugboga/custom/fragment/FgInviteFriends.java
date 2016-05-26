package com.hugboga.custom.fragment;

import com.hugboga.custom.R;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by qingcha on 16/5/26.
 */
@ContentView(R.layout.fg_invite_friends)
public class FgInviteFriends extends BaseFragment {

    @ViewInject(R.id.invite_listview)
    ZListView listView;

    @Override
    protected void initHeader() {
        fgTitle.setText(getString(R.string.collect_guide_title));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }
}
