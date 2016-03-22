package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.constants.ResourcesConstants;
import com.hugboga.custom.utils.PhoneInfo;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.fg_servicer_center)
public class FgServicerCenter extends BaseFragment {


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fg_servicer_center, null);
//        return view;
//    }

//    @Override
//    protected String fragmentTitle() {
//        leftBtn.setImageResource(R.mipmap.top_back_black);
//        titleText.setTextColor(getResources().getColor(R.color.my_content_title_color));
//        return "客服中心";
//    }

//    @Override
//    protected void requestDate() {
//    }


    @Override
    protected void inflateContent() {
    }

//    @Override
//    protected void setClick(View view) {
////	view.findViewById(R.id.fg_home_btn).setOnClickListener(this);
//    }

    @Event({R.id.servicer_center_top_btn1, R.id.servicer_center_top_btn2, R.id.service_center_btn1, R.id.service_center_btn2, R.id.service_center_btn3, R.id.service_center_btn4, R.id.service_center_btn5})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.servicer_center_top_btn1:
                //境内客服
                PhoneInfo.CallDial(getActivity(), Constants.CALL_NUMBER_IN);
                break;
            case R.id.servicer_center_top_btn2:
                //境外客服
                PhoneInfo.CallDial(getActivity(), Constants.CALL_NUMBER_OUT);
                break;
            case R.id.service_center_btn1:
                //预订须知
                toWebInfo(ResourcesConstants.H5_NOTICE);
                break;
            case R.id.service_center_btn2:
                //服务承诺
                toWebInfo(ResourcesConstants.H5_SERVICE);
                break;
            case R.id.service_center_btn3:
                //订单取消规则
                toWebInfo(ResourcesConstants.H5_CANCEL);
                break;
            case R.id.service_center_btn4:
                //费用说明
                toWebInfo(ResourcesConstants.H5_PRICE);
                break;
            case R.id.service_center_btn5:
                //常见问题
                toWebInfo(ResourcesConstants.H5_PROBLEM);
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
        Bundle bundle = new Bundle();
        bundle.putString(FgWebInfo.WEB_URL, url);
        startFragment(new FgWebInfo(),bundle);
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
        fgTitle.setText("客服中心");
        fgTitle.setTextColor(getResources().getColor(R.color.my_content_title_color));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }


}
