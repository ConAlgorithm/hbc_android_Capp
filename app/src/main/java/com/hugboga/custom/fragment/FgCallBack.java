package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.request.RequestCallBack;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fg_call_back)
public class FgCallBack extends BaseFragment {

    @ViewInject(R.id.call_back_content)
    EditText contentEditText; //反馈内容

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestCallBack) {
            RequestCallBack mParser = (RequestCallBack) request;
            showTip("意见反馈提交成功");
            finish();
        }
    }

    @Override
    protected void inflateContent() {
    }

    @Event({R.id.call_back_submit})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.call_back_submit:
                //提交意见
                String content = contentEditText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    showTip("请填写内容后再提交");
                    contentEditText.requestFocus();
                    return;
                }

                RequestCallBack requestCallBack = new RequestCallBack(getActivity(), content);
                requestData(requestCallBack);
                break;
            default:
                break;
        }
    }

    @Override
    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
    }

    @Override
    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        fgTitle.setText("意见反馈");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

}
