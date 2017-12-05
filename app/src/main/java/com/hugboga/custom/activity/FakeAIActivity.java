package com.hugboga.custom.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FakeAIAdapter;
import com.hugboga.custom.data.bean.ai.DuoDuoSaid;
import com.hugboga.custom.data.bean.ai.FakeAIArrayBean;
import com.hugboga.custom.data.bean.ai.FakeAIBean;
import com.hugboga.custom.data.bean.ai.FakeAIQuestionsBean;
import com.hugboga.custom.data.bean.ai.AiRequestInfo;
import com.hugboga.custom.data.request.RaqustFakeAI;
import com.hugboga.custom.data.request.RequsetFakeAIChange;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.ai.AiTagView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/28.
 */

public class FakeAIActivity extends BaseActivity {

    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.header_right_btn)
    ImageView headerRightBtn;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.header_right_txt)
    TextView headerRightTxt;
    @BindView(R.id.view_bottom)
    View viewBottom;
    @BindView(R.id.header_title_center)
    TextView headerTitleCenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.scrollView_LinearLayout)
    LinearLayout scrollViewLinearLayout;
    @BindView(R.id.horizontalScrollView)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.edit_text)
    EditText editText;

    private FakeAIAdapter fakeAIAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_fake_ai;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initView();
        requestHotSearch();
    }

    private void initView() {
        headerTitleCenter.setText("旅行小管家");
        fakeAIAdapter = new FakeAIAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fakeAIAdapter);
        //第一次点击输入EditText时调用
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });
        //点击了退出软件盘调用。。。没效果
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_BACK)) {
                    collapseSoftInputMethod(editText);
                    return true;
                }
                return false;
            }
        });
        //软件盘点击确定监听
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (editText.getText().toString().equals("") || editText.getText().toString().equals(null))
                    return false;
                collapseSoftInputMethod(editText);
                addClientData(editText.getText().toString());
                editTextOver();
                return true;
            }
        });
        //handler.sendEmptyMessageDelayed(0,3000);
    }

    public void fakeData(List<FakeAIArrayBean> hotDestinationReqList) {
        editTextExist();
        if (hotDestinationReqList != null && hotDestinationReqList.size() > 0) {
            addScrollViewItem(hotDestinationReqList);
        }
    }

    private void addClientData(String data) {
        fakeAIAdapter.addMyselfMessage(data);
        requestSelf(null, data);
    }

    @OnClick({R.id.header_left_btn, R.id.edit_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.edit_text:
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RaqustFakeAI) {
            FakeAIBean dataList = (FakeAIBean) request.getData();
            if (dataList != null) {
                initTipMessage(dataList);
                fakeData(dataList.hotDestinationReqList);
                initServiceMessage(dataList.duoDuoSaid);
            }
        } else if (request instanceof RequsetFakeAIChange) {
            FakeAIQuestionsBean data = ((RequsetFakeAIChange) request).getData();
            //TODO 问答回复，稍后做处理
//            fakeData(data.durationReqList);
            initServiceMessage(data.duoDuoSaid);
        }
    }

    /**
     * 初始化界面信息
     */
    private void initTipMessage(FakeAIBean dataList) {
        fakeAIAdapter.resetHeaderInfo(dataList.hiList); //设置头部信息
    }

    /**
     * 显示服务端问题
     *
     * @param duoDuoSaid
     */
    private void initServiceMessage(List<DuoDuoSaid> duoDuoSaid) {
        if (duoDuoSaid != null && duoDuoSaid.size() > 0) {
            for (DuoDuoSaid bean : duoDuoSaid) {
                fakeAIAdapter.addServerMessage(bean.questionValue);
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void addScrollViewItem(final List<FakeAIArrayBean> list) {
        scrollViewLinearLayout.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            AiTagView view = new AiTagView(this);
            view.init(list.get(i));
            view.setOnClickListener(new AiTagView.OnClickListener() {
                @Override
                public void click(AiTagView view, FakeAIArrayBean bean) {
                    scrollViewButtonClick(bean);
                }
            });
            scrollViewLinearLayout.addView(view);
        }
    }

    private void scrollViewButtonClick(FakeAIArrayBean bean) {
        fakeAIAdapter.addMyselfMessage(bean.destinationName);
        editTextOver();
        requestSelf(bean, null);
    }

    private void editTextOver() {
        horizontalScrollView.setVisibility(View.INVISIBLE);
        editText.setFocusable(false);
        editText.setText("");
        editText.setBackground(getResources().getDrawable(R.drawable.shape_rounded_ai_edit_over));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            collapseSoftInputMethod(editText);
        }
    }

    private void editTextExist() {
        editText.setFocusable(true);
        editText.setBackground(getResources().getDrawable(R.drawable.shape_rounded_ai_edit));
    }

    /**
     * 初始化界面信息
     */
    private void requestHotSearch() {
        RaqustFakeAI requestHotSearch = new RaqustFakeAI(this);
        HttpRequestUtils.request(this, requestHotSearch, this, false);
    }

    /**
     * 为自己的问题询问答案
     */
    private void requestSelf(FakeAIArrayBean bean, String str) {
        AiRequestInfo info = new AiRequestInfo();
        if (bean != null) {
            info.destinationId = String.valueOf(bean.destinationId);
            info.destinationType = String.valueOf(bean.destinationType);
            info.destinationName = bean.destinationName;
            info.guideCount = String.valueOf(bean.guideCount);
        }
        if (!TextUtils.isEmpty(str)) {
            info.userWant = str;
        }
        RequsetFakeAIChange requsetFakeAIChange = new RequsetFakeAIChange(this, info);
        HttpRequestUtils.request(this, requsetFakeAIChange, this, false);
    }

}
