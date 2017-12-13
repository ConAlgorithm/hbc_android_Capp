package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.FakeAIActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 目的地想怎么玩入口
 * Created by HONGBO on 2017/11/30 11:04.
 */

public class CityWhatModel extends EpoxyModelWithHolder<CityWhatModel.CityWhatVH> {

    Context mContext;

    public CityWhatModel(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected CityWhatModel.CityWhatVH createNewHolder() {
        return new CityWhatVH();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_item_what_layout;
    }

    @Override
    public void bind(CityWhatVH holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
    }

    /**
     * 是否显示此model
     * @param isShow
     */
    public void noteicModel(boolean isShow){
        if(isShow){
            show();
        }else{
            hide();
        }
    }

    public class CityWhatVH extends EpoxyHolder {

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.city_item_what_btn)
        public void onClick(View view) {
            // 这里开始咨询跳转到指定坐席的人工客服
            if (CommonUtils.isLogin(mContext, "推荐页面")) {//判断是否登陆
                UnicornServiceActivity.Params params = new UnicornServiceActivity.Params();
                params.sourceType = UnicornServiceActivity.SourceType.TYPE_CHARTERED;
                Intent intent = new Intent(mContext, UnicornServiceActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                mContext.startActivity(intent);
            }
        }
    }
}
