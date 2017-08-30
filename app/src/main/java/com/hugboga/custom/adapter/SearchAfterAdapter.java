package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17/8/23.
 */

public class SearchAfterAdapter extends EpoxyAdapter implements HttpRequestListener{
    SearchDestinationModel searchDestinationModel;
    SearchMoreModel searchMoreModel;
    GuideLineItemHeaderModel guideLineItemHeaderModel;
    GuideItemModel guideItemModel;
    LineItemModel lineItemModel;
    LoadingModel loadingModel;
    EmptyDataModel emptyDataModel;
    Context context;
    String keyword;
    List<SearchDestinationModel> searchDestinationModels = new ArrayList<>();
    List<SearchGroupBean> listDestination = null;
    List<SearchGuideBean.GuideSearchItemBean> resultBean = null;
    List<SearchLineBean.GoodsPublishStatusVo>  goods = null;
    int typeNo = 2;
    public void addAfterSearchDestinationModel(Context context, List<SearchGroupBean> list, String keyword) {
        this.context = context;
        this.keyword = keyword;
        this.listDestination = list;
        typeNo =2;
        if(getItemCount() >0){
            removeModels();
        }
        if(listDestination == null || listDestination.size() == 0){
            listDestination = null;
        }
        if(searchDestinationModels!= null && searchDestinationModels.size()>0){
            searchDestinationModels.clear();
        }

        if(list!= null && list.size() >0){
            for(int i = 0; i<list.size() ; i++){
                searchDestinationModel = new SearchDestinationModel(context,list.get(i),keyword);
                searchDestinationModels.add(searchDestinationModel);
                //addModel(searchDestinationModel);
            }
            addModels(searchDestinationModels);
        }

        //只展示前三个数据
        if(searchDestinationModels.size() > 3){
            for(int i=3;i<searchDestinationModels.size();i++){
                hideModel(searchDestinationModels.get(i));
            }
        }
        if(list.size()>3){
            String searchMoreString = "展开更多";
            searchMoreModel = new SearchMoreModel(context,searchMoreString,list,keyword);
            addModel(searchMoreModel);
        }

        //暂时不展示
        loadingModel = new LoadingModel();
        addModel(loadingModel);

        RequestSearchGuide requestSearchGuide = new RequestSearchGuide(context,keyword,0,3);
        HttpRequestUtils.request(context,requestSearchGuide,this,false);
    }

    public void removeModels(){
        if(getItemCount() > 0){
            removeAllModels();
        }
    }

    public void showAllData(){
        if(searchDestinationModels!= null && searchDestinationModels.size() > 3){
            for(int i=3;i<searchDestinationModels.size();i++){
                showModel(searchDestinationModels.get(i));
            }
        }
        if(searchMoreModel!= null){
            hideModel(searchMoreModel);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestSearchLine) {
            SearchLineBean searchLineBean = (SearchLineBean) request.getData();
            if(loadingModel != null){
                removeModel(loadingModel);
            }
//            if(searchLineBean!= null && searchLineBean.goods!= null && searchLineBean.goods.size() == 0){
//                typeNo--;
//            }
//            if(typeNo == 0){
//
//               emptyDataModel = new EmptyDataModel();
//                addModel(emptyDataModel);
//            }
            if(searchLineBean == null || searchLineBean.goods == null || searchLineBean.count == 0 || searchLineBean.goods.size() == 0){
                this.goods = null;
            }
            if(searchLineBean!= null && searchLineBean.goods!= null && searchLineBean.goods.size() >0){
                this.goods = searchLineBean.goods;
                GuideLineItemHeaderModel guideLineItemHeaderModel = new GuideLineItemHeaderModel(context,searchLineBean.count,"相关线路",keyword);
                addModel(guideLineItemHeaderModel);
                for(int i=0;i<searchLineBean.goods.size();i++) {
                    LineItemModel lineItemModel = new LineItemModel(context,searchLineBean.goods.get(i),keyword);
                    addModel(lineItemModel);
                }

            }
            if(listDestination== null && goods == null && resultBean == null){
                emptyDataModel = new EmptyDataModel();
                addModel(emptyDataModel);
            }
        }else if(request instanceof RequestSearchGuide){
            SearchGuideBean searchGuideBean = (SearchGuideBean) request.getData();
            if(loadingModel != null){
                removeModel(loadingModel);
            }
//            if(typeNo == 1){
//                emptyDataModel = new EmptyDataModel();
//                addModel(emptyDataModel);
//            }
            if(searchGuideBean == null || searchGuideBean.resultBean== null ||searchGuideBean.totalSize == 0 || searchGuideBean.resultBean.size() == 0){
                this.resultBean = null;
            }
            if(searchGuideBean!= null && searchGuideBean.resultBean!= null && searchGuideBean.resultBean.size() >0){
                this.resultBean = searchGuideBean.resultBean;
                GuideLineItemHeaderModel guideLineItemHeaderModel = new GuideLineItemHeaderModel(context,searchGuideBean.totalSize,"相关司导",keyword);
                addModel(guideLineItemHeaderModel);
                for(int i=0;i<searchGuideBean.resultBean.size();i++) {
                    GuideItemModel guideItemModel = new GuideItemModel(context,searchGuideBean.resultBean.get(i),keyword);
                    addModel(guideItemModel);
                }

            }
            RequestSearchLine requestSearchLine = new RequestSearchLine(context,keyword,0,3);
            HttpRequestUtils.request(context,requestSearchLine,this,false);
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if(loadingModel != null){
            removeModel(loadingModel);
        }
        typeNo = 2;
    }
}
