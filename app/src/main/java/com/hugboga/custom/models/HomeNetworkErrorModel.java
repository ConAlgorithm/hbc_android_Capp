package com.hugboga.custom.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/15.
 */

public class HomeNetworkErrorModel extends EpoxyModelWithHolder {

    ReloadListener listener;

    public HomeNetworkErrorModel(ReloadListener listener) {
        this.listener = listener;
    }
    @Override
    protected EpoxyHolder createNewHolder() {
        return new NetWorkErrorHolder();
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        NetWorkErrorHolder netWorkErrorHolder = (NetWorkErrorHolder)holder;
        netWorkErrorHolder.reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.reload();
                }
            }
        });
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.no_network_layout;
    }

    class NetWorkErrorHolder extends EpoxyHolder{

        @Bind(R.id.home_empty_tv)
        View reload;

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ReloadListener{
        void reload();
    }
}
