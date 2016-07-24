package com.hugboga.custom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityBean;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by qingcha on 16/7/23.
 */
public class ChooseCityAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<CityBean> cityList = new ArrayList<CityBean>();
    private int showType = ShowType.DEFAULT;

    //用来区分UI展示类型
    public static final class ShowType {
        /**
         * 默认：（城市名）
         * */
        public static final int DEFAULT = 0x0001;

        /**
         * 搜索联想：搜索词高亮（城市名 国家）
         * */
        public static final int SEARCH_PROMPT = 0x0002;

        /**
         * 显示国家：（城市名 国家）
         * */
        public static final int SHOW_COUNTRY = 0x0003;

        /**
         * 挑选城市：（icon 城市名）
         * */
        public static final int SELECT_CITY = 0x0004;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public ChooseCityAdapter(Context conText) {
        this.mContext = conText;
        showType = ShowType.DEFAULT;
        inflater = LayoutInflater.from(mContext);
    }

    public void setData(List<CityBean> _list) {
        this.cityList = _list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (cityList == null) {
            return new View(mContext);
        }
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_choosecity, parent, false);
            holder.selectedIV = (ImageView) convertView.findViewById(R.id.item_choosecity_selected_iv);
            holder.titleTV = (TextView) convertView.findViewById(R.id.item_choosecity_title_tv);
            holder.subTitleTV = (TextView) convertView.findViewById(R.id.item_choosecity_subtitle_tv);
            holder.bottomLine = convertView.findViewById(R.id.item_choosecity_bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CityBean cityBean = cityList.get(position);
        if (cityBean == null) {//防止空指针
           return convertView;
        }

        CharSequence title = null;
        String subTitle = null;
        switch (showType) {
            case ShowType.DEFAULT:
                title = cityBean.name;
                break;
            case ShowType.SEARCH_PROMPT:
                title = getSearchPromptHighlightString(cityBean.name, cityBean.keyWord);
                subTitle = cityBean.placeName;
                break;
            case ShowType.SHOW_COUNTRY:
                title = cityBean.name;
                subTitle = cityBean.placeName;
                break;
        }
        holder.titleTV.setText(title);
        holder.subTitleTV.setText(subTitle);

        //判断底部的线是否隐藏
        if (showType == ShowType.SEARCH_PROMPT) {
            holder.bottomLine.setVisibility(View.VISIBLE);
        } else {
            if (position + 1 < cityList.size() && cityList.get(position + 1) != null
                    && !TextUtils.isEmpty(cityBean.firstLetter)
                    && cityBean.firstLetter.equals(cityList.get(position + 1).firstLetter)) {
                holder.bottomLine.setVisibility(View.VISIBLE);
            } else {
                holder.bottomLine.setVisibility(View.GONE);
            }
        }

        //挑选城市
        if (showType == ShowType.SELECT_CITY) {
            if (cityBean.isSelected) {
                holder.selectedIV.setVisibility(View.VISIBLE);
            } else {
                holder.selectedIV.setVisibility(View.GONE);
            }
            if (cityBean.isNationality) {
                convertView.setClickable(false);
            } else {
                convertView.setClickable(true);
            }
        }
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.item_choosecity_sections, parent, false);
            holder.sectionsTV = (TextView) convertView.findViewById(R.id.item_choosecity_sections_tv);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        CityBean cityBean = cityList.get(position);
        holder.sectionsTV.setText(getSectionsStr(cityBean));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return getSectionsStr(cityList.get(position)).subSequence(0, 1).charAt(0);
    }

    private String getSectionsStr(CityBean cityBean) {
        String result = null;
        if (showType == ShowType.SEARCH_PROMPT) {
            result = mContext.getString(R.string.guess_you_want);
        } else {
            result = cityBean.firstLetter;
        }
        return result;
    }

    /**
     * 搜索词高亮
     * */
    private SpannableStringBuilder getSearchPromptHighlightString(String name, String keyWord) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(name);
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(Color.parseColor("#FDCE02"));
        int start = ssb.toString().indexOf(keyWord);
        int end = start + keyWord.length();
        ssb.setSpan(yellowSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    class ViewHolder {
        ImageView selectedIV;
        TextView titleTV;
        TextView subTitleTV;
        View bottomLine;
    }

    class HeaderViewHolder {
        TextView sectionsTV;
    }

}
