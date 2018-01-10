package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.QueryCityActivity;
import com.hugboga.custom.activity.SearchDestinationGuideLineActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SearchLineBean;
import com.hugboga.custom.utils.SearchUtils;
import com.hugboga.custom.utils.Tools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/21.
 */

public class LineSearchListItem extends LinearLayout implements HbcViewBehavior {
    @BindView(R.id.title_line_search)
    TextView title;
    @BindView(R.id.poi_des)
    TextView poiDes;
    @BindView(R.id.tagNameDes)
    TextView tagNameDes; //标签描述语
    @BindView(R.id.search_line_location)
    TextView location;
    @BindView(R.id.pic_line)
    ImageView picLine;
    @BindView(R.id.price_per)
    TextView pricePer;
    Context context;
    Activity activity;
    SearchLineBean.GoodsPublishStatusVo goodsPublishStatusVo;
    String keyword;

    public LineSearchListItem(Context context) {
        this(context, null);
        this.context = context;
    }

    public LineSearchListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.line_search_item, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {

        if (_data != null && _data instanceof SearchLineBean.GoodsPublishStatusVo) {
            goodsPublishStatusVo = (SearchLineBean.GoodsPublishStatusVo) _data;
            if (keyword != null) {
                goodsPublishStatusVo.keyword = keyword;
            }
        }
        final String allLocation = goodsPublishStatusVo.depCityName + "-" + goodsPublishStatusVo.depPlaceName;
        if (goodsPublishStatusVo.keyword != null && goodsPublishStatusVo.keyword.toString().length() > 0) {
            SpannableString temptitle = SearchUtils.matcherSearchText(context, getResources().getColor(R.color.all_bg_yellow), goodsPublishStatusVo.name, goodsPublishStatusVo.keyword);
            SpannableString templocation = SearchUtils.matcherSearchText(context, getResources().getColor(R.color.all_bg_yellow), allLocation, goodsPublishStatusVo.keyword);

            title.setText(temptitle);
            location.setText(templocation);
        } else {
            title.setText(goodsPublishStatusVo.name);
            location.setText(allLocation);
        }
        resetTagName(); //标签描述语
        pricePer.setText("¥" + (int) goodsPublishStatusVo.prePrice + "起/人");
        Tools.showImage(picLine, goodsPublishStatusVo.pics, R.mipmap.line_search_default);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((activity instanceof SearchDestinationGuideLineActivity)
                        || (activity instanceof QueryCityActivity)) {
                    Intent intent = new Intent(activity, SkuDetailActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, goodsPublishStatusVo.goodsDetailUrl);
                    intent.putExtra(Constants.PARAMS_ID, goodsPublishStatusVo.no);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    intent.putExtra("isFromHome", true);
                    activity.startActivity(intent);
                }
            }
        });
        if (goodsPublishStatusVo.poiNames != null && goodsPublishStatusVo.poiNames.size() > 0) {
            poiDes.setVisibility(VISIBLE);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("包含“");
            for (int i = 0; i < goodsPublishStatusVo.poiNames.size(); i++) {
                stringBuilder.append(goodsPublishStatusVo.poiNames.get(i)).append("”地点、“");
            }
            CharSequence charSequence = stringBuilder.subSequence(0, stringBuilder.length() - 2);
            SpannableString displayPoi = SearchUtils.matcherSearchText(context, getResources().getColor(R.color.all_bg_yellow), charSequence.toString(), goodsPublishStatusVo.keyword);
            poiDes.setText(displayPoi);
        } else {
            poiDes.setVisibility(GONE);
        }
    }

    public void setkeyWord(String keyword) {
        this.keyword = keyword;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getEventSource() {
        return "搜索";
    }

    /**
     * 显示标签提示语
     */
    private void resetTagName() {
        if (goodsPublishStatusVo.tagNames != null && goodsPublishStatusVo.tagNames.size() > 0) {
            tagNameDes.setVisibility(View.VISIBLE);
            tagNameDes.setText(getStrOfKeyword(getStrTagName(goodsPublishStatusVo.tagNames)));
        } else {
            tagNameDes.setVisibility(View.GONE);
        }
    }

    /**
     * 组织标签显示文字
     *
     * @param tagNames
     * @return
     */
    private String getStrTagName(List<String> tagNames) {
        StringBuilder sb = new StringBuilder();
        for (String tagName : tagNames) {
            if(sb.length()>0){
                sb.append("、");
            }
            sb.append("");
            sb.append("\"");
            sb.append(tagName);
            sb.append("\"");
            sb.append("游玩线路");
        }
        return "属于" + sb.toString();
    }

    /**
     * 对关键字进行加工处理
     *
     * @param str
     * @return
     */
    private SpannableString getStrOfKeyword(String str) {
        SpannableString ss = new SpannableString(str);
        if (goodsPublishStatusVo.keyword != null && goodsPublishStatusVo.keyword.toString().length() > 0) {
            ss = SearchUtils.matcherSearchText(context, getResources().getColor(R.color.all_bg_yellow), str, goodsPublishStatusVo.keyword);
        }
        return ss;
    }
}
