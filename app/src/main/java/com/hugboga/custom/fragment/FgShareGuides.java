package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.EvaluateData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;


import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/7/15.
 */
@ContentView(R.layout.fg_share_guides)
public class FgShareGuides extends BaseFragment{

    @Bind(R.id.share_guides_avatar_iv)
    PolygonImageView avatarIV;
    @Bind(R.id.share_guides_description_tv)
    TextView descriptionTV;

    private Params params;
    private boolean shareSucceed = false;

    public static class Params implements Serializable {
        EvaluateData evaluateData;
        String orderNo;
        String guideAvatar;
    }

    public static FgShareGuides newInstance(Params params) {
        FgShareGuides fragment = new FgShareGuides();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS_DATA, params);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shareSucceed) {
            finish();
        }
    }

    @Override
    protected void initHeader(Bundle savedInstanceState) {
        super.initHeader(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                params = (Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        shareSucceed = false;
        fgTitle.setText(getString(R.string.share_guides_title));
        if (fgLeftBtn != null) {
            int padding = UIUtils.dip2px(20);
            ImageView _fgLeftBtn = (ImageView) fgLeftBtn;
            _fgLeftBtn.setImageResource(R.mipmap.top_white_close);
            _fgLeftBtn.setPadding(padding, padding, padding, padding);
            _fgLeftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (params == null || params.evaluateData == null) {
            finish();
        }
        if (TextUtils.isEmpty(params.guideAvatar)) {
            avatarIV.setImageResource(R.mipmap.journey_head_portrait);
        } else {
            Tools.showImage(avatarIV, params.guideAvatar);
        }
        descriptionTV.setText(getString(R.string.share_guides_description_2, params.evaluateData.commentTipParam1));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initHeader() {

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

    @OnClick({R.id.share_guides_unwilling_tv, R.id.share_guides_share_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_guides_unwilling_tv:
                finish();
                break;
            case R.id.share_guides_share_tv:
                EvaluateData evaluateData = params.evaluateData;
                if (evaluateData == null) {
                    break;
                }
                String shareUrl = CommonUtils.getBaseUrl(evaluateData.wechatShareUrl) + "orderNo=" +  params.orderNo + "&userId=" + UserEntity.getUser().getUserId(getContext());
                CommonUtils.shareDialog(getContext()
                        , evaluateData.wechatShareHeadSrc
                        , evaluateData.wechatShareTitle
                        , evaluateData.wechatShareContent
                        , shareUrl);
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        if (action.type == EventType.WECHAT_SHARE_SUCCEED) {
            shareSucceed = true;
        }
    }
}
