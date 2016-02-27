package com.hugboga.custom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.TestBean;
import com.hugboga.custom.data.request.RequestTest;
import com.hugboga.custom.data.request.RequestTest2;
import com.hugboga.custom.data.request.RequestTest3;

import org.xutils.common.Callback;
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
    protected Callback.Cancelable requestData() {
        return null;
    }


    @Event(value = {R.id.fg_test_btn,R.id.fg_test_btn2,R.id.fg_test_btn3},type = View.OnClickListener.class)
    private void onClickView(View view){
        LogUtil.e("onClickView " + view);
        switch (view.getId()){
            case R.id.fg_test_btn:
                RequestTest request = new RequestTest();
                requestData(request);
                break;
            case R.id.fg_test_btn2:
                RequestTest2 request2 = new RequestTest2();
                requestData(request2);
                break;
            case R.id.fg_test_btn3:
                RequestTest3 request3 = new RequestTest3();
                requestData(request3);
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if(request instanceof  RequestTest){
            RequestTest mRequest = (RequestTest)request;
            TestBean bean =mRequest.getData();
          Toast.makeText(getActivity(), mRequest.getData().toString(), Toast.LENGTH_LONG).show();
        }else if(request instanceof  RequestTest2){
            RequestTest2 mRequest = (RequestTest2)request;
          Toast.makeText(getActivity(), mRequest.getData().toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }
}
