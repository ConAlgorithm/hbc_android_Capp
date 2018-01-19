package com.hugboga.custom.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FakeAIAdapter;
import com.hugboga.custom.data.bean.ai.ServiceType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.qiyukf.unicorn.api.ProductDetail;
import com.qiyukf.unicorn.api.ProductDetail.Builder;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ServiceQuestionBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.ai.AiRequestInfo;
import com.hugboga.custom.data.bean.ai.DuoDuoSaid;
import com.hugboga.custom.data.bean.ai.FakeAIArrayBean;
import com.hugboga.custom.data.bean.ai.FakeAIBean;
import com.hugboga.custom.data.bean.ai.FakeAIQuestionsBean;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.data.request.RaquestFakeAI;
import com.hugboga.custom.data.request.RequestFakeAIChange;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.ai.AiTagView;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import tk.hongbo.label.adapter.FilterAdapter;

import static com.hugboga.custom.activity.AiResultActivity.KEY_AI_RESULT;
import static com.hugboga.custom.activity.AiResultActivity.KEY_AI_RESULT_TITLE;
import static com.hugboga.custom.activity.AiResultActivity.KEY_AI_RESULT_TO_SERVICE;

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
    @BindView(R.id.button)
    Button button;
    private AiRequestInfo info;
    public static final int AIGETDATA_DURATION = 1;//天数
    public static final int AIGETDATA_ACCOMPANY = 2;//伴随
    private FakeAIAdapter fakeAIAdapter;
    private int buttonType; //判断客服状态
    private ArrayList<String> strings = new ArrayList<String>();//传递给客服的客户对话
    private String customServiceId;

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
        headerTitleCenter.setText(R.string.fake_ai_head);
        info = new AiRequestInfo();
        info.distinctId = SensorsDataAPI.sharedInstance(FakeAIActivity.this).getAnonymousId();
        fakeAIAdapter = new FakeAIAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fakeAIAdapter);
        //点击了退出软件盘调用
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

    }

    public void fakeData(List hotDestinationReqList) {

        if (hotDestinationReqList != null && hotDestinationReqList.size() > 0) {
            addScrollViewItem(hotDestinationReqList);
        }
    }

    private void addClientData(String data) {
        fakeAIAdapter.addMyselfMessage(data);
        requestSelf(null, data);
    }

    @OnClick({R.id.header_left_btn, R.id.edit_text, R.id.button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.edit_text:
                handler.sendEmptyMessageDelayed(0, 500);
                break;
            case R.id.button:
                Intent intent = null;
                switch (buttonType) {
                    case 1://跳转客服对话
                        //ArrayList<String> strings   携带跳转客服的参数
                        if (CommonUtils.isLogin(FakeAIActivity.this, "AI界面")) {//判断是否登陆
                            intent = new Intent(FakeAIActivity.this, UnicornServiceActivity.class);
                            intent.putExtra(Constants.PARAMS_DATA, getParams());
                            intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                            startActivity(intent);
                            SensorsUtils.onAppClick(getEventSource(), getEventSource(), "和旅行小管家继续沟通", getIntentSource());
                            finish();
                        }
                        break;
                    case 2://跳转填单页
                        intent = new Intent(FakeAIActivity.this, TravelPurposeFormActivity.class);
                        if (info.userSaidList != null && info.userSaidList.size() >= 2) {
                            intent.putExtra("cityName", info.userSaidList.get(0).saidContent);
                        }
                        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                        startActivity(intent);
                        finish();
                        break;
                }


                break;
        }
    }

    private UnicornServiceActivity.Params getParams() {  //跳转到客服的 卡片参数
        UnicornServiceActivity.Params params = new UnicornServiceActivity.Params();
        params.sourceType = UnicornServiceActivity.SourceType.TYPE_AI_RESULT;
        if (customServiceId != null) {
            params.groupId = Integer.parseInt(customServiceId);
        }
        if (strings != null && strings.size() > 0) {
            params.aiChatRecords = strings.toString();
        }
        ProductDetail.Builder builder = new ProductDetail.Builder();

        builder.setUrl(BuildConfig.SHARE_BASE_URL_4 + "/app/jiaAIpop.html?id=" + info.askDuoDuoSessionID);
        builder.setTitle(getResources().getString(R.string.ai_requirements));
        builder.setDesc(getResources().getString(R.string.ai_details));
        builder.setPicture("https://hbcdn.huangbaoche.com/im/im_default.png");
        builder.setShow(1);
        builder.setAlwaysSend(true);
        params.productDetail = builder.build();
        return params;
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RaquestFakeAI) {
            FakeAIBean dataList = (FakeAIBean) request.getData();
            if (dataList.askDuoDuoSessionID != null) {
                info.askDuoDuoSessionID = dataList.askDuoDuoSessionID;
            }
            if (dataList.userSaidList != null) {
                info.userSaidList = dataList.userSaidList;
            }
            if (dataList != null) {
                initTipMessage(dataList);
                fakeData(dataList.hotDestinationReqList);
                initServiceMessage(dataList.duoDuoSaid);
            }
            if (dataList.serviceTypeReqList != null) {
                Message message = handler.obtainMessage();
                message.obj = dataList.serviceTypeReqList;
                handler.sendMessageDelayed(message, 150);
            }

        } else if (request instanceof RequestFakeAIChange) {
            FakeAIQuestionsBean data = ((RequestFakeAIChange) request).getData();
            fakeAIAdapter.settingCardClick(true);
            if (data.recommendationDestinationHome != null) {
                //有推荐结果
                initServiceMessage(data.duoDuoSaid);
                Message message = handler.obtainMessage();
                message.obj = data;
                handler.sendMessageDelayed(message, 2000);
            } else {
                info.setData(data);
                if (data.customServiceStatus != null) {
                    skipDialogue(data.customServiceStatus);
                } else if (data.durationReqList != null && data.durationReqList.size() != 0) {
                    fakeData(data.durationReqList);
                    editText.setHint(R.string.fake_ai_hint_two);
                } else if (data.accompanyReqList != null && data.accompanyReqList.size() != 0) {
                    editText.setHint(R.string.fake_ai_hint_three);
                    fakeData(data.accompanyReqList);
                } else if (data.hotDestinationReqList != null && data.hotDestinationReqList.size() != 0) {
                    fakeData(data.hotDestinationReqList);
                }
                if (data.userSaidList != null) {
                    strings.clear();
                    for (int i = 0; i < data.userSaidList.size(); i++) {
                        strings.add(data.userSaidList.get(i).saidContent);
                    }
                }
                if (data.customServiceId != null) {
                    customServiceId = data.customServiceId;
                }
                if ("1".equals(data.chooseServiceTypeOption)) {
                    Message message = handler.obtainMessage();
                    message.obj = data.chooseServiceTypeList;
                    handler.sendMessage(message);
                    fakeAIAdapter.clearWaitView();
                }
                initServiceMessage(data.duoDuoSaid);
            }
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
    private void initServiceMessage(final List<DuoDuoSaid> duoDuoSaid) {
        if (duoDuoSaid != null && duoDuoSaid.size() > 0) {
            new BuildMessageTask().execute(duoDuoSaid);
        }
    }

    /**
     * 收到滚动到底部消息
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof FakeAIQuestionsBean) {
                Intent intent = new Intent(FakeAIActivity.this, AiResultActivity.class);
                DestinationHomeVo destinationHomeVo = (DestinationHomeVo) ((FakeAIQuestionsBean) msg.obj).recommendationDestinationHome;
                String recommendationInfo = ((FakeAIQuestionsBean) msg.obj).recommendationGoodsInfo;
                intent.putExtra(KEY_AI_RESULT, destinationHomeVo);
                intent.putExtra(KEY_AI_RESULT_TITLE, recommendationInfo);
                intent.putExtra(KEY_AI_RESULT_TO_SERVICE, getParams());
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);
                finish();
            } else if (msg.obj instanceof List) {
                List<ServiceType> serviceTypes = (List) msg.obj;
                fakeAIAdapter.addServerCard(serviceTypes);
            } else if (msg.obj instanceof DuoDuoSaid) {
                fakeAIAdapter.addServerMessage(((DuoDuoSaid) msg.obj).questionValue);
            }
            recyclerView.scrollToPosition(fakeAIAdapter.getItemCount() - 1);
        }
    };

    class BuildMessageTask extends AsyncTask<Object, DuoDuoSaid, DuoDuoSaid> {
        @Override
        protected DuoDuoSaid doInBackground(Object[] objects) {
            if (objects.length == 0) {
                return null;
            }
            List<DuoDuoSaid> duoDuoSaid = (List<DuoDuoSaid>) objects[0];
            for (DuoDuoSaid bean : duoDuoSaid) {
                Message message = handler.obtainMessage();
                message.obj = bean;
                handler.sendMessage(message);
                if (bean.questionId != null) {
                    info.questionId = bean.questionId;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return duoDuoSaid.get(duoDuoSaid.size() - 1);
        }

        @Override
        protected void onPostExecute(DuoDuoSaid o) {
            super.onPostExecute(o);
            editTextExist();
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        fakeAIAdapter.addServerMessage(getResources().getString(R.string.fake_ai_requestrrror));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void addScrollViewItem(final List list) {

        for (int i = 0; i < list.size(); i++) {
            AiTagView view = new AiTagView(this);
            view.init(list.get(i));
            view.setOnClickListener(new AiTagView.OnClickListener() {
                @Override
                public void click(AiTagView view, FakeAIArrayBean bean, int type) {
                    scrollViewButtonClick(bean, type);
                }
            });
            scrollViewLinearLayout.addView(view);
        }

    }

    private void scrollViewButtonClick(FakeAIArrayBean bean, int type) {
        fakeAIAdapter.addMyselfMessage(bean.destinationName);
        editTextOver();
        if (type == 0) {
            requestSelf(bean, null);
        } else if (type == AIGETDATA_DURATION) {
            info.durationOptId = bean.destinationId;//此参数为时间ID
            requestSelf(null, null);
        } else if (type == AIGETDATA_ACCOMPANY) {
            info.accompanyOptId = bean.destinationId;//此参数为伴随ID
            requestSelf(null, null);
        }
    }

    /**
     * 禁止输入
     */
    private void editTextOver() {
        scrollViewLinearLayout.removeAllViews();
        horizontalScrollView.setVisibility(View.INVISIBLE);
        editText.setText("");
        editText.setHint("");
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            collapseSoftInputMethod(editText);
        }
    }

    /**
     * 可以输入
     */

    private void editTextExist() {
        horizontalScrollView.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(horizontalScrollView, "translationX", UIUtils.getScreenHeight(), 0);
        animator.setDuration(1000);
        animator.start();
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.requestFocus();

    }

    /**
     * 点击包车玩法卡片
     */
    public void clickCharteredBus() {
        editTextOver();
        info.serviceTypeId = "3";
        requestSelf(null, null);
    }

    private void skipDialogue(String str) {
        String buttonContent;
        if (str.equals("1"))
            buttonContent = getResources().getString(R.string.fake_ai_buttoncontent_one);
        else
            buttonContent = getResources().getString(R.string.fake_ai_buttoncontent_two);
        buttonType = Integer.parseInt(str);
        button.setText(buttonContent);
        editText.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
        fakeAIAdapter.clearWaitView();
        handler.sendEmptyMessageDelayed(1, 200);

    }

    /**
     * 初始化界面信息
     */
    private void requestHotSearch() {
        RaquestFakeAI requestHotSearch = new RaquestFakeAI(this, info.distinctId);
        HttpRequestUtils.request(this, requestHotSearch, this, false);
    }

    /**
     * 为自己的问题询问答案
     */
    private void requestSelf(FakeAIArrayBean bean, String str) {
        handler.sendEmptyMessage(0);
        info.userWant = null;
        if (bean != null) {
            info.destinationId = String.valueOf(bean.destinationId);
            info.destinationType = String.valueOf(bean.destinationType);
            info.destinationName = bean.destinationName;
        }
        if (!TextUtils.isEmpty(str)) {
            info.userWant = str;
        }
        fakeAIAdapter.settingCardClick(false);
        RequestFakeAIChange requsetFakeAIChange = new RequestFakeAIChange(this, info);
        HttpRequestUtils.request(this, requsetFakeAIChange, this, false);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//点击的是返回键
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {//按键的按下事件
                finish();
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public String getEventSource() {
        return "AI对话";
    }
}