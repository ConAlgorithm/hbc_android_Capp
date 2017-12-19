package com.hugboga.custom.models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.data.bean.SearchGroupBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/23.
 */

public class SearchMoreModel extends EpoxyModelWithHolder<SearchMoreModel.SearchMoreHolder> {

    String searchMoreString;
    SearchMoreHolder searchMoreHolder;
    List<SearchGroupBean> list;
    Context context;
    String keyword;

    public SearchMoreModel(Context context, String searchMoreString, List<SearchGroupBean> list, String keyword) {
        this.searchMoreString = searchMoreString;
        this.context = context;
        this.list = list;
        this.keyword = keyword;
    }

    @Override
    protected SearchMoreHolder createNewHolder() {
        return new SearchMoreHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.search_destination_footer_layout;
    }

    @Override
    public void bind(SearchMoreHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        searchMoreHolder = holder;
        init();
    }

    static class SearchMoreHolder extends EpoxyHolder {
        View itemView;
        @BindView(R.id.text)
        TextView textView;
        @BindView(R.id.img)
        ImageView imageView;

        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    private void init() {
        if (searchMoreHolder != null) {
            searchMoreHolder.textView.setText(searchMoreString);
            if (searchMoreString.startsWith(context.getResources().getString(R.string.home_search_more_about))) {
                searchMoreHolder.imageView.setImageResource(R.mipmap.icon_more);
                searchMoreHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (context instanceof ChooseCityNewActivity) {
                            ((ChooseCityNewActivity) context).searchAllResult(keyword.trim());
                        }
                    }
                });
            } else if (searchMoreString.startsWith(context.getResources().getString(R.string.home_search_moree))) {
                searchMoreHolder.imageView.setImageResource(R.mipmap.search_pull_down);
                searchMoreHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (context instanceof ChooseCityNewActivity) {
                            ((ChooseCityNewActivity) context).showMoreSearchDestination();
                        }
                    }
                });
            }
        }
    }
}
