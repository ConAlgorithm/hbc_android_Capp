package com.hugboga.custom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FakeAIAdapter;
import com.hugboga.custom.data.bean.ai.AiRequestInfo;
import com.hugboga.custom.data.bean.ai.DuoDuoSaid;
import com.hugboga.custom.data.bean.ai.FakeAIArrayBean;
import com.hugboga.custom.data.bean.ai.FakeAIBean;
import com.hugboga.custom.data.bean.ai.FakeAIQuestionsBean;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.request.RaqustFakeAI;
import com.hugboga.custom.data.request.RequsetFakeAIChange;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.ai.AiTagView;

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
            data.goodsList = testData(); //TODO remove
            if (data.goodsList != null) {
                //有推荐结果
                Intent intent = new Intent(this, AiResultActivity.class);
                intent.putParcelableArrayListExtra(KEY_GOODS, (ArrayList<? extends Parcelable>) data.goodsList);
                startActivity(intent);
            } else {
//            fakeData(data.durationReqList);
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
