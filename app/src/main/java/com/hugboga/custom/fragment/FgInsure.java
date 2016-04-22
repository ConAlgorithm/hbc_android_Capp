package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hugboga.custom.R;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dyt on 16/4/22.
 */
@ContentView(R.layout.fg_insure_list)
public class FgInsure extends BaseFragment {
    @Bind(R.id.list)
    ListView list;
    @Bind(R.id.emptyView)
    TextView emptyView;

    @Override
    protected void initHeader() {
        fgTitle.setText("常用投保人");
        fgRightBtn.setText("新增");
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFragment(new FgAddInsure());
            }
        });
    }

    @Override
    protected void initView() {
        list.setEmptyView(emptyView);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
