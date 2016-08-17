package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.PhoneInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hugboga.custom.data.net.UrlLibs.H5_CANCEL;

/**
 * Created on 16/8/6.
 */

public class ServicerCenterActivity extends BaseActivity {
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.servicer_center_top_btn1)
    TextView servicerCenterTopBtn1;
    @Bind(R.id.servicer_center_top_btn2)
    TextView servicerCenterTopBtn2;
    @Bind(R.id.servicer_center_top)
    LinearLayout servicerCenterTop;
    @Bind(R.id.servicer_center_line)
    ImageView servicerCenterLine;
    @Bind(R.id.service_center_btn1)
    TextView serviceCenterBtn1;
    @Bind(R.id.service_center_btn2)
    TextView serviceCenterBtn2;
    @Bind(R.id.service_center_btn3)
    TextView serviceCenterBtn3;
    @Bind(R.id.service_center_btn4)
    TextView serviceCenterBtn4;
    @Bind(R.id.service_center_btn5)
    TextView serviceCenterBtn5;

    @OnClick({R.id.servicer_center_top_btn1, R.id.servicer_center_top_btn2, R.id.service_center_btn1, R.id.service_center_btn2, R.id.service_center_btn3, R.id.service_center_btn4, R.id.service_center_btn5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.servicer_center_top_btn1:
                //境内客服
                PhoneInfo.CallDial(activity, Constants.CALL_NUMBER_IN);
                break;
            case R.id.servicer_center_top_btn2:
                //境外客服
                PhoneInfo.CallDial(activity, Constants.CALL_NUMBER_OUT);
                break;
            case R.id.service_center_btn1:
                //预订须知
                toWebInfo(UrlLibs.H5_NOTICE);
                break;
            case R.id.service_center_btn2:
                //服务承诺
                toWebInfo(UrlLibs.H5_SERVICE);
                break;
            case R.id.service_center_btn3:
                //订单取消规则
                toWebInfo(H5_CANCEL);
                break;
            case R.id.service_center_btn4:
                //费用说明
                toWebInfo(UrlLibs.H5_PRICE);
                break;
            case R.id.service_center_btn5:
                //常见问题
                Intent intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_PROBLEM);
                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    /**
     * 打开网页
     *
     * @param url
     */
    private void toWebInfo(String url) {
        Intent intent = new Intent(activity, WebInfoActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, url);
        startActivity(intent);
    }


    protected void initHeader() {
        headerTitle.setText("服务规则");
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_servicer_center);
        ButterKnife.bind(this);
        initHeader();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}