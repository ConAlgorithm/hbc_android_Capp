package com.hugboga.custom.adapter;

import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.SearchGroupBean;
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

public class SearchAfterAdapter extends EpoxyAdapter {
    SearchDestinationModel searchDestinationModel;
    SearchMoreModel searchMoreModel;
    GuideLineItemHeaderModel guideLineItemHeaderModel;
    GuideItemModel guideItemModel;
    LineItemModel lineItemModel;
    LoadingModel loadingModel;
    EmptyDataModel emptyDataModel;
    Context context;
    List<SearchDestinationModel> searchDestinationModels = new ArrayList<>();
    public void addAfterSearchDestinationModel(Context context, List<SearchGroupBean> list, String keyword) {
        this.context = context;
        if(getItemCount() >0){
            removeModels();
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

//        guideLineItemHeaderModel = new GuideLineItemHeaderModel();
//        addModel(guideLineItemHeaderModel);
//
//        guideItemModel = new GuideItemModel();
//        addModel(guideItemModel);
//        addModel(guideItemModel);
//        addModel(guideItemModel);
//
//        guideLineItemHeaderModel = new GuideLineItemHeaderModel();
//        addModel(guideLineItemHeaderModel);
//
//        lineItemModel = new LineItemModel();
//        addModel(lineItemModel);
//        addModel(lineItemModel);
//        addModel(lineItemModel);
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
}
