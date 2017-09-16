package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;

import butterknife.Bind;
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

    @OnClick({R.id.service_center_btn1, R.id.service_center_btn2, R.id.service_center_btn3, R.id.service_center_btn4, R.id.service_center_btn5, R.id.service_insurance_btn6})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.service_center_btn1:
                //预订须知
                toWebInfo(UrlLibs.H5_NOTICE_V2_2);
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
                toWebInfo(UrlLibs.H5_PRICE_V2_2);
                break;
            case R.id.service_center_btn5:
                //常见问题
                toWebInfo(UrlLibs.H5_PROBLEM);
                break;
            case R.id.service_insurance_btn6:
                //保险说明
                toWebInfo(UrlLibs.H5_INSURANCE);
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
        intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
        startActivity(intent);
    }


    protected void initHeader() {
        headerTitle.setText(R.string.servicer_title);
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(38), UIUtils.dip2px(38));
        headerRightImageParams.rightMargin = UIUtils.dip2px(18);
        headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        headerRightBtn.setLayoutParams(headerRightImageParams);
        headerRightBtn.setPadding(0,0,0,0);
        headerRightBtn.setImageResource(R.mipmap.topbar_cs);
        headerRightBtn.setVisibility(View.VISIBLE);
        headerRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogUtil.getInstance(activity).showDefaultServiceDialog(activity,getEventSource());
                CommonUtils.csDialog(activity,null,null,null,UnicornServiceActivity.SourceType.TYPE_DEFAULT,getEventSource());
            }
        });


    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_servicer_center;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initHeader();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public String getEventSource() {
        return "服务规则";
    }
}
