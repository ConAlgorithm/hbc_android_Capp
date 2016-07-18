package com.hugboga.custom.fragment;

import android.os.Bundle;
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
import com.hugboga.custom.utils.UIUtils;
import net.grobas.view.PolygonImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;


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

    private EvaluateData evaluateData;
    private String orderNo;
    private boolean shareSucceed = false;

    public static FgShareGuides newInstance(EvaluateData _data, String _orderNo) {
        FgShareGuides fragment = new FgShareGuides();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS_DATA, _data);
        bundle.putString(Constants.PARAMS_ID, _orderNo);
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
            evaluateData = (EvaluateData) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
            orderNo = savedInstanceState.getString(Constants.PARAMS_ID);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                evaluateData = (EvaluateData) bundle.getSerializable(Constants.PARAMS_DATA);
                orderNo = bundle.getString(Constants.PARAMS_ID);
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
        if (evaluateData == null) {
            finish();
        }
        descriptionTV.setText(getString(R.string.share_guides_description_2, evaluateData.commentTipParam1));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (evaluateData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, evaluateData);
            outState.putString(Constants.PARAMS_ID, orderNo);
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
                if (evaluateData == null) {
                    break;
                }
                String shareUrl = CommonUtils.getBaseUrl(evaluateData.wechatShareUrl) + "orderNo=" + orderNo + "&userId=" + UserEntity.getUser().getUserId(getContext());
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
