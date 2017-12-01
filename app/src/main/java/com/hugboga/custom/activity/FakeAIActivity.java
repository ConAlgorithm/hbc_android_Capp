package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FakeAIAdapter;
import com.hugboga.custom.adapter.viewholder.FakeAIViewHoder;
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
    @BindView(R.id.fake_image)
    ImageView fakeImage;
    @BindView(R.id.fake_text_create1)
    TextView fakeTextCreate1;
    @BindView(R.id.fake_text_create2)
    TextView fakeTextCreate2;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    private ArrayList<AdapterData> adapterData = new ArrayList<AdapterData>();
    private FakeAIAdapter myAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_fake_ai;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        adapterData.add(new AdapterData(1, "请问您想去要去那？"));
        initView();
        requestHotSearch();
    }

    private void initView() {
        fakeImage.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fakeai_hi));
        headerTitleCenter.setText("旅行小管家");
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        myAdapter = new FakeAIAdapter(this) {
            @Override
            public int itemCount() {
                return adapterData.size();
            }

            @Override
            public int getViewType(int position) {
                return getType(position);
            }

            @Override
            public void bindData(FakeAIViewHoder holder, int position) {
                switch (getType(position)) {
                    case FakeAIAdapter.FAKEADAPTERTYPE_ONE:

                        break;


                }

                holder.textView.setText(adapterData.get(position).getTv());

            }
        };

        recyclerView.setAdapter(myAdapter);
        //第一次点击输入EditText时调用
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //if (hasFocus)
                  //  horizontalScrollView.setVisibility(View.GONE);


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
                addClientData(editText.getText().toString());
                return true;
            }
        });
    }

    private void addClientData(String data) {
        adapterData.add(new AdapterData(0, data));
        editText.setText(null);
        myAdapter.notifyDataSetChanged();
        collapseSoftInputMethod(editText);
        recyclerView.scrollToPosition(adapterData.size() - 1);

    }

    public int getType(int position) {

        if (adapterData.get(position).getImageView() == 0)
            return FakeAIAdapter.FAKEADAPTERTYPE_TWO;
        else
            return FakeAIAdapter.FAKEADAPTERTYPE_ONE;
    }

    @OnClick({R.id.header_left_btn, R.id.edit_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.edit_text:
               // horizontalScrollView.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RaqustFakeAI) {
            final FakeAIBean dataList = (FakeAIBean)request.getData();
            if (dataList != null ) {

                MLog.i("MMM",dataList.toString());


            } else {
        horizontalScrollView.setVisibility(GONE);
            }
        }
    }
    private void addScrollViewItem(final ArrayList list){
        for (int i = 0; i < list.size(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            final Button button =new Button(this);
            layoutParams.leftMargin = 10;
            button.setLayoutParams(layoutParams);
            button.setGravity(Gravity.CENTER);
            //button.setBackground();
            horizontalScrollView.addView(button);
            button.setTag(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scrollViewButtonClick((Integer) button.getTag(),list);
                }
            });
        }

    }
    private void scrollViewButtonClick(int position,ArrayList list){

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


    class AdapterData {
        private int type;
        private String tv;

        public AdapterData(int type, String tv) {
            this.type = type;
            this.tv = tv;
        }

        public int getImageView() {
            return type;
        }

        public void setImageView(int imageView) {
            this.type = imageView;
        }

        public String getTv() {
            return tv;
        }

        public void setTv(String tv) {
            this.tv = tv;
        }
    }
}
