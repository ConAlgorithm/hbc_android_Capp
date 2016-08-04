package com.hugboga.custom.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/8/4.
 */
public class ShareGuidesActivity extends BaseActivity{

    @Bind(R.id.share_guides_avatar_iv)
    PolygonImageView avatarIV;
    @Bind(R.id.share_guides_description_tv)
    TextView descriptionTV;

    private Params params;
    private boolean shareSucceed = false;

    public static class Params implements Serializable {
        public EvaluateData evaluateData;
        public String orderNo;
        public String guideAvatar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        setContentView(R.layout.fg_share_guides);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shareSucceed) {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    private void initView() {
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
                String shareUrl = CommonUtils.getBaseUrl(evaluateData.wechatShareUrl) + "orderNo=" +  params.orderNo + "&userId=" + UserEntity.getUser().getUserId(this);
                CommonUtils.shareDialog(this
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
