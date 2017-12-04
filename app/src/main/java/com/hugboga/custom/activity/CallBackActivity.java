package com.hugboga.custom.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.request.RequestCallBack;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 16/8/6.
 */

public class CallBackActivity extends BaseActivity {
    @BindView(R.id.call_back_content)
    EditText contentEditText; //反馈内容
    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.header_right_btn)
    ImageView headerRightBtn;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.header_right_txt)
    TextView headerRightTxt;
    @BindView(R.id.call_back_submit)
    Button callBackSubmit;

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCallBack) {
            RequestCallBack mParser = (RequestCallBack) request;
            CommonUtils.showToast(R.string.callback_submit_succesd);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideInputMethod(contentEditText);
    }

    @OnClick({R.id.call_back_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.call_back_submit:
                //提交意见
                String content = contentEditText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    CommonUtils.showToast(R.string.callback_inupt_check);
                    contentEditText.requestFocus();
                    return;
                }

                RequestCallBack requestCallBack = new RequestCallBack(activity, content);
                requestData(requestCallBack);
                break;
            default:
                break;
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_call_back;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initHeader();
    }


    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        headerTitle.setText(R.string.callback_title);
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}

