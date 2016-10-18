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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/8/6.
 */

public class CallBackActivity extends BaseActivity {
    @Bind(R.id.call_back_content)
    EditText contentEditText; //反馈内容
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.call_back_submit)
    Button callBackSubmit;

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCallBack) {
            RequestCallBack mParser = (RequestCallBack) request;
            CommonUtils.showToast("意见反馈提交成功");
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
                    CommonUtils.showToast("请填写内容后再提交");
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
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_call_back);
        ButterKnife.bind(this);
        initHeader();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        headerTitle.setText("意见反馈");
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}

