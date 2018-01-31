package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.SearchGroupBean;
import com.hugboga.custom.data.bean.SearchGuideBean;
import com.hugboga.custom.data.bean.SearchLineBean;
import com.hugboga.custom.data.request.RequestSearchGuide;
import com.hugboga.custom.data.request.RequestSearchLine;
import com.hugboga.custom.models.EmptyDataModel;
import com.hugboga.custom.models.GuideItemModel;
import com.hugboga.custom.models.GuideLineItemHeaderModel;
import com.hugboga.custom.models.LineItemModel;
import com.hugboga.custom.models.LoadingModel;
import com.hugboga.custom.models.SearchDestinationModel;
import com.hugboga.custom.models.SearchMoreModel;
import com.hugboga.custom.utils.SearchUtils;
import com.hugboga.custom.widget.search.SearchHistoryView;

import java.util.ArrayList;
import java.util.List;

import static com.hugboga.custom.widget.search.SearchHistoryView.queryGuideRun;
import static com.hugboga.custom.widget.search.SearchHistoryView.queryLineRun;

/**
 * Created by zhangqiang on 17/8/23.
 */

public class SearchAfterAdapter extends EpoxyAdapter implements HttpRequestListener {
    SearchDestinationModel searchDestinationModel;
    SearchMoreModel searchMoreModel;
    GuideLineItemHeaderModel guideLineItemHeaderModel;
    GuideItemModel guideItemModel;
    LineItemModel lineItemModel;
    LoadingModel loadingModel;
    LoadingModel displayModel;
    EmptyDataModel emptyDataModel;
    Context context;
    String keyword;
    List<SearchDestinationModel> searchDestinationModels = new ArrayList<>();
    List<SearchGroupBean> listDestination = null;
    List<SearchGuideBean.GuideSearchItemBean> resultBean = null;
    List<SearchLineBean.GoodsPublishStatusVo> goods = null;
    boolean hasListDestination;
    boolean hasResultBean;
    boolean hasGoods;

    public void addAfterSearchDestinationModel(Context context, List<SearchGroupBean> list, String keyword) {
        this.context = context;
        this.keyword = keyword;
        this.listDestination = list;
        if (getItemCount() > 0) {
            removeModels();
        }
        if (listDestination == null || listDestination.size() == 0) {
            listDestination = null;
            hasListDestination = false;
        } else if (listDestination != null && listDestination.size() > 0) {
            hasListDestination = true;
        }

        if (searchDestinationModels != null && searchDestinationModels.size() > 0) {
            searchDestinationModels.clear();
        }

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                searchDestinationModel = new SearchDestinationModel(context, list.get(i), keyword);
                searchDestinationModels.add(searchDestinationModel);
            }
            addModels(searchDestinationModels);
        }

        //只展示前三个数据
        if (searchDestinationModels.size() > 3) {
            for (int i = 3; i < searchDestinationModels.size(); i++) {
                hideModel(searchDestinationModels.get(i));
            }
        }
        if (list != null && list.size() > 3) {
            String searchMoreString = context.getResources().getString(R.string.home_search_moree);
            searchMoreModel = new SearchMoreModel(context, searchMoreString, list, keyword);
            addModel(searchMoreModel);
        }

        loadingModel = new LoadingModel(context.getResources().getString(R.string.home_search_loading));
        addModel(loadingModel);

        RequestSearchGuide requestSearchGuide = new RequestSearchGuide(context, keyword, 0, 3);
        HttpRequestUtils.request(context, requestSearchGuide, this, false);
        SearchHistoryView.queryGuideRun = true;
    }

    public void removeModels() {
        if (getItemCount() > 0) {
            removeAllModels();
        }
    }

    public void showAllData() {
        if (searchDestinationModels != null && searchDestinationModels.size() > 3) {
            for (int i = 3; i < searchDestinationModels.size(); i++) {
                showModel(searchDestinationModels.get(i));
            }
        }
        if (searchMoreModel != null) {
            hideModel(searchMoreModel);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestSearchLine) {
            SearchLineBean searchLineBean = (SearchLineBean) request.getData();

            if (loadingModel != null) {
                removeModel(loadingModel);
                loadingModel = null;
            }
            if (emptyDataModel != null) {
                removeModel(emptyDataModel);
                emptyDataModel = null;
            }
            if (searchLineBean == null || searchLineBean.goods == null || searchLineBean.count == 0 || searchLineBean.goods.size() == 0) {
                this.goods = null;
                hasGoods = false;
            } else {
                hasGoods = true;
            }

            if (hasListDestination && !hasGoods && !hasResultBean) {
                displayModel = new LoadingModel(context.getResources().getString(R.string.home_search_all_here));
                addModel(displayModel);
            }

            if (searchLineBean != null && searchLineBean.goods != null && searchLineBean.goods.size() > 0) {
                this.goods = searchLineBean.goods;
                GuideLineItemHeaderModel guideLineItemHeaderModel = new GuideLineItemHeaderModel(context, searchLineBean.count, context.getResources().getString(R.string.search_line_title), keyword);
                addModel(guideLineItemHeaderModel);
                for (int i = 0; i < searchLineBean.goods.size(); i++) {
                    LineItemModel lineItemModel = new LineItemModel(context, searchLineBean.goods.get(i), keyword);
                    addModel(lineItemModel);
                }

            }
            if (listDestination == null && goods == null && resultBean == null) {
                emptyDataModel = new EmptyDataModel();
                addModel(emptyDataModel);
            }

            //搜索结果埋点
            if (hasListDestination || hasGoods || hasResultBean) {
                SearchUtils.setSensorsShareEvent(keyword, SearchUtils.isHistory, SearchUtils.isRecommend, true);
            } else {
                SearchUtils.setSensorsShareEvent(keyword, SearchUtils.isHistory, SearchUtils.isRecommend, false);
            }
            queryLineRun = false;
        } else if (request instanceof RequestSearchGuide) {
            SearchGuideBean searchGuideBean = (SearchGuideBean) request.getData();

            if (searchGuideBean == null || searchGuideBean.resultBean == null || searchGuideBean.totalSize == 0 || searchGuideBean.resultBean.size() == 0) {
                this.resultBean = null;
                hasResultBean = false;
            } else {
                hasResultBean = true;
            }
            if (searchGuideBean != null && searchGuideBean.resultBean != null && searchGuideBean.resultBean.size() > 0) {
                this.resultBean = searchGuideBean.resultBean;
                GuideLineItemHeaderModel guideLineItemHeaderModel = new GuideLineItemHeaderModel(context, searchGuideBean.totalSize, context.getResources().getString(R.string.search_guide_title), keyword);
                addModel(guideLineItemHeaderModel);
                for (int i = 0; i < searchGuideBean.resultBean.size(); i++) {
                    GuideItemModel guideItemModel = new GuideItemModel(context, searchGuideBean.resultBean.get(i), keyword);
                    addModel(guideItemModel);
                }

            }
            queryGuideRun = false;
            RequestSearchLine requestSearchLine = new RequestSearchLine(context, keyword, 0, 3);
            HttpRequestUtils.request(context, requestSearchLine, this, false);
            queryLineRun = true;
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (loadingModel != null) {
            removeModel(loadingModel);
        }
        if (emptyDataModel != null) {
            removeModel(emptyDataModel);
        }
        emptyDataModel = new EmptyDataModel();
        addModel(emptyDataModel);
    }
}
