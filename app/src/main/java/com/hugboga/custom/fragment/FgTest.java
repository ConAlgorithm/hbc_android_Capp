package com.hugboga.custom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.request.RequestTest;
import com.hugboga.custom.data.request.RequestTest2;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_fg_test)
public class FgTest extends BaseFragment implements View.OnTouchListener {

    public static final String KEY_NAME="name";

    @ViewInject(R.id.fg_test_text)
    private TextView text;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    protected void initHeader() {
    }

    @Override
    protected void initView() {
        String tmpName = getArguments().getString(KEY_NAME);
        if(tmpName!=null)
        text.setText(tmpName);
    }

    @Override
    protected void requestDate() {

    }


    @Event(value = {R.id.fg_test_btn,R.id.fg_test_btn2},type = View.OnClickListener.class)
    private void onClickView(View view){
        LogUtil.e("onClickView "+view);
        switch (view.getId()){
            case R.id.fg_test_btn:
                requestTest();
                break;
            case R.id.fg_test_btn2:
                requestTest2();
                break;


        }
    }


    private void requestTest() {
        RequestTest request = new RequestTest();
        HttpRequestUtils.request(request, this);

    }
    private void requestTest2() {
        RequestTest2 request = new RequestTest2();
        HttpRequestUtils.request(request, this);
    }
    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if(request instanceof  RequestTest){
            RequestTest mRequest = (RequestTest)request;
          Toast.makeText(getActivity(), mRequest.getData().toString(), Toast.LENGTH_LONG).show();
        }else if(request instanceof  RequestTest2){
            RequestTest2 mRequest = (RequestTest2)request;
          Toast.makeText(getActivity(), mRequest.getData().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
