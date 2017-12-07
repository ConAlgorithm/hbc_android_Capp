package com.hugboga.custom.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FakeAIAdapter;
import com.hugboga.custom.data.bean.ai.DuoDuoSaid;
import com.hugboga.custom.data.bean.ai.FakeAIArrayBean;
import com.hugboga.custom.data.bean.ai.FakeAIBean;
import com.hugboga.custom.data.bean.ai.FakeAIQuestionsBean;
import com.hugboga.custom.data.bean.ai.AiRequestInfo;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.request.RaqustFakeAI;
import com.hugboga.custom.data.request.RequsetFakeAIChange;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.UnicornUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.ai.AiTagView;
import com.jakewharton.disklrucache.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hugboga.custom.activity.AiResultActivity.KEY_GOODS;

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
    private int i = 1;

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
                break;
            case R.id.button:
                Intent intent = null;
                switch (buttonType) {
                    case 1://跳转客服对话
                        UnicornUtils.openServiceActivity(FakeAIActivity.this, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null);
                        break;
                    case 2://跳转填单页
                        intent = new Intent(FakeAIActivity.this, TravelPurposeFormActivity.class);
                        if (info.userSaidList != null && info.userSaidList.size() >= 2) {
                            intent.putExtra("cityName", info.userSaidList.get(0).saidContent);
                        }
                        startActivity(intent);
                        break;
                }
                finish();
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        boolean messageConsumption = false;//判断数据是否被消费掉
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
//            data.goodsList = testData(); //TODO remove
            if (data.goodsList != null && !messageConsumption) {
                //有推荐结果
                Intent intent = null;
                messageConsumption = true;
                if (data.goodsList.destinationGoodsList != null && data.goodsList.destinationGoodsList.size() > 0) {
                    intent = new Intent(this, AiResultActivity.class);
                    intent.putParcelableArrayListExtra(KEY_GOODS, (ArrayList<? extends Parcelable>) data.goodsList.destinationGoodsList);
                }else{

                }
                startActivity(intent);
                finish();
            } else {
                if (data.customServiceStatus != null && !messageConsumption) {
                    messageConsumption = true;
                    skipDialogue(data.customServiceStatus);

                }
                if (data.durationReqList != null && data.durationReqList.size() != 0 && !messageConsumption) {
                    messageConsumption = true;
                    fakeData(data.durationReqList);
                }
                if (data.accompanyReqList != null && data.accompanyReqList.size() != 0 && !messageConsumption) {
                    messageConsumption = true;
                    fakeData(data.accompanyReqList);
                }
                if (data.hotDestinationReqList != null && data.hotDestinationReqList.size() != 0 && !messageConsumption) {
                    messageConsumption = true;
                    fakeData(data.hotDestinationReqList);
                }
                if (data.userSaidList != null)
                    info.userSaidList = data.userSaidList;

                if (data.chooseDestinationId != null) {
                    info.customServiceId = data.chooseDestinationId;
                }
                if (data.chooseDestinationType != null) {
                    info.chooseDestinationType = data.chooseDestinationType;
                }
                initServiceMessage(data.duoDuoSaid);


            }
        }
    }

    private List<DestinationGoodsVo> testData() {
        String str = "[\n" +
                "            {\n" +
                "                \"arrCityId\":0,\n" +
                "                \"dayCount\":15,\n" +
                "                \"depCityId\":218,\n" +
                "                \"depCityName\":\"大阪\",\n" +
                "                \"goodsImageUrl\":\"https://hbcdn-dev.huangbaoche.com/fr-hd-dev/EqanTa3jjg0!m\",\n" +
                "                \"goodsName\":\"测试添加15天商品\",\n" +
                "                \"goodsNo\":\"IC9171090001\",\n" +
                "                \"goodsVersion\":4,\n" +
                "                \"guideCount\":15,\n" +
                "                \"guideHeadImageUrl\":\"https://hbcdn-dev.huangbaoche.com/guide/20150429033033.jpg\",\n" +
                "                \"perPrice\":\"999.0\",\n" +
                "                \"placeList\":\"\",\n" +
                "                \"shareUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=IC9171090001\",\n" +
                "                \"skuDetailUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=IC9171090001\",\n" +
                "                \"userFavorCount\":0\n" +
                "            },\n" +
                "            {\n" +
                "                \"arrCityId\":0,\n" +
                "                \"dayCount\":1,\n" +
                "                \"depCityId\":217,\n" +
                "                \"depCityName\":\"东京\",\n" +
                "                \"goodsImageUrl\":\"https://hbcdn-dev.huangbaoche.com/fr-hd-dev/ISyIlyPxhg0!m\",\n" +
                "                \"goodsName\":\"sd \",\n" +
                "                \"goodsNo\":\"IC9172370001\",\n" +
                "                \"goodsVersion\":9,\n" +
                "                \"guideCount\":1,\n" +
                "                \"perPrice\":\"57.0\",\n" +
                "                \"placeList\":\"\",\n" +
                "                \"shareUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=IC9172370001\",\n" +
                "                \"skuDetailUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=IC9172370001\",\n" +
                "                \"userFavorCount\":0\n" +
                "            },\n" +
                "            {\n" +
                "                \"arrCityId\":0,\n" +
                "                \"dayCount\":13,\n" +
                "                \"depCityId\":218,\n" +
                "                \"depCityName\":\"大阪\",\n" +
                "                \"goodsImageUrl\":\"https://hbcdn-dev.huangbaoche.com/fr-hd-dev/DFXRdITh1Q0!m\",\n" +
                "                \"goodsName\":\"(20170421-10:58复制)测试推荐15天\",\n" +
                "                \"goodsNo\":\"LT9171110002\",\n" +
                "                \"goodsVersion\":4,\n" +
                "                \"guideCount\":13,\n" +
                "                \"perPrice\":\"16854.0\",\n" +
                "                \"placeList\":\"\",\n" +
                "                \"shareUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=LT9171110002\",\n" +
                "                \"skuDetailUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=LT9171110002\",\n" +
                "                \"userFavorCount\":0\n" +
                "            },\n" +
                "            {\n" +
                "                \"arrCityId\":0,\n" +
                "                \"dayCount\":1,\n" +
                "                \"depCityId\":217,\n" +
                "                \"depCityName\":\"东京\",\n" +
                "                \"goodsImageUrl\":\"https://hbcdn-dev.huangbaoche.com/default/20160425/201604251612025590.png!m\",\n" +
                "                \"goodsName\":\"【东京后花园】轻井泽奇幻石之教堂 银座街 血拼奥特莱斯一日游  中文兼车导\",\n" +
                "                \"goodsNo\":\"QU1160140012\",\n" +
                "                \"goodsVersion\":4,\n" +
                "                \"guideCount\":1,\n" +
                "                \"perPrice\":\"null\",\n" +
                "                \"placeList\":\"神户->京都->福冈->福山->新泻->高松->山梨县->下吕->金泽->福井->宜野湾->浦添->南城->丰见城->大分->Flexstay Inn 常盘台->Hotel MyStays 羽田->The b 赤坂酒店->阿帕新桥虎之门酒店-\",\n" +
                "                \"shareUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=QU1160140012\",\n" +
                "                \"skuDetailUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=QU1160140012\",\n" +
                "                \"userFavorCount\":0\n" +
                "            },\n" +
                "            {\n" +
                "                \"arrCityId\":0,\n" +
                "                \"dayCount\":4,\n" +
                "                \"depCityId\":217,\n" +
                "                \"depCityName\":\"东京\",\n" +
                "                \"goodsImageUrl\":\"https://hbcdn-dev.huangbaoche.com/default/20161115/201611151536197821.jpg!m\",\n" +
                "                \"goodsName\":\"【冬季必选 私享温泉】箱根温泉 芦之湖 御殿场奥特莱斯一日游 东京专车接送 中文司兼导\",\n" +
                "                \"goodsNo\":\"ST1160140002\",\n" +
                "                \"goodsVersion\":11,\n" +
                "                \"guideCount\":4,\n" +
                "                \"perPrice\":\"138.0\",\n" +
                "                \"placeList\":\"箱根\",\n" +
                "                \"shareUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=ST1160140002\",\n" +
                "                \"skuDetailUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=ST1160140002\",\n" +
                "                \"userFavorCount\":0\n" +
                "            },\n" +
                "            {\n" +
                "                \"arrCityId\":0,\n" +
                "                \"dayCount\":3,\n" +
                "                \"depCityId\":1,\n" +
                "                \"depCityName\":\"悉尼\",\n" +
                "                \"goodsImageUrl\":\"https://hbcdn-dev.huangbaoche.com/default/20160524/201605241720129989.jpg!m\",\n" +
                "                \"goodsName\":\"固定线路_0523_背包客旅行_副本2\",\n" +
                "                \"goodsNo\":\"ST9161440005\",\n" +
                "                \"goodsVersion\":16,\n" +
                "                \"guideCount\":3,\n" +
                "                \"perPrice\":\"4075.0\",\n" +
                "                \"placeList\":\"\",\n" +
                "                \"shareUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=ST9161440005\",\n" +
                "                \"skuDetailUrl\":\"https://m-dev.huangbaoche.com/app/detail.html?t=1512409200685&goodsNo=ST9161440005\",\n" +
                "                \"userFavorCount\":0\n" +
                "            }\n" +
                "        ]";
        return new Gson().fromJson(str, new TypeToken<ArrayList<DestinationGoodsVo>>() {
        }.getType());
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
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    for (DuoDuoSaid bean : duoDuoSaid) {
                        fakeAIAdapter.addServerMessage(bean.questionValue);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

        }
        recyclerViewRoll();
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
            scrollViewLinearLayout.addView(view);
        }
        editTextExist();
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
            requestSelf(null, bean.destinationName);
        }
    }

    /**
     * 禁止输入
     */
    private void editTextOver() {
        scrollViewLinearLayout.removeAllViews();
        horizontalScrollView.setVisibility(View.INVISIBLE);
        editText.setText("");
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
            buttonContent = "和旅行小管家继续沟通";
        else
            buttonContent = "留下意向，让小管家明天联系我";
        buttonType = Integer.parseInt(str);
        button.setText(buttonContent);
        editText.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
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
        recyclerViewRoll();
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

    private void recyclerViewRoll() {
        recyclerView.scrollToPosition(recyclerView.getChildCount()-1);
    }
}
