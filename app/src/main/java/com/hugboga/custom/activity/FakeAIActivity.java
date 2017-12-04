package com.hugboga.custom.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FakeAIAdapter;
import com.hugboga.custom.data.bean.FakeAIBean;
import com.hugboga.custom.data.request.RaqustFakeAI;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;

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
    private  FakeAIAdapter fakeAIAdapter;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            fakeData();
        }
    };
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
    public  void  fakeData(){
        FakeAIBean fakeAIBean =  new FakeAIBean();
        fakeAIAdapter.setData_All(fakeAIBean);
        editTextExist();
        ArrayList list = new ArrayList<>();
        list.add("东京");
        list.add("悉尼");
        list.add("纽约");
        list.add("伦敦");
        list.add("海南");
        list.add("土耳其");
        addScrollViewItem(list);
    }
    private void addClientData(String data) {

        fakeAIAdapter.setData_ItemTwo(data);

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
            final FakeAIBean dataList = (FakeAIBean) request.getData();
            if (dataList != null) {
                Toast.makeText(FakeAIActivity.this,dataList.toString(),Toast.LENGTH_SHORT);



            } else {

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

    private void addScrollViewItem(final ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final Button button = new Button(this);
            layoutParams.leftMargin = 13;
            layoutParams.rightMargin = 13;
            button.setLayoutParams(layoutParams);
            button.setGravity(Gravity.CENTER);
            button.setText(list.get(i).toString());
            button.setTextSize(12);
            button.setTextColor(getResources().getColor(R.color.contacts_letters_color));
            button.setBackground( getResources().getDrawable(R.drawable.fake_ai_scrollbutton));
            scrollViewLinearLayout.addView(button);
            button.setTag(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scrollViewButtonClick((Integer) button.getTag(), list);
                }
            });
        }

    }

    private void scrollViewButtonClick(int position, ArrayList list) {
       fakeAIAdapter.setData_ItemTwo((String) list.get(position));
       editTextOver();
    }
    private  void editTextOver(){
        horizontalScrollView.setVisibility(View.INVISIBLE);
        editText.setFocusable(false);
        editText.setText("");
        editText.setBackground(getResources().getDrawable(R.drawable.shape_rounded_ai_edit_over));
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            collapseSoftInputMethod(editText)


       ;
    }
    private  void editTextExist (){
        editText.setFocusable(true);
        editText.setBackground(getResources().getDrawable(R.drawable.shape_rounded_ai_edit));
    }

    private void requestHotSearch() {
        RaqustFakeAI requestHotSearch = new RaqustFakeAI(this);
        HttpRequestUtils.request(this, requestHotSearch, this, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
