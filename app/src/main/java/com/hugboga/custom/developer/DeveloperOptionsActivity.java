package com.hugboga.custom.developer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.hugboga.custom.R;
import com.hugboga.custom.action.ActionUtils;
import com.hugboga.custom.action.constants.ActionPageType;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.action.data.ActionGuideDetailBean;
import com.hugboga.custom.action.data.ActionOrderDetailBean;
import com.hugboga.custom.action.data.ActionSkuDetailBean;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.OnClick;

/**
 * Created by qingcha on 16/8/15.
 */
public class DeveloperOptionsActivity extends BaseActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_developer_options;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initDefaultTitleBar();
        fgTitle.setText("Developer Options");
    }

    @OnClick(R.id.developer_orderdetail_confirm_tv)
    public void intentOrderDetail() {
        EditText editText = (EditText) findViewById(R.id.developer_orderdetail_et);
        if (editText.getText() == null || editText.getText().toString() == null) {
            CommonUtils.showToast("订单编号不能为空");
        }
        ActionOrderDetailBean actionOrderDetailBean = new ActionOrderDetailBean();
        actionOrderDetailBean.orderNo = editText.getText().toString();
        ActionBean actionBean = new ActionBean(ActionPageType.ORDER_DETAIL, actionOrderDetailBean, "");
        ActionUtils.doAction(this, actionBean);
    }

    @OnClick(R.id.developer_guidedetail_confirm_tv)
    public void intentGuideDetail() {
        EditText editText = (EditText) findViewById(R.id.developer_guidedetail_et);
        if (editText.getText() == null || editText.getText().toString() == null) {
            CommonUtils.showToast("司导ID不能为空");
        }
        ActionGuideDetailBean actionGuideDetailBean = new ActionGuideDetailBean();
        actionGuideDetailBean.guideId = editText.getText().toString();
        ActionBean actionBean = new ActionBean(ActionPageType.GUIDE_DETAIL, actionGuideDetailBean, "");
        ActionUtils.doAction(this, actionBean);
    }

    @OnClick(R.id.developer_skudetail_confirm_tv)
    public void intentSkuDetail() {
        EditText editText = (EditText) findViewById(R.id.developer_skudetail_et);
        if (editText.getText() == null || editText.getText().toString() == null) {
            CommonUtils.showToast("商品ID不能为空");
        }
        ActionSkuDetailBean actionSkuDetailBean = new ActionSkuDetailBean();
        actionSkuDetailBean.goodsNo = editText.getText().toString();
        ActionBean actionBean = new ActionBean(ActionPageType.SKU_DETAIL, actionSkuDetailBean, "");
        ActionUtils.doAction(this, actionBean);
    }

    @OnClick(R.id.developer_element_tv)
    public void intentElementWeb() {
        Intent intent = new Intent(this, WebInfoActivity.class);
        final String url = "https://cdms2.huangbaoche.com/app/switchRoute.html?ak=" + UserSession.getUser().getAccessKey(this);
        intent.putExtra(WebInfoActivity.WEB_URL, url);
        intent.putExtra(WebInfoActivity.WEB_DEV, true);
        startActivity(intent);
    }
}
