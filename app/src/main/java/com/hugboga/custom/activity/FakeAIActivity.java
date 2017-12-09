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
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FakeAIAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ServiceQuestionBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.ai.AiRequestInfo;
import com.hugboga.custom.data.bean.ai.DuoDuoSaid;
import com.hugboga.custom.data.bean.ai.FakeAIArrayBean;
import com.hugboga.custom.data.bean.ai.FakeAIBean;
import com.hugboga.custom.data.bean.ai.FakeAIQuestionsBean;
import com.hugboga.custom.data.request.RaquestFakeAI;
import com.hugboga.custom.data.request.RequestFakeAIChange;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.ai.AiTagView;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

import static com.hugboga.custom.activity.AiResultActivity.KEY_AI_RESULT;

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


    @Override
    public int getContentViewId() {
        return R.layout.activity_fake_ai;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        info = new AiRequestInfo();
        initView();
        requestHotSearch();
    }

    private void initView() {
        headerTitleCenter.setText(R.string.fake_ai_head);
        fakeAIAdapter = new FakeAIAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(fakeAIAdapter);
        //第一次点击输入EditText时调用
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                handler.sendEmptyMessageDelayed(0, 1000);
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

    }

    public void fakeData(List hotDestinationReqList) {

        if (hotDestinationReqList.size() > 0) {
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
                handler.sendEmptyMessage(0);
                break;
            case R.id.button:
                Intent intent = null;
                switch (buttonType) {
                    case 1://跳转客服对话
                        UnicornServiceActivity.Params params = new UnicornServiceActivity.Params();
                        params.sourceType = UnicornServiceActivity.SourceType.TYPE_CHAT_LIST;
                        ServiceQuestionBean.QuestionItem questionItem = new ServiceQuestionBean.QuestionItem();
                        questionItem.customRole = SharedPre.getInteger(UserEntity.getUser().getUserId(MyApplication.getAppContext()), SharedPre.QY_GROUP_ID, 0);
                        params.questionItem = questionItem;
                        intent = new Intent(FakeAIActivity.this, UnicornServiceActivity.class);
                        intent.putExtra(Constants.PARAMS_DATA, params);
                        break;
                    case 2://跳转填单页
                        intent = new Intent(FakeAIActivity.this, TravelPurposeFormActivity.class);
                        if (info.userSaidList != null && info.userSaidList.size() >= 2) {
                            intent.putExtra("cityName", info.userSaidList.get(0).saidContent);
                        }
                        break;
                }
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RaquestFakeAI) {
            FakeAIBean dataList = (FakeAIBean) request.getData();
            if (dataList.askDuoDuoSessionID != null) {
                info.askDuoDuoSessionID = dataList.askDuoDuoSessionID;
            }
            if (dataList != null) {
                initTipMessage(dataList);
                fakeData(dataList.hotDestinationReqList);
                initServiceMessage(dataList.duoDuoSaid);
            }
        } else if (request instanceof RequestFakeAIChange) {
            FakeAIQuestionsBean data = ((RequestFakeAIChange) request).getData();
            //TODO 问答回复，稍后做处理
            if (data.recommendationDestinationHome != null) {
                //有推荐结果
                initServiceMessage(data.duoDuoSaid);
                Message message = handler.obtainMessage();
                message.obj =data;
                handler.sendMessageDelayed(message,1000);
            } else {

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
                    info.userSaidList = data.userSaidList;
                }
                if (data.chooseDestinationId != null) {
                    info.destinationId = data.chooseDestinationId;
                }
                if (data.chooseDestinationType != null) {
                    info.destinationType = data.chooseDestinationType;
                }
                if (data.chooseDurationId != null) {
                    info.durationOptId = data.chooseDurationId;
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
            if(msg.obj instanceof FakeAIQuestionsBean){
                Intent intent = new Intent(FakeAIActivity.this, AiResultActivity.class);
                intent.putExtra(KEY_AI_RESULT, ((FakeAIQuestionsBean)msg.obj).recommendationDestinationHome);
                startActivity(intent);
                finish();
            }
            recyclerView.scrollToPosition(fakeAIAdapter.getItemCount() - 1);
        }
    };

    class BuildMessageTask extends AsyncTask<Object, Object, DuoDuoSaid> {

        @Override
        protected DuoDuoSaid doInBackground(Object[] objects) {
            if (objects.length == 0) {
                return null;
            }
            List<DuoDuoSaid> duoDuoSaid = (List<DuoDuoSaid>) objects[0];
            for (DuoDuoSaid bean : duoDuoSaid) {
                fakeAIAdapter.addServerMessage(bean.questionValue);
                handler.sendEmptyMessage(0);
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
            if (o.questionId!=null){
                info.questionId = o.questionId;
            }
            editTextExist();
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
            scrollViewLinearLayout. addView(view);
        }

    }

    private void scrollViewButtonClick(FakeAIArrayBean bean, int type) {
        fakeAIAdapter.addMyselfMessage(bean.destinationName);
        editTextOver();
        if (type == 0) {
            requestSelf(bean, null);
        } else if (type == AIGETDATA_DURATION) {
            info.durationOptId = bean.destinationId;//此参数为时间ID
            requestSelf(null, bean.destinationName);
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


    private void skipDialogue(String str) {
        String buttonContent;
        if (str.equals("1"))
            buttonContent = getResources().getString(R.string.fake_ai_buttoncontent_one);
        else
            buttonContent =  getResources().getString(R.string.fake_ai_buttoncontent_two);
        buttonType = Integer.parseInt(str);
        button.setText(buttonContent);
        editText.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化界面信息
     */
    private void requestHotSearch() {
        RaquestFakeAI requestHotSearch = new RaquestFakeAI(this);
        HttpRequestUtils.request(this, requestHotSearch, this, false);
    }

    /**
     * 为自己的问题询问答案
     */
    private void requestSelf(FakeAIArrayBean bean, String str) {
        handler.sendEmptyMessage(0);
        if (bean != null) {
            info.destinationId = String.valueOf(bean.destinationId);
            info.destinationType = String.valueOf(bean.destinationType);
            info.destinationName = bean.destinationName;
            info.guideCount = String.valueOf(bean.guideCount);
        }
        if (!TextUtils.isEmpty(str)) {
            info.userWant = str;
        }

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
}
